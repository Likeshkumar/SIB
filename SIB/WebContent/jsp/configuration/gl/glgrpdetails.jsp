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

<table border="0" cellpadding="0" cellspacing="0" width="90%" >
<s:iterator  value="lst_gldetails" >
<tr><td>INST ID</td><td>: ${INST_ID}</td></tr>
<tr><td>GROUP CODE</td><td>: ${GROUP_CODE}</td></tr>
<tr><td>GROUP NAME</td><td>: ${GROUP_NAME}</td></tr>
<tr><td>GROUP SEQUNCE</td><td>: ${GROUP_SEQUNCE}</td></tr>
<tr><td>GROUP PARENTID</td><td>: ${GROUP_PARENTID}</td></tr>
<tr><td>MAKER ID</td><td>: ${MAKER_ID}</td></tr>
<tr><td>CHECKER ID</td><td>: ${CHECKER_ID}</td></tr>
<tr align="center"><td><s:submit name="close" value="close" onclick="windowclose();"/></td></tr>

</s:iterator>



</table>
