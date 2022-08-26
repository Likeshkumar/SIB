<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<style>
 .disabled{
 	border-color: silver !important;
 }
</style>
<script type="text/javascript">
function display(radiotype)
{	
	if(radiotype=='accrad')
	{
		document.transaction.acc.disabled=false;
		document.transaction.acc.style.borderColor="grey";
		document.transaction.chn.disabled=true;
		document.transaction.chn.style.borderColor="silver";
		document.transaction.chn.value="";
	}
	if(radiotype=='chnrad')
	{
	 	document.transaction.acc.disabled=true;
	 	document.transaction.acc.style.borderColor="silver";
		document.transaction.acc.value="";
		document.transaction.chn.disabled=false;
		document.transaction.chn.style.borderColor="grey";
	}	
}
  //----------------------validation--------------------------------------//
function validate()
{
	
	var radiogroup = document.transaction.radname;
	for (var i=0; i<radiogroup.length; i++) 
	{
		if (radiogroup[i].checked)
		{
		break;
		}
	}
	if (i==radiogroup.length)
	{
		errMessage(document.getElementById("accrad"),"No radio button is checked");		
	return false;
	}

	if (document.transaction.fromdate.value=="")
	{
		errMessage(document.transaction.fromdate,"Enter Fromdate");
		return false;
	}
	if (document.transaction.todate.value=="")
	{
		errMessage(document.transaction.todate,"Enter Todate");
		return false;
	}
	if(document.transaction.accrad.checked)
	{
		if (document.transaction.acc.value=="")
		{
			errMessage(document.transaction.acc,"Enter account no");
			return false;
		}
	}
	if(document.transaction.chnrad.checked)
	{
		if (document.transaction.chn.value=="")
		{
			errMessage(document.transaction.chn,'Enter CHN');
		return false;
		}
	}
	return true;

}
  </script>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form name="transaction" id="transaction" action="generatetransactionstatusReportgenerationAction.do" autocomplete="off">
	<table cellspacing="5" cellpadding="2" border="0" align="center" style="margin-top:30px">
		<tr>
		<td>Criteria</td>
		<td>:</td>
		<td> 
			<input type="radio" name="radname" id="accrad" value="accrad" onclick='display(this.value)'/>Account NO
			<input type="radio" name="radname" id="chnrad" value="chnrad" onclick='display(this.value)'/>CHN
		</td>
		</tr>
		<tr>
			<td>Fromdate </td>
			<td>:</td>
			<td><input type="text" id="fromdate" name="fromdate" />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transaction.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>Todate</td>
			<td>:</td>
			<td><input type="text" id="todate" name="todate" />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transaction.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		
		
		<tr>
			<td>Account No</td>
			<td>:</td>
			<td><input type="text" id="acc" name="acc" disabled="disabled" style="border-color:silver"/></td>
		</tr>
		<tr>
			<td>CHN</td>
			<td>:</td>
			<td><input type="text" id="chn" name="chn" disabled="disabled" style="border-color:silver"/></td>
		</tr>
				
		<tr>
			<td colspan="3">
				<table>
					<tr>
						<td></td>
						<td><input type="submit" name="generate" value="Generate" onclick="return validate();"/></td>
						<td><input type="reset" name="Cancel" value="Cancel"/></td>
						</tr>
				</table>
			</td>
		</tr>

	</table>
  </form>
</body>
</html>