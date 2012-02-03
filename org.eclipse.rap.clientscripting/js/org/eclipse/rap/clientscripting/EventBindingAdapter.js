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

org.eclipse.rwt.protocol.AdapterRegistry.add( "rwt.clientscripting.EventBinding", {

  factory : function( properties ) {
  	var source = org.eclipse.rwt.protocol.ObjectManager.getObject( properties.targetObject );
  	var eventType = properties.eventType;
  	var targetFunction = org.eclipse.rwt.protocol.ObjectManager.getObject( properties.listener );
    return new org.eclipse.rap.clientscripting.EventBinding( source, eventType, targetFunction );
  },

  destructor : qx.lang.Function.returnTrue,

  properties : [],

  propertyHandler : {},

  listeners : [],

  listenerHandler : {},

  methods : [],

  methodHandler : {}

} );
