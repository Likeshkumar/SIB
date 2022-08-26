<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>

<script>
function putName ( subaccountid )
{
	if( cardtypeid != -1 ){
		var url = "fchCardtypeDescCardTypeAction.do?subaccounttype="+subaccountid;
		var result = AjaxReturnValue(url);
		document.getElementById("subaccounttypedesc").value = result;
	}else{
		document.getElementById("subaccounttypedesc").value = '';
	}
}

function getSubAccountTypes( instid ) { 	
	var url = "listOfsubaccounttypeAccountSubTypeAction.do?instid="+instid; 
	var result = AjaxReturnValue(url); 
	document.getElementById("subaccounttype").innerHTML=result; 
}
	 
</script>

<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="accountsubtypeform" action="editAccountSubTypeDetailsAccountSubTypeAction.do" autocomplete="off">
<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="center">
		
		
	<%-- 	<tr>
			<td>INSTITUTION</td>
			<td><s:select label="-Select-" 
				headerKey="-1" headerValue="--- Select ---"
				list="instlist" 
				listValue="INST_NAME"  onchange= 'getSubAccountTypes(this.value)'
    			listKey="INST_ID" 
   				name="instid" id="instid" /></td>
		</tr> --%>
		
		<tr>
			<td>Edit Sub Account Type</td>
			<td><s:select label="-Select-" 
				headerKey="-1" headerValue="--- Select ---"
				list="acctsubtypelist" 
				listValue="ACCTSUBTYPEDESC"
    			listKey="ACCTSUBTYPEID" 
   				name="accountsubtype" id="accountsubtype" /></td>
		</tr>	 
 		 
		
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
			<td colspan="3">
				<s:submit value="Edit" name="submit" id="submit" />
			</td>			
		</tr>
</table>

</div>
</s:form>

 
</body>