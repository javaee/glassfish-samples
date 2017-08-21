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
package org.glassfish.servlet.http_upgrade_war;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;

/**
 * This class illustrates the implementation of HTTPUpgradeHandler
 * @author Daniel
 */
public class ProtocolUpgradeHandler implements HttpUpgradeHandler {

    private static final String CRLF = "\r\n";
    private static final byte[] echoData = new byte[128];
    private WebConnection wc = null;

    @Override
    public void init(WebConnection wc) {
        this.wc = wc;
        try {

            ServletOutputStream output = wc.getOutputStream();
            ServletInputStream input = wc.getInputStream();
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            // Reading the data into byte array
            input.read(echoData);

            // Setting new protocol header 
            String resStr = "Dummy Protocol/1.0 " + CRLF;
            resStr += "Server: Glassfish/ServerTest" + CRLF;
            resStr += "Content-Type: text/html" + CRLF;
            resStr += "Connection: Upgrade" + CRLF;
            resStr += "Date: " + dateFormat.format(calendar.getTime()) + CRLF;
            resStr += CRLF;
            // Appending data with new protocol
            resStr += new String(echoData) + CRLF;
            
            // Sending back to client
            System.out.println("Echo data is: " + new String(echoData));
            output.write(resStr.getBytes());
            output.flush();

        } catch (IOException ex) {
            Logger.getLogger(ProtocolUpgradeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void destroy() {
        try {
            wc.close();
        } catch (Exception ex) {
            Logger.getLogger(ProtocolUpgradeHandler.class.getName()).log(Level.SEVERE, 
                    "Failed to close connection", ex);
        }
    }
}
