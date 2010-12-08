<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<fmt:setBundle basename="LogStrings"/>

<html>
<head>
<title><fmt:message key="mailbrowser.jsp.title"/></title>
</head>
<body bgcolor="white">

<p><fmt:message key="mailbrowser.jsp.overview"/></p>
<p><fmt:message key="mailbrowser.jsp.note"/>
<ul>
<li><fmt:message key="mailbrowser.jsp.defaultconfig"/></li>
<li><fmt:message key="mailbrowser.jsp.allfields"/></li>
</ul>
</p>

<%
    if (session.getAttribute("folder") == null)
	session.setAttribute("folder", "Inbox");
    if (session.getAttribute("server") == null)
	session.setAttribute("server", "ServerName");
    if (session.getAttribute("username") == null)
	session.setAttribute("username", "UserName");
    if (session.getAttribute("password") == null)
	session.setAttribute("password", "");
%>

<form method="POST" action="browse">
<table>

  <tr>
    <th align="center" colspan="2">
      <fmt:message key="mailbrowser.jsp.entermessage"/>
    </th>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.folder"/></th>
    <td align="left">
      <input type="text" name="folder" size="60" value="<%= session.getAttribute("folder") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.server"/></th>
    <td align="left">
      <input type="text" name="server" size="60" value="<%= session.getAttribute("server") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.username"/></th>
    <td align="left">
      <input type="text" name="username" size="60" value="<%= session.getAttribute("username") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.password"/></th>
    <td align="left">
      <input type="password" name="password" size="60" value="<%= session.getAttribute("password") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.protocol"/></th>
    <td align="left">
      <input type="text" name="protocol" size="60" value="IMAP">
    </td>
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value="<fmt:message key="mailbrowser.jsp.sendbutton"/>">
    </td>
    <td align="left">
      <input type="reset" value="<fmt:message key="mailbrowser.jsp.resetbutton"/>">
    </td>
  </tr>

</table>
</form>

</body>
</html>
