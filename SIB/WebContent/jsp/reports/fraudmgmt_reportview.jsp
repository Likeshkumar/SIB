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
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<div id="fw_container">
<h1><s:property value="%{reporttitle}"></s:property></h1>	
	<%-- <s:form action="selectedReportMerchantReports" name="reportsgen" method="post" autocomplete="off"> --%>
	<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
		<thead>
			  <tr>
			  	<th> S.NO </th>
				<th> CARD NUMBER </th>
				<th> TXN CODE </th>
				<th> REFERENCE  NUMBER </th>
				<th> TXN DATE  </th>
				<th> TXN TIME </th>
				<th> TXN AMOUNT  </th>
			</tr>
		</thead>
		<tbody>
			 <% int rowcnt = 0; Boolean alt=true; %> 
			 <s:iterator value="fraudreportlist">
				<tr	<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>	>
					<td> <%= rowcount %> </td>
				 	<td>${CARD_NO}</td>
				 	<td>${TXN_CODE}</td>
				 	<td>${TXN_REF_NO}</td>
				 	<td>${TRANDATE}</td>
				 	<td>${TRAN_TIME}</td>
				 	<td>${TRANSACTION_AMOUNT}</td>
				 </tr>
			</s:iterator>
		</tbody>
	</table>
</div>
