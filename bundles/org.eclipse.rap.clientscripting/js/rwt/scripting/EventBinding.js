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

rwt.qx.Class.createNamespace( "rwt.scripting", {} );

rwt.scripting.EventBinding = function( source, eventType, targetFunction ) {
  var ClientScriptingUtil = rwt.scripting.ClientScriptingUtil;
  try {
    this._eventType = eventType;
    this._source = source;
    this._public = ClientScriptingUtil.isPublicObject( source );
    this._targetFunction = targetFunction;
    if( this._public ) {
      this._eventSource = source;
      this._nativeType = eventType;
    } else {
      this._nativeType = ClientScriptingUtil.getNativeEventType( source, this._eventType );
      this._eventSource = ClientScriptingUtil.getNativeEventSource( source, this._eventType );
    }
    this._bind();
  } catch( ex ) {
    throw new Error( "Could not create EventBinding " + eventType + ":" + ex.message );
  }
};

rwt.scripting.EventBinding.prototype = {

  _bind : function() {
    if( this._public ) {
      this._eventSource.addListener( this._nativeType, this._targetFunction );
    } else {
      this._eventSource.addEventListener( this._nativeType, this._processEvent, this );
    }
  },

  _unbind : function() {
    if( this._public ) {
      this._eventSource.removeListener( this._nativeType, this._targetFunction );
    } else {
      this._eventSource.removeEventListener( this._nativeType, this._processEvent, this );
    }
  },

  _processEvent : function( event ) {
    try {
      var EventProxy = rwt.scripting.EventProxy;
      var ClientScriptingUtil = rwt.scripting.ClientScriptingUtil;
      var SWT = rwt.scripting.SWT;
      var eventProxy = new EventProxy( SWT[ this._eventType ], this._source, event );
      var wrappedEventProxy = ClientScriptingUtil.wrapAsProto( eventProxy );
      this._targetFunction( wrappedEventProxy );
      ClientScriptingUtil.postProcessEvent( eventProxy, wrappedEventProxy, event );
      EventProxy.disposeEventProxy( eventProxy );
    } catch( ex ) {
      var msg = "Error in ClientScripting event type ";
      throw new Error( msg + this._eventType + ": " + ex.message ? ex.message : ex );
    }
  },

  getType : function() {
    return this._eventType;
  },

  dispose : function() {
    this._unbind();
    this._source = null;
    this._targetFunction = null;
  }

};

}());

