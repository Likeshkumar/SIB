<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.ResourceBundle"%>


<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script>  
<jsp:include page="/displayresult.jsp"></jsp:include>
<%
String acttype = (String)session.getAttribute("act_type");
String instid = (String)session.getAttribute("Instname");
String branch = (String)session.getAttribute("BRANCHCODE");
String usertype = (String)session.getAttribute("USERTYPE");
String branchname = (String)session.getAttribute("BRANCH_DESC");
String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ") ==null?"":(String)session.getAttribute("DATEFILTER_REQ");
String btnval=null;
if( acttype.equals("insert")){
	btnval = "Save";
}else if( acttype.equals("edit")){
	btnval = "Update";
}
%>
<form action="saveGlGroupGLConfigure.do" method="post" autocomplete="off">
	<input type="hidden" name="regtype" id="regtype" />
	<table class="formtable" cellpadding="0" align="center" width="30%" cellspacing="0"  >
		<% if( acttype.equals("insert")){ %> 
		<tr>
			 <td>Group Code   </td>
			 <td> : <s:textfield name="glgroupcode" id="glgroupcode"  value="%{glbeans.glgrpcode}"  readonly="%{glbeans.flag}"  /></td>
		</tr>
		<%}else{%>		
		
		<tr>
			 <td> Group Code  </td>
			 <td> : <s:textfield name="glgroupcode" id="glgroupcode"  value="%{glbeans.grpcode}"  readonly="%{glbeans.flag}"  /></td>
		</tr>
		<%} %>
		
		<tr>
			  <td>Group Desc</td>
			 <td> : <s:textfield name="glgroupddesc" id="glgroupddesc" value="%{glbeans.grpdesc}"/> </td>
		</tr>
		
	<s:set name="noglgrp" value="noglgroup"  /> 
	 	
			 	  <tr>
						 <td>Master Group</td>
						 <td> :  
						  <s:if test="%{glbeans.glgroup not empty }"> 
							<s:select name="masterglcode" id="masterglcode" list="glbeans.glgroup" listKey="GROUP_CODE" 
							listValue="GROUP_NAME" headerKey="P" headerValue="PRIMARY" value="%{glbeans.mastrecde}" ></s:select>	 
						</s:if>		
						 <s:else> 
						 	<select><option value"PRIMARY">PRIMARY</option> </select>
						 </s:else>
						   
						  </td>
				</tr>    
	 
	<tr>
	 		<td colspan="2">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
							<td><input type="submit"  name="order" id="order" value="Save"  onclick="return glvalidation()"/></td> 
							<td><input type="button" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  onclick="return confirmCancel()"/></td>
						</tr>
					</table>
	 		</td>
	</tr>
	</table>


</form>
 