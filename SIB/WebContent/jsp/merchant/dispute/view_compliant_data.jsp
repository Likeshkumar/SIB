<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dispute</title>
<script type="text/javascript" src="js/script.js"></script>
</head>
<script>
function showProcessing(){
	
	var status = document.getElementById("status");
	var closecomment = document.getElementById("closecomment"); 
	 
	if( status.value == "-1"){ 	alert("Select the closing status");return false; 	}
	
	
	if( closecomment.value == "" ){ 	alert("Enter the closing comment.");  return false; }
	
	 
	
	if ( confirm ("Do you want to continue....") ){
		
		parent.showprocessing();
	}else{
		return false;
	}
}

</script>
<body>
<s:form  action="changeCompliantStatusDispute.do" autocomplete="off" name="diputeraiseviewform" onsubmit="return showProcessing()">
	 
	 	<s:hidden name="compliantid" id="compliantid" value="%{dispute.compliantcode}" />
		<table border="0" cellpadding="0" cellspacing="0" width="100%" align='center' rules="none" frame="box">
		<tr>
			<td> Registered Date :   <s:property value="dispute.compliantregister"/> </td>
			<td> &nbsp; </td>
			<td> Status :   
				<s:if test=' dispute.compliantstatus=="O" '>  <span style='color:green;font-weight:bold'>   Open </span> </s:if>
				<s:elseif test=' dispute.compliantstatus=="A" '>  <span style='color:maroon;font-weight:bold'>  Approved </span> </s:elseif>
				<s:elseif test=' dispute.compliantstatus=="R" '> <span style='color:red;font-weight:bold'> Rejected </span> </s:elseif>
			</td> 
			<td> &nbsp; </td>
			<td> Registered By :  <s:property value="dispute.usercodebean"/> </td>
			<td> &nbsp; </td>
		</tr>
		</table>
		
	 
	
	<table border="0" cellpadding="0" cellspacing="0" width="75%"> 
	<tr>
	<td valign="top">
				<fieldset>
					<legend><b>Customer Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" style="text-align:left" >
											<tr><td>Name of Cutomer</td><td> : <s:property value="dispute.customernamebean"/> </td></tr>
											<tr><td>Card Number</td><td> :  <s:property value="dispute.cardnobean"/> </td></tr>											
											<tr><td>Account Number</td><td> :  <s:property value="dispute.acctnumberbean"/> </td></tr>
											<tr><td>Email Address</td><td> :  <s:property value="dispute.emailbean"/> </td></tr>
											<tr><td>Phone Number</td><td> :   <s:property value="dispute.phonenumberbean"/> </td></tr>
											<tr><td>Complaint Date</td><td> :  <s:property value="dispute.customernamebean"/> </td></tr>
											<tr><td>Comment</td><td> :  <s:property value="dispute.commentbean"/> </td></tr>
											 
						</table>
				</fieldset>
			</td>
			
			
		<%-- <td>
				<fieldset>
					<legend><b>Transaction Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0"  rules="none" frame="box">
											<tr><td>Transaction Date </td><td> :  <s:property value="dispute.txndatebean"/> </td></tr>
											<tr><td>Network</td><td> :  <s:property value="dispute.cardschemebean"/> </td></tr>
											<tr><td>Bank Name</td><td> : <s:property value="dispute.instidbean"/> </td></tr>											
											<tr><td>Type of Transaction</td><td> : <s:property value="dispute.typeoftxnbean"/> </td></tr>
											<tr><td>Terminal ID</td><td> :  <s:property value="dispute.terminalidbean"/> </td></tr>
											<tr><td>Location</td><td> :   <s:property value="dispute.customernamebean"/> </td></tr>
											<tr><td>Reference Number</td><td> :  <s:property value="dispute.refnobean"/> </td></tr>
											<tr><td>Trace Number</td><td> :  <s:property value="dispute.trancenobean"/> </td></tr>
											<tr><td>Transaction Currency</td><td> :   <s:property value="dispute.txncurrencybean"/> </td></tr>
											<tr><td>Amount Request</td><td> :  <s:property value="dispute.amtreqbean"/> </td></tr>
											<tr><td>Amount Dispensed</td><td> :   <s:property value="dispute.amtdispensedbean"/>  </td></tr>
											<tr><td>Amount to be claim</td> <td> :  <s:property value="dispute.amtclaimedbean"/> </td></tr>
											<tr><td>Reversal Mode </td> <td> :  <s:property value="dispute.reversemode"/> </td></tr>
						</table>
				</fieldset>
			</td> --%>
	</tr>
	</table>
	<s:if test=' dispute.compliantstatus=="O" '>
	<table>
		<tr><td> Status </td> <td> : <s:select list="#{'A':'Approve', 'R':'Reject'}" id="status"  name="status" headerKey="-1" headerValue="-SELECT-"/> </td></tr>
		<tr><td valign="top"> Close Comment  </td> <td> :  <s:textarea name="closecomment" id="closecomment" maxlength="63"  /> </td></tr>
		<tr><td><s:submit value="Submit" id="dispsubmit" name="disputesubmit" onclick="return registerValidation();" /></td>
			<td><input type="button" value="Cancel"  class="cancelbtn" onclick="return confirmCancel()" /></td></tr>
	</table> 
	</s:if>
</s:form>
</body>
</html>