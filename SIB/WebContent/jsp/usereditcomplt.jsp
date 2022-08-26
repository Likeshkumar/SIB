<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<div align="center">

		<table border='0' cellpadding='0' cellspacing='0' width='50%' >
		<tr align="center">
			<td colspan="2">
				<%
					String userstatus_update = null;
					userstatus_update = (String) session.getAttribute("userstatus_update");
					session.removeAttribute("userstatus_update");
				%>
				<%
					if (userstatus_update != null) 
					{
				%>
						<font color="red" ><b><%=userstatus_update%></b></font>
				<%
					} 
				%>
			</td>
		</tr>
		</table>
	<div align="center" >
			<s:iterator  value="updateduser_detail">
					<table border="0"  width="50%"  class="tblstyle">
						<tr><td><input type="hidden" name="userid" id="userid" value="${USERID}"></td></tr>
						<tr>  	
							<td  align="left" ><b>User Name</b></td>
							<td align="left"><input type="text" name="username" value="${USERNAME}" maxlength="16"></td> 
						</tr>
						<tr> 
							<td align="left"><b>First Name</b> </td>
							<td align="left"> <input type="text" name="firstname"  value="${FIRSTNAME}" maxlength="25" ></td>
						</tr>
						<tr>
							<td align="left"> <b>Last Name</b></td>
							<td align="left"><input type="text" name="lastname" value="${LASTNAME}" maxlength="25"> </td>
						</tr>
						<tr>
							<td align="left"> <b>Email Idsss</b></td>
							<td align="left"> <input type="text" name="email" value="${EMAILID}" maxlength="30"> </td>
						</tr>
						<tr>
							<td align="left"> <b>User Status</b></td>
							<td align="left"> <input type="text" name="UserStatus" value="${USERSTATUS}"> </td>
						</tr>
					</table>
		</s:iterator>	
	</div> 
</div>
