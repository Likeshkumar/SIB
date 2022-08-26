<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>

<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="currencyrateform" action="authorizeCurrencyCurrencyRateAction.do" autocomplete="off" onsubmit="return validateCurrency()" >
  
 	<table border="0" cellpadding="0" cellspacing="0" width="30%" class="table">
	 		<tr>
	 		 	<td> Currency</td>
	 			<td> <s:textfield  value="%{currencydesc}" readonly="true"/> </td>	 
	 		</tr>
	 		<tr>
	 		 	<td> Buying Rate</td>
	 			<td> <s:textfield value="%{buyrate}" readonly="true"/> </td>	 
	 		</tr>
	 		<tr>
	 		 	<td> Selling Rate</td>
	 			<td> <s:textfield value="%{sellrate}" readonly="true"/> </td>	 
	 		</tr>
	 		<s:hidden name="currencycode"/>
	</table>
	<br><br>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
			<td> <s:submit value="Authorize" name="submit" id="submit" /> </td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td>
		</tr>
	</table>
</s:form> 
</body>
