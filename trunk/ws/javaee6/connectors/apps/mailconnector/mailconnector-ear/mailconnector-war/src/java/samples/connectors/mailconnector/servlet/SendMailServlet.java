/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

package samples.connectors.mailconnector.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Example servlet that sends a mail message via a JNDI resource.
 *
 * @author Craig McClanahan
 */

public class SendMailServlet extends HttpServlet {

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {

        // Acquire request parameters we need
        String from = request.getParameter("mailfrom");
        String to = request.getParameter("mailto");
        String subject = request.getParameter("mailsubject");
        String content = request.getParameter("mailcontent");
        if ((from == null) || (to == null) ||
            (subject == null) || (content == null)) {
            RequestDispatcher rd =
                getServletContext().getRequestDispatcher("/sendmail.jsp");
            rd.forward(request, response);
            return;
        }

	// Save these values in the session object (would be used as default 
	//  values by JSP page)
	HttpSession httpSession = request.getSession();
	httpSession.setAttribute("mailfrom", from);
	httpSession.setAttribute("mailto", to);
	httpSession.setAttribute("mailsubject", subject);

        // Prepare the beginning of our response
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Sun Java System Application Server " +
                       "Connector 1.5 Examples: Sending Results</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=\"white\">");

        try {

            // Acquire our JavaMail session object
	    Context initial = new InitialContext();
            Session session = 
              (Session) initial.lookup("java:comp/env/TheMailSession");

            // Prepare our mail message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress dests[] = new InternetAddress[]
                { new InternetAddress(to) };
            message.setRecipients(Message.RecipientType.TO, dests);
            message.setSubject(subject);
            message.setContent(content, "text/plain");

            // Send our mail message
            Transport.send(message);

            // Report success
            writer.println("<strong>Message successfully sent!</strong>");

        } catch (Throwable t) {

            writer.println("<font color=\"red\">");
            writer.println("ENCOUNTERED EXCEPTION:  " + t);
            writer.println("<pre>");
            t.printStackTrace(writer);
            writer.println("</pre>");
            writer.println("</font>");

        }

        // Prepare the ending of our response
        writer.println("<br><br>");
        writer.println("<a href=\"sendmail.jsp\">Create a new message</a><br>");
        writer.println("<a href=\"index.html\">Return to main page</a><br>");
        writer.println("</body>");
        writer.println("</html>");        

    }

}
