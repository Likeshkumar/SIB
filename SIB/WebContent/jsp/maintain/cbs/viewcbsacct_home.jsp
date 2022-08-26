<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>

<script type="text/javascript">
	function getCbsAccountNumbers(){
		var cardno = document.getElementById("cardno");
		var url = "getCbsAcctDetailsCbsAccount.do?cardno="+cardno.value+"&type=VIEW";
		 var tablerec = ""; 
		var result = AjaxReturnValue(url); 
		 document.getElementById("cbsview").innerHTML=result;
		 return false;
	}
<!--

//-->
</script>
<s:form action="#" name="orderrm" onsubmit="return getCbsAccountNumbers()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="90%"
		align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="40%" align="center">
					<tr>
						<td>Card No</td>
						<td> : <input type='text' name='cardno' id='cardno'   maxlength=19/>
						 <td><s:submit value="Submit" name="order" id="order" /></td>
					</tr> 
				</table>
			</td>
		</tr> 
	</table>

	<table border="0" id="cbsview" cellpadding="0" cellspacing="0" width="100%"	style="text-align:center;border:1px solid gray">
		
	</table>
	
</s:form>

