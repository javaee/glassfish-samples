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
package enterprise.security_stateless_appclient;

import javax.ejb.EJB;
import enterprise.security_stateless_ejb.Sless;

public class SlessAppClient {
    @EJB private static Sless sless;

    public static void main(String args[]) {
        System.out.println(sless.helloRolesAllowed());

        try {
             sless.helloRolesAllowed2();
             throw new IllegalStateException(
                 "Unexpected succesful call for helloRolesAllowed2()");
        } catch(Exception ex) {
             System.out.println("Expected Exception for sless.helloRolesAllowed2()");
        }

        System.out.println(sless.helloPermitAll());

        try {
             sless.helloDenyAll();
             throw new IllegalStateException(
                 "Unexpected succesful call for helloDenyAll()");
        } catch(Exception ex) {
             System.out.println("Expected Exception for sless.helloDenyAll()");
        }
    }
}
