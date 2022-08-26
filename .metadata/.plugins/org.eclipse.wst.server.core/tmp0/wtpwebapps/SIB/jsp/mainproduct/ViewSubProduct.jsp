<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<script>
function makeConfirm( ) {
	
	//var message = "Do you want to "+ msg ;
	//var x = confirm ( 'Do You Wanna Proceed...' );
	if ( confirm ( 'Do You Wanna Proceed...' ) ){
		return true;
	}else {
		return false;
	}
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<table border='1' cellpadding='0' cellspacing='0' width='90%'>


<tr bgcolor="#88D4D8">
	<th>Product Name</th>
	<th>Sub Product Name</th>
	<th>Edit</th>
	<th>Delete</th>

</tr>

   
		<s:iterator  value="SubProductname">
			
			<tr> 
				 <td> ${PRODUCT_TYPE} </td>  	
		         <td> ${PRODUCT_DESC} </td> 
		         <td align="center"><form action="EditSubProduct.do" method="post" autocomplete="off"><input type="image"  src="images/edit.png" alt="submit Button"><input type="hidden" name="productid" value="${PRODUCT_CODE}"></form></td>
				 <td align="center"><form action="DeleteSubProduct.do" method="post" autocomplete="off"><input type="image"  src="images/delete.png" alt="submit Button"><input type="hidden" name="productid" value="${PRODUCT_CODE}"></form></td>
			</tr>
			  	
		</s:iterator>
	
	

</table>


</body>
</html>