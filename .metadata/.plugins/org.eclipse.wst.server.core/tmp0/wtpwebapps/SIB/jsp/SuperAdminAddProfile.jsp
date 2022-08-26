<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
<script type="text/javascript" src="js/validationusermanagement.js"></script> 
<script type="text/javascript" src="js/script.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>







<s:form action="superAddProfSuperAdminAddProfAction" name="addprofile" autocomplete="off">
<div align="center">
<!-- <input type="hidden" name="branch" value="0" id="branch"> -->

<table border='0' cellpadding='0' cellspacing='0' width='40%' class="formtable">
		<tr>
				<td>
				Institution :
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

	
   		   
   		    <tr>
	   		    <td>Profile Name :<span><font color="red">*</font></span> </td>
				<td>	<s:textfield name="profilename" id="profilename" onkeypress='return alphanumerals(event)' value="%{bean.profilename}"/>
				<s:fielderror fieldName="profilename" cssClass="errmsg" /></td>
				</td>
			</tr>
			<tr>
				<td>Profile Description : </td>
				<td><textarea name="profiledesc" id="profiledesc" onkeypress='return alphanumerals(event)' style="resize: none;max-width: 200px; max-height: 70px;"  value="%{bean.profiledesc}"></textarea>
				<s:fielderror fieldName="profiledesc" cssClass="errmsg" />
				</td>
			</tr>
			
	
		<!-- 
		<tr>
		
				<td align='center'><input type="checkbox" name="branch" value="1" > </td> 
				<td> Branch Attached </td> 
			
			</tr>
		 
			<tr>
				<td align='center'><input type="checkbox" name="ipaddress" value="1" > </td> 
				<td> Login IP Address </td> 
			</tr>
			<tr>
				<td  align='center'><input type="checkbox" name="userexpdate" value="1" > </td> 
				<td> User Expiry Date </td> 
			</tr>
			
			<tr>
				<td  align='center'><input type="checkbox" name="pswreptable" value="1"  > </td> 
				<td> Password Repeatable </td> 
			</tr>
			<tr>
				<td  align='center'><input type="checkbox" name="pswexpdate" value="1"  > </td> 
				<td> Password Expiry Date </td> 
			</tr>
			
 -->

<%-- <tr><td></td><td><s:submit value="Next" name="submit" onclick="return addProfileForm();" /></td></tr> --%>
<tr><td></td><td><s:submit value="Next" name="submit" /></td></tr>
</table>


</div>
</s:form>
