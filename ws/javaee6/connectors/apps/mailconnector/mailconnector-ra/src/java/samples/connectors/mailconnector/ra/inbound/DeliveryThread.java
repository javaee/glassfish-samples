/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
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


import java.util.*;
import java.util.logging.*;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.resource.spi.work.*;

/**
 * 
 * @author Alejandro Murillo
 *
 */

public class DeliveryThread  implements Work, WorkContextProvider
{
    public static final Logger  logger = 
        Logger.getLogger("samples.connectors.mailconnector.ra.inbound");
    static ResourceBundle 	resource = 
        java.util.ResourceBundle.getBundle("samples.connectors.mailconnector.ra.inbound.LocalStrings"); 

    private EndpointConsumer  endpointConsumer;
    private Message msg;
    private List<WorkContext> workContexts = new ArrayList<WorkContext>();
    
    /**
     * Constructor.
     */

    public DeliveryThread(EndpointConsumer endpointConsumer, Message msg)
    {
        this.endpointConsumer = endpointConsumer;
        this.msg = msg;
        initializeWorkContexts(msg);
        logger.fine("[DeliveryThread::Constructor] Leaving");

    }

    /**
     * release: called by the WorkerManager
     */

    public void release()
    {
        logger.fine("[DT] Worker Manager called release for deliveryThread ");
    }

    /**
     * run
     */

    public void run()
    { 
	logger.fine("[DT] WorkManager started delivery thread ");
                
        try
	{
            endpointConsumer.deliverMessage(msg);
        } catch (Exception te) {
            logger.info("deliveryThread::run got an exception");
            te.printStackTrace();
        }
        
	logger.fine("[DT] DeliveryThread leaving");
    }

    public List<WorkContext> getWorkContexts() {
        return workContexts;
    }

    private void initializeWorkContexts(Message msg) {
        try {
            Address[] recepients = msg.getFrom();
            if(recepients != null && recepients.length > 0){
                //Let us consider first recepient alone.
                Address recepient = recepients[0];
                String recepientId = recepient.toString();

                if(recepientId.indexOf("@") > 0){
                    recepientId = recepientId.substring(0,recepientId.indexOf("@"));
                }

                //Assuming that the password is same as username
                MySecurityContext mysc = new MySecurityContext(recepientId, recepientId, recepientId);
                getWorkContexts().add(mysc);
            }
        } catch (MessagingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }

}
