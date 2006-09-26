package enterprise.interceptor_stateless_ejb_test;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.interceptor_stateless_ejb.StatelessSession;
import enterprise.interceptor_stateless_ejb.BadArgumentException;

public class InterceptorStatelessEjbTest {

    @Test public void test1() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            Assert.assertEquals("Failed to change case",
                sless.initUpperCase("duke"), "Duke");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test2() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase("4duke");
            Assert.assertEquals("Failed to change case", "A", "B");
        } catch(BadArgumentException badEx) {
            Assert.assertEquals("FailedtoConvert", "a", "a");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test3() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase(" duke");
            Assert.assertEquals("Failed to change case", "A", "B");
        } catch(BadArgumentException badEx) {
            Assert.assertEquals("FailedtoConvert", "a", "a");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test4() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase("\nDuke");
            Assert.assertEquals("Failed to change case", "A", "B");
        } catch(BadArgumentException badEx) {
            Assert.assertEquals("FailedtoConvert", "a", "a");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test5() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase("Duke");
            Assert.assertEquals("Failed to change case", "Duke", retVal);
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

}
