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
package org.eclipse.rap.demo.clientscripting.internal;

import org.eclipse.rap.examples.IExampleContribution;
import org.eclipse.rap.examples.IExamplePage;


public class ValidationExampleContribution implements IExampleContribution {

  public String getId() {
    return "validation";
  }

  public String getTitle() {
    return "Input Validation";
  }

  public IExamplePage createPage() {
    return new ValidationExamplePage();
  }
}
