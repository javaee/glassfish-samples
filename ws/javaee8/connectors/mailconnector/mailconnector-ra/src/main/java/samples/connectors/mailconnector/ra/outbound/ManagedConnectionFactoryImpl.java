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
import javax.resource.ResourceException;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.io.Serializable;
import javax.resource.spi.security.PasswordCredential;
import java.util.*;
import java.util.logging.*;
import java.beans.*;
import java.io.*;

/**
 * An object of this class is a factory of both ManagedConnection and connection
 * factory instances. This class supports connection pooling by defining methods
 * for matching and creating connections. This class is implemented as a
 * JavaBeans component.
 */

@ConnectionDefinition(connectionFactory = samples.connectors.mailconnector.api.JavaMailConnectionFactory.class, connectionFactoryImpl = samples.connectors.mailconnector.ra.outbound.JavaMailConnectionFactoryImpl.class, connection = samples.connectors.mailconnector.api.JavaMailConnection.class, connectionImpl = samples.connectors.mailconnector.ra.outbound.JavaMailConnectionImpl.class)
public class ManagedConnectionFactoryImpl implements ManagedConnectionFactory,
        Serializable {
    private transient PrintWriter           out;
    private transient PropertyChangeSupport changes = new PropertyChangeSupport(
                                                            this);

    static Logger                           logger  = Logger.getLogger(
                                                            "samples.connectors.mailconnector.ra.outbound",
                                                            "samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     */

    public ManagedConnectionFactoryImpl() {
        logger.fine(" 1.- ManagedConnectionFactoryImpl::Constructor");
    }

    /**
     * Creates a Connection Factory instance. The ConnectionFactory instance is
     * initialized with the passed ConnectionManager. In the managed scenario,
     * ConnectionManager is provided by the application server.
     * 
     * @param cxManager
     *            ConnectionManager to be associated with created EIS connection
     *            factory instance
     * 
     * @return EIS-specific Connection Factory instance
     * 
     * @exception ResourceException
     *                if the attempt to create a connection factory fails
     */

    public Object createConnectionFactory(ConnectionManager cxManager)
            throws ResourceException {
        logger.fine(" 2.- MCF::createConnectionFactory(cxManager)");
        JavaMailConnectionFactoryImpl cf = null;
        try {
            cf = new JavaMailConnectionFactoryImpl(this, cxManager);
        } catch (Exception e) {
            throw new ResourceException(e.getMessage());
        }
        return cf;
    }

    /**
     * Creates a Connection Factory instance. The Connection Factory instance is
     * initialized with a default ConnectionManager. In the non-managed
     * scenario, the ConnectionManager is provided by the resource adapter.
     * 
     * @return EIS-specific Connection Factory instance
     * 
     * @exception ResourceException
     *                if the attempt to create a connection factory fails
     */

    public Object createConnectionFactory() throws ResourceException {
        logger.fine(" 2.- MCF::createConnectionFactory()");
        return new JavaMailConnectionFactoryImpl(this, null);
    }

    /**
     * ManagedConnectionFactory uses the security information (passed as
     * Subject) and additional ConnectionRequestInfo (which is specific to
     * ResourceAdapter and opaque to application server) to create this new
     * connection.
     * 
     * @param subject
     *            caller's security information
     * @param cxRequestInfo
     *            additional resource adapter specific connection request
     *            information
     * 
     * @return ManagedConnection instance
     * 
     * @exception ResourceException
     *                if the attempt to create a connection fails
     */

    public ManagedConnection createManagedConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logger.fine(" 3A.- ManagedConnectionFactory::createManagedConnection(Subject, cxM)");
        String userName = null;
        ManagedConnectionImpl mc = null;

        mc = new ManagedConnectionImpl(this, subject, cxRequestInfo);

        return mc;
    }

    /**
     * Returns a matched managed connection from the candidate set of
     * connections. ManagedConnectionFactory uses the security info (as in
     * Subject) and information provided through ConnectionRequestInfo and
     * additional Resource Adapter specific criteria to do matching. A MC that
     * has the requested store is returned as a match
     * 
     * @param connectionSet
     *            candidate connection set
     * @param subject
     *            caller's security information
     * @param cxRequestInfo
     *            additional resource adapter specific connection request
     *            information
     * 
     * @return ManagedConnection if resource adapter finds an acceptable match,
     *         otherwise null
     * 
     * @exception ResourceException
     *                if the match fails
     */

    public ManagedConnection matchManagedConnections(Set connectionSet,
            Subject subject, ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        PasswordCredential pc = Util.getPasswordCredential(this, subject,
                cxRequestInfo);

        String userName = null;

        if (pc != null)
            userName = pc.getUserName();

        Iterator it = connectionSet.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof ManagedConnectionImpl) {
                ManagedConnectionImpl mc = (ManagedConnectionImpl) obj;
                ManagedConnectionFactory mcf = mc.getManagedConnectionFactory();

                if (Util.isPasswordCredentialEqual(mc.getPasswordCredential(),
                        pc)
                // && mcf.equals(this)
                        && mc.isTheSameStore((ConnectionRequestInfoImpl) cxRequestInfo)) {
                    logger.finest("MCF::matchManagedConnections: found match mc = "
                            + mc);
                    return mc;
                }
            }
        }
        return null;
    }

    /**
     * Sets the log writer for this ManagedConnectionFactory instance. The log
     * writer is a character output stream to which all logging and tracing
     * messages for this ManagedConnectionfactory instance will be printed.
     * 
     * @param out
     *            an output stream for error logging and tracing
     * 
     * @exception ResourceException
     *                if the method fails
     */

    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.out = out;
    }

    /**
     * Gets the log writer for this ManagedConnectionFactory instance.
     * 
     * @return PrintWriter an output stream for error logging and tracing
     * 
     * @exception ResourceException
     *                if the method fails
     */

    public PrintWriter getLogWriter() throws ResourceException {
        return this.out;
    }

    /**
     * Returns the hash code for the ManagedConnectionFactory. (Concatenation of
     * the MCF property values)
     * 
     * @return int hash code for the ManagedConnectionFactory
     */

    public int hashCode() {
        // The rule here is that if two objects have the same Id
        // i.e. they are equal and the .equals method returns true
        // then the .hashCode method *must* return the same
        // hash code for those two objects

        int hashcode = new String("").hashCode();

        if (userName != null)
            hashcode += userName.hashCode();

        if (password != null)
            hashcode += password.hashCode();

        if (serverName != null)
            hashcode += serverName.hashCode();

        if (protocol != null)
            hashcode += protocol.hashCode();

        if (folderName != null)
            folderName += folderName.hashCode();

        return hashcode;
    }

    /**
     * Check if this ManagedConnectionFactory is equal to another
     * ManagedConnectionFactory.
     * 
     * @param obj
     *            another ManagedConnectionFactory
     * 
     * @return boolean true if the properties are the same
     */

    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof ManagedConnectionFactoryImpl) {
                ManagedConnectionFactoryImpl other = (ManagedConnectionFactoryImpl) obj;

                if (!userName.equals(other.getUserName()))
                    return false;
                if (!password.equals(other.getPassword()))
                    return false;
                if (!serverName.equals(other.getServerName()))
                    return false;
                if (!folderName.equals(other.getFolderName()))
                    return false;
                if (!protocol.equals(other.getProtocol()))
                    return false;

                return true;
            }
        }
        return false;
    }

    /**
     * Associate PropertyChangeListener with the ManagedConnectionFactory, in
     * order to notify about properties changes.
     * 
     * @param lis
     *            the PropertyChangeListener to be associated with the
     *            ManagedConnectionFactory
     */

    public void addPropertyChangeListener(PropertyChangeListener lis) {
        changes.addPropertyChangeListener(lis);
    }

    /**
     * Delete association of PropertyChangeListener with the
     * ManagedConnectionFactory.
     * 
     * @param lis
     *            the PropertyChangeListener to be removed
     */

    public void removePropertyChangeListener(PropertyChangeListener lis) {
        changes.removePropertyChangeListener(lis);
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        logger.finest("Before readObject mcf");

        in.defaultReadObject();
        this.changes = new PropertyChangeSupport(this);
        this.out = null;
        logger.finest("after readObject mcf");
    }

    //
    // Properties
    //

    // serverName property value
    private String serverName = new String("unknownServerName");

    // userName property value
    private String userName   = new String("unknownUserName");

    // password property value
    @ConfigProperty(type = String.class, defaultValue = "UnknownPassword")
    private String password   = new String("unknownPassword");

    // folderName property value
    @ConfigProperty(type = String.class, defaultValue = "UnknownFolderName")
    private String folderName = new String("Inbox");

    // Normally imap or pop3
    private String protocol   = new String("imap");

    /**
     * Returns the value of the serverName property.
     * 
     * @return the value of the serverName property
     */

    public String getServerName() {
        return this.serverName;
    }

    /**
     * Sets the value of the serverName property
     * 
     * @param serverName
     *            String containing the value to be assigned to serverName
     */

    @ConfigProperty(type = String.class, defaultValue = "UnknownHostName")
    public void setServerName(String serverName) {
        String oldName = this.serverName;
        this.serverName = serverName;
        changes.firePropertyChange("serverName", oldName, serverName);
    }

    /**
     * Returns the value of the userName property.
     * 
     * @return the value of the userName property
     */

    public String getUserName() {
        return this.userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param userName
     *            String containing the value to be assigned to userName
     */

    @ConfigProperty(type = String.class, defaultValue = "UnknownUserName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the value of the password property.
     * 
     * @return the value of the password property
     */

    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param password
     *            String containing the value to be assigned to password
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the value of the folderName property.
     * 
     * @return the value of the folderName property
     */

    public String getFolderName() {
        return this.folderName;
    }

    /**
     * Sets the value of the folderName property.
     * 
     * @param folderName
     *            String containing the value to be assigned to folderName
     */

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Returns the value of the protocol property.
     * 
     * @return String the value of the protocol property
     */

    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param protocol
     *            String containing the value to be assigned to protocol
     */

    @ConfigProperty(description = "Typically Imap", type = String.class, defaultValue = "Imap")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}
