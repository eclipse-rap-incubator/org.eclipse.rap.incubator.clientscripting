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

var ObjectRegistry = rwt.remote.ObjectRegistry;
var EventBinding = rwt.scripting.EventBinding;

rwt.remote.HandlerRegistry.add( "rwt.scripting.EventBinding", {

  factory : function( properties ) {
    var source = ObjectRegistry.getObject( properties.targetObject );
    var isPublic = isPublicObject( source );
    var eventType = properties.eventType;
    var targetFunction = ObjectRegistry.getObject( properties.listener );
    if( isPublic ) {
      source.addListener( eventType, targetFunction );
    } else {
      EventBinding.addListener( source, eventType, targetFunction );
    }
    return {
      "isPublic" : isPublic,
      "eventType" : eventType,
      "source" : source,
      "targetFunction" : targetFunction
    };
  },

  destructor : function( binding ) {
    if( binding.isPublic ) {
      binding.source.removeListener( binding.eventType, binding.targetFunction );
    } else {
      EventBinding.removeListener( binding.source, binding.eventType, binding.targetFunction );
    }
  }

} );

var isPublicObject = function( obj ) {
  return ObjectRegistry.getEntry( ObjectRegistry.getId( obj ) ).handler.isPublic === true;
};

}());
