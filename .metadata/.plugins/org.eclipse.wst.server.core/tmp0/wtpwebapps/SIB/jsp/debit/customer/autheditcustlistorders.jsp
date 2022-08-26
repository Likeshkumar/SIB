<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/personalize/script/PersonalCardOrder.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<script>
function deAuthorize(){
	if( !selectallvalidationFromIndex() ){
		return false;
	}
	
	var reason = prompt("Reason For Reject");
	if( reason == null ){
		return false;
	}
	document.getElementById("reason").value = reason;
	return true;
}

</script>

<div align="center">
	<s:form action="authsavecustdetailsCustomerDetailsAction"
		name="authrizeorder" autocomplete="off" namespace="/">
		<s:hidden name="reason" id="reason" />
		<table width="90%" cellpadding="0" border="0" cellspacing="0"
			class="formtable">
			<tr>
				<th>Sl no</th>
				<th><input type='checkbox' onclick="checkedAll(this.form)"
					id="checkall" /></th>
				<th>Card Number</th>
				<th>Account No</th>
				<th>Order Ref No</th>
				<th>Name</th>
				<th>Product Name</th>
				<th>Mobile No</th>
				<th>User Name</th>
				<th>Modified Date</th>

			</tr>
			<% int rowcnt = 0; Boolean alt=true; %>
			<s:iterator value="authcardorder" status="incr">
				<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
					<td><%= rowcount %></td>
					<td><s:checkbox name="personalrefnum"
							id="personalrefnum%{#incr.index}" fieldValue="%{ORG_CHN}" /></td>
					<td>${MCARD_NO}</td>
					<td>${ACCT_NO}</td>
					<td>${ORDER_REF_NO}</td>
					<td>${EMB_NAME}</td>
					<td>${PNAME}</td>
					<td>${MOBILE}</td>
					<td>${USERNAME}</td>
					<td>${MAKER_DATE }</td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="8"><s:submit value="authorize" name="authorize"
						id="authorize" onclick="return selectallvalidationFromIndex()" />

					<input type="button" name="cancel" id="cancel" class="cancelbtn"
					value="Cancel" onclick="return confirmCancel()" /> <s:submit
						value="Reject" name="deauthorize" id="deauthorize"
						onclick="return deAuthorize();" /></td>
			</tr>
		</table>

	</s:form>
</div>
