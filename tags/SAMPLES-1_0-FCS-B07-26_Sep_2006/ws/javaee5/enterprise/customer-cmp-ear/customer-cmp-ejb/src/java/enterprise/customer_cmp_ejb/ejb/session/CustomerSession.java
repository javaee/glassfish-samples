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
