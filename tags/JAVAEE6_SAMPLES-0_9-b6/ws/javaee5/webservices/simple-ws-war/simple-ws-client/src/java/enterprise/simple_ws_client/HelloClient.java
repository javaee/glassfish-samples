package enterprise.simple_ws_client;

import javax.xml.ws.BindingProvider;

public class HelloClient {

    public static void main(String args[]) {
        System.out.println("HelloClient Main ...");
	try {
		Hello port = new HelloService().getHelloPort();
		log((BindingProvider)port);
		String response = port.sayHello("pujita and punit");
        	System.out.println("response = " + response);
	} catch (Exception e) {
	}
    }


    private static final void log(BindingProvider port) {
        if (Boolean.getBoolean("wsmonitor")) {
            String address = (String)port.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
            address = address.replaceFirst("8080", "4040");
            port.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
        }
    }

}
