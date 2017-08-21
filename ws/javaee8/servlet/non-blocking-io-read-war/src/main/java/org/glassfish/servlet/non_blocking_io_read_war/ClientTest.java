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
package org.glassfish.servlet.non_blocking_io_read_war;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet acts as client to create HTTP connection to server, sends two
 * parts of data with 3 seconds sleeping in between, in order to simulate data
 * blocking.
 * @author Daniel Guo
 */
@WebServlet(name = "ClientTest", urlPatterns = {"/"})
public class ClientTest extends HttpServlet {

    OutputStream output = null;
    InputStream input = null;

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

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Non-blocking-read-war</title>");
        out.println("</head>");
        out.println("<body>");

        String urlPath = "http://"
                + request.getServerName()
                + ":" + request.getLocalPort() //default http port is 8080
                + request.getContextPath()
                + "/ServerTest";

        URL url = new URL(urlPath);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setChunkedStreamingMode(2);
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.connect();

        try {
            output = conn.getOutputStream();
            // Sending the first part of data to server
            String firstPart = "Hello";
            out.println("Sending to server: " + firstPart + "</br>");
            out.flush();
            writeData(output, firstPart);

            Thread.sleep(2000);

            // Sending the second part of data to server
            String secondPart = "World";
            out.println("Sending to server: " + secondPart + "</br></br>");
            out.flush();
            writeData(output, secondPart);

            // Getting the echo data from server
            input = conn.getInputStream();
            printEchoData(out, input);

            out.println("Please check server log for detail");
            out.flush();
        } catch (IOException ioException) {
            Logger.getLogger(ReadListenerImpl.class.getName()).log(Level.SEVERE,
                    "Please check the connection or url path", ioException);
        } catch (InterruptedException interruptedException) {
            Logger.getLogger(ReadListenerImpl.class.getName()).log(Level.SEVERE,
                    "Thread sleeping error", interruptedException);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (Exception ex) {
                }
            }
        }

        out.println("</body>");
        out.println("</html>");

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

    protected void writeData(OutputStream output, String data) throws IOException {
        if (data != null && !data.equals("") && output != null) {
            output.write(data.getBytes());
            output.flush();
        }
    }

    protected void printEchoData(PrintWriter out, InputStream input) throws IOException {
        while (input.available() > 0 && input != null && out != null) {
            out.print((char) input.read());
        }
        out.println("</br>");
    }
}
