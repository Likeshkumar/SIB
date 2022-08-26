<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script>
	function logginOut(){
		 
		 window.parent.location.reload();
		 window.location = "loginLink.do?msg=Session Expired";
	}

</script>
</head>
<body onload="logginOut()">
 <% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>
<table border="0" cellpadding="0" cellspacing="0"  width="50%" class="formtable">
 <tr><td> Session expired... <a href="loginLink.do?msg=Session Expired" style='color:#000'>LOGIN AGAIN</a> </td></tr> 
 </table>

</body>
</html>