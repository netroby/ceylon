/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.util;

import java.util.Comparator;

import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;

public class TypeComparator implements Comparator<Type> {

    @Override
    public int compare(Type t1, Type t2) {
        if (ModelUtil.isTypeUnknown(t1)) {
            return ModelUtil.isTypeUnknown(t2) ? 0 : -1;
        }
        if (ModelUtil.isTypeUnknown(t2)) {
            return ModelUtil.isTypeUnknown(t1) ? 0 : -1;
        }
        if (t1.isSubtypeOf(t2)) {
            return 1;
        }
        if (t2.isSubtypeOf(t1)) {
            return -1;
        }
        //Check the members
        for (Declaration d : t1.getDeclaration().getMembers()) {
            if (d instanceof TypedDeclaration || d instanceof ClassOrInterface) {
                Declaration d2 = t2.getDeclaration().getMember(d.getName(), null, false);
                if (d2 != null) {
                    final Declaration dd2 = ModelUtil.getContainingDeclaration(d2);
                    if (dd2 instanceof TypeDeclaration && t1.getDeclaration().inherits((TypeDeclaration)dd2)) {
                        return 1;
                    }
                }
            }
        }
        for (Declaration d : t2.getDeclaration().getMembers()) {
            if (d instanceof TypedDeclaration || d instanceof ClassOrInterface) {
                Declaration d2 = t1.getDeclaration().getMember(d.getName(), null, false);
                if (d2 != null) {
                    final Declaration dd2 = ModelUtil.getContainingDeclaration(d2);
                    if (dd2 instanceof TypeDeclaration && t2.getDeclaration().inherits((TypeDeclaration)dd2)) {
                        return -1;
                    }
                }
            }
        }
        return 0;
    }

}
