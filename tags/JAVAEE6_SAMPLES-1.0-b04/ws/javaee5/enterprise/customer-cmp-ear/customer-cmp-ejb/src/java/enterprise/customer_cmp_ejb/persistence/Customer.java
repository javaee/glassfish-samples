/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

/*
 * Customer.java
 *
 * Created on November 24, 2005, 7:40 PM
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
import javax.persistence.OneToMany;



@Entity
@NamedQueries(
    value={@NamedQuery(name="findCustomerByFirstName", query="select object(c) from Customer c where c.firstName= :firstName"),
    @NamedQuery(name="findCustomerByLastName", query="select object(c) from Customer c where c.lastName= :lastName")}
)

public class Customer implements java.io.Serializable{

    //access methods for cmp fields
    private String id;
    private String firstName;
    private String lastName;
    private Collection<Address> addresses;
    private Collection<Subscription> subscriptions;
    
    public Customer(){
        
    }
    
    @Id
    @Column(name="customerid")
    public String getCustomerID(){      //primary key
        return id;
    }
    public void setCustomerID(String id){
        this.id=id;
    }
    
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public Customer(String id, String firstName, String lastName) {          
        setCustomerID(id);
        setFirstName(firstName);
        setLastName(lastName);
    }


    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    //targetEntity not needed as the property is a generic java data type
    //cascade will default to none, needs to be ALL because we need to save newly added addresses
    //fetch specified as EAGER, because we need to access the association outside of a txn.
    //mapped by is not needed because this is a unidirectional mapping
    public Collection<Address> getAddresses(){
        return addresses;
    }
    public void setAddresses (Collection<Address> addresses){
        this.addresses=addresses;
    }

    @ManyToMany(fetch=FetchType.EAGER )
    @JoinTable(
            name="CUSTOMERBEANSUBSCRIPTIONBEAN",
            joinColumns=@JoinColumn(name="CUSTOMERBEAN_CUSTOMERID96", referencedColumnName="customerid"), 
            inverseJoinColumns=@JoinColumn(name="SUBSCRIPTIONBEAN_TITLE", referencedColumnName="TITLE") 
    )
    public Collection<Subscription> getSubscriptions(){
        return subscriptions;
    }
    public void setSubscriptions (Collection<Subscription> subscriptions){
        this.subscriptions=subscriptions;
    }

    //business methods
    //We could have as well used a java.util.List for a collection of addresses. 
    //But what's below is only for demonstration purposes and the use of Collection instead of List is for that purpose only.
    @Transient
    //since the signature starts with a get, need to annotate it as @Transient
    public ArrayList getAddressList() {
        ArrayList list = new ArrayList();
        Iterator c = getAddresses().iterator();
        while (c.hasNext()) {
            list.add((Address)c.next());
        }
        return list;
    }

    @Transient
    public ArrayList getSubscriptionList() {
        ArrayList list = new ArrayList();
        Iterator c = getSubscriptions().iterator();
        while (c.hasNext()) {
            list.add((Subscription)c.next());
        }
        return list;
    }

    
    // other EntityBean methods
    @PostPersist
    public void postCreate (){
        System.out.println("Customer::postCreate:");
    }


    
    @PostRemove
    public void ejbRemove() {
        System.out.println("Customer::postRemove");
    }
    

}
