<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">
function mobileaddvalidation()
{
	var smsgroup_id = document.getElementById("smsgroupid");
	var mobile_no = document.getElementById("mobileno");
	if(smsgroup_id.value == "-1")
	{
		alert(" Please Enter Group Name ");
		errMessage( smsgroup_id, "Select SMS Group" );
		return false;
	}
	if(mobile_no.value == "")
	{
		
		errMessage( mobile_no, "Please Enter Mobile Number" );
		return false;
	}
	return true;
}
</script>
<script type="text/javascript" src="js/script.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<div>
<s:form action="addMobilenumberConfigurationFraudManagementAction.do" name="addsmsgroup" autocomplete="off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="40%">
		<tr>
			<td>
				Select Group
			</td>
			<td>
				:<s:select list="smsgrouplist" listKey="SMSGRP_ID"  listValue="SMSGRP_NAME"  name="smsgroupid" id="smsgroupid"  headerValue="-- Select SMS Group --"  headerKey="-1"/>
			</td>
		</tr>
		<tr>
			<td>
				Mobile Number
			</td>
			<td>
				:<s:textfield name="mobileno" id="mobileno" maxlength="18" onKeyPress=" return numerals(event);"/>
			</td>			
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="20%">
		<tr>
			<td>
				<s:submit value=" Add " name="submitsmsgrp" id="submitsmsgrp" onclick="return mobileaddvalidation();"/>
			</td>
			<td>
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>			
			</td>
		</tr>		
	</table>
</s:form>
</div>