/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.clientscripting.internal.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.*;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.mockito.ArgumentCaptor;


public class ClientScriptingResources_Test extends TestCase {

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testRegistersWithResourceManager() {
    ResourceManager resourceManager = mock( ResourceManager.class );
    Fixture.fakeResourceManager( resourceManager );

    ClientScriptingResources.ensure();

    verify( resourceManager ).register( eq( "clientscripting.js" ), ( InputStream )notNull() );
  }

  public void testDoesNotRegisterWithResourceManagerTwice() {
    ResourceManager resourceManager = mock( ResourceManager.class );
    when( Boolean.valueOf( resourceManager.isRegistered( anyString() ) ) ).thenReturn( Boolean.TRUE );
    Fixture.fakeResourceManager( resourceManager );

    ClientScriptingResources.ensure();

    verify( resourceManager, times( 0 ) ).register( anyString(), any( InputStream.class ) );
  }

  public void testLoadsJavaScript() {
    JavaScriptLoader loader = mock( JavaScriptLoader.class );
    Client client = mock( Client.class );
    when( client.getService( eq( JavaScriptLoader.class ) ) ).thenReturn( loader );
    Fixture.fakeClient( client );

    ClientScriptingResources.ensure();

    verify( loader ).require( endsWith( "clientscripting.js" ) );
  }

  public void testRegistersWithApplication() throws IOException {
    Application application = mock( Application.class );

    ClientScriptingResources.register( application );

    ArgumentCaptor<String> resourceName = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<ResourceLoader> resourceLoader = ArgumentCaptor.forClass( ResourceLoader.class );
    verify( application ).addResource( resourceName.capture(), resourceLoader.capture() );

    assertNotNull( resourceLoader.getValue().getResourceAsStream( resourceName.getValue() ) );
  }

  public void testReadInputStream() throws IOException {
    InputStream inputStream = new ByteArrayInputStream( "line1\nline2\n".getBytes() );
    StringBuilder builder = new StringBuilder();

    ClientScriptingResources.read( inputStream, builder );

    assertEquals( "line1\nline2\n", builder.toString() );
  }

  public void testReadLongInputStream() throws IOException {
    String longString = createRandomString( 10000 ); // bigger than buffer size
    InputStream inputStream = new ByteArrayInputStream( longString.getBytes() );
    StringBuilder builder = new StringBuilder();

    ClientScriptingResources.read( inputStream, builder );

    assertEquals( longString, builder.toString() );
  }

  private static String createRandomString( int length ) {
    String chars = "abcdefghijklmnopqrstuvwxyz";
    StringBuilder builder = new StringBuilder( length );
    for( int i = 0; i < length; i++ ) {
      builder.append( chars.charAt( ( int )( Math.random() * chars.length() ) ) );
    }
    return builder.toString();
  }

}
