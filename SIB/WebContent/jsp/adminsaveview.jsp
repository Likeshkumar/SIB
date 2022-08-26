<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<div align="center">
<s:form action="saveViewDetailsUserManagementAction" autocomplete="off">

	<s:hidden name="doact" id="doact" value="%{doact}"/>

		   <!-- Profile Details Legend -->
		    <input type="hidden" name="branch" value="0" id="branch">
		    
			<fieldset style="width: 70%;">
   		    <legend><b>Profile Details:</b></legend>
   		    <br> 
   		     <table border='0' cellpadding='0' cellspacing='0' width='50%' align="center">
   		   
			 
			 <s:iterator  value="editprofiledetail"  >
			<input type="hidden" name="profileid" id="profileid" value="${PROFILE_ID}">	
			<input type="hidden" name="instname" id="instname" value="${instname}">	
			<input type="hidden" name="profname" id="profname" value="${PROFILE_NAME}">		
			<tr>
			<td>Profile Name :<span><font color="red">*</font></span> </td>
			<td><b>${PROFILE_NAME}</b>  </td> 
			</tr>
			
			<tr>
			<td>Profile Description: </td>
			<td><textarea name="profiledesc" id="profiledesc" style="resize: none;max-width: 200px; max-height: 70px;">${PROFILE_DESC}</textarea></td>
			</tr>
			
			<tr><td colspan="2"> <table border='0' class="formtable" cellspacing='0' cellpadding='0' width='100%'>
						<tr>
							<td>Config/Edited by1: </td>
							<td class="textcolor">  ${ADDED_BY} </td>
						 
							<td>Configured date : </td>
							<td class="textcolor">   ${ADDED_DATE}  </td>
						</tr>
						<tr>
							<td>Auth/De-auth by </td>
							<td class="textcolor">  ${AUTH_BY} </td>
						 
							<td>Authorized/Deauth date  </td>
							<td class="textcolor">  ${AUTH_DATE} </td>
						</tr>
						<tr>
							<td>Status </td>
							<td class="textcolor">  ${AUTH_CODE} </td>
						 
							<td>Remarks </td>
							<td class="textcolor">  ${REMARKS}</td>
						</tr>
					</table>
				</td>
			</tr> 
			</s:iterator>
			
			</table>
			<br>
			</fieldset>
			
			<!-- Login Security Check Legend -->

			<fieldset style="width: 70%;">
   		    <legend><b>Login Security Check:</b></legend>
   		    <br> 
   		     <table border='0' cellpadding='0' cellspacing='0' width='50%' align="center">		
				
				<%-- <tr>
				<s:if test="%{brattched=='Y'}">
					
				<td><input type="checkbox" name="branch" id="branch"
					<s:if test="%{loginbrnch=='Y'}"> checked='checked' </s:if> 			
					 value='1'>
					</td><td> Branch Validation</td>
				</s:if>
				<s:else>
					<td>
						<input type="hidden" name="branch" value="0" id="branch"> 
					</td> 
				</s:else>
				</tr>  --%>
				
				<tr>
					<td><input type="checkbox" name="ipaddress" disabled="disabled"
					<s:if test="%{loginipadd=='Y'}"> checked='checked' </s:if> 
					value="1" > </td> 
					<td> Login IP Address </td> 
				</tr>
				<tr>
					<td><input type="checkbox" name="userexpdate" disabled="disabled"
					<s:if test="%{loginexpry=='Y'}"> checked='checked' </s:if>
					value="1" > </td> 
					<td> User Expiry Date </td> 
				</tr>
				
				<tr>
					<td><input type="checkbox" name="pswreptable" disabled="disabled"
					<s:if test="%{userpassreapat=='Y'}"> checked='checked' </s:if>
					value="1" > </td> 
					<td> Password Repeatable </td> 
				</tr>
				<tr>
					<td><input type="checkbox" name="pswexpdate" disabled="disabled"
					<s:if test="%{userpassexp=='Y'}"> checked='checked' </s:if>
					value="1" > </td> 
					<td> Password Expiry Date </td> 
				</tr>
			</table>
			<br>
			</fieldset>
			<br>
			<s:submit name="submitprofile" id="submitprofile" value="Next"/>
			<s:reset  value="Reset"/>
			
			
		


</s:form>

</div>


</body>
</html>