<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script type="text/javascript"
	src="jsp/personalize/script/PersonalCardOrder.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<style>
</style>
<div align="center">
	<s:form action="personalCardRegisterBulkCustomerRegister"
		name="cardgen" autocomplete="off" namespace="/">

		<table width="90%" cellpadding="0" border="0" cellspacing="0"
			class="formtable">
			<tr>
				<th>Sl no</th>
				<th><input type='checkbox' onclick="checkedAll(this.form)"
					id="checkall" /></th>
				<th style='text-align: center;'>BatchID</th>
				<th style='text-align: center;'>Customer ID</th>
				<th style='text-align: center;'>Account Number</th>
				<th style='text-align: center;'>Embosing Name</th>
				<th style='text-align: center;'>Product Name</th>
				<th style='text-align: center;'>Date Of birth</th>
				<th style='text-align: center;'>Account Type</th>
				<th style='text-align: center;'>REG_STATUS</th>
				<!-- <th style='text-align:center;'> Status </th> -->
			</tr>
			<% int rowcnt = 0; Boolean alt=true; %>
			<s:iterator value="customerDataList">
				<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
					<td><%= rowcount %></td>
					<td><s:checkbox name="customerId"
							id="customerId<%=rowcount%>" fieldValue="%{CUSTOMER_ID}" /></td>
					<td>${BATCH_ID}</td>
					<td>${CUSTOMER_ID}</td>
					<td>${P_ACCOUNT_NO}</td>
					<td>${NAME}</td>
					<td>${PRODUCT_TYPE}</td>
					<td>${DOB}</td>
					<td>${ ACCOUNT_TYPE }</td>
					<td>${ REG_STATUS }</td>
				<%-- 	<td>${ USERNAME }</td> --%>
					<%-- <td> ${ STATUS }      </td> --%>
				</tr>
			<input type="hidden" name="acctType" id="acctType"	value="${ACCOUNT_TYPE}">
				  <input type="hidden" name="productCode" id="productCode"	value="${PRODUCTCODE}">  
				 <input type="hidden" name="accountNo" id="accountNo" value="${P_ACCOUNT_NO}"> 
			</s:iterator>
			<tr>
				<td colspan="8"><s:submit value="Submit" name="authorize"
						id="authorize" onclick="return selectallvalidation();" /> <input
					type="button" name="cancel" id="cancel" class="cancelbtn"
					value="Cancel" onclick="return confirmCancel()" /></td>
			</tr>
		</table>

	</s:form>
</div>
