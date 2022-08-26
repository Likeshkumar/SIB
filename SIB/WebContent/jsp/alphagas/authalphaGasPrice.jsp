<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<script type="text/javascript" src="jsp/personalize/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">
function selectallvalidation(){
	var orderref = document.getElementById("productid"); 
	if(!orderref.checked)
	{
		errMessage(orderref," Cannot be empty.Please select the checkbox ");
		return false;
	}
	parent.showprocessing();
	return true;
 }
 
</script>
<style>
.calgn{
	text-align:center !important;
}
</style>

<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
<s:form action="alphaGaspriceauthorizeAlphaGasPriceAction.do"  name="authrizeorder"  autocomplete = "off"  namespace="/">
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable">
					<tr>
						<th class="calgn"> Sl no </th>
						<th class="calgn">  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th class="calgn"> UNIT PRICE </th>
						<th class="calgn"> PRODUCT DESC</th>
						<th class="calgn"> CONFIGURED BY </th>
						<th class="calgn"> CONFIGURED DATE TIME</th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="authList">
				<input type="hidden" name="unitprice" id="unitprice" value="${UNITPRICE}">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td class="calgn"> <%= rowcount %> </td>
						<td class="calgn"> <s:checkbox name="productid"  id="productid" fieldValue="%{PRODUCTID}"/>  </td>
						<td class="calgn"> ${UNITPRICE}  </td>
						<td class="calgn"> ${PRODUCTDESC}  </td>
						<td class="calgn"> ${CONFIGBY} </td>
						<td class="calgn"> ${ CONFIG_DATE }  </td>
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="6">
							<s:submit value="Authorize" name="authorize" id="authorize" onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Reject" name="authorize" id="deauthorize" onclick="return selectallvalidation();"/>				 
						</td>
					</tr>
	 	</table> 	
</s:form>
</div>
 