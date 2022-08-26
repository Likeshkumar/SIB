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
<s:form>
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
					<thead>				
					<tr>					
						<th>CARD NUM </th>
						<th>ORDER REF NO</th>
						<th>PRODUCT NAME</th>
						<th>EMB NAME</th>
						<th>PERSONALIZED FILE NAME </th>
						<th>ISSUED TYPE </th>
						<th>CARD STATUS</th>
					</tr>
					</thead>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="findcardreceivedlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt;%>
					>
						<td> ${CARD_NO}  </td>
						<td>${ORDER_REF_NO}</td>
						<td> ${PRODUCT_DESC}  </td>
						<td> ${EMB_NAME}  </td>
						<td> ${PRENAME}  </td>
						<td> ${ISSUEDTYPE}  </td>
						<td> ${STATUS_DESC}   </td>
					</tr> 
			 	</s:iterator> 
</table> 	 	
<table border=0 cellpadding=0 cellspacing=0 width=20%>
	<tr>
	<td colspan="10">
		<input type="button" value="Back" name="submit" id="submit" onclick="goBack()"/>
		</td>
	</tr>
</table>
	 	

 
</s:form>
</div>
 