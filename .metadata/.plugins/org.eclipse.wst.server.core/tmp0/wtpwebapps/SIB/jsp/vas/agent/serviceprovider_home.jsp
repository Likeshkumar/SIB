<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<script>
	function validateForm(){
		var serviceprovider = document.getElementById("serviceprovider");
		 
		
		if( serviceprovider ){
			if( serviceprovider.value == ""){ errMessage(serviceprovider, "Enter Service provider name....");return false;}
		} 
		 
		parent.showprocessing();
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="saveServiceProviderVasAgent"  name="orderform" onsubmit="return validateForm()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">	 
	<tr><td>Service Provider </td><td> : <s:textfield name="serviceprovider" id="serviceprovider" /> </td></tr> 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return validFilter()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 