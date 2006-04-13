/*
 * Client.java
 *
 * Created on March 7, 2006, 11:31 PM
 */

package client;

import javax.xml.ws.WebServiceRef;
import endpoint.HelloService;
import endpoint.Hello;

public class Client
{
    @WebServiceRef(wsdlLocation="http://localhost:8080/Hello/HelloService?WSDL")
    static HelloService service;
    
    public static void main(String[] args)
    {
        Client client = new Client();
        client.doHello();
    }
    
    public void doHello()
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
    }
}
