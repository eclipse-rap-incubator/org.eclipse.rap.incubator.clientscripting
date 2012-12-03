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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.resources.ResourceLoader;


public final class ClientScriptingResources {

  private static final String CHARSET = "UTF-8";
  private static final String REGISTER_NAME = "clientscripting.js";
  private static final String PREFIX = "org/eclipse/rap/clientscripting/";
  private static final String[] ALL_RESOURCES = {
    "ClientScriptingUtil.js",
    "EventBindingAdapter.js",
    "EventBinding.js",
    "EventProxy.js",
    "Function.js",
    "ListenerAdapter.js",
    "SWT.js",
    "WidgetProxy.js"
  };

  public static void ensure() {
    ensureRegistered();
    ensureLoaded();
  }

  private static void ensureRegistered() {
    if( !RWT.getResourceManager().isRegistered( REGISTER_NAME ) ) {
      try {
        register();
      } catch( IOException exception ) {
        throw new RuntimeException( "Failed to register resources", exception );
      }
    }
  }

  private static void ensureLoaded() {
    JavaScriptLoader loader = RWT.getClient().getService( JavaScriptLoader.class );
    loader.ensure( RWT.getResourceManager().getLocation( REGISTER_NAME ) );
  }

  private static void register() throws IOException {
    InputStream inputStream = getConcatentatedInputStream();
    try {
      RWT.getResourceManager().register( REGISTER_NAME, inputStream );
    } finally {
      inputStream.close();
    }
  }

  public static void register( Application application ) {
    application.addResource( REGISTER_NAME, new ResourceLoader() {
      public InputStream getResourceAsStream( String resourceName ) throws IOException {
        return getConcatentatedInputStream();
      }
    } );
  }

  private static InputStream getConcatentatedInputStream() throws IOException {
    return new ByteArrayInputStream( concatResources().getBytes( CHARSET ) );
  }

  private static String concatResources() throws IOException {
    StringBuilder builder = new StringBuilder();
    for( String resourceName : ALL_RESOURCES ) {
      InputStream inputStream = getResourceAsStream( resourceName );
      try {
        read( inputStream, builder );
      } finally {
        inputStream.close();
      }
    }
    return builder.toString();
  }

  private static InputStream getResourceAsStream( String resourceName ) {
    ClassLoader classLoader = ClientScriptingResources.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream( PREFIX + resourceName );
    if( inputStream == null ) {
      throw new RuntimeException( "Resource not found: " + resourceName );
    }
    return inputStream;
  }

  static void read( InputStream inputStream, StringBuilder builder ) throws IOException {
    InputStreamReader reader = new InputStreamReader( inputStream, CHARSET );
    BufferedReader bufferedReader = new BufferedReader( reader );
    char[] buffer = new char[ 4096 ];
    int read = bufferedReader.read( buffer );
    while( read != -1 ) {
      builder.append( buffer, 0, read );
      read = bufferedReader.read( buffer );
    }
  }

  private ClientScriptingResources() {
  }

}
