/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Defaulted;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(extendsType = "ceylon.language::Throwable")
@SharedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public class AssertionError extends java.lang.Error implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(AssertionError.class);

    private static final long serialVersionUID = -1790691559137471641L;

    @Ignore // JPA constructor
    protected AssertionError() {
        this.message = null;
    }
    
    @Ignore
    private final java.lang.String message;
    
    @Ignore
    public AssertionError(
            @TypeInfo("ceylon.language::String")
            @Name("message")
            @NonNull
            java.lang.String message) {
        super(message);
        this.message = message;
    }
    
    @Ignore
    public static java.lang.Throwable $init$cause() {
        return null;
    }
    
    public AssertionError(
            @TypeInfo("ceylon.language::String")
            @Name("message")
            @NonNull
            java.lang.String message,
            @TypeInfo("ceylon.language::Throwable?")
            @Name("cause")
            @Defaulted
            @Nullable
            java.lang.Throwable cause) {
        super(message, cause);
        this.message = message;
    }
        
    @Override
    @Ignore
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @Override
    @Ignore
    public java.lang.String getMessage() {
        return message;
    }

    @Override
    @Ignore
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    @Override
    @Ignore
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Ignore
    public static String $default$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $default$cause(java.lang.String description){
        return null;
    }

    //@Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
