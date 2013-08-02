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

  getGCFor : function( widget ) {
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
  }

};

}());
