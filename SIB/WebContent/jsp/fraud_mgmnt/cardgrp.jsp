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
<form action="savecardgrpFraudManagementAction.do"  autocomplete="off" name="custinfoform">
	<table border="0" cellpadding="0" cellspacing="0" class="formtable">
			<tr>
				<td>CARD NAME</td>
				<td>:</td>
				<td><s:textfield name="cardgrp" id="cardgrp"></s:textfield></td>
			</tr>
			<%--  
			<tr>
				<td>CARD DESCRIPTION</td>
				<td>:</td>
				<td><s:textarea name="carddesc" id="carddesc"></s:textarea></td>
			</tr>
			 --%>
			<tr>
				<td>DEVICE TYPE</td>
				<td>:</td>
				<td><s:select list="#{'P':'POS','ATM':'ATM'}" name="devicetype" id="devicetype" headerKey="-1" headerValue="-- Select Device Type --"></s:select></td>
			</tr>
			<tr>
				<td>TXN TYPE</td>
				<td>:</td>
				<td><s:select list="%{schemelist}"  listKey="ACTION_CODE"  listValue="ACTION_DESC" name="txncode" id="txncode" headerKey="-1" headerValue="-- Select Txncode --"></s:select></td>
			</tr>
			<tr>
				<td>SCHEME CODE</td>
				<td>:</td>
				<td><s:select list="%{schemedetails}"   listKey="BIN" listValue="SCHEME_NAME" name="shemecode" id="shemecode" headerKey="-1" headerValue="-- Select Scheme --"></s:select></td>
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