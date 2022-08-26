<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script> 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
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


function setAjaxOutputReload()
{
   if(httpObject.readyState == 4)
   {
		document.getElementById('ipaddress1').innerHTML = httpObject.responseText;
		document.getElementById('loginretrycount').innerHTML = httpObject.responseText;
   }
}

function reloadCount()
{
	httpObject = getHTTPObjectForBrowser();
	//alert(httpObject);
	
	if (httpObject != null) {
	 	 
	 	 var profile=( document.AddAdminUserForm.profile.value );
	 	 var instid=( document.AddAdminUserForm.instid.value );
	 	 //alert(profile);
	 	 httpObject.open("GET", "callUserEnableUserManagementAction.do?profile="+profile+"&instid="+instid, true);
	     
	     httpObject.send(null);

	 httpObject.onreadystatechange = setAjaxOutputReload;
	// alert("dssd");    
	 }
}

function getprofileid(){
	  	var instid=( document.AddAdminUserForm.instid.value );
		var url = "callProfileListUserManagementAction.do?instid="+instid;
		//alert(url);
 		result = AjaxReturnValue(url);
 		document.getElementById("profile").innerHTML = result;   		
}

function AddAdminUserForm_validation()
{
	// alert("sss");
	 var profile = document.AddAdminUserForm.profile;
	 var usernamel = document.AddAdminUserForm.username;
	 var cbsusername = document.AddAdminUserForm.cbsusername;
	 var password = document.AddAdminUserForm.psw;
	 var cpassword = document.AddAdminUserForm.cpsw;
	 var fname = document.AddAdminUserForm.fname;
	 var lname = document.AddAdminUserForm.lname;
	 var email = document.AddAdminUserForm.email;
	 var status = document.AddAdminUserForm.status;
	 var branch = document.AddAdminUserForm.loginbrchreq;
	 var ipaddress = document.AddAdminUserForm.ipaddress;	
	 var userexpirydate = document.AddAdminUserForm.loginexpreq;
	 var passwordrepeatcount = document.AddAdminUserForm.passwordrpt;
	 var passwordexpiry = document.AddAdminUserForm.pswexpchck;
	 var instid = document.AddAdminUserForm.instid;
	 var re=  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{6,}$/;   
	 var pswval = document.getElementById("psw").value;
	 var validPassword = re.test(pswval);
	 //alert("validConfirmpassword--->  "+validConfirmpassword);
	 if(instid){
		 if(instid.value == "-1")
		 {
			 errMessage(instid,"Please Select Institution ID");			
			 return false;
		 }	
	 }
	 if(profile){
	 if(profile.value == "00")
	 {
		 errMessage(profile,"Please Select Profile Name");
		 document.getElementById("profile").focus();
		 return false;
	 }	
	}
	 if((usernamel.value == "Enter User Name")||(usernamel.value == ""))
	 {
		 errMessage(usernamel,"Please Enter User Name");
		 document.getElementById("username").focus();
	     return false;
	 }	
	 if(cbsusername.value == "")
	 {
		 errMessage(cbsusername,"Please Enter CBS User Name");
		 document.getElementById("cbsusername").focus();
	     return false;
	 }	
	 if(password.value == "")
	 {
		 errMessage(password,"Please Enter Password");
		 document.getElementById("psw").focus();
		 return false;
	 }	
	 if( validPassword==false)
	 {
		alert("Invalid Password");
		errMessage(psw,"Password Should contain Upper case,Lower case,Special character,Numeric and minimum length 6 digits " );
		return false;
	 }
	 if(cpassword.value == "")
	 {
		 errMessage(cpassword,"Please Enter Confirm Password");
		 document.getElementById("cpsw").focus();
		 return false;
	 }	
	 if(password.value!=cpassword.value)
	 {
		 errMessage(cpsw,"Password and Confirm Password Not Matched");
		 document.getElementById("cpsw").focus();
		 return false;
	 }
	 if(fname.value == "")
	 {
		 errMessage(fname,"Please Enter First Name");
		 cpassword.focus;
		 document.getElementById("fname").focus();
		 return false;
	 }	
	 if(lname.value == "")
	 {
		 errMessage(lname,"Please Enter Last Name");
		 cpassword.focus;
		 document.getElementById("lname").focus();
		 return false;
	 }	
	 if(email.value == "")
	 {
		 errMessage(email,"Please Enter email id");
		 document.getElementById("email").focus();
		 return false;
	 }
	 else
	  {
		
			 if( !emailvalidator( email.value ) )
			 {
				 errMessage(email,"InValid E-Mail Address" );
			 	return false;
			 }
	
	  }
 	 if(status){
		 if(status.value == "-1")
			{
				errMessage(status,"Select the Status  ");
			 	return false;
			}	  
		 
	 }
	if(branch){
			if(branch.value == "00")
			{
				errMessage(branch,"Select Branch");
			 	return false;
			}	 
	 }
	 if(ipaddress)
	 {
		var ipaddressvalue = document.AddAdminUserForm.ipaddress.value.substr(0,1);
		if(ipaddress.value == "")
		{
			errMessage(ipaddress,"Select IP Address");
		 	return false;
		}else if(ipaddressvalue == "0")
		{
			errMessage(ipaddress,"IP Address cannot start with zero");
		 	return false;
		}	
	 }
	 if(userexpirydate)
	 {
		if(userexpirydate.value == "")
		{
			errMessage(userexpirydate,"Select User Expiry Date ");
		 	return false;
		}	 
	 }
	 if(passwordrepeatcount)
	 {
		var passwordrepeatcountvalue = document.AddAdminUserForm.passwordrpt.value.substr(0,1);
		if(passwordrepeatcount.value == ""){
			errMessage(passwordrepeatcount," Enter Password Repeat Days Count ");
		 	return false;
		}else if ( passwordrepeatcountvalue == "0"){	
			errMessage(passwordrepeatcount," Password Repeat Days Count cannot start with zero ");
			return false;
	   	} 	 
	 }
	 if(passwordexpiry)
	 {
		if(passwordexpiry.value == "")
		{
			errMessage(passwordexpiry,"Select the Password Expiry Date  ");
		 	return false;
		}	 
	 } 
	return true;
}


