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
function CardValidatepage(){
	var fdate=document.getElementById("fromdate");
	var tdate=document.getElementById("todate");
	var bin = document.getElementById("bin");
	var merchrxt = document.getElementById("merchidtxt");
	if(fdate.value==""){
		errMessage(fdate,"select from date");
		return false;
	}
	if(tdate.value==""){
		errMessage(tdate,"select Todate");
		return false;
	}
	if(bin.value == "-1"){
		errMessage(bin,"Select bin");
		return false;
	}
	if(merchrxt.value=="yes")
	{
		if(merchrxt.value == "")
		{
			errMessage(merchrxt,"Enter the MerchantID");
			return false;
		}
	}
	return true;
	
}

function enabletable()
{
	var isset=0;
	var colRadio = document.getElementsByName('merchid');
	for (var i = 0; i < colRadio.length; i++)
	{
		if (colRadio[i].checked)
		{
			  isset=  colRadio[i].value;
		}
	}
	if(isset=="no")
	{
	
			document.getElementById('show').style.display='none';
	}
	else
	{
			document.getElementById('show').style.display='table-row';
	}
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="cardActivationgenreportReportgenerationAction.do" name="cardactivation" method="post" autocomplete="off"> --%>
<s:form action="creditcardstatementreportReportgenerationAction.do" name="cardactivation" method="post" autocomplete="off">
<table>
	<tr>
		<td>From Date</td>
		<td> : <input type="text" name="fromdate" id="fromdate" onchange="return yearvalidation(this.id);"  style="width:167px">
			<img src="images/cal.gif" id="image" onclick="displayCalendar(document.cardactivation.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
	<tr>
		<td>To Date</td>
		<td> : <input type="text" name="todate" id="todate" onchange="return yearvalidation(this.id);"  style="width:167px">
			<img src="images/cal.gif" id="image" onclick="displayCalendar(document.cardactivation.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
		</td>
	</tr>
	<tr>
		<td>Bin</td>	
		<td> : <select id="bin" name="bin">
					<option value="-1">Select Bin </option>
					<option value="ALL">ALL</option>
					<s:iterator value="bin_name_list">
						<option value="${BIN}">${BIN_DESC}</option>
					</s:iterator>
				</select>
		</td>
	</tr>
	<tr>
		<td>Merchant Id</td>	
		<td> : <input type="radio" id="merchid" name="merchid" onclick="enabletable();" value="yes" />Yes
			   <input type="radio" checked id="merchid" name="merchid"  onclick="enabletable();" value="no" />NO
  		</td>
	</tr>
	<tr id="show"  style="display:none" >
		<td colspan="1">Enter merchant id</td>
		<td><input type="text" name="merchidtxt" id="merchidtxt" /></td>
	</tr>
</table>
<table>
	<tr>
		<td><s:submit name="getreport" id="getreport" value="PDF"   onclick=" return CardValidatepage();"/></td>
		<td><s:submit name="getreport" id="getreport" value="EXCEL" onclick=" return CardValidatepage();"/></td>
	</tr>
</table>
</s:form>
