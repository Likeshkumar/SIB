<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script> 

<s:form name="addprofile" action="getEditInstUserManagementAction" autocomplete="off">
<%@taglib uri="/struts-tags" prefix="s" %>
<div align="center">
<table border='0' cellpadding='0' cellspacing='0' width='30%' class="formtable">
<tr align="center">
		<td>
		Institution <b class="mand">*</b>:
		</td>
		<td>
				<s:action name="getEditInstUserManagementAction"/>
 				<!-- <s:bean name="com.ifp.beans.loginbean" var="resd" ></s:bean> -->
 				<select name="instname" id="instname">
 					<option value="-1">--Select Institution--</option>
 					<s:iterator  value="#resd.Instlist">
 						<option value="${INST_ID}">${INST_ID}</option>
 					</s:iterator>
 				</select>
		</td>
</tr>
<tr><td></td><td><s:submit value="Next" name="submit" onclick="return addProfileForm ( )" /></td></tr>
</table>
</div>
</s:form>
