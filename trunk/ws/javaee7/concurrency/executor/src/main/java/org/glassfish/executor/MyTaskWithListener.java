/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
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

import java.util.Map;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;

/**
 * @author Arun Gupta
 */
public class MyTaskWithListener implements Runnable, ManagedTask, ManagedTaskListener {
    
    private int id;

    public MyTaskWithListener() {
    }

    public MyTaskWithListener(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.printf("%d: running");
    }

    @Override
    public void taskSubmitted(Future<?> future, ManagedExecutorService mes, Object task) {
        System.out.printf("%d: submitted", id);
    }

    @Override
    public void taskAborted(Future<?> future, ManagedExecutorService mes, Object task, Throwable thrwbl) {
        System.out.printf("%d: aborted", id);
    }

    @Override
    public void taskDone(Future<?> future, ManagedExecutorService mes, Object task, Throwable thrwbl) {
        System.out.printf("%d: done", id);
    }

    @Override
    public void taskStarting(Future<?> future, ManagedExecutorService mes, Object task) {
        System.out.printf("%d: starting", id);
    }

    @Override
    public ManagedTaskListener getManagedTaskListener() {
        return this;
    }

    @Override
    public Map<String, String> getExecutionProperties() {
        return null;
    }
    
}
