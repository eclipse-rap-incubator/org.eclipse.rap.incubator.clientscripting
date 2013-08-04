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
package org.eclipse.rap.clientscripting.demo;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.WidgetDataWhiteList;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


public class CustomBehaviors {

  private static final String RESOURCES_PREFIX = "org/eclipse/rap/clientscripting/demo/";

  private CustomBehaviors() {
    // prevent instantiation
  }

  public static void addUpperCaseBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "UpperCase.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    text.addListener( SWT.Verify, clientListener );
  }

  public static void addDigitsOnlyBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DigitsOnly.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    text.addListener( SWT.Modify, clientListener );
  }

  public static void addDateFieldBehavior( Text text ) {
    text.setText( "__.__.____" );
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DateField.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    text.addListener( SWT.KeyDown, clientListener );
    text.addListener( SWT.Verify, clientListener );
    text.addListener( SWT.MouseUp, clientListener );
    text.addListener( SWT.MouseDown, clientListener );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Counter.js" );
    ClientListener listener = new ClientListener( scriptCode );
    control.addListener( SWT.MouseDown, listener );
  }

  public static void addLoggerBehavior( Widget widget ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Logger.js" );
    ClientListener listener = new ClientListener( scriptCode );
    widget.addListener( SWT.KeyDown, listener );
    widget.addListener( SWT.KeyUp, listener );
    widget.addListener( SWT.FocusIn, listener );
    widget.addListener( SWT.FocusOut, listener );
    widget.addListener( SWT.MouseDown, listener );
    widget.addListener( SWT.MouseUp, listener );
    widget.addListener( ClientListener.MouseEnter, listener );
    widget.addListener( ClientListener.MouseExit, listener );
    widget.addListener( SWT.MouseDoubleClick, listener );
    widget.addListener( SWT.Selection, listener );
    widget.addListener( SWT.DefaultSelection, listener );
    widget.addListener( SWT.Show, listener );
    widget.addListener( SWT.Hide, listener );
  }

  public static void addPaintingBehavior( final Canvas canvas ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Painting.js" );
    ClientListener listener = new ClientListener( scriptCode );
    canvas.addMouseListener( new MouseAdapter() {
      @Override
      public void mouseUp( MouseEvent e ) {
        canvas.setForeground( getRandomColor( canvas.getDisplay() ) );
        canvas.redraw();
      }
    } );
    canvas.addListener( ClientListener.MouseMove, listener );
    canvas.addListener( ClientListener.Paint, listener );
    canvas.setForeground( getRandomColor( canvas.getDisplay() ) );
    canvas.redraw();
  }


  public static void addFocusNextBehavior( Text text, Control next ) {
    text.setTextLimit( 4 );
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "FocusSwitch.js" );
    ClientListener listener = new ClientListener( scriptCode );
    text.addListener( SWT.Modify, listener );
    WidgetDataWhiteList.addKey( "next" );
    text.setData( "next", WidgetUtil.getId( next ) );
  }

  public static void addFocusPreviousBehavior( Text text, Control previous ) {
    text.setTextLimit( 4 );
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "FocusSwitch.js" );
    ClientListener listener = new ClientListener( scriptCode );
    text.addListener( SWT.Modify, listener );
    WidgetDataWhiteList.addKey( "previous" );
    text.setData( "previous", WidgetUtil.getId( previous ) );
  }

  public static Color getRandomColor( Device device ) {
    RGB rgb = new RGB(
      ( int )Math.round(  Math.random() * 255 ),
      ( int )Math.round(  Math.random() * 255 ),
      ( int )Math.round(  Math.random() * 255 )
    );
    return new Color( device, rgb );
  }

  public static void addNumKeyBehavior( Text text, int number, Button button ) {
    button.setData( "textWidget", WidgetUtil.getId( text ) );
    button.setData( "numValue", Integer.valueOf( number ) );
    ensure( "NumKey.js" );
    button.addListener( SWT.MouseDown, new ClientListener( "var handleEvent = demoscripts.numKey;") );
  }

  private static void ensure( String fileName ) {
    String path = RESOURCES_PREFIX + fileName;
    InputStream stream = CustomBehaviors.class.getClassLoader().getResourceAsStream( path );
    ResourceManager manager = RWT.getResourceManager();
    manager.register( path, stream );
    try {
      stream.close();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    JavaScriptLoader jsl = RWT.getClient().getService( JavaScriptLoader.class );
    jsl.require( manager.getLocation( path ) );
  }

}
