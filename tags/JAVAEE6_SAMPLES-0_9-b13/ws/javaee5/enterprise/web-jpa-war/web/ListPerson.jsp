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
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Of Persons</title>
    </head>
    <body>

    <h1>List of Persons currently in Database</h1>
    
<table id="personListTable" border="3">
<tr >
    <th bgcolor=>ID</th>
    <th bgcolor=>FirstName</th>
    <th bgcolor=>LastName</th>
</tr>
<c:forEach var="person" begin="0" items="${requestScope.personList}">
<tr>
    <td>${person.id}&nbsp;&nbsp;</td> 
    <td>${person.firstName}&nbsp;&nbsp;</td> 
    <td>${person.lastName}&nbsp;&nbsp;</td> 
</tr> 

</c:forEach>

</table>
<a href="CreatePerson.jsp"><strong>Create a Person Record</strong></a>
</body>
</html>
