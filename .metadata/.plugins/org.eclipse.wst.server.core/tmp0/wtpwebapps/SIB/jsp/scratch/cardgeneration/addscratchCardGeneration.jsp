<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 <link rel="stylesheet" type="text/css" href="style/calendar.css"/>
 <script type="text/javascript" src="js/calendar.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script>
function fetchProductConfig(FLAG)
{
	//var productName = document.getElementById("productName").value;
	//var productDesc = document.getElementById("productDesc").value;	
	var productName = document.getElementById("productName").value;
	
	document.fetchProductConfigForm.action="fetchProductProductConfiguration.do?productName="+productName+"&act="+FLAG;
	document.fetchProductConfigForm.submit();
}

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
	var productCode = document.getElementById("productName").value;
	var denomValue = document.getElementById("denomValue").value;
		var url="fetchBatchIdScratchCardProduction.do?act="+FLAG+"&denomValue="+denomValue+"&productCode="+productCode;
		var response=AjaxReturnValue(url);
		
		document.getElementById('p_batchId').innerHTML = response;
		return false;
	
}

function addScratchCard(flag)
{
	var productCode = document.getElementById("productName").value;
	var batchid = document.getElementById("p_batchId").value;
	var denomValue = document.getElementById("denomValue").value;
	document.addScratchCardForm.action="saveScratchCardScratchCardProduction.do?act="+flag+"&productCode="+productCode+"&denomValue="+denomValue+"&batchid="+batchid;
	document.addScratchCardForm.submit();
	
}

function viewAllDetails(flag)
{
	var productCode = document.getElementById("productName").value;
	var batchid = document.getElementById("p_batchId").value;
	var url="listScratchCardScratchCardProduction.do?productCode="+productCode+"&act="+flag+"&batchid="+batchid;
	var response=AjaxReturnValue(url);
	document.getElementById('alldetails').innerHTML = response;
	document.getElementById('view_scratchcardprocess').style.display="";
	if(response!='NoDataFound')
	document.getElementById('generatebutton').style.display="";
	return false;
	
}




</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form  name ="addScratchCardForm" method="post" autocomplete="off"> 
	<s:hidden id="act" name="act" value="%{scratchcardbeans.flag}"></s:hidden>
		<div align="center">
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
				<input type="button" onclick="viewAllDetails('${scratchcardbeans.flag}')" value="Submit"/></td></tr>
			</table>
</div>			
			
			<br/><br/>
<div id="view_scratchcardprocess" style="display:none">			
<table align="center" border="0"  cellspacing="0" cellpadding="0" width="80%" class="formtable" >
	<tr>
		<th>Product Name</th>
		<th>Denom Value</th>
		<th>Card Count</th>
		<th>Added By</th>
		<th>Added Date</th>
		<th>Auth By</th>
		<th>Auth Date</th>
		<th>Auth Status</th>
		<th>Batch Id</th>
	</tr>
	<tr id="alldetails"></tr>
</table>

	<table id="generatebutton" style="display:none"><tr><td>
		<input type="button" onclick="addScratchCard('${scratchcardbeans.flag}'),parent.showprocessing();" value="Generate"/></td></tr>
	</table>
	
</div>
</form>	
	
	