<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<jsp:include page="/displayresult.jsp"></jsp:include>

<table width="90%" cellpadding="0"  cellspacing="0" border="1" >
	<s:iterator value="customerdetailslist">
		<tr>
			<td>Name</td><td>:${FULNAME}</td>
			<td>Father Name</td><td>:${FATHER_NAME}</td>
		</tr>
		<tr>
			<td>Mother Name</td><td>:${MOTHER_NAME}</td>
			<td>Marital Status</td><td>:${MARITAL_STATUS}</td>
		</tr>
		<tr>
			<td>Gender</td><td>:${GENDER}</td>
			<td>Date Of Birth</td><td>:${DOB}</td>
		</tr>
		<tr>
			<td>Nationality</td><td>:${NATION}</td>
			<td>Email Address</td><td>:${EMAIL_ADDRESS}</td>
		</tr>
		<tr>
			<td>Mobile No</td><td>:${MOBILE_NO}</td>
			<td>Phone No</td><td>:${PHONE_NO}</td>
		</tr>
			<tr><td>Occupation</td><td>:${OCCUPATION}</td>
			<td>Identity Number</td><td>:${ID_NUMBER}</td>
		</tr>
		<tr>
			<td>Identity Document</td><td>:${ID_DOCUMENT}</td>
			<td>Order Date</td><td>:${MAKER_DATE}</td>
		</tr>
		<tr><td>Postal Address</td><td>:${PONE}</td><td>Resident Address</td><td>:${RONE}</td></tr>
		<tr><td></td><td>:${PTWO}</td><td></td><td>:${RTWO}</td><tr>
		<tr><td></td><td>:${PTHREE}</td><td></td><td>:${RTHREE}</td></tr>
		<tr><td></td><td>:${PFOUR}</td><td></td><td>:${RFOUR}</td></tr>
		
	</s:iterator>
</table>
