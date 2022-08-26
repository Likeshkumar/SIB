<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<script type="text/javascript" src="js/script.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:iterator value="glcard">
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
	
		<tr>
			<td>FEE</td>
			<td class="textcolor"> :
				${GL_COM_ID}
			</td>	   
		</tr>
		<tr> 
			 <td>GL Code</td>
			 <td class="textcolor"> :
			 	${GL_CODE}
			 </td>
		 
			  <td>GL Desc</td>
			 <td class="textcolor"> :
			 	${GL_NAME}
			 </td>
		</tr>
		 
		 <tr>
			  <td>GL Short Name</td>
			 <td class="textcolor"> :
				 ${GL_SHORT_NAME}
			 </td>
			 
			  <td>Balance Type</td>
			 <td class="textcolor"> :
			 	${GLBALTYPE}
			</td> 
		</tr>
		
		 <tr>
			   
			  <td>ALIE</td>
			 <td class="textcolor"> :  
				${GLALIE}
			</td> 
			
			 <td>Statement Type</td>
			 <td class="textcolor"> :
					${GLSTATEMENTTYPE}
			</td> 
			
		</tr>
		
		<tr>
		 	  <td> Entry Allowed </td>
			 <td class="textcolor"> : 
			 	${GLENTRYALLOWED}
			 </td>
			  
			  
			   <td>Position</td>
			 <td class="textcolor"> : 
					${GLPOSITION}
			</td> 
		</tr>
	   
	 
	 	<tr>
			  <td>Currency</td>
			 <td class="textcolor"> :
			 		 ${CUR_DESC}		
			</td> 
			 
			 
			 <td>GL Group</td>
			 <td class="textcolor"> : 	
 				${GROUP_NAME}
			 </td>
			 
		</tr>
		
		
	 
	 	<tr>
	 		<td colspan="4">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
						<td>
						<s:form action="editgrpglGLConfigure.do" autocomplete="off">
							<s:submit value="Edit" name="order" id="order" onclick="return glvalidation()" />
							<s:hidden name="glcode" id="glcode" value="%{GL_CODE}"></s:hidden>
						</s:form> 
						<td>
							<input type="button"  onclick="goBack()" name="cancel" id="cancel" value="Back"   class="cancelbtn"  />
							 
						</td>
						</tr>
					</table>
	 		</td>
	 	</tr>
	</table>
</s:iterator>
