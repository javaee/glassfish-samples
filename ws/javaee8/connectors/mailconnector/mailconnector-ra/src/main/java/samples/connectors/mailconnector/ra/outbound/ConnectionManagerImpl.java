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

import java.lang.Object;
import java.util.logging.*;
import javax.resource.spi.*;
import javax.resource.ResourceException;
import java.io.Serializable;

/**
 * The default ConnectionManager implementation for the non-managed scenario.
 * This provides a hook for a resource adapter to pass a connection request to
 * an application server.
 */

public class ConnectionManagerImpl implements ConnectionManager, Serializable {
    static Logger logger = Logger.getLogger("samples.connectors.mailconnector.ra.outbound");

    public ConnectionManagerImpl() {
        logger.fine("In ConnectionManagerImpl");
    }

    /**
     * The method allocateConnection is called by the resource adapter's
     * connection factory instance. This lets the connection factory instance
     * provided by the resource adapter pass a connection request to the
     * ConnectionManager instance. The connectionRequestInfo parameter
     * represents information specific to the resource adapter for handling the
     * connection request.
     * 
     * @param mcf
     *            used by application server to delegate connection
     *            matching/creation
     * @param cxRequestInfo
     *            connection request information
     * 
     * @return connection handle with an EIS specific connection interface
     * 
     * @exception ResourceException
     *                if an error occurs
     */

    public Object allocateConnection(ManagedConnectionFactory mcf,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        ManagedConnection mc = mcf.createManagedConnection(null, cxRequestInfo);
        return mc.getConnection(null, cxRequestInfo);
    }
}
