<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
</head>
<form>
	<table border="0" cellpadding="0" cellspacing="6" width="50%" class="formtable">
	<tr><th colspan="3"><s:property value="%{feesubdesc}" /></th></tr>
	<tr><th class="brown">TRANSACTION NAME</th><th class="brown">FEE AMOUNT</th><th class="brown">FEE MODE</th></tr>
		<s:iterator value="runningSchemeList">
			<tr>
				<td>${FEE_ACTION}</td>
				<td>${FEE_AMOUNT}</td>
				<td>${FEEMODE}</td>
			</tr>
		</s:iterator>
	</table>
</form>

