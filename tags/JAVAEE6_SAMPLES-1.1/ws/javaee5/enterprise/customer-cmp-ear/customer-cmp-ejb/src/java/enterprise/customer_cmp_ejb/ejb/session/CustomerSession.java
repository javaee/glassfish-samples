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
 * CustomerSession.java
 *
 * Created on January 16, 2006, 6:38 PM
 *
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

package enterprise.customer_cmp_ejb.ejb.session;

import javax.ejb.Stateless;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.persistence.*;
import javax.ejb.*;
import java.util.List;

import enterprise.customer_cmp_ejb.persistence.*;
import enterprise.customer_cmp_ejb.common.*;
/**
 *
 * @author Rahul Biswas
 *
 * Why a facade?  
 * 1. session beans are thread safe, and EMs are not necessarily; so injecting a EM into a SessionBean makes it safe. 
 * 2. Tx management is taken care of by container
 * 3. of course, because it's a facade [we can combine operations].
 * 
 */
@Stateless
@TransactionManagement(value=TransactionManagementType.CONTAINER)

public class CustomerSession implements CustomerSessionLocal, CustomerSessionRemote{
    
    @javax.persistence.PersistenceContext(unitName="persistence_sample")
    private EntityManager em ;
    
    
    public CustomerSession(){
        
    }

    public Customer searchForCustomer(String id){
        
        Customer cust = (Customer)em.find(Customer.class, id);
        return cust;
    }
    
    
    public Subscription searchForSubscription(String id){
        
        Subscription subscription = (Subscription)em.find(Subscription.class, id);
        return subscription;
    }
    
    public Address searchForAddress(String id){
        
        Address address = (Address)em.find(Address.class, id);
        return address;
        
    }
    
    //This is the default; here as an example of @TransactionAttribute
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void remove(Object obj){
        Object mergedObj = em.merge(obj);
        em.remove(mergedObj);
    }
    
    public void persist(Object obj){
        em.persist(obj);
    }
    
    public List findAllSubscriptions(){
    
        List subscriptions = em.createNamedQuery("findAllSubscriptions").getResultList();
        return subscriptions;
        
    }
    
    public List findCustomerByFirstName(String firstName){
        List customers = em.createNamedQuery("findCustomerByFirstName").setParameter("firstName", firstName).getResultList();
        return customers;
    }
    
    public List findCustomerByLastName(String lastName){
        List customers = em.createNamedQuery("findCustomerByLastName").setParameter("lastName", lastName).getResultList();
        return customers;
    }
    
    
    public Customer addCustomerAddress(Customer cust, Address address){
        
        Customer mergedCust = em.merge(cust);
        mergedCust.getAddresses().add(address);
        return mergedCust;
        
    }
    
    public Customer removeCustomerSubscription(String cust, String subs) throws SubscriptionNotFoundException{
        
        //System.out.println("called remove Customer Subscription.....");
        
        Customer customer = (Customer)em.find(Customer.class, cust);
        Subscription subscription = (Subscription)em.find(Subscription.class, subs);
        
        if(!customer.getSubscriptions().contains(subscription)){
            System.out.println("remove: did not find a subscription obj for :"+subscription.getTitle());
            throw new SubscriptionNotFoundException();
        }
        
        customer.getSubscriptions().remove(subscription);
        subscription.getCustomers().remove(customer);
        
        return customer;
    }
    
    public Customer addCustomerSubscription(String cust, String subs) throws DuplicateSubscriptionException{
        
        //System.out.println("called add Customer Subscription.....");
        Customer customer = (Customer)em.find(Customer.class, cust);
        Subscription subscription = (Subscription)em.find(Subscription.class, subs);
        
        if(customer.getSubscriptions().contains(subscription)){
            System.out.println("add: found an existing subscription obj for :"+subscription.getTitle());
            throw new DuplicateSubscriptionException();
        }
        
        customer.getSubscriptions().add(subscription);
        subscription.getCustomers().add(customer);
        
        return customer;
        
    }
    
}
