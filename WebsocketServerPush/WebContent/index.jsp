<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Websocket serverpush</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
var webSocket = new WebSocket("ws://localhost:8080/"+location.pathname.split("/")[1]+"/actions");
webSocket.onmessage = function(event){
	$("#testData").append("<li>"+event.data+"</li>");
}
function test(){
	$.ajax({
		  method: "GET",
		  url: "log4jMessage",
		  dataType: 'text',
		  success: function(response){	
			  console.log(response);
		  }
	});
}
</script>
</head>
<body>
hello
<form action="log4jMessage">
<button type="button"  onclick="test()">Test</button>
<ul id="testData" class="list-unstyled activity-list"></ul>
</form>
</body>
</html>