 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<script>
	function processSettlement(){ 
		parent.showprocessing();
	}
</script>
 
<s:form action="settleMerchantActionSettlementProcess.do"  name="orderform" onsubmit="return processSettlement()" autocomplete = "off"  namespace="/">
   
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				 
					<input type="hidden" name="bin" value="<%= request.getParameter("cardtype") %>" />
					<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
					<thead>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th> 
						<th> Merchant Id </th>
						<th> Merchant Name  </th>
						<th> Name </th> 
						<th> Account Type  </th>
						<th> Account Number </th> 
						<th> MCC </th>
						<th> Transaction Count </th>
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
					 
				<s:iterator value="setbean.settlemerchants">
					<tr> 
						<%  int rowcount = ++rowcnt; %> 
							<td> <%= rowcount %> </td> 
						<s:iterator value="%{ATTR}"> 
							<td> <input type="checkbox" name="merchantid"  id="orderrefnum<%=rowcount%>"  value="${MERCH_ID}"/>  </td>
							<td> ${MERCH_ID}  </td>
							<td> ${MERCH_NAME}  </td>
							<td> ${FIRST_NAME}  ${LAST_NAME}  </td>
							<td> ${ACCTTYPE_DESC}  </td> 
							<td> ${SETTLEMENT_ACCTNO}  </td> 
							<td> ${MCC_DESC}  </td>
							 <td> ${TXNCOUNT}  </td>
						 </s:iterator>
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	 	<table>
	 			<tr> 	
	 					<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidation();"/> 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/> 
						</td>
					</tr>
	 	</table>
</div>
</s:form>
 