<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 
<s:form action="updateSchemeglGLConfigure.do" method="post" autocomplete="off">
<s:iterator value="schemedetailslist">
		<s:set name="schemedeatilsglcode">${GL_CODE}</s:set>
		<s:set name="schemedeatilsappaction">${APP_ACTION}</s:set>
		
		<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
	
			<tr> 
				 <td>Scheme Code</td>
				 <td class="textcolor"> :<s:textfield name="schemecode" id="schemecode"  value="%{SCH_CODE}" readonly="true"></s:textfield> 
				 						<s:hidden name="schemecode" id="schemecode"  value="%{SCH_CODE}"></s:hidden>
				 </td>
			 
				  <td>Scheme Desc</td>
				 <td class="textcolor"> :<s:textfield name="schemedescription" id="schemedescription"  value="%{SCH_NAME}"></s:textfield>  </td>
			</tr>
			 
			<tr> 
				 <td>Scheme Short name</td>
				 <td class="textcolor">  : <s:textfield name="schemeshortname" id="schemeshortname"  value="%{SCH_SHORTNAME}"></s:textfield> </td>
			 
				  <td>Parent Gl</td>
				 <td class="textcolor">  : 
				 <select name="parntglcode" id="parntglcode" onchange="enableFeeAct(this.value)">
			 		<option value="-1" > -SELECT- </option>
			 		<s:iterator  value="glbeans.glgroup">
			 		<s:set name="glgroupglcode">${GL_CODE}</s:set>
 						
 					<option value='${GL_CODE}'
					 				<s:if test="%{#schemedeatilsglcode==#glgroupglcode}">
					 				 selected="${GL_CODE}"
					 				</s:if>
		 				>${GL_NAME}
		 		   </option>
 						
 						
 						
 					</s:iterator>
			 </select>
				 </td>
			</tr>
		 
		 
		 <tr> 
				 <td>Status</td>
				 <td class="textcolor"> :  
				 <s:select list="#{'1':'ACTIVE','0':'IN-ACTIVE'}" name="schemestatus" id="schemestatus" headerKey="-1" headerValue="-SELECT-" value="%{SCH_STATUS}"></s:select>
				 </td>
			 
				 <td>Map Action   </td>
				 <td class="textcolor"> : 
					 <select name="applicableaction" id="applicableaction">
				 		<option value="-1" > -SELECT- </option>
				 		<s:iterator  value="glbeans.actionlist">
				 			<s:set name="actionlistacctioncode">${ACTION_CODE}</s:set>
							<option value='${ACTION_CODE}'
						 				<s:if test="%{#schemedeatilsappaction==#actionlistacctioncode}">
						 				 selected="${ACTION_CODE}"
						 				</s:if>
			 				>${ACTION_DESC}</option>	
	 					</s:iterator>
				 	</select>  
			    </td>
				 
			</tr>
			
		</table>
			<br>
			<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
				<tr align="center">
					<td>
						<s:submit name="submit" value="Update" onclick="return glvalidation();" ></s:submit>	
					</td>
					<td><input type="button" value="Back" name="submit" id="submit" onclick="goBack()"/></td>
				</tr>
			</table>
	</s:iterator>
</s:form>