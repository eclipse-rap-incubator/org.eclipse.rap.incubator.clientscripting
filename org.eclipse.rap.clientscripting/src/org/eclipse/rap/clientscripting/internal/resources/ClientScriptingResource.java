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

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;


public class ClientScriptingResource implements IResource {

  private static final String PATH_PREFIX = "/org/eclipse/rap/clientscripting/";

  private final String location;

  public ClientScriptingResource( String location ) {
    this.location = PATH_PREFIX + location;
  }

  public ClassLoader getLoader() {
    return ClientScriptingResource.class.getClassLoader();
  }

  public String getLocation() {
    return location;
  }

  public String getCharset() {
    return "UTF-8";
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
