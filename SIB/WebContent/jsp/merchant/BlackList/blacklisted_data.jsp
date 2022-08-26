<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
 
<jsp:include page="/displayresult.jsp"></jsp:include>
<script>
	function validateinstorder(){
	   parent.showprocessing();
		 
	}
</script>
<div align="center">
<s:form action="blackListedListMerchantRegister.do"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				  	 
				<% int rowcnt = 0; Boolean alt=true; %>  
				<s:iterator value="mregbean.blacklistedata">
					<tr >  
						<td> First Name </td> <td> : ${FIRSTNAME}  </td> <td> Last Name </td> <td> : ${SECONDNAME}  </td> 
					</tr>  
					<tr >  
						<td> List type </td> <td> : ${UNLIST_TYPE}  </td> <td> Nationality</td> <td> : ${NATIONALITY}  </td> 
					</tr> 
					
					<tr >  
						<td> Date of Birth </td> <td> : ${DATE_OF_BIRTH}  </td> <td> Birth Country</td> <td> : ${BIRTH_COUNTRY}  </td> 
					</tr> 
					
					<tr >  
						<td> Comments </td> <td colspan="3"> : ${COMMENTS}  </td> 
					</tr>
					
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							 
						 
							<input type="submit" name="back" id="back" class="cancelbtn" value="Back" onclick="history.go(-1)" />
							
							 
				 
						</td>
					</tr>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 