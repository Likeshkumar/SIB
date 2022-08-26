<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<style type="text/css">
.tdheadstyle{
	text-align: center;	
	background-color:#D0D0D0;
	vertical-align: middle;
	height: 26px;
    padding-bottom: 0; 
    color: black;
    font:bold;
    font-size: 12px;
    font-family: verdana;
}
.tdstyle{
	padding: 5px;
    text-align: center;
    vertical-align: middle;
    width: 40px;
    background-color:#E8E8E8;
}
</style>
</head>

<s:if test="customer_reg == 'N'">
<s:iterator value="deleteorderinfo">
	 <fieldset style="height: 180px;width: 755px">
	<legend>Order Details</legend>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="padding-top: 12px">
	<tr>
		<td class="tdheadstyle">Order Reference No</td>
		<td class="tdheadstyle">Product Name</td>
		<td class="tdheadstyle">Order DateTime</td>
		<td class="tdheadstyle">Ordered By</td>
		<td class="tdheadstyle">Number Of Cards</td>
	</tr>

	<tr>
		<td class="tdstyle">${ORDER_REF_NO}</td>
		<td class="tdstyle">${PROD_NAME}</td>
		<td class="tdstyle">${ORDERED_DATE}</td>
		<td class="tdstyle">${USER_NAME}</td>
		<td class="tdstyle">${CARD_QUANTITY}</td>
	</tr>
	<tr>
		<td colspan="5">
		</td>
	</tr>	
	<tr>
		<td class="tdheadstyle">Bin</td>
		<td class="tdheadstyle">Embossing Name</td>
		<td class="tdheadstyle">Encoding Name</td>
		<td class="tdheadstyle">Application Date</td>
		<td class="tdheadstyle">Application No</td>
	</tr>
	<tr>
		<td class="tdstyle">${BIN}</td>
		<td class="tdstyle">${EMBOSSING_NAME}</td>
		<td class="tdstyle">${ENCODE_DATA}</td>
		<td class="tdstyle">${APP_DATE}</td>
		<td class="tdstyle">${APP_NO}</td>
	</tr>	
	</table>
	 </fieldset> 
	</s:iterator>
</s:if>

<s:if test="customer_reg == 'Y'">
<s:iterator value="deleteorderinfo">
	 <fieldset style="height: 180px;width: 755px">
	<legend>Order Details</legend>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="padding-top: 12px">
	<tr>
		<td class="tdheadstyle">Order Reference No</td>
		<td class="tdheadstyle">Product Name</td>
		<td class="tdheadstyle">Order DateTime</td>
		<td class="tdheadstyle">Ordered By</td>
		<td class="tdheadstyle">Number Of Cards</td>
	</tr>

	<tr>
		<td class="tdstyle">${ORDER_REF_NO}</td>
		<td class="tdstyle">${PROD_NAME}</td>
		<td class="tdstyle">${ORDERED_DATE}</td>
		<td class="tdstyle">${USER_NAME}</td>
		<td class="tdstyle">${CARD_QUANTITY}</td>
	</tr>
	<tr>
		<td colspan="5">
		</td>
	</tr>	
	<tr>
		<td class="tdheadstyle">Bin</td>
		<td class="tdheadstyle">Embossing Name</td>
		<td class="tdheadstyle">Encoding Name</td>
		<td class="tdheadstyle">Application Date</td>
		<td class="tdheadstyle">Application No</td>
	</tr>
	<tr>
		<td class="tdstyle">${BIN}</td>
		<td class="tdstyle">${EMBOSSING_NAME}</td>
		<td class="tdstyle">${ENCODE_DATA}</td>
		<td class="tdstyle">${APP_DATE}</td>
		<td class="tdstyle">${APP_NO}</td>
	</tr>	
	</table>
	 </fieldset> 
	<br><br>
	<fieldset style="height: 180px;width: 755px">
	<legend>Customer Details</legend> 
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="padding-top: 12px">
	<tr>
		<td class="tdheadstyle">First Name</td>
		<td class="tdheadstyle">Middle Name</td>
		<td class="tdheadstyle">Last Name</td>
		<td class="tdheadstyle">Father Name</td>
		<td class="tdheadstyle">Mother Name</td>
	</tr>
	<tr>
		<td class="tdstyle">${FNAME}</td>
		<td class="tdstyle">${MNAME}</td>
		<td class="tdstyle">${LNAME}</td>
		<td class="tdstyle">${FATHER_NAME}</td>
		<td class="tdstyle">${MOTHER_NAME}</td>
	</tr>
	<tr>
		<td colspan="5">
		</td>
	</tr>	
	<tr>
		<td class="tdheadstyle">Date Of Birth</td>
		<td class="tdheadstyle">E-Mail</td>
		<td class="tdheadstyle">Mobile No</td>
		<td class="tdheadstyle">Phone No</td>
		<td class="tdheadstyle">Occupation</td>
	</tr>
	<tr>
		<td class="tdstyle">${DOB}</td>
		<td class="tdstyle">${EMAIL_ADDRESS}</td>
		<td class="tdstyle">${MOBILE_NO}</td>
		<td class="tdstyle">${PHONE_NO}</td>
		<td class="tdstyle">${OCCUPATION}</td>
	</tr>
	</table>
	</fieldset>
</s:iterator>
</s:if>
