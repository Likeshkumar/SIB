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
function validateReportpage(){
	var fraudid = document.reportsgen.fraudid;
	//var fraudid = document.getElementById("fraudid");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	var product = document.getElementById("product");
	var bin = document.getElementById("bin");
	
	if(product){
		if((product.value == "-1")){
			errMessage(fraudid, "Select the Product");
			return false;
		}	
	}	
	if(fromdate){
		if( trim(fromdate.value) == ""){
			errMessage(fromdate, "Select the from Date ");
			return false;
		}	
	}
	if(todate){
		if( trim(todate.value) == ""){
			errMessage(todate, "Select the To Date");
			return false;
		}
	}
	if(bin){
		if( bin.value == "-1"){
			errMessage(todate, "Select the BIN");
			return false;
		}		
	}
	
	//alert(gentype.value);
	return true;
}

</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="selectedReportMerchantReports" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="gentxnFailerHomeReportgenerationAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Bin </td>
	
		<td>	<select name="bin" id="bin">
		 					<option value="-1">--Select--</option>
		 					<option value="ALL"> ALL </option>
		 						<s:iterator  value="prodlist">
		 							<option value="${BIN}">${BIN_DESC}</option>
		 						</s:iterator>
		 				</select>
		</td>
		
	</tr>
		<tr>
			<td>
				From Date 
			</td>
			<td>
				<input type="text" name="fromdate" id="fromdate"   style="width:196px" onchange="return yearvalidation(this.id);">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td>
				To Date 
			</td>
			<td>
				<input type="text" name="todate" id="todate"  style="width:196px" onchange="return yearvalidation(this.id);">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">				
			</td>
		</tr>		
</table>
<table>
	<tr>
		<td>
			<s:submit name="getreport" id="getreport" value="Submit" onclick=" return validateReportpage();"/>			
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>
