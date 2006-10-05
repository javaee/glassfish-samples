/*
 * CustomerCMPEarTest.java
 *
 * Created on April 5, 2006, 3:11 PM
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "LICENSE"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
 * CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
 * PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
 * NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
 * SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
 * SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
 * PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES").  SUN
 * SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
 * HIGH RISK ACTIVITIES.
 */

/**
 *
 * @author Rahul Biswas
 */

package enterprise.customer_cmp_ear_test;

import junit.framework.Assert;
import org.junit.*;

import javax.naming.InitialContext;
import javax.ejb.EJB;
import enterprise.customer_cmp_ejb.ejb.session.CustomerSessionRemote;
import enterprise.customer_cmp_ejb.persistence.*;

public class CustomerCMPEarTest {
    
    /** Creates a new instance of CustomerCMPEarTest */
    public CustomerCMPEarTest() {
    }
    
    private String CUSTOMER_ID="99999";
    @Test public void testCustomerSample() {
	try {
		InitialContext ic = new InitialContext();

                String CUSTOMER_ID=String.valueOf(System.currentTimeMillis());
                
                CustomerSessionRemote sess = (CustomerSessionRemote)ic.lookup("enterprise.customer_cmp_ejb.ejb.session.CustomerSessionRemote");
                
                System.out.println("Adding new customer with id:"+CUSTOMER_ID);
                Customer customer = new Customer(CUSTOMER_ID, "Rahul", "Biswas");
                sess.persist(customer);
                
                System.out.println("Searching for customer with id:"+CUSTOMER_ID);
                Customer searchedCustomer= sess.searchForCustomer(CUSTOMER_ID);
		
                Assert.assertNotNull(searchedCustomer);
                
	} catch(Exception e) {
		e.printStackTrace();
		Assert.fail("encountered exception in CustomerCMPEarTest:testCustomerSample");
	}
  }

}
