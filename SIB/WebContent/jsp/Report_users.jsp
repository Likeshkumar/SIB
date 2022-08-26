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
	//alert("Report Selected is "+report);
	if(report == -1)
	{
		return false;
	}
return true;
}
</script>
</head>
<div align="center">
	<s:form action="selectedReportReportgenerationAction" name="reportsgen" namespace="/" autocomplete="off">
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

	<!-- 
	<table border='1' cellpadding='0' cellspacing='0' width='30%' >
		<tr align="center">
		<td>  
			<s:select label="Reports "  
			headerKey="-1" headerValue="Select Report"
			list="reportnames" 
			listValue="%{reportname}"
			listKey="%{reportorder}" 
			id="reportvalue" 
			name="reportvalue" style="width:200px" />
		</td>
		</tr>
	</table>
	 -->
	 
	 	<table border='1' cellpadding='0' cellspacing='0' width='30%' >
		<tr align="center">
		<td>
				<select name="reportvalue" id="reportvalue">
 				<option value="-1">--Select Report--</option>
 				<s:iterator  value="reports_name_list">
 				<option value="${REPORT_ID}">${REPORT_NAME}</option>
 				</s:iterator>
 				</select>  
		</td>
		</tr>
	</table>
	<br>
	<table border='1' cellpadding='0' cellspacing='0' width='50%' >
		<tr>
			<td>
				From Date
			</td>
			<td>
				<input type="text" name="fromdate" id="fromdate"   style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date
			</td>
			<td>
				<input type="text" name="todate" id="todate"  style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
			</td>
		</tr>

	</table>
	<table border='1' cellpadding='0' cellspacing='0' width='20%' >
		<tr>
			<td>
				<s:submit name="getreport" id="getreport" value="  Get Report  " onclick=" return validateReportpage();"/>
			</td>
			<td>
				<s:reset value="Reset"/>
			</td>
		</tr>
	</table>
	<% } } %>
	</s:form>
</div>