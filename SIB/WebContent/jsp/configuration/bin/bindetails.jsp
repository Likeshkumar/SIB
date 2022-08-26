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
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>

<table align="center" border="0"  cellspacing="1" cellpadding="0" width="80%" class="formtable">
	<tr style="color: maroon;">
		<th colspan="2">VIEW BIN DETAILS</th>
	</tr>
		<% int rowcnt = 0; Boolean alt=true; %> 
<s:iterator  value="lst_bindetails" >
	<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
	<tr><td>BIN</td><td>: ${BIN}</td></tr>
	<tr><td>BIN DESCRIPTION</td><td>: ${BIN_DESC}</td></tr>
	<tr><td>CHN LENGTH</td><td>: ${CHNLEN}</td></tr>
	<tr><td>BASE LENGTH</td><td>: ${BASELEN}</td></tr>
	<tr><td>PIN LENGTH</td><td>: ${PIN_LENGTH}</td></tr>
	<tr><td>PIN GEN METHOD</td><td>: ${GEN_METHOD}</td></tr>
</s:iterator>	
</table>
<tr align="center"><td><s:submit name="close" value="close" onclick="windowclose();"/></td></tr>

