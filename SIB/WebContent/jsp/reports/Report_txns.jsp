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
<div align="center">
	<s:form action="renderReportTxnReportAction" name="reportsgen" namespace="/" target="_form" autocomplete="off">
		<table border='0' cellpadding='0' cellspacing='0' width='60%' >
			<tr align="center">
				<td colspan="2">
						<%
								String ErrorMessage = null;
								ErrorMessage = (String) session.getAttribute("ErrorMessage");
								session.removeAttribute("ErrorMessage");
						%>
							<%
								if (ErrorMessage != null) 
									{
							%>
										<font color="red" ><b><%=ErrorMessage%></b></font>
							<%
									} 
							%>
				</td>
			</tr>
		</table>
			<% 
				String Error_status = null;
				Error_status=(String) session.getAttribute("Errorstat");
				session.removeAttribute("Errorstat");
				if (Error_status != null)
				{
					if(!(Error_status.equals("E")))
					{
			%>

	 <div align="center">
	 	<table border='0' cellpadding='0' cellspacing='0' width='10%' >
		<tr align="center">
		<td colspan="1">
				<select name="reportvalue" id="reportvalue">
 				<option value="-1">--Select Report--</option>
 				<s:iterator  value="reports_name_list">
 				<option value="${REPORT_ID}">${REPORT_NAME}</option>
 				</s:iterator>
 				</select>  
		</td>
		</tr>
	</table>
	</div>
	<br>
	<div align="center">
	<table border='0' cellpadding='0' cellspacing='0' width='25%' >
		<tr>
			<td>
				From Date 
			</td>
			<td>
				<input type="text" name="fromdate" id="fromdate"  readonly="readonly" style="width:120px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd/mm/yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date 
			</td>
			<td>
				<input type="text" name="todate" id="todate" readonly="readonly"  style="width:120px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd/mm/yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
			</td>
		</tr>

	</table>
	</div>
	<div align="center">
	<table border='0' cellpadding='0' cellspacing='0' width='15%' >
		<tr>
			<td>&nbsp;			</td>
		</tr>
		<tr>
			<td>
				<s:submit name="getreport" id="getreport" value=" View " onclick=" return validateReportpage();"/>
			</td>
			<td>
				<s:reset value="Reset"/>
			</td>
		</tr>
	</table>
	</div>
	<% } } %>
	</s:form>
</div>