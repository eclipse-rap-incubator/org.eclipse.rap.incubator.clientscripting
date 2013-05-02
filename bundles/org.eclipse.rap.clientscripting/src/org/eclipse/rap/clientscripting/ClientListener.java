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
package org.eclipse.rap.clientscripting;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.rap.clientscripting.internal.resources.ClientScriptingResources;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;


/**
 * A special SWT event listener that is implemented in JavaScript and will be executed on a RAP
 * client. The handleEvent() method of this method will never be called.
 */
public class ClientListener implements Listener {

  private static final String REMOTE_TYPE = "rwt.clientscripting.Listener";

  public static final int KeyDown = SWT.KeyDown;
  public static final int KeyUp = SWT.KeyUp;
  public static final int MouseDown = SWT.MouseDown;
  public static final int MouseUp = SWT.MouseUp;
  public static final int MouseMove = 5;
  public static final int MouseEnter = 6;
  public static final int MouseExit = 7;
  public static final int MouseDoubleClick = SWT.MouseDoubleClick;
  /**
   * <strong>Warning: Paint even is only supported on Canvas.</strong>
   * Using it on other widgets may produce unexpected results.
   */
  public static final int Paint = 9;
  /**
   * Currently only supported on List
   */
  public static final int Selection = SWT.Selection;
  /**
   * Currently only supported on List
   */
  public static final int DefaultSelection = SWT.DefaultSelection;
  public static final int FocusIn = SWT.FocusIn;
  public static final int FocusOut = SWT.FocusOut;
  public static final int Show = SWT.Show;
  public static final int Hide = SWT.Hide;
  public static final int Modify = SWT.Modify;
  public static final int Verify = SWT.Verify;

  private final RemoteObject remoteObject;
  private final Collection<ClientListenerBinding> bindings;

  /**
   * Creates an instance of ClientListener with the specified JavaScript code. The JavaScript code
   * is supposed to have a method named <code>handleEvent</code>. This method will be called with a
   * single argument, <code>event</event>.
   *
   * @param scriptCode the JavaScript code of the event handler
   */
  public ClientListener( String scriptCode ) {
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
    ClientScriptingResources.ensure();
    bindings = new ArrayList<ClientListenerBinding>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "code", scriptCode );
  }

  /**
   * This method will NOT be called on a ClientListener.
   */
  public void handleEvent( Event event ) {
  }

  /**
   * @deprecated Use {@link Widget#addListener(int, Listener)} instead
   */
  @Deprecated
  public void addTo( Widget widget, int eventType ) {
    if( widget == null ) {
      throw new NullPointerException( "widget is null" );
    }
    if( widget.isDisposed() ) {
      throw new IllegalArgumentException( "Widget is disposed" );
    }
    ClientListenerBinding binding = new ClientListenerBinding( this, widget, eventType );
    addBinding( binding );
  }

  /**
   * @deprecated Use {@link Widget#removeListener(int, Listener)} instead
   */
  @Deprecated
  public void removeFrom( Widget widget, int eventType ) {
    if( widget == null ) {
      throw new NullPointerException( "widget is null" );
    }
    ClientListenerBinding binding = findBinding( widget, eventType );
    if( binding != null ) {
      binding.dispose();
    }
  }

  /**
   * @deprecated ClientListeners do not need to be disposed anymore.
   */
  @Deprecated
  public void dispose() {
  }

  /**
   * @deprecated ClientListeners do not need to be disposed anymore.
   */
  @Deprecated
  public boolean isDisposed() {
    return false;
  }

  String getRemoteId() {
    return remoteObject.getId();
  }

  Collection<ClientListenerBinding> getBindings() {
    return bindings;
  }

  ClientListenerBinding findBinding( Widget widget, int eventType ) {
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getWidget() == widget && binding.getEventType() == eventType ) {
        return binding;
      }
    }
    return null;
  }

  private void addBinding( final ClientListenerBinding binding ) {
    if( !bindings.contains( binding ) ) {
      bindings.add( binding );
      binding.getWidget().addDisposeListener( new DisposeListener() {
        public void widgetDisposed( DisposeEvent event ) {
          binding.dispose();
        }
      } );
    }
  }

}
