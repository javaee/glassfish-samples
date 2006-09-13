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
package enterprise.security_stateless_ejb;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
public class SlessEJB implements Sless {

    @RolesAllowed("javaee")
    public String helloRolesAllowed() {
        return "SlessEJB.helloRolesAllowed(): Hello World";
    }

    @RolesAllowed("noauthuser")
    public String helloRolesAllowed2() {
        return "SlessEJB.helloRoleAllowed2(): Hello World";
    }

    @PermitAll
    public String helloPermitAll() {
        return "SlessEJB.helloPermitAll(): Hello World";
    }

    @DenyAll
    public String helloDenyAll() {
        return "SlessEJB.helloDenyAll(): Hello World";
    }
}
