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
	var merchanttypeid 	= document.servicereportform.merchanttypeid;	
	if(merchanttypeid.value == "-1")
	{
		errMessage(merchanttypeid,"Please Select Merchant Type");
		return false;
	}
	return true;
}
	
function callAjaxfunctions()
{
	//alert("Calling");
	var merchant_type = document.getElementById("merchanttypeid").value;
	//alert(merchant_type);
	var url="callAJaxviewmerchtypeMerchantRegister.do?merchant_type="+merchant_type;
	var response=AjaxReturnValue(url);
	//alert("response "+response);
	document.getElementById('ajax').innerHTML = response;

	return false;
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body>
<s:form action="downloadmerchanttypeMerchantRegister.do"  name="servicereportform" autocomplete = "off" namespace="/" javascriptTooltip="true">
     <table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount" class="formtable">
 		<tr>
     		<td class="fnt">Select Merchant Type:</td>     		
			<td> : 
				<select name="merchanttypeid" id="merchanttypeid">
						<option value="-1">-SELECT-</option>
						<option value="A">ALL</option>
						<s:iterator value="mregbean.merchtypelist">
						<option value="${MERCHANT_TYPE_ID}"> ${MERCHANT_TYPE_NAME} </option>
					</s:iterator>
				</select> 
			 </td>
     	</tr>
     </table>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>			
			<input type="submit" value="View" name="submit" id="submit" onclick="return validateValues();"/>
		</td>
		<td>
			<input type="submit" name="submit" id="submit" value="Download"  onclick="return validateValues()"/>
		</td>
	</tr>
</table>

<div id="ajax"></div>
</s:form>
</body>
</html>