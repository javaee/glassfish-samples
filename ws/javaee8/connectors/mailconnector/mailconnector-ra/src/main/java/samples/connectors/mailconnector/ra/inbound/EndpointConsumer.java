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

package samples.connectors.mailconnector.ra.inbound;

import javax.resource.spi.endpoint.*;

import java.rmi.*;
import java.util.logging.*;
import java.lang.reflect.*;

import javax.mail.*;

/**
 * JavaMail Client RMI interface.
 * This is a singleton class that represents the Client interface used by the
 * JavaMail Service.
 * @author Alejandro Murillo
 */

public class EndpointConsumer {
    ActivationSpecImpl     activationSpec;
    MessageEndpointFactory endpointFactory;
    MailServerFolder       folder    = null;

    static Logger          logger    = Logger.getLogger(
                                             "samples.connectors.mailconnector.ra.inbound",
                                             "samples.connectors.mailconnector.ra.inbound.LocalStrings");

    public Method          onMessage = null;

    /**
     * Constructor. Creates a JavaMail Client Interface object and exports it so
     * that the server can access it.
     * 
     * @param endpointFactory
     *            a MessageEndpointFactory
     * @param activationSpec
     *            the activation spec
     */

    public EndpointConsumer(MessageEndpointFactory endpointFactory,
            ActivationSpecImpl activationSpec) throws Exception {
        this.endpointFactory = endpointFactory;
        this.activationSpec = activationSpec;
        try {
            folder = new MailServerFolder(activationSpec);
        } catch (AuthenticationFailedException ie) {
            logger.info("[EC] Authentication problem when opening Mail Folder: "
                    + getUniqueKey()
                    + " Wrong password?, fix ejb-jar.xml, rebuild and redeploy");
            // ie.printStackTrace();
            throw ie;
        } catch (Exception ie) {
            logger.info("[EC] Unexpected Error while opening Mail Folder: "
                    + getUniqueKey()
                    + " check for typos with foldername, username, password or hostname in ejb-jar.xml, rebuild and redeploy");
            // ie.printStackTrace();
            throw ie;
        }
        logger.info("[EC] Created EndpointConsumer for: " + getUniqueKey());
    }

    /**
     * Delivers it to the appropriate EndPoint.
     * 
     * @param message
     *            the message to be delivered
     */

    public void deliverMessage(javax.mail.Message message)
            throws RemoteException {
        MessageEndpoint endpoint = null;

        Object[] args = { message };

        try {
            // o Create endpoint, passing XAResource.
            // o Call beforeDelivery to allow the appserver
            // to engage delivery in transaction, if required.
            // o Deliver Message.

            if ((endpoint = endpointFactory.createEndpoint(null)) != null) {
                // If this was an XA capable RA then invoke
                // endpoint.beforeDelivery();
                ((samples.connectors.mailconnector.api.JavaMailMessageListener) endpoint)
                        .onMessage(message);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "messagereceiver.onmessageexception",
                    e.getMessage());
        } catch (Error error) {
            logger.log(Level.WARNING, "messagereceiver.onmessageexception",
                    error.getMessage());
        } catch (Throwable t) {
            logger.log(Level.WARNING, "messagereceiver.onmessageexception",
                    t.getMessage());
        } finally {
            // o Call afterDelivery to to permit the Application Server to
            // complete or rollback transaction on delivery. This should
            // occur even if an exception has been thrown.
            // o Call release to indicate the endpoint can be recycled.

            if (endpoint != null) {
                // If this was an XA capable RA then invoke
                // endpoint.afterDelivery();
                endpoint.release();
            }
        }
    }

    public boolean hasNewMessages() throws Exception {
        logger.fine("[EC] Checking for new messages on: " + getUniqueKey());
        return folder.hasNewMessages();
    }

    public String getUniqueKey() {
        return activationSpec.getUserName() + "::"
                + activationSpec.getFolderName() + "@"
                + activationSpec.getServerName();
    }

    public Message[] getNewMessages() {
        Message msgs[] = null;
        try {
            msgs = folder.getNewMessages();
            if (msgs != null) {
                for (int i = 0; i < msgs.length; i++) {
                    if (!msgs[i].isSet(Flags.Flag.SEEN)) // Deliver only once
                    {
                        // deliverMessage(msgs[i]);
                        // Mark message as seen
                        msgs[i].setFlag(Flags.Flag.SEEN, true);
                    }
                }
            }
        } catch (Exception ie) {
            logger.info("[EC] getNewMessages caught an exception. Bailing out");
            ie.printStackTrace();
        }
        return msgs;

    }
}
