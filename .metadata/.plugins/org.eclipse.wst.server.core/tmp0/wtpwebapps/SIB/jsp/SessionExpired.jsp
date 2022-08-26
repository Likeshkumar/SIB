<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">

<script>
function noBack()
{
	window.history.forward(1);
}

</script>
<body onUnload="noBack()">
<div align="center">
<br>
<br>
<br>
<table border='0' cellpadding='0' cellspacing='0' width='100%' >
	<tr align="center">
		<td>
			The Session Is Expired
		</td>
	</tr>
	<tr align="center">
		<td>
			If you Want To Login Again Please Follow Link
		</td>
	</tr>
	<tr align="center">
		<td>
			<s:a  href="loginLink.do"> <b style="color: Red">Please Click Here</b></s:a> 
		</td>
	</tr>
</table>
</div>
</body>
