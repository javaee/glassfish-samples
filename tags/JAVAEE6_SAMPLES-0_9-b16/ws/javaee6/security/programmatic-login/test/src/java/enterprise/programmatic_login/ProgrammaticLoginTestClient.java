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
package enterprise.programmatic_login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.Assert;
import org.junit.Test;

public class ProgrammaticLoginTestClient {

    static String host = System.getProperty("http.host", "localhost");
    static String portS = System.getProperty("http.port", "8080");
    static final String url = "/programmatic-login/loginservlet";
    static final String userName = "txtUserName=javaee6user";
    static final String password = "&txtPassword=abc123";
    

    @Test
    public void loginWithCredentials()
            throws Exception {
        String endPoint = "http://" + host + ":" + portS + url;
        String requestParams = userName + password;
        StringBuilder response = sendGetRequest(endPoint, requestParams);
        System.out.println(response);
        int fromIndex = response.indexOf("After Login...");
        int secondFromIndex = response.indexOf("IsUserInRole?..",fromIndex);
        String isUserInRoleVal = response.substring(secondFromIndex+15,secondFromIndex+20);
        Assert.assertTrue(isUserInRoleVal != null && isUserInRoleVal.contains("true"));
    }

    private static StringBuilder sendGetRequest(String endpoint, String requestParameters) {
        String result = null;

        StringBuilder data = new StringBuilder();
        try {


            String urlStr = endpoint;
            if (requestParameters != null && requestParameters.length() > 0) {
                urlStr += "?" + requestParameters;
            }
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                data.append(line);
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

   @Test
    public  void loginWithoutCredentials()
    throws Exception {
        String endPoint = "http://" + host + ":" + portS + url;
        String requestParams = userName + password;
        StringBuilder response = sendGetRequest(endPoint, null);
        System.out.println(response);
        int fromIndex = response.indexOf("After Login...");
        int secondFromIndex = response.indexOf("IsUserInRole?..",fromIndex);
        String isUserInRoleVal = response.substring(secondFromIndex+15,secondFromIndex+20);
        Assert.assertTrue(isUserInRoleVal != null && isUserInRoleVal.contains("false"));
    
    }
}
