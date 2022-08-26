<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
				<%
					String Errorstat=null;
					String ErrorMessage=null;
					Errorstat=(String) session.getAttribute("Errorstat");
					ErrorMessage=(String) session.getAttribute("ErrorMessage");
					session.removeAttribute("Errorstat");
					session.removeAttribute("ErrorMessage");
					if (ErrorMessage != null ) 
					{
				%>
					<table border="0" cellpadding="0" cellspacing="0" width="90%"><tr> <td  align="center"> 
						<tr align="center">
						<td colspan="2">
							<font color="Red"><b><%=ErrorMessage%></b></font>
						</td>
						</tr>
					</table>
				<% 
					}
					else
					{
				%>
					<table border="0" cellpadding="0" cellspacing="0" width="90%"><tr> <td  align="center"> 
						<tr align="center">
						<td colspan="2">
							<font color="Red"><b>Please Try Again</b></font>
						</td>
						</tr>
					</table>
				<%
					}
				%>