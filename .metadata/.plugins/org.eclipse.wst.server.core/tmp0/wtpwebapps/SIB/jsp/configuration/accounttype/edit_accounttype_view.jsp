<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
		<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
	
</head>

<script>
function validateAccountType(){
	
var accounttypeidlen = document.getElementById("accounttypelenval").value;
	
	//alert(accounttypeidlen);
	var accounttypeid = document.getElementById("accounttypeid");
	var accounttypedesc = document.getElementById("accounttypename");
	
	//alert(accounttypeid.value.length + accounttypeidlen);
	
	if( accounttypeid.value == "" ){
		errMessage(accounttypeid, "Enter Accont type value ");
		return false;
	}else{
		if( accounttypeid.value.length != accounttypeidlen ){
			errMessage(accounttypeid, "Account type value length should be "+accounttypeidlen+" digit");
			return false;
		}
	}
	
	if( accounttypedesc.value == "" ){
		errMessage(accounttypedesc, "Enter Account type name ");
		return false;
	}
	
	
	return true;
}

function putName ( cardtypeid )
{
	
	
	
	if( cardtypeid != -1 ){
		var url = "fchCardtypeDescCardTypeAction.do?cardtypeid="+cardtypeid;
		var result = AjaxReturnValue(url);
		document.getElementById("cardtypedesc").value = result;
	}else{
		document.getElementById("cardtypedesc").value = '';
	}
}

function chkChars(field,id,enteredchar)
{
	//alert('1:'+field+'2:'+id+'3:'+enteredchar);
	charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
    	//alert(document.getElementById(id).value.charAt(i));   
    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

function validateNumber (field,id,enteredchar)
{
	charvalue= "0123456789";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
    	
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
       	//alert(document.getElementById(id).value.charAt(i));
       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

	 
</script>
<%String act = (String)session.getAttribute("act");%>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form name="accounttypeform" action="editsaveAccountTypeAccountTypeAction.do" autocomplete="off" onsubmit="return validateAccountType()" > --%>
<s:form name="accounttypeform" action="editsaveAccountTypeAccountTypeAction.do" autocomplete="off"  >
<input type="hidden" name="act"  id="act" value="<%=act%>"> 
  
<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="center">
		<s:hidden name="accounttypelenval" id="accounttypelenval" value="%{accounttypelenval}"/>
		<s:iterator value="accounttypelist">
		<%-- <tr>
			<td>INSTITUTION</td>
			<td><s:select label="-Select-" 
				headerKey="-1" headerValue="--- Select ---"
				list="instlist" 
				listValue="INST_NAME"
    			listKey="INST_ID" 
   				name="instid" id="instid" /></td>
		</tr>
		 --%>
		<tr>
 			<td>ACCOUNT TYPE ID
 			</td> 
			<%-- <td><s:textfield name="accounttypeid" id="accounttypeid" readonly="true" value="%{ACCTTYPEID}" maxlength="%{accounttypelenval}" onkeyup="validateNumber('ACCOUNT TYPE ID',this.id,this.value)" ></s:textfield></td> --%> 
			<td><s:textfield name="accounttypeid" id="accounttypeid" readonly="true" value="%{ACCTTYPEID}" maxlength="%{accounttypelenval}"  ></s:textfield>
			<s:fielderror fieldName="accounttypeid" cssClass="errmsg" />
			</td>
			 
		</tr> 
		   
		<tr>
 			<td>ACCOUNT TYPE NAME
 			</td> 
			<%-- <td><s:textfield name="accounttypename" id="accounttypename" value="%{ACCTTYPEDESC}"  maxlength="25" onkeyup="chkChars('ACCOUNT TYPE NAME',this.id,this.value)" ></s:textfield></td> --%> 
			<td><s:textfield name="accounttypename" id="accounttypename" value="%{ACCTTYPEDESC}"  maxlength="25" onkeypress="return alphanumerals(event);" ></s:textfield>
			<s:fielderror fieldName="accounttypename" cssClass="errmsg" />
			</td>
			 
		</tr> 
 		</s:iterator> 
		
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Update" name="submit" id="submit" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>

</div>
</s:form>

 
</body>
