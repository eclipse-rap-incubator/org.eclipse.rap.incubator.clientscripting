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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.rap.clientscripting.internal.resources.ClientScriptingResources;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Widget;



public class ClientFunction {

  private static final String REMOTE_TYPE = "rwt.clientscripting.Listener";

  private final RemoteObject remoteObject;
  private final Collection<ClientListenerBinding> bindings;

  public ClientFunction( String scriptCode ) {
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
    ClientScriptingResources.ensure();
    bindings = new ArrayList<ClientListenerBinding>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "code", scriptCode );
  }

  protected String getRemoteId() {
    return remoteObject.getId();
  }

  Collection<ClientListenerBinding> getBindings() {
    return bindings;
  }

  protected void addTo( Widget widget, int eventType ) {
    if( widget == null ) {
      throw new NullPointerException( "widget is null" );
    }
    if( widget.isDisposed() ) {
      throw new IllegalArgumentException( "Widget is disposed" );
    }
    ClientListenerBinding binding = new ClientListenerBinding( this, widget, eventType );
    addBinding( binding );
  }

  protected void removeFrom( Widget widget, int eventType ) {
    if( widget == null ) {
      throw new NullPointerException( "widget is null" );
    }
    ClientListenerBinding binding = findBinding( widget, eventType );
    if( binding != null ) {
      binding.dispose();
    }
  }

  protected ClientListenerBinding findBinding( Widget widget, int eventType ) {
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getWidget() == widget && binding.getEventType() == eventType ) {
        return binding;
      }
    }
    return null;
  }

  protected void addBinding( final ClientListenerBinding binding ) {
    if( !bindings.contains( binding ) ) {
      bindings.add( binding );
      binding.getWidget().addDisposeListener( new DisposeListener() {
        public void widgetDisposed( DisposeEvent event ) {
          binding.dispose();
        }
      } );
    }
  }


}
