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

package transactional;

import javax.interceptor.InvocationContext;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.transaction.*;
import java.io.IOException;


@WebServlet(name="TransactionalServlet", urlPatterns={"/TransactionalServlet"})
public class TransactionalServlet extends HttpServlet {

 	public static ServletOutputStream m_out;
 	
 	@Inject
    UserTransaction userTransaction;
    
    @Inject
    BeanMandatory beanMandatory;
    
    @Inject
    BeanNever beanNever;
    
    @Inject
    BeanNotSupported beanNotSupported;
    
    @Inject
    BeanRequired beanRequired;
    
    @Inject
    BeanRequiresNew beanRequiresNew;
    
    @Inject
    BeanSupports beanSupports;
    

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String transactionalInterceptor = request.getParameter("TransactionalInterceptor");
		
    	m_out = response.getOutputStream();
    	m_out.println("<HTML>");
    	m_out.println("<HEAD>");
    	m_out.println("<title>CDI Sample Application for TransactionScoped Annotation</title>");
   	 	m_out.println("</HEAD>");
    	m_out.println("<BODY>");
    	m_out.println("TransactionalInterceptor value is -> " + transactionalInterceptor);
    	m_out.println("<BR><BR>");
		
        if (transactionalInterceptor.equalsIgnoreCase("MANDATORY")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Should get an error.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanMandatory.getId());
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println("<BR><BR>");
			} catch (Exception transactionalException) {
				if (transactionalException.getCause() instanceof TransactionRequiredException) {
					m_out.println("Got TransactionRequiredException for transactionalException.getCause() as expected.");
					m_out.println("<BR><BR>");
				} else {
					m_out.println("If you see this, it means there is something wrong!");
					m_out.println(transactionalException.getMessage());
					m_out.println("<BR><BR>");
				}
			}
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanMandatory.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();    			
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else if (transactionalInterceptor.equalsIgnoreCase("NEVER")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction.</b>"); 
				m_out.println("<BR><BR>");
				m_out.println(beanNever.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. Should get an error.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanNever.getId());
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println("<BR><BR>");
			} catch (Exception transactionalException) {					
				if (transactionalException.getCause() instanceof InvalidTransactionException) {
					m_out.println("Got InvalidTransactionException for transactionalException.getCause() as expected.");
					m_out.println("<BR><BR>");
				} else {
					m_out.println("If you see this, it means there is something wrong!");
					m_out.println(transactionalException.getMessage());
					m_out.println("<BR><BR>");
				}
			} finally {
				try {
            		userTransaction.rollback();
            	} catch (Exception e) {
            		m_out.println("Got unexpected exception in finally rollback for NEVER" + e.getMessage());
            	}
       		}
        } else if (transactionalInterceptor.equalsIgnoreCase("NOT_SUPPORTED")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction.</b>"); 
				m_out.println("<BR><BR>");
				m_out.println(beanNotSupported.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. Transaction is suspended during the method call. </b>");
				m_out.println("<BR><BR>");
				m_out.println(beanNotSupported.getId());
				userTransaction.commit();
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else if (transactionalInterceptor.equalsIgnoreCase("REQUIRED")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Transaction would be started automatically for the method call.</b>"); 
				m_out.println("<BR><BR>");
				m_out.println(beanRequired.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanRequired.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else  if (transactionalInterceptor.equalsIgnoreCase("REQUIRES_NEW")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Transaction would be started automatically for the method call.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanRequiresNew.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. NEW Transaction would be started automatically for the method call. </b>");
				m_out.println("<BR><BR>");
				m_out.println(beanRequiresNew.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else  if (transactionalInterceptor.equalsIgnoreCase("SUPPORTS")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Method is executed outside transaction. </b>");
				m_out.println("<BR><BR>");
				m_out.println(beanSupports.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. Method is executed within transaction context.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanSupports.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } 
        
        m_out.println("</BODY>");
    	m_out.println("</HTML>");
    }
    
    /** 
     * Handles the HTTP <code>GET</code> method.
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
