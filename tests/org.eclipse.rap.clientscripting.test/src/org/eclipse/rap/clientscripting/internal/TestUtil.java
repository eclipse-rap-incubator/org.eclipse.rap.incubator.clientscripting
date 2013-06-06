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
package org.eclipse.rap.clientscripting.internal;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Widget;


public class TestUtil {

  public static Connection fakeConnection( RemoteObject remoteObject, String remoteType ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( eq( remoteType ) ) ).thenReturn( remoteObject );
    Fixture.fakeConnection( connection );
    return connection;
  }

  public static ClientListenerBinding findBinding( ClientListener listener,
                                                   Widget widget,
                                                   int type )
  {
    ClientFunction function = listener; // the IDE does not see findBinding otherwise?
    return function.findBinding( WidgetUtil.getId( widget ), ClientListenerUtil.getEventType( type ) );
  }

}
