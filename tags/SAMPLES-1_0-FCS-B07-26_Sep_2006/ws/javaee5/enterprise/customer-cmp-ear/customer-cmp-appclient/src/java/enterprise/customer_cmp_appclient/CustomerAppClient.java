/*
 * CustomerAppClient.java
 *
 * Created on April 3, 2006, 1:42 PM
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


package enterprise.customer_cmp_appclient;

import javax.naming.InitialContext;
import java.util.List;
import javax.ejb.EJB;
import enterprise.customer_cmp_ejb.ejb.session.CustomerSessionRemote;
import enterprise.customer_cmp_ejb.persistence.*;
        
public class CustomerAppClient {

    @EJB 
    private static CustomerSessionRemote sess;

    public static void main(String args[]) {
	try {
		InitialContext ic = new InitialContext();
                String CUSTOMER_ID="99999";
                
                System.out.println("Searching for customer with id:"+CUSTOMER_ID);
                Customer searchedCustomer= sess.searchForCustomer(CUSTOMER_ID);
                
                if(searchedCustomer==null){
                    throw new Exception("searched customer not found");
                }
                
                System.out.println("found customer with id:"+CUSTOMER_ID);
                System.out.println("First Name:"+searchedCustomer.getFirstName());
                System.out.println("Last Name:"+searchedCustomer.getLastName());
                
	} catch(Exception e) {
		e.printStackTrace();
	}
  }

}