<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">
function validateReportpage()
{
	var  report = document.reportsgen.reportvalue.options[document.reportsgen.reportvalue.selectedIndex].value;
	var fromdate=document.getElementById('fromdate').value;
	var todate=document.getElementById('todate').value;
	var fromdate_date =new Date(fromdate);
	var todate_date =new Date(todate);
	if(report == -1)
	{
		alert(" Please Select The Report ");
		return false;
	}
	if(fromdate=="") 
	{
		 alert(" From Date Cannot Be Empty");
		 return false;
	}
	if(todate=="")
	{
		 alert(" To Date Cannot Be Empty");
		 return false;
	}
	if (Date.parse(fromdate_date) > Date.parse(todate_date)) 
	{
		alert("    		Invalid Date Range!\nFrom Date cannot be More Than To Date!");
		return false;
	}
	//	window.open('viewSchemeFeeStruct.do?schemecode='+schemecode,'','left=350,top=150,width=300,height=200,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
	//	window.open("renderReportGenerateCardReportAction.do?fromdate="+fromdate+",todate="+todate+",report"+)";
	//window.open('renderReportGenerateCardReportAction.do?fromdate='+fromdate+'&&todate='+todate+'&&reportvalue='+report+'',left=350,top=150,width=300,height=200,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');

return true;
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
	<s:form action="glViewReportActionReportgenerationAction" name="reportsgen" autocomplete="off" namespace="/"   >
		<table border='0' cellpadding='0' cellspacing='0' width='15%' class="formtable" >
		<tr>
			<td>&nbsp;			</td>
		</tr>
		<tr>
			<td>
				<s:submit name="getreport" id="getreport" value="Generate" onclick=" return validateReportpage();"/>
			</td>
			 
		</tr>
		</table>
	</s:form>
</div>