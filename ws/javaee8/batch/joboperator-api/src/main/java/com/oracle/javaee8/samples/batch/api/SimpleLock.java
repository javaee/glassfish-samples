package com.oracle.javaee8.samples.batch.api;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: makannan
 * Date: 4/1/13
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
@SessionScoped
public class SimpleLock
    implements Serializable {

    @Override
    public String toString() {
        return "SimpleLock{" +
                System.identityHashCode(this) +
                '}';
    }

}
