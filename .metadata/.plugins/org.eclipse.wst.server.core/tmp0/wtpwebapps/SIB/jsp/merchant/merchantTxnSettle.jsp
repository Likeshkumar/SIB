<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="jsp/merchant/script/merchscript.js"></script>
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="genMerchantTxnSettleReportMerchantSettlementReportsAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Select Business Date</td>
		<td>
			: <input type="text" name="businessdate" id="businessdate" onchange="return yearvalidation(this.id);"  style="width:167px">
			<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.businessdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
	<tr>
			<td>Enter Merchant Name </td>
		
			<td> : <select name="merchid" id="merchid">
					<option value="-1">Select Merchant name </option>
						<s:iterator value="merchantname">
							<option value="${MERCH_ID}">${MERCH_NAME}</option>
						</s:iterator>
					</select>
			</td>
	</tr>
</table>
<table>
	<tr>
		<td>
			<s:submit name="getreport" id="getreport" value="  Submit " onclick=" return validateMerchantdizeReport();"></s:submit>
		</td>
		<td>
			<input id="cancel" class="cancelbtn" type="button" onclick="return confirmCancel()" value="Cancel" name="cancel">
		</td>
	</tr>
</table>
</s:form>
</body>
</html>