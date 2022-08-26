<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
	<table>
			<%
				String adminuser_delete_status = null;
				adminuser_delete_status = (String) session.getAttribute("adminuser_delete_status");
				session.removeAttribute("adminuser_delete_status");
				if (adminuser_delete_status != null) 
				{
			%>
					<tr align="center">
						<td colspan="2">		
							<font color="red" ><b><%=adminuser_delete_status%></b></font>
						</td>
					</tr>
			<%
				}
			%>
			
	</table>
</div>