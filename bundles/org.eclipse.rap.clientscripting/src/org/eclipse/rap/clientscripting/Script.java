/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.clientscripting;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;


public class Script {

  private static final String REMOTE_TYPE = "rwt.clientscripting.Script";
  private RemoteObject remoteObject;

  public Script( String text ) {
    if( text == null ) {
      throw new NullPointerException( "Text must not be null" );
    }
    Connection connection = RWT.getUISession().getConnection();
    remoteObject = connection.createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "text", text );
  }

  public String getId() {
    return remoteObject.getId();
  }

  public void dispose() {
    remoteObject.destroy();
  }

}
