<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
<script>
function goBack()
{
	window.history.back();
}
function valuecheck(){
	//alert("Select the institution ID");
	var inst=document.getElementById("instids");	
	if(inst.value=="-1"){
		errMessage(inst,"Select the institution ID");
		return false;
	}
	return true;
}

function selectChechBox(){
	var checkeduser = document.getElementsByName("checkeduser");
	
	var checkboxchecked = false;
	 
	for ( var i=0; i< checkeduser.length; i++){
		var checkbox = document.getElementById("checkeduser"+i);
		if( checkbox.checked ){
			checkboxchecked = true;
			break;
		}
	}
	
	if( !checkboxchecked ){
		errMessage(document.getElementById("checkeduser0"), "Select User");
		return false;	
	}
	return true;
}
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
#textcolor
{
color: maroon;
font-size: small;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<s:if test="forgotuserreq!=null">
	<%-- <s:if test="institutionlist!=null">
	<s:form action="forgotpasswordUserManagementAction" name="forgotpwdprocess" autocomplete = "off"  namespace="/">
	
			<table border='0' cellpadding='0' cellspacing='10' height="224px">
				<tr>
					<td align="right">Inst ID :&nbsp;&nbsp;</td>
					<td align="left">
						<select name="instids" id="instids">
							<option value="-1">---- Select ----</option>
							<s:iterator value="institutionlist">
								<option value="${INST_ID}">${INST_NAME}</option>
							</s:iterator>
						</select>	
					</td>
				</tr>
				<tr>
					<td align="right">User Type :&nbsp;&nbsp;</td>
					<td align="left">
						<input type="radio" name="ustype" id="ustype" value="all" checked/> All <br/><br/><input type="radio" name="ustype" id="ustype" value="inst"/>&nbsp;Institution Admin <br/><br/><input type="radio" name="ustype" id="ustype" value="normal"/>&nbsp;Normal User 
					</td>
				</tr>
				<tr>
					<td></td>
					<td align="center">
							<input type='submit' name='goback' value='Submit' onclick="return valuecheck();"/>
							<input type='button' name='goback' value='Back' onclick="goBack()"/>
						</td>
				</tr>
			</table>
	</s:form>
	</s:if> --%>
  <s:form action="resetForgotpwdUserManagementAction"  id="resetpwd" name="resetpwd" autocomplete = "off"   onsubmit="return selectChechBox()"   namespace="/">
 
		<table  id="example" border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable viewtable" ><!--  class="pretty" -->
		 
		<tr>
			<th>
			</th>
			<th> Sl no </th>
			<th>
				Institution
			</th>
						
			<th>
				User Name
			</th>
			<th>
				User type
			</th>
			
			<th>
				First Name 
			</th>
			<th>
				Last Name 
			</th>
			<th>
				Email Id 
			</th>
			
			<th>
				Last Login
			</th>
			<th>
				Request Processed By
			</th>
			<th>
				Request Processed Date
			</th>
			<th>
				Status
			</th>
		</tr>
	   
	   
		<% int rowcnt = 0; Boolean alt=true; %> 
		<s:iterator value="forgotuserreq">
			<% int i=0; %>
			<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt;%>>
				<td><input type="radio" name="checkeduser"  id="checkeduser<%=i%>"  value="${USERID}-${INSTID}"/></td>
				<td> <%= rowcount %> </td>			
				<td>${INSTID}</td>	
				<td>${USERNAME}</td>
				<td>${USERTYPE}</td>
				<td>${FIRSTNAME}</td>
				<td>${LASTNAME}</td>
				<td>${EMAILID}</td>
				<td>${LASTLOGIN}</td>
				<td>${PASSWDRESET_BY}</td>
				<td>${PASSWDRESET_DATE}</td>
				<td>${FORGOTPASSWORDFLAG}</td>
			</tr>
			<% i++; %>
		</s:iterator>
	 
		</table>
		<table>
	 			<tr> 	
	 					<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" /> 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/> 
						</td>
					</tr>
	 	</table>
 
	</s:form>
</s:if>
<s:else>
<table>
	<tr><td><font color="red">No Request found for Reset Forgot Password</font></td></tr>
</table>
</s:else>


