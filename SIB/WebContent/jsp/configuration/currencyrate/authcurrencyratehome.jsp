<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>

<script>
function validateCurrency(){
	var currencycode = document.getElementById("currencycode");
	if( currencycode.value == "-1"){
		errMessage(currencycode, "Select Currency");
		return false;
	} 
	return true;
}

</script>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="currencyrateform" action="getAuthCurrencyCurrencyRateAction.do" autocomplete="off" onsubmit="return validateCurrency()" >
<%-- <input type="hidden" name="act"  id="act" value="<%=act%>">  --%>
  
 	<table border="0" cellpadding="0" cellspacing="0" width="30%"  >
	 		<tr>
	 		 	<td> Currency</td>
	 			<td><select id="currencycode"  name="currencycode">
	 					<option value="-1">Select Currency</option>
						 <s:iterator value="currencylist">   
						 	<option value="<s:property value="CURRCODE"/>"><s:property value="CURRDESC"/></option>
						</s:iterator>   
					 </select>
				</td>	 
	 		</tr>
	</table>
	<br><br>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
			<td> <s:submit value="Submit" name="submit" id="submit" /> </td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td>
		</tr>
	</table>
</s:form> 
</body>
