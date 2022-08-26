<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script>
 
</script>
<style type="text/css">
table{
	margin:0 auto;
	border-collapse:collapse;
}

table.feestruct td{
	padding-top:15px;
	border:1px solid #000;
	 border-collapse:collapse;
}
*{
	font-family:verdana;
	font-size:12px;
}
 

</style>
</head>
<body style='background:#efefef'>
	<s:if test="viewstatus=='Y'">
	<table border='0' cellpadding='0' cellspacing='0' width='90%' class='feestruct'>
	  <tr> 
	  	<td colspan='2' style='text-align:center'> <b> Fee Code : ${schemecode} </b></td>
	  </tr>
	 <s:iterator value="schemeFeeList">	
	 	 <tr> 
	 	 	<td>${FEE_ACTION}</td>
			<td>${FEE_AMOUNT}</td>
		</tr>
	 </s:iterator> 
		 <tr> 	
		 	<td colspan='2' style='text-align:center'> 
		 		<input type='submit' name='close' value='Close' onclick='javascript:window.close()'/>
		 	</td>
		 </tr>
	</table>
	</s:if>
	<s:if test="viewstatus=='N'">
	response.sendRedirect("viewSchemeSchemeConfigAction.do");
	<table border='0' cellpadding='0' cellspacing='0' width='90%' class='feestruct'>
		<tr align="center">
			<td>
				<b style="color: red">No Fee Details Found </b>
			</td>
		</tr>
		 <tr> 	
		 	<td colspan='2' style='text-align:center'> 
		 		<input type='submit' name='close' value='Close' onclick='javascript:window.close()'/>
		 	</td>
		 </tr>		
	</table>
	</s:if>
	<s:if test="viewstatus=='E'">
	<table border='0' cellpadding='0' cellspacing='0' width='90%' class='feestruct'>
		<tr align="center">
			<td>
				<b style="color: red"> Error While Get The Detail </b>
			</td>
		</tr>
		 <tr> 	
		 	<td colspan='2' style='text-align:center'> 
		 		<input type='submit' name='close' value='Close' onclick='javascript:window.close()'/>
		 	</td>
		 </tr>		
	</table>
	</s:if>	
</body>
</html>