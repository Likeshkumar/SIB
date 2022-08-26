<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script> 
<jsp:include page="/displayresult.jsp"></jsp:include>

<script>
function deAuthorize(){
	if( !selectallvalidationFromIndex() ){
		return false;
	}
	
	var reason = prompt("Reason For Reject");
	if( reason == null ){
		return false;
	}
	document.getElementById("reason").value = reason;
	return true;
}

</script>

<div align="center">
<s:form action="selectedCardListCardMigration"  name="authrizeorder"  autocomplete = "off"  namespace="/">
	<s:hidden name="reason" id="reason" />
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Card Number </th>
						<th> Account No </th>
						<th> Name </th>						
						<th> Mobile No </th>
						<th> CIN</th>
						<th> BIN</th>
						 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cardmigratebean.recordsList" status="incr" >
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum%{#incr.index}" fieldValue="%{CHN}"/>  </td>
						<%-- <td> <s:hidden name="accountno"  id="accountno%{#incr.index}" fieldValue="%{PRIMARY_ACCOUNT_NUMBER}"/> --%>  </td>
						
						<td> ${CHN}  </td>
						<td> ${PAN}  </td>
						<td> ${ENC_NAME}  </td>
						<td> ${MOBILE_NO}  </td>
						<td> ${CUSTOMER_ID} </td> 
						<td> ${BIN}</td> 
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidationFromIndex()"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							
				 
						</td>
					</tr>
	 	</table> 
	
</s:form>
</div>
 