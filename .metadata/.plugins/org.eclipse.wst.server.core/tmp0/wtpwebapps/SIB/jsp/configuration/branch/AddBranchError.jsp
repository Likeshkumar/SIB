<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<div align="center">
<% 	
	String addBranchMessage=null;
	String addBranchErrorStatus=null;
	addBranchMessage=(String) session.getAttribute("addBranchMessage");
	addBranchErrorStatus=(String) session.getAttribute("addBranchErrorStatus");
	session.removeAttribute("addBranchErrorStatus");
	session.removeAttribute("addBranchMessage");
	
	if(addBranchMessage!=null)
	{
		%>
			<table align="center" border="0" cellspacing="0" cellpadding="1" width="50%">
					<tr align="center">
						<td colspan="2">
							<font color="Red"><b><%=addBranchMessage%></b></font>
						</td>
					</tr>
			</table>
		<%
	}
	if((addBranchErrorStatus=="S"))
	{
		%>
		
		
		<table>
			  <tr>
			    <th>YOU ARE NOT VALID FOR THIS OPERATION</th>
			  </tr>
			  <tr>
			    <th>THIS IS HAPPENED SINCE YOU HAVE NO SELECTED  "ATTACH BRANCH"  DURING INSTITUTION</th>
			  </tr>
			  <tr>
			    <th>PLEASE CONTACT YOUR ADMINISTRATOR</th>
			  </tr>
		</table>
		</div>
		<%
	}
		%>