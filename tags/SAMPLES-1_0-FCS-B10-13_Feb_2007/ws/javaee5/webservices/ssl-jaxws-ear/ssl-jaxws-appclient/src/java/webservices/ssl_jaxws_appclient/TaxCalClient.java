/*
 * @(#)TaxCalClient.java        
 *
 * Copyright (c) 2004-2006 Sun Microsystems, Inc.
 * 4150,Network Circle, Santa Clara, California, 95054, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 */

package webservices.ssl_jaxws_appclient;

import webservices.ssl_jaxws.Tax;
import webservices.ssl_jaxws.TaxService;
import javax.xml.ws.WebServiceRef;
import javax.xml.rpc.Stub;

/**
 * This is Application Client program Tax Webservice using SSL port.
 *
 * @version 1.0  27 July 2006
 * @author Jagadesh Munta
 */
        
public class TaxCalClient {
    /*
     * Tests the getStateTax and getFedTax with expected values. If value 
     * matched,then test PASSED else FAILED.
     */
        @WebServiceRef(wsdlLocation="http://localhost:8080/ssl-jaxws-war/TaxService?wsdl")
        static TaxService service;

    public static void main (String[] args) {

        if(args.length<2){
            System.out.println("USage: TaxCalClient <income> <deductions>");
            return;
        } 
        double income=Double.parseDouble(args[0]);
        double deductions=Double.parseDouble(args[1]);
        
        System.out.println("Invoking TaxService...");
        try { 
            TaxCalClient client = new TaxCalClient();
            double fedTax = client.getFedTax(income, deductions);
            System.out.println("Income="+income+"\nDeductions="+deductions+
                            "\nFederal tax=" +fedTax);
            
        } catch(Exception e) {
                e.printStackTrace();
        }
    }
    

    public double getFedTax(double income, double deductions) throws Exception {        
          double fedTax = 0.0;
            if (service!=null) {
                Tax port = service.getTaxPort();
                //((Stub)port)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY,
                //        taxEndpoint);

                fedTax = port.getFedTax(income, deductions);                
            }else {
                throw new Exception("Error: Not able to get the tax service port " +
                        "and is null!");
            }
           return fedTax;
    }
}

