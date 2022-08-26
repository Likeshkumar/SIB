<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<script>
	function showProcessing(){
		
		parent.showprocessing();
	}
	
	function validFilter(){
		var usercode = document.getElementById("usercode");
		if( usercode.value == -1 ){
			errMessage(usercode, "Select username....");
			return false;
		}
		return true;
	}
	function getAuthUserList( instid ){
		var url = "viewUserAtuhListUserManagementAction.do?instid="+instid;
		var result = AjaxReturnValue(url);
		document.getElementById("usercode").innerHTML=result;
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ"); 
%>
 
<s:form action="deleteUserAuthHomeUserManagementAction.do"  name="orderform" onsubmit="return showProcessing()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="30%" class="formtable">	 
	
    	 
    <tr>
		<td>
		  Institution
		</td>
		<td> :
		  		
 				<select name="instid" id="instid" onchange="getAuthUserList(this.value)" >
 				<option value="-1">--Select -- </option>
 				<s:iterator  value="institutionlist">
 				<option value="${INST_ID}">${INST_NAME}</option>
 				</s:iterator>
 				</select> 
		</td>
    </tr>
    
    
	 
     	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Next" name="order" id="order" onclick="return validFilter()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 