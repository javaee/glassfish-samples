package enterprise.simple_wsdl_client;

import javax.xml.ws.WebServiceRef;

public class HelloClient {
    /*
    @WebServiceRef(wsdlLocation="http://localhost:8080/simple-ws-war/HelloService?wsdl")
    static HelloService service;
    */
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            HelloClient client = new HelloClient();
            client.doTest(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void doTest(String[] args) {
        try {
            System.out.println("Retrieving the port from the hello service: ");
            Hello port = new HelloService().getHelloPort();
            System.out.println("Invoking the sayHello operation on the port.");

            String name;
            if (args.length > 0) {
                name = args[0];
            } else {
                name = "No Name";
            }
            
            String response = port.sayHello(name);
            System.out.println(response);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

