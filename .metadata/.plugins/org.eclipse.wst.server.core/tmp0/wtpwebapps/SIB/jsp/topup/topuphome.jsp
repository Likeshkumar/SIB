<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
	function getAccounts()
	{
		alert("Get Account Info details ");
		var cardno = document.getElementById("cardnum").value;
		alert("Card No ===> "+cardno);
		var urlinfo = "gettingCardaccountsTopupAction.do?cardinfo="+cardno;
		alert("URL IS ===> "+urlinfo);
		var reponse = AjaxReturnValue(urlinfo);
		alert("reponse"+reponse);
		document.getElementById("acctinfo").innerHTML = reponse;
		return true;
	}
	
	function checkCardnumber()
	{
		alert("Checking Card");
		var cardno = document.getElementById("cardnum").value;
		alert("Card Number==> "+cardno);
		if(cardno == "")
		{
			alert("Please Card Number");
			return false;
		}
		return true;
	}

</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="getAccountdetailsTopupAction"  name="cardtopup" autocomplete="off" namespace="/">
<center>
	<fieldset style="width: 14cm; height: 2cm;">
	<legend>Card Info</legend>
	<br>
		<table border="0" cellpadding="0" cellspacing="5">
			<tr>
				<td>
					Card Number
				</td>
				<td>
					<s:textfield name="cardnum" id="cardnum" />
				</td>
				<!-- 
				<td>
					<input type="image"  src="images/view.png" alt="button" onclick="return getAccounts();">
				</td>
				 -->
			</tr>
		</table>
	</fieldset>
</center>
<!-- 
	<fieldset style="width: 14cm; height: 2cm;">
	<legend>Card Info</legend>
	<table border="0" cellpadding="0" cellspacing="5">
		<div id="acctinfo">
		</div>
	</table>
	</fieldset>	
 -->
	<table border="0" cellpadding="0" cellspacing="5">
	<tr>
		<td>
			<s:submit name="topup" id="topup" value="Next" onclick="return checkCardnumber();"></s:submit>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>

	</table>
 
</s:form>