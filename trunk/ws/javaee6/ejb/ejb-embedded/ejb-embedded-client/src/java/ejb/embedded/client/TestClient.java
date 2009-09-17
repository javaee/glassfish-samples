package sample.ejb.embedded.client;

import sample.ejb.embedded.SimpleEjb;

import java.util.Map;
import java.util.HashMap;
import javax.ejb.*;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

public class TestClient {

    private static String appName;

    public static void main(String[] s) throws Exception {
        TestClient t = new TestClient();
        t.test();

    }

    private void test() throws Exception {

        Map<String, Object> p = new HashMap<String, Object>();
        p.put(EJBContainer.APP_NAME, "sample");

        EJBContainer c = null;
        try {
            c = EJBContainer.createEJBContainer(p);
            Context ic = c.getContext();
            System.out.println("Looking up EJB...");
            SimpleEjb ejb = (SimpleEjb) ic.lookup("java:global/sample/SimpleEjb");
            System.out.println("Invoking EJB...");
            System.out.println("Inserting entities...");
            ejb.insert(5);
            System.out.println("JPA count returned: " + ejb.verify());
            System.out.println("Done calling EJB");
        } finally {
            if (c != null) {
                System.out.println("Closing container ...");
                c.close();
                System.out.println("Done Closing container");
            }
        }

        System.out.println("..........FINISHED Embedded test");
    }
}
