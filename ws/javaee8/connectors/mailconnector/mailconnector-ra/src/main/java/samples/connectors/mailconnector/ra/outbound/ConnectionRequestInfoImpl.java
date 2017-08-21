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

import javax.resource.spi.*;
import java.lang.Object;

/**
 * This class implments the ConnectionRequestInfo interface, which enables a
 * resource adapter to pass its own request-specific data structure across the
 * connection request flow.
 */

public class ConnectionRequestInfoImpl implements ConnectionRequestInfo {
    private String userName   = null;
    private String password   = null;
    private String folderName = null;
    private String serverName = null;
    private String protocol   = null;

    /**
     * Constructor.
     * 
     * @param userName
     *            user name
     * @param password
     *            user password
     * @param folderName
     *            folder name
     * @param serverName
     *            server name
     * @param protocol
     *            protocol
     */

    public ConnectionRequestInfoImpl(String userName, String password,
            String folderName, String serverName, String protocol) {
        this.userName = userName;
        this.password = password;
        this.folderName = folderName;
        this.serverName = serverName;
        this.protocol = protocol;
    }

    //
    // Getter methods
    //

    /**
     * Returns the user name value.
     * 
     * @return String containing the user name
     */

    public String getUserName() {
        return userName;
    }

    /**
     * Returns the password value.
     * 
     * @return String containing the password
     */

    public String getPassword() {
        return password;
    }

    /**
     * Returns the server name value.
     * 
     * @return String containing the server name
     */

    public String getServerName() {
        return serverName;
    }

    /**
     * Returns the folder name value.
     * 
     * @return String containing the folder name
     */

    public String getFolderName() {
        return folderName;
    }

    /**
     * Returns the protocol value.
     * 
     * @return String containing the protocol
     */

    public String getProtocol() {
        return protocol;
    }

    /**
     * Checks whether this instance is equal to another.
     * 
     * @param obj
     *            other object
     * 
     * @return true if the two instances are equal, false otherwise
     */

    public boolean equals(Object obj) {
        boolean equal = false;

        if (obj != null) {
            if (obj instanceof ConnectionRequestInfoImpl) {
                ConnectionRequestInfoImpl other = (ConnectionRequestInfoImpl) obj;

                equal = (this.userName).equals(other.userName)
                        && (this.password).equals(other.password)
                        && (this.serverName).equals(other.serverName)
                        && (this.protocol).equals(other.protocol);
                // equal = Util.isEqual(this.userName, other.userName) &&
                // Util.isEqual(this.password, other.password) &&
                // Util.isEqual(this.serverName, other.serverName) &&
                // Util.isEqual(this.protocol, other.protocol);
            }
        }
        return equal;

    }

    /**
     * Returns the hashCode of the ConnectionRequestInfoImpl.
     * 
     * @return the hash code of this instance
     */

    public int hashCode() {
        int hashcode = new String("").hashCode();

        if (userName != null)
            hashcode += userName.hashCode();

        if (password != null)
            hashcode += password.hashCode();

        if (serverName != null)
            hashcode += serverName.hashCode();

        if (protocol != null)
            hashcode += protocol.hashCode();

        return hashcode;
    }
}
