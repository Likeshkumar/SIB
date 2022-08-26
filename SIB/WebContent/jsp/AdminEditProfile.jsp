<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
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
   	
   	   //alert(httpObject.responseText);
      
       document.getElementById('subproduct').innerHTML = httpObject.responseText;
           
      
   }
}

function reloadCount(){
	httpObject = getHTTPObjectForBrowser();
	//alert(httpObject);
	//alert("edit profile");
	if (httpObject != null) {
	 	 
	 	 var instname=( document.ProductAddFofm.instname.value );
	 	// alert(instname);
	 	 httpObject.open("GET", "editProfileViewUserManagementAction.do?instname="+instname+"&doaction=edit", true);
	     
	     httpObject.send(null);

	 httpObject.onreadystatechange = setAjaxOutputReload;
	// alert("dssd");    
	 }	 
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
<s:form name="ProductAddFofm" action="saveEditUserManagementAction" autocomplete="off">
<table border='0' cellpadding='0' cellspacing='6' width='40%' class="formtable">

<s:if test="%{#display !=''}">
	<font color="red"><s:property value="display"></s:property></font>
</s:if>
	<tr align="left">
			<td> Institution : </td>
			<td>			
	 				<select name="instname" id="instname" onchange="reloadCount()">
	 				<option value="-1">--Select Institution--</option>
	 				<s:iterator  value="institutionlist">
	 				<option value="${INST_ID}">${INST_NAME}</option>
	 				</s:iterator>
	 				</select>
			</td>
	</tr>
<tr align="left">
<td>Select Profile : </td>
		<td>
			<s:div id="subproduct" name="subproduct">
			<select  name="subproduct" id="subproduct">
				<option value='00'>--Select--</option>
			</select>
			</s:div>
		</td>
</tr>
</table>
<table>
	<tr><td><s:submit value="Next" name="submit" onclick="return editProfileDetails()" /></td></tr>
</table>
</s:form>
<s:bean name="com.ifp.Action.userManagementAction" var="resd" >
	<s:param name="display"></s:param>
</s:bean>
</div>

