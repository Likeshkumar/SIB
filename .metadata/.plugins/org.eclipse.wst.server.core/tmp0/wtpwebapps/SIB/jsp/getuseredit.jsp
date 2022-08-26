<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
<script>
function validate_form(){
	     
	     var branch = document.getElementById("branch");
	    // alert("dsad"+branch);
		 var fname = document.orderform.firstname.value;
		 var lname = document.orderform.lastname.value;
		 var emailval = document.orderform.email.value;
		 var userstatus = document.orderform.userstatus;
		 var ipaddress = document.orderform.ipadres;
		 var userexpate = document.orderform.userexpate;
		 var passwrdrepeatcount = document.orderform.passwrdrepeatcount;
         var passwrdexpirydate = document.orderform.passwrdexpirydate;
         
        
       
        	
		
		 var filter = /^(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)$/;

		   if(branch.value == "ALL")
			{
				errMessage(branch,"Select the Branch");
			 	return false;
			}  
		 if(fname == ""){			 
			 errMessage(firstname,"Please Enter First Name");
			 return false;	
		}	
		if(lname == ""){			 	 
			 errMessage(lastname,"Please Enter First Name");
			 return false;
		}	
		if(emailval == ""){
			 errMessage(email,"Please Enter email id");			
			 return false;
		}else{
			if( !emailvalidator( emailval ) ){
			 errMessage(email,"InValid E-Mail Address");
			 return false;
			}
		}
		if(userstatus){
			 if(userstatus.value == "-1")
				{
					errMessage(userstatus," Select the Status  ");
				 	return false;
				}
		 }
		
		 if(ipaddress)
		 {
			 var ipaddressvalue = document.orderform.ipadres.value.substr(0,1);
			if(ipaddress.value == "")
			{
				errMessage(ipadres,"Select IP Address");
			 	return false;
			}else if(ipaddress.value == "0")
			{
				errMessage(ipadres,"IP Address cannot start with zero");
			 	return false;
			}	
			else if (!filter.test(ipaddress.value)){
	    		errMessage(ipadres," IP ADDRESS IS INVALID " );
		   		return false;
	    	 }
		 }
		 if(userexpate)
		 {
			if(userexpate.value == "")
			{
				errMessage(userexpate,"Select User Expiry Date ");
			 	return false;
			}	 
		 }
/* 		 if(loginretrycount)
		 {
			if(loginretrycount.value == ""){
				errMessage(loginretrycount," Enter Login Retry Count ");
			 	return false;
			}else if ( loginretrycountvalue == "0"){	
				errMessage(loginretrycount," Login Retry Count cannot start with zero ");
				return false;
		   	} 	 
		 } */
		 if(passwrdrepeatcount)
		 {
			var passwordrepeatcountvalue = document.orderform.passwrdrepeatcount.value.substr(0,1);
			if(passwrdrepeatcount.value == "")
			{
				errMessage(passwrdrepeatcount,"Enter Password Repeat count  ");
			 	return false;
			}else if ( passwordrepeatcountvalue == "0"){	
				errMessage(passwrdrepeatcount," Password Repeat count cannot start with zero ");
				return false;
		   	} 	 
		 }
		 if(passwrdexpirydate){
				if(passwrdexpirydate.value == "")
				{
					errMessage(passwrdexpirydate," Select Password expiry date  ");
				 	return false;
				}	 
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
function checkipformat()
{
	var ip=document.orderform.ipadres.value;

	var filter = /^([0-9a-f]{2}([:-]|$)){6}$|([0-9a-f]{4}([.]|$)){3}$/i;;
		//
	
	
	if (!filter.test(ip))
		{	
			errMessage(ipadres,"Please enter a valid IP Address");
	   	 	document.orderform.ipadres.focus();
	   		return true;
		}
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
String usertype = (String)session.getAttribute("USERTYPE");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="editUserdetailsupdateUserManagementAction" name="orderform" autocomplete="off">
<s:hidden name="instid" id="instid" value="%{institutionid}"></s:hidden>
				<div align="center" >
					<table border='0' cellpadding='0' cellspacing='0' width='50%' class="formtable">
					<s:iterator  value="edituserdetail" var="status">
					<input type="hidden" name="userid" id="userid" value="${USERID}">
					<tr>
						<td  align="left" ><b>Profile</b></td>
						<td><select name="profile" id="profile" onchange="reloadCount();">
							<!-- <option value="0">Select</option> -->
							
				             <s:iterator  value="profilename" var="stat" >
				            
							     <option value="${PROFILE_ID}"
							     <s:if test="#status.PROFILE_ID==#stat.PROFILE_ID" >
								selected
								</s:if>
								> 
							   ${PROFILE_NAME}</option>
							 </s:iterator>
				          
							</select>
						</td>
					</tr>
					<%-- <tr>
						<td  align="left" ><b>Branch</b></td>
						<td><select name="branch" id="branch">
							<option value="">Select</option>
							
				             <s:iterator  value="branchlist" var="stat" >
				            
							     <option value="${BRANCH_CODE}"
							     <s:if test="#status.BRANCH_CODE==#stat.BRANCH_CODE" >
								selected
								</s:if>
								> 
							   ${BRANCH_NAME}</option>
							 </s:iterator>
				          
							</select>
						</td>
					</tr>
					 --%>
					 
				<% 	if(!usertype.equals("SUPERADMIN"))
				{
			%>
		 <td class="fnt">Branch</td>
						
						<td>
			 				<select name="branch" id="branch">
				 				<option value="ALL">Select</option>
				 				<s:iterator  value="branchlist">
				 					<option value="${BRANCH_CODE}">${BRANCH_NAME}</option>
				 				</s:iterator>
			 				</select>
						</td>
			
			<%	
				}
				else
				{
			%>
		
			<%		
				}
			%>
    
				
					
						<tr>  	
							<td  align="left" ><b>User Name</b></td>
							<td align="left"><input type="text" name="username" id="username" readonly="true" value="${USERNAME}" maxlength="16"></td> 
						</tr>
						<tr> 
							<td align="left"><b>First Name</b><b class="mand">*</b> </td>
							<td align="left"> <input type="text" name="firstname" id="firstname"  value="${FIRSTNAME}" maxlength="25">
							<s:fielderror fieldName="firstname" cssClass="errmsg" />
							</td>
						</tr>
						<tr>
							<td align="left"> <b>Last Name</b><b class="mand">*</b></td>
							<td align="left"><input type="text" name="lastname" id="lastname" value="${LASTNAME}" maxlength="25"> 
							<s:fielderror fieldName="lastname" cssClass="errmsg" />
							</td>
						</tr>
						<tr>
							<td align="left"> <b>Email Id</b><b class="mand">*</b></td>
							<td align="left"> <input type="text" name="email" id="email" value="${EMAILID}" maxlength="30">
							<s:fielderror fieldName="email" cssClass="errmsg" />
							 </td>
						</tr>
						<tr>
							<td align="left"> <b>User Status</b><b class="mand">*</b></td>
							<td align="left"><s:select name="userstatus" id="userstatus"  list="#{'1':'Active','0':'InActive'}" value="%{USERSTATUS}"></s:select></td>
						</tr>
						<s:if test="%{LOGINIPADDRESSREQUIRED == 1}">
							<tr>
								<td align="left"> <b>IP Address</b><b class="mand">*</b></td>
								<td align="left"> 
									<input type="text" name="ipadres" id="ipadres" value="${IPADDRESS}" onkeypress='return numberOnlyWithdot(event)' onchange='checkipformat(this);' maxlength='15'>
								</td>
							</tr>
						</s:if>
						<s:else></s:else>
						<s:if test="%{LOGINEXPIRYDATEREQUIRED == 1}">
							<tr>
								<td align="left"> <b>User Expiry Date</b><b class="mand">*</b></td>
								<td align="left"> 
									<input type="text" name="userexpate" id="userexpate" value="${EXPIRYDATE}" readonly="readonly" onchange="return yearvalidation(this.id);" style="width:182px">
									<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.userexpate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
								</td>
							</tr>
						</s:if>
						<s:else></s:else>
						<s:if test="%{LOGINRETRYCOUNTREQUIRED == 1}">
							<tr>
								<td align="left"> <b>Login Retry Count</b><b class="mand">*</b></td>
								<td align="left"> 
									<input type="text" name="logincount" id="logincount" value="${RETRYCOUNT}" onKeyPress='return numerals(event);' maxlength='2'>
								</td>
							</tr>
						</s:if>
						<s:else></s:else>
						<s:if test="%{USERPASSWORDREPEATABLE == 1}">
							<tr>
								<td align="left"> <b>Password Repeat Days Count</b><b class="mand">*</b></td>
								<td align="left"> 
									<input type="text" name="passwrdrepeatcount" id="passwrdrepeatcount" value="${PSWREPEATCOUNT}" onKeyPress='return numerals(event);' maxlength='2'>
								</td>
							</tr>
						</s:if>
						<s:else></s:else>
						<s:if test="%{USERPASSWORDEXPIRYCHECK == 1}">
							<tr>
								<td align="left"><b>Password Expiry Date</b><b class="mand">*</b></td>
								<td align="left"> 
									<input type="text" name="passwrdexpirydate" id="passwrdexpirydate" value="${PASSWORDEXPIRYDATE}" readonly="readonly" onchange="return yearvalidation(this.id);" style="width:182px">
									<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.passwrdexpirydate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
								</td>
							</tr>
						</s:if>
						<s:else>
						</s:else>
						</s:iterator>				
					</table>
					<table>
						<tr>
							<!-- <td><input type="submit" name="submit" value="UPDATE" onclick="return validate_form();"></td> -->
							<td><input type="submit" name="submit" value="UPDATE" ></td>
						</tr>		
					</table>
			</div>
</s:form>
