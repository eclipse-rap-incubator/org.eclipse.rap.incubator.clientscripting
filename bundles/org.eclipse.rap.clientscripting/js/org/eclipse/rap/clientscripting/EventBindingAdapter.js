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

(function(){

rwt.remote.HandlerRegistry.add( "rwt.clientscripting.EventBinding", {

  factory : function( properties ) {
    var ObjectRegistry = rwt.remote.ObjectRegistry;
    var EventBinding = org.eclipse.rap.clientscripting.EventBinding;
    var source = ObjectRegistry.getObject( properties.targetObject );
    var eventType = properties.eventType;
    var targetFunction = ObjectRegistry.getObject( properties.listener );
    return new EventBinding( source, eventType, targetFunction );
  }

} );

}());