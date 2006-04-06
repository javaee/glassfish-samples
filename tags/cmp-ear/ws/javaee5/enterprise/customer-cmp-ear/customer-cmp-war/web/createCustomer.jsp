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
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/> </title></head>

<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/> </h2>

<fmt:message key="create_customer"/> :
<p>
<form method="post" action="/customer/createCustomer.jsp">
<table border=10>
  <tr>
    <td><fmt:message key="customer_id"/> : </td>
    <td><input type="text" name="id" size="11" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="first_name"/> : </td>
    <td><input type="text" name="firstName" size="25" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="last_name"/> : </td>
    <td><input type="text" name="lastName" size="25" value=""></td>
  </tr>
</table>
<p>
<input type="submit" name="submit" value="Submit">
<p>
</form>

<%
String id = request.getParameter("id");
String firstName = request.getParameter("firstName");
String lastName = request.getParameter("lastName");

if (id != null && !"".equals(id)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        Customer customer = new Customer(id, firstName, lastName);
        custSession.persist(customer);
%>
<fmt:message key="new_customer"/> : 
<a href = "/customer/editCustomer.jsp?cid=<%=id%>">
<%=customer.getLastName()%>, 
<%=customer.getFirstName()%>
</a>
<fmt:message key="created"/> . 
</p>
<a href = "/customer/searchCustomer.jsp">SEARCH</a></p>
<!--<fmt:message key="create_customer_failed"/>-->
<%
    } catch(Exception e) {
        e.printStackTrace();
        out.println("Create Customer Failed : " + e.toString()); 
    } 
}
%>

<hr>
[<a href="/customer/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
