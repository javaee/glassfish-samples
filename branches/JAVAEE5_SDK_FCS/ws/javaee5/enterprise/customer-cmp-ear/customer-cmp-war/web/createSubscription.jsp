<%--
 Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 Use is subject to license terms.
--%>

<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
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
<form method="post" action="/customer/createSubscription.jsp">
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
[<a href="/customer/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
