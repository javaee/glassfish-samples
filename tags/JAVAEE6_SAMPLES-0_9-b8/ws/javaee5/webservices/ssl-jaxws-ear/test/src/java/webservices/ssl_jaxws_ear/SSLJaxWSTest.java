/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package webservices.ssl_jaxws_ear;

import org.junit.*;

import javax.naming.InitialContext;
import webservices.ssl_jaxws.*;
import javax.xml.ws.WebServiceRef;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class SSLJaxWSTest {

      @WebServiceRef(wsdlLocation="http://localhost:8080/ssl-jaxws-war/TaxService?wsdl")
      static TaxService service;
    
      public static void main(String[] args) {
            try {
                
                    // service is injected by the client container
                    if (service==null) {
                        System.out.println("Invalid Service - null");
                        return;
                    }   

                    // get the port
                    //Tax port = service.getTaxPort(); 
                    QName portName = new QName("http://webservices/ssl_jaxws","TaxPort");
                    Tax port = service.getPort(portName,Tax.class);

                    double income = 90000.0, deductions=10000.0, expFedTax=16000.0;
                    double fedTax = port.getFedTax(income, deductions);   
                    if (fedTax!=expFedTax) {
                        System.out.println("Test Failed");                        
                    } else {
                        System.out.println("Test Passed");
                    }

            } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("Test Failed");
            }    

    }
      
     @Test public void getFedTax() {
            try {
                
                    URL url = new URL("http://localhost:8080/ssl-jaxws-war/TaxService?wsdl");
                    QName serviceName = new QName("http://webservices/ssl_jaxws","TaxService");
                    QName portName = new QName("http://webservices/ssl_jaxws","TaxPort");
                    Service service = Service.create(url,serviceName);
                    Assert.assertNotNull(
                            "Invalid Service",
                            service);           
                    Tax port = service.getPort(portName,Tax.class);
                    //((Stub)port)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY,
                    //        taxEndpoint);
                    double income = 90000.0, deductions=10000.0, expFedTax=16000.0;
                    double fedTax = port.getFedTax(income, deductions);   
                    Assert.assertEquals("Not expected tax amount returned from service",
                            expFedTax, fedTax);

            } catch(Exception e) {
                    e.printStackTrace();
                    Assert.fail("SSLJaxWSTest:getFedTax");
            }    

    }      
}
