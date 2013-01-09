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

import org.eclipse.rap.clientscripting.internal.ClientListenerUtil;
import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;


@SuppressWarnings( "restriction" )
class ClientListenerBinding {

  private static final String REMOTE_TYPE = "rwt.clientscripting.EventBinding";

  private final ClientListener listener;
  private final Widget widget;
  private final int eventType;
  private final RemoteObject remoteObject;
  private boolean disposed;

  ClientListenerBinding( ClientListener listener, Widget widget, int eventType ) {
    this.listener = listener;
    this.widget = widget;
    this.eventType = eventType;
    remoteObject = RemoteObjectFactory.getInstance().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "listener", listener.getRemoteId() );
    remoteObject.set( "targetObject", WidgetUtil.getId( widget ) );
    remoteObject.set( "eventType", ClientListenerUtil.getEventType( eventType ) );
  }

  public Widget getWidget() {
    return widget;
  }

  public int getEventType() {
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
      if( eventType == other.eventType && widget == other.widget && listener == other.listener ) {
        result = true;
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + eventType;
    result = prime * result + widget.hashCode();
    result = prime * result + listener.hashCode();
    return result;
  }

}
