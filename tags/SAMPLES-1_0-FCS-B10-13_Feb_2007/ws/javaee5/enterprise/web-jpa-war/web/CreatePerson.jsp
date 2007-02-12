<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create a Person Record</title>
    </head>
    <body>

    <h1>Create a Person record</h1>
    <form id="createPersonForm" action="CreatePerson" method="post">
    <table>
        <tr><td>ID:</td><td><input type="text" id = "id" name="id" /></td></tr>
        <tr><td>FirstName</td><td><input type="text" id = "firstName" name="firstName" /></td></tr>
        <tr><td>LastName</td><td><input type="text" id = "lastName" name="lastName" /></td></tr>
    </table>
    <input type="submit" id="CreateRecord" value="CreateRecord" />
    </form>
<a href="ListPerson"><strong>Go to List of persons</strong></a>
</body>
</html>
