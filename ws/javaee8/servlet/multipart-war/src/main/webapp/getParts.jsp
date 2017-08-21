<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>

<%@ page import="javax.servlet.http.Part" %>


<html>
<head>
  <title>File Upload Example</title>
</head>

<body>
  <h1>Files Received at the Server</h1>
  <br/>

<%
  for (Part p: request.getParts()) {

    /* out.write("Part: " + p.toString() + "<br/>\n"); */
      
    out.write("Part name: " + p.getName() + "<br/>\n");
    out.write("Size: " + p.getSize() + "<br/>\n");
    out.write("Content Type: " + p.getContentType() + "<br/>\n");
    out.write("Header Names:");
    for (String name: p.getHeaderNames()) {
        out.write(" " + name);
    }
    out.write("<br/><br/>\n");

/*
 * If the part is a text file, the following code can be added to read
 * the uploaded file, and dumps it on the response.

    java.io.InputStreamReader in =
      new java.io.InputStreamReader(p.getInputStream());

    int c = in.read();
    while (c != -1) {
      if (c == '\n') out.write("<br/>");
      out.write(c);
      c = in.read();
    }
 */

  }
%>

</body>
</html>

