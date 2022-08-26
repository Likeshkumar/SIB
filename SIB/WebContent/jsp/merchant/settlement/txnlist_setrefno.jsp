 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<script>
	function processSettlement(){ 
		parent.showprocessing();
	}
	function close_window() {
		  if (confirm("Close Window?")) {
		    close();
		  }
		}
</script>
 
<s:form action="settleMerchantActionSettlementProcess.do"  name="orderform" onsubmit="return processSettlement()" autocomplete = "off"  namespace="/">
   
<div id="fw_container">
<table><tr><td colspan="10" style="text-align:center"> <input type="button" value="Close" class="cancelbtn" onclick="close_window();return false;" /></td></tr></table>	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				 
					
					<thead>
					<tr><td colspan="10"> <input type="button" value="Close" onclick="close_window();return false;" /></td></tr>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th> 
						 <th> Card Number </th>
						<th> Terminl Id  </th> 
						<th> Batch </th> 
						<th> Txn Ref no </th>
						<th> Txn Amount </th>
						<th> Tran Type  </th>
						<th> Txn Currency </th>
						<th> Txn Conversion Rate </th> 
						 
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="setbean.txnlistbysetrefno">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name="merchantid"  id="orderrefnum<%=rowcount%>"  value="${REFNUM}"/>  </td>
						<td> ${CHN}  </td>
						<td> ${TERMID}  </td>
						<td>  ${BATCHID}   </td>
						<td>  ${REFNUM}    </td> 
						<td style="text-align:right"><b> ${TXNAMOUNT}  </b> </td> 
						 <td> ${TXNTYPE} </td>
						 <td> ${TXNCURRENCY} </td>
						 <td> ${TXNCONVRATE}  </td>
						 
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	  
</div>
</s:form>
 