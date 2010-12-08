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
<title><fmt:message key="sendmail.jsp.title"/></title>
</head>
<body bgcolor="white">

<p><fmt:message key="sendmail.jsp.overview"/></p>
<ul>
<li><fmt:message key="sendmail.jsp.defaultconfig"/></li>
<li><fmt:message key="sendmail.jsp.applicationlogic"/></li>
<li><fmt:message key="sendmail.jsp.allfields"/></li>
</ul>

<%
    if (session.getAttribute("mailto") == null)
	session.setAttribute("mailto", "joe@localhost");
    if (session.getAttribute("mailfrom") == null)
	session.setAttribute("mailfrom", "tom@localhost");
    if (session.getAttribute("mailsubject") == null)
	session.setAttribute("mailsubject", "What's up");
%>

<form method="POST" action="mail">
<table>

  <tr>
    <th align="center" colspan="2">
      <fmt:message key="sendmail.jsp.entermessage"/>
    </th>
  </tr>

  <tr>
    <th align="right"><fmt:message key="sendmail.jsp.from"/></th>
    <td align="left">
      <input type="text" name="mailfrom" size="60" value="<%= session.getAttribute("mailfrom") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="sendmail.jsp.to"/></th>
    <td align="left">
      <input type="text" name="mailto" size="60" value="<%= session.getAttribute("mailto") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="sendmail.jsp.subject"/></th>
    <td align="left">
      <input type="text" name="mailsubject" size="60" value="<%= session.getAttribute("mailsubject") %>">
    </td>
  </tr>

  <tr>
    <td colspan="2">
      <textarea name="mailcontent" rows="10" cols="80">
      </textarea>
    </td> 
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value="<fmt:message key="sendmail.jsp.sendbutton"/>">
    </td>
    <td align="left">
      <input type="reset" value="<fmt:message key="sendmail.jsp.resetbutton"/>">
    </td>
  </tr>

</table>
</form>

</body>
</html>
