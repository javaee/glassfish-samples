package enterprise.servlet_stateless_ejb_test;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;

import org.junit.*;

public class ServletStatelessEjbTest {

    @Test public void test1() {
        try {
            String url = "http://localhost:8080/servlet-stateless-war/servlet";
            URL u = new URL(url);
            HttpURLConnection c1 = (HttpURLConnection)u.openConnection();
            int code = c1.getResponseCode();
            InputStream is = c1.getInputStream();
            BufferedReader input = new BufferedReader (new InputStreamReader(is));
            String line = null;
            boolean status = true;
            while((line = input.readLine()) != null) {
                if (line.contains("Greeting from StatelessSessionBean:")) {
                    status = false;
                }
            }
            if(code != 200) {
                status = false;
            }

            Assert.assertEquals("Failed to invoke servlet", true, status);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("encountered exception in ServletStatelessEjbTest : test1");
        }
    }

    @Test public void test2() {
        try {
            String url = "http://localhost:8080/servlet-stateless-war/servlet?name=glassfish_user";
            URL u = new URL(url);
            HttpURLConnection c1 = (HttpURLConnection)u.openConnection();
            int code = c1.getResponseCode();
            InputStream is = c1.getInputStream();
            BufferedReader input = new BufferedReader (new InputStreamReader(is));
            String line = null;
            boolean status = false;
            while((line = input.readLine()) != null) {
                if (line.contains("Greeting from StatelessSessionBean:")) {
                    status = true;
                }
            }
            if(code != 200) {
                status = false;
            }

            Assert.assertEquals("Failed to invoke servlet", true, status);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("encountered exception in ServletStatelessEjbTest : test2");
        }
    }
}

