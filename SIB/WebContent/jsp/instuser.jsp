<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="editinstuser" action="getInstadminuserdetailsUserManagementAction" autocomplete="off">

 
<s:hidden name="inst_id" id="inst_id" value="%{inst_bean}"></s:hidden>	
	<div align="center">
		<table border='0' cellpadding='0' cellspacing='0' width='40%'  class="formtable" >
		<tr align="center">
			<td colspan="2">
					<%
							String adminusersdetails = null;
							adminusersdetails = (String) session.getAttribute("userdeletestatus");
							session.removeAttribute("userdeletestatus");
					%>
						<%
							if (adminusersdetails != null) 
								{
						%>
							<font color="red" ><b><%=adminusersdetails%></b></font>
						<%
								} 
						%>
			</td>
		</tr>
			<% 
				String Eror_status = null;
				Eror_status=(String) session.getAttribute("Eror_status");
				session.removeAttribute("Eror_status");
				if (Eror_status != null)
				{
					if(!(Eror_status.equals("E")))
					{
			%>	
			<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>	
			<tr>
				<td>
				Select User :
				</td>
				<td>
	 				<select name="username" id="username">
	 					<option value="-1">--Select User--</option>
	 					<s:iterator  value="instusername">
	 						<option value="${USERID}">${USERNAME}</option>
	 					</s:iterator>
	 				</select>
				</td>
			</tr>
			<tr><td></td><td><s:submit value="Next" name="submit" onclick="return EditInstUser ( )" /></td></tr>
			<% } } %>
		</table>
	</div>
 
 
</s:form>
