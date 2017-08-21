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
import javax.resource.ResourceException;
import java.lang.String;
import javax.resource.spi.IllegalStateException;
import java.util.*;
import java.util.logging.*;

/**
 * The ManagedConnectionMetaData interface provides information about the
 * underlying EIS instance associated with a ManagedConnection instance. An
 * application server uses this information to get runtime information about a
 * connected EIS instance.
 */

public class ManagedConnectionMetaDataImpl implements ManagedConnectionMetaData {
    private ManagedConnectionImpl mc;

    static Logger                 logger   = Logger.getLogger(
                                                   "samples.connectors.mailconnector.ra.outbound",
                                                   "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle                resource = java.util.ResourceBundle
                                                   .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mc
     *            the managed connection that created this instance of
     *            ManagedConnectionMetaData
     */

    public ManagedConnectionMetaDataImpl(ManagedConnectionImpl mc) {
        logger.info("ManagedConnectionMetaDataImpl::Constructor");
        this.mc = mc;
    }

    /**
     * Returns the product name of the underlying EIS instance connected through
     * the ManagedConnection.
     * 
     * @return product name of the EIS instance
     */

    public String getEISProductName() throws ResourceException {
        String productName = null;

        // ToDo: Add service specific code here

        return productName;
    }

    /**
     * Returns the product version of the underlying EIS instance connected
     * through the ManagedConnection.
     * 
     * @return product version of the EIS instance
     */

    public String getEISProductVersion() throws ResourceException {
        String productVersion = null;
        // ToDo: Add service specific code here

        return productVersion;
    }

    /**
     * Returns the maximum number of active concurrent connections that an EIS
     * instance can support across client processes. If an EIS instance does not
     * know about (or does not have) any such limit, it returns zero.
     * 
     * @return maximum number of active concurrent connections
     */

    public int getMaxConnections() throws ResourceException {
        int maxConnections = 0;

        // ToDo: Add service specific code here

        return maxConnections;
    }

    /**
     * Returns the name of the user associated with the ManagedConnection
     * instance. The name corresponds to the resource principal under whose
     * security context a connection to the EIS instance has been established.
     * 
     * @return name of the user
     */

    public String getUserName() throws ResourceException {
        if (mc.isDestroyed()) {
            throw new IllegalStateException(
                    resource.getString("DESTROYED_CONNECTION"));
        }
        return mc.getUserName();
    }
}
