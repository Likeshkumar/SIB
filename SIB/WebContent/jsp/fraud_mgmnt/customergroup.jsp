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

<script type="text/javascript">


</script>
<body>
<form action="savecustomergrpFraudManagementAction.do" autocomplete="off" name="custinfoform">
	<table>
		<tr>
			<td>GROUP NAME</td><td>:</td><td><s:textfield name="cusname" id="cusname"></s:textfield></td>
		</tr>
		<tr>
			<td>GROUP DESCRIPTION</td><td>:</td><td><s:textarea name="cusdesc" id="cusdesc"></s:textarea></td>
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