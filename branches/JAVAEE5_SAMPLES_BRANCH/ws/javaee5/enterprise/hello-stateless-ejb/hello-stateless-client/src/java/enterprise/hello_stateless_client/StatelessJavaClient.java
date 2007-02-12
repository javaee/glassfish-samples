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
package enterprise.hello_stateless_client;

import javax.naming.InitialContext;
import enterprise.hello_stateless_ejb.StatelessSession;

public class StatelessJavaClient {

    public static void main(String args[]) {
        System.out.println("returnMessage():" + returnMessage());
        System.out.println("StatelessSession bean says : " + returnMessage());
    }

    public static String returnMessage() {

        try {

            InitialContext ic = new InitialContext();
            StatelessSession sless = 
                (StatelessSession) ic.lookup("enterprise.hello_stateless_ejb.StatelessSession");
			return (sless.hello());

        } catch(Exception e) {
            e.printStackTrace();
        }

	return null;
    }

}
