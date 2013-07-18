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

var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
var Processor = rwt.remote.MessageProcessor;
var ObjectManager = rwt.remote.ObjectRegistry;
var WidgetProxy = org.eclipse.rap.clientscripting.WidgetProxy;
var EventBinding = org.eclipse.rap.clientscripting.EventBinding;
var SWT = org.eclipse.rap.clientscripting.SWT;

var text;

rwt.qx.Class.define( "org.eclipse.rap.clientscripting.WidgetProxy_Test", {

  extend : rwt.qx.Object,

  members : {

  testCreateTextWidgetProxyFromPublicAPI : function() {
      var widgetProxy = WidgetProxy.getInstance( text );

      var otherProxy = rap.getObject( "w3" );

      assertIdentical( widgetProxy, otherProxy );
    },

    testCreateTextWidgetProxyTwice : function() {
      var widgetProxy1 = WidgetProxy.getInstance( text );
      var widgetProxy2 = WidgetProxy.getInstance( text );

      assertTrue( widgetProxy1 === widgetProxy2 );
    },

    testDisposeWidgetProxy : function() {
      var widgetProxy = WidgetProxy.getInstance( text );

      text.destroy();
      TestUtil.flush();

      assertTrue( TestUtil.hasNoObjects( widgetProxy ) );
      if( widgetProxy.__proto__ ) { // __proto__ is not an ECMA standard
        var proto = widgetProxy.__proto__;
        assertTrue( TestUtil.hasNoObjects( proto ) );
      }
    },

    testDisposeUserData : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      widgetProxy.setData( "key", {} );
      var data = rwt.remote.HandlerUtil.getServerData( text );
      assertFalse( TestUtil.hasNoObjects( data ) );

      text.destroy();
      TestUtil.flush();

      assertTrue( TestUtil.hasNoObjects( data ) );
    },

    testSetter : function() {
      var widgetProxy = WidgetProxy.getInstance( text );

      widgetProxy.setText( "foo" );

      assertEquals( "foo", text.getValue() );
    },

    testSetGetData : function() {
      var widgetProxy1 = WidgetProxy.getInstance( text );
      var widgetProxy2 = WidgetProxy.getInstance( text );

      widgetProxy1.setData( "myKey", 24 );

      assertNull( widgetProxy2.getData( "myWrongKey" ) );
      assertEquals( 24, widgetProxy2.getData( "myKey" ) );
    },

    testSetDataTooManyArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.setData( "myKey", 24, "foo" );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testSetDataTooFewArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.setData( 24 );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testGetDataTooManyArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.getData( "myKey", 24 );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testGetDataTooFewArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.getData();
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testSetTextSync : function() {
      TestUtil.initRequestLog();
      var widgetProxy = WidgetProxy.getInstance( text );

      widgetProxy.setText( "foo" );
      rwt.remote.Server.getInstance().send();
      var msg = TestUtil.getMessageObject();
      assertEquals( "foo", msg.findSetProperty( "w3", "text" ) );
    },

    testTextGetText : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.setValue( "foo" );

      var value = widgetProxy.getText();

      assertEquals( "foo", value );
    },

    testTextGetSelection : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.setValue( "foo" );
      text.setSelection( [ 1,2 ] );

      var value = widgetProxy.getSelection();

      assertEquals( [ 1, 2 ], value );
    },

    testTextGetEditable_returnsTrue : function() {
      var widgetProxy = WidgetProxy.getInstance( text );

      var value = widgetProxy.getEditable();

      assertTrue( value );
    },

    testTextGetEditable_returnsFalse : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      widgetProxy.setEditable( false );

      var value = widgetProxy.getEditable();

      assertFalse( value );
    },

    testTextForceFocus : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.blur();

      var value = widgetProxy.forceFocus();

      assertTrue( text.isFocused() );
      assertTrue( value );
    },

    testTextForceFocus_NotVisible : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.blur();
      text.setVisibility( false );

      var value = widgetProxy.forceFocus();

      assertFalse( text.isFocused() );
      assertFalse( value );
    },

    testTextForceFocus_ParentNotVisible : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.blur();
      text.getParent().setVisibility( false );
      TestUtil.flush();

      var value = widgetProxy.forceFocus();

      assertFalse( text.isFocused() );
      assertFalse( value );
    },

    testTextForceFocus_NotEnabled : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.blur();
      text.setEnabled( false );

      var value = widgetProxy.forceFocus();

      assertFalse( text.isFocused() );
      assertFalse( value );
    },

    testListGetSelection : function() {
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.widgets.List",
        "properties" : {
          "style" : [ ],
          "parent" : "w2",
          "items" : [ "a", "b", "c" ]
        }
      } );
      var list = ObjectManager.getObject( "w4" );
      TestUtil.flush();
      var widgetProxy = WidgetProxy.getInstance( list );
      TestUtil.click( list.getItems()[ 1 ] );

      var value = widgetProxy.getSelection();

      assertEquals( [ "b" ], value );
    },

    testListGetSelection_unescaped : function() {
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.widgets.List",
        "properties" : {
          "style" : [ ],
          "parent" : "w2",
          "items" : [ "a", "b & x", "c" ]
        }
      } );
      var list = ObjectManager.getObject( "w4" );
      TestUtil.flush();
      var widgetProxy = WidgetProxy.getInstance( list );
      TestUtil.click( list.getItems()[ 1 ] );

      var value = widgetProxy.getSelection();

      assertEquals( [ "b & x" ], value );
    },

    testRedraw : function() {
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.widgets.Canvas",
        "properties" : {
          "style" : [ ],
          "parent" : "w2"
        }
      } );
      var canvas = ObjectManager.getObject( "w4" );
      var widgetProxy = WidgetProxy.getInstance( canvas );
      var logger = this._createLogger();
      TestUtil.flush();
      new EventBinding( canvas, "Paint", logger );

      assertEquals( 0, logger.log.length );
      widgetProxy.redraw();

      assertEquals( 1, logger.log.length );
      canvas.destroy();
    },

    ////////
    // Helper

    _createLogger : function() {
      var log = [];
      var result = {
        "log" : log,
        "call" : function( arg ) {
          log.push( arg );
        }
      };
      return result;
    },


    setUp : function() {
      TestUtil.createShellByProtocol( "w2" );
      Processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.widgets.Text",
        "properties" : {
          "style" : [ "SINGLE", "RIGHT" ],
          "parent" : "w2"
        }
      } );
      TestUtil.flush();
      text = ObjectManager.getObject( "w3" );
      text.focus();
    },

    tearDown : function() {
      Processor.processOperation( {
        "target" : "w2",
        "action" : "destroy"
      } );
      text = null;
    }

  }

} );

}());
