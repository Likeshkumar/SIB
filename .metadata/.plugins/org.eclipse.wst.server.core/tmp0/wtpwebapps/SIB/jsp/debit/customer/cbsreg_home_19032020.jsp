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
	var cbsidnum = document.getElementById("cbsidnum");
	var seltype = document.getElementById("seltype");
	
	if (seltype.value == "-1")
	{
		errMessage(seltype,"Please Select Type");
		return false;
		
	}
	
	if (cbsacct.value == "" && seltype.value == "1")
	{
		errMessage(cbsacct,"Please Enter Cbs Account Number");
		return false;
		
	}
	if (cbsacct.value.length > 15)
	{
		errMessage(cbsacct,"Account Number length is 15, you entered more than that !!!");
		return false;
		
	}
	if (cbsidnum.value == "" && seltype.value == "2")
	{
		errMessage(cbsidnum,"Please Enter Cbs Id Number");
		return false;
		
	}
	
	if(cbsacct.value != ""){
		var url = "checkAccountNoExistCbsCustomerReg.do?accountnovalue="+cbsacct.value;
		var result = AjaxReturnValue(url);
		//alert(result);
		if(result!='NEW')
		{
			errMessage(cbsacct, "Account No Alredy Exist Try Differant !");
			return false; 
			
		}
	}
	
}
function selectType(val){
	
	var cbsacct = document.getElementById("cbsacct");	
	var cbsidnum = document.getElementById("cbsidnum");
	
	if(val == "1"){
		cbsidnum.disabled=true;
		cbsacct.disabled=false;
	}else if(val == "2"){
		cbsacct.disabled=true;
		cbsidnum.disabled=false;
	}
	
}

</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

 
<form action='cbsCustomerRegEntryListCbsCustomerReg.do' name='form1' id='form1' onsubmit="return showProcessing()">
 
<table border="0" cellpadding="0" cellspacing="2" width="50%" class="formtable viewtable" method="post" >

<tr>
		<td >
			<b>Choose Type</b>
		</td>
		<td >
 				<select name="seltype" id="seltype" onclick="return selectType(this.value)">
 					<option value="-1">--Select Type--</option>
 					<option value="1">CBS Acct No</option>
 					<option value="2">CBS Id No</option>
 				</select>
		</td>
    </tr>
	 
<tr>
	<td> Enter CBS Account Number </td>
	<td><input type="text" name="cbsacct" maxlength="25" id="cbsacct" value=""/></td>
</tr>
<!-- <tr><td> Or </td></tr> -->
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