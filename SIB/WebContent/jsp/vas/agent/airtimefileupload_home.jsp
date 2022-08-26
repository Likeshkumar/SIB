<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<script>
	function validateForm(){
		var agentcode = document.getElementById("agentcode");
		var denomvalue = document.getElementById("denomvalue"); 
		if( agentcode ){
			if( agentcode.value == "-1"){ errMessage(agentcode, "Select Agent Code...");return false;}
		} 
		if( denomvalue ){
			if( denomvalue.value == ""){ errMessage(denomvalue, "Enter Denomination Value...");return false;}
		} 
		 
		parent.showprocessing();
	}
	
	function getDenomValues( agentid ){
		
		var denomdiv = document.getElementById("denomdiv"); 
		
		denomdiv.innerHTML="Processing...";
		var url = "getAllDenomValuesVasAgent.do?agentid="+agentid; 
		var result = AjaxReturnValue(url);
		 
		var jsonobj = JSON.parse(result);
		if( jsonobj["RESPCODE"] == 0 ){
			denomdiv.style.display="block";
			denomdiv.innerHTML=jsonobj["RESPREASON"]; 
			return false;
	   }
		
		
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="uploadAirTimeVasAgent"  name="orderform"  enctype="multipart/form-data"  onsubmit="return validateForm()"  autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">	 
	 
	<tr><td>Agent Name </td><td> : 
		<s:select name="agentcode" id="agentcode"  list="agentbean.agentlist" listKey="AGENT_ID" listValue="AGENT_NAME" headerKey="-1" headerValue="-SELECT-" onchange="getDenomValues(this.value)"/>
		
	</td>  
	</tr> 
	<tr>
		<td> Upload File </td>
		<td> : <s:file name="airtimeupload" id="airtimeupload"  /> </td>
	</tr>
	 
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
 
 