/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

package samples.connectors.mailconnector.ra.outbound;

import java.util.*;
import java.util.logging.*;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.security.*;
import javax.security.auth.Subject;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Utilities. The following methods handle authentication.
 * Note: If subject is null, credential is created from the
 * ConnectionRequestInfo user/password info. Otherwise it is created using the
 * subject object.
 * @author Alejandro E. Murillo
 */

public class Util {
    static Logger  logger  = Logger.getLogger(
                             "samples.connectors.mailconnector.ra.outbound",
                             "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    static ResourceBundle resource = java.util.ResourceBundle
                                    .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Returns a PasswordCredential.
     * 
     * @param mcf
     *            a ManagedConnectionFactory object
     * @param subject
     *            security context as JAAS subject
     * @param info
     *            ConnectionRequestInfo instance
     * 
     * @return a PasswordCredential
     */

    static public PasswordCredential getPasswordCredential(
            final ManagedConnectionFactory mcf, final Subject subject,
            ConnectionRequestInfo info) throws ResourceException {
        if (subject == null) {
            if (info == null) {
                logger.fine("\tUtil::GetPasswordCred: INFO is null");
                return null;
            } else {

                ConnectionRequestInfoImpl myinfo = (ConnectionRequestInfoImpl) info;

                // Can't create a PC with null values

                if (myinfo.getUserName() == null
                        || myinfo.getPassword() == null) {
                    logger.fine("\tUtil::GetPasswordCred: User or password is null");
                    return null;
                }

                char[] password = myinfo.getPassword().toCharArray();

                PasswordCredential pc = new PasswordCredential(
                        myinfo.getUserName(), password);

                pc.setManagedConnectionFactory(mcf);
                logger.fine("\tUtil::GetPasswordCred: returning a created PC");
                return pc;
            }
        } else {
            PasswordCredential pc = (PasswordCredential) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            Set creds = subject
                                    .getPrivateCredentials(PasswordCredential.class);
                            Iterator iter = creds.iterator();
                            while (iter.hasNext()) {
                                PasswordCredential temp = (PasswordCredential) iter
                                        .next();
                                if (temp != null
                                        && temp.getManagedConnectionFactory() != null
                                        && temp.getManagedConnectionFactory()
                                                .equals(mcf)) {
                                    return temp;
                                }
                            }
                            return null;
                        }
                    });
            if (pc == null) {
                throw new java.lang.SecurityException(
                        resource.getString("util.no_credential"));
            } else {
                logger.fine("\tUtil::GetPasswordCred: returning a FOUND PC");
                return pc;
            }
        }
    }

    /**
     * Determines whether two strings are the same.
     * 
     * @param a
     *            first string
     * @param b
     *            second string
     * 
     * @return true if the two strings are equal; false otherwise
     */

    static public boolean isEqual(String a, String b) {
        if (a == null) {
            return (b == null);
        } else {
            return a.equals(b);
        }
    }

    /**
     * Determines whether two PasswordCredentials are the same.
     * 
     * @param a
     *            first PasswordCredential
     * @param b
     *            second PasswordCredential
     * 
     * @return true if the two parameters are equal; false otherwise
     */

    static public boolean isPasswordCredentialEqual(PasswordCredential a,
            PasswordCredential b) {
        if (a == b)
            return true;
        if ((a == null) && (b != null))
            return false;
        if ((a != null) && (b == null))
            return false;
        if (!isEqual(a.getUserName(), b.getUserName()))
            return false;

        String p1 = null;
        String p2 = null;

        if (a.getPassword() != null) {
            p1 = new String(a.getPassword());
        }
        if (b.getPassword() != null) {
            p2 = new String(b.getPassword());
        }
        return (isEqual(p1, p2));
    }
}
