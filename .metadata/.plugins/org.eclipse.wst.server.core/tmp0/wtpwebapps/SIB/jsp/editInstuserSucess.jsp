<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
		<table border='0' cellpadding='0' cellspacing='0' width='30%' >
		<tr align="center">
			<td colspan="2">
					<%
							String usereditstatus = null;
							usereditstatus = (String) session.getAttribute("userEditdetailsMessage");
							session.removeAttribute("userEditdetailsMessage");
					%>
						<%
							if (usereditstatus != null) 
								{
						%>
							<font color="red" ><b><%=usereditstatus%></b></font>
						<%
								} 
						%>
			</td>
		</tr>

</table>
</div>