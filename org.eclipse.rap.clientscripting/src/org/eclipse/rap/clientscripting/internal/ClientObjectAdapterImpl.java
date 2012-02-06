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
package org.eclipse.rap.clientscripting.internal;


public class ClientObjectAdapterImpl implements ClientObjectAdapter {

  private final String id = ObjectIdGenerator.getNextId();
  private boolean created = false;

  public String getId() {
    return id;
  }

  public boolean isCreated() {
    return created;
  }

  public void setCreated() {
    created = true;
  }
}