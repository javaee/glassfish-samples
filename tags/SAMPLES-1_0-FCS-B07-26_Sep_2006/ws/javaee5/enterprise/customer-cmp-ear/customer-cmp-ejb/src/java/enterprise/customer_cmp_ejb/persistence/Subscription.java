/*
 * Subscription.java
 *
 * Created on November 25, 2005, 11:01 AM
 *
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

/**
 *
 * @author Rahul Biswas
 */

package enterprise.customer_cmp_ejb.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.*;


@Entity
@NamedQuery(name="findAllSubscriptions", query="select s from Subscription s")

public  class Subscription implements java.io.Serializable{

    private String title;
    private String type;
    private Collection<Customer> customers;
    
    public Subscription(){
        
    }
    
    @Id
    public String getTitle(){ //primary key
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    //access methods for cmr fields
    @ManyToMany(mappedBy="subscriptions")
    public Collection<Customer> getCustomers(){
        return customers;
    }
    public void setCustomers(Collection<Customer> customers){
        this.customers=customers;
    }

    public Subscription (
            String title,
            String type) {

        if (type.equals(SubscriptionType.MAGAZINE)) {
            _create(title,SubscriptionType.MAGAZINE);
        } 
        else if (type.equals(SubscriptionType.JOURNAL)) {
            _create(title,SubscriptionType.JOURNAL);
        } 
        else if (type.equals(SubscriptionType.NEWS_PAPER)) {
            _create(title,SubscriptionType.NEWS_PAPER);
        }
        else
            _create(title,SubscriptionType.OTHER);

    }
    

    private String _create (
            String title,
            String type)  {
        

        setTitle(title);
        setType(type);
        return title;
    }
    
}


    