function checkipformat()
{
	var ip=document.AddAdminUserForm.ipaddress.value;
	var filter = /^(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)$/;

	if (!filter.test(ip))
		{	
			errMessage(ipaddress,"Please enter a valid IP Address");
	   	 	document.AddAdminUserForm.ipaddress.focus();
	   		return true;
		}
}

function numberOnlyWithdot(evt){
	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
	//alert(keyvalue);
		if((!(keyvalue<48 || keyvalue>57)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13)|| (keyvalue== 46))
		{
			return true;
		}
		else
		{ 
			return false;
		} 
	}


function checkUserAvailable( username ){
	var instid = document.getElementById("instid");
	var username = document.getElementById("username");
	if( instid.value == -1 ){
		errMessage(instid, "Select institution");
		return false;
	}
	
	if( username.value == "" ){
		errMessage(username, "Enter User Name...");
		document.getElementById("submit").disabled=true;
		return false;
	}else{
		document.getElementById("submit").disabled=false;
	}
	
	var url = "checkUserNameExistUserManagementAction.do?instid="+instid.value+"&username="+username.value;
	//alert(url);
	var result = AjaxReturnValue(url);
	var resp = result.split("~");
	errMessage(username, resp[0]);
	if( resp[1] == "1" ){
		document.getElementById("submit").disabled=true;
	}else{
		document.getElementById("submit").disabled=false;
	}
	
	return false;
}


</script>
</head>

<s:if test="%{profile_exist=='N'}">
<div align='center'><font color='red'>Profile Not Exist ...</font><a href='addProfileUserManagementAction.do'><font color='blue'>Please Add Profile</font></a></div>
</s:if>
<s:else>




<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="SaveUserDetUserManagementAction" name="AddAdminUserForm" autocomplete="off" >
<div align="center">
	<table border="0" cellpadding="0" cellspacing="6" width="50%" class="formtable">
	<% String username = (String)session.getAttribute("SS_USERNAME");
	String usertype = (String)session.getAttribute("USERTYPE");
	if(usertype.equals("SUPERADMIN")){
	%>
		<tr>
			<td width="50%">
				Institution<Span><font class="mand">*</font></Span>
			</td>
			<td>
				<select name="instid" id="instid" onchange="getprofileid()">
					<option value="-1">Select</option>
					 <s:iterator  value="allinst"  >
					     <option value="${INST_ID}">${INST_NAME}</option>
					 </s:iterator>
				</select>
			</td>
		</tr>
		<tr>
			<td width="50%">
				Profile<Span><font class="mand">*</font></Span>
			</td>
			<td><select name="profile" id="profile" onchange="reloadCount();"><option value="00">Select</option></select>
			</td>
		</tr>  
		
	<%}else{%>	
		<tr>
			<td width="50%">
				<input type="hidden" name="instid" id="instid" value="<%=(String)session.getAttribute("Instname")%>" />
				Profile<Span><font class="mand">*</font></Span>
			</td>
				<td>
					<select name="profile" id="profile" onchange="reloadCount();" >
						<option value="00">Select</option>
						 <s:iterator  value="profilename"  >
						     <option value="${PROFILE_ID}">${PROFILE_NAME}</option>
						 </s:iterator>
					</select>
				</td>
		</tr>
		<%}%>
		<tr>
			<td>
				User Name<Span ><font class="mand">*</font></Span>
			</td>
			<td>
				<input type="text" name="username" id="username" maxlength="16" onBlur="bluruser(this.value)" onchange="checkUserAvailable(this.value)">
			</td>
		</tr>
		
		<tr>
			<td>
				CBS User Name<Span ><font class="mand">*</font></Span>
			</td>
			<td>
				<input type="text" name="cbsusername" id="cbsusername" maxlength="12">
			</td>
		</tr>
		
		<input type="hidden" name="psw" id="psw" value="Test@1234"  maxlength="25" >
		<input type="hidden" name="cpsw" id="cpsw" value="Test@1234" maxlength="25" >

		<!-- <tr>
			<td>Password<Span><font class="mand">*</font></Span></td>
			<td><s:password name="psw" id="psw" onchange="password_validation();" tooltip="hello" tooltipIconPath="/images/cal.gif" tooltipCssClass="xhtml"  /></td> 
			 <td>
			 <input type="password" name="psw" id="psw" maxlength="25"  >	
			 </td>
		</tr>
		<tr>
			<td>Confirm Password<Span><font class="mand">*</font></Span></td>
			<td><input type="password" name="cpsw" id="cpsw" onpaste="return false;" maxlength="25" ></td>
		</tr> -->
		<tr>
			<td>First Name<Span><font class="mand">*</font></Span></td>
			<td><input type="text" name="fname" id="fname"  maxlength="25" ></td>
		</tr>
		<tr>
			<td>Last Name<Span><font class="mand">*</font></Span></td>
			<td><input type="text" name="lname" id="lname"  maxlength="25"  ></td>
		</tr>
		<tr>
			<td>Email Id<Span><font class="mand">*</font></Span></td>
			<td><input type="text" name="email" id="email" maxlength="40" ></td>
		</tr>
		<tr>
			<td>Status<Span><font class="mand">*</font></Span></td>
			<td>
				<Select name="status" id="status">
					<option value="-1">Select</option>
					<option value="1">Active</option>
					<option value="0">In Active</option>
				</Select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="ipaddress1" align="left">
				</div>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td></td>
			<td><input type="submit" name="submit"  id="submit" value="Submit" onclick="return AddAdminUserForm_validation();"></td>
		</tr>
	</table>
</div>
</s:form>
</s:else>
