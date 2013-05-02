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

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;


public class Demo extends AbstractEntryPoint {

  @Override
  protected void createContents( Composite parent ) {
    parent.setLayout( new GridLayout( 2, false ) );
    addUpperCaseExample( parent );
    addDigitsOnlyExample( parent );
    addDateFieldExample( parent );
    addCounterExample( parent );
    addCanvasExample( parent );
    addListExample( parent );
  }

  private void addUpperCaseExample( Composite parent ) {
    addHeaderLabel( parent, "Auto upper case text field:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addUpperCaseBehavior( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addDigitsOnlyExample( Composite parent ) {
    addHeaderLabel( parent, "Digits only, validation on client:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addDigitsOnlyBehavior( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addDateFieldExample( Composite parent ) {
    addHeaderLabel( parent, "A simple date field, validation on server:" );
    final Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addDateFieldBehavior( text );
    addDateValidator( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addCounterExample( Composite parent ) {
    addHeaderLabel( parent, "A button that counts:" );
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Click me!" );
    button.setLayoutData( new GridData( 120, SWT.DEFAULT ) );
    CustomBehaviors.addCounterBehavior( button );
  }

  private void addCanvasExample( Composite parent ) {
    addHeaderLabel( parent, "Canvas:" );
    Canvas canvas = new Canvas( parent, SWT.BORDER );
    canvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    CustomBehaviors.addPaintingBehavior( canvas );
  }

  private void addListExample( Composite parent ) {
    addHeaderLabel( parent, "List:" );
    List list = new List( parent, SWT.BORDER | SWT.MULTI );
    list.setLayoutData( new GridData( SWT.FILL, SWT.TOP, false, false) );
    list.setItems( new String[]{ "Look", "in", "your", "javascript", "console" } );
    CustomBehaviors.addLoggerBehavior( list );
    Menu popup = new Menu( list );
    new MenuItem( popup, SWT.PUSH ).setText( "Item 1" );
    new MenuItem( popup, SWT.PUSH ).setText( "Item 2" );
    list.setMenu( popup );
    CustomBehaviors.addLoggerBehavior( popup );
  }

  private static void addHeaderLabel( Composite parent, String text ) {
    Label label = new Label( parent, SWT.NONE );
    label.setText( text );
    GridData layoutData = new GridData();
    layoutData.verticalIndent = 10;
    label.setLayoutData( layoutData );
  }

  private void addDateValidator( final Text text ) {
    text.addFocusListener( new FocusAdapter() {
      Color color = new Color( text.getDisplay(), 255, 128, 128 );
      @Override
      public void focusLost( FocusEvent event ) {
        if( !verifyDate( text.getText() ) ) {
          text.setBackground( color );
        } else {
          text.setBackground( null );
        }
      }
    } );
  }

  private boolean verifyDate( String date ) {
    String[] values = date.split( "\\.", 3 );
    boolean valid = true;
    try {
      if( Integer.parseInt( values[ 0 ] ) > 31 ) {
        valid = false;
      }
      if( Integer.parseInt( values[ 1 ] ) > 12 ) {
        valid = false;
      }
      Integer.parseInt( values[ 2 ].trim() ); // remove trailing " "
    } catch( NumberFormatException ex ) {
      valid = false;
    }
    return valid;
  }

}
