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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.junit.*;


public class ClientFunction_Test {

  private Shell shell;
  private Display display;
  private ClientFunction function;

  @Before
  public void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createListener();
  }

  @After
  public void tearDown() throws Exception {
    Fixture.tearDown();
  }

  @Test
  public void testCreation_failsWithNull() {
    try {
      new ClientFunction( null );
      fail();
    } catch( NullPointerException expected ) {
      assertTrue( expected.getMessage().contains( "scriptCode" ) );
    }
  }

  @Test
  public void testCreation_createsRemoteObject() {
    Connection connection = fakeConnection( mock( RemoteObject.class ) );

    new ClientFunction( "script code" );

    verify( connection ).createRemoteObject( "rwt.clientscripting.Listener" );
  }

  @Test
  public void testCreation_initializesRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ClientFunction( "script code" );

    verify( remoteObject ).set( eq( "code" ), eq( "script code" ) );
  }

  @Test
  public void testDispose_disposesBindings() {
    Label label = new Label( shell, SWT.NONE );
    function.addTo( label, SWT.MouseDown );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    label.dispose();

    ClientListenerBinding binding = function.findBinding( label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testAddTo_createsBinding() {
    function.addTo( shell, SWT.MouseDown );

    assertNotNull( function.findBinding( shell, SWT.MouseDown ) );
  }

  @Test
  public void testAddTo_ignoresSubsequentCalls() {
    function.addTo( shell, SWT.KeyDown );
    function.addTo( shell, SWT.KeyDown );

    assertEquals( 1, function.getBindings().size() );
  }

  @Test
  public void testAddTo_failsWithNullWidget() {
    try {
      function.addTo( null, SWT.MouseDown );
      fail();
    } catch( NullPointerException exception ) {
      assertEquals( "widget is null", exception.getMessage() );
    }
  }

  @Test
  public void testAddTo_failsWithDisposedWidget() {
    Label label = new Label( shell, SWT.NONE );
    label.dispose();

    try {
      function.addTo( label, SWT.MouseDown );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertEquals( "Widget is disposed", exception.getMessage() );
    }
  }

  @Test
  public void testRemoveFrom_failsWithNullWidget() {
    try {
      function.removeFrom( null, SWT.MouseDown );
      fail();
    } catch( NullPointerException exception ) {
      assertEquals( "widget is null", exception.getMessage() );
    }
  }

  @Test
  public void testRemoveFrom_disposesBinding() {
    Label label = new Label( shell, SWT.NONE );
    function.addTo( label, SWT.MouseDown );

    function.removeFrom( label, SWT.MouseDown );

    ClientListenerBinding binding = function.findBinding( label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_mayBeCalledTwice() {
    Label label = new Label( shell, SWT.NONE );
    function.addTo( label, SWT.MouseDown );

    function.removeFrom( label, SWT.MouseDown );
    function.removeFrom( label, SWT.MouseDown );

    ClientListenerBinding binding = function.findBinding( label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_ignoresNonExistingBinding() {
    Label label = new Label( shell, SWT.NONE );

    function.removeFrom( label, SWT.MouseDown );

    assertNull( function.findBinding( label, SWT.MouseDown ) );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
  }

  private void createListener() {
    function = spy( new ClientFunction( "code" ) );
  }

}
