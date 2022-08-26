<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"></script>
<div align="center">
	
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="60%" cellspacing="0"  >
			<s:iterator value="viewgl">
			    	 <tr>
				 		 <td>Group Code</td><td class="textcolor">${GROUP_CODE}</td> 
				 	 </tr>
				 	 <tr>
					 	<td>Group Desc</td><td class="textcolor">${GROUP_NAME}</td>  
					 </tr>
					 <tr>
				 	 	<td>Master Group</td><td class="textcolor">${GROUP_PARENTID}</td>  
				 	 </tr>
					  <tr> 
					 	 <td></td><td class="textcolor"></td>
					  </tr>
			</s:iterator>
		</table>
		<br>
		<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
			<tr align="center">
				<td>
					<s:form action="editglGLConfigure.do" method="post" autocomplete="off">
						<s:hidden name="glgrpcode" id="glgrpcode" value="%{glgrp_code}"></s:hidden>
						<s:submit name="submit" value="Edit" onclick="return check_form();" ></s:submit>
					</s:form>
				</td>
				<td><s:submit value="Back" name="submit" id="submit" onclick="goBack()"/></td>
			</tr>
		</table>
</div>