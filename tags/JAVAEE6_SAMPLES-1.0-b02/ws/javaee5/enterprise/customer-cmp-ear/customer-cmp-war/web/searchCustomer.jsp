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
<fmt:setBundle basename="LocalStrings"/>

<%@ page language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="javax.ejb.ObjectNotFoundException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Customer" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/></title></head>
<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/> </h2>

<fmt:message key="search_for_customer"/> :
<p>
    <form method="get" action="/customer-cmp-war/searchCustomer.jsp">
    Search by 
    <select name="searchCriteria">
      <option value="customerID" selected><fmt:message key="customer_id"/> 
      <option value="lastName"><fmt:message key="last_name"/> 
      <option value="firstName"><fmt:message key="first_name"/> 
    </select>
    <input type="text" name="searchText" size="25">
    <p>
    <input type="submit" value=<fmt:message key="search"/> >
    </form>

<%
String text = request.getParameter("searchText");
String criteria = request.getParameter("searchCriteria");

if (text != null && !"".equals(text)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        List customers=new ArrayList(); //creating empty list handles any errors gracefully.
        if ("customerID".equals(criteria)) {
            Customer customer = custSession.searchForCustomer(text);
            if(customer!=null){
                customers.add(customer);
            }
        }	  
        else if ("lastName".equals(criteria)) {
          customers = custSession.findCustomerByLastName(text);
        } else if ("firstName".equals(criteria)) {
          customers = custSession.findCustomerByFirstName(text);
        } else {
        }
  

%>
<fmt:message key="results"/> : <p>
<%
for (int i = 0; i < customers.size(); i++) {
  Customer c = (Customer)(customers).get(i);
  String cid = (String)c.getCustomerID();
%>
<a href="/customer-cmp-war/editCustomer.jsp?cid=<%=cid%>"> 
<%=c.getLastName()%>, <%=c.getFirstName()%></a> has
<%=c.getAddresses().size()%> addresses,
<%=c.getSubscriptions().size()%> subscriptions.
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
[<a href="/customer-cmp-war/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
