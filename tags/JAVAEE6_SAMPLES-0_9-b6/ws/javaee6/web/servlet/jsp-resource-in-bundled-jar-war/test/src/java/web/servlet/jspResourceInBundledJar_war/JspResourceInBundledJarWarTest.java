/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package web.servlet.jspResourceInBundledJar_war;

import java.io.*;
import java.net.*;
import org.junit.*;

/**
 * junit test for jspAndStaticResourcesInLocalJar-war.
 *
 * @author Jan Luehe
 */
public class JspResourceInBundledJarWarTest {

    private static final String REQUEST_URI =
        "/jspResourceInBundledJar_war/jsp/helloWorld.jsp";

    private String host = null;
    private int port = 8080;

    @Before
    public void setUpClass() throws Exception {
        host = System.getProperty("javaee.server.name");
        port = Integer.parseInt(System.getProperty("javaee.server.port"));
    }

    @Test
    public void testWebClient() throws Exception {
        String url = "http://" + host + ":" + port + REQUEST_URI;
        System.out.println("Connecting to " + url);
        HttpURLConnection conn = null;
        BufferedReader input = null;
        try {
            conn = (HttpURLConnection)(new URL(url)).openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                Assert.fail(
                    "jspResourceInBundledJar_war failed with response code: " +
                    code);
            } else {
                input = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
                String line = input.readLine();
                Assert.assertEquals("Invalid return message", "Hello World",
                    line);
            }
        } catch(Exception ex) {
            Assert.fail("jspResourceInBundledJar_war failed with exception: " +
                        ex);
        } finally {
            if (input != null) {
                input.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
