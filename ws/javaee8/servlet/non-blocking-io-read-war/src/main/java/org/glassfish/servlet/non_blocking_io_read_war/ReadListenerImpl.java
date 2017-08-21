/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.servlet.non_blocking_io_read_war;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

/**
 * This is the class to illustrate the implementation of ReadListener
 * @author Daniel Guo
 */
public class ReadListenerImpl implements ReadListener {

    private ServletInputStream input;
    private ServletOutputStream output;
    private AsyncContext context;
    private StringBuilder sb = new StringBuilder();

    ReadListenerImpl(ServletInputStream input, ServletOutputStream output, AsyncContext context) {
        this.input = input;
        this.output = output;
        this.context = context;
    }

    /*
     * This method illustrates what ReadListener will do when data 
     * is available to be read.
     */
    @Override
    public void onDataAvailable() throws IOException {
        System.out.println("Data is available");
        while (input.isReady() && !input.isFinished()) {
            sb.append((char) input.read());
        }
        sb.append(" ");
    }

    /*
     * This method illustrates what ReadListender will do 
     * when all the data has been read. 
     */
    @Override
    public void onAllDataRead() throws IOException {
        try {
            output.print("Echo the reverse String from server: " + sb.reverse().toString() + "</br>");
            output.flush();
        } finally {
            context.complete();
        }
        System.out.println("Data is all read");
    }

    /*
     * This method illustrates what ReadListender will do 
     * when error occurs. 
     */
    @Override
    public void onError(Throwable t) {
        context.complete();
        System.out.println("--> onError");
    }
}
