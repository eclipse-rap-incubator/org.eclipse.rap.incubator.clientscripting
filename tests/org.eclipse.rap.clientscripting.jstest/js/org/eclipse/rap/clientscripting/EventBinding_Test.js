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

var EventBinding = org.eclipse.rap.clientscripting.EventBinding;
var EventProxy = org.eclipse.rap.clientscripting.EventProxy;
var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
var Processor = rwt.remote.MessageProcessor;
var ObjectManager = rwt.remote.ObjectRegistry;
var SWT = org.eclipse.rap.clientscripting.SWT;
var EventHandlerUtil = rwt.event.EventHandlerUtil;

var text;
var textEl;

rwt.qx.Class.define( "org.eclipse.rap.clientscripting.EventBinding_Test", {

  extend : rwt.qx.Object,

  members : {

    testCreateBindingByProtocol : function() {
      var code = "var handleEvent = function(){};";
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code
        }
      } );

      Processor.processOperation( {
        "target" : "w5",
        "action" : "create",
        "type" : "rwt.clientscripting.EventBinding",
        "properties" : {
          "eventType" : "KeyDown",
          "targetObject" : "w3",
          "listener" : "w4"
        }
      } );

      var result = ObjectManager.getObject( "w5" );
      assertTrue( result instanceof EventBinding );
      assertIdentical( "KeyDown", result.getType() );
    },

    testBindKeyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      new EventBinding( text, "KeyDown", logger );
      TestUtil.press( text, "A" );

      assertEquals( 1, logger.log.length );
    },

    testDisposeBindKeyEvent : function() {
      var logger = this._createLogger();

      var binding = new EventBinding( text, "KeyDown", logger );
      binding.dispose();
      TestUtil.press( text, "A" );

      assertEquals( 0, logger.log.length );
      assertNull( binding._source );
      assertNull( binding._targetFunction );
    },

    testBindCreatesProxyEvent : function() {
      var logger = this._createLogger();

      new EventBinding( text, "KeyDown", logger );
      TestUtil.press( text, "A" );

      var event = logger.log[ 0 ];
      assertTrue( event instanceof EventProxy );
    },

    testBindDisposesProxyEvent : function() {
      var logger = this._createLogger();

      new EventBinding( text, "KeyDown", logger );
      TestUtil.press( text, "A" );

      var event = logger.log[ 0 ];
      assertTrue( TestUtil.hasNoObjects( event ) );
    },

    testDoItFalseKeyDown : function() {
      var listener = {
        "call" : function( event ) {
          event.doit = false;
        }
      };

      new EventBinding( text, "KeyDown", listener );
      var domEvent = TestUtil.createFakeDomKeyEvent( text.getElement(), "keypress", "a" );
      TestUtil.fireFakeDomEvent( domEvent );

      assertTrue( EventHandlerUtil.wasStopped( domEvent ) );
    },

    testDoItFalseMouseDown : function() {
      var listener = {
        "call" : function( event ) {
          event.doit = false;
        }
      };

      new EventBinding( text, "MouseDown", listener );
      var domEvent = TestUtil.createFakeDomKeyEvent( text.getElement(), "keypress", "a" );
      TestUtil.fireFakeDomEvent( domEvent );

      // SWT doesnt support preventing native selection behavior (e.g. Text widget)
      assertFalse( EventHandlerUtil.wasStopped( domEvent ) );
    },

    testBindKeyUp : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      var binding = new EventBinding( text, "KeyUp", logger );
      TestUtil.keyDown( textEl, "A" );
      assertEquals( 0, logger.log.length );
      TestUtil.keyUp( textEl, "A" );

      assertEquals( 1, logger.log.length );
    },


    testBindFocusInEvent : function() {
      text.blur();
      var logger = this._createLogger();

      new EventBinding( text, "FocusIn", logger );
      text.focus();

      assertEquals( 1, logger.log.length );
    },

    testBindFocusOutEvent : function() {
      text.focus();
      var logger = this._createLogger();

      new EventBinding( text, "FocusOut", logger );
      text.blur();

      assertEquals( 1, logger.log.length );
    },

    testBindMouseDown : function() {
      var logger = this._createLogger();

      new EventBinding( text, "MouseDown", logger );
      TestUtil.fakeMouseEventDOM( textEl, "mousedown" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseUp : function() {
      var logger = this._createLogger();

      TestUtil.fakeMouseEventDOM( textEl, "mousedown" );
      new EventBinding( text, "MouseUp", logger );
      TestUtil.fakeMouseEventDOM( textEl, "mouseup" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseMove : function() {
      var logger = this._createLogger();

      TestUtil.fakeMouseEventDOM( textEl, "mouseover" );
      new EventBinding( text, "MouseMove", logger );
      TestUtil.fakeMouseEventDOM( textEl, "mousemove" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseEnter : function() {
      var logger = this._createLogger();

      new EventBinding( text, "MouseEnter", logger );
      TestUtil.fakeMouseEventDOM( textEl, "mouseover" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseExit : function() {
      var logger = this._createLogger();

      TestUtil.fakeMouseEventDOM( textEl, "mouseover" );
      new EventBinding( text, "MouseExit", logger );
      TestUtil.fakeMouseEventDOM( textEl, "mouseout" );

      assertEquals( 1, logger.log.length );
    },

    testBindShow : function() {
      var logger = this._createLogger();
      text.setVisibility( false );

      new EventBinding( text, "Show", logger );
      text.setVisibility( true );

      assertEquals( 1, logger.log.length );
    },

    testBindHide : function() {
      var logger = this._createLogger();
      text.setVisibility( true );

      new EventBinding( text, "Hide", logger );
      text.setVisibility( false );

      assertEquals( 1, logger.log.length );
    },

    testBindVerifyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      new EventBinding( text, "Verify", logger );
      this._inputText( text, "goo" );

      assertEquals( 1, logger.log.length );
    },

    testDisposeVerifyEventBinding : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      var binding = new EventBinding( text, "Verify", logger );
      binding.dispose();
      this._inputText( text, "goo" );

      assertEquals( 0, logger.log.length );
      assertEquals( "goo", text.getValue() );
    },

    testVerifyEventFiredBeforeChange : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var textValue;
      var handler = {
        "call" : function( event ) {
          textValue = event.widget.getText();
        }
      };

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "bar" );

      assertEquals( "bar", text.getValue() );
      assertEquals( "foo", textValue );
    },

    testVerifyEventDoItFalse : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.doit = false;
        }
      };

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "bar" );

      assertEquals( "foo", text.getValue() );
      assertEquals( "foo", text.getComputedValue() );
    },

    testVerifyEventDoItFalseSelection : function() {
      TestUtil.flush();
      text.setValue( "fooxxx" );
      var handler = {
        "call" : function( event ) {
          event.doit = false;
        }
      };

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "foobarxxx", [ 3, 3 ] );

      assertEquals( 3, text._getSelectionStart() );
      assertEquals( 0, text._getSelectionLength() );
    },

    testVerifyBindingProtectAgainstTypeOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.type = "boom";
        }
      } ;

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "bar" );

      assertEquals( "bar", text.getValue() );
    },

    testVerifyEventTextOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
        }
      };

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "foob", [ 3, 3 ] );

      assertEquals( "foobar", text.getValue() );
    },

    testVerifyEventSelectionAfterTextOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
        }
      } ;

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "foxo", [ 2, 2 ] );

      assertEquals( "fobaro", text.getValue() );
      assertEquals( 5, text._getSelectionStart() );
      assertEquals( 0, text._getSelectionLength() );
    },

    testVerifyEventSelectionAfterReplacementTextOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
        }
      } ;

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "fxo", [ 1, 2 ] );

      assertEquals( "fbaro", text.getValue() );
      assertEquals( 4, text._getSelectionStart() );
      assertEquals( 0, text._getSelectionLength() );
    },

    testSelectionDuringVerifyEvent : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var selection;
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
          selection = event.widget.getSelection();
        }
      } ;

      new EventBinding( text, "Verify", handler );
      this._inputText( text, "foxo", [ 2, 2 ] );

      assertEquals( 5, text._getSelectionStart() );
      assertEquals( 0, text._getSelectionLength() );
      assertEquals( [ 2, 2 ], selection );
    },

    testSelectionByKeyPressDuringVerifyEvent : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var selection;
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
          selection = event.widget.getSelection();
        }
      } ;

      new EventBinding( text, "Verify", handler );
      text._setSelectionStart( 2 );
      text._setSelectionLength( 0 );
      TestUtil.press( text, "x" );
      this._inputText( text, "foxo", [ 2, 2 ] );

      assertEquals( 5, text._getSelectionStart() );
      assertEquals( 0, text._getSelectionLength() );
      assertEquals( [ 2, 2 ], selection );
    },

    testBindModifyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      new EventBinding( text, "Modify", logger );
      text.setValue( "foo" );

      assertEquals( 1, logger.log.length );
    },

    testBindVerifyAndModifyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      new EventBinding( text, "Modify", logger );
      new EventBinding( text, "Verify", logger );
      this._inputText( text, "foo" );

      assertEquals( 2, logger.log.length );
      assertEquals( SWT.Verify, logger.log[ 0 ].type );
      assertEquals( SWT.Modify, logger.log[ 1 ].type );
    },

    testBindPaintEvent : function() {
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
      var logger = this._createLogger();
      TestUtil.flush();

      new EventBinding( canvas, "Paint", logger );
      canvas.dispatchSimpleEvent( "paint" );
      TestUtil.flush();


      assertEquals( 1, logger.log.length );
      canvas.destroy();
    },

    testBindSelectionEventOnList : function() {
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
      var logger = this._createLogger();
      TestUtil.flush();

      new EventBinding( list, "Selection", logger );
      TestUtil.click( list.getItems()[ 1 ] );
      TestUtil.flush();

      assertEquals( 1, logger.log.length );
    },

    testBindDefaultSelectionEventOnList : function() {
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
      var logger = this._createLogger();
      TestUtil.flush();

      new EventBinding( list, "DefaultSelection", logger );
      TestUtil.doubleClick( list.getItems()[ 1 ] );
      TestUtil.flush();

      assertEquals( 1, logger.log.length );
    },

    testBindToPublicObject : function() {
      var obj = this._createPublicObject( "x1" );
      var logger = this._createLogger();

      new EventBinding( obj, "Selection", logger );
      obj.listenerHolder.dispatchSimpleEvent( "Selection", "myEventObject" );

      assertEquals( 1, logger.log.length );
      assertEquals( "myEventObject", logger.log[ 0 ] );
    },

    testRemoveFromPublicObject : function() {
      var obj = this._createPublicObject( "x1" );
      var logger = this._createLogger();

      var binding = new EventBinding( obj, "Selection", logger );
      binding.dispose();
      obj.listenerHolder.dispatchSimpleEvent( "Selection", "myEventObject" );

      assertEquals( 0, logger.log.length );
    },

    /////////
    // helper

    _createLogger : function() {
      var result = {
        "log" : [],
        "call" : function( arg ) {
          this.log.push( arg ); // it's important that "this" is available, like in Function.js
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
      textEl = text.getElement();
    },

    tearDown : function() {
      Processor.processOperation( {
        "target" : "w2",
        "action" : "destroy"
      } );
      text = null;
    },

    _inputText : function( textWidget, text, oldSel ) {
      if( typeof oldSel !== "undefined" ) {
        textWidget._setSelectionStart( oldSel[ 0 ] );
        textWidget._setSelectionLength( oldSel[ 1 ] - oldSel[ 0 ] );
        TestUtil.click( textWidget ); // pasting
      }
      textWidget._inValueProperty = true;
      textWidget._inputElement.value = text;
      textWidget._inValueProperty = false;
      textWidget._oninputDom( { "propertyName" : "value" } );
      TestUtil.forceTimerOnce();
    },

    _createPublicObject : function( id ) {
      var type = "eventbindingtest.publicType";
      if( !rwt.remote.HandlerRegistry.hasHandler( type ) ) {
        rap.registerTypeHandler( type, {
          factory : function() {
            return {
              "listenerHolder" : new rwt.qx.Target(),
              "addListener" : function( type, handler ) {
                this.listenerHolder.addEventListener( type, handler );
              },
              "removeListener" : function( type, handler ) {
                this.listenerHolder.removeEventListener( type, handler );
              }
            };
          }
        } );
      }
      Processor.processOperation( {
        "target" : id,
        "action" : "create",
        "type" : type,
        "properties" : {}
      } );
      return rap.getObject( id );
    }


  }

} );

}());
