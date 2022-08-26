<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script> 
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="chanePswUserManagementAction" autocomplete="off">
<body>
<div align="center">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
<td>Old Password : </td>
<td><input type="password" name="oldpassword" id="oldpassword"></td>
</tr>

<tr>
<td>New Password : </td>
<td><input type="password" name="newpassword" id="newpassword"></td>
</tr>

<tr>
<td>Confirm New Password : </td>
<td><input type="password" name="cnewpassword" id="cnewpassword"></td>
</tr>

<tr><td></td><td>
		<input type="hidden" name="token" id="csrfToken"value="${sessionScope.token}">
<input type="submit" name="submit" onclick="return passwordvalidator();" value="SUBMIT"></td></tr>
</table>

</div> 
</body>
</s:form>

 
 