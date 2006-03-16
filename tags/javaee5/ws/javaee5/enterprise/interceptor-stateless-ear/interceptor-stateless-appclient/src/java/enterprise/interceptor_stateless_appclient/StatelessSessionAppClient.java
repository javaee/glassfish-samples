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
package enterprise.interceptor_stateless_appclient;

import javax.ejb.EJB;
import enterprise.interceptor_stateless_ejb.*;

public class StatelessSessionAppClient {

    @EJB
    private static StatelessSession sless;

    public static void main(String args[]) {

        tryToConvert("duke");
        tryToConvert(" duke");
        tryToConvert(null);
        tryToConvert(" duke");
        tryToConvert("4duke");
        tryToConvert("Duke");
    }

    private static void tryToConvert(String inputStr) {
        try {
            String upCaseStr = sless.initUpperCase(inputStr);
            System.out.println("Converted " + inputStr + " to " + upCaseStr);
        } catch (BadArgumentException badEx) {
            System.out.println("Cannot convert (BadArgument)" + inputStr);
        } catch (Exception ex) {
            System.out.println("Got some exception while converting: "
                    + inputStr);
        }
    }

}
