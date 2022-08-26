<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String instCardorderErrorStatus=null;
	String instCardorderErrorMessage=null;
	String instCardorderSaveError = null;
	String instCardorderSaveErrorMessage = null;
	
	
	instCardorderErrorStatus = (String) session.getAttribute("instCardorderErrorStatus");
	instCardorderErrorMessage=(String) session.getAttribute("instCardorderErrorMessage");
	instCardorderSaveError=(String) session.getAttribute("instCardorderSaveError");
	instCardorderSaveErrorMessage=(String) session.getAttribute("instCardorderSaveErrorMessage");
	
	session.removeAttribute("instCardorderErrorStatus");
	session.removeAttribute("instCardorderErrorMessage");
	session.removeAttribute("instCardorderSaveError");
	session.removeAttribute("instCardorderSaveErrorMessage");
	
	if(instCardorderSaveError != null)
	{
%>
						
						<div align="center">
						<table border='0' cellpadding='0' cellspacing='0' width='80%' >
						<tr align="center">
							<td colspan="2">
								<font color="Red"><b><%=instCardorderSaveErrorMessage%></b></font>
							</td>
						</tr>
						</table>
						</div>
<%
	}
if(instCardorderSaveError == "S" || instCardorderSaveError == null)
{
			if(instCardorderErrorStatus =="E" )
			{
			%>
			<div align="center">
			<table border='0' cellpadding='0' cellspacing='0' width='60%' >
			<tr align="center">
				<td colspan="2">
					<font color="Red"><b><%=instCardorderErrorMessage%></b></font>
				</td>
			</tr>
			</table>
			</div>
			<%
			}
			else
			{
			%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="editinstuser" action="getUsereditdetailsUserManagementAction" autocomplete="off">
<s:hidden name="institution_id" id="institution_id" value="%{institutionid}"></s:hidden>
<div align="center">
	<s:if test="%{edit_user=='N'}">
		<table border='0' cellpadding='0' cellspacing='0' width='40%' class="formtable" >
				<tr>
					<td>
						Select User <b class="mand">*</b> :
					</td>
					<td>
		 				<select name="username" id="username">
		 						<option value="-1">--Select The User--</option>
		 					<s:iterator  value="instusername">
		 						<option value="${USERID}">${USERNAME}</option>
		 					</s:iterator>
		 				</select>
					</td>
				</tr>
				<tr align="center">
					<td colspan="2">
						<s:submit value="Next" name="submit" onclick="return EditInstUser();" />
					</td>
				</tr>
			</table>
	</s:if>
	<s:if test="%{edit_user=='Y'}">
			<table border="0" cellpadding="0" cellspacing="0" width="50%">
				<tr>
					<td><font color="red">No User is Added....To Add User </font></td>
					<!--<td><form action="addUserDetUserManagementAction.do"><input type="submit" value="Click Here" style="background:none;border:0;text-decoration:underline;color:blue;cursor:pointer" ></form></td>-->
					<td><a href="addUserDetUserManagementAction.do" style="background:none;border:0;text-decoration:underline;color:blue;cursor:pointer">Click Here</a></td>
				</tr>
			</table>
	</s:if>
	</div>
</s:form>
<% } } %>