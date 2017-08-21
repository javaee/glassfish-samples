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

/**
 * Filter that is registered by the
 * <tt>web.servlet.dynamicregistration_war.TestServletContextListener</tt>.
 * <p>This Filter retrieves (from its <tt>FilterConfig</tt>) the
 * initialization parameter that was added by the
 * <tt>web.servlet.dynamicregistration_war.TestServletContextListener</tt>
 * when it registered the Filter, and stores this initialization parameter
 * in the request (as a request attribute), so it can be read by the Servlet
 * to which this Filter was mapped.
 * @author Jan Luehe
 */
public class TestFilter implements Filter {

    private String filterInitParam;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterInitParam = filterConfig.getInitParameter("filterInitName");
    }   

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        req.setAttribute("filterInitName", filterInitParam);
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
