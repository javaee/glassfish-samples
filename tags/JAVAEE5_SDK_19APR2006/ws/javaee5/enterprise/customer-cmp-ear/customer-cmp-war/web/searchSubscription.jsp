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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="javax.ejb.ObjectNotFoundException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Customer" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Subscription" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.CustomerSessionLocal" %>
<%@ page import='java.util.*' %>
 
<html>
<head><title> <fmt:message key="cmp_demo_title"/> </title></head>
<body bgcolor="white">
<center>
<h2> <fmt:message key="cmp_demo_title"/> </h2>

<fmt:message key="search_by_title"/> :
<p>
    <form method="get" action="/customer/searchSubscription.jsp">
    <input type="text" name="searchText" size="25">
    <p>
    <input type="submit" value= <fmt:message key="search"/> >
    </form>

<%
String text = request.getParameter("searchText");

Subscription subscription = null;
if (text != null && !"".equals(text)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        subscription = custSession.searchForSubscription(text);

%>
<fmt:message key="results"/> : <p>
<%
if (subscription != null) {
%>
<%=subscription.getTitle()%> [<%=subscription.getType()%>] 
<p>
<fmt:message key="people_who_have_subscription"/> :
<p>
<%
Collection customers = subscription.getCustomers();
Iterator cust_iter = customers.iterator();

while(cust_iter.hasNext()){
  Customer customer = (Customer)cust_iter.next();
%>
  <a href="/customer/editCustomer.jsp?cid=<%=customer.getCustomerID()%>">
  <%=customer.getLastName()%>, <%=customer.getFirstName()%></a>
  <p>
<%
}
if (customers.size() == 0) {
%>
<fmt:message key="none"/> .
<%}%>
<p>
<%
}
%>
<%
    } catch(Exception e) {
        e.printStackTrace();
        out.println(e.toString());
    }
}
%>

<hr>
[<a href="/customer/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
