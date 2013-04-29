/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
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
import java.util.Arrays;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.client.WidgetDataWhiteListImpl;

@SuppressWarnings("restriction")
public class WidgetDataWhiteList {

  /**
   * Adds a string to the list of keys of widget data that are synchronized with the client.
   * It is save to add the same key twice, there are no side-effects. The data is only transferred from
   * server to client, not back. Widgets can currently not be transferred directly, use their
   * id instead, i.e.
   * <p><code>
   *   widget.setData( "otherWidget", WidgetUtil.getId( otherWidget ) );
   * </code></p>
   * and in JavaScript:
   * <p><code>
   *   var otherWidget = rap.getObject( widget.getData( "otherWidget" ) );
   * </code></p>
   * @see org.eclipse.swt.widgets.Widget#setData(String, Object)
   * @param string The key to add to the list.
   */
  public static void addKey( String string ) {
    WidgetDataWhiteListImpl service = getWhiteListService();
    List<String> list = getCurrentKeys( service );
    if( !list.contains( string ) ) {
      list.add( string );
      service.setKeys( list.toArray( new String[ list.size() ]) );
    }
  }

  private static List<String> getCurrentKeys( WidgetDataWhiteListImpl service ) {
    String[] currentKeys = service.getKeys() != null ? service.getKeys() : new String[ 0 ];
    return new ArrayList<String>( Arrays.asList( currentKeys ) );
  }

  private static WidgetDataWhiteListImpl getWhiteListService() {
    Client client = RWT.getClient();
    org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList service
      = client.getService( org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList.class );
    return ( WidgetDataWhiteListImpl )service;
  }
}
