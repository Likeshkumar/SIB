<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.4.min.js"></script>
<sx:head />
<script type="text/javascript">
function validateReportpage(){
	//var batchid = document.reportsgen.batchid;
	var filelist = document.getElementById("Filelist");
	var merchantcode = document.getElementById("merchantcode");
	if(filelist.value == "-1"){
		errMessage(filelist, "Select the File Name");
		return false;
	}
	if(merchantcode.value == "-1"){
		errMessage(merchantcode, "Select the File Name");
		return false;
	}
	
	return true;
}
function getMerchantlist()
{
	var fileid=(document.getElementById('Filelist'));
	
	if(fileid.value == "-1")
	{
		errMessage( fileid, "Please Select Merchant " );
		return false;
	}
	else
	{
		
		var url="merchNetSettlelistMerchantSettlementReportsAction.do?fileid="+fileid.value+" ";
		/* alert(url); */
		var response=AjaxReturnValue(url);
		//alert(response);
		document.getElementById('merchantcode').innerHTML = response;
		 
		return true;
	}
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="merchSettlementreportMerchantSettlementReportsAction" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="merchNetTransReportMerchantSettlementReportsAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Settlement File </td><td>&nbsp;</td>
			<td> <select id="Filelist" name="Filelist" onchange="getMerchantlist()">
			<option value="-1">Select File Name </option>
				<s:iterator value="settlementfilelist">
					<option value="${SETTLE_FILE_NAME}">${SETTLE_FILE_NAME}</option>
					</s:iterator>
			</select>
			</td>
 	</tr> 
 	
 	<tr>
		<td>Merchant Name </td><td>&nbsp;</td>
			<td> <select id="merchantcode" name="merchantcode" >
			
			</select>
			</td>
 	</tr> 	
 		
</table>
<table>
	<tr>
		<td>
			<s:submit name="getreport" id="getreport" value="  Next " onclick=" return validateReportpage();"/>
		</td>
		<td>
			<input id="cancel" class="cancelbtn" type="button" onclick="return confirmCancel()" value="Cancel" name="cancel">
		</td>
	</tr>
</table>
</s:form>
