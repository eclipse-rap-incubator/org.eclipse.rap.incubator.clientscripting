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

import org.eclipse.rap.clientscripting.Script;
import org.eclipse.rap.clientscripting.internal.resources.ClientScriptingResources;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;


public class ClientFunction {

  private static final String REMOTE_TYPE = "rwt.clientscripting.Listener";

  private final RemoteObject remoteObject;
  private final Collection<ClientListenerBinding> bindings;

  public ClientFunction( String scriptCode ) {
    this();
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
    remoteObject.set( "scriptCode", scriptCode );
  }

  public ClientFunction( Script script ) {
    this();
    if( script == null ) {
      throw new NullPointerException( "Parameter is null: script" );
    }
    remoteObject.set( "scriptId", script.getId() );
  }

  private ClientFunction() {
    bindings = new ArrayList<ClientListenerBinding>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    ClientScriptingResources.ensure();
  }

  protected String getRemoteId() {
    return remoteObject.getId();
  }

  Collection<ClientListenerBinding> getBindings() {
    return bindings;
  }

  protected ClientListenerBinding addTo( String targetId, String eventType ) {
    if( findBinding( targetId, eventType ) == null ) {
      ClientListenerBinding binding = new ClientListenerBinding( this, targetId, eventType );
      bindings.add( binding );
      return binding;
    } else {
      return null;
    }
  }

  protected void removeFrom( String targetId, String eventType ) {
    ClientListenerBinding binding = findBinding( targetId, eventType );
    if( binding != null ) {
      binding.dispose();
      bindings.remove( binding );
    }
  }

  protected ClientListenerBinding findBinding( String targetId, String eventType ) {
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getTargetId().equals( targetId ) && binding.getEventType().equals( eventType ) ) {
        return binding;
      }
    }
    return null;
  }

}
