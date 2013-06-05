/*******************************************************************************
 * Copyright (c) 2012, 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.clientscripting.internal;

import static org.eclipse.rap.clientscripting.TestUtil.fakeConnection;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;


public class ClientListenerBinding_Test {

  private Shell shell;
  private Display display;
  private Label label1;
  private Label label2;
  private ClientListener listener1;
  private ClientListener listener2;
  private ClientListenerBinding binding;
  private ClientListenerBinding equalBinding;
  private ClientListenerBinding bindingWithDifferentWidget;
  private ClientListenerBinding bindingWithDifferentEvent;
  private ClientListenerBinding bindingWithDifferentListener;

  @Before
  public void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createListeners();
    createBindingss();
  }

  @After
  public void tearDown() throws Exception {
    Fixture.tearDown();
  }

  @Test
  public void testEquals() {
    assertTrue( binding.equals( equalBinding ) );
    assertFalse( binding.equals( null ) );
    assertFalse( binding.equals( new Object() ) );
    assertFalse( binding.equals( bindingWithDifferentWidget ) );
    assertFalse( binding.equals( bindingWithDifferentEvent ) );
    assertFalse( binding.equals( bindingWithDifferentListener ) );
  }

  @Test
  public void testHashCode() {
    assertTrue( binding.hashCode() != 0 );
    assertTrue( binding.hashCode() == equalBinding.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentWidget.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentEvent.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentListener.hashCode() );
  }

  @Test
  public void testCreation() {
    assertSame( label1, binding.getWidget() );
    assertEquals( SWT.MouseDown, binding.getEventType() );
  }

  @Test
  public void testCreation_createsRemoteObject() {
    Connection connection = fakeConnection( mock( RemoteObject.class ) );

    binding = new ClientListenerBinding( listener1, label1, ClientListener.KeyDown );

    verify( connection ).createRemoteObject( eq( "rwt.clientscripting.EventBinding" ) );
  }

  @Test
  public void testCreation_initializesRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    binding = new ClientListenerBinding( listener1, label1, ClientListener.KeyDown );

    verify( remoteObject ).set( eq( "listener" ), eq( listener1.getRemoteId() ) );
    verify( remoteObject ).set( eq( "targetObject" ), eq( WidgetUtil.getId( label1 ) ) );
    verify( remoteObject ).set( eq( "eventType" ), eq( "KeyDown" ) );
  }

  @Test
  public void testDispose_destroysRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    binding = new ClientListenerBinding( listener1, label1, ClientListener.KeyDown );

    binding.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testDispose_mayBeCalledTwice() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    binding = new ClientListenerBinding( listener1, label1, ClientListener.KeyDown );

    binding.dispose();
    binding.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testIsDisposed_falseByDefault() {
    assertFalse( binding.isDisposed() );
  }

  @Test
  public void testIsDisposed_trueAfterDispose() {
    binding.dispose();

    assertTrue( binding.isDisposed() );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
    label1 = new Label( shell, SWT.NONE );
    label2 = new Label( shell, SWT.NONE );
  }

  private void createListeners() {
    listener1 = new ClientListener( "code" );
    listener2 = new ClientListener( "code" );
  }

  private void createBindingss() {
    binding = new ClientListenerBinding( listener1, label1, SWT.MouseDown );
    equalBinding = new ClientListenerBinding( listener1, label1, SWT.MouseDown );
    bindingWithDifferentWidget = new ClientListenerBinding( listener1, label2, SWT.MouseDown );
    bindingWithDifferentEvent = new ClientListenerBinding( listener1, label1, SWT.MouseUp );
    bindingWithDifferentListener = new ClientListenerBinding( listener2, label1, SWT.MouseDown );
  }

}
