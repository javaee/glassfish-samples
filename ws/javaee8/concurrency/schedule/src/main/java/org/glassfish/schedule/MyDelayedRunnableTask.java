/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
