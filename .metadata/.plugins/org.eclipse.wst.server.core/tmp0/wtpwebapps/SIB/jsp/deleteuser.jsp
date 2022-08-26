<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>


<head>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="addprofile" action="getInstadminuserUserManagementAction" autocomplete="off">
	<div align="center">
		<table border='0' cellpadding='0' cellspacing='6' width='30%' class="formtable" >
				<%
					String adminuserdeletestatus = null;
					adminuserdeletestatus = (String) session.getAttribute("adminuserdeletestatus");
					session.removeAttribute("adminuserdeletestatus");
				%>
				<%
					if (adminuserdeletestatus != null) 
					{
				%>
						<font color="red" ><b><%=adminuserdeletestatus%></b></font>
				<%
					} 
				%>
				<% 
					String Err_status = null;
					Err_status=(String) session.getAttribute("Err_status");
					session.removeAttribute("Err_status");
					if (Err_status != null)
					{
						if(!(Err_status.equals("E")))
						{
				%>		
			<tr align="center">
					<td>
						Institution <b class="mand">*</b>:
					</td>
					<td>
		 				<select name="instname" id="instname">
		 					<option value="-1">--Select Institution--</option>
		 						<s:iterator  value="institutionlist">
		 							<option value="${INST_ID}">${INST_NAME}</option>
		 						</s:iterator>
		 				</select>
					</td>
			</tr>
			<% } } %>
		</table>
		<table>
			<tr>
				<td><s:submit value="Next" name="submit" onclick="return addProfileForm ( )" /></td>
			</tr>		
		</table>
	</div>
</s:form>
