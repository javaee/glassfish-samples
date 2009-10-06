
<html>
    <head>
        <title>JSP Page</title>
    </head>
    <body>
       <%if (request.getAttribute("USERNAME") != null) {
            out.println("Welcome"+(String)request.getAttribute("USERNAME"));
        }
        else {%>
           No credentials obtained
       <% }
        %> 
    </body>
</html>
