<html>

    <body>
        Test Http Method Omissions in Servlet
        <br><br>
        <form name="formPost" action="/http-method-omission/omissionservlet" method="POST">
            The Post Method is permitted only for javaee6user<br>
            <input type="submit" value="Try Post" />
        </form>
        <br><br>
        <form name="formGet" action="/http-method-omission/omissionservlet" method="GET">
            The Get method is denied access to all users <br>
            <input type="submit" value="Try Get" />
        </form>
        
    </body>
</html>
    