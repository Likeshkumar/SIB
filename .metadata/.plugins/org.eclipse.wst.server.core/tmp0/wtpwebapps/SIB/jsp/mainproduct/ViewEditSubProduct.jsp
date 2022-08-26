<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div align="center">
<s:form name="ProductAddFofm" action="SaveEditSubProduct" autocomplete="off" >
<table border='0' cellpadding='0' cellspacing='0' width='40%' align = 'center'>


		<s:iterator  value="subproductname"  >
		  <tr>
		  <td width="20%"> Product Name</td>
		  <input type="hidden" name="productcode" id="productcode" value="${PRODUCT_CODE}" >
		  <td width="60%"> : <input type="text" name="productname" id="productname" value="${PRODUCT_DESC}" ></td>
		  </tr>
		 
		  <tr><td colspan="2" align="center"> <s:submit value="submit" name="submit" onclick="return validate_form ( )" /> </td></tr>
	   </s:iterator>
	  

 


</table>
</s:form>
</div>
</body>
</html>