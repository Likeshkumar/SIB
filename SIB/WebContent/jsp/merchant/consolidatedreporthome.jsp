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
	var merchenttype = document.getElementById("merchenttype");
	var txntype = document.getElementById("txntype");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	if(merchenttype.value == "-1"){
		errMessage(merchenttype, "Select the Merchant Name");
		return false;
	}
	if(txntype.value == "-1"){
		errMessage(txntype, "Select the Transaction type");
		return false;
	}
	if(fromdate.value == "-1"){
		errMessage(fromdate, "Select the From Date");
		return false;
	}
	if(todate.value == "-1"){
		errMessage(todate, "Select the To Date");
		return false;
	}
	return true;
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="merchSettlementreportMerchantSettlementReportsAction" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="genconsolidatedReportMerchantSettlementReportsAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Merchant Name</td><td>&nbsp;</td>
			<td> <select id="merchenttype" name="merchenttype">
			<option value="-1">Select Merchant Name </option>
				<s:iterator value="Merchantlist">
					<option value="${MERCH_ID}">${MERCH_NAME}
					</option>
					</s:iterator>
			</select>
			</td>
 	</tr>
 	<tr>
		<td>Transaction Type </td><td>&nbsp;</td>
			<td> <select id="txntype" name="txntype">
			<option value="-1">Select Transaction Type </option>
				<s:iterator value="Translist">
					<option value="${TXN_CODE}">${ACTION_CODE}
					</option>
					</s:iterator>
			</select>
			</td>
 	</tr>
 		<tr>
			<td>
				From Date
			</td><td>&nbsp;</td>
			<td>
				<input type="text" name="fromdate" id="fromdate"   onchange="return yearvalidation(this.id);"  style="width:167px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date
			</td><td>&nbsp;</td>
			<td>
				<input type="text" name="todate" id="todate" onchange="return yearvalidation(this.id);"  style="width:167px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
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
