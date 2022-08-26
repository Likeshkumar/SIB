 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<script>
	function processSettlement(){ 
		parent.showprocessing();
	}
	function getSetRefNoTxn( setrefno ){
		 if( confirm( "Do yow want to view the list of transaction for " + setrefno ) ){
		 
			 var url = "transactionViewSettlementProcess.do?setrefno="+setrefno; 
				newwindow = window.open(url,'','left=350,top=150,width=800,height=400,location=no,menubar=no,menu=no,status=no,scrollbars=yes,resizable=yes,toolbar=no,titlebar=0,fullscreen = yes,directories=no');
				 
				newwindow.focus(); 
		 }else{
			 return false;
		 }
		
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
						 <th> Merchant Code  </th>
						<th> Merchant Name  </th> 
						<th> Txn Amount </th> 
						<th> Commission Amount </th>
						<th> Discount Amount  </th>
						<!-- <th> Sum of Amount  </th> -->
						<th> Commission Name </th> 
						<th> Discount Name </th>
						<th> Reference Number </th>
						<th> Generated User </th>
						<th> Date </th>
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="setbean.cbssettlemerchantlist">
					<tr onclick="return getSetRefNoTxn( '${SETTLE_REFNO}' )" > 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name="merchantid"  id="orderrefnum<%=rowcount%>"  value="${MERCHANT_ID}"/>  </td>
						<td> ${MERCHANT_ID}  </td>
						<td> ${MERCHANTNAME}  </td>
						<td><b> ${TXN_AMOUNT} </b> </td>
						<td><b> ${COMMISSIONDATA} </b>  </td> 
						<td><b> ${DISCOUNT_AMOUNT}  </b> </td> 
						 <%-- <td> <b>${TXNSUMAMT}   </b></td> --%>
						 <td> ${COMMISSIONDESC}  </td>
						 <td> ${DISCOUNTDESC}  </td>
						 <td> ${SETTLE_REFNO}  </td>
						 <td> ${USERNAME}  </td>
						  <td> ${PROCESSED_DATE}  </td>
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
 