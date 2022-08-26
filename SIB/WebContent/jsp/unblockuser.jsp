<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
<body>
<div align="center">
	<s:if test="%{unblocklist_status=='Y'}">
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="80%">
				<tr>
					<th colspan="2">LIST OF BLOCKED USERS</th>
				</tr>
			</table>
				<br><br>
				<table align="center" border="0"  cellspacing="1" cellpadding="0" width="80%" class="formtable">
				<tr style="color: maroon;">
					<th>User Name</th>
					<th>Institution Id</th>
					<th>Profile Name</th>
					<th>User Status</th>
					<th>Unblocked-By</th>
					<th>Unblocked-Date</th>
					<th>Unblock</th>
				</tr>
				 <s:iterator  value="blockuserdetail"  >
					<tr>
						<td style="text-align:center;"> 
						${USERNAME}</td>
						<td style="text-align:center;"> 
						${INSTID}</td>
						<td style="text-align:center;"> 
						${PROFILE_NAME}</td>
						<td style="text-align:center;"> 
						${USERBLOCK}</td>
						<td style="text-align:center;"> 
						${UNBLOCKED_BY}</td>
						<td style="text-align:center;"> 
						${UNBLOCKED_DATE}</td>
						<td>
							<form action="unblckusrUserManagementAction.do" method="post">
							<input type="image"  src="images/unlock 3.png" alt="submit Button">
							<input type="hidden" name="userid" value="${USERID}">
							<input type="hidden" name="instid" id="instid" value="${INSTID}">
							</form>
						</td>
					</tr>
				</s:iterator>
			</table>
	</s:if>
	<s:if test="%{unblocklist_status=='N'}">
		<table border="0" cellpadding="0" cellspacing="0" width="80%">
			<tr>
				<td align="center">
						<b style="color: Red;width: 15px">No Blocked User Exists</b>
				</td>
			</tr>
		</table>
	</s:if>
	<s:if test="%{unblocklist_status=='E'}">
		<table border="0" cellpadding="0" cellspacing="0" width="60%">
			<tr>
				<td>
						<b style="color: Red;width: 15px">Error While Getting the Blocked User Details</b>
				</td>
			</tr>
		</table>	
	</s:if>

</div>
</body>
</html>