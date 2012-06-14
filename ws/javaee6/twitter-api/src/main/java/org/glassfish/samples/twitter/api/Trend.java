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
public class Trend {
    private String name;
    private String url;
    private String query;
    private String promoted_content;
    private String events;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getPromoted_content() {
        return promoted_content;
    }

    public void setPromoted_content(String promoted_content) {
        this.promoted_content = promoted_content;
    }
    
    
}
