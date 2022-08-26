<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 
<script>
	function enableFeeAct( glsch ){
		 
		var url = "enableFeeActGLConfigure.do?glscemem="+glsch;
	 
		var result = AjaxReturnValue(url);
		 
		if( result == 1 ){
			document.getElementById("applicableaction").disabled=false;
		}else{
			document.getElementById("applicableaction").disabled=true;
			document.getElementById("applicableaction").selectedIndex = 0;
		}
	}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
 
<form action="saveGlSchemeGLConfigure.do" method="post" autocomplete="off">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
	
		 
		<tr > 
			 <td>Scheme Code</td>
			 <td> : <s:textfield name="schemecode" id="schemecode"  value="%{glbeans.glgrpcode}"  readonly="%{glbeans.flag}"/> </td>
		 
			  <td>Scheme Desc</td>
			 <td> : <s:textfield name="schemedescription" id="schemedesc" /> </td>
		</tr>
		 
		<tr > 
			 <td>Scheme Short name</td>
			 <td> : <s:textfield name="schemeshortname" id="schemeshortname"   /> </td>
		 
			  <td>Parent Gl</td>
			 <td> : <select name="parntglcode" id="parntglcode" onchange="enableFeeAct(this.value)">
			 		<option value="-1" > -SELECT- </option>
			 		<s:iterator  value="glbeans.glgroup">
 						<option value="${GL_CODE}" >${GL_NAME}</option>
 					</s:iterator>
			 </select> </td>
		</tr>
	 
	 
	 <tr > 
			 <td>Status</td>
			 <td> : <select name="schemestatus" id="schemestatus">
			 	<option value="-1">SELECT</option>
			 	<option value="1">ACTIVE</option>
			 	<option value="0">IN-ACTIVE</option>
			 	</select>
			 </td>
		 
			 <td>Map Action   </td>
			 <td> : <select name="applicableaction" id="applicableaction" disabled>
			 		<option value="-1" > -SELECT- </option>
			 		<s:iterator  value="glbeans.actionlist">
 						<option value="${ACTION_CODE}" >${ACTION_DESC}</option>
 					</s:iterator>
			 </select> </td>
			 
		</tr>
	 	<tr>
	 		<td colspan="4">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
						<td>
							<s:submit value="Submit" name="order" id="order"  onclick="return glvalidation()"/> 
						<td>
							<input type="button"  onclick="return confirmCancel()" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  />
							 
						</td>
						</tr>
					</table>
	 		</td>
	 	</tr>
	</table>


</form>
 
 