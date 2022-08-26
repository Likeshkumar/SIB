<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
<style type="text/css">
.deleteuserselectiondiv
{
    position:absolute;
	border:0px solid #454454;
	left:30%;
	top:25%;
	width:50%;
	height:70%;
}
.tblstyle
{
    font-family:Verdana,Arial, Helvetica, sans-serif;
	font-size:11px;
	font-wight:500;
}

table.formtable td{
	border:1px solid #efefef;
	text-align:left;
}
</style>


<script>
	function deAuthorize(){
		var deauth = prompt("Enter the reason for De-Authorization", ""); 
		document.getElementById("reason").value = deauth; 
	}
	function authUser(){
		if ( !confirm ("Do you want to continue") ){
			return false;
		}
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<s:form action="authDeauthActionUserUserManagementAction.do" autocomplete="off" onsubmit="validateRecords();" >
	<div align="center">
		<table align="center" border="0"  cellspacing="0" cellpadding="0" width="40%" class="formtable">
		<tr><td>
		<s:hidden name="reason" id="reason"/>
			<s:iterator  value="userdetail">
					<input type="hidden" name="userid" id="userid" value="${USERID}">
					<input type="hidden" name="username" id="username" value="${USERNAME}">
					<s:hidden name="instid" id="instid" value="%{institutionid}"></s:hidden>
					<tr>
							<td align="left"> <b>User Name</b></td><td align="left"><b> ${USERNAME}</b> </td>
					</tr>
					
					<tr style="background-color:#F6F6F9"> 
						<td align="left"><b>First Name</b> </td><td align="left"> <b>${FIRSTNAME}</b> </td>
					</tr>
					
					<tr>
						<td align="left"> <b>Last Name</b></td><td align="left"><b> ${LASTNAME}</b> </td>
					</tr>
					
					<tr style="background-color:#F6F6F9">
						<td align="left"> <b>Email Id</b></td><td align="left"> <b>${EMAILID}</b> </td>
					</tr> 
					
					<tr >
					   <td align="left"> <b>Profile Name</b></td><td align="left"> <b>${PROFILEDESC}</b> </td> 
					</tr>
					
					<tr style="background-color:#F6F6F9">
						<td align="left"> <b>User Status</b></td><td align="left"> <b>${AUTH_STATUS} </b></td>
					</tr>
					
					<tr>
					 		<td align="left"> <b>Branch Desc</b></td><td align="left"> <b>${BRANCHDESC} </b></td>
					
					</tr>
					
				</s:iterator>	 
				</td></tr></table>
				
				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Authorize" name="auth" id="order" onclick="return authUser()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
		</td>
		<td>
			<s:submit value="Reject" name="deauth" id="order" onclick="return deAuthorize()"/>
		</td>
		</tr>
</table>
	</div>
</s:form>
