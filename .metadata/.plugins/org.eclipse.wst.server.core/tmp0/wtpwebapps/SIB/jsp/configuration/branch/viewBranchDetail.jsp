<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
	function goBack()
	{
		window.history.back()
	}
	function validation_authbranch(){
	 	var auth = document.getElementById("auth0").value;
	 	var branchid = document.getElementById("branchid").value;
	 	var BRANCH_NAME = document.getElementById("BRANCH_NAME").value;
	 	var reason = prompt('Enter the Reason for Reject');
		 if( reason ){
			 var url = "authdeauthbranchBranchAction.do?branchid="+branchid+"&BRANCH_NAME="+BRANCH_NAME+"&reason="+reason+"&auth="+auth;
			 window.location = url; 
		 }  
		 return false;
	}
</script>
<style>
	#textcolor
	{
	color: maroon;
	font-size: small;
	}
</style>
<%
String instid = (String)session.getAttribute("Instname"); 
String flag = (String)session.getAttribute("act");
%>
<form action="authdeauthbranchBranchAction.do" method="post" autocomplete="off">
<s:iterator var="subproditr" value="branch_detail">
<s:hidden name="branchid" id="branchid" value="%{BRANCH_CODE}"></s:hidden>
<s:hidden name="BRANCH_NAME" id="BRANCH_NAME" value="%{BRANCH_NAME}"></s:hidden>
		<table  border="1"  cellspacing="0" cellpadding="0" width="80%" rules="none" frame="box">
			<tr>
				<td>BRANCH CODE </td>
				<td id="textcolor">
					${BRANCH_CODE}</td>
				<td>BRANCH NAME</td>
				<td id="textcolor">
					${BRANCH_NAME}</td>
			</tr>
			
			<tr>
				<td>BRANCH ADDRESS 1 </td>
				<td id="textcolor">
					${BR_ADDR1}</td>
				<td>BRANCH ADDRESS 2</td>
				<td id="textcolor">
					${BR_ADDR2}</td>
			</tr>
			
			<tr>
				<td>BRANCH ADDRESS 3 </td>
				<td id="textcolor">
					${BR_ADDR3}</td>
				<td>BRANCH CITY</td>
				<td id="textcolor">
					${BR_CITY}</td>
			</tr>
			
			<tr>
				<td>BRANCH STATE </td>
				<td id="textcolor">
					${BR_STATE}</td>
				<td>BR PHONE </td>
				<td id="textcolor">
					${BR_PHONE}</td>
			</tr>
			<tr>
				<td> AUTH STATUS </td>
				<td id="textcolor">
					${AUTH_CODE}</td>
				<td>CONFIGURED BY</td>
				<td id="textcolor">
					${USER_NAME}
				</td>
			</tr>
			<tr>
				<td> REASON </td>
				<td id="textcolor">
					${REMARKS}
				</td>				
			</tr>
			
			<tr>
			
			</tr>
			
		</table>
	</s:iterator>

<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
		<tr align="center">
		<% if(flag.equals("C")){%>
			<td colspan='8' style='text-align:center'> 
							<input type="submit" name="auth" id="auth1" value="Authorize"/>
							<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authbranch()"/>
			</td>
		<%}else{ %>
			<td>
					<input type="button" value="Back" onclick="goBack();">
			</td>
		<%} %>
		</tr>	
	</table>
</form>