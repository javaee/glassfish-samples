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
import java.lang.String;
import java.util.*;
import java.util.logging.*;

/**
 * This class provides information about an EIS instance connected through a
 * Connection instance.
 */

public class ConnectionMetaDataImpl implements ConnectionMetaData {

    private ManagedConnectionImpl mc;

    static Logger                 logger   = Logger.getLogger(
                                                   "samples.connectors.mailconnector.ra.outbound",
                                                   "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle                resource = ResourceBundle
                                                   .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mc
     *            the physical connection of the JavaMailConnection that created
     *            this instance of ConnectionMetaDataImpl
     */

    public ConnectionMetaDataImpl(ManagedConnectionImpl mc) {
        logger.fine("ConnectionMetaDataImpl::constructor");
        this.mc = mc;
    }

    /**
     * Returns product name of the underlying EIS instance connected through the
     * Connection that produced this metadata.
     * 
     * @return product name of the EIS instance
     */

    public String getEISProductName() throws ResourceException {
        String productName = "JavaMail Connector";
        return productName;
    }

    /**
     * Returns product version of the underlying EIS instance.
     * 
     * @return product version of the EIS instance
     */

    public String getEISProductVersion() throws ResourceException {
        String productVersion = "0.1";
        return productVersion;
    }

    /**
     * Returns the user name for an active connection known to the Mail Server.
     * 
     * @return String representing the user name
     */

    public String getUserName() throws ResourceException {

        if (mc.isDestroyed()) {
            throw new ResourceException(
                    resource.getString("DESTROYED_CONNECTION"));
        }
        return mc.getUserName();
    }

    // Could return other connection info (serverName, etc.)
}
