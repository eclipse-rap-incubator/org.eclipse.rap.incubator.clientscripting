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

var ClientScriptingUtil = rwt.scripting.ClientScriptingUtil;
var SWT = rwt.scripting.SWT;

var wrapperRegistry = {};

// TODO : better name?
rwt.scripting.EventBinding = {

  addListener : function( widget, eventType, targetFunction ) {
    var wrapperKey = this._getWrapperKey( widget, eventType, targetFunction );
    if( wrapperRegistry[ wrapperKey ] == null ) {
      var nativeType = ClientScriptingUtil.getNativeEventType( widget, eventType );
      var nativeSource = ClientScriptingUtil.getNativeEventSource( widget, eventType );
      var wrappedListener = this._wrapListener( widget, eventType, targetFunction );
      nativeSource.addEventListener( nativeType, wrappedListener, window );
      wrapperRegistry[ wrapperKey ] = wrappedListener;
    }
  },

  removeListener : function( widget, eventType, targetFunction ) {
    var wrapperKey = this._getWrapperKey( widget, eventType, targetFunction );
    if( wrapperRegistry[ wrapperKey ] != null ) {
      var nativeType = ClientScriptingUtil.getNativeEventType( widget, eventType );
      var nativeSource = ClientScriptingUtil.getNativeEventSource( widget, eventType );
      var wrappedListener = wrapperRegistry[ wrapperKey ];
      nativeSource.removeEventListener( nativeType, wrappedListener, window );
      wrapperRegistry[ wrapperKey ] = null;
    }
  },

  _wrapListener : function( widget, eventType, targetFunction ) {
    return function( nativeEvent ) {
      try {
        var eventProxy = new rwt.scripting.EventProxy( SWT[ eventType ], widget, nativeEvent );
        var wrappedEventProxy = ClientScriptingUtil.wrapAsProto( eventProxy );
        targetFunction( wrappedEventProxy );
        ClientScriptingUtil.postProcessEvent( eventProxy, wrappedEventProxy, nativeEvent );
        rwt.scripting.EventProxy.disposeEventProxy( eventProxy );
      } catch( ex ) {
        var msg = "Error in scripting event type ";
        throw new Error( msg + eventType + ": " + ex.message ? ex.message : ex );
      }
    };
  },

  _getWrapperKey : function( widget, eventType, targetFunction ) {
    var result = [
      rwt.qx.Object.toHashCode( widget ),
      eventType,
      rwt.qx.Object.toHashCode( targetFunction )
    ];
    return result.join( ":" );
  }

};

}());

