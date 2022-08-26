 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<script>
function loaddenomvalue(){
	var productcode = document.getElementById("productcode").value;
	var denomvalue = document.getElementById("denomvalue").value;
	var batchid = document.getElementById("batchid").value;
	if( productcode != -1 ){
		var url = "authDeauthreceiveBatchFileDownload.do?productcode="+productcode+"&denomvalue="+denomvalue+"&batchid="+batchid;
		var result = AjaxReturnValue(url);		
		document.getElementById("denomvalue").innerHTML = result;
	}else{
		errMessage(productcode,"Select Product");
	}
	return false;
}

function validation_authReceiveBatchvalue(){ 
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason == null ){	  
		 return false;
	 }  
	 
	 document.getElementById("reason").value = reason;
	 return true;
}
</script>

<s:if test='scratchbeans.flag== "C"'>
 	<form action="authorizeCheckerbatchBatchBatchIssuanceProcess.do" autocomplete="off" name="denomconfig" >
 </s:if>
 <s:else> 
 	<form action="issueCardActionBatchIssuanceProcess.do" autocomplete="off" name="denomconfig" >
 </s:else>
 <input type="hidden" name="act" id="act" value="${scratchbeans.flag}">  
	<div id="fw_container">	
	<s:hidden name="reason" id="reason" value="" />
			<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
						<thead>
						<tr>
							<th> Sl no </th>
							<th> Batch Id </th>	
							<th>No of cards</th>					
							<th> Product Name </th>
							<th> Denom Value </th>
							<th> Auth Status </th>
							<th> Received Date </th>
							<th> Received by </th>						
						</tr>
						</thead>
						<% int rowcnt = 0; Boolean alt=true; %> 
						<s:iterator value="scratchbeans.scratchlist">
						 <input type="hidden" id="productcode" name="productcode" value="${SCHPROD_CODE}" />
						 <input type="hidden" id="denomvalue" name="denomvalue" value="${DENOM_VALUE}" />
						 <input type="hidden" id="batchid" name="batchid" value="${BATCH_ID}" />
						 <input type="hidden" id="noofcards" name="noofcards" value="${CARDCOUNT}"/>
						<tr
						<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
						>
							<td> <%= rowcount %> </td>
							<td> ${BATCH_ID}  </td>
							<td> ${CARDCOUNT}  </td>
							<td> ${SCHPROD_NAME} </td>						
							<td> ${DENOM_VALUE}   </td>
							<td> ${AUTH_STATUS}  </td>
							<td> ${RECEIVED_DATE}</td> 
						    <td> ${RECEIVED_BY}</td>
						</tr> 
				 	</s:iterator>
		 	</table>		 	
		 	<table>
 				<tr> 	
 					<td colspan="8">
 					<s:if test='scratchbeans.flag== "C"'>
						<input type="submit" name="auth" id="auth1" value="Authorize"/>
						<input type="submit" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authReceiveBatchvalue()"/>
					</s:if><s:else>						
						<s:submit value="Submit" name="submit" id="submit"  />
						<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
						</s:else>
					</td>
				</tr>
		 	</table>
	</div>
</form>