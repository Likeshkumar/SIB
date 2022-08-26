 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<script>
function selectScratchCard(FLAG)
{
	var productName = document.getElementById("productName").value;
		var url="fetchScratchCardScratchCardProduction.do?act="+FLAG+"&productName="+productName;
		var response=AjaxReturnValue(url);
		document.getElementById('denomValue').innerHTML = response;
		return false;
	
}

function fetchBatchId(FLAG)
{
	//alert(FLAG);
	var productCode = document.getElementById("productName").value;
	var denomValue = document.getElementById("denomValue").value;
		var url="fetchBatchIdScratchCardProduction.do?act="+FLAG+"&denomValue="+denomValue+"&productCode="+productCode;
		var response=AjaxReturnValue(url);
		
		document.getElementById('p_batchId').innerHTML = response;
		return false;
	
}

function authorizeordeothorize(buttonstatus)
{
var batchid = document.getElementById("batchid").value;
var noOfCards = document.getElementById("noOfCards").value;
var flag = document.getElementById("flag").value;

var reason=true;
if(buttonstatus=='deauth')
	{
	reason = prompt('Enter the Reason for Reject?');
	}
	 if( reason ){
 var url = "authorizeScratchCardScratchCardProduction.do?&buttonstatus="+buttonstatus+"&reason="+reason+"&batchid="+batchid+"&noOfCards="+noOfCards+"&act="+flag;
 window.location = url; 
return false;
}
}

</script>
 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<script>

</script>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
 
<div id="fw_container">


<form  action ="viewScratchCardScratchCardProduction.do" method="post" autocomplete="off"> 		<div align="center">
<input type="hidden" value='${scratchcardbeans.flag}' id="act" name="act"/>
			<table align="center" border="0"  cellspacing="0" cellpadding="0" width="50%" class="formtable">
				  	<tr>
				  		<td  align="right">Select Product Name</td>
					  		<td width="50%" align="left">
								<select id="productName" name="productName" onchange = "selectScratchCard('${scratchcardbeans.flag}')">
									<option value="">-Select Product Name-</option>
									<s:iterator  value="scratchcardbeans.productList">
										<option value="${SCHPROD_CODE}">${SCHPROD_NAME}</option>
									</s:iterator>
								</select>
							</td>
					</tr>
					
					
			<tr>
				<td>Denom Value : </td>
				<td>
				<select id="denomValue" name="denomValue" onchange="fetchBatchId('${scratchcardbeans.flag}')">
				</select>
			</tr>
						
			<tr>
				<td>Batch ID : </td>
				<td>
				<select id="p_batchId" name="batchId" >
									
				</select>
			</tr>
			
			</table>
				
			<table><tr><td>
				<input type="submit"  value="view"/></td></tr>
			</table>
</div>			
</form>	


<form  name ="authorizeScratchCardForm"  method="post" autocomplete="off">
<input type="hidden" value='${scratchcardbeans.flag}' id = "flag" name="act"/>
<s:if test ="%{!scratchcardbeans.scratchCard.isEmpty()}">
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
					<thead>
					<tr>
						<th> Sl no </th>
						<th>PRODUCT NAME</th>						
						<th>CARD COUNT</th>
						<th>ADDED BY</th>
						<th>ADDED DATE</th>
						<th>CURRENT STATUS</th>
						<th>FILEGEN BY</th>
						<th>RECEIVED BY</th>
						<th>ISSUED BY</th>
						<th>AUTH BY</th>
						<th>AUTH DATE</th>
						<th>AUTH STATUS</th>
						<th>BATCH ID</th>
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
					
				<s:iterator value="scratchcardbeans.scratchCard">
				  <input type="hidden" name = "batchid" id = "batchid" value="${BATCH_ID}"/>
				  <input type="hidden" name="scratchrefnum"  id="scratchrefnum"  value="${BATCH_ID}"/>
				  <input type="hidden" name = "noOfCards" id = "noOfCards" value="${CARDCOUNT}"/>
				  <tr> 
					
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td>						
						<td>${scratchcardbeans.productName} </td>						
						<td>${CARDCOUNT}</td>
						<td>${ADDED_BY}</td>
						<td>${ADDED_DATE}</td>
						<td>
						<s:if test='%{STATUS_CODE == "01"}'>
						waiting for file download
						</s:if>
						<s:if test='%{STATUS_CODE == "02"}'>
						waiting for recieve
						</s:if>
						<s:if test='%{STATUS_CODE == "03"}'>
						waiting for issue
						</s:if>
						</td>
						<td>${FILEGEN_BY}</td>
						<td>${RECEIVED_BY}</td>
						<td>${ISSUED_BY}</td>
						<td>${AUTH_BY}</td>
						<td>${AUTH_DATE}</td>
						<td>
						<s:if test='%{AUTH_STATUS == "0"}'>
						Waiting for Authorization
						</s:if>
						<s:if test='%{AUTH_STATUS == "1"}'>
						Authorized
						</s:if>
						<s:if test='%{AUTH_STATUS == "9"}'>
						Rejected
						</s:if>
						</td>
						<td>${BATCH_ID}</td>
						 
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	 	
	 	<s:if test='%{scratchcardbeans.flag == "C"}'>
	 	
	 	<table>
	 			<tr> 	
	 					<td colspan="8">
							<input type="button" value="Authorize" name="submit" id="auth" onclick="authorizeordeothorize('auth')"/> 
							<input type="button" value="Reject" name="submit" id="deauth" onclick="authorizeordeothorize('deauth')"/> 
						</td>
					</tr>
	 	</table>
	 	</s:if>
 </s:if>
</form>

 