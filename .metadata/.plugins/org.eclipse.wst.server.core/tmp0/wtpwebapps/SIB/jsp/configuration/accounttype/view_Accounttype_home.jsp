<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>

<script>
function putName ( accttypeid )
{
	if( accttypeid != -1 ){
		var url = "fchCardtypeDescAccountTypeAction.do?accounttype="+accttypeid;
		var result = AjaxReturnValue(url);
		document.getElementById("accounttypedesc").value = result;
	}else{
		document.getElementById("accounttypedesc").value = '';
	}
}

function getAccountTypes( instid ) {
 
	var url = "listOfAccountTypeAccountTypeAction.do?instid="+instid; 
	var result = AjaxReturnValue(url); 
	document.getElementById("accounttype").innerHTML=result; 
}
	 
</script>

<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="accounttypeform" action="viewAccounttypeDetailsAccountTypeAction.do" autocomplete="off">
<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="center">
		
		
		  <tr>
			<td>INSTITUTION</td>
			<td><s:select label="-Select-" 
				headerKey="-1" headerValue="--- Select ---"
				list="instlist" 
				listValue="INST_NAME"  onchange= 'getAccountTypes(this.value)'
    			listKey="INST_ID" 
   				name="instid" id="instid" /></td>
		</tr>
		
		<tr>
 			<td>ACCOUNT TYPE
 			</td> 
			<td><select name="accounttype" id="accounttype"></select></td> 
			 
		</tr> 
 		 
		
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
			<td colspan="3">
				<s:submit value="View" name="submit" id="submit" />
			</td>			
		</tr>
</table>

</div>
</s:form>

 
</body>
