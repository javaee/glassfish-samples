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
package org.glassfish.schedule;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;


public class MyDelayedRunnableTask implements Runnable, ManagedTask, ManagedTaskListener {

    private int id;
    private int startCount = 0;

    public MyDelayedRunnableTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.printf("%d: Running DelayedRunnable Task at %s", id, new Date(System.currentTimeMillis()));
        try {
            // sleep for 2 seconds
            // This would affect invocation time when scheduled using
            // scheduledWithFixedDelay but not scheduledAtFixedRate
            Thread.sleep(2000);             
        } catch (InterruptedException ex) {
            Logger.getLogger(MyDelayedRunnableTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void taskSubmitted(Future<?> future, ManagedExecutorService mes, Object o) {
    }

    @Override
    public void taskAborted(Future<?> future, ManagedExecutorService mes, Object o, Throwable thrwbl) {
    }

    @Override
    public void taskDone(Future<?> future, ManagedExecutorService mes, Object o, Throwable thrwbl) {
    }

    @Override
    public void taskStarting(Future<?> future, ManagedExecutorService mes, Object o) {
        // cancel task after 3 starts
        if (++startCount > 3) {
            future.cancel(true);
            System.out.println("cancelled periodic task");
        }
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
