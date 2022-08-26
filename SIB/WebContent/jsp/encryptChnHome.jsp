<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/table.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">

function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;
   return true;
}

function registerValidation()
{
	var encrypt1 = document.getElementById("encrypt");
	//alert(encrypt1);
	if(encrypt1.value==" ")	
	{
		errMessage(customer,"Enter Any value");
		return false;	
	}
}

</script>
<style>
.textcolor
{
color: maroon;
font-size: small;
align:center;
}

</style>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="encryptCardDecryption.do"  method="post" name="orderform" autocomplete = "off"  namespace="/">
	<table border="0" cellpadding="0" cellspacing="4" width="50%" class="formtable">
	

				<tr>
					<td>Enter Clear CHN </td>
					<td>
						<s:textfield name="encrypt" id="encrypt" onkeypress='return isNumberKey(event)' maxlength="19"/>
					</td>
				</tr>
	
	</table>	
	<br>
	<table width='20%'>
		<tr align="center">
			<td colspan='2'><input type="submit"  value="Submit" onclick="return registerValidation();" /></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
	</table>
</s:form>
