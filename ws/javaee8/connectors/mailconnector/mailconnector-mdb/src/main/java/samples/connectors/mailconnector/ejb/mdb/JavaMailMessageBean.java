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

package samples.connectors.mailconnector.ejb.mdb;

import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import java.util.*;
import java.util.logging.*;
import java.text.*;
import javax.naming.*;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

import samples.connectors.mailconnector.api.*;

/**
 * A simple message-driven bean. The code illustrates the requirements of a
 * message-driven bean class:
 * <ul>
 * <li>It implements the <code>JavaMailMessageListener</code> interface.
 * <li>The class is defined as <code>public</code>.
 * <li>The class cannot be defined as <code>abstract</code> or
 * <code>final</code>.
 * <li>It implements one <code>onMessage</code> method.
 * <li>It implements one <code>ejbCreate</code> method and one
 * <code>ejbRemove</code> method.
 * <li>It contains a public constructor with no arguments.
 * <li>It must not define the <code>finalize</code> method.
 * </ul>
 * Unlike session and entity beans, message-driven beans do not have the remote
 * or local interfaces that define client access. Client components do not
 * locate message-driven beans and invoke methods on them. Although
 * message-driven beans do not have business methods, they may contain helper
 * methods that are invoked internally by the onMessage method.
 */
@MessageDriven(messageListenerInterface=JavaMailMessageListener.class,
        name="JavaMailMDB",
        activationConfig = {
        @ActivationConfigProperty(propertyName = "serverName", propertyValue = "localhost"),
        @ActivationConfigProperty(propertyName = "userName", propertyValue = "joe"),
        @ActivationConfigProperty(propertyName = "password", propertyValue = "joe"),
        @ActivationConfigProperty(propertyName = "folderName", propertyValue = "INBOX"),
        @ActivationConfigProperty(propertyName = "protocol", propertyValue = "IMAP"),
        @ActivationConfigProperty(propertyName = "interval", propertyValue = "30") })
@TransactionManagement(TransactionManagementType.CONTAINER)

public class JavaMailMessageBean implements MessageDrivenBean,
        JavaMailMessageListener {
    static final Logger logger = Logger.getLogger("samples.connectors.mailconnector.ejb.mdb");

    private transient MessageDrivenContext mdc    = null;
    private Context context;

    /**
     * Default constructor. Creates a bean.
     */

    public JavaMailMessageBean() {
        logger.info("<MDB> In JavaMailMessageBean.JavaMailMessageBean()");
    }

    /**
     * Sets the context for the bean.
     * 
     * @param mdc
     *            the message-driven-bean context
     */

    public void setMessageDrivenContext(MessageDrivenContext mdc) {
        logger.info("<MDB> In JavaMailMessageBean.setMessageDrivenContext()");
        this.mdc = mdc;
    }

    /**
     * Creates a bean. Required by EJB spec.
     */

    public void ejbCreate() {
        logger.info("<MDB> In JavaMailMessageBean.ejbCreate()");
    }

    /**
     * When the inbox receives a message, the EJB container invokes the
     * <code>onMessage</code> method of the message-driven bean. The
     * <code>onMessage</code> method displays information from the message
     * headers, formulates a reply to the message, and sends it. (The code to
     * send the reply is currently commented out.)
     * 
     * @param message
     *            incoming message
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onMessage(javax.mail.Message message) {
        final String mailer = "JavaMailer";

        try {
            logger.info("<MDB> ---- Got a message ");
            logger.info("<MDB> SUBJECT: " + message.getSubject());

            context = new InitialContext();
            Session session = (Session) context
                    .lookup("java:app/env/TheMailSession");

            Message reply = new MimeMessage(session);
            reply.setFrom();

            Address[] addresses = message.getFrom();
            if ((addresses != null) || (addresses.length < 1)) {
                addresses = message.getReplyTo();
            }

            logger.info("<MDB> SENDER : " + addresses[0].toString());

            Address[] recepients = message.getFrom();
            if (recepients != null && recepients.length > 0) {
                String recepientId = recepients[0].toString();
                if (recepientId.indexOf("@") > 0) {
                    recepientId = recepientId.substring(0,
                            recepientId.indexOf("@"));
                }
                // logger.info("<MDB> isCallerInRole("+recepientId+") : " +
                // mdc.isCallerInRole(recepientId));
                logger.info("<MDB> getCallerPrincipal() : "
                        + mdc.getCallerPrincipal());
            }

            reply.setRecipients(Message.RecipientType.TO, addresses);
            reply.setSubject("MDB reply RE: " + message.getSubject());

            DateFormat dateFormatter = DateFormat.getDateTimeInstance(
                    DateFormat.LONG, DateFormat.SHORT);

            Date timeStamp = new Date();

            String messageText = "Dear " + addresses[0].toString() + '\n'
                    + "Thank you for your email." + '\n'
                    + "We received your message on "
                    + dateFormatter.format(timeStamp) + ".";

            reply.setText(messageText);

            reply.setHeader("X-Mailer", mailer);
            reply.setSentDate(timeStamp);

            // We used to reply to the sender, but this might
            // send undesired replies when testing with a busy
            // mailbox. Uncomment the following line if you wish
            // to do that.

            // Transport.send(reply);

        } catch (Exception e) {
            // logger.info("<MDB> Could not send the email: " + e.toString());
            throw new EJBException(e.getMessage());
        }
    } // onMessage

    /**
     * Removes the bean. Required by EJB specification.
     */

    public void ejbRemove() {
        logger.info("<MDB> In JavaMailMessageBean.remove()");
    }

} // class
