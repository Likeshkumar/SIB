<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
table.viewtable td{
	padding-top:20px;
}
</style>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script> 
<script>

function retrieveCBSDetails()
{
	
	var cbsacct = document.getElementById("cbsacct");	
	if (cbsacct.value == "")
	{
		errMessage(cbsacct,"Enter Cbs Account Number");
		return false;
		
	}
	
	var url = "checkAccountNoExistCbsCustomerReg.do?accountnovalue="+cbsacct.value;
	var result = AjaxReturnValue(url);
	//alert(result);
	if(result!='NEW')
	{
		errMessage(cbsacct, "Account No Alredy Exist Try Differant !");
		return false; 
		
	}
	
	/*else{
		
		var url = "cbsCustomerRegEntryCbsCustomerReg.do?cbsacct="+cbsacct.value;
		alert(url);
		var result = AjaxReturnValue(url);
		document.getElementById("customerregdetails").innerHTML=result;
	}  */
	
	/* var url = "getCBSdetailsDebitCustomerRegister.do?cbsacct="+cbsacct.value;
	var result = AjaxReturnValue(url);*/
	//document.getElementById("customerregdetails").innerHTML=result;
	//return result;
}

</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

 
<form action='cbsCustomerRegEntryCbsCustomerReg.do' name='form1' id='form1' onsubmit="return showProcessing()">
 
<table border="0" cellpadding="0" cellspacing="2" width="50%" class="formtable viewtable" method="post" >	 
<tr>
	<td> Enter CBS Account Number </td>
	<td><input type="text" name="cbsacct" maxlength="25" id="cbsacct" value=""/></td>
</tr>
<tr>
	<td> Enter CBS Id No </td>
	<td><input type="text" name="cbsidnum" maxlength="25" id="cbsidnum" value=""/></td>
</tr>
</table>
<br>
<div align="center" >
<input type="submit" value="Retrieve" name="retrieve" id="retrieve" onClick="return retrieveCBSDetails();"/>
</div>




</form>