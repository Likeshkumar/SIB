<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<style type="text/css">
.deleteuserselectiondiv
{
    position:absolute;
	border:0px solid #454454;
	left:30%;
	top:25%;
	width:50%;
	height:70%;
}
.tblstyle
{
    font-family:Verdana,Arial, Helvetica, sans-serif;
	font-size:11px;
	font-wight:500;
}

table.formtable td{
	border:1px solid gray;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<s:form action="deleleteInstadminuserUserManagementAction" autocomplete="off" >
	<div align="center">

<table align="center" border="0"  cellspacing="1" cellpadding="0" width="50%" class="formtable">
				<%
					String instadminuserinfo = null;
					instadminuserinfo = (String) session.getAttribute("instadminuserinfo");
					session.removeAttribute("instadminuserinfo");
					String Errstatus = null;
					Errstatus=(String) session.getAttribute("Errstatus");
					session.removeAttribute("Errstatus");
					if (instadminuserinfo != null) 
					{
				%>
						<tr align="center">
							<td colspan="2">		
								<font color="red" ><b><%=instadminuserinfo%></b></font>
							</td>
						</tr>
				<%
					}
					if (Errstatus != null)
					{
						if(!(Errstatus.equals("E")))
						{
				%>
	 

					<s:iterator  value="userdetail">
						<input type="hidden" name="userid" id="userid" value="${USERID}">
						<input type="hidden" name="username" id="username" value="${USERNAME}">
						<s:hidden name="instid" id="instid" value="%{institutionid}"></s:hidden>
						<tr style="background-color: #E0E0EB;">
								<td align="left"> <b>User Name</b></td><td align="left"><b> ${USERNAME}</b> </td>
						</tr>
						
						<tr style="background-color:#F6F6F9"> 
							<td align="left"><b>First Name</b> </td><td align="left"> <b>${FIRSTNAME}</b> </td>
						</tr>
						
						<tr style="background-color: #E0E0EB;">
							<td align="left"> <b>Last Name</b></td><td align="left"><b> ${LASTNAME}</b> </td>
						</tr>
						
						<tr style="background-color:#F6F6F9">
							<td align="left"> <b>Email Id</b></td><td align="left"> <b>${EMAILID}</b> </td>
						</tr>
						
						<tr style="background-color: #E0E0EB;">
							<td align="left"> <b>User Status</b></td><td align="left"> <b>${USERSTATUS} </b></td>
						</tr>
						
						<tr style="background-color:#F6F6F9">
						   <td align="left"> <b>Profile Name</b></td><td align="left"> <b>${PROFILE_NAME}</b> </td> 
						</tr>
					</s:iterator>
						<tr align="center">
							<td colspan="2"><input type="submit" name="submit" value="DELETE"></td>
						</tr>	
				<%
						}
					}
				%>	
		</table>	
	</div>
</s:form>
