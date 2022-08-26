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
		var agentcode = document.getElementById("agentcode");
		var agentname = document.getElementById("agentname");
		var interfacecode = document.getElementById("interfacecode");
		var bankcode = document.getElementById("bankcode");
		var ifsccode = document.getElementById("ifsccode");
		var agnetacctno = document.getElementById("agnetacctno");
		
		if( serviceprovider ){
			if( serviceprovider.value == "-1"){ errMessage(serviceprovider, "Select Service provider...");return false;}
		} 
		if( agentcode ){
			if( agentcode.value == ""){ errMessage(agentcode, "Enter Agent Code...");return false;}
		} 
		if( agentname ){
			if( agentname.value == ""){ errMessage(agentname, "Enter Agent Name...");return false;}
		} 
		if( interfacecode ){
			if( interfacecode.value == "-1"){ errMessage(interfacecode, "Select Interface...");return false;}
		} 
		if( bankcode ){
			if( bankcode.value == "-1"){ errMessage(bankcode, "Select Bank Name...");return false;}
		} 
		if( ifsccode ){
			if( ifsccode.value == ""){ errMessage(ifsccode, "Enter IFSC Code...");return false;}
		} 
		if( agnetacctno ){
			if( agnetacctno.value == ""){ errMessage(agnetacctno, "Enter Account number...");return false;}
		}
		parent.showprocessing();
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="saveAgentInfoVasAgent"  name="orderform" onsubmit="return validateForm()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">	 
	<tr><td>Service Provider</td><td> : <s:select list="agentbean.serviceprovider" name="serviceprovider" id="serviceprovider" listKey="PROVIDER_ID"  listValue="PROVIDER_NAME" headerKey="-1" headerValue="-SELECT-"/> </td></tr>
	<tr><td>Agent Code </td><td> : <s:textfield name="agentcode" id="agentcode" /> </td></tr>
	<tr><td>Agent Name </td><td> : <s:textfield name="agentname" id="agentname" /> </td></tr>
	<tr><td>Interface </td><td> : <select name="interfacecode" id="interfacecode">
										<option value="-1"> -SELECT - </option>
										<option value="SERVICE"> Web-Service </option>
										<option value="ISO"> ISO </option>
										<option value="FILE"> File </option>
								  </select> </td></tr>
	<tr><td>Agent-Account Holding Bank </td><td> : <select name="bankcode" id="bankcode">
										<option value="-1"> -SELECT- </option>
										<option value="0"> NOT NEEDED </option>
										<option value="CBS"> CBS </option>
										<option value="DCB"> DCB </option> 
								  </select> </td></tr>
	<tr><td>IFSC Code </td><td> : <s:textfield name="ifsccode" id="ifsccode"  value="0"/> </td></tr>							  
	<tr><td>Agent Account Number </td><td> : <s:textfield name="agnetacctno" id="agnetacctno" value="0"/> <br/>
		<small class="dt"> *Agent have account number enter account number else enter zero</small> </td></tr>
    	
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
 
 