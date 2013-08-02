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
package org.eclipse.rap.clientscripting.internal.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class ClientScriptingResources_Test {

  @Before
  public void setUp() throws Exception {
    Fixture.setUp();
  }

  @After
  public void tearDown() throws Exception {
    Fixture.tearDown();
  }

  @Test
  public void testRegistersWithResourceManager() {
    ResourceManager resourceManager = mock( ResourceManager.class );
    Fixture.fakeResourceManager( resourceManager );

    ClientScriptingResources.ensure();

    verify( resourceManager ).register( eq( "clientscripting.js" ), ( InputStream )notNull() );
  }

  @Test
  public void testRegistersConcatenatedResources() {
    final AtomicReference<String> registeredStringCaptor = new AtomicReference<String>();
    ResourceManager resourceManager = mock( ResourceManager.class );
    Fixture.fakeResourceManager( resourceManager );
    doAnswer( new Answer<Object>() {
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        InputStream inputStream = ( InputStream )invocation.getArguments()[1];
        registeredStringCaptor.set( read( inputStream, "UTF-8" ) );
        inputStream.close();
        return null;
      }
    } ).when( resourceManager ).register( anyString(), any( InputStream.class ) );

    ClientScriptingResources.ensure();

    String registeredCode = registeredStringCaptor.get();
    assertTrue( registeredCode.contains( "scripting.EventBinding =" ) );
    assertTrue( registeredCode.contains( "scripting.SWT =" ) );
    assertTrue( registeredCode.contains( "scripting.EventProxy =" ) );
  }

  static String read( InputStream inputStream, String charset ) throws IOException {
    InputStreamReader reader = new InputStreamReader( inputStream, charset );
    StringBuilder builder = new StringBuilder();
    char[] buffer = new char[ 4096 ];
    int read = reader.read( buffer );
    while( read != -1 ) {
      builder.append( buffer, 0, read );
      read = reader.read( buffer );
    }
    return builder.toString();
  }

  @Test
  public void testDoesNotRegisterWithResourceManagerTwice() {
    ResourceManager resourceManager = mock( ResourceManager.class );
    when( Boolean.valueOf( resourceManager.isRegistered( anyString() ) ) ).thenReturn( Boolean.TRUE );
    Fixture.fakeResourceManager( resourceManager );

    ClientScriptingResources.ensure();

    verify( resourceManager, times( 0 ) ).register( anyString(), any( InputStream.class ) );
  }

  @Test
  public void testLoadsJavaScript() {
    JavaScriptLoader loader = mock( JavaScriptLoader.class );
    Client client = mock( Client.class );
    when( client.getService( eq( JavaScriptLoader.class ) ) ).thenReturn( loader );
    Fixture.fakeClient( client );

    ClientScriptingResources.ensure();

    verify( loader ).require( endsWith( "clientscripting.js" ) );
  }

  @Test
  public void testRegistersWithApplication() throws IOException {
    Application application = mock( Application.class );

    ClientScriptingResources.register( application );

    ArgumentCaptor<String> resourceName = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<ResourceLoader> resourceLoader = ArgumentCaptor.forClass( ResourceLoader.class );
    verify( application ).addResource( resourceName.capture(), resourceLoader.capture() );

    assertNotNull( resourceLoader.getValue().getResourceAsStream( resourceName.getValue() ) );
  }

}
