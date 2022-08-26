<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">
function registerValidation(){
	var customer = document.getElementById("customer");
	var cardno = document.getElementById("cardno");
	var accnum = document.getElementById("accnum");
	var emailaddrss = document.getElementById("emailaddrss");
	var phnum = document.getElementById("phnum");
	var complintdate = document.getElementById("complintdate");
	var trandate = document.getElementById("trandate");
	var comment = document.getElementById("comment");
	var netwrk = document.getElementById("netwrk");
	var bankname = document.getElementById("bankname");
	var branchname = document.getElementById("branchname");
	var txntype = document.getElementById("txntype");
	var terminalid = document.getElementById("terminalid");
	var refnum = document.getElementById("refnum");
	var amtreq = document.getElementById("amtreq");
	var amtdispense = document.getElementById("amtdispense");
	var amtclaimed = document.getElementById("amtclaimed");
	var reversalmode = document.getElementById("reversalmode");
	 
	
	 
	
	if(customer.value=="")	{
		errMessage(customer,"ENTER CUSTOMER NAME");
		return false;	
	}
	if(cardno.value=="")	{
		errMessage(cardno,"ENTER CARD NUMBER");
		return false;	
	}
	 
//	if(accnum.value=="")	{		errMessage(accnum,"ENTER ACOUNT NUMBER");	return false;	}  
 //	 if(emailaddrss.value=="")	{	errMessage(emailaddrss,"ENTER EMAIL ADDRESS");	return false;}
//	else{	if( !emailvalidator( emailaddrss.value ) ) { 	errMessage(emailaddrss,"InValid E-Mail Address");	return false; } } 
	   
	if(phnum.value=="")	{
		errMessage(phnum,"ENTER PHONE NUMBER");
		return false;	
	}
	if(complintdate.value=="")	{
		errMessage(complintdate,"ENTER COMPLAINT DATE");
		return false;	
	}
	if(trandate.value=="")	{
		errMessage(trandate,"ENTER TRANSACTION DATE");
		return false;	
	}
	if(comment.value=="")	{
		errMessage(comment,"ENTER COMMENT");
		return false;	
	}
	
	if( reversalmode.value == "-1"){
		errMessage(reversalmode,"Select the Reversal Mode");
		return false;	
	}
	
	
	if(netwrk.value=="")	{
		errMessage(netwrk,"ENTER NETWORK ");
		return false;	
	}
	if(bankname.value=="")	{
		errMessage(bankname,"ENTER BANK NAME");
		return false;	
	}
	if(branchname.value=="")	{
		errMessage(branchname,"ENTER BRANCH NAME");
		return false;	
	}
	if(txntype.value=="")	{
		errMessage(txntype,"ENTER TYPE OF TRANSACTION");
		return false;	
	}
	if(terminalid.value=="")	{
		errMessage(terminalid,"ENTER TERMINAL ID");
		return false;	
	}
	if(refnum.value=="")	{
		errMessage(refnum,"ENTER REF NUM");
		return false;	
	}
	if(amtreq.value=="")	{
		errMessage(amtreq,"ENTER AMOUNT REQUIRED");
		return false;	
	}
	/* if(amtdispense.value=="")	{
		errMessage(amtdispense,"ENTER AMOUNT DISPENSED");
		return false;	
	}
	if(amtclaimed.value=="")	{
		errMessage(amtclaimed,"ENTER AMOUNT CLAIMED");
		return false;	
	}	 */
	
	
}



	
	
