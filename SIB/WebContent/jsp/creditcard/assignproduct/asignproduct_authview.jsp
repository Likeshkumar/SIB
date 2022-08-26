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
<form action="authProductActionCreditProductLimit.do" onsubmit="return validateForm()"  autocomplete = "off">
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
 
<s:iterator value="crlimitbean.applist" var="app">
 <tr> 
	<td class="txt">   	Added By  </td> <td> : ${ADDED_BY}  </td>
	<td class="txt">   	Added Date  </td> <td> : ${ADDED_DATE}  </td>
	<td class="txt">   	Status  </td> <td> : ${AUTH_STATUS}  </td>	 
</tr>
<tr> 
	<td class="txt">   	Auth By  </td> <td> : ${AUTH_BY}  </td>
	<td class="txt">   	Auth Date </td> <td> : ${AUTH_DATE}  </td>
	<td class="txt">   	Remarks </td> <td> : ${REMARKS}  </td>	 	 
</tr> 
<tr><th colspan="6"> Product &amp; Limit Details</th></tr>
<tr>
	 <s:hidden name="customerid" id="customerid" value="%{crlimitbean.customerid}"/>  
	<td class="txt">   Product </td> <td> : <s:property value="crlimitbean.productdesc"/>  </td>
	<td class="txt">   Sub-Product </td> <td> : <s:property value="crlimitbean.subproductdesc"/>     </td>
	
</tr>

<tr> 
	<td class="txt">   Credit Limit  </td> <td> : <s:property value="crlimitbean.creditlimit"/> </td>
	<td class="txt">   Cash Limit  </td> <td> :  <s:property value="crlimitbean.cashlimit"/></td>	
	 
	  
</tr>

<tr> 
	<td class="txt">   E-Com Limit  </td> <td> : <s:property value="crlimitbean.ecomlimit"/> </td>
	<td class="txt">   Transfer Limit  </td> <td> :  <s:property value="crlimitbean.ecomlimit"/> </td>	 
	 
	
</tr>
</s:iterator>



 
</table> 
  
  <table border="0" cellpadding="0" cellspacing="4" width="20%"  ">	 
		<tr>
		  <s:if test="%{crlimitbean.doact == '$AUTH'}">
		<td>
			<s:submit value="Authorize" name="authorize" id="authorize" onclick="return confirmAuth(this.id)"/>
		</td>
		
		<td>
			<s:submit value="Reject" name="deauthorize" id="deauthorize" onclick="return deAuthReason(this.id, this.value)"/>
		</td>
		</s:if>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
 </form>