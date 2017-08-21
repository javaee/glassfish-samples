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

import javax.mail.*;

import java.util.*;
import java.util.logging.*;
// import samples.connectors.mailconnector.ra.outbound.*;
import samples.connectors.mailconnector.share.*;

/**
 * @author Alejandro Murillo
 */

public class MailServerStore {
    private javax.mail.Session       session;
    private javax.mail.Authenticator authenticator;
    private javax.mail.Store         store;
    private Properties               mailProperties;

    private String                   userName, password, serverName, protocol;

    public static final Logger  logger = Logger.getLogger("samples.connectors.mailconnector.ra.outbound");

    /**
     * Constructor.
     * 
     * @param spec
     *            the ConnectionSpec (ConnectionRequestInfo)
     */
    public MailServerStore(ConnectionSpecImpl spec) throws Exception {
        userName = spec.getUserName();
        password = spec.getPassword();
        serverName = spec.getServerName();
        protocol = spec.getProtocol();

        this.authenticator = null;

        mailProperties = new Properties();
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.store.protocol", protocol);
        mailProperties.setProperty("mail.smtp.host", serverName);

        connectStore();
    }

    /**
     * Closes the Store.
     * 
     * @exception Exception
     *                if the close fails
     */

    public void closeStore() throws Exception {
        /*
         * The JavaMail Session object does not have an explicit close.
         */

        // logger.info("Listener::close()");
        this.store.close();
        this.store = null;
        this.authenticator = null;
        this.session = null;
    }

    /**
     * Opens a connection to the mail server. Associated with a MC
     * 
     * @exception Exception
     *                if the open fails
     */

    private void connectStore() throws Exception {
        try {
            // Get a session object
            session = javax.mail.Session.getDefaultInstance(mailProperties);
            
            // Get a store object
            store = session.getStore("imap");
            this.store.connect(serverName, userName, password);
        } catch (Exception te) {
            logger.log(Level.SEVERE, "[S] Caught an exception when obtaining a JavaMail Session", te);
            throw new Exception(te.getMessage());
        }
    }

    public javax.mail.Folder getFolder(String folderName) throws Exception {
        javax.mail.Folder folder;
        folder = this.store.getFolder(folderName);

        if ((folder == null) || (!folder.exists())) {
            Exception e = new Exception("Folder " + folderName  + " does not exist or is not found");
            throw e;
        }

        return folder;
    }

    /**
     * Retrieves new messages. Used by a JavaMailConnection
     * 
     * @return an array of messages
     */

    public Message[] getNewMessages(javax.mail.Folder folder) throws Exception {
        if ((folder == null) || (!folder.exists())) {
            Exception e = new Exception("Folder " + folder
                    + " does not exist or is not found");
            throw e;
        }

        if (!folder.isOpen()) {
            folder.open(javax.mail.Folder.READ_WRITE);
        }

        //
        // Deliver only new messages to the MDB
        //
        try {
            int newMsgs = folder.getNewMessageCount();
            if (newMsgs > 0) {
                int msgCount = folder.getMessageCount();
                Message msgs[] = folder.getMessages(msgCount - newMsgs + 1, msgCount);
                return msgs;
            }
        } catch (Exception e) {
            logger.info("[S] Exception obtaining messages from mail server");
        }
        return null;
    }

    /**
     * Retrieves headers of new messages.
     * 
     * @return a string array containing the message headers
     */

    public String[] getNewMessageHeaders(javax.mail.Folder folder)
            throws Exception {
        if ((folder == null) || (!folder.exists())) {
            Exception e = new Exception("Folder " + folder
                    + " does not exist or is not found");
            throw e;
        }

        if (!folder.isOpen()) {
            folder.open(javax.mail.Folder.READ_WRITE);
        }
        //
        // Deliver only new messages to the MDB
        //

        try {
            int newMsgs = folder.getNewMessageCount();

            if (newMsgs > 0) {
                int msgCount = folder.getMessageCount();
                Message[] msgs = folder.getMessages(msgCount - newMsgs + 1,
                        msgCount);
                String[] headers = new String[msgs.length];
                logger.info("messages length: " + msgs.length);
                logger.info("headers length: " + headers.length);
                for (int i = 0; i < headers.length; i++) {
                    logger.info("<MSF> Packing message with SUBJECT: "
                            + msgs[i].getSubject());
                    headers[i] = msgs[i].getSubject();
                }
                return headers;
            }
        } catch (Exception e) {
            logger.severe("[S] Exception obtaining messages from mail server:");
            e.printStackTrace();
        }
        return null;
    }

    public boolean isTheSameStore(ConnectionRequestInfoImpl cxRequestInfo) {
        if (!userName.equals(cxRequestInfo.getUserName()))
            return false;
        if (!password.equals(cxRequestInfo.getPassword()))
            return false;
        if (!serverName.equals(cxRequestInfo.getServerName()))
            return false;
        if (!protocol.equals(cxRequestInfo.getProtocol()))
            return false;

        logger.info("isTheSameStore: found match!");
        return true;
    }
}
