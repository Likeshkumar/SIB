<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
				<%
					String error_balance_sheet = null;
					error_balance_sheet = (String) session.getAttribute("error_balance_sheet");
					session.removeAttribute("error_balance_sheet");
				%>
				<%
					if (error_balance_sheet != null) 
					{
				%>
					
					<div align="center">
					<font color="red"> <%=error_balance_sheet%></font>
					</div>
				<% 
					}
				%>