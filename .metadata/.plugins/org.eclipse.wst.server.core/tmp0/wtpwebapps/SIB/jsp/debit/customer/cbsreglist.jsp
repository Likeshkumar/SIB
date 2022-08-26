<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/ordervalidation.js"></script> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript">
function selectallvalidation1(){
	valid=true;
	var orderref = null;
	var orderreflen = document.getElementsByName("cbsrefnum");
	var oneselected = false;
	for(var i=0;i<orderreflen.length;i++)
	{ 
		orderref = orderreflen[i];
		if(orderref.checked==true)
		{
			oneselected = true;	
			break;

		}
			
	}
	if(!oneselected)
	{
		errMessage(orderreflen[0],"Select any one Order");
		return false;
	}
 }
</script>


<div align="center">
<s:form action="cbsCustomerRegEntryCbsCustomerReg.do"  name="authrizeorder"  autocomplete = "off"  namespace="/">

	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Account No </th>
						<th> Currency </th>
						<th> First Name </th>
						<th> Last Name </th>						
						<th> Mobile No </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cbsreglist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="cbsrefnum"  id="cbsrefnum<%=rowcount%>" fieldValue="%{CUST_AC_NO}~%{CUST_NO}"/>  </td>
						<td> ${CUST_AC_NO}  </td>
						<td> ${CCY}  </td>
						<td> ${FIRST_NAME}  </td>
						<td> ${LAST_NAME}  </td>
						<td> ${ TELEPHONE }  </td>
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Register" name="authorize" id="authorize" onclick="return selectallvalidation1();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
				 
						</td>
					</tr>
	 	</table> 
	
</s:form>
</div>
 