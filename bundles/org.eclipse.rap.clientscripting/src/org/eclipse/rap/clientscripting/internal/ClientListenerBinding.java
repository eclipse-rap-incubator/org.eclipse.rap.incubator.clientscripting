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

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;


public class ClientListenerBinding {

  private static final String REMOTE_TYPE = "rwt.clientscripting.EventBinding";

  private final ClientFunction function;
  private final String targetId;
  private final String eventType;
  private final RemoteObject remoteObject;
  private boolean disposed;

  public ClientListenerBinding( ClientFunction function, String targetId, String eventType ) {
    this.function = function;
    this.targetId = targetId;
    this.eventType = eventType;
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "listener", function.getRemoteId() );
    remoteObject.set( "targetObject", targetId );
    remoteObject.set( "eventType", eventType );
  }

  public String getTargetId() {
    return targetId;
  }

  public String getEventType() {
    return eventType;
  }

  public void dispose() {
    if( !disposed ) {
      remoteObject.destroy();
    }
    disposed = true;
  }

  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public boolean equals( Object obj ) {
    boolean result = false;
    if( this == obj ) {
      result = true;
    } else if( obj != null && getClass() == obj.getClass() ) {
      ClientListenerBinding other = ( ClientListenerBinding )obj;
      if( eventType.equals( other.eventType )
          && targetId.equals( other.targetId )
          && function == other.function )
      {
        result = true;
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + eventType.hashCode();
    result = prime * result + targetId.hashCode();
    result = prime * result + function.hashCode();
    return result;
  }

}
