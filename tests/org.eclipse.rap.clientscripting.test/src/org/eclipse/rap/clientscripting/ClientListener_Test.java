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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
  }

  private void createListener() {
    listener = spy( new ClientListener( "code" ) );
  }

}
