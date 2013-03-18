<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.

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

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CDI Servlet Injection</title>
        <script type="text/javascript" src="resources/ajax.js">
        </script>
        <link type="text/css" rel="stylesheet" href="/cdi-servlet/resources/stylesheet.css" />
    </head>
    <body>
        <form name="myForm" method="POST" action="LoginServlet">
            <table class="title-panel">
                <tbody>
                    <tr>
                        <td><span class="title-panel-text">Login Servlet</span></td>
                    </tr>
                    <tr>
                        <td><span class="title-panel-subtext">Powered By Servlet 3.0 and CDI</span></td>
                    </tr>
                </tbody>
            </table>
            <table height="30" style="font-size: 16px">
              <tr>
                <td>Enter any value for user name and password.</td>
              </tr>
            </table>
            <table style="font-size: 16px">
              <tr>
                <td style="color:red">*</td>
                <td>Denotes required entry.</td>
              </tr>
            </table>
            <table height="30">
            <table border="1" style="font-size: 18px">
              <tr>
                <td>User Name:</td>
                <td><input type="text" name="username" id="username" /></td>
                <td style="color:red">*</td>
              </tr>
              <tr>
                <td>Password:</td>
                <td><input type="password" name="password" id="password" /></td>
                <td style="color:red">*</td>
              </tr>
            </table>
            <table border="1">
              <tr>
                <td colspan="2"><input type="button" value="Submit"  onclick="ajaxFunction();" /></td>
                <td colspan="2"><input type="button" value="Reset"  onclick="resetFunction();" /></td>
              </tr> 
            </table>
            </table>
            <table height="20">
              <tr>
                <td><div id="message" style="color:red;font-size: 14px"></td>
              </tr>
            </table>

         </form>
    </body>
</html>
