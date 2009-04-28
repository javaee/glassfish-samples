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
    private int NumConsumer = 6;
    private int NumSupplier = 3;
    private int NumParts = 3;
    private int NumUser = NumConsumer + NumSupplier;

    // server info
    private String host = System.getProperty("javaee.server.name", "localhost");
    private String port = System.getProperty("javaee.server.port", "8080");
    private String contextRoot = "/locking";
    //Base URL to call Servlet
    private String baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";

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
            //callServlet(baseURL + "checkOLR");
            simulateParallelUpdates("updateWPL");
            //callServlet(baseURL + "checkPLR");
        } catch (IOException ex) {
            System.out.println("Got Exception while executing test" + ex);
        }
    }

    private void initData() throws IOException {
        String url = baseURL + "initData" + "&nc=" + NumConsumer + "&ns=" + NumSupplier + "&np=" + NumParts;
        callServlet(url);
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

    private void simulateParallelUpdates(final String operation) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < NumUser; i++) {
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
        System.out.println(NumUser + " Threads started for " + operation);
        for (int i = 0; i < NumUser; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < NumUser; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done multi-threading!");
    }
}
