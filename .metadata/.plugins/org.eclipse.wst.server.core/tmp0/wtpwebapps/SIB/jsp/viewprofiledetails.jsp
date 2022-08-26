<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
</head>
	<body>
		<s:if test="%{view_profile=='N'}">
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
				<tr style="color: maroon;">
					<th>PROFILE NAME</th>
					<th>PROFILE DETAILS</th>
				</tr>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator  value="profiledetail">
						<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
							<td style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${PROFILE_NAME}</td>
							<td align="center">${PROFILE_DESC}</td>
						</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:if test="%{view_profile=='Y'}">
			<table border="0" cellpadding="0" cellspacing="0" width="60%">
				<tr>
					<td align="center">
							<b style="color: Red;width: 15px">No Profile Exist</b>
					</td>
				</tr>
			</table>
		</s:if>
	</body>
</html>