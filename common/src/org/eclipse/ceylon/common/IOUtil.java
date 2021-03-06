/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common;

import java.io.IOException;
import java.io.InputStream;

/**
 * Deals with IO
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class IOUtil {
    
    public static byte[] readStream(InputStream stream) throws IOException{
        try{
            byte[] ret = new byte[4096];
            int offset = 0;
            int read = 0;
            while((read = stream.read(ret, offset, ret.length-offset)) != -1){
                offset += read;
                if(offset == ret.length){
                    byte[] grown = new byte[ret.length+4096];
                    System.arraycopy(ret, 0, grown, 0, ret.length);
                    ret = grown;
                }
            }
            
            // now make a perfect fit array if not perfect
            if(offset != ret.length){
                byte[] fit = new byte[offset];
                System.arraycopy(ret, 0, fit, 0, offset);
                return fit;
            }
            return ret;
        }finally{
            stream.close();
        }
    }
}
