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

rwt.qx.Class.createNamespace( "org.eclipse.rap.clientscripting", {} );

org.eclipse.rap.clientscripting.WidgetProxy = function( originalWidget ) {
  var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
  ClientScriptingUtil.attachSetter( this, originalWidget );
  ClientScriptingUtil.attachGetter( this, originalWidget );
  ClientScriptingUtil.attachUserData( this, originalWidget );
  if( rwt.remote.WidgetManager.getInstance().isControl( originalWidget ) ) {
    ClientScriptingUtil.attachControlMethods( this, originalWidget );
  }
  ClientScriptingUtil.addDisposeListener( originalWidget, function() {
    org.eclipse.rap.clientscripting.WidgetProxy.disposeWidgetProxy( originalWidget );
  } );
};

org.eclipse.rap.clientscripting.WidgetProxy._PROXY_KEY =
  "org.eclipse.rap.clientscripting.WidgetProxy.PROXY";

org.eclipse.rap.clientscripting.WidgetProxy._GC_KEY =
  "org.eclipse.rap.clientscripting.WidgetProxy.GC";

org.eclipse.rap.clientscripting.WidgetProxy.getInstance = function( widget ) {
  return rap._.getWrapperFor( widget );
};

var getWrapperFor = rap._.getWrapperFor;
rap._.getWrapperFor = function( obj ) {
  var result = getWrapperFor.call( rap._, obj );
  var PROXY_KEY = org.eclipse.rap.clientscripting.WidgetProxy._PROXY_KEY;
  if( obj.getUserData( PROXY_KEY ) == null ) {
    org.eclipse.rap.clientscripting.WidgetProxy.call( result, obj );
    obj.setUserData( PROXY_KEY, result );
  }
  return result;
};

org.eclipse.rap.clientscripting.WidgetProxy.disposeWidgetProxy = function( widget ) {
  var protoInstance = widget.getUserData( this._PROXY_KEY );
  var userData = widget.getUserData( rwt.remote.HandlerUtil.SERVER_DATA );
  org.eclipse.rap.clientscripting.ClientScriptingUtil.disposeObject( protoInstance );
  org.eclipse.rap.clientscripting.ClientScriptingUtil.disposeObject( userData );
};

}());
