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

package samples.connectors.mailconnector.share;

import javax.resource.cci.*;
import java.beans.*;

/**
 * This implementation class is used by an application component to pass
 * connection-specific info/properties to the getConnection method in the
 * JavaMailConnectionFactoryImpl class. This class is implemented as a JavaBeans
 * component.
 */

public class ConnectionSpecImpl implements ConnectionSpec {
    private PropertyChangeSupport changes    = new PropertyChangeSupport(this);
    private String                userName   = null;
    private String                password   = null;
    private String                folderName = null;
    private String                serverName = null;
    private String                protocol   = null;

    /**
     * ConnectionSpecImpl constructor (no arguments).
     */

    public ConnectionSpecImpl() {
    }

    /**
     * Returns the user name value.
     * 
     * @return String containing the user name
     */

    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the password value.
     * 
     * @return String containing the password
     */

    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the server name value.
     * 
     * @return String containing the server name
     */

    public String getServerName() {
        return this.serverName;
    }

    /**
     * Returns the folder name value.
     * 
     * @return String containing the folder name
     */

    public String getFolderName() {
        return this.folderName;
    }

    /**
     * Returns the protocol value.
     * 
     * @return String containing the protocol
     */

    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Sets the user name value.
     * 
     * @param userName
     *            the user name
     */

    public void setUserName(String userName) {
        String oldName = this.userName;
        this.userName = userName;
        changes.firePropertyChange("userName", oldName, userName);
    }

    /**
     * Sets the password value.
     * 
     * @param password
     *            the user password
     */

    public void setPassword(String password) {
        String oldPass = this.password;
        this.password = password;
        changes.firePropertyChange("password", oldPass, password);
    }

    /**
     * Sets the folder name value.
     * 
     * @param folderName
     *            the folder name
     */

    public void setFolderName(String folderName) {
        String oldFolderName = this.folderName;
        this.folderName = folderName;
        changes.firePropertyChange("folderName", oldFolderName, folderName);
    }

    /**
     * Sets the server name value.
     * 
     * @param serverName
     *            the server name
     */

    public void setServerName(String serverName) {
        String oldServerName = this.serverName;
        this.serverName = serverName;
        changes.firePropertyChange("serverName", oldServerName, serverName);
    }

    /**
     * Sets the protocol value.
     * 
     * @param protocol
     *            the server name
     */

    public void setProtocol(String protocol) {
        String oldProtocol = this.protocol;
        this.protocol = protocol;
        changes.firePropertyChange("protocol", oldProtocol, protocol);
    }

    /**
     * Associate PropertyChangeListener with the ConnectionSpecImpl in order to
     * notify about properties changes.
     * 
     * @param listener
     *            the listener to be associated with the connection spec
     */

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    /**
     * Delete association of PropertyChangeListener with the ConnectionSpecImpl.
     * 
     * @param listener
     *            the listener to be deleted
     */

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}
