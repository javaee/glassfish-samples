<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2006-2010 Oracle and/or its affiliates. All rights reserved.

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

<%@page contentType="text/html"%>

<HTML>
<HEAD><TITLE>Cluster - Ha JSP Sample </TITLE></HEAD>
<BODY BGCOLOR="white">
<H1>Cluster - HA JSP Sample </H1>
<B>HttpSession Information:</B>
<UL>
<LI>Served From Server:   <b><%= request.getServerName() %></b></LI>
<LI>Server Port Number:   <b><%= request.getServerPort() %></b></LI>
<LI>Executed From Server: <b><%= java.net.InetAddress.getLocalHost().getHostName() %></b></LI>
<LI>Executed Server IP Address: <b><%= java.net.InetAddress.getLocalHost().getHostAddress() %></b></LI>
<LI>Session ID:    <b><%= session.getId() %></b></LI>
<LI>Session Created:  <%= new java.util.Date(session.getCreationTime())%></LI>
<LI>Last Accessed:    <%= new java.util.Date(session.getLastAccessedTime())%></LI>
<LI>Session will go inactive in  <b><%= session.getMaxInactiveInterval() %> seconds</b></LI>
</UL>
<BR>
<B> Enter session attribute data: </B><BR>
<FORM ACTION="HaJsp.jsp" METHOD="POST" NAME="Form1">
    Name of Session Attribute: 
    <INPUT TYPE="text" SIZE="20" NAME="dataName">
    <BR>
    Value of Sesion Attribute: 
    <INPUT TYPE="text" SIZE="20" NAME="dataValue">
    <BR>
    <INPUT TYPE="submit" NAME="action" VALUE="ADD SESSION DATA">
    <INPUT TYPE="submit" NAME="action" VALUE="RELOAD PAGE">
</FORM>
<FORM ACTION="ClearSession.jsp" method="POST" name="Form2" >
    <INPUT TYPE="submit" NAME="action" VALUE="CLEAR SESSION">
</FORM>

<%
    String dataname = request.getParameter("dataName");
    String datavalue = request.getParameter("dataValue");
    if (dataname != null && datavalue != null && !dataname.equals("")) {
        System.out.println("Add to session: " + dataname + " = " + datavalue);
        session.setAttribute(dataname,datavalue);
    }
%>
<HR><BR>
<B>Data retrieved from the HttpSession: </B>
<% 
    java.util.Enumeration valueNames = session.getAttributeNames();
    if (!valueNames.hasMoreElements()) {
        System.out.println("No parameter entered for this request");
    } else {
        out.println("<UL>");
        while (valueNames.hasMoreElements()) {
            String param = (String) valueNames.nextElement();
            String value = session.getAttribute(param).toString();
            out.println("<LI>" + param + " = " + value + "</LI>");
        }
        out.println("</UL>");
    }
%>
<BR><BR>
<HR>
<B>INSTRUCTIONS</B>
<UL>
<LI>Add session data using the form. Upon pressing ADD SESSION DATA, the current session data will be listed.</LI>
<LI>Click on RELOAD PAGE to display the current session data without adding new data.</LI>
<LI>Click on CLEAR SESSION to invalidate the current session.</LI>
</UL>

</BODY>
</HTML>
