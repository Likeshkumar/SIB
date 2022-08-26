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
%>
<form action="saveGlGroupGLConfigure.do" method="post" autocomplete="off">
<input type="hidden" name="regtype" id="regtype" />
<s:iterator value="viewgl">	
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" align="center" width="30%" cellspacing="0"  > 
		<tr>
			 <td>Group Code   </td>
			 <td> : <s:textfield name="glgroupcode" id="glgroupcode"  value="%{GROUP_CODE}"  readonly="true"  />
			 		<s:hidden name="glgroupcode" id="glgroupcode"  value="%{GROUP_CODE}"/>
			 </td>
		</tr>
		<tr>
			  <td>Group Desc</td>
			 <td> : <s:textfield name="glgroupddesc" id="glgroupddesc" value="%{GROUP_NAME}"/> </td>
		</tr>
	    <tr>
			 <td>Master Group</td>
			 <td> :  
					<s:select name="masterglcode" id="masterglcode" list="%{glbeans.glgroup}" listKey="GROUP_CODE" listValue="GROUP_NAME" headerKey="P" headerValue="PRIMARY" value="%{GROUP_PARENTID}" ></s:select>			 	
			  </td>
		</tr> 
	 
	<tr>
	 		<td colspan="2">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
							<td><input type="submit"  name="order" id="order" value="UPDATE"  onclick="return glvalidation()"/></td> 
							<td><input type="button" name="cancel" id="cancel" value="BACK"   class="cancelbtn"  onclick="return goBack();"/></td>
						</tr>
					</table>
	 		</td>
	</tr>
	</table>
 </s:iterator>
</form>
 