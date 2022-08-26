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
<%
String cusname= (String)session.getAttribute("custypedesc");
%>
<form action="savecustomerinfoFraudManagementAction.do" autocomplete="off" name="custinfoform">
	<table>

		<tr>
			<td>NAME</td><td>:</td><td><s:select list="%{schemelist}" listKey="CUSTGRPDESC" listValue="CUSTGRPDESC"	name="card" id="card"></s:select></td>
		</tr>
		<tr>
			<td>MOBILE NUMBER</td><td>:</td><td><s:textfield name="mblnum" id="mblnum"></s:textfield></td>
		</tr>
		<tr>
			<td>CUSTOMER ID</td><td>:</td><td><s:textfield name="cusid" id="cusid"></s:textfield></td>
		</tr>
		<tr>
			<td>DOB</td><td>:</td><td>
				<s:textfield name="dob" id="dob" readonly="true" style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.custinfoform.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>ADDRESS</td><td>:</td><td><s:textfield name="address" id="address"></s:textfield></td>
		</tr>
		<tr>
			<td>NETBANKING_IPADDRESS</td><td>:</td><td><s:textfield name="ipadrss" id="ipadrss"></s:textfield></td>
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
