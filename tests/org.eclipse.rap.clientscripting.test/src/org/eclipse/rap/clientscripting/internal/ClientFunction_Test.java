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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.*;


public class ClientFunction_Test {

  private static final String CLIENT_LISTENER_TYPE =  "rwt.clientscripting.Listener";
  private static final String LISTENER_BINDING_TYPE =  "rwt.clientscripting.EventBinding";

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
    Connection connection = fakeConnection( mock( RemoteObject.class ), CLIENT_LISTENER_TYPE );

    new ClientFunction( "script code" );

    verify( connection ).createRemoteObject( "rwt.clientscripting.Listener" );
  }

  @Test
  public void testCreation_initializesRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject, CLIENT_LISTENER_TYPE );

    new ClientFunction( "script code" );

    verify( remoteObject ).set( eq( "code" ), eq( "script code" ) );
  }


  @Test
  public void testAddTo_createsBinding() {
    function.addTo( "w101", "MouseDown" );

    assertNotNull( function.findBinding( "w101", "MouseDown" ) );
  }

  @Test
  public void testAddTo_ignoresSubsequentCalls() {
    Connection connection = fakeConnection( mock( RemoteObject.class ), LISTENER_BINDING_TYPE );

    function.addTo( "w101", "KeyDown" );
    function.addTo( "w101", "KeyDown" );

    assertEquals( 1, function.getBindings().size() );
    verify( connection, times( 1 ) ).createRemoteObject( eq( LISTENER_BINDING_TYPE ) );
  }

  @Test
  public void testRemoveFrom_disposesBinding() {
    function.addTo( "w101", "MouseDown" );
    ClientListenerBinding binding = function.findBinding( "w101", "MouseDown" );

    function.removeFrom( "w101", "MouseDown" );

    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_RemovesBindingFromList() {
    function.addTo( "w101", "MouseDown" );

    function.removeFrom( "w101", "MouseDown" );

    assertNull( function.findBinding( "w101", "MouseDown" ) );
  }

  @Test
  public void testRemoveFrom_mayBeCalledTwice() {
    function.addTo( "w101", "MouseDown" );
    ClientListenerBinding binding = function.findBinding( "w101", "MouseDown" );

    function.removeFrom( "w101", "MouseDown" );
    function.removeFrom( "w101", "MouseDown" );

    assertTrue( binding.isDisposed() );
  }

  @Test
  public void testRemoveFrom_ignoresNonExistingBinding() {
    function.removeFrom( "w101", "MouseDown" );

    assertNull( function.findBinding( "w101", "MouseDown" ) );
  }

  private void createListener() {
    function = spy( new ClientFunction( "code" ) );
  }

}
