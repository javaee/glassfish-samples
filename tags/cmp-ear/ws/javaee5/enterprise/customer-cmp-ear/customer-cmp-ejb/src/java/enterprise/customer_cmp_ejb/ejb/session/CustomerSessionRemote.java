/*
 * CustomerSessionLocal.java
 *
 * Created on January 16, 2006, 6:43 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 *
 * @author Rahul Biswas
 */

package enterprise.customer_cmp_ejb.ejb.session;

import javax.ejb.Remote;
import enterprise.customer_cmp_ejb.persistence.*;
import enterprise.customer_cmp_ejb.common.*;

import java.util.List;

@Remote
public interface CustomerSessionRemote {
    
    public Customer searchForCustomer(String id);
    
    public Subscription searchForSubscription(String id);
    
    public Address searchForAddress(String id);
    
    public void persist(Object obj);
    
    public List findAllSubscriptions();
    
    public List findCustomerByFirstName(String firstName);
    
    public List findCustomerByLastName(String lastName);
    
    public void remove(Object obj);
    
    public Customer addCustomerAddress(Customer cust, Address address); 

    public Customer removeCustomerSubscription(String cust, String subs) throws SubscriptionNotFoundException;
    
    public Customer addCustomerSubscription(String cust, String subs) throws DuplicateSubscriptionException;
}
