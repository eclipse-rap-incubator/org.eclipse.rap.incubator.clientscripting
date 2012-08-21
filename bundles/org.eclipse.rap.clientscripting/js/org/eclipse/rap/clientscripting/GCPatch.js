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


qx.Mixin.define( "org.eclipse.rap.clientscripting.GCPatch", {

  construct : function() {
    this._canvas.rwtObject = this;
    this._textCanvas.rwtObject = this;
    this._elements = [ this._canvas, this._textCanvas ]; // only for destructor
  },

  destruct : function() {
    this._elements[ 0 ].rwtObject = null;
    this._elements[ 1 ].rwtObject = null;
    this._elements.length = 0;
    this._elements = null;
  },

  members : {
  }

} );

qx.Class.__initializeClass( org.eclipse.swt.graphics.GC );
qx.Class.patch( org.eclipse.swt.graphics.GC, org.eclipse.rap.clientscripting.GCPatch );