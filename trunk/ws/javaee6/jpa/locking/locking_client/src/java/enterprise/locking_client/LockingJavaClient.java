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

package enterprise.locking_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LockingJavaClient {

    // Sample data, which can be adjusted to see locking effect
    private final int NumConsumers = 6;
    private final int NumSuppliers = 3;
    private final int NumParts = 3;
    private final int NumUsers = NumConsumers + NumSuppliers;

    // server info
    private final String host = System.getProperty("javaee.server.name", "localhost");
    private final String port = System.getProperty("javaee.server.port", "8080");
    private final String contextRoot = "/locking";
    //Base URL to call Servlet
    private final String baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";

    public static void main(String args[]) throws Exception {
        System.out.println("LockingJavaClient: Test is starting");
        LockingJavaClient client = new LockingJavaClient();
        client.doTest();
        System.out.println("LockingJavaClient: Test is ended");
    }

    public void doTest() {
        try {
            initData();
            simulateParallelUpdates("updateWOL");
            simulateParallelUpdates("updateWPL");
        } catch (IOException ex) {
            System.out.println("Got Exception while executing test" + ex);
        }
    }

    /**
     * Initializes data required for this test.
     */
    private void initData() throws IOException {
        String url = baseURL + "initData" + "&nc=" + NumConsumers + "&ns=" + NumSuppliers + "&np=" + NumParts;
        callServlet(url);
    }

    /**
     * Call update for given userID using given updateMethod
     * @return true if the update succeeded false otherwise
     */
    private boolean callUpdate(String updateMethod, int userID) throws IOException {
        String url = baseURL + updateMethod + "&uid=" + userID;
        return callServlet(url);
    }

    /**
     * Calls the servlet with given url.
     * @return true if the call succeeded false otherwise
     */
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
                final String METHODRESULT_TOKEN = "MethodResult=";
                if (line.contains(METHODRESULT_TOKEN)) {
                    String result = line.substring(METHODRESULT_TOKEN.length());
                    callResult = Boolean.parseBoolean(result);
                }
            }
        } else {
            System.err.println("Unexpected return code: " + code);
        }
        return callResult;
    }

    /**
     * Simulate parallel updates for given operation using NumUsers
     */
    private void simulateParallelUpdates(final String operation) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < NumUsers; i++) {
            final int userId = i + 1; // userId are indexed at 1
            threads.add(new Thread() {
                public void run() {
                    try {
                        boolean updateResult = callUpdate(operation, userId);
                        System.out.println("Result for operation " + operation +
                                " for userId " + userId + " is " + (updateResult ? "Success" : "Failure"));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        System.out.println("\nStarting parallel updates with " + NumUsers + " users for operation: " + operation);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NumUsers; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < NumUsers; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel updates executed with " + NumUsers + " users for operation: " + operation + " Time taken:" + (endTime - startTime) + " miliseconds");
    }
}
