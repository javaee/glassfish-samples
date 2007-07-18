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
<%@ page import="java.util.Collection" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Address" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Customer" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Subscription" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import="enterprise.customer_cmp_ejb.common.*" %>

<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/> </title></head>
<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/> </h2>

<%
String cid = request.getParameter("cid");
Customer customer = null;
CustomerSessionLocal custSession=null;

try {
    InitialContext ic = new InitialContext();
    Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
    custSession = (CustomerSessionLocal) o;
    customer = custSession.searchForCustomer(cid);
} catch(Exception e) {
    e.printStackTrace();
    out.println(e.toString());
}

String removeCustomer = request.getParameter("removeCustomer");
if ("Remove".equals(removeCustomer)) {
    custSession.remove(customer);
    
}
else {

String sub_title = request.getParameter("subscription");
String add = request.getParameter("addSubscription");
String remove = request.getParameter("removeSubscription");

//out.println(sub_title);

if (sub_title != null && !"".equals(sub_title)) {
    try {
        
        //Subscription subs=custSession.searchForSubscription(sub_title);
        if ("Add".equals(add)) {
          customer = custSession.addCustomerSubscription(cid, sub_title);
        } 
        else if ("Remove".equals(remove)) {
          customer = custSession.removeCustomerSubscription(cid, sub_title);
        }
        else {
            
        }
    }catch(DuplicateSubscriptionException dup_ex){
%>
        <fmt:message key="duplicate_subs_ex"/>
<%        
        
    }catch(SubscriptionNotFoundException subs_ex){
        //ignore
    }catch(Exception e) {
        e.printStackTrace();
        out.println(e.toString());
    }
}

String id = request.getParameter("id");
String street = request.getParameter("street");
String city = request.getParameter("city");
String zip = request.getParameter("zip");
String state = request.getParameter("state");

if (id != null && !"".equals(id)) {
    try {

        Address address = new Address(id,street,city,zip,state);
        customer = custSession.addCustomerAddress(customer, address);
    } catch(Exception e) {
        e.printStackTrace();
        out.println("Create new address FAILED : " + e.toString());
    }
}

List allSubscriptions = null;
try {

    allSubscriptions = custSession.findAllSubscriptions();
    
} catch(Exception e) {
    e.printStackTrace();
    out.println(e.toString());
}
%>

<table border=10>
  <tr>
    <td><fmt:message key="customer_id"/> : </td>
    <td><%=customer.getCustomerID()%></td>
  </tr>
  <tr>
    <td><fmt:message key="first_name"/> : </td>
    <td><%=customer.getFirstName()%></td>
  </tr>
  <tr>
    <td><fmt:message key="last_name"/> : </td>
    <td><%=customer.getLastName()%></td>
  </tr>

<%
Collection addresses = customer.getAddresses();
Iterator add_iter = addresses.iterator();

while(add_iter.hasNext()){
  Address address = (Address)add_iter.next();
%>
<tr>
    <td>Address [<%=address.getAddressID()%>]</td>
    <td><%=address.getStreet()%></td>
</tr>
<%
}
%>
<%
Collection subscriptions = customer.getSubscriptions();
Iterator subs_iter = subscriptions.iterator();

while(subs_iter.hasNext()) {
  Subscription subscription = (Subscription)subs_iter.next();
%>
<tr>
    <td>Subscription [<%=subscription.getType()%>]</td>
    <td><%=subscription.getTitle()%></td>
</tr>
<%
}
%>
</table>

<p>

<form method="post" ation="/customer-cmp-war/editCustomer.jsp?cid=<%=cid%>">
<input type="submit" name="removeCustomer" value=<fmt:message key="remove"/> >
</form>

<p>

<fmt:message key="add_new_subscription"/> :
<form method="post" action="/customer-cmp-war/editCustomer.jsp?cid=<%=cid%>">
<table border="2">
<%
if (allSubscriptions.size() == 0) {
%>
<tr>
    <td><fmt:message key="subscription"/> </td>
    <td><fmt:message key="none_avaialable"/> . [<a href="/customer-cmp-war/createSubscription.jsp"><fmt:message key="create_here"/> .</a>]</td>
</tr>
<%
} else {
%>
<tr>
<td>
<select name="subscription">
<%
  for (int i = 0; i < allSubscriptions.size(); i++) {
    Subscription subscription = (Subscription)allSubscriptions.get(i);
%>
    <option value="<%=subscription.getTitle()%>">
    <%=subscription.getTitle()%>[<%=subscription.getType()%>]</option>
<%
  }
%>
</select>
</td>
<td><input type="submit" name="addSubscription" value=<fmt:message key="add"/> ></td>
<td><input type="submit" name="removeSubscription" value=<fmt:message key="remove"/> ></td>
</tr>
<p>

<%
}
%>
</table>
</form>

<fmt:message key="add_an_address"/> :
<p>
<form method="post" action="/customer-cmp-war/editCustomer.jsp?cid=<%=cid%>">
<table border=10>
  <tr>
    <td><fmt:message key="address_id"/> :</td>
    <td><input type="text" name="id" size="11" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="street"/> : </td>
    <td><input type="text" name="street" size="40" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="city"/> : </td>
    <td><input type="text" name="city" size="25" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="state"/> : </td>
    <td><input type="text" name="state" size="25" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="zip"/> : </td>
    <td><input type="text" name="zip" size="25" value=""></td>
  </tr>
</table>
<p>
<input type="submit" name="submit" value=<fmt:message key="add_address"/> >
<p>
</form>

<% } %>
<hr>
[<a href="/customer-cmp-war/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
