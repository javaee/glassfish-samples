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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet acts as Client to illustrate the handshake process for HTTP
 * upgrade
 * @author Daniel
 */
@WebServlet(name = "ClientTest", urlPatterns = {"/"})
public class ClientTest extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        final String CRLF = "\r\n";
        final String host = request.getServerName();// "localhost";
        final int port = request.getServerPort(); // 8080;
        final String contextRoot = "/http-upgrade-war";
        final String Data = "Hello World";
        InputStream input = null;
        OutputStream output = null;
        BufferedReader reader = null;
        Socket s = null;

        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ClientTest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Http Upgrade Process</h1>");


            // Setting the HTTP upgrade request header
            String reqStr = "POST " + contextRoot + "/ServerTest HTTP/1.1" + CRLF;
            reqStr += "User-Agent: Java/1.7" + CRLF;
            reqStr += "Host: " + host + ":" + port + CRLF;
            reqStr += "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2" + CRLF;
            reqStr += "Upgrade: Dummy Protocol" + CRLF;
            reqStr += "Connection: Upgrade" + CRLF;
            reqStr += "Content-type: application/x-www-form-urlencoded" + CRLF;
            reqStr += "Transfer-Encoding: chunked" + CRLF;
            reqStr += CRLF;
            reqStr += Data + CRLF;

            // Create socket connection to ServerTest
            s = new Socket(host, port);
            input = s.getInputStream();
            output = s.getOutputStream();

            // Send request header with data
            output.write(reqStr.getBytes());
            output.flush();

            out.println("<h2>Sending upgrade request to server......</h2>");
            out.println("<h3>Request header with data:</h3>");
            out.println();

            String reqStrDisplay = reqStr.replaceAll("\r\n", "</br>");
            out.println(reqStrDisplay);
            out.println("--------------------------------------- </br></br>");
            out.flush();

            reader = new BufferedReader(new InputStreamReader(input));

            out.println("<h2>Server accept upgrade request, send back the response:</h2>");
            out.println("<h3>Response header:</h3>");
            out.println();

            // Reading the response, and displaying the header from server
            printHeader(reader, out);

            out.println("--------------------------------------- </br></br>");
            out.flush();

            out.println("<h2>Server send back the response with new protocol and data:</h2>");
            out.println("<h3>Response header with data:</h3>");

            // Reading the response, and displaying the header from server
            printHeader(reader, out);

            // Reading the echo data
            String dataOutput;
            if ((dataOutput = reader.readLine()) != null) {
                // Print out the data after header
                out.println("</br>" + dataOutput + "</br>");
                out.println("--------------------------------------- </br></br>");
                out.println("<h2>Connection with new protocol established</h2>");
            }
            out.flush();
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ioException) {
            Logger.getLogger(ProtocolUpgradeHandler.class.getName()).log(Level.SEVERE, null, ioException);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (s != null) {
                s.close();
            }
        }
    }

    protected void printHeader(BufferedReader reader, PrintWriter out) throws IOException {
        for (String line; (line = reader.readLine()) != null;) {
            if (line.isEmpty()) {
                break; // Stop when headers are completed.
            }
            out.println(line + "</br>");
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
