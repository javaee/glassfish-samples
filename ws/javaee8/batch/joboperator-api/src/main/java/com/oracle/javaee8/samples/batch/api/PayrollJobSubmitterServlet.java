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
package com.oracle.javaee8.samples.batch.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author makannan
 */
public class PayrollJobSubmitterServlet extends HttpServlet {
    
    @EJB
    SampleDataHolderBean initializer;

    @Inject
    private SimpleLock simpleLock;

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
        PrintWriter pw = response.getWriter();
        try {
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Sample Batch application demonstrating JobOperator API</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<h1>Sample Batch application demonstrating JobOperator API</h1>");
            pw.println("<p>This sample application, submits a batch job that performs payroll processing for the selected month.</p>");
            pw.println("<p>To run, first select a month and click 'Calculate Payroll'. This will submit a batch job. Each execution will");
            pw.println("</br> create a new JobExecution with an associated unique executionID. The table below will list details about each job execution.</p>");

            String origSelectedMonthYear = request.getParameter("inputMonthYear");
            String selectedMonthYear = origSelectedMonthYear == null
                ? "JAN-2013" : origSelectedMonthYear;
            if (request.getParameter("calculatePayroll") != null) {
                submitJobFromXML("PayrollJob", selectedMonthYear);
            }
            displayPayrollForm(pw, selectedMonthYear);
            displayJobDetails(pw);

            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            pw.close();
        }
    }

    private void displayPayrollForm(PrintWriter pw, String monthYear) {
        pw.println("<form>");
        pw.println("Calculate Payroll for the month of: ");
        pw.println("<select name=\"inputMonthYear\">");
        for (String monYr : initializer.getAllMonthYear()) {
            pw.println("<option " + (monYr.equals(monthYear) ? "Selected" : " ")
                    + " value=\"" + monYr +"\">" + monYr + "</option>");
        }
        pw.println("<td><input type=\"submit\" name=\"calculatePayroll\" value=\"Calculate Payroll\"/></td></tr>");
        pw.println("<td><input type=\"submit\" name=\"refresh\" value=\"refresh\"/></td></tr>");
        pw.println("</table>");
        pw.println("</form>");
    }

    private void displayJobDetails(PrintWriter pw) {
        pw.println("<table>");
        pw.println("<tr><td>Status of Submitted Jobs</td></tr>");
        pw.println("<table border=\"yes\">");
        pw.println("<tr><td>Job Name</td><td>ExecutionID</td>"
                + "<td>Batch Status</td><td>Exit Status</td>"
                + "<td>Start Time Status</td><td>End Time</td>"
                    + "</tr>");

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        try {
            for (JobInstance jobInstance : jobOperator.getJobInstances("payroll", 0, Integer.MAX_VALUE-1)) {
                for (JobExecution jobExecution : jobOperator.getJobExecutions(jobInstance)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<tr>");
                    sb.append("<td>").append(jobExecution.getJobName()).append("</td>");
                    sb.append("<td>").append(jobExecution.getExecutionId()).append("</td>");
                    sb.append("<td>").append(jobExecution.getBatchStatus()).append("</td>");
                    sb.append("<td>").append(jobExecution.getExitStatus()).append("</td>");
                    sb.append("<td>").append(jobExecution.getStartTime()).append("</td>");
                    sb.append("<td>").append(jobExecution.getEndTime()).append("</td>");
                    pw.println(sb.toString());
                }
            }
        } catch (Exception ex) {

        }
        pw.println("</table>");
        pw.println("</table>");
    }

    private long submitJobFromXML(String jobName, String selectedMonthYear)
            throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();

        Properties props = new Properties();
        props.setProperty("monthYear", selectedMonthYear);

        return jobOperator.start(jobName, props);
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
