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

package enterprise.locking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This test class exercises the Locking demo application
 */
public class LockingTest {


    // System property values used to configure the HTTP connection
    private String contextRoot = "/locking";
    private String host = "localhost";
    private String port = "8080";
    private String baseURL = null;


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() throws Exception {
        host = System.getProperty("javaee.server.name");
        port = System.getProperty("javaee.server.port");
        // System.out.println("host="+host+", port="+port);
        baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";
    }

    @Test
    public void lockingTest() throws Exception {
        System.out.println("lockingTest: Test is starting.");
	boolean updateResult = false;
        int userId = 1;
        try {
 	    updateResult = callUpdate("updateWOL", userId);
            Assert.assertTrue("updateWOL", updateResult);
 	    updateResult = callUpdate("updateWPL", userId);
            Assert.assertTrue("updateWPL", updateResult);
        } catch (Exception ex) {
            System.out.println("Got Ex");
            // ex.printStackTrace();
        }
        System.out.println("lockingTest: Test is ended.");
    }

    private boolean callUpdate(String updateMethod, int userID) throws IOException {
        String url = baseURL + updateMethod + "&uid=" + userID;
        return callServlet(url);
    }

    private boolean callServlet(String url) throws IOException {
        boolean callResult = false;
        System.out.println("Calling URL:" + url);
        HttpURLConnection conn = (HttpURLConnection)
                (new URL(url)).openConnection();
        int code = conn.getResponseCode();
        if (code == 200) {
            InputStream is = conn.getInputStream();
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(is));
            String line = null;
            while ((line = input.readLine()) != null) {
                final String METHODRESULT = "MethodResult=";
                if (line.contains(METHODRESULT)) {
                    String result = line.substring(METHODRESULT.length());
                    callResult = Boolean.parseBoolean(result);
                }
            }
        } else {
            System.err.println("Unexpected return code: " + code);
        }
        return callResult;
    }

}

