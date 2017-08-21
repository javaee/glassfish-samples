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

import javax.resource.cci.*;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import java.util.*;
import java.util.logging.*;

import samples.connectors.mailconnector.api.*;

/**
 * Application-level connection handle that is used by a client component to
 * access an EIS instance.
 */

public class JavaMailConnectionImpl implements JavaMailConnection {

    private ManagedConnectionImpl mc;                                                                                      // if
                                                                                                                            // mc
                                                                                                                            // is
                                                                                                                            // null,
                                                                                                                            // connection
                                                                                                                            // is
                                                                                                                            // invalid
    private javax.mail.Folder     folder;                                                                                  // Folder
                                                                                                                            // 1x1
                                                                                                                            // with
                                                                                                                            // connection

    static Logger                 logger   = Logger.getLogger(
                                                   "samples.connectors.mailconnector.ra.outbound",
                                                   "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle                resource = java.util.ResourceBundle
                                                   .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mc
     *            a physical connection to an underlying EIS
     * @param connectionInfo
     *            connection-specific info/properties
     * 
     */

    JavaMailConnectionImpl(ManagedConnectionImpl mc, javax.mail.Folder folder) {
        this.mc = mc;
        this.folder = folder;
        logger.fine(" 5. JavaMailConnectionImpl::Constructor");
    }

    /**
     * Retrieves a ManagedConnection.
     * 
     * @return a ManagedConnection instance representing the physical connection
     *         to the EIS
     */

    public ManagedConnectionImpl getManagedConnection() {
        logger.finest(" -- In JavaMailConnectionImpl::getManagedConnection mc="
                + mc);
        return mc;
    }

    /**
     * Returns a javax.resource.cci.LocalTransaction instance that enables a
     * component to demarcate resource manager local transactions on the
     * Connection.
     * 
     * Because this implementation does not support transactions, the method
     * throws an exception.
     * 
     * @return a LocalTransaction instance
     */
    public javax.resource.cci.LocalTransaction getLocalTransaction()
            throws ResourceException {
        throw new ResourceException(resource.getString("NO_TRANSACTION"));
    }

    /**
     * Returns the metadata for the ManagedConnection.
     * 
     * @return a ConnectionMetaData instance
     */
    public ConnectionMetaData getMetaData() throws ResourceException {
        logger.finest(" -- In JavaMailConnectionImpl:: getMetaData mc=" + mc);
        return new ConnectionMetaDataImpl(mc);
    }

    /**
     * Closes the connection.
     */
    public void close() throws ResourceException {
        logger.finest(" -- In JavaMailConnectionImpl:: close mc=" + mc);
        if (mc == null)
            return; // connection is already closed
        mc.removeJavaMailConnection(this);

        // Send a close event to the App Server
        mc.sendEvent(ConnectionEvent.CONNECTION_CLOSED, null, this);
        mc = null;
    }

    /**
     * Associates connection handle with new managed connection.
     * 
     * @param newMc
     *            new managed connection
     */

    public void associateConnection(ManagedConnectionImpl newMc)
            throws ResourceException {
        checkIfValid();
        // dissociate handle from current managed connection
        mc.removeJavaMailConnection(this);
        // associate handle with new managed connection
        newMc.addJavaMailConnection(this);
        mc = newMc;
    }

    /**
     * Checks the validity of the physical connection to the EIS.
     */

    void checkIfValid() throws ResourceException {
        logger.finest(" -- In JavaMailConnectionImpl::checkIfValid mc=" + mc);
        if (mc == null) {
            throw new ResourceException(
                    resource.getString("INVALID_CONNECTION"));
        }
    }

    /**
     * Sets the physical connection to the EIS as invalid. The physical
     * connection to the EIS cannot be used any more.
     */

    void invalidate() {
        logger.fine(" -- In JavaMailConnectionImpl::invalidate mc=" + mc);
        mc = null;
    }

    /**
     * Application-specific method. Fetches new messages from the mail server.
     * 
     * @return an array of messages
     */

    public javax.mail.Message[] getNewMessages() throws ResourceException {
        checkIfValid();
        try {
            return mc.getNewMessages(folder);
        } catch (Exception e) {
            logger.warning("ManagedConnectionImpl::getNewMessages threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }
    }

    /**
     * Application-specific method. Fetches new message headers from the mail
     * server.
     * 
     * @return a String array of message headers
     */
    public String[] getNewMessageHeaders() throws ResourceException {
        checkIfValid();
        try {
            return mc.getNewMessageHeaders(folder);
        } catch (Exception e) {
            logger.warning("ManagedConnectionImpl::getNewMessageHeaders threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }
    }
}
