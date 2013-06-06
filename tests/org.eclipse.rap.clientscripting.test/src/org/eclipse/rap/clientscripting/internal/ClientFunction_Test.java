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

import static org.eclipse.rap.clientscripting.internal.TestUtil.fakeConnection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.junit.*;


public class ClientFunction_Test {

  private static final String TARGET_ID = "w101";

  private ClientFunction function;

  @Before
  public void setUp() throws Exception {
    Fixture.setUp();
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
  public void testAddTo_createsBinding() {
    function.addTo( TARGET_ID, SWT.MouseDown );

    assertNotNull( function.findBinding( TARGET_ID, SWT.MouseDown ) );
  }

  @Test
  public void testAddTo_ignoresSubsequentCalls() {
    function.addTo( TARGET_ID, SWT.KeyDown );
    function.addTo( TARGET_ID, SWT.KeyDown );

    assertEquals( 1, function.getBindings().size() );
  }

  @Test
  public void testRemoveFrom_disposesBinding() {
    function.addTo( TARGET_ID, SWT.MouseDown );

    function.removeFrom( TARGET_ID, SWT.MouseDown );

    ClientListenerBinding binding = function.findBinding( TARGET_ID, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_mayBeCalledTwice() {
    function.addTo( TARGET_ID, SWT.MouseDown );

    function.removeFrom( TARGET_ID, SWT.MouseDown );
    function.removeFrom( TARGET_ID, SWT.MouseDown );

    ClientListenerBinding binding = function.findBinding( TARGET_ID, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_ignoresNonExistingBinding() {
    function.removeFrom( TARGET_ID, SWT.MouseDown );

    assertNull( function.findBinding( TARGET_ID, SWT.MouseDown ) );
  }

  private void createListener() {
    function = spy( new ClientFunction( "code" ) );
  }

}
