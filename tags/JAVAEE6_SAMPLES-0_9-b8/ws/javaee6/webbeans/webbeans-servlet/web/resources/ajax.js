        function getXMLObject()  {
            var xmlHttp = false;
            try {
                xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
            } catch (e) {
                try {
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
                } catch (e2) {
                    xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
                }
            }
            if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
                xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
            }
            return xmlHttp;  // Mandatory Statement returning the ajax object created
        }

        var xmlhttp = new getXMLObject();

        function ajaxFunction() {
            if(xmlhttp) {
                var username = document.getElementById("username");
                var password = document.getElementById("password");
                xmlhttp.open("POST","LoginServlet",true); //getname will be the servlet name
                xmlhttp.onreadystatechange  = handleServerResponse;
                xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xmlhttp.send("username=" + username.value + "&password=" + password.value);
            }
        }

        function handleServerResponse() {
            if (xmlhttp.readyState == 4) {
                if(xmlhttp.status == 200) {
                    document.getElementById("message").innerHTML=xmlhttp.responseText; 
                } else {
                    alert("Error during AJAX call. Please try again");
                }
            }
        }

        function resetFunction() {
            document.getElementById("username").value="";
            document.getElementById("password").value="";
            document.getElementById("message").innerHTML="";
        }

