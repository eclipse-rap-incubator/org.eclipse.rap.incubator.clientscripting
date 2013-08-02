/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/

(function() {

var Function = rwt.scripting.Function;
var SWT = rwt.scripting.SWT;
var MessageProcessor = rwt.remote.MessageProcessor;

rwt.qx.Class.define( "org.eclipse.rap.clientscripting.Script_Test", {

  extend : rwt.qx.Object,

  members : {

    testCreateScriptWithTextByProtocol : function() {
      var code = "1+1;";

      var op = [ "create", "r3", "rwt.scripting.Script", { "text" : code } ];
      MessageProcessor.processOperationArray( op );

      var ObjectManager = rwt.remote.ObjectRegistry;
      var result = ObjectManager.getObject( "r3" );
      assertEquals( 2, eval( result.getText() ) );
      MessageProcessor.processOperationArray( [ "destroy", "r3" ] );
    }

  }

} );

}());
