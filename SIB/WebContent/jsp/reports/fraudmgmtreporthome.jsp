<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script type="text/javascript">
function validateReportpage(grptype){
	var fraudid = document.reportsgen.fraudid;
	//var fraudid = document.getElementById("fraudid");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	var gentype = document.getElementById("gentype");
	//alert(fraudid.value);
	if((fraudid.value == "-1")||(fraudid.value == "")){
		errMessage(fraudid, "Select the Rejected Reason");
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
	//alert(fraudid.value);
	if(fraudid.value == "AMTBASED"){
		//alert("amt base === ");
		var amt = document.getElementById("excamt");
		if(amt.value==""){
			//alert("amt check");
			errMessage(amt, "Please Enter the Exceed Amount ");
			return false;
		}
	}
	gentype.value=grptype;
	//alert(gentype.value);
	return true;
}
function viewamttextbox(reason){
	//alert("reason"+reason);
	if(reason=="AMTBASED"){
		//alert("show");
		document.getElementById("exceedamt").style.display="table-row";
	}else{
		//alert("not show");
		document.getElementById("exceedamt").style.display="none";
	}
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="selectedReportMerchantReports" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="FraudmgmtGenReportReportgenerationAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Rejected Reason </td>
	
		<td>
		<s:select list="%{Fraudreport_list}"  name="fraudid" id="fraudid" listKey="REASON_KEY" listValue="DESCRIPTION" headerKey="-1" headerValue="----- SELELCT REASON ------ " cssStyle="width: 288px;" onchange="viewamttextbox(this.value)"/>
			<input type="hidden" value=""  id="gentype" name="gentype"/>
		</td>
	</tr>
		<tr>
			<td>
				From Date
			</td>
			<td>
				<input type="text" name="fromdate" id="fromdate"   style="width:287px" onchange="return yearvalidation(this.id);">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date
			</td>
			<td>
				<input type="text" name="todate" id="todate"  style="width:287px" onchange="return yearvalidation(this.id);">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
			</td>
		</tr>
		<tr id="exceedamt" style="display:none">
			<td>
				Exceed Amount 
			</td>
			<td>
				<input type="text" name="excamt" id="excamt" onKeyPress="return numerals(event);" />
			</td>		
		</tr>
</table>
<table>
	<tr>
		<td>
			<s:submit name="getreport" id="getreport" value="Generate PDF Report" onclick=" return validateReportpage('pdf');"/>
			<s:submit name="getreport" id="getreport" value="View Report" onclick=" return validateReportpage('view');"/>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>
