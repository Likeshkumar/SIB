<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function validateReportpage()
{
	var  busin = document.getElementById("business");
	if(busin.value == "")
	{		
		errMessage(busin,"Enter business Date");
		return false;
	}
}
</script>
</head>
 <jsp:include page="/displayresult.jsp"/>
<div align="center">
	<s:form action="genGLReportReportgenerationAction" autocomplete="off"  name="reportsgen" namespace="/">
		<table border='0' cellpadding='0' cellspacing='0' width='30%' >
		<tr>
			<td>
				Business Date
			</td>
			<td>
				<input type="text" name="business" id="business"   style="width:160px">				
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.business,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>		
		</table>
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<td>
				<s:submit name="getreport" id="getreport" value="  Get Report  " onclick=" return validateReportpage();"/>
			</td>
			<td>
				<s:reset value="Reset"/>
			</td>
		</tr>
	</table>	
	</s:form>
</div>