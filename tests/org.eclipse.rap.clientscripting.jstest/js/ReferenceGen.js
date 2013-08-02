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

/*global console:false */

(function() {

  var nl = "</br>";
  var lv1 = "*  ";
  var text = "## Available Setter:" + nl;
  var common = org.eclipse.rwt.remote.HandlerUtil._controlProperties;
  var registry = org.eclipse.rwt.remote.HandlerRegistry._registry;

  var ignore = [ "children", "activeKeys", "cancelKeys", "customVariant", "parent" ];

  var toSetter = function( properties ) {
    var result = [];
    for( var i = 0; i < properties.length; i++ ) {
      result[ i ] = "set" + qx.lang.String.toFirstUp( properties[ i ] );
    }
    return result.join( ", ");
  };

  var filterCommon = function( list ) {
    return list.slice( 0, common.length );
  };

  var filterIgnore = function( list ) {
    var result = list;
    for( var i = 0; i < ignore.length; i++ ) {
      var indexOf = result.indexOf( ignore[ i ] );
      if( indexOf != -1 ) {
        result.splice( indexOf, indexOf + 1 );
      }
    }
    return result;
  };

  var filter = function( list ) {
    return filterIgnore( filterCommon( list ) );
  };

  text += lv1 + "<b>org.eclipse.swt.widgets.Control</b>: " + toSetter( filterIgnore( common ) ) + nl;

  for( var key in registry ) {
    if( key.indexOf( "rwt.widgets" ) === 0 ) {
      try {
        var adapter = registry[ key ];
        var type = "org.eclipse.swt.widget" + key.slice( 10 );
        text += lv1 + "<b>" + type + "</b>: ";
        if( adapter.properties.length > 0 ) {
          text += toSetter( filter( adapter.properties ) );
        }
        text += nl;
      } catch( ex ) {
        console.log( ex );
      }
    }
  }

  text += nl;
  text += "## Available Getter:" + nl;

  var widgets = rwt.scripting.ClientScriptingUtil._getterMapping;

  for( var key in widgets ) {
    var type = "org.eclipse.swt.widget" + key.slice( key.lastIndexOf( "." ) );
    text += lv1 + "<b>" + type + "</b>: ";
    var getters = widgets[ key ];
    var gettersArr = [];
    for( var getter in getters ) {
      gettersArr.push( getter );
    }
    text += gettersArr.join() + nl;
  }

  console.log( text );

}());
