<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/validationall.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
</head>
<body>

<div align="center">
<s:form action="backtononpersonalorderformCardorderAction" namespace="/" autocomplete="off">
<br><br><br><br>
<table border="0" cellpadding="0" cellspacing="0" width="70%" >
<s:bean name="com.ifp.beans.cardorderbean" var="resd" ></s:bean>
<tr>
	<td>
	<center><b>The Order is Successfully placed with the Order Reference Number <font color="red" face="verdana" size="3"><s:property value="#resd.orderrefnum"/></font></b></center>
	</td>
</tr>

</table>
<br><br><br><br><br>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
<tr align="center">
		<td>
			<s:submit name="Back" value="Back"/>
		</td>
		<td>
			<input type="button" name="cancle" value="Cancel" onclick="return forwardtoCardOrderPage();">
		</td>
</tr>

</table>
</s:form>
</div>


</body>
</html>