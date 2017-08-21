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

package samples.connectors.mailconnector.ra;

import javax.resource.NotSupportedException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.*;
import javax.resource.spi.work.*;
import javax.resource.*;

import javax.naming.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

import samples.connectors.mailconnector.ra.inbound.*;

/**
 * JavaMail Resource Adapter
 * @author Alejandro E. Murillo
 */

@Connector(description = "Sample adapter using the JavaMail API", 
  displayName = "InboundResourceAdapter", 
  vendorName = "Sun Microsystems, Inc.", 
  eisType = "MAIL", 
  version = "1.0", 
  authMechanisms = { 
        @AuthenticationMechanism(authMechanism = "BasicPassword", 
          credentialInterface = AuthenticationMechanism.CredentialInterface.PasswordCredential) 
  }
 * // Since the following attribute values denote the default values of the
 * annotation, // they need not be specified explicitly
 * transactionSupport =
 * TransactionSupport.TransactionSupportLevel.NoTransaction,
 * reauthenticationSupport = false
 */
)
public class ResourceAdapterImpl implements ResourceAdapter,
        java.io.Serializable {
    protected transient BootstrapContext bootCtx;
    protected transient WorkManager      workManager;
    public transient Context             jndiContext = null;
    private Work                         pollingThread;

    public Method                        onMessage   = null;

    static Logger logger = Logger.getLogger(
                           "samples.connectors.mailconnector.ra",
                           "samples.connectors.mailconnector.ra.LocalStrings");
    ResourceBundle resource = ResourceBundle
                             .getBundle("samples.connectors.mailconnector.ra.LocalStrings");

    /**
     * Constructor.
     */
    public ResourceAdapterImpl() {
    }

    /**
     * Called by the AppServer to initialize the Resource Adapter.
     * 
     * @param ctx
     *            the BootstrapContext
     */

    public void start(BootstrapContext ctx)
            throws ResourceAdapterInternalException {
        /*
         * Bootstrap context - used to acquire WorkManager, Timer, or
         * XATerminator
         */

        this.bootCtx = ctx;

        try {
            // Get the initial JNDI Context
            this.jndiContext = new InitialContext();

            // Get Work Manager
            this.workManager = ctx.getWorkManager();
        } catch (Exception ex) {
            logger.severe(resource.getString("resourceadapterimpl.noservice"));
            ex.printStackTrace();
            throw new ResourceAdapterInternalException(
                    resource.getString("resourceadapterimpl.noservice"));
        }

        setOnMessageMethod();

        /* Start the polling thread */

        try {
            pollingThread = new PollingThread(workManager);
            workManager.scheduleWork(pollingThread);
        } catch (WorkRejectedException ex) {
            ResourceAdapterInternalException newEx = new ResourceAdapterInternalException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.worker_activation_rejected"),
                            new Object[] { ex.getMessage() }));
            newEx.initCause(ex);
            throw newEx;
        } catch (Exception ex) {
            ResourceAdapterInternalException newEx = new ResourceAdapterInternalException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.worker_activation_failed"),
                            new Object[] { ex.getMessage() }));

            newEx.initCause(ex);
            throw newEx;
        }
    }

    /**
     * Sets the method for the onMessage method used in MessageListener.
     */

    private void setOnMessageMethod() {
        Method onMessageMethod = null;

        try {
            Class msgListenerClass = samples.connectors.mailconnector.api.JavaMailMessageListener.class;
            Class[] paramTypes = { javax.mail.Message.class };
            onMessageMethod = msgListenerClass.getMethod("onMessage",
                    paramTypes);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        onMessage = onMessageMethod;
    }

    /**
     * Gets the method used to deliver messages.
     * 
     * @return the onMessage method
     */

    public Method getOnMessageMethod() {
        return onMessage;
    }

    /**
     * Called by the Application Server to indicate shutdown is imminent. The
     * Application Server should have undeployed all the message endpoints prior
     * to this call, but the JavaMail-RA will iterate through them and ensure
     * that all the message endpoints are no longer consuming messages.
     */

    public void stop() {
        logger.info("[RA.stop()] Stopping the polling thread");
        try {
            ((PollingThread) pollingThread).stopPolling();
        } catch (Exception ex) {
            logger.severe(resource.getString("resourceadapterimpl.noservice"));
        }
    }

    /**
     * Called by the Application Server when a message-driven bean
     * (MessageEndpoint) is deployed. Causes the resource adapter instance to do
     * the necessary setup (setting up message delivery for the message endpoint
     * with a message provider).
     * 
     * @param endpointFactory
     *            a message endpoint factory instance
     * @param spec
     *            an ActivationSpec instance
     * 
     * @exception NotSupportedException
     *                if message endpoint activation is rejected because of
     *                incorrect activation setup information
     */

    public void endpointActivation(MessageEndpointFactory endpointFactory,
            ActivationSpec spec) throws NotSupportedException {
        logger.finest("[RA.endpointActivation()] Entered");

        try {
            EndpointConsumer ec = new EndpointConsumer(endpointFactory,
                    (ActivationSpecImpl) spec);
            ((PollingThread) pollingThread).addEndpointConsumer(
                    endpointFactory, ec);
        } catch (Exception ex) {
            logger.finest("[RA.endpointActivation()] An Exception was caught while activating the endpoint");
            logger.finest("[RA.endpointActivation()] Please check the server logs for details");
            NotSupportedException newEx = new NotSupportedException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.endpoint_activation_fail"),
                            new Object[] { ex.getMessage() }));
            newEx.initCause(ex);
            // throw newEx; // UNCOMMENT THIS LINE TO LET THE SERVER KNOW an
            // error happened
            // Has been commented out to avoid deployment failures during QA
            // testing
            // As a real user/password is required on ejb-jar.xml
        }
    }

    /**
     * Called by Application Server when the MessageEndpoint (message-driven
     * bean) is undeployed. The instance passed as arguments to this method call
     * should be identical to that passed in for the corresponding
     * endpointActivation call. This causes the resource adapter to stop
     * delivering messages to the message endpoint.
     * 
     * @param endpointFactory
     *            a message endpoint factory instance
     * @param spec
     *            an activation spec instance
     */

    public void endpointDeactivation(MessageEndpointFactory endpointFactory,
            ActivationSpec spec) {
        logger.finest("[RA.endpointdeactivation()] Entered");

        ((PollingThread) pollingThread).removeEndpointConsumer(endpointFactory);

    }

    /**
     * This method is called by the Application Server on the restart of the
     * Application Server when there are potential pending transactions. For
     * example, it may be called after a server crash. The Application Server
     * requests the XA Resources that correspond to the Activation Specs for the
     * endpoints that it is restarting. It may use those XA Resources to
     * determine transaction status and attempt to commit or rollback.
     * 
     * Because this implementation does not support transactions, this method
     * does nothing.
     * 
     * @param specs
     *            an array of ActivationSpec objects
     * 
     * @return an XAResource
     */

    public javax.transaction.xa.XAResource[] getXAResources(
            ActivationSpec[] specs) throws ResourceException {
        /*
         * Do nothing
         */

        return null;
    }

}
