/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.twitter.api;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author arungup
 */
@XmlRootElement
@Named
public class Trends {
    private Locations[] locations;
    private String as_of;
    private Trend[] trends;
    private String created_at;

    public String getAs_of() {
        return as_of;
    }

    public void setAs_of(String as_of) {
        this.as_of = as_of;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Locations[] getLocations() {
        return locations;
    }

    public void setLocations(Locations[] locations) {
        this.locations = locations;
    }

    public Trend[] getTrends() {
        return trends;
    }

    public void setTrends(Trend[] trends) {
        this.trends = trends;
    }
    
    
}
