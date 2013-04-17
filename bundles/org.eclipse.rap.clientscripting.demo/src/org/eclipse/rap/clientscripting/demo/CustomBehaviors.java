/*******************************************************************************
 * Copyright (c) 2013, 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.clientscripting.demo;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
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
    clientListener.addTo( text, ClientListener.Verify );
  }

  public static void addDigitsOnlyBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DigitsOnly.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.Modify );
  }

  public static void addDateFieldBehavior( Text text ) {
    text.setText( "__.__.____" );
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DateField.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.KeyDown );
    clientListener.addTo( text, ClientListener.Verify );
    clientListener.addTo( text, ClientListener.MouseUp );
    clientListener.addTo( text, ClientListener.MouseDown );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Counter.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.addTo( control, ClientListener.MouseDown );
  }

  public static void addLoggerBehavior( Widget widget ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Logger.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.addTo( widget, ClientListener.KeyDown );
    listener.addTo( widget, ClientListener.KeyUp );
    listener.addTo( widget, ClientListener.FocusIn );
    listener.addTo( widget, ClientListener.FocusOut );
    listener.addTo( widget, ClientListener.MouseDown );
    listener.addTo( widget, ClientListener.MouseUp );
    listener.addTo( widget, ClientListener.MouseEnter );
    listener.addTo( widget, ClientListener.MouseExit );
    listener.addTo( widget, ClientListener.MouseDoubleClick );
    listener.addTo( widget, ClientListener.Selection );
    listener.addTo( widget, ClientListener.DefaultSelection );
    listener.addTo( widget, ClientListener.Show );
    listener.addTo( widget, ClientListener.Hide );
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
    listener.addTo( canvas, ClientListener.MouseMove );
    listener.addTo( canvas, ClientListener.Paint );
    canvas.setForeground( getRandomColor( canvas.getDisplay() ) );
    canvas.redraw();
  }

  public static Color getRandomColor( Device device ) {
    RGB rgb = new RGB(
      ( int )Math.round(  Math.random() * 255 ),
      ( int )Math.round(  Math.random() * 255 ),
      ( int )Math.round(  Math.random() * 255 )
    );
    return new Color( device, rgb );
  }

}
