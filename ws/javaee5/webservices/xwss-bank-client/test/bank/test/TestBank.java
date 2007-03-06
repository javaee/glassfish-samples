/*
 * TestBank.java
 *
 * Created on March 6, 2007, 4:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bank.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 *
 * @author Manveen Kaur
 */
public class TestBank extends TestCase {
    
    public TestBank(String name) {
        super(name);
    }
    
    public static void main(String[] args) {
        
        System.out.println("Invoking Secure Bank Service...");
        
        TestRunner.run(new TestSuite(TestBank.class));
    }
    
    public static void testDeposit() {
        try {
            bank.client.BankAccountService service = new bank.client.BankAccountService();
            bank.client.BankAccount port = service.getBankAccountPort();
            
            double amount = 1000.0;
            double originalBalance = port.getBalance();
            double result = port.deposit(amount);
            
            assertEquals(result, amount + originalBalance);
            
        } catch(Exception e) {
            fail("Test case failed due to exception");
            e.printStackTrace();
        }
    }
    
}