function calculateAmt(){
	var requestedamt = document.getElementById("amtreq");
	var dispensedamt = document.getElementById("amtdispense");
	var claimamt = requestedamt.value - dispensedamt.value;
	
	if( isNumber( amtdispense.value ) ) {
		
		document.getElementById("amtclaimed").value = claimamt;
		document.getElementById("dispsubmit").disabled = false;
	}else{
		errMessage(dispensedamt,"THE ENTERED DISPENSED AMOUNT IS INVALID");
		document.getElementById("dispsubmit").disabled = true;
	}
	
}
function submit1(cardno,traceno,refno,amt){
	var instid=document.getElementById("instidbean").value;
	var conf = confirm( "Are you sure, Do you want to register complaint ");
	if( conf ){
		window.location = "registerComplaintDispute.do?cardno="+cardno+"&traceno="+traceno+"&refno="+refno+"&amt="+amt;
	} 
}
</script>
</head>
<s:form name="diputeraiseform" action="#"  autocomplete="off" namespace="/" method="post" >
	<table border="0" cellpadding="0" cellspacing="0" width="100%"  align='center' rules="none" frame="box">
		<tr>
			<td valign="top">
				<fieldset>
					<legend><b>Transaction Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
								<tr><td>Institution</td><td> : <s:textfield name="instidbean" id="instidbean" value="%{dispute.instidbean}" readonly="true"/></td></tr>
								<tr><td>Response</td><td> : <s:textfield name="response" id="response" value="%{dispute.response}" readonly="true"/></td></tr>
								<tr><td>Type of Transaction</td><td> : <s:textfield name="typeoftxnbean" id="typeoftxnbean" value="%{dispute.typeoftxnbean}" readonly="true"/></td></tr>
								<tr><td>Transaction Date</td><td> : <s:textfield name="txndatebean" id="txndatebean" value="%{dispute.txndatebean}" readonly="true"/></td></tr>
								<tr><td>Transaction Amount</td><td> : <s:textfield name="txnamtbean" id="txnamtbean" value="%{dispute.txnamtbean}" readonly="true"/></td></tr>
								<tr><td>Transaction Currency</td><td> : <s:textfield name="txncurrencybean" id="txncurrencybean"  value="%{dispute.txncurrencybean}" readonly="true"/></td></tr>
								<tr><td>Expiry Date</td><td> : <s:textfield name="expirydate" id="expirydate" value="%{dispute.expirydate}" readonly="true"/></td></tr>
						</table>
				</fieldset>
			</td>
			<td>
				<fieldset>
					<legend><b> Acquirer</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
								<tr><td>Country</td><td> : <s:textfield name="country" id="country" value="%{dispute.country}" readonly="true"/></td></tr>
								<tr><td>Customer ID</td><td> : <s:textfield name="customerid" id="customerid" value="%{dispute.customerid}" readonly="true"/></td></tr>
								<tr><td>Terminal ID</td><td> : <s:textfield name="terminalid" id="terminalid" value="%{dispute.terminalidbean}" readonly="true"/></td></tr>
								<tr><td>Activity</td><td> : <s:textfield name="activity" id="activity" value="%{dispute.activity}" readonly="true"/></td></tr>
								<tr><td>Merchant Id</td><td> : <s:textfield name="merchantid" id="merchantid" value="%{dispute.merchantid}" readonly="true"/></td></tr>
								<tr><td>Terminal Location</td><td> : <s:textfield name="terminallocationbean" id="terminallocationbean" value="%{dispute.terminallocationbean}" readonly="true"/></td></tr>
						</table>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td valign="top">
				<fieldset>
					<legend><b>Transaction Input</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
								<tr><td>Acquiring Mode</td><td> : <s:textfield name="acquiringmode" id="acquiringmode" value="%{dispute.acquiringmode}" readonly="true"/></td></tr>
								<tr><td>Card Number Present</td><td> : <s:textfield name="carnopresent" id="carnopresent" value="%{dispute.carnopresent}" readonly="true"/></td></tr>
								<tr><td>Track2 Present</td><td> : <s:textfield name="track2present" id="track2present" value="%{dispute.track2present}" readonly="true"/></td></tr>
								<tr><td>Expiry Date Present</td><td> : <s:textfield name="expdatepresent" id="expdatepresent" value="%{dispute.expdatepresent}" readonly="true"/></td></tr>
								<tr><td>PIN Present</td><td> : <s:textfield name="pinpresent" id="pinpresent" value="%{dispute.pinpresent}" readonly="true"/></td></tr>
								<tr><td>Chip Data Present</td><td> : <s:textfield name="chipdatapresent" id="chipdatapresent" value="%{dispute.chipdatapresent}" readonly="true"/></td></tr>
								<tr><td>CVV1 / iCVV Present</td><td> : <s:textfield name="cvv1icvvpresent" id="cvv1icvvpresent" value="%{dispute.cvv1icvvpresent}" readonly="true"/></td></tr>
								<tr><td>CVV2 Present</td><td> : <s:textfield name="cvv2present" id="cvv2present" value="%{dispute.cvv2present}" readonly="true"/></td></tr>
						</table>
				</fieldset>
			</td>
			<td>
				<fieldset>
					<legend><b> Security Validation</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
								<tr><td>Expiry Date Check</td><td> : <s:textfield name="expdatecheck" id="expdatecheck" value="%{dispute.expdatecheck}" readonly="true"/></td></tr>
								<tr><td>PIN Check</td><td> : <s:textfield name="pincheck" id="pincheck" value="%{dispute.pincheck}" readonly="true"/></td></tr>
								<tr><td>Cryptogram Check</td><td> : <s:textfield name="cryptogramcheck" id="cryptogramcheck" value="%{dispute.cryptogramcheck}" readonly="true"/></td></tr>
								<%-- <tr><td>CVV1 / iCVV Check</td><td> : <s:textfield name="cvv1icvvcheck" id="cvv1icvvcheck" value="%{dispute.cvv1icvvcheck}" readonly="true"/></td></tr>
								<tr><td>CVV2 Check</td><td> : <s:textfield name="cvv2check" id="cvv2check" value="%{dispute.cvv2check}" readonly="true"/></td></tr> --%>
						</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<table>
		<tr><%-- <td><input  type="button"  value="Register Complaints" id="dispsubmit" name="disputesubmit" onclick="return submit1('<s:property value="%{dispute.cardnobean}"/>','<s:property value="%{dispute.trancenobean}"/>','<s:property value="%{dispute.refnobean}"/>','<s:property value="%{dispute.txnamtbean}"/>');"/></td> --%>
		<td><input type="button" value="Cancel"  class="cancelbtn" onclick="javascript:window.close();" /></td></tr>
	</table> 
</s:form>
</body>
</html>