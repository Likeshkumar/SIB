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
function validateReportpage(){
	var batchid = document.reportsgen.batchid;
	var open_close = document.reportsgen.open_close;
	if(batchid.value == ""){
		errMessage(batchid, "Select the Merchant Name");
		return false;
	}
	if(open_close.value == "-1"){
		errMessage(open_close, "Select the Open Or close");
		return false;
	}
	return true;
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="selectedReportMerchantReports" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="selectedReportMerchantReports.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td> Merchant Name : </td>
	
		<td>
			<sx:autocompleter 
			name="batchid" id="batchid" autoComplete="false" 
			list="autocomlete"  
			listValue="NEWMERCHDESC" cssStyle="width: 288px;"/>
		</td> 
	</tr>
	
	<tr>
		<td> Open / Close : </td>
		<td>
			<select name="open_close" id="open_close" >
				<option value="-1">select</option>
				<option value="open">open</option>
				<option value="close">close</option>
			</select>
		</td>
	</tr>
	
		
</table>
<table>
	<tr>
		<td>
			<s:submit name="getreport" id="getreport" value="  Next " onclick=" return validateReportpage();"/>
		</td>
		<td>
			<input id="cancel" class="cancelbtn" type="button" onclick="return confirmCancel()" value="Cancel" name="cancel">
		</td>
	</tr>
</table>
</s:form>
