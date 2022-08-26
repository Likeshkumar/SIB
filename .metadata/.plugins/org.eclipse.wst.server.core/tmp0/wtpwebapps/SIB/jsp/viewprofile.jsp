<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
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
	//alert("auth profile");
	if (httpObject != null) {
	 	 var doact = document.getElementById("doact").value;
	 	 var instname=( document.addprofile.instname.value );
	 	// alert(instname);
	 	 httpObject.open("GET", "editProfileViewUserManagementAction.do?instname="+instname+"&doaction="+doact, true);
	     
	     httpObject.send(null);

	 httpObject.onreadystatechange = setAjaxOutputReload;
	// alert("dssd");    
	 }
	 
	
	 
}

function addProfileForm() { 
	
	valid=true;
	var instname = document.getElementById("instname1");  
	var profileid=  document.getElementById("profileid"); 
	if( instname ){
		if( instname.value == "-1"){
			errMessage(instname,"Select institution");
			return false;
		}
	}
	if( profileid ){
		if(profileid.value=="00"){
			errMessage(profileid,"Please Select The Profile");
			return false;
		}	 
	} 
	
	return valid;

}

</script>
<s:form name="addprofile" action="getViewProfileUserManagementAction" autocomplete="off">
<input type="hidden" name="flag" id="flag" value="View">
<s:hidden type="hidden" name="doact" id="doact" value="%{doact}" />
<jsp:include page="/displayresult.jsp"></jsp:include>	
	<div align="center">
		<table border='0' cellpadding='0' cellspacing='0' width='30%' class="formtable" >
		 
		<s:if test="issuperadmin">
			<tr align="center">
					<td>
					Institution  <b class="mand">*</b>:
					</td>
					<td>
		 				<select name="instname" id="instname1" onchange="reloadCount()">
			 					<option value="-1">--Select Institution--</option>
				 			<s:iterator  value="institutionlist">
				 				<option value="${INST_ID}">${INST_NAME}</option>
			 				</s:iterator>
		 				</select>
					</td>
			</tr>	
			
			<tr align="center">
				<td>Select Profile  <b class="mand">*</b>: </td>
				<td>
					<s:div id="subproduct" name="subproduct">
						<select  name="subproduct" id="profileid">
							<option value='00'>--Select--</option></select>
					</s:div>
				</td>
			</tr>	
			
			
		</s:if>
		<s:else>
		<tr align="center">
					<td>
					Institution  <b class="mand">*</b>:
					</td>
					<td> : <s:property value="%{instname}"/> <s:hidden name="instname" id="instname" value="%{instname}" /> </td>
		 				 
			</tr>	
			
			<tr align="center">
				<td>Select Profile  <b class="mand">*</b>: </td>
				<td>
					<s:div id="subproduct" name="subproduct">
				 
						<s:select name="subproduct" id="profileid" list="profiledetail" headerKey="-1" headerValue="-select-" listKey="PROFILE_ID" listValue="PROFILE_NAME" />
					</s:div>
				</td>
			</tr>	
			
		</s:else>
			
		</table>
		<table>
			<tr><td></td><td><s:submit value="Next" name="submit" onclick="return addProfileForm()" /></td></tr>
		</table>
	</div>
</s:form>
