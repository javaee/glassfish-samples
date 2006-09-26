/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */
package enterprise.security_stateless_ear;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.appserv.security.ProgrammaticLogin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

import enterprise.security_stateless_ejb.Sless;

public class SecurityStatelessEarTest {
    private static Sless sless = null;

    @BeforeClass
    public static void setup() throws NamingException {
        InitialContext ic = new InitialContext();
        ProgrammaticLogin plogin = new ProgrammaticLogin();
        plogin.login("javaee", "javaee");
        sless = (Sless)ic.lookup("enterprise.security_stateless_ejb.Sless");
    }

    @Test
    public void helloRolesAllowed() {
        try {
            assertEquals(
                "Invalid return message",
                "SlessEJB.helloRolesAllowed(): Hello World",
                sless.helloRolesAllowed());
        } catch(Exception e) {
            e.printStackTrace();
            fail("encountered exception in SecurityStatelessEarTest:helloRolesAllowed");
        }
    }

    @Test(expected=Exception.class)
    public void helloRolesAllowed2() {
        sless.helloRolesAllowed2();
    }

    @Test
    public void helloPermitAll() {
        try {
            assertEquals(
                "Invalid return message",
                "SlessEJB.helloPermitAll(): Hello World",
                sless.helloPermitAll());
        } catch(Exception e) {
            e.printStackTrace();
            fail("encountered exception in SecurityStatelessEarTest:helloPermitAll");
        }
    }

    @Test(expected=Exception.class)
    public void helloDenyAll() {
        sless.helloDenyAll();
    }
}
