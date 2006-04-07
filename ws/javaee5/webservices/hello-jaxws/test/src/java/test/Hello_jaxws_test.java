/*
 * Hello_jaxws_test.java
 *
 */

package test;

import javax.xml.ws.WebServiceRef;
import endpoint.HelloService;
import endpoint.Hello;
import org.junit.*;
import junit.framework.TestCase;

public class Hello_jaxws_test extends TestCase
{
    @WebServiceRef(wsdlLocation="http://localhost:8080/Hello/HelloService?WSDL")
    static HelloService service;
    
    
    @Test public void doHello()
    {
        try
        {
            Hello port = service.getHelloPort();
            String ret = port.getHello(System.getProperty("user.name"));
            System.out.println("Hello result = " + ret);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		Assert.assertTrue(true);
    }

	@Before protected void setUp()
	{
		me = new Hello_jaxws_test();
	}

	private Hello_jaxws_test me;
}
