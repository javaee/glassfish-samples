<%--
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
  
   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License. You can obtain
   a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
   or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
   language governing permissions and limitations under the License.
  
   When distributing the software, include this License Header Notice in each
   file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
   Sun designates this particular file as subject to the "Classpath" exception
   as provided by Sun in the GPL Version 2 section of the License file that
   accompanied this code.  If applicable, add the following below the License
   Header, with the fields enclosed by brackets [] replaced by your own
   identifying information: "Portions Copyrighted [year]
   [name of copyright owner]"
  
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
<fmt:setBundle basename="LocalStrings"/>
<%@ page language="java" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Customer" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Subscription" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.SubscriptionType" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/> </title></head>
<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/> </h2>

<fmt:message key="create_subscription"/> :
<p>
<form method="post" action="/customer-cmp-war/createSubscription.jsp">
<table border=10>
  <tr>
    <td><fmt:message key="title"/>: </td>
    <td><input type="text" name="title" size="20" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="type"/> : </td>
    <td>
      <select name="type">
        <option><%=SubscriptionType.JOURNAL%></option>
        <option selected><%=SubscriptionType.MAGAZINE%></option>
        <option><%=SubscriptionType.NEWS_PAPER%></option>
        <option><%=SubscriptionType.OTHER%></option>
      </select>
    </td>
  </tr>
</table>
<p>
<input type="submit" name="submit" value="Submit">
<p>
</form>

<%
String title = request.getParameter("title");
String type = request.getParameter("type");

if (title != null && !"".equals(title)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        Subscription subscription = new Subscription(title,type);
        custSession.persist(subscription);
%>
<fmt:message key="new_subscription"/> : 
<font color="blue">
<%=subscription.getTitle()%>, 
<%=subscription.getType()%>
</font>
<fmt:message key="created"/> .
</p>
<!--<fmt:message key="create_subscription_failed"/> -->
<%
    } catch(Exception e) {
        e.printStackTrace();  
        out.println("Create Subscription Failed : " + e.toString()); 
    }   
}
%>

<hr>
[<a href="/customer-cmp-war/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
