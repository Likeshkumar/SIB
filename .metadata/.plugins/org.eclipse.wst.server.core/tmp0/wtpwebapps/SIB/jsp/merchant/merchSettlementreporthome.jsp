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
	//var batchid = document.reportsgen.batchid;
	var merchenttype = document.getElementById("merchenttype");	
	if(merchenttype.value == "-1"){
		errMessage(merchenttype, "Select the Merchant Name");
		return false;
	}	
	return true;
}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%-- <s:form action="merchSettlementreportMerchantSettlementReportsAction" name="reportsgen" method="post" autocomplete="off"> --%>

<s:form action="merchSettlementreportMerchantSettlementReportsAction.do" name="reportsgen" method="post" autocomplete="off">
<table>
	<tr>
		<td>Merchant Name</td>
			<td> : <select id="merchenttype" name="merchenttype">
			<option value="-1">Select Merchant Name </option>
				<s:iterator value="Merchantlist">
					<option value="${MERCH_ID}">${MERCH_NAME}
					</option>
					</s:iterator>
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
