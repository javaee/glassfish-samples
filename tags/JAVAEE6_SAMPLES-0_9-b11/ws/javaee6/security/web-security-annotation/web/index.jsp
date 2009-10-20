<html>
    <head>
        <title>Test Web Security Annotations on a Servlet</title>
    </head>
    <body>
        <br>
        <form name="formGet" action="/web-security-annotation/annotate" method="GET">
            The Get Method is permitted only for javaee6user (@RolesAllowed)<br>
            <input type="submit" value="Try Get" />
        </form>
        <br><br>
        <form name="formPost" action="/web-security-annotation/annotate" method="POST">
            The Post method is denied access to all users (@DenyAll)<br>
            <input type="submit" value="Try Post" />
        </form>
        
    </body>
</html>
    
