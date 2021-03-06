/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.tools;

public class ModuleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ModuleNotFoundException() {
        super();
    }

    public ModuleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(Throwable cause) {
        super(cause);
    }

}
