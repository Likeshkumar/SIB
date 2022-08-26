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
	
	if(customer.value=="")	{
		errMessage(customer,"ENTER CUSTOMER NAME");
		return false;	
	}
	if(cardno.value=="")	{
		errMessage(cardno,"ENTER CARD NUMBER");
		return false;	
	}
	if(accnum.value=="")	{
		errMessage(accnum,"ENTER ACOUNT NUMBER");
		return false;	
	}
	if(emailaddrss.value=="")	{
		errMessage(emailaddrss,"ENTER EMAIL ADDRESS");
		return false;	
	}
	else{
		if( !emailvalidator( emailaddrss.value ) ) {
			 	errMessage(emailaddrss,"InValid E-Mail Address");
			 	return false;
		}
	}
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
	}	
}
</script>
</head>
<s:form name="diputeraiseform" action="saveregistercomplaintMerchantProcess" autocomplete="off">
	<table border="0" cellpadding="0" cellspacing="0" width="100%"  align='center' rules="none" frame="box">
		<tr>
			<td>
				<fieldset>
					<legend><b> Enter Customer Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
											<tr><td width="50%">Name of Cutomer</td><td><s:textfield name="customer" id="customer"/></td></tr>
											<tr><td>Card Holder Number</td><td><s:textfield name="cardno" id="cardno" maxlength="20"/></td></tr>
											<tr><td>Account Number</td><td><s:textfield name="accnum" id="accnum"/></td></tr>
											<tr><td>Email Address</td><td><s:textfield name="emailaddrss" id="emailaddrss"/></td></tr>
											<tr><td>Phone Number</td><td><s:textfield name="phnum" id="phnum"/></td></tr>
											<tr><td>Complaint Date</td>
												<td>
													<input type="text" name="complintdate" id="complintdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
													<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.diputeraiseform.complintdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">	
												</td>
											</tr>
											<tr>
												<td>Transaction Date</td>
												<td>
													<input type="text" name="trandate" id="trandate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
													<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.diputeraiseform.trandate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">												
												</td>
											</tr>
											<tr><td>Comment</td><td><s:textarea style="height:60px" name="comment" id="comment">   </s:textarea></td></tr>
						</table>
				</fieldset>
			</td>
			<td>
				<fieldset>
					<legend><b>Enter Transaction Details</b></legend>
						<table border="0" cellpadding="0" cellspacing="0" align='center' rules="none" frame="box">
											<tr><td width="50%">Network</td><td><s:textfield name="netwrk" id="netwrk"/></td></tr>
											<tr><td>Bank Name</td><td><s:textfield name="bankname" id="bankname"/></td></tr>
											<tr><td>Branch Name</td><td><s:textfield name="branchname" id="branchname"/></td></tr>
											<tr><td>Type of Transaction</td><td><s:textfield name="txntype" id="txntype"/></td></tr>
											<tr><td>Terminal ID</td><td><s:textfield name="terminalid" id="terminalid"/></td></tr>
											<tr><td>Reference Number</td><td><s:textfield name="refnum" id="refnum"/></td></tr>
											<tr><td>Amount Request</td><td><s:textfield name="amtreq" id="amtreq" onKeyPress="return numerals(event);" /></td></tr>
											<tr><td>Amount Dispensed</td><td><s:textfield name="amtdispense" id="amtdispense" onKeyPress="return numerals(event);" /></td></tr>
											<tr><td>Amount to be claimed</td><td><s:textfield name="amtclaimed" id="amtclaimed" onKeyPress="return numerals(event);"/></td></tr>
						</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<table>
		<tr><td><s:submit value="submit" onclick="return registerValidation();" /></td><td><s:submit value="Cancel"/></td></tr>
	</table> 
</s:form>
</body>
</html>