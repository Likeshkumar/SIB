<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript"></script>
<body>
<form action="savecardlistFraudManagementAction.do" autocomplete="off" name="custinfoform">
	<table>

			<tr>
				<td>CARD NUMBER</td>
				<td>:</td>
				<td><s:textfield name="cardno" id="cardno"></s:textfield></td>
			</tr>
			<tr>
				<td>GROUP DESC:</td>
				<td>:</td>
				<td><s:select list="%{listcardcode}" listKey="CARDGRP_ID" listValue="CARD_NAME"	name="cardgrp" id="cardgrp" headerKey="-1" headerValue="-- Select Group --"></s:select></td>
			</tr>
	</table>
	<table>
		<tr>
			<td>
				<s:submit value="Submit" onclick="return validation()"/>
				<input type="button" onclick="return confirmCancel()" class="cancelbtn" value="Cancel" id="cancel" name="cancel">
			</td>
		</tr>
	</table>
	</form>
</body>
