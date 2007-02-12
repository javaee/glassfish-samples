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
