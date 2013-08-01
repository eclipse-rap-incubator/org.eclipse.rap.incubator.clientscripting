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

(function() {

var Function = org.eclipse.rap.clientscripting.Function;
var SWT = org.eclipse.rap.clientscripting.SWT;

rwt.qx.Class.define( "org.eclipse.rap.clientscripting.Function_Test", {

  extend : rwt.qx.Object,

  members : {

    testCreateFunctionWrongNamed : function() {
      var code = "function foo(){}";
      try {
        new Function( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    /*global global:true */
    testCreateFunctionWithHelper : function() {
      var code = "var foo = function(){  global = 1;  };var handleEvent = function(){ foo(); };";
      var listener = new Function( code );
      listener.call();
      assertEquals( 1, global );
      delete global; // An alternative would be to create a storage for such cases in TestUtil
    },

    testCreateFunctionSyntaxError : function() {
      var code = "null.no!;";
      try {
        new Function( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testCreateFunctionNoFunction : function() {
      var code = "1";
      try {
        new Function( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testCreateFunctionByProtocol : function() {
      var ObjectManager = rwt.remote.ObjectRegistry;
      var processor = rwt.remote.MessageProcessor;
      var code = "var handleEvent = function(){};";

      processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "scriptCode" : code
        }
      } );

      var result = ObjectManager.getObject( "w3" );
      assertTrue( result instanceof Function );
    },

    testCreateFunctionByProtocol_withScriptId : function() {
      var ObjectManager = rwt.remote.ObjectRegistry;
      var processor = rwt.remote.MessageProcessor;
      var code = "var handleEvent = function(){};";
      var createScript = [ "create", "r3", "rwt.clientscripting.Script", { "text" : code } ];
      processor.processOperationArray( createScript );

      processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "scriptId" : "r3"
        }
      } );

      var result = ObjectManager.getObject( "w3" );
      assertTrue( result instanceof Function );
    },

    testCallWithArgument : function() {
      var code = "function handleEvent( e ){ e.x++; }";
      var listener = new Function( code );
      var event = {
        x : 1
      };

      listener.call( event );

      assertEquals( 2, event.x );
    },

    testNoContext : function() {
      var code = "var handleEvent = function(){ this.x++; }";
      var listener = new Function( code );
      listener.x = 1;

      listener.call();

      assertEquals( 1, listener.x );
    },

    testImportedClasses : function() {
      var obj = {};
      var code = "function handleEvent( obj ){ obj.SWT = SWT; }";
      var fun = new Function( code );

      fun.call( obj );

      assertIdentical( SWT, obj.SWT );
    }

  }

} );

}());
