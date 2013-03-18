/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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
package transactionscoped;

import javax.enterprise.context.ContextNotActiveException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.*;
import java.io.IOException;

@WebServlet(name="TransactionScopedServlet", urlPatterns={"/TransactionScopedServlet"})
public class TransactionScopedServlet extends HttpServlet {
  public static ServletOutputStream m_out;

  @Inject
  UserTransaction userTransaction;

  @Inject
  Bean1 bean1;

  @Inject
  Bean1 bean1_1;

  @Inject
  Bean2 bean2;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    m_out = response.getOutputStream();
    m_out.println("<HTML>");
    m_out.println("<HEAD>");
    m_out.println("<title>CDI Sample Application for TransactionScoped Annotation</title>");
    m_out.println("</HEAD>");
    m_out.println("<BODY>");
    m_out.println( "<BR>" );

    try {
      	userTransaction.begin();

		m_out.println("<b>New transaction</b>" );
		m_out.println("<BR>");
		m_out.println("<BR>");
	
		m_out.println(bean1.getId());
		m_out.println("<BR>");
	
		m_out.println(bean1_1.getId());
		m_out.println("<BR>");
	
		m_out.println(bean2.getId());
		m_out.println("<BR>");
	
		userTransaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }

	m_out.println("<BR>");
    m_out.println("<b>Transaction is over.</b>" );
    m_out.println("<BR>");
	m_out.println("<BR>");
	
    try {
      	userTransaction.begin();
 
		m_out.println("<b>New transaction</b>" );
		m_out.println("<BR>");
		m_out.println("<BR>");
	
		m_out.println(bean1.getId());
		m_out.println("<BR>");
	
		m_out.println(bean1_1.getId());
		m_out.println("<BR>");
	
		m_out.println(bean2.getId());
		m_out.println("<BR>");

     	userTransaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
	
	m_out.println("<BR>");
    m_out.println("<b>Transaction is over.</b>" );
    m_out.println("<BR>");
    m_out.println("<BR>");

	m_out.println("<b>Calling TransactionScoped bean outside transaction.</b>" );
    m_out.println("<BR>");
    m_out.println("<BR>");
    try {
      bean1.getId();
      m_out.println( "ERROR: Should have gotten a ContextNotActiveException.");
    } catch ( ContextNotActiveException cnae ) {
      m_out.println( "Got a ContextNotActiveException as expected.");
    }

    m_out.println("</BODY>");
    m_out.println("</HTML>");
  }
}