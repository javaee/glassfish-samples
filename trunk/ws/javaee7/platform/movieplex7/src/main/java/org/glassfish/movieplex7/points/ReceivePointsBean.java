/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.movieplex7.points;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/**
 * @author Arun Gupta
 */
@JMSDestinationDefinition(name = "java:global/jms/pointsQueue",
        interfaceName = "javax.jms.Queue")
@Named
@SessionScoped
public class ReceivePointsBean implements Serializable {

    @Inject
//    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    JMSContext context;
    @Resource(mappedName = "java:global/jms/pointsQueue")
    Queue pointsQueue;

    public String receiveMessage() {
        String message = context.createConsumer(pointsQueue).receiveBody(String.class);
        System.out.println("Received message: " + message);
        return message;
    }

    public int getQueueSize() {
        int count = 0;
        try {
            QueueBrowser browser = context.createBrowser(pointsQueue);
            Enumeration elems = browser.getEnumeration();
            while (elems.hasMoreElements()) {
                elems.nextElement();
                count++;
            }
        } catch (JMSException ex) {
            Logger.getLogger(ReceivePointsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Getting queue size: " + count);
        return count;
    }
}
