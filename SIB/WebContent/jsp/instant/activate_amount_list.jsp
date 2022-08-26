<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript" src="js/script.js"></script>
<script>
	function getLoadAmt(){
		
		if( document.getElementById("cardno").value != "" ){
			var cardno = document.getElementById("cardno").value;
			 var url= "dispLoadAmtInstCardActivateProcess.do?cardno="+cardno;
			 var result = AjaxReturnValue(url);  
			 document.getElementById("summerytabl").innerHTML=result;
		}else{
			alert( "Invalid card no ");
		}
		
	}
</script>
</head>
<body onload="getLoadAmt()">

	<s:hidden name="cardno" id="cardno" value="%{cardno}" />
	<div id="summerytabl"></div>
</body>
</html>