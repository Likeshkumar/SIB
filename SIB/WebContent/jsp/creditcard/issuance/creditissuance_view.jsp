<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
table.viewtable td{
	padding-top:5px;
}
</style>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script> 
 

<script>
	


function deAuthReason( deauthbtn, btnval ){ 
	 
 
	
	var reason = prompt("Enter the Reason for De-Authozed ");
	disable( deauthbtn, btnval );
	
	return false;
	
	
	if( reason == null ){
		return false;
	}	
	document.getElementById("reason").value=reason;
	return true;
}

function confirmAuth(){
	if( !confirm("Do you want to submit ?") ){
		return false;
	}
	return true;
}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include> 
<%
String instid = (String)session.getAttribute("Instname");
 
%>
<s:form action="%{crissuebean.formaction}"  onsubmit="return validateForm()"  autocomplete = "off">
 
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="formtable viewtable">	 
<s:hidden name="doact" id="doact" value="%{creditbean.doact}" />
<s:hidden name="reason" id="reason"   />

 
 
<s:iterator value="crlimitbean.customerinfolist">
 
<tr> 
	<td class="txt">  First Name </td> <td> : ${FIRSTNAME}  </td>
	<td class="txt">  Middle Name </td> <td> : ${MIDDLENAME}  </td>
	<td class="txt">  Last Name </td> <td> : ${LASTNAME}  </td>
</tr>

 
<tr> 
	<td class="txt">   	Name On Card </td> <td> : ${ENCNAME}  </td>
	<td class="txt">  Nationality </td> <td> : ${NATIONALITY}  </td>
	<td class="txt"> Date of Birth </td> <td> : ${DOB}  </td>
</tr>

</s:iterator>
 
 <tr>
	 <s:hidden name="cardno" id="cardno" value="%{crissuebean.cardno}"/>  
	<td class="txt">   Product </td> <td> : <s:property value="crlimitbean.productdesc"/>  </td>
	<td class="txt">   Sub-Product </td> <td> : <s:property value="crlimitbean.subproductdesc"/>     </td>
	
</tr>
 
</table> 
  
  <table border="0" cellpadding="0" cellspacing="4" width="20%"  ">	 
		<tr>
		  
		 
		
		<td>
			<s:submit value="Submit" name="submit" id="submit" onclick="return confirmAuth()"/>
		</td>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
 </s:form>