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
package enterprise.annotation_override_interceptor_appclient;

import java.util.List;

import javax.ejb.EJB;
import enterprise.annotation_override_interceptor_ejb.*;

public class StatelessSessionAppClient {

    @EJB
    private static StatelessSession sless;

    public static void main(String args[]) {
	try {
            sless.initUpperCase("hello, World!!");
            sless.initLowerCase("Build.xml");
        } catch (Exception  badEx) {
		badEx.printStackTrace();
        }

        List<String> upperList = sless.getInterceptorNamesFor("initUpperCase");
	printList("initUpperCase", upperList);

	try {
	    sless.isOddNumber(7);
        } catch (Exception  badEx) {
		badEx.printStackTrace();
        }

	List<String> isOddNumberList = sless.getInterceptorNamesFor("isOddNumber");
	printList("isOddNumber", isOddNumberList);
    }

    private static void printList(String msg, List<String> list) {
	System.out.print("Interceptors invoked for " + msg + "(): ");
	String delimiter = "";
        for (String str : list) {
            System.out.print(delimiter + str);
	    delimiter = ", ";
        }
	System.out.println("}");
    }
}
