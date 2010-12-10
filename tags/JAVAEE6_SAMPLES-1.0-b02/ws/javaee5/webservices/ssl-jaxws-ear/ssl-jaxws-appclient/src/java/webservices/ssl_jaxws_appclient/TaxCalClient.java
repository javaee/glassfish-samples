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
 * @(#)TaxCalClient.java        
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

