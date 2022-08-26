<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">
function mailAddvalidation()
{
	var mailgrp_id = document.getElementById("mailgroupid");
	var mail_id = document.getElementById("mailid");
	if(mailgrp_id.value == "-1")
	{
		alert(" Please Enter Group Name ");
		errMessage( mailgrp_id, "Select Mail Group" );
		return false;
	}
	if(mail_id.value == "")
	{
		
		errMessage( mail_id, "Please Enter Mail Id " );
		return false;
	}
	return true;
}
</script>
<script type="text/javascript" src="js/script.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<div>
<s:form action="addMailConfigurationFraudManagementAction.do" name="addmailid" autocomplete="off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="40%">
		<tr>
			<td>
				Select Group
			</td>
			<td>
				:<s:select list="maillist" listKey="MAILGRP_ID"  listValue="MAILGRP_NAME"  name="mailgroupid" id="mailgroupid"  headerValue="-- Select SMS Group --"  headerKey="-1"/>
			</td>
		</tr>
		<tr>
			<td>
				Mail Id
			</td>
			<td>
				:<s:textfield name="mailid" id="mailid" maxlength="30" />
			</td>			
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="20%">
		<tr>
			<td>
				<s:submit value=" Add " name="submitmailgrp" id="submitmailgrp" onclick="return mailAddvalidation();"/>
			</td>
			<td>
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>			
			</td>
		</tr>		
	</table>
</s:form>
</div>