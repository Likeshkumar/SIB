<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
</head>
<script type="text/javascript">
function validateValues()
{
	var merchantid 	= document.getElementById("merchid");
	if(merchantid.value == "")
	{
		errMessage(merchantid,"Please Enter Merchant ID");
		return false;
	}
	return true;
}
	
function callAjaxfunctions()
{
	//alert("Calling");
	var merchant_id = document.getElementById("merchid");
	if(merchant_id.value == "")
	{
		alert(merchant_id);
		errMessage(merchant_id,"Please Enter Merchant ID");
		return false;
	}
	
	var url="listmerchantdetailsMerchantRegister.do?merchant_id="+merchant_id.value;
	var response=AjaxReturnValue(url);
	//alert("response "+response);
	document.getElementById('ajax').innerHTML = response;

	return false;
}
function confirmUnblock()
{
	var merchid = document.getElementById("merchid").value;
	if ( confirm("Do You Want To Unblock this merchant'"+merchid+"'")  ) 
	{
		return true;
	}
	else
	{
		return false;
	}
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body>
<s:form action="updateunblockmerchantlistMerchantRegister.do"  >
     <table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount" class="formtable">
 		<tr>
     		<td class="fnt"> Enter Merchant ID </td>
     		<td> :
				<s:textfield name="merchid" id="merchid" maxLength="15"/> 
			</td>
     	</tr>
     </table>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>			
			<input type="button" value="Submit" name="submit" id="submit" onclick="return callAjaxfunctions();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />
		</td>
	</tr>
</table>

<div id="ajax"></div>
</s:form>
</body>
</html>
