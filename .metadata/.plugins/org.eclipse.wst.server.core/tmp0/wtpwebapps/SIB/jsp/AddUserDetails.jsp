<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script> 


</head>
<script type="text/javascript">

var httpObject = null;
function getHTTPObjectForBrowser(){
if (window.ActiveXObject)
      return new ActiveXObject("Microsoft.XMLHTTP");
else if (window.XMLHttpRequest) return new XMLHttpRequest();
else {
     alert("Browser does not support AJAX...........");
    return null;
   }
  }


function setAjaxOutputReload(){
   if(httpObject.readyState == 4){
   	
   	 // alert(httpObject.responseText);
      
       document.getElementById('ipaddress').innerHTML = httpObject.responseText;
       
       document.getElementById('loginretrycount').innerHTML = httpObject.responseText;
      
   }
}

function reloadCount(){
	httpObject = getHTTPObjectForBrowser();
	//alert(httpObject);
	
	if (httpObject != null) {
	 	 
	 	 var profile=( document.ProductAddFofm.profile.value );
	 	 //alert(profile);
	 	 httpObject.open("GET", "callUserEnableUserManagementAction.do?profile="+profile, true);
	     
	     httpObject.send(null);

	 httpObject.onreadystatechange = setAjaxOutputReload;
	// alert("dssd");    
	 }
	
	
	
	 
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="ProductAddFofm" action="SaveUserDetUserManagementAction" autocomplete="off">
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
<tr>
		<td>Profile<Span><font class="mand">*</font></Span></td>
		<td><select name="profile" id="profile" onchange="reloadCount();">
			<option value="0">Select</option>
			
             <s:iterator  value="profilename"  >
			     <option value="${PROFILE_ID}">${PROFILE_NAME}</option>
			 </s:iterator>
          
			</select>
		</td>
</tr>
<tr>
	<td>User Name<Span ><font class="mand">*</font></Span></td>
	<td><input type="text" name="username" id="username" onchange="doAjaxCall();" autofocus="autofocus" maxlength="16"></td>
</tr>
<input type="hidden" name="psw" id="psw" value="Test@1234"  maxlength="25" >
<input type="hidden" name="cpsw" id="cpsw" value="Test@1234" maxlength="25" >
<!-- <tr>
	<td>Password<Span><font class="mand">*</font></Span></td>
	<td><input type="password" name="psw" id="psw" maxlength="25" ></td>
</tr>
<tr>
	<td>Confirm Password<Span><font class="mand">*</font></Span></td>
	<td><input type="password" name="cpsw" id="cpsw" maxlength="25" ></td>
</tr> -->
<tr>
	<td>First Name<Span><font class="mand">*</font></Span></td>
	<td><input type="text" name="fname" id="fname" maxlength="25"  ></td>
</tr>
<tr>
	<td>Last Name<Span><font class="mand">*</font></Span></td>
	<td><input type="text" name="lname" id="lname" maxlength="25"  ></td>
</tr>
<tr>
	<td>Email<Span><font class="mand">*</font></Span></td>
	<td><input type="text" name="email" id="email" maxlength="40"  ></td>
</tr>
<tr>

<td style='width:176px'>Password Repeat Days  </td>
<td><input type='text' name='passwordrpt' id='passwordrpt'  ></td>
</tr>

<tr>
	<td>Status<Span><font class="mand">*</font></Span></td>
	<td>
	<Select name="status" id="status">
		<option value="-1">--Select--</option>
		<option value="1">Active</option>
		<option value="0">In Active</option>
	</Select>
	</td>
</tr>



<tr>
	<td colspan="2">
		<div id="ipaddress" align="left" >
		</div>
	</td>
</tr>
	<tr>
		<td></td>
		<td><input type="submit" name="submit" value="Submit" onclick="return addUserDetails();"></td>
	</tr>
</table>
</s:form>

