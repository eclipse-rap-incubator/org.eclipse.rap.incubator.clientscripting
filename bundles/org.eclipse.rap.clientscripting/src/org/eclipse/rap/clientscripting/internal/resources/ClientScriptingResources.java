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

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.service.ResourceLoader;


public final class ClientScriptingResources {

  private static final String REGISTER_NAME = "clientscripting.js";
  private static final String PREFIX = "rwt/scripting/";
  private static final String[] ALL_RESOURCES = {
    "SWT.js",
    "EventBinding.js",
    "handler/EventBindingHandler.js",
    "EventProxy.js",
    "FunctionFactory.js",
    "handler/FunctionHandler.js",
    "WidgetProxyFactory.js",
    "init.js"
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
    loader.require( RWT.getResourceManager().getLocation( REGISTER_NAME ) );
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

  private static InputStream getConcatentatedInputStream() {
    Vector<InputStream> inputStreams = new Vector<InputStream>( ALL_RESOURCES.length );
    for( String resourceName : ALL_RESOURCES ) {
      inputStreams.add( getResourceAsStream( resourceName ) );
    }
    return new SequenceInputStream( inputStreams.elements() );
  }

  private static InputStream getResourceAsStream( String resourceName ) {
    ClassLoader classLoader = ClientScriptingResources.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream( PREFIX + resourceName );
    if( inputStream == null ) {
      throw new RuntimeException( "Resource not found: " + resourceName );
    }
    return inputStream;
  }

  private ClientScriptingResources() {
  }

}
