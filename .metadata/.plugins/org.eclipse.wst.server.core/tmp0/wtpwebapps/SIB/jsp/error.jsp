<%-- 
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name="com.ifp.util.ErrorClass" var="error" >
<div>
 <table border="0" cellpadding="0" cellspacing="0" width="100%" >
 	<tr><td><br></td></tr>
 	<tr align="center"><td><div><b><font color="blue"><s:property value="Header" /></font></b></div></td></tr>
	<tr align="center"><td><s:property value="Message1"></s:property></td></tr>
	<tr align="center"> <td><s:property value="Message2"></s:property></td></tr>
	<tr align="center"><td><s:property value="Message3"></s:property></td></tr>
	<tr align="center"><td>
		<s:if test="%{#linkrequired !=''}">
			<s:form name="errorpage" action="loginLink.do" >
				<s:submit  value="Retry" id="Retry"></s:submit>
			</s:form>
		</s:if>
	</td></tr>

</table>
</div>
</s:bean>

<s:bean name="com.ifp.util.ErrorClass" var="resd" >
<s:param name="Message1"></s:param>
<s:param name="Message2"></s:param>
<s:param name="Message3"></s:param>
<s:param name="linkrequired"></s:param>
<s:param name="link"></s:param>
</s:bean> --%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

 <center><b><font size="5" color="red">Something Went Wrong, Please Contact Technical Team.</font></b></center> 

</body>
</html>