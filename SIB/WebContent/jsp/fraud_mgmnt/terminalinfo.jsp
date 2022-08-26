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
	<form action="saveterminalinfoFraudManagementAction.do" autocomplete="off"  name="custinfoform">
<table>
		<tr>
			<td>
				TERMINAL ID
			</td>
			<td>:</td>
			<td><s:textfield name="terminalid" id="terminalid"></s:textfield></td>
		</tr>
		<tr>
			<td>
				TERMINAL TYPE
			</td>
			<td>:</td>
			<td><s:select list="#{'P':'POS','ATM':'ATM'}" headerKey="-1"  headerValue="-SELECT TERMINAL-" name="terminaltype" id="terminaltype"></s:select></td>
		</tr>
		<tr>
			<td>
				TERMINAL LOCATION
			</td>
			<td>:</td>
			<td><s:textfield name="terminalloc" id="terminalloc"></s:textfield></td>
		</tr>
		<tr>
			<td>
				DISTANCE FROM HEADOFFICE
			</td>
			<td>(in meters)</td>
			
			<td><s:textfield name="distance" id="distance"></s:textfield></td>
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
