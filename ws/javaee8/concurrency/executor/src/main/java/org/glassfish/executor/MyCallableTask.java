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
package org.glassfish.executor;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Arun Gupta
 */
public class MyCallableTask implements Callable<Product> {

    private int id;
    
    public MyCallableTask(int id) {
        this.id = id;
    }
    
    @Override
    public Product call() {
        try {
            System.out.format("%d (callable): starting", id);
            String moduleName = null;
            try {
                // Try doing JNDI lookup inside a task
                Context ctx = new InitialContext();
                moduleName = (String) ctx.lookup("java:module/ModuleName");
            } catch (NamingException ex) {
                Logger.getLogger(MyCallableTask.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.format("%d (callable): Module name is %s. sleeping 2 seconds", id, moduleName);
            Thread.sleep(2000);
            System.out.format("%d (callable): complete", id);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestResourceServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Product(id);
    }
}
