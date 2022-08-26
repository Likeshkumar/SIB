<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script>
function windowclose()
{
	window.close();
}
</script>
<div align="center">
<table border="0" cellpadding="0" cellspacing="0" width="90%" >
<s:iterator  value="subprod_detail" >
<tr><td>PRODUCT_CODE</td><td>: ${PRODUCT_CODE}</td></tr>
<tr><td>PRODUCT_NAME</td><td>: ${PRODUCT_NAME}</td></tr>
<tr><td>PERSONALIZED</td><td>: ${PERSONALIZED}</td></tr>
<tr><td>RELOADABLE</td><td>: ${RELOADABLE}</td></tr>
<tr><td>ATM_TRANS</td><td>: ${ATM_TRANS}</td></tr>
<tr><td>NO_PERCARD_ALLOWED</td><td>: ${NO_PERCARD_ALLOWED}</td></tr>
<tr><td>SERVICE_CODE</td><td>: ${SERVICE_CODE}</td></tr>
<tr><td>SCHEME_CODE</td><td>: ${SCHEME_CODE}</td></tr>
<tr><td>CARD_CCY</td><td>: ${CARD_CCY}</td></tr>
<tr align="center"><td><s:submit name="close" value="close" onclick="windowclose();"/></td></tr>
</s:iterator>
</table>
</div>