package endpoint;

import javax.jws.WebService;

@WebService
public class Hello
{
    public String getHello(String name)
    {
        return "Hello " + name + "!";
    }
}


