<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<%@taglib uri="/struts-tags" prefix="s"%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%String flag = (String)session.getAttribute("PROFILE_FLAG"); %>

<div align="center">
<s:form action="profileAction" name="addprofiledetails" autocomplete="off">

		<table border='0' cellpadding='0' cellspacing='0' width='50%' >
		<tr align="center">
			<td colspan="2">
				<%
					String user_mgnt_message = null;
					user_mgnt_message = (String) session.getAttribute("user_mgnt_message");
					session.removeAttribute("user_mgnt_message");
				%>
				<%
					if (user_mgnt_message != null) 
					{
				%>
					<font color="Red"><b><%=user_mgnt_message%></b></font>
				<%
					} 
				%>	
			</td>			
		</tr>
		
		
		<tr align="center">
			<td colspan="2">
					<%
							String addprofile_status  = null;
							addprofile_status = (String) session.getAttribute("ErrorMessage");
							session.removeAttribute("ErrorMessage");
					%>
						<%
							if (addprofile_status != null) 
								{
						%>
						<%
								} 
						%>
			</td>
		</tr>
		</table>



	<fieldset style="width:70%">
	<legend><b>Profile Details:</b></legend>
		
		<table id="errmsg"></table>
		<br> 
			<table border='0' cellpadding='0' cellspacing='0' width='50%' align="center">
			<tr>
				<td>Institute Name : </td> 
				<td> <input type="hidden" name="instname" value="instname"  id="instname"><font color="blue"><b>${instname}${BRANCHATTCHED}</b></font> </td> 
			</tr>
			<tr>
				<td>Profile Name :<span><font color="red">*</font></span> </td>
				<td><s:textfield name="profilename" id="profilename" onkeypress='return alphanumerals(event)' value="%{bean.profilename}"/></td>
			</tr>
			<tr>
				<td>Profile Description : </td>
				<td><textarea name="profiledesc" id="profiledesc" style="resize: none;max-width: 200px; max-height: 70px;" onkeypress='return alphanumerals(event)' ></textarea></td>
			</tr>
			</table>
		<br>
	</fieldset>
	<!-- Login Security Check Legend -->
	<%-- <fieldset style="width: 70%">
	<legend><b>Login Security Check:</b></legend>
	<br> 
	
	<table>
		
		<tr>
			<s:if test="%{brattched=='Y'}">	
					<td><input type="checkbox" name="branch" value="1" id="branch">Branch Validation </td>
			</s:if>
			<s:else>
				<td>
					<input type="hidden" name="branch" value="0" id="branch"> 
				</td> 
			</s:else> 
		</tr>

		<tr>
			<td><input type="checkbox" name="ipaddress" value="1" >Login IP Address </td> 
		</tr>
		<tr>
			<td><input type="checkbox" name="userexpdate" value="1" >User Expiry Date </td> 
		</tr>
		<tr>
			<td><input type="checkbox" name="pswreptable" value="1" >Password Repeatable </td> 
		</tr>
		<tr>
			<td><input type="checkbox" name="pswexpdate" value="1" >Password Expiry Date  </td> 
		</tr>
	</table>
	</fieldset> --%>
	<br>
	<table width="20%">
		<tr>
			<td>
				<%-- <s:submit name="submitprofile" id="submitprofile" value="Next" onclick="return addProfileDetails()"/> --%>
			<s:submit name="submitprofile" id="submitprofile" value="Next" /> 
			
			</td>
			<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>	 
			</td>
		</tr>
	</table>

</s:form>
</div>

