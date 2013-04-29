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


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.client.WidgetDataWhiteListImpl;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings( "restriction" )
public class WidgetDataWhiteList_Test {

  private WidgetDataWhiteListImpl whiteList;

  @Before
  public void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakeNewRequest();
    Client client = mock( Client.class );
    whiteList = mock( WidgetDataWhiteListImpl.class );
    Class<org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList> type
      = org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList.class;
    stub( client.getService( eq( type ) ) ).toReturn( whiteList );
    Fixture.fakeClient( client );
  }

  @Test
  public void testAddKey_Once() {
    WidgetDataWhiteList.addKey( "foo" );

    verify( whiteList ).setKeys( eq( new String[] { "foo" } ) );
  }

  @Test
  public void testAddKey_DoNothingIfAlreadyInList() {
    stub( whiteList.getKeys() ).toReturn( new String[] { "foo" } );

    WidgetDataWhiteList.addKey( "foo" );

    verify( whiteList, never() ).setKeys( any( String[].class ) );
  }

  @Test
  public void testAddKey_DoNotOverwriteExistingKeys() {
    stub( whiteList.getKeys() ).toReturn( new String[] { "bar" } );

    WidgetDataWhiteList.addKey( "foo" );

    verify( whiteList ).setKeys( eq( new String[]{ "bar", "foo" } ) );
  }

}
