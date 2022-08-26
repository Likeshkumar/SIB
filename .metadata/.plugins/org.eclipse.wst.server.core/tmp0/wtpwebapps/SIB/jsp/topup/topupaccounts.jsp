<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<script type="text/javascript" src="js/script.js"></script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="topupCardAccountamountTopupAction"  name="cardtopup" autocomplete="off" namespace="/">
	<fieldset style="width: 14cm; height: 2cm;">
	<legend>Account Info</legend>
	<br>
	<table border="0" cellpadding="0" cellspacing="5">
		<s:iterator value="cardaccountlist">
		<tr>
			<td>
				<s:checkbox name="acctnum" id="acctnum" fieldValue="%{ACCT_NO}"></s:checkbox>
			</td>
			<td>
				${ACCT_NO}
			</td>
			<td>
				${CCY_DESC} Account
			</td>
		</tr>
		</s:iterator>
	</table>
	</fieldset>
	<table border="0" cellpadding="0" cellspacing="5">
	<tr>
		<td>
		<s:submit value="TopUp" name="Next" />
		</td>
		
		<td>
		<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
	</table>
</s:form>