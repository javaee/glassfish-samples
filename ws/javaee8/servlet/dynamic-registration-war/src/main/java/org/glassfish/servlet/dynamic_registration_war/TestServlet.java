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

package org.glassfish.servlet.dynamic_registration_war;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet that is registered by the
 * <tt>web.servlet.dynamicregistration_war.TestServletContextListener</tt>.
 * <p>This Servlet verifies that the initialization parameter that was
 * added by the <tt>TestServletContextListener</tt> when it registered the
 * Servlet is present in its <tt>ServetConfig</tt>.
 * <p>The Servlet also verifies that the Filter mapped to it has been
 * invoked, by checking the request for the presence of the Filter's
 * initialization parameter that was added by the
 * <tt>TestServletContextListener</tt> when it registered the Filter,
 * and was set on the request (as a request attribute) by the Filter as
 * the request passed through the Filter.
 * 
 * <p>If any of the verification steps fail, the Servlet will throw an
 * Exception. Otherwise, it outputs the string <tt>HELLO WORLD!</tt> to the
 * response.
 * @author Jan Luehe
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        if (!"servletInitValue".equals(getServletConfig().getInitParameter(
                        "servletInitName"))) {
            throw new ServletException("Missing servlet init param");
        }

        if (!"filterInitValue".equals(req.getAttribute("filterInitName"))) {
            throw new ServletException("Missing request attribute that was " +
                "supposed to have been set by programmtically registered " +
                "Filter");
        }

        if (!"listenerAttributeValue".equals(req.getAttribute(
                "listenerAttributeName"))) {
            throw new ServletException("Missing request attribute that was " +
                "supposed to have been set by programmtically registered " +
                "ServletRequestListener");
        }

        res.getWriter().println("HELLO WORLD! GLASSFISH\n");
    }
}
