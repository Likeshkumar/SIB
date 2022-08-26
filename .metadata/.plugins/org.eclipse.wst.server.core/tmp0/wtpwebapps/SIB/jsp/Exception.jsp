<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<br><br>
<div align="center">
<table>
<s:bean name="com.ifp.exceptions.Exceptionifp" var="suresh">
	<tr>
		<td align="center"><b style="color: red" ><s:property value="message"/></b></td>
	</tr>
</s:bean>
</table>
</div>
</body>
</html>