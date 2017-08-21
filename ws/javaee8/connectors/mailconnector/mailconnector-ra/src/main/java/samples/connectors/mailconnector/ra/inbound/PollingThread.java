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

import javax.resource.NotSupportedException;
import javax.resource.spi.endpoint.*;
import javax.resource.spi.work.*;

import javax.mail.*;

import java.util.*;
import java.util.logging.*;

/**
 * @author Alejandro Murillo
 */

public class PollingThread implements Work {
    public static final Logger      logger            = Logger.getLogger("samples.connectors.mailconnector.ra.inbound");
    static ResourceBundle           resource          = java.util.ResourceBundle
                                                              .getBundle("samples.connectors.mailconnector.ra.inbound.LocalStrings");

    private boolean                 active            = false;
    protected transient WorkManager workManager;
    private transient HashMap       endpointConsumers = null;

    private static int              QUANTUM           = 30;                                                                          // 30
                                                                                                                                      // Seconds

    /**
     * Constructor.
     */

    public PollingThread(WorkManager workManager) {
        this.active = true;
        this.workManager = workManager;

        /*
         * Set up the hash tables for the use of the resource adapter. These
         * tables hold references to MessageEndpointFactory and
         * endpointConsumers. The factoryToConsumer table links the Message
         * factory id to the Consumer Id.
         */

        endpointConsumers = new HashMap(10);

        logger.info("[PollingThread::Constructor] Leaving");
    }

    /**
     * release: called by the WorkerManager
     */

    public void release() {
        logger.info("[S] Worker Manager called release for PollingThread ");
        active = false;
    }

    /**
     * run
     */

    public void run() {
        logger.info("[PT] WorkManager started polling thread ");

        // do not overuse system resources
        // setPriority(Thread.MIN_PRIORITY);

        while (active) {
            try {
                pollEndpoints();
                Thread.sleep(QUANTUM * 1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.fine("[PT] Polling Thread Leaving");
    }

    private void pollEndpoints() {
        logger.fine("[PT] Polling endpoints entering");

        synchronized (endpointConsumers) {
            Collection consumers = endpointConsumers.entrySet();

            if (consumers != null) {
                Iterator iter = consumers.iterator();

                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    EndpointConsumer ec = (EndpointConsumer) entry.getValue();
                    try {
                        if (ec.hasNewMessages()) {
                            Message[] messages = ec.getNewMessages();
                            if (messages != null) {
                                for (Message msg : messages) {
                                    scheduleMessageDeliveryThread(ec, msg);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        logger.fine("[PT] Polling endpoints Leaving");
    }

    /**
     * @param message
     *            the message to be delivered
     */

    private void scheduleMessageDeliveryThread(EndpointConsumer ec, Message msg)
            throws Exception {
        logger.info("[PT] scheduling a delivery FROM: " + ec.getUniqueKey());
        try {
            Work deliveryThread = new DeliveryThread(ec, msg);
            workManager.scheduleWork(deliveryThread);
        } catch (WorkRejectedException ex) {
            NotSupportedException newEx = new NotSupportedException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.worker_activation_rejected"),
                            new Object[] { ex.getMessage() }));
            newEx.initCause(ex);
            throw newEx;
        } catch (Exception ex) {
            NotSupportedException newEx = new NotSupportedException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.worker_activation_failed"),
                            new Object[] { ex.getMessage() }));

            newEx.initCause(ex);
            throw newEx;
        }
    }

    public void stopPolling() {
        removeAllEndpointConsumers();
        this.active = false;
    }

    public void addEndpointConsumer(MessageEndpointFactory endpointFactory,
            EndpointConsumer ec) {
        logger.finest("[PT.addEndpointConsumer()] Entered");

        synchronized (endpointConsumers) {
            endpointConsumers.put(endpointFactory, ec);
        }
    }

    public void removeEndpointConsumer(MessageEndpointFactory endpointFactory) {
        logger.finest("[PT.removeEndpointConsumer()] Entered");

        EndpointConsumer ec = (EndpointConsumer) endpointConsumers
                .get(endpointFactory);

        synchronized (endpointConsumers) {
            endpointConsumers.remove(ec);
        }
    }

    /**
     * Iterates through the endpointConsumers, shutting them down and preparing
     * for stopping the Resource Adapter.
     */

    private void removeAllEndpointConsumers() {
        synchronized (endpointConsumers) {
            Collection consumers = endpointConsumers.entrySet();

            if (consumers != null) {
                Iterator iter = consumers.iterator();

                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    EndpointConsumer ec = (EndpointConsumer) entry.getValue();
                    try {
                        endpointConsumers.remove(ec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        endpointConsumers = null;
    }
}
