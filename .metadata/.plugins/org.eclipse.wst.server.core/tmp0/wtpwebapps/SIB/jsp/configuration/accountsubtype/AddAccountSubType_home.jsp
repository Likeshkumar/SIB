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

function validateAccountSubType(){
	
	var accountsubtypelenval = document.getElementById("accountsubtypelenval").value;
		
		//alert(accounttypeidlen);
		var accounttypeid = document.getElementById("accounttypeid");
		var accountsubtypeid = document.getElementById("accountsubtypeid");
		
		
		
		var accountsubtypename = document.getElementById("accountsubtypename");
		
		//alert(accountsubtypeid.value.length + accountsubtypelenval);
		
		if( accountsubtypeid.value == "" ){
			errMessage(accounttypeid, "Select Accont Sub type ID ");
			return false;
		}else{
			if( accountsubtypeid.value.length != accountsubtypelenval ){
				errMessage(accountsubtypeid, "Account Sub type value length should be "+accountsubtypelenval+" digit");
				return false;
			}
		}
		
		if( accountsubtypename.value == "" ){
			errMessage(accountsubtypename, "Enter Account Sub type name ");
			return false;
		}
		
		
		return true;
	}

</script>
<%String act = (String)session.getAttribute("act");%>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form name="accounttypeform" action="saveAccountSubTypeAccountSubTypeAction.do" autocomplete="off" onsubmit="return validateAccountSubType()" > --%>
<s:form name="accounttypeform" action="saveAccountSubTypeAccountSubTypeAction.do" autocomplete="off" >
<input type="hidden" name="act"  id="act" value="<%=act%>"> 
  
<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="center">
		<s:hidden name="accountsubtypelenval" id="accountsubtypelenval" value="%{accountsubtypelenval}"/>
		
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
			<td>
			<select id="accounttypeid" name="accounttypeid" onchange="">
					<s:if test ="%{accounttypelist.isEmpty()}">
					<option value="">No Currency Found</option>
					</s:if>
					<s:else>
								<option value="" >Select AccountType</option>   
								<s:iterator value="accttypelist">  
								<option value="<s:property value="ACCTTYPEID"/>">
								<s:property value="ACCTTYPEDESC"/></option>
								</s:iterator>
					</s:else>  
					</select> 
					<s:fielderror fieldName="accounttypeid" cssClass="errmsg" />
			</td> 
			 
		</tr> 
		
		<tr>
 			<td>ACCOUNT SUB TYPE ID
 			</td> 
			<td><s:textfield name="accountsubtypeid" id="accountsubtypeid" value="%{bean.accountsubtypeid}" maxlength="%{accountsubtypelenval}"
onkeyup="validateNumber('ACCOUNT SUB TYPE ID',this.id,this.value)" ></s:textfield>
<s:fielderror fieldName="accountsubtypeid" cssClass="errmsg" />
</td> 
			 
		</tr> 
		   
		<tr>
 			<td>ACCOUNT SUB TYPE NAME
 			</td> 
			<td><s:textfield name="accountsubtypename" id="accountsubtypename"   value="%{bean.accountsubtypename}"  maxlength="25" >
			</s:textfield>
				<s:fielderror fieldName="accountsubtypename" cssClass="errmsg" />
				</td> 
			 
		</tr> 
 		 
		
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="submit" id="submit" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>

</div>
</s:form>

 
</body>
