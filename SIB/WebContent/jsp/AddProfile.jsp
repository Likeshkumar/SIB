<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<s:form action="adminProfUserManagementAction" name="addprofile" autocomplete="off">
<div align="center">
<table border='0' cellpadding='0' cellspacing='0' width='40%' >
		<tr>
		<td>
					<s:if test="%{#display !=''}">
						<tr><td></td><td align="center"><font color="red"><s:property value="display"></s:property></font></td></tr>
					</s:if>
		</td>
					
		</tr>
	
		<tr>
				<td>
				Institution :
				</td>
				<td>
						
		 				
		 				<select name="instname" id="instname">
		 				<option value="-1">--Select Institution--</option>
		 				<s:iterator  value="institutionlist">
		 				<option value="${INST_ID}">${INST_ID}</option>
		 				</s:iterator>
		 				</select>
				</td>
		</tr>

	
   		   
   		    <tr>
	   		    <td>Profile Name :<span><font color="red">*</font></span> </td>
				<td><input type="text" name="profilename" id="profilename" maxlength="20"/></td>
			</tr>
			<tr>
				<td>Profile Description : </td>
				<td><textarea name="profiledesc" id="profiledesc" style="resize: none;max-width: 200px; max-height: 70px;" maxlength="70"></textarea></td>
			</tr>
			
			
			
			<tr>
				<td><input type="checkbox" name="branch" value="1" > </td> 
				<td> Branch Validation </td> 
			</tr>
			
			<!-- 
			<tr>
				<td><input type="checkbox" name="retrycount" value="1" > </td> 
				<td> Login Retry Count </td> 
			</tr>
			 -->
			 
			<tr>
				<td align='center'><input type="checkbox" name="ipaddress" value="1"  > </td> 
				<td> Login IP Address </td> 
			</tr>
			<tr>
				<td  align='center'><input type="checkbox" name="userexpdate" value="1" > </td> 
				<td> User Expiry Date </td> 
			</tr>
			
			<tr>
				<td  align='center'><input type="checkbox" name="pswreptable" value="1" > </td> 
				<td> Password Repeatable </td> 
			</tr>
			<tr>
				<td  align='center'><input type="checkbox" name="pswexpdate" value="1" > </td> 
				<td> Password Expiry Date</td> 
			</tr>
			


<tr><td></td><td><s:submit value="Next" name="submit" onclick="return addProfileForm ( )" /></td></tr>
</table>

<s:bean name="com.ifp.Action.userManagementAction" var="resd" >
	<s:param name="display"></s:param>
</s:bean>
</div>
</s:form>
