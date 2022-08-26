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
function validateMerchantwalletReport(){
	//var batchid = document.reportsgen.batchid;
	var bankname = document.getElementById("bankname");
	var status= document.getElementById("status");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");	
	if(bankname.value == "-1"){
		errMessage(bankname, "Select the Bank Name");
		return false;
	}
	if(status.value=="-1"){
		errMessage(status, "Select the Status");
		return false;
	}
	if(fromdate.value == ""){
		errMessage(fromdate, "Select the From date");
		return false;
	}
	if(todate.value == ""){
		errMessage(todate, "Select the todate date");
		return false;
	}
	
	return true;
}

</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="merchWalletdepositreportMerchantSettlementReportsAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Bank Name</td>
	
		<td> : <select id="bankname" name="bankname">
				<option value="-1">Select Bank Name </option>
					<s:iterator value="banklist">
						<option value="${BANK_CODE}">${BANK_NAME}
				</option>
					</s:iterator>
			</select>
			</td>
	</tr>
		<tr>
		<td>Status</td>
	
		<td> : <select id="status" name="status">
				<option value="-1">Status</option>
				<option value="A">Approved</option>
				<option value="R">Rejected</option>	
			</select>
		</td>
	</tr>
		<tr>
			<td>
				From Date
			</td>
			<td>
				: <input type="text" name="fromdate" id="fromdate" onchange="return yearvalidation(this.id);"  style="width:167px" readyonly="true">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date
			</td>
			<td>
				: <input type="text" name="todate" id="todate" onchange="return yearvalidation(this.id);"  style="width:167px" readyonly="true">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
			</td>
		</tr>
</table>
<table>
	<tr>
		<td>
			<s:submit name="getreport" id="getreport" value="  Submit " onclick="return validateMerchantwalletReport();"/>
		</td>
		<td>
			<input id="cancel" class="cancelbtn" type="button" onclick="return confirmCancel()" value="Cancel" name="cancel">
		</td>
	</tr>
</table>
</s:form>
