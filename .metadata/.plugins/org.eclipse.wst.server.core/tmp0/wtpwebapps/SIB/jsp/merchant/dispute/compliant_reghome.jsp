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
	/* var netwrk = document.getElementById("netwrk");
	var bankname = document.getElementById("bankname");
	var branchname = document.getElementById("branchname");
	var txntype = document.getElementById("txntype");
	var terminalid = document.getElementById("terminalid");
	var refnum = document.getElementById("refnum");
	var amtreq = document.getElementById("amtreq");
	var amtdispense = document.getElementById("amtdispense");
	var amtclaimed = document.getElementById("amtclaimed");
	var reversalmode = document.getElementById("reversalmode"); */
	 
	
	 
	
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
	
	/* if( reversalmode.value == "-1"){
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
	if(amtdispense.value=="")	{
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
</script>
</head>
<s:form name="diputeraiseform" action="saveregistercomplaintDispute" autocomplete="off">
	<table border="0" cellpadding="0" cellspacing="0" width="100%"  align='center' rules="none" frame="box">
		<tr>
			<td valign="top">
				<fieldset>
					<legend><b> Customer Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
											<tr><td width="50%">Name of Cutomer</td><td> : <s:textfield name="customer" id="customer"/> <span class="mand">*</span> </td></tr>
											<tr><td>Card Number</td><td> : <s:textfield name="cardno" id="cardno" maxlength="20" value="%{dispute.cardnobean}" readonly="true"/> <span class="mand">*</span></td></tr>
											<tr><td>Account Number</td><td> : <s:textfield name="accnum" id="accnum"/></td></tr>
											<tr><td>Email Address</td><td> : <s:textfield name="emailaddrss" id="emailaddrss"/></td></tr>
											<tr><td>Phone Number</td><td> : <s:textfield name="phnum" id="phnum" maxlength="16"/> <span class="mand">*</span> </td></tr>
											
											<tr>
												<td>Transaction Date</td>
												<td> : 
													<input type="text" name="trandate" id="trandate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
													<span class="mand">*</span>
													<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.diputeraiseform.trandate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">												
												</td>
											</tr>
											
											<tr><td>Complaint Date</td>
												<td> : 
												 
													<s:textfield name="complintdate" id="complintdate" value="%{dispute.compliandatetbean}"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px" />
													 	
												</td>
											</tr>
											
											
											<tr><td>Comment</td><td> : <s:textarea style="height:60px" name="comment" id="comment">   </s:textarea></td></tr>
						</table>
				</fieldset>
			</td>
			<%-- <td>
				<fieldset>
					<legend><b>Transaction Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
											<tr><td>Reversal Mode </td><td> : <s:select name="reversalmode" id="reversalmode" list="#{'420':'Full-Reversal'}" headerKey="-1" headerValue="SELECT"/></td></tr>
											<tr><td width="50%">Network</td><td> : <s:textfield name="netwrk" id="netwrk" value="%{dispute.cardschemebean}" readonly="true"/></td></tr>
											<tr><td>Bank Name</td><td> : <s:textfield name="bankname" id="bankname" value="%{dispute.banknamebean}" readonly="true"/></td></tr>											
											<tr><td>Type of Transaction</td><td> : <s:textfield name="txntype" id="txntype" value="%{dispute.typeoftxnbean}" readonly="true"/></td></tr>
											<tr><td>Terminal ID</td><td> : <s:textfield name="terminalid" id="terminalid" value="%{dispute.terminalidbean}" readonly="true"/></td></tr>
											<tr><td>Location</td><td> : <s:textfield name="termlocation" id="termlocation" value="%{dispute.terminallocationbean}" readonly="true"/></td></tr>
											<tr><td>Reference Number</td><td> : <s:textfield name="refnum" id="refnum"  value="%{dispute.refnobean}" readonly="true" /></td></tr>
											<tr><td>Trace Number</td><td> : <s:textfield name="traceno" id="traceno"  value="%{dispute.trancenobean}" readonly="true" /></td></tr>
											<tr><td>Transaction Currency</td><td> : <s:textfield name="txncurrency" id="txncurrency"  value="%{dispute.txncurrencybean}" readonly="true" onKeyPress="return numerals(event);" /></td></tr>
											<tr><td>Amount Requested</td><td> : <s:textfield name="amtreq" id="amtreq"  value="%{dispute.txnamtbean}" readonly="true" onKeyPress="return numerals(event);" /></td></tr>
											
											<tr><td>Amount to be claimed <a href="#" style='color:red' onclick="calculateAmt()">Click here </a></td><td> : <s:textfield name="amtclaimed" id="amtclaimed" readonly="true" onKeyPress="return numerals(event);"/></td></tr>
						</table>
				</fieldset>
			</td> --%>
			
			<s:hidden name="refnum" value="%{dispute.refnobean}"/>
			<s:hidden name="traceno" value="%{dispute.trancenobean}"/>
			<s:hidden name="amtreq" value="%{dispute.txnamtbean}"/>
			
		</tr>
	</table>
	<table>
		<tr><td><s:submit value="submit" id="dispsubmit" name="disputesubmit" onclick="return registerValidation();"/></td><td><input type="button" value="Cancel"  class="cancelbtn" onclick="javascript:window.close();" /></td></tr>
	</table> 
</s:form>
</body>
</html>