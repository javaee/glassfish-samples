var wsUri = getRootUri() + "/websocket-auction/auction";
var output;
var debug = false;
var websocket;
var separator = ":";
var id = 0;

function getRootUri() {
    return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port == "" ? "8080" : document.location.port);
}

function init() {
    output = document.getElementById("output");
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        login();
    };
    websocket.onmessage = function (evt) {
        handleResponse(evt)
    };
    websocket.onerror = function (evt) {
        onError(evt)
    };
}

function login() {
}

function doLogin() {
    var myStr = "lreq" + separator + id + separator + document.getElementById("loginID").value;
    websocket.send(myStr);
    window.setTimeout('to_select()', 10);
}

function to_select() {
    window.location = "select.html?name=" + document.getElementById("loginID").value;
}


function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    if (debug) {
        var pre = document.createElement("p");
        pre.style.wordWrap = "break-word";
        pre.innerHTML = message;
        output.appendChild(pre);
    }
}

window.addEventListener("load", init, false);
