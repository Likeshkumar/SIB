<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function coppyEmbosstoEncode(event,value){
	//alert("Event "+event+"  value===>"+value);
	var s = alphabets(event);
	//alert(" ssss "+s);
	if(s==true)
		{
			//alert("Value is Aplpha"+value);
			document.getElementById("encode").value = value;
			return true;
		}else{
			//alert("Invalid");
			return false;
		}
}

function callAjaxfunctions()
{
	var ch_no 	= document.servicereportform.ch_no;	
	if(ch_no.value == "")
	{
		errMessage(ch_no,"Please Enter The Card Number");
		return false;
	} 
	
	var ch_no = document.getElementById("ch_no").value;
	document.getElementById('ajax').innerHTML = "<span style='display:block;text-align:center;font-weight:bold'>Processing....</span>";
	var url="callAJaxservicereportReportgenerationAction.do?ch_no="+ch_no;
	var response=AjaxReturnValue(url);
	//alert("response "+response);
	document.getElementById('ajax').innerHTML = response;

	return false;
}
</script>
<style>
.textcolor
{
color: maroon;
}
#ajax{
	background: none repeat scroll 0 0 #F9F6F4;
    margin: 10px;
 }
</style>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="downloadservicereportReportgenerationAction.do"  name="servicereportform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount" class="formtable" >  	
    <tr>	
		<td> 
		Card Number
		</td>
		<td> : 
			<input type="text" name="ch_no" id="ch_no" style="width:160px" onkeypress="return numerals(event)">
		</td>
	</tr> 
</table>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>			
			<input type="button" value="View" name="submit" id="submit" onclick="return callAjaxfunctions();"/>
		</td>
		<td>
			<input type="submit" name="download" id="download" value="Download"  onclick="return validateValues()"/>
		</td>
	</tr>
</table>
<div id="ajax"></div>
</s:form>

