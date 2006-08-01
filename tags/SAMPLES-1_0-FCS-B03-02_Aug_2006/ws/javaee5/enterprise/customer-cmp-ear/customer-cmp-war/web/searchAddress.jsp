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
<%@ page import="javax.ejb.ObjectNotFoundException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Address" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/></title></head>
<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/></h2>

<fmt:message key="search_address_by_id"/>:
<p>
    <form method="get" action="/customer/searchAddress.jsp">
    <input type="text" name="searchText" size="25">
    <p>
    <input type="submit" value=<fmt:message key="search"/> >
    </form>

<%
String text = request.getParameter("searchText");

Address address = null;
if (text != null && !"".equals(text)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        address = custSession.searchForAddress(text);

%>
<fmt:message key="results"/> : <p>
<%
if (address != null) {
%>
<fmt:message key="address"/>  [<%=address.getAddressID()%>] : 
<%=address.getStreet()%>, <%=address.getCity()%> 
<p>
<%
} else {
%>
<fmt:message key="address"/> [<%=text%>] <fmt:message key="not_found"/>.
<%
}
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
