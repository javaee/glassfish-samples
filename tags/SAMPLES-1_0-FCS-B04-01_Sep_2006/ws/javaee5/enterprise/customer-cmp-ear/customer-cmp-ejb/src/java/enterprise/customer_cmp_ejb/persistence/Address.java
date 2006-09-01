/*
 * Address.java
 *
 * Created on November 24, 2005, 7:02 PM
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

/**
 *
 * @author Rahul Biswas
 */

package enterprise.customer_cmp_ejb.persistence;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.*;


@Entity
//name defaults to the unqualified entity class name.        
//default access is property.
    
public class Address implements java.io.Serializable{
    
    private String addressID;
    private String street;
    private String city;
    private String zip;
    private String state;
    
    public Address(String id, String street, String city, String zip, String state){

        setAddressID(id);
        setStreet(street);
        setCity(city);
        setZip(zip);
        setState(state);

    }
    
    public Address(){
    
    }
    
    @Id //this is the default 
    @Column(name="addressID")
    public String getAddressID(){      //primary key
        return addressID;
    }
    public void setAddressID(String id){
        this.addressID=id;
    }
    
    //will default to Street
    public String getStreet(){
        return street;
    }
    public void setStreet(String street){
        this.street=street;
    }

    //will default to city
    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city=city;
    }

    //will default to zip
    public String getZip(){
        return zip;
    }
    public void setZip(String zip){
        this.zip=zip;
    }

    //will default to state
    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
    }
    
    
}
