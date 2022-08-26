<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<jsp:include page="/displayresult.jsp"></jsp:include>

<div align="center">
	<table border="0" cellpadding="0" cellspacing="0" width="45%">
		<tr align="center">
			<td>
	         	<form action="personalProductdetailsPersonalCardOrderAction.do" method="post" autocomplete="off">
	         		<input type="image"  src="images/newuserorder.jpg" alt="submit Button">
	         	</form>
	         </td>
			 <td align="center">
			 	<form action="kyCustomercardorderPersonalCardOrderAction.do" method="post" autocomplete="off">
			 		<input type="image"  src="images/kycorder.jpg" alt="submit Button">
			 	</form>
			 </td>		
		</tr>
	</table>

</div>