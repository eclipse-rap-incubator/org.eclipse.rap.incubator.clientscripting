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
package org.eclipse.rap.clientscripting;

import static org.eclipse.rap.clientscripting.internal.TestUtil.findBinding;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.junit.*;


@SuppressWarnings( "deprecation" )
public class ClientListener_Test {

  private Shell shell;
  private Display display;
  private ClientListener listener;

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
  public void testAddListener_callsAddTo() {
    shell.addListener( SWT.MouseDown, listener );

    verify( listener ).addTo( shell, SWT.MouseDown );
  }

  @Test
  public void testAddListener_doesNotCrashWithNonClientFunction() {
    try {
      shell.addListener( SWT.MouseDown, spy( new Listener() {

        public void handleEvent( Event event ) {
        }

        @SuppressWarnings( "unused" )
        public void addTo() {
        }
      } ) );
    } catch( Exception e ) {
      fail();
    }
  }

  @Test
  public void testRemoveListener_callsRemoveFrom() {
    Label label = new Label( shell, SWT.NONE );
    label.addListener( SWT.MouseDown, listener );

    label.removeListener( SWT.MouseDown, listener );

    verify( listener ).removeFrom( label, SWT.MouseDown );
  }

  @Test
  public void testRemoveListener_doesNotCrashWithNonClientFunction() {
    try {
      shell.removeListener( SWT.MouseDown, spy( new Listener() {

        public void handleEvent( Event event ) {
        }

        @SuppressWarnings( "unused" )
        public void removeFrom() {
        }
      } ) );
    } catch( Exception e ) {
      fail();
    }
  }

  @Test
  public void testDispose_disposesBindings() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    label.dispose();

    ClientListenerBinding binding = findBinding( listener, label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testAddTwiceAndDispose() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseUp );
    listener.addTo( label, SWT.MouseUp );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );

    label.dispose();

    ClientListenerBinding binding = findBinding( listener, label, SWT.MouseUp );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testAddTo_createsBinding() {
    listener.addTo( shell, SWT.MouseDown );

    assertNotNull( findBinding( listener, shell, SWT.MouseDown ) );
  }

  @Test
  public void testAddTo_failsWithNullWidget() {
    try {
      listener.addTo( null, SWT.MouseDown );
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
      listener.addTo( label, SWT.MouseDown );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertEquals( "Widget is disposed", exception.getMessage() );
    }
  }

  @Test
  public void testRemoveFrom_failsWithNullWidget() {
    try {
      listener.removeFrom( null, SWT.MouseDown );
      fail();
    } catch( NullPointerException exception ) {
      assertEquals( "widget is null", exception.getMessage() );
    }
  }

  @Test
  public void testRemoveFrom_disposesBinding() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    ClientListenerBinding binding = findBinding( listener, label, SWT.MouseDown );

    listener.removeFrom( label, SWT.MouseDown );

    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_mayBeCalledTwice() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    ClientListenerBinding binding = findBinding( listener, label, SWT.MouseDown );

    listener.removeFrom( label, SWT.MouseDown );
    listener.removeFrom( label, SWT.MouseDown );

    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_ignoresNonExistingBinding() {
    Label label = new Label( shell, SWT.NONE );

    listener.removeFrom( label, SWT.MouseDown );

    assertNull( findBinding( listener, label, SWT.MouseDown ) );
  }


  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
  }

  private void createListener() {
    listener = spy( new ClientListener( new Script( "code" ) ) );
  }

}
