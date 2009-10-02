/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package enterprise.web_security_annotation;

import java.io.*;


import java.security.Principal;

import javax.annotation.security.DeclareRoles;

import javax.annotation.security.PermitAll;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author nithyasubramanian
 */
@WebServlet("/annotate")
@DeclareRoles({"javaee6user"})
@PermitAll
public class AnnotationServlet extends HttpServlet {
   
   

    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        Principal userPrincipal = request.getUserPrincipal();
        String userName = userPrincipal.getName();
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // TODO output your page here
            out.println("<html>");
            out.println("<body>");
            out.println("Welcome " + userName); 
            out.println("</body>");
            out.println("</html>");
            
        } finally { 
            out.close();
        }
       
        
        
    } 

    @RolesAllowed("javaee6user")
    //@TransportProtected
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 
    
    

   @DenyAll
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    

}
