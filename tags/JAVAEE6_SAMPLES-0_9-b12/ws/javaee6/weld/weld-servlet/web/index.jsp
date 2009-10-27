<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Weld Servlet Injection</title>
        <script type="text/javascript" src="resources/ajax.js">
        </script>
        <link type="text/css" rel="stylesheet" href="/weld-servlet/resources/stylesheet.css" />
    </head>
    <body>
        <form name="myForm" method="POST" action="LoginServlet">
            <table class="title-panel">
                <tbody>
                    <tr>
                        <td><span class="title-panel-text">Login Servlet</span></td>
                    </tr>
                    <tr>
                        <td><span class="title-panel-subtext">Powered By Servlet 3.0 and Weld</span></td>
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
