/*
 * BankAccount.java
 *
 * Created on March 6, 2007, 4:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bank.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Manveen Kaur
 */
@WebService()
public class BankAccount {
    
    double balance = 0;
    
    /**
     * Web service operation to retrieve balance
     */
    @WebMethod
    public double getBalance() {
        return balance;
    }

    /**
     * Web service operation to depsit into your bank account
     */
    @WebMethod
    public double deposit(@WebParam(name = "amount") double amount) {        
        balance = balance + amount;
        return balance;
    }
    
}
