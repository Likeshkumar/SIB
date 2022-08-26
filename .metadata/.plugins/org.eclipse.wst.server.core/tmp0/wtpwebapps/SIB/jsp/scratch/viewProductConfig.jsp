<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<style type="text/css">

</style>

<script>
function authorize(productcode,flag,buttonstatus){
	var reason=true;
	if(buttonstatus=='deauth'){
		reason = prompt('Enter the Reason for Reject');
	}if( reason ){
		//alert("reason-- "+reason);
		document.authDeauthProductConfigForm.action="authProductProductConfiguration.do?productcode="+productcode+"&act="+flag+"&buttonstatus="+buttonstatus+"&reason="+reason;
		document.authDeauthProductConfigForm.submit();
	}
	return true;
}

</script>
<s:form name="authDeauthProductConfigForm" method="post" autocomplete="off">
<div align="center">
	<jsp:include page="/displayresult.jsp"></jsp:include>
	<table align="center" border="0"  cellspacing="1" cellpadding="2" style="width:80%" class="formtable">
			<thead>
			<tr>
				<th nowrap>Product Name</th>
				<th nowrap>Product Description</th>
				<th nowrap>Current Status </th>
				<th nowrap>Added By </th>
				<th nowrap>Added Date </th>
				<th nowrap>Auth By </th>
				<th nowrap>Auth Date </th>
				<th nowrap>Remarks </th>
				<s:if test='%{scratchcardbeans.flag == "C"}'>
					<th nowrap>Authorize</th>
					<th nowrap>Reject</th>
				</s:if>
			</tr>
			</thead>
			<tbody>
				
					<s:iterator  value="scratchcardbeans.productList">
					<tr>
						<td >${SCHPROD_NAME}</td>
						<td>${SCHPROD_DESC}</td>
						<td nowrap>
							<s:if test="%{AUTH_STATUS==1}">Authorized </s:if>
							<s:if test="%{AUTH_STATUS==0}">Waiting for authorization</s:if>
							<s:if test="%{AUTH_STATUS==9}">De - Authorized</s:if>
						</td>
						<td>${ADDED_BY}</td>
						<td>${ADDED_DATE}</td>
						<td>${AUTH_BY}</td>
						<td>${AUTH_DATE}</td>
						<td>${REASON}</td>
						<s:if test='%{scratchcardbeans.flag == "C"}'>
							<s:if test="%{AUTH_STATUS==0}">
								<%-- <input type="button" value="authorize" onclick="authorize('${SCHPROD_CODE}','${scratchcardbeans.flag}','auth')"/>
								<input type="button" value="De -auth" onclick="authorize('${SCHPROD_CODE}','${scratchcardbeans.flag}','deauth')"/> --%>
								<td><input type="image"  src="images/enable.gif" alt="submit Button" onclick="authorize('${SCHPROD_CODE}','${scratchcardbeans.flag}','auth')"></td>
								<td><input type="image"  src="images/disabled.gif" alt="submit Button" onclick="authorize('${SCHPROD_CODE}','${scratchcardbeans.flag}','deauth')"></td>
							</s:if>
						</s:if>
						<s:else></s:else>
						</tr>
					</s:iterator>
				
				<s:if test ="%{scratchcardbeans.productList.isEmpty()}"><tr><td colspan=9 > No Data Found </td></tr></s:if>
			</tbody>
	</table>
</div>
</s:form>
