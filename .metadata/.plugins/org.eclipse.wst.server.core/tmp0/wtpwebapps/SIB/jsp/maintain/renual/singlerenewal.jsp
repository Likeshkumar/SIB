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

function validation()
{
	
	var cardno = document.getElementById("cardno");	
	if (cardno.value == "")
	{
		errMessage(cardno,"Enter Card Number");
		return false;
		
	}
	
	/* var url = "checkAccountNoExistCbsCustomerReg.do?accountnovalue="+cardno.value;
	var result = AjaxReturnValue(url);
	//alert(result);
	if(result!='NEW')
	{
		errMessage(cbsacct, "Account No Alredy Exist Try Differant !");
		return false; 
		
	} */
	
}

</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

 
<form action='singleRenewalBulkRenual.do' name='form1' id='form1' >
 
<table border="0" cellpadding="0" cellspacing="2" width="50%" class="formtable viewtable" method="post" >	 
<tr>
	<td> Enter Card Number </td>
	
	<td><input type="text" name="cardno" maxlength="19" id="cardno" value=""/></td>
	<td>
		<input type="submit" value="Submit" name="retrieve" id="retrieve" onClick="return validation();"/>
	</td>
	
</tr>
</table>
</form>