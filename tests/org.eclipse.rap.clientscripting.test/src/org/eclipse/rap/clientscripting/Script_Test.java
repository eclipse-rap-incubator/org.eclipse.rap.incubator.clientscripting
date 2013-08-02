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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.*;


public class Script_Test {

  private static final String REMOTE_TYPE = "rwt.scripting.Script";
  private Connection connection;
  private RemoteObject remoteObject;

  @Before
  public void setUp() throws Exception {
    Fixture.setUp();
    connection = mock( Connection.class );
    remoteObject = mock( RemoteObject.class );
    when( remoteObject.getId() ).thenReturn( "foo" );
    when( connection.createRemoteObject( REMOTE_TYPE ) ).thenReturn( remoteObject );
    Fixture.fakeConnection( connection );
  }

  @After
  public void tearDown() throws Exception {
    Fixture.tearDown();
  }

  @Test( expected = NullPointerException.class )
  public void testConstructor_nullArgumenFails() {
    new Script( null );
  }

  @Test
  public void testConstructor_createsRemoteObject() {
    new Script( "1+1;" );

    verify( connection ).createRemoteObject( eq( REMOTE_TYPE ) );
  }

  @Test
  public void testConstructor_setsTextOnRemoteObject() {
    new Script( "1+1;" );

    verify( remoteObject ).set( eq( "text" ), eq( "1+1;" ) );
  }

  @Test
  public void testGetId() {
    Script script = new Script( "1+1;" );

    assertEquals( "foo", script.getId() );
  }

  @Test
  public void testDispose_callsDestroyOnRemoteObject() {
    Script script = new Script( "1+1;" );

    script.dispose();

    verify( remoteObject ).destroy();
  }

}
