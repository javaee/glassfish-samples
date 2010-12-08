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

package enterprise.locking.servlet;

import enterprise.locking.ejb.StatelessSessionBean;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="TestServlet", urlPatterns={"/test/*"})
public class TestServlet extends HttpServlet {

    @EJB
    private StatelessSessionBean testEJB;

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        int thinkTime = 2; // Seconds
        try {
            out.println("TestServlet at " + request.getContextPath());
            String testcase = request.getParameter("tc");
            out.println("testcase = " + testcase);
            if ("initData".equals(testcase)) {

                // Step 1. initialize Data
                int numberOfConsumers = Integer.parseInt(request.getParameter("nc"));
                int numberOfSuppliers = Integer.parseInt(request.getParameter("ns"));
                int numberOfParts = Integer.parseInt(request.getParameter("np"));
                // ----------------------------------------
                // Populate test data
                testEJB.initData(numberOfConsumers, numberOfSuppliers, numberOfParts);
                out.println("MethodResult=" + Boolean.TRUE);

            } else if ("updateWOL".equals(testcase)) {

                // 2. update With Optimistic Locking with multiple user access
                String userID = request.getParameter("uid");
                int uID = Integer.parseInt(userID);
                // ----------------------------------------
                // Test Optimistic Locking for Version Object (Part).
                // Expect that only some updates will go through
                boolean updateResult = testEJB.updateWithOptimisticLock(uID, thinkTime);
                out.println("MethodResult=" + updateResult);

            } else if ("updateWPL".equals(testcase)) {

                // 4. update With Pessimistic Locking with multiple user access
                String userID = request.getParameter("uid");
                int uID = Integer.parseInt(userID);
                // ----------------------------------------
                // Test Pessimistic Locking for Version Object
                // Expect that all updates will go through
                boolean updateResult = testEJB.updateWithPessimisticLock(uID, thinkTime);
                out.println("MethodResult=" + updateResult);
                // ----------------------------------------

            } else {
                String invalidTestCaseMessage = "Invalid test case: " + testcase;
                System.out.println("Invalid test case: " + testcase);
                out.println(invalidTestCaseMessage);
            }
        } catch (Exception ex) {
            System.out.println("Failure in TestServlet");
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "TestServlet";
    }


}
