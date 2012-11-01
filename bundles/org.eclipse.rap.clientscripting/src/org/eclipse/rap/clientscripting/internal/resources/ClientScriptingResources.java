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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.resources.ResourceLoader;


public final class ClientScriptingResources {

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

  public static void register( Application application ) {
    ClientscriptingResourceLoader resourceLoader = new ClientscriptingResourceLoader();
    for( String resourceName : ALL_RESOURCES ) {
      application.addResource( resourceName, resourceLoader );
    }
  }

  private ClientScriptingResources() {
  }

  private static class ClientscriptingResourceLoader implements ResourceLoader {

    public InputStream getResourceAsStream( String resourceName ) throws IOException {
      ClassLoader classLoader = ClientScriptingResources.class.getClassLoader();
      return classLoader.getResourceAsStream( PREFIX + resourceName );
    }

  }

}
