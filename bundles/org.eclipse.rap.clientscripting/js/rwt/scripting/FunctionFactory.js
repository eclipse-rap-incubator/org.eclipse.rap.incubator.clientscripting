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

rwt.qx.Class.createNamespace( "rwt.scripting", {} );

(function(){

/*global handleEvent:false */
rwt.scripting.FunctionFactory = {

  createFunction : function( functionScript, name, scope ) {
    var result;
    var code = [
      this._getScopeScript( scope ),
      functionScript,
      "\n\n",
      "typeof ",
      name,
      " === \"undefined\" ? null : ",
      name,
      ";" ];
    try {
      result = this._secureEval.apply( window, [ code.join( "" ) ] );
    } catch( ex ) {
      var msg = "Could not parse Script for " + name + ":" + ( ex.message ? ex.message : ex );
      throw new Error( msg );
    }
    if( typeof result !== "function" ) {
      throw new Error( "Script does not define a function " + name );
    }
    return result;
  },

  _secureEval : function() {
    return eval( arguments[ 0 ] );
  },

  _getScopeScript : function( scope ) {
    var result = [];
    for( var key in scope ) {
      // NOTE: currently the values are evaluated "as is", i.e. json string.
      //       Support for protocol objects would be possible, but may require "evalInScope" feature
      result.push( "var ", key, " = ", scope[ key ], ";\n" );
    }
    return result.join( "" );
  }

};

}());
