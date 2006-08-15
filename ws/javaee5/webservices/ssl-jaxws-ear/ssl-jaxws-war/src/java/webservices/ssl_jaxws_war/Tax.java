/*
 * Tax.java
 *
 * Created on July 30, 2006, 1:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package webservices.ssl_jaxws_war;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Tax WebService endpoint using the Java EE 5 annotations. 
 * Used to demonstrate https over ssl on Java EE 5 platform.
 *
 * @version 1.0  27 July 2006
 * @author Jagadesh Munta
 */

@WebService(
        name="Tax",
        serviceName="TaxService",
        targetNamespace="http://webservices/ssl_jaxws"
)
public class Tax {

    private static final double FED_TAX_RATE = 0.2; //20%

    public Tax() {
    }

    /*
     * Get the federal tax for given income and deductions
     */
    @WebMethod(operationName="getFedTax", action="urn:GetFedTax")
    public double getFedTax(double income, double deductions) {
        return ((income -  deductions) * FED_TAX_RATE);
    }

    
}
