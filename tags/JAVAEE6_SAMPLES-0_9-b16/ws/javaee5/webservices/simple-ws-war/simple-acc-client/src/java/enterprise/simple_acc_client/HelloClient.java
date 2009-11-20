package enterprise.simple_acc_client;

import javax.xml.ws.WebServiceRef;

public class HelloClient {

    @WebServiceRef(wsdlLocation="http://localhost:8080/simple-ws-war/HelloService?WSDL")
	    static HelloService service;

    public static void main(String args[]) {
        System.out.println("HelloClient Main ...");
	HelloClient hc = new HelloClient();
	hc.doHello();
    }

    public void doHello() {
        try
        {
            Hello port = service.getHelloPort();
            String ret = port.sayHello(System.getProperty("user.name"));
            System.out.println("Hello result = " + ret);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
