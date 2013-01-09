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
package org.eclipse.rap.clientscripting.internal;

import org.eclipse.rap.clientscripting.ClientListener;


public class ClientListenerUtil {

  public static String getEventType( int bindingType ) {
    String result = null;
    switch( bindingType ) {
      case ClientListener.KeyUp:
        result = "KeyUp";
      break;
      case ClientListener.KeyDown:
        result = "KeyDown";
      break;
      case ClientListener.FocusIn:
        result = "FocusIn";
      break;
      case ClientListener.FocusOut:
        result = "FocusOut";
      break;
      case ClientListener.MouseDown:
        result = "MouseDown";
      break;
      case ClientListener.MouseUp:
        result = "MouseUp";
      break;
      case ClientListener.MouseEnter:
        result = "MouseEnter";
      break;
      case ClientListener.MouseExit:
        result = "MouseExit";
      break;
      case ClientListener.MouseMove:
        result = "MouseMove";
      break;
      case ClientListener.MouseDoubleClick:
        result = "MouseDoubleClick";
      break;
      case ClientListener.Modify:
        result = "Modify";
      break;
      case ClientListener.Verify:
        result = "Verify";
      break;
      case ClientListener.Paint:
        result = "Paint";
      break;
    }
    if( result == null ) {
      throw new IllegalArgumentException( "Unknown Event Type " + bindingType );
    }
    return result;
  }

}
