/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
