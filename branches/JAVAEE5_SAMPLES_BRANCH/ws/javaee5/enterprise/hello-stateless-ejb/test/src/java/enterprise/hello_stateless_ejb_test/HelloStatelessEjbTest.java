package enterprise.hello_stateless_ejb_test;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.hello_stateless_ejb.StatelessSession;

public class HelloStatelessEjbTest {

  @Test public void returnMessage() {
	try {
		InitialContext ic = new InitialContext();
		StatelessSession sless =
			(StatelessSession) ic.lookup("enterprise.hello_stateless_ejb.StatelessSession");
		Assert.assertEquals(
			"Invalid return message",
			"hello, world!\n",
			sless.hello());
	} catch(Exception e) {
		e.printStackTrace();
		Assert.fail("encountered exception in HelloStatelessEjbTest:returnMessage");
	}
  }

}
