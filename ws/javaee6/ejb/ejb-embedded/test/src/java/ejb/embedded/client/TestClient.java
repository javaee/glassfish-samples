package sample.ejb.embedded.test;

import sample.ejb.embedded.SimpleEjb;

import org.junit.*;

import java.util.Map;
import java.util.HashMap;
import javax.ejb.*;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

public class TestClient {

    @Test public void test() {

        try {
            Map<String, Object> p = new HashMap<String, Object>();
            p.put(EJBContainer.APP_NAME, "sample");
    
            EJBContainer c = EJBContainer.createEJBContainer(p);
            Context ic = c.getContext();
            System.out.println("Looking up EJB...");
            SimpleEjb ejb = (SimpleEjb) ic.lookup("java:global/sample/SimpleEjb");
            System.out.println("Invoking EJB...");
            System.out.println("Inserting entities...");
            ejb.insert(5);
            int res = ejb.verify();
            System.out.println("JPA call returned: " + res);
            System.out.println("Done calling EJB");
    
            System.out.println("Closing container ...");
            c.close();
            System.out.println("Done Closing container");
    
            Assert.assertTrue("Unexpected number of entities", (res == 5));
            System.out.println("..........SUCCESSFULLY finished embedded test");
       } catch (Exception e) {
           e.printStackTrace();
            Assert.fail("Failed Embedded test");
       }
    }
}
