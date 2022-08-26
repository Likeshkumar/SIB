<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
function CardValidatepage(){
	var productid=document.getElementById("productid");
	if(productid.value=="-1"){
		errMessage(productid,"Select Product");
		return false;
	}
	return true;	
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="genCorporateReportTransactionReport.do" name="cardactivation" method="post" autocomplete="off">
	<table cellspacing="5" cellpadding="2" border="0" align="center" class="formtable" style="margin-top:30px">
		<tr>
			<td class="fnt">Select Product</td>
			<td> :</td>
			<td>
				<select name="productid" id="productid">
						<option value="-1">--Select Product--</option>
						<s:iterator  value="productlist">
							<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
						</s:iterator>
				</select>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td><s:submit name="getreport" id="getreport" value="PDF"   onclick=" return CardValidatepage();"/></td>
			<td><s:submit name="getreport" id="getreport" value="EXCEL" onclick=" return CardValidatepage();"/></td>
		</tr>
	</table>
</s:form>
