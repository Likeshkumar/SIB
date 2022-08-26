 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
 
 
 
<s:form action="authWalletMerchActionVasWallet"  name="orderform" onsubmit="return validateinstorder()"  autocomplete = "off"  namespace="/">
   
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				  
					<thead>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th> 
						<th> Mercahnt id </th>
						<th> Bank Code </th>
						<th> Branch Code </th>
						<th>Account Number</th>
						<th> Deposited Amount </th>
						<th> Deposited date </th>
						<th> Uploaded by </th>
						<th> Upload date </th> 
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="walletbean.walletauthmerchlist">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name=recordlist  id="orderrefnum<%=rowcount%>"  value="${RECORD_ID}"/>  </td>
						<td> ${ MERCHANTID }</td>  
						<td> ${BANK_CODE}  </td>
						<td> ${BRANCH_CODE}  </td>
						<td> ${ACCTNUMBER}  </td>
						<td> ${DEPOSIT_AMT}  </td>
						<td> ${DEPOSITED_DATE} </td>
						<td> ${UPLOADED_BY}   </td>
						<td> ${ UPLOADED_DATE }  </td> 
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	 	<table>
	 			<tr> 	
	 					<td colspan="8">
							<s:submit value="Authorize" name="authorize" id="authorize" onclick="parent.showprocessing()"/> 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/> 
							<s:submit value="Reject" name="reject" id="reject" onclick="parent.showprocessing()" /> 
						</td>
					</tr>
	 	</table>
</div>
</s:form>
 