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

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.resource.Referenceable;
import javax.resource.cci.*;
import javax.resource.spi.*;
import javax.resource.ResourceException;
import javax.naming.Reference;

import samples.connectors.mailconnector.api.*;
import samples.connectors.mailconnector.share.*;

/**
 * This implementation class provides an interface for getting a connection to a
 * Mail Server. It looks up a ConnectionFactory instance in the JNDI namespace
 * and uses it to get Mail Server connections.
 */

public class JavaMailConnectionFactoryImpl implements
        JavaMailConnectionFactory, Serializable, Referenceable {
    static Logger                    logger   = Logger.getLogger("samples.connectors.mailconnector.ra.outbound");
    ResourceBundle                   resource = ResourceBundle
                                                      .getBundle("samples.connectors.mailconnector.ra.outbound.ConnectionFactory");

    private ManagedConnectionFactory mcf;
    private ConnectionManager        cm;

    // Local variables

    private Reference                reference;
    private transient PrintWriter    out;
    private transient int            milliseconds;

    /**
     * JavaMailConnectionFactoryImpl constructor (no arguments).
     */

    public JavaMailConnectionFactoryImpl() {
    }

    /**
     * JavaMailConnectionFactoryImpl constructor.
     * 
     * @param mcf
     *            the ManagedConnectionFactory that created this
     *            JavaMailConnectionFactory instance
     * @param cm
     *            the ConnectionManager
     */

    public JavaMailConnectionFactoryImpl(ManagedConnectionFactory mcf,
            ConnectionManager cm) {
        logger.fine(" 3. JavaMailConnectionFactoryImpl::Constructor");
        this.mcf = mcf;
        if (cm == null) {
            this.cm = new ConnectionManagerImpl();
        } else {
            this.cm = cm;
        }
    }

    /**
     * Gets a connection to the Mail Server. Passes along mail server and user
     * info.
     * 
     * @return Connection Connection instance
     */

    public JavaMailConnection createConnection() throws ResourceException {
        JavaMailConnection con = null;

        logger.fine(" 3.- JMCFI::createConnection -- Client requested a connection. Get it from Connection Manager");

        // Use the default values of the Managed connection factory

        con = (JavaMailConnection) cm.allocateConnection(mcf, null);

        logger.fine(" 6.- JMCFI::createConnection -- Returning Connection to Client");

        return con;
    }

    /**
     * Gets a connection to a Mail Server instance. A component should use the
     * getConnection variant with a javax.resource.cci.ConnectionSpec parameter
     * if it needs to pass any resource-adapter-specific security information
     * and connection parameters.
     * 
     * @param properties
     *            connection parameters and security information specified as
     *            ConnectionSpec instance
     * @return a JavaMailConnection instance
     */

    public JavaMailConnection createConnection(ConnectionSpec properties)
            throws ResourceException {
        JavaMailConnection con = null;

        logger.fine(" 3.- JMCFI::createConnection -- Client requested a connection. Get it from Connection Manager");

        ConnectionRequestInfoImpl info = new ConnectionRequestInfoImpl(
                ((ConnectionSpecImpl) properties).getUserName(),
                ((ConnectionSpecImpl) properties).getPassword(),
                ((ConnectionSpecImpl) properties).getFolderName(),
                ((ConnectionSpecImpl) properties).getServerName(),
                ((ConnectionSpecImpl) properties).getProtocol());

        con = (JavaMailConnection) cm.allocateConnection(mcf, info);

        logger.fine(" 6.- JMCFI::createConnection -- Returning Connection to user");

        return con;
    }

    /**
     * Sets the log writer for the ConnectionFactory instance. The log writer is
     * a character output stream to which all logging and tracing messages for
     * the Connectionfactory instance will be printed.
     * 
     * @param out
     *            log writer associated with the ConnectionFactory
     */

    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.out = out;
    }

    /**
     * Gets the log writer for the ConnectionFactory instance.
     * 
     * @return PrintWriter log writer associated with the ConnectionFactory
     */

    public PrintWriter getLogWriter() throws ResourceException {
        return out;
    }

    /**
     * Sets the maximum time in milliseconds that this connection factory will
     * wait while attempting to connect to a Mail Server. A value of zero
     * specifies that the timeout is the default system timeout if there is one;
     * otherwise it specifies that there is no timeout. When a ConnectionFactory
     * object is created, the timeout is initially zero.
     * 
     * @param milliseconds
     *            connection establishment timeout in milliseconds
     */

    public void setTimeout(int milliseconds) throws ResourceException {
        this.milliseconds = milliseconds;
    }

    /**
     * Gets the maximum time in milliseconds that this connection factory can
     * wait while attempting to connect to a Mail Server.
     * 
     * @return connection establishment timeout in milliseconds
     */

    public int getTimeout() throws ResourceException {
        return milliseconds;
    }

    /**
     * This method is declared in the javax.resource.Referenceable interface and
     * should be implemented in order to support JNDI registration.
     * 
     * @param reference
     *            a Reference instance
     */

    public void setReference(Reference reference) {
        logger.fine("In JavaMailConnectionFactoryImpl::setReference");
        this.reference = reference;
    }

    /**
     * This method is declared in the javax.naming.Referenceable interface and
     * should be implemented in order to support JNDI registration.
     * 
     * @return a Reference instance
     */

    public Reference getReference() {
        logger.fine("In JavaMailConnectionFactoryImpl::getReference");
        return reference;
    }
}
