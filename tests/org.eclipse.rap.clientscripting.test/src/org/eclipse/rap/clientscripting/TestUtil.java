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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.testfixture.Fixture;


@SuppressWarnings( "restriction" )
public class TestUtil {

  static RemoteObjectFactory fakeRemoteObjectFactory( RemoteObject remoteObject ) {
    RemoteObjectFactory factory = mock( RemoteObjectFactory.class );
    when( factory.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    Fixture.fakeRemoteObjectFactory( factory );
    return factory;
  }

}
