<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">
function smsGroupvalidation()
{
	var name = document.getElementById("smsgroupname");
	var desc = document.getElementById("smsgroupdesc");
	if(name.value == "")
	{
		alert(" Please Enter Group Name ");
		errMessage( name, "Please Enter Group Name " );
		return false;
	}
	if(desc.value == "")
	{
		alert(" Please Enter Group Description ");
		errMessage( desc, "Please Enter Group Description" );
		return false;
	}
	return true;
}
</script>
<script type="text/javascript" src="js/script.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<div align="center">
<s:form action="addingSMSGroupFraudManagementAction.do" name="addsmsgroup" autocomplete="off" namespace="/">
	<fieldset style=" height: 117px;width: 353px;">
	<legend><b> SMS Group Configure </b></legend>
	<br>	
	<table border="0" cellpadding="0" cellspacing="0" width="50%">
		<tr>
			<td>
				Group Name
			</td>
			<td>:</td>
			<td>
				<s:textfield name="smsgroupname" id="smsgroupname"></s:textfield>
			</td>
		</tr>		
		<tr>
			<td>
				Group Description 
			</td>
			<td>:</td>
			<td>
				<s:textfield name="smsgroupdesc" id="smsgroupdesc"></s:textfield>
			</td>
		</tr>
	</table>
	<br>
	</fieldset>
	<table border="0" cellpadding="0" cellspacing="0" width="20%">
		<tr>
			<td>
				<s:submit value="Submit" name="submitsmsgrp" id="submitsmsgrp" onclick="return smsGroupvalidation();"/>
			</td>
			<td>
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>			
			</td>
		</tr>
	</table>
</s:form>
</div>