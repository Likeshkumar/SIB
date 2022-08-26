<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<script type="text/javascript" src="js/script.js"></script> 
<jsp:include page="/displayresult.jsp"></jsp:include>
	<s:iterator value="schemedetailslist">
		<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
	
			<tr> 
				 <td>Scheme Code</td>
				 <td class="textcolor"> : ${SCH_CODE} </td>
			 
				  <td>Scheme Desc</td>
				 <td class="textcolor"> : ${SCH_NAME} </td>
			</tr>
			 
			<tr> 
				 <td>Scheme Short name</td>
				 <td class="textcolor">  : ${SCH_SHORTNAME} </td>
			 
				  <td>Parent Gl</td>
				 <td class="textcolor">  : ${GL_NAME} </td>
			</tr>
		 
		 
		 <tr> 
				 <td>Status</td>
				 <td class="textcolor"> : ${SCHSTATUS}
				 </td>
			 
				 <td>Map Action   </td>
				 <td class="textcolor"> : ${APPACTION}  </td>
				 
			</tr>
			
		</table>
			<br>
			<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
				<tr align="center">
					<td>
						<s:form action="editschemeGLConfigure.do" method="post" autocomplete="off">
						<s:hidden name="schemecode" id="schemecode" value="%{SCH_CODE}"></s:hidden>
							<s:submit name="submit" value="Edit" onclick="return check_form();" ></s:submit>
						</s:form>
					</td>
					<td><s:submit value="Back" name="submit" id="submit" onclick="goBack()"/></td>
				</tr>
			</table>
	</s:iterator>

