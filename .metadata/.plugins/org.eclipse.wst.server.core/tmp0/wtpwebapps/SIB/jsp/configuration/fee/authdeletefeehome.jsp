<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/popup.js"></script>
<script>
function editSubfee( subfeeid,masterfeecode ){	 
	//var masterfeecode = document.getElementById("masterfeecode"); 
	//alert(" masterfeecode "+masterfeecode);
	var url = "editSubFeeHomeFeeConfig.do?masterfeecode="+masterfeecode+"&subfeecode="+subfeeid+"&flag="+"DEAuth"+"&userstatus="+"C";
	if( confirm( "Do you want to continue...") ){
		window.location=url;
		parent.showprocessing();
	}else{
		return false;
	}
}
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
.textcolor
{
color: maroon;
font-size: small;
}
.textcolor1
{
color: black;
font-size: small;
}
.bordercl td,th
{
  border:1px solid #CCCCCC;
}
</style>
</head>

<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addFeeActionFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">
		<table border='0' cellpadding='0' cellspacing='0' width='90%'> 
			<tr>
				<td colspan='4' style='padding-top:30px'>
					<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable">
						<tr> 
							<th>Fee Name</th>
							<th>Sub Fee Desc</th>
							<th>From Date </th>
							<th>To Date </th>
							<th> Fee type </th>
							<th>Fee Configured date</th>
							<th>Configured by</th>		
							<th>Status</th>
							<th>Deleted Status</th>
							<th>Remarks</th>					
						</tr>
						<s:iterator value="feebean.subfee">							
							<tr> 
								<td>${FEE_DESC}</td>
								<td><a onclick="return editSubfee(${FEE_SUBCODE},${FEE_CODE});"> ${SUBFEE_DESC}</a></td>
								<td>${FROMDATE}</td>
								<td>${TODATE}</td>
								<td>${SUBFEE_TYPE}</td>
								<td>${CONFIG_DATE}</td>
								<td>${USERNAME}</td>
								<td>${AUTH_CODE}</td>
								<td>${DELETED_FLAG}</td>
								<td>${REMARKS}</td>
							</tr>
						</s:iterator> 
					</table>
				</td>
			</tr>
		</table>
</s:form>
</body>
</html>