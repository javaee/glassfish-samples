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
package criteriaquery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CriteriaQueryJavaClient {

    // server info
    private final String host = System.getProperty("javaee.server.name", "localhost");
    private final String port = System.getProperty("javaee.server.port", "8080");
    private final String contextRoot = "/criteriaQuery";
    //Base URL to call Servlet
    private final String baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";

    public static void main(String args[]) throws Exception {
        System.out.println("CriteriaQueryJavaClient: Test is starting");
        CriteriaQueryJavaClient client = new CriteriaQueryJavaClient();
        client.doTest();
        System.out.println("CriteriaQueryJavaClient: Test is ended");
    }

    public void doTest() {
        try {
            createData();
            queryData();
        } catch (IOException ex) {
            System.out.println("Got Exception while executing test" + ex);
        }
    }

    /**
     * Initializes data required for this test.
     */
    private void createData() throws IOException {
        String url = baseURL + "createData";
        callServlet(url);
    }

    private void queryData() throws IOException {
        String url = baseURL + "queryData";
        callServlet(url);
    }

    /**
     * Calls the servlet with given url.
     * @return true if the call succeeded false otherwise
     */
    private void callServlet(String url) throws IOException {
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
                System.out.println(line);
            }
        } else {
            System.err.println("Unexpected return code: " + code);
        }
    }

}
