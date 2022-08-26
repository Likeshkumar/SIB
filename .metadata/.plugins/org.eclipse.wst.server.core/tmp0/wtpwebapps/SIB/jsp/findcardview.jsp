<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<head>
<script type="text/javascript" src="js/script.js"></script> 
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>

<div id="fw_container">	
<s:form name="diputeraiseform" action="#"  autocomplete="off" namespace="/" method="post" >
	<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
		
		<thead>
			<tr>
				<th>Registration Type</th>
				<th>Card No</th>
				<th>Name</th>
				<th>Product</th>
				<th>Reg Date</th>
				<th>PIN Date</th>
				<th>PRE</th>
				<th>PRE Date</th>
				<th>Issued Type</th>
				<th>Status</th>
				
			</tr>	
		</thead>
		<s:iterator value="carddetailslist">
			<tr>
				<td>${REGTYPE}</td>
				<td>${MCARD_NO}</td>
				<td>${EMB_NAME}</td>
				<td>${PRODUCT}</td>
				<td>${REG_DATE}</td>
				<td>${PIN_DATE}</td>
				<td>${PREFILE}</td>
				<td>${PRE_DATE}</td>
				<td>${ISSUEDTYPE}</td>
				<td>${CARD_STATUS}</td>
			</tr>
		</s:iterator>
	</table>
	<table>
	
		<!-- <input type="hidden" name="token" id="csrfToken"value="123"> -->  
		<tr><td><td><input type="button" value="Back"  name="submit" id="submit" onclick="goBack()" /></td></tr>
	</table> 
</s:form>
</div>
</body>
</html>