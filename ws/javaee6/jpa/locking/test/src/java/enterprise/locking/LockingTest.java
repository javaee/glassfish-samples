/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
package enterprise.locking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    // Sample data, which can be adjusted to see locking effect
    private int NumConsumer = 6;
    private int NumSupplier = 3;
    private int NumType = 3;
    private int NumUser = NumConsumer + NumSupplier;

    // System property values used to configure the HTTP connection
    private String contextRoot = "/locking";
    private String host = "localhost";
    private String port = "8080";

    // simulate multiple users
    private static int UserCount = 0;

    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() throws Exception {
        host = System.getProperty("javaee.server.name");
        port = System.getProperty("javaee.server.port");
        // System.out.println("host="+host+", port="+port);
    }

    @Test
    public void lockingTest() throws Exception {
        System.out.println("lockingTest: Test is starting.");
        try {
            test("secondRun");
            testThread("updateWOL");
            test("checkOLR");
            testThread("updateWPL");
            test("checkPLR");
        } catch (Exception ex) {
            System.out.println("Got Ex");
            // ex.printStackTrace();
        }
        System.out.println("lockingTest: Test is ended.");
    }

    private void test(String testcase) throws Exception {
        String EXPECTED_RESPONSE = "Test:Pass";
        String EXPECTED_MESSAGE = "MESSAGE";

        boolean status = false;
        String url = "http://" + host + ":" + port + contextRoot +
                "/test/?tc=" + testcase;
        if ("initData".equals(testcase)) {
            url = url + "&nc=" + NumConsumer + "&ns=" + NumSupplier + "&nt=" + NumType;
        } else if ("updateWOL".equals(testcase)) {
            UserCount++;
            url = url + "&uid=" + UserCount;
        } else if ("updateWPL".equals(testcase)) {
            UserCount++;
            url = url + "&uid=" + UserCount;
        }
        // System.out.println("url="+url);

        HttpURLConnection conn = (HttpURLConnection)
                (new URL(url)).openConnection();
        int code = conn.getResponseCode();
        if (code != 200) {
            System.err.println("Unexpected return code: " + code);
        } else {
            InputStream is = conn.getInputStream();
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(is));
            String line = null;
            while ((line = input.readLine()) != null) {
                // System.out.println("line="+line);
                if (line.contains(EXPECTED_MESSAGE)) {
                    System.out.println(" ");
                    System.out.println(line);
                    System.out.println(" ");
                } else if (line.contains(EXPECTED_RESPONSE)) {
                    status = true;
                    break;
                }
            }
            /*
	    if (line == null) {
	      System.out.println(testcase+": Unable to find " +
	      EXPECTED_RESPONSE + " in the response");
	    } */
        }
        if ("checkOLR".equals(testcase) || "checkPLR".equals(testcase)) {
            System.out.println("testcase=" + testcase + ", status=" + status);
            Assert.assertTrue(testcase, status);
        }
    }

    public void testThread(String testcase) {
        boolean status = false;
        UserCount = 0;
        final String tc = testcase;
        try {
            ArrayList<Thread> threads = new ArrayList<Thread>();
            int i = 0;
            for (i = 0; i < NumUser; i++) {
                threads.add(new Thread() {
                    public void run() {
                        // static int threadid = 0;
                        try {
                            test(tc);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            System.out.println(NumUser + " Threads starts for " + tc);
            for (i = 0; i < NumUser; i++) {
                threads.get(i).start();
            }

            for (i = 0; i < NumUser; i++) {
                threads.get(i).join();
            }
            System.out.println("Done multi-threading!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

