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

rwt.qx.Class.createNamespace( "rwt.scripting", {} );

var ObjectRegistry = rwt.remote.ObjectRegistry;

rwt.scripting.ClientScriptingUtil = {

  _wrapperHelper : function(){},

  _eventTypeMapping : {
    "*" : {
      "KeyDown" : "keypress",
      "KeyUp" : "keyup",
      "MouseDown" : "mousedown",
      "MouseUp" : "mouseup",
      "MouseMove" : "mousemove",
      "MouseEnter" : "mouseover",
      "MouseExit" : "mouseout",
      "MouseDoubleClick" : "dblclick",
      "Paint" : "paint",
      "FocusIn" : "focus",
      "FocusOut" : "blur",
      "Show" : "appear",
      "Hide" : "disappear"
    },
    "rwt.widgets.List" : {
      "Selection" : "changeSelection",
      "DefaultSelection" : "dblclick"
    },
    "rwt.widgets.Text" : {
      "Verify" : "input", // TODO [tb] : does currently not react on programatic changes
      "Modify" : "changeValue"
    }
  },

  isPublicObject : function( obj ) {
    return ObjectRegistry.getEntry( ObjectRegistry.getId( obj ) ).handler.isPublic === true;
  },

  getNativeEventSource : function( source, eventType ) {
    var SWT = rwt.scripting.SWT;
    var result;
    if( source.classname === "rwt.widgets.List" && eventType === "Selection" ) {
      result = source.getManager();
    } else {
      result = source;
    }
    return result;
  },

  getNativeEventType : function( source, eventType ) {
    var map = this._eventTypeMapping;
    var result;
    if( map[ source.classname ] && map[ source.classname ][ eventType ] ) {
      result = map[ source.classname ][ eventType ];
    } else {
      result = map[ "*" ][ eventType ];
    }
    return result;
  },

  wrapAsProto : function( object ) {
    this._wrapperHelper.prototype = object;
    var result = new this._wrapperHelper();
    this._wrapperHelper.prototype = null;
    return result;
  },

  postProcessEvent : function( event, wrappedEvent, originalEvent ) {
    var SWT = rwt.scripting.SWT;
    switch( event.type ) {
      case SWT.Verify:
        this._postProcessVerifyEvent( event, wrappedEvent, originalEvent );
      break;
      case SWT.KeyDown:
      case SWT.KeyUp:
        this._postProcessKeyEvent( event, wrappedEvent, originalEvent );
      break;
    }
  },

  initEvent : function( event, type, target, originalEvent ) {
    var SWT = rwt.scripting.SWT;
    event.widget = rwt.scripting.WidgetProxyFactory.getWidgetProxy( target );
    event.type = type;
    switch( type ) {
      case SWT.KeyDown:
      case SWT.KeyUp:
        this._initKeyEvent( event, originalEvent );
      break;
      case SWT.MouseDown:
      case SWT.MouseUp:
      case SWT.MouseMove:
      case SWT.MouseEnter:
      case SWT.MouseExit:
      case SWT.MouseDoubleClick:
        this._initMouseEvent( event, originalEvent );
      break;
      case SWT.Verify:
        this._initVerifyEvent( event, originalEvent );
      break;
      case SWT.Paint:
        this._initPaintEvent( event, target );
      break;
    }
  },

  _initKeyEvent : function( event, originalEvent ) {
    var charCode = originalEvent.getCharCode();
    var SWT = rwt.scripting.SWT;
    if( charCode !== 0 ) {
      event.character = String.fromCharCode( charCode );
      // TODO [tb] : keyCode will be off when character is not a-z
      event.keyCode = event.character.toLowerCase().charCodeAt( 0 );
    } else {
      var keyCode = this._getLastKeyCode();
      switch( keyCode ) {
        case 16:
          event.keyCode = SWT.SHIFT;
        break;
        case 17:
          event.keyCode = SWT.CTRL;
        break;
        case 18:
          event.keyCode = SWT.ALT;
        break;
        case 224:
          event.keyCode = SWT.COMMAND;
        break;
        default:
          event.keyCode = keyCode;
        break;
      }
    }
    this._setStateMask( event, originalEvent );
  },

  _initMouseEvent : function( event, originalEvent ) {
    var target = originalEvent.getTarget()._getTargetNode();
    var offset = rwt.html.Location.get( target, "scroll" );
    event.x = originalEvent.getPageX() - offset.left;
    event.y = originalEvent.getPageY() - offset.top;
    if( originalEvent.isLeftButtonPressed() ) {
      event.button = 1;
    } else if( originalEvent.isRightButtonPressed() ) {
      event.button = 3;
    } if( originalEvent.isMiddleButtonPressed() ) {
      event.button = 2;
    }
    this._setStateMask( event, originalEvent );
  },

  _initPaintEvent : function( event, target ) {
    var gc = this._getGCFor( target );
    event.gc = gc.getNativeContext();
  },

  _getGCFor : function( widget ) {
    var gc = widget.getUserData( rwt.scripting.WidgetProxyFactory._GC_KEY );
    if( gc == null ) {
      gc = this._findExistingGC( widget );
      if( gc == null ) {
        gc = new rwt.widgets.GC( widget );
      }
      widget.setUserData( rwt.scripting.WidgetProxyFactory._GC_KEY, gc );
    }
    return gc;
  },

  _findExistingGC : function( widget ) {
    var children = widget._getTargetNode().childNodes;
    var result = null;
    for( var i = 0; i < children.length && result == null; i++ ) {
      if( children[ i ].rwtObject instanceof rwt.widgets.GC ) {
        result = children[ i ].rwtObject;
      }
    }
    return result;
  },

  _initVerifyEvent : function( event, originalEvent ) {
    var text = originalEvent.getTarget();
    if( text.classname === "rwt.widgets.Text" ) {
      var keyCode = this._getLastKeyCode();
      var newValue = text.getComputedValue();
      var oldValue = text.getValue();
      var oldSelection = text.getSelection();
      var diff = this._getDiff( newValue, oldValue, oldSelection, keyCode );
      if(    diff[ 0 ].length === 1
          && diff[ 1 ] === diff[ 2 ]
          && diff[ 0 ] === originalEvent.getData()
      ) {
        event.keyCode = keyCode;
        event.character = diff[ 0 ];
      }
      event.text = diff[ 0 ];
      event.start = diff[ 1 ];
      event.end = diff[ 2 ];
    }
  },

  _postProcessVerifyEvent : function( event, wrappedEvent, originalEvent ) {
    var widget = originalEvent.getTarget();
    if( wrappedEvent.doit !== false ) {
      if( event.text !== wrappedEvent.text && event.text !== "" ) {
        // insert replacement text
        originalEvent.preventDefault();
        var currentText = widget.getValue();
        var textLeft = currentText.slice( 0, event.start );
        var textRight = currentText.slice( event.end, currentText.length );
        var carret = textLeft.length + wrappedEvent.text.length;
        widget.setValue( textLeft + wrappedEvent.text + textRight );
        widget.setSelection( [ carret, carret ] );
      }
    } else {
      // undo any change
      originalEvent.preventDefault();
      widget._renderValue();
      widget._renderSelection();
    }
  },

  _postProcessKeyEvent : function( event, wrappedEvent, originalEvent ) {
    if( wrappedEvent.doit === false ) {
      originalEvent.preventDefault();
    }
  },

  _setStateMask : function( event, originalEvent ) {
    var SWT = rwt.scripting.SWT;
    event.stateMask |= originalEvent.isShiftPressed() ? SWT.SHIFT : 0;
    event.stateMask |= originalEvent.isCtrlPressed() ? SWT.CTRL : 0;
    event.stateMask |= originalEvent.isAltPressed() ? SWT.ALT : 0;
    event.stateMask |= originalEvent.isMetaPressed() ? SWT.COMMAND : 0;
  },

  _getLastKeyCode : function() {
    // NOTE : While this is a private field, this mechanism must be integrated with
    // KeyEventSupport anyway to support the doit flag better.
    return rwt.remote.KeyEventSupport.getInstance()._currentKeyCode;
  },

  _getDiff : function( newValue, oldValue, oldSel, keyCode ) {
    var start;
    var end;
    var text;
    if( newValue.length >= oldValue.length || oldSel[ 0 ] !== oldSel[ 1 ] ) {
      start = oldSel[ 0 ];
      end = oldSel[ 1 ];
      text = newValue.slice( start, newValue.length - ( oldValue.length - oldSel[ 1 ] ) );
    } else {
      text = "";
      if(    oldSel[ 0 ] === oldSel[ 1 ]
          && keyCode === 8 // backspace
          && ( oldValue.length - 1 ) === newValue.length
      ) {
        start = oldSel[ 0 ] - 1;
        end = oldSel[ 0 ];
      } else {
        start = oldSel[ 0 ];
        end = start + oldValue.length - newValue.length;
      }
    }
    return [ text, start, end ];
  }

};

}());
