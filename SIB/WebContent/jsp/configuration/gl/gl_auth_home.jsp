<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.ResourceBundle"%>
<script type="text/javascript" src="js/script.js"></script> 
<style>
table.resulttable td{
	border:1px solid gray;
}
</style>
 <script>
 	function showGLDet( glcodeval ){
 		 var url = "showGLDetGLConfigure.do?glcode="+glcodeval; 		  
 		 var res = AjaxReturnValue(url);
 		 
 		 document.getElementById("result").innerHTML = res;
 	}
 	
  	function selectGlGrp(){
  		var glgrpcode = document.getElementById("glgrpcode"); 
  		
 		if( glgrpcode.value == "-1"){
 			errMessage(glgrpcode, 'Please select GL');
 			return false;
 		}
 		errMessage(glgrpcode, '');
 		return true;
  	}
  	
 	function deAuthReason() { 
 			if( !selectGlGrp() ){ 
 				return false;
 			} 
 			
 			var glgrpcode = document.getElementById("glgrpcode");
 			glgrpcodeval = glgrpcode.value; 
	 		  
 		 	var reason = prompt('Enter the Reason for Reject');
			 if( reason ){
				 var url = "authGlGLConfigure.do?glgrpcode="+glgrpcodeval+"&deauthreason="+reason;
				 window.location = url; 
			 }  
			 return false;
 	}
  
 </script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="authGlGLConfigure.do" id="glauthform" method="post" onsubmit="return selectGlGrp()" autocomplete="off">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef;text-align:center"  border="0" cellpadding="0" align="center" width="70%" cellspacing="0"  >
	  
	  
	<s:set name="noglgrp" value="noglgroup"  />
	 
	 <s:if test="%{#noglgrp!='NOGLGROUOP'}"> 
	 
	 	<tr>
			  <td> &nbsp; </td>
			 <td>GL  : <select name="glgrpcode" id="glgrpcode" onchange="showGLDet(this.value)">
			 		<option value="-1" > -SELECT- </option>
			 		<s:iterator  value="glgroup">
 				<option value="${GL_CODE}" >${GL_NAME}</option>
 				</s:iterator>
			 <select/> </td>
		</tr> 
	
	 
	 <tr>
	 	<td style="text-align:center;" colspan="2">
	 		<div id="result"> 
	 		</div>
	 	</td>
	 </tr>
	 
	 <tr>
			<td colspan="2" style="text-align:center">
				<input type="submit" name="sunmit" value="Authorize" />
				<input type="submit" name="sunmit" value="DeAuthorize" onclick="return deAuthReason()" />
				<input type="button"  onclick="return confirmCancel()" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  />
			</td>
		</tr>
	 			
	  </s:if>
	 <s:else>
	 	<tr><td style="text-align:center;color:red;padding-top:10px"> NO GL  FOR AUTHORIZE / DE AUTHORIZE </td></tr>
	 
	 </s:else>
	 
	</table>


</form>
 