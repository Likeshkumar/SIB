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
<s:if test="%{resultstatus=='N'}">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td align="center">
						<b style="color: Red;width: 15px">Please Configure The Limits</b>
				</td>
			</tr>
		</table>
	</s:if>
<s:else>
<table border="1" cellspacing="0" cellpadding="0" width="80%" align="center" >
	<tr bgcolor="#88D4D8">
		<th>BIN</th>
		<th>Product Name</th>
		<th>Maximum Amount</th>
		<th>No. of Reload <br>/ Day</th>
		<th>Daily Transaction <br>amount</th>
		<th>Daily Transaction <br>Count</th>
		<th>Edit Limit</th>
		<th>View Limit</th>
	</tr>
	<s:iterator  value="Limitname" >
	    <tr>
			<td>${BIN}</td>
			<td>${PRODUCT_NAME}</td>
			<td align="center">${MAXRELOAD}</td>
			<td align="center">${RELOADCOUNT}</td>
			<td align="center">${TXNAMT}</td>
			<td align="center">${TXNCNT}</td>
		    <td align="center">
		    	<form action="editLimitdetailsAddLimitAction.do" autocomplete="off" method="post">
		    		<input type="image"  src="images/edit.png" alt="submit Button">
		    		<input type="hidden" name="limitid" value="${LIMIT_ID}" >
		    	</form>
		 	</td>
		 	 <td align="center">
		    	<form action="ViewEditLimit.do" autocomplete="off" method="post">
		    		<input type="image"  src="images/view.png" alt="submit Button">
		    		<input type="hidden" name="limitid" value="${LIMIT_ID}" >
		    	</form>
		 	</td>
	   </tr>
   </s:iterator>
</table>
</s:else>
</body>
</html>