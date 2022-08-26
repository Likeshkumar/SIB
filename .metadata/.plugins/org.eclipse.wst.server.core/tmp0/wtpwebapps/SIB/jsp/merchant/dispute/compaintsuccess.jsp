<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript" src="js/script.js"></script> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
		<table border='0' cellpadding='0' cellspacing='0' width='30%' >
		<tr align="center">
			<td colspan="2">
					<%
							String usereditstatus = null;
							usereditstatus = (String) session.getAttribute("prevmsg");
							session.removeAttribute("prevmsg");
					%>
						<%
							if (usereditstatus != null) 
								{
						%>
							<font color="green" ><b><%=usereditstatus%></b></font>
						<%
								} 
						%>
			</td>
		</tr>

</table>

<table>
		<tr><td><input type="button" value="Close"  class="cancelbtn" onclick="javascript:window.close();" /></td></tr>
	</table> 

</div>
</head>