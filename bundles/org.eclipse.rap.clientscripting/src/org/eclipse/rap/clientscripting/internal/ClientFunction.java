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

  protected void addTo( String targetId, int eventType ) {
    ClientListenerBinding binding = new ClientListenerBinding( this, targetId, eventType );
    addBinding( binding );
  }

  protected void removeFrom( String targetId, int eventType ) {
    ClientListenerBinding binding = findBinding( targetId, eventType );
    if( binding != null ) {
      binding.dispose(); // TODO still in collection?
    }
  }

  protected void disposeBindingsWithTarget( String targetId ) {
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getTargetId() ==  targetId ) {
        binding.dispose();
      }
    }
  }

  ClientListenerBinding findBinding( String targetId, int eventType ) {
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getTargetId() ==  targetId && binding.getEventType() == eventType ) {
        return binding;
      }
    }
    return null;
  }

  private void addBinding( final ClientListenerBinding binding ) {
    if( !bindings.contains( binding ) ) {
      bindings.add( binding );
    }
  }



}
