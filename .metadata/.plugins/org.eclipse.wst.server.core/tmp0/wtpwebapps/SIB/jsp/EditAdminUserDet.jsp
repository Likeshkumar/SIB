<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script> 
<script type="text/javascript" src="js/script.js"></script> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="addprofile" action="getEditinstuserUserManagementAction" autocomplete="off">
	<div align="center">
		<table border='0' cellpadding='0' cellspacing='6' width='40%' class="formtable" >
					<%
							String usereditstatus = null;
							usereditstatus = (String) session.getAttribute("usereditstatus");
							session.removeAttribute("usereditstatus");
					%>
						<%
							if (usereditstatus != null) 
								{
						%>
							<font color="red" ><b><%=usereditstatus%></b></font>
						<%
								} 
						%>
			<% 
				String Error_status = null;
				Error_status=(String) session.getAttribute("Error_status");
				session.removeAttribute("Error_status");
				if (Error_status != null)
				{
					if(!(Error_status.equals("E")))
					{
			%>
			<tr align="center">
			<td>Institution <b class="mand">*</b>:</td>
					<td>
						<select name="instname" id="instname">
			 				<option value="-1">--Select Institution--</option>
			 				<s:iterator  value="institutionlist">
			 					<option value="${INST_ID}">${INST_NAME}</option>
			 				</s:iterator>
			 			</select>
					</td>
			</tr>
			<tr align="center">
					<td colspan="2">
						<s:submit value="Next" name="submit" onclick="return addProfileForm ( )" />
					</td>
			</tr>
			 <%} }%>
		</table>
	</div>
</s:form>
