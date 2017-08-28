/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 */
package org.glassfish.servlet.mapping_discovery_war;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ejburns
 */
@WebServlet(name = "ServletA",
        urlPatterns = {
            "/ServletA",
            "/AForwardToB",
            "/AIncludesB",
            "/AAsyncDispatchToB",
            "*.a"
        },
        asyncSupported = true
)
public class ServletA extends HttpServlet {

    public static void printCurrentMappingDetails(HttpServletRequest request,
            PrintWriter out) throws IOException {
            HttpServletMapping forwardMapping = (HttpServletMapping) request.getAttribute(RequestDispatcher.FORWARD_MAPPING);
            HttpServletMapping includeMapping = (HttpServletMapping) request.getAttribute(RequestDispatcher.INCLUDE_MAPPING);
            HttpServletMapping asyncMapping = (HttpServletMapping) request.getAttribute(AsyncContext.ASYNC_MAPPING);

            out.print("<p> " + request.getHttpServletMapping() + "</p>");
            out.print("<p> FORWARD_MAPPING: " + forwardMapping + "</p>");
            out.print("<p> INCLUDE_MAPPING: " + includeMapping + "</p>");
            out.print("<p> ASYNC_MAPPING: " + asyncMapping + "</p>");
            out.print("<hr />");


    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpServletMapping mapping = request.getHttpServletMapping();

        if (mapping.getPattern().equals("/AForwardToB")) {
            RequestDispatcher rd = request.getRequestDispatcher("/ServletB");
            rd.forward(request, response);
            return;
        }

        if (mapping.getPattern().equals("/AAsyncDispatchToB")) {
            AsyncContext asyncContext = request.startAsync();
            asyncContext.dispatch("/ServletB");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletA</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletA at " + request.getContextPath() + "</h1>");

            printCurrentMappingDetails(request, out);

            if (mapping.getPattern().equals("/AIncludesB")) {
                RequestDispatcher rd = request.getRequestDispatcher("/ServletB");
                rd.include(request, response);
            }

            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
