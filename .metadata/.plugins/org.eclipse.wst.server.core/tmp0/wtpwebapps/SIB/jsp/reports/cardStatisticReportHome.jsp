<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>

function validateValues()
{
	
	var appdate = document.getElementById("fromdate");
	var appnum = document.getElementById("todate");
	var chn = document.getElementById("chn");
	if(appdate.value == "")
	{
		errMessage(appdate,"Please Select The From Date");
		return false;
	}
	if(appnum.value == "")
	{
		errMessage(appnum,"Please Enter the To Date");
		return false;
	}
	if(chn.value==""){
		errMessage(chn,"Please Enter the CHN ");
		return false;
	}
	return true;
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="gencardStatisticReportReportgenerationAction"  name="personalorderform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount">
    <tr> 
    	<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.personalorderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
    </tr>	
    	
    <tr>	
		<td> 
		To Date
		</td>
		<td> : 
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.personalorderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> 
	 <tr>	
		<td> 
		CHN
		</td>
		<td> : 
			<input type="text" name="chn" id="chn" style="width:160px"/>
		</td>
	</tr> 
</table>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>	
			<s:submit value="Next" name="next_process" id="next_process" onclick="return validateValues();"/>
		</td>
		<td>
			<!-- <input type="button" name="cancle" value="Cancel" onclick="return forwardtoCardOrderPage();"> -->
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>

