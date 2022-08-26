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
	var batchid = document.reportsgen.batchid;
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	if(batchid.value == ""){
		errMessage(batchid, "Select the Merchant Name");
		return false;
	}
	if( trim(fromdate.value) == ""){
		errMessage(fromdate, "Select the from Date ");
		return false;
	}
	if( trim(todate.value) == ""){
		errMessage(todate, "Select the To Date");
		return false;
	}	
	return true;
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="selectedReportMerchantReports" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="generateReportMerchantReports.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Merchant Name</td>
	
		<td>
			<sx:autocompleter 
			name="batchid" id="batchid" autoComplete="false" 
			list="autocomlete"  
			listValue="NEWMERCHDESC" cssStyle="width: 288px;"/>
			<input type="hidden" name="nobatch" id="nobatch" value="no"/>
		</td> 
		
		
	</tr>
		<tr>
			<td>
				From Date
			</td>
			<td>
				<input type="text" name="fromdate" id="fromdate" onchange="return yearvalidation(this.id);"  style="width:167px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date
			</td>
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
