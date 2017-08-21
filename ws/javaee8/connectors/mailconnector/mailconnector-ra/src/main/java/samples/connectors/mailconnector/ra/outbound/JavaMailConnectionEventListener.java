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
import java.util.*;
import java.util.logging.*;

/**
 * The connector architecture provides an event callback mechanism that enables
 * an application server to receive notifications from a ManagedConnection
 * instance. The App Server implements this class in order to listen to event
 * notifications from ManagedConnection instances.
 */

public class JavaMailConnectionEventListener {
    private Vector            listeners;
    private ManagedConnection mcon;

    static Logger logger   = Logger.getLogger(
                             "samples.connectors.mailconnector.ra.outbound",
                             "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle  resource = ResourceBundle
                               .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mcon
     *            the managed connection that created this instance
     */

    public JavaMailConnectionEventListener(ManagedConnection mcon) {
        logger.fine(" 3C.- JavaMailConnectionEventListener::Constructor");
        listeners = new Vector();
        this.mcon = mcon;
    }

    /**
     * Sends a connection event to the application server.
     * 
     * @param eventType
     *            the ConnectionEvent type
     * @param ex
     *            exception indicating a connection-related error
     * @param connectionHandle
     *            the connection handle associated with the ManagedConnection
     *            instance
     */

    public void sendEvent(int eventType, Exception ex, Object connectionHandle) {
        Vector list = (Vector) listeners.clone();
        ConnectionEvent ce = null;
        if (ex == null) {
            ce = new ConnectionEvent(mcon, eventType);
        } else {
            ce = new ConnectionEvent(mcon, eventType, ex);
        }
        if (connectionHandle != null) {
            ce.setConnectionHandle(connectionHandle);
        }

        for (int i = 0; i < list.size(); i++) {
            ConnectionEventListener l = (ConnectionEventListener) list
                    .elementAt(i);

            switch (eventType) {
                case ConnectionEvent.CONNECTION_CLOSED:
                    l.connectionClosed(ce);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
                    l.localTransactionStarted(ce);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
                    l.localTransactionCommitted(ce);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
                    l.localTransactionRolledback(ce);
                    break;
                case ConnectionEvent.CONNECTION_ERROR_OCCURRED:
                    l.connectionErrorOccurred(ce);
                    break;
                default:
                    throw new IllegalArgumentException(
                            resource.getString("ILLEGAL_EVENT_TYPE")
                                    + eventType);
            }
        }
    }

    /**
     * Adds a connection event listener to the ManagedConnection Listener
     * instance. The registered ConnectionEventListener instances are notified
     * of connection close and error events and of local transaction-related
     * events on the ManagedConnection.
     * 
     * @param listener
     *            a new ConnectionEventListener to be registered
     */

    public void addConnectorListener(ConnectionEventListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Removes an already registered connection event listener.
     * 
     * @param listener
     *            the already registered connection event listener to be removed
     */

    public void removeConnectorListener(ConnectionEventListener listener) {
        listeners.removeElement(listener);
    }
}
