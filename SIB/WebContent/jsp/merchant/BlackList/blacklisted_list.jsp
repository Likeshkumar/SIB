<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<jsp:include page="/displayresult.jsp"></jsp:include>
<script>
	function showBlackListedDetails( blid ){ 
		var url = "viewBlackListedDetailsMerchantRegister.do?blid="+blid; 
		window.location = url;
	}
	
	function checkBox(){
		alert( "checkbox");
		return false;
	}
</script>
<div align="center">
<s:form action="authorizeBlackListedMerchantRegister.do"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				  	<tr> <td colspan="7" style="text-align:center"> Click on the row to view the details of black listed </td> </tr>
					 <tr>
						<th> SL No </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Merchant Id  </th>
						<th>Merchant Name </th>
						<th> First Name</th>
						<th> Second Name  </th>
						<th> Phone Number </th>
						 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %>  
				<s:iterator value="mregbean.blacklist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name="merchantid"  id="orderrefnum<%=rowcount%>"  value="${MERCH_ID}" />  </td>
						<td   onclick="showBlackListedDetails( ${BLACKLISTED_CODE} )" > ${MERCH_ID}  </td>
						<td  onclick="showBlackListedDetails( ${BLACKLISTED_CODE} )" > ${MERCH_NAME}  </td>
						<td  onclick="showBlackListedDetails( ${BLACKLISTED_CODE} )" > ${FIRST_NAME} </td>
						<td  onclick="showBlackListedDetails( ${BLACKLISTED_CODE} )"  > ${LAST_NAME}   </td>
						<td> ${ PRIM_MOBILE_NO }  </td> 
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Approve" name="submit" id="approve" onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Delete" name="delete" id="delete" class="cancelbtn"  />
							 
				 
						</td>
					</tr>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 