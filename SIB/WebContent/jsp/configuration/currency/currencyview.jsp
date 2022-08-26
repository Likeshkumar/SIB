<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
 <script type="text/javascript">
function ask_confirm()
{
	var c=confirm("Do You Want To Delete");
	return c;
}

function edit_confirm()
{
	var d=confirm("Do You Want To Continue");
	return d;
}
</script>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 

<div align="center">
<table align="center" border="0"  cellspacing="1" cellpadding="0" width="90%" class="formtable">
		<tr style="color: maroon;">
			<th>CURRENCY</th>
			<th>CODE</th>
			<th>STATUS</th>
			<s:if test="%{status=='view'}"><th>EDIT</th></s:if><s:elseif test="%{status=='auth'}"><th>AUTHORIZE</th></s:elseif>
			<!-- <th>DELETE</th> -->
			<th>AUTH STATUS</th>
			<th>AUTH BY</th>
			<th>ADDED BY</th>
			<th>REASON</th>
		</tr>
	<% int rowcnt = 0; Boolean alt=true; %>
<s:iterator  value="currencydetails" >
	<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
		<td style="text-align:center;">${CURRENCY_DESC}</td>
		<td align="center">${CURRENCY_CODE}</td>
		<td align="center">${CURRENCY_STATUS}</td>
		<td align="center">
		<s:if test="%{status=='view'}">
			<form action="editCurrencyCurrencyAction.do" method="post" > 
				<input type="hidden" name="currcode" id="currcode" value="${CURRENCY_CODE}"/>
				<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
				<!-- <input type="image"  src="images/edit.png" alt="submit Button" onclick="return edit_confirm();"> -->
				<input type="image"  src="images/edit.png" alt="submit Button">
			</form>
		</s:if><s:elseif test="%{status=='auth'}">
				<form action="authCurrencydetailsCurrencyAction.do" method="post" > 
				<input type="hidden" name="currcode" id="currcode" value="${CURRENCY_CODE}"/>
				<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
				<!-- Authorize/Reject<input type="image"  src="images/enable.gif" alt="submit Button" onclick="return edit_confirm();"> -->
				Authorize/Reject<input type="image"  src="images/enable.gif" alt="submit Button">
			</form>
		</s:elseif>
		</td>
		<td>${AUTH_CODE}</td>
		<td>${AUTH_BY}</td>
		<td>${ADDED_BY}</td>
		<td>${REMARKS}</td>
		<%-- <td align="center">
			<form action="deleteCurrencyCurrencyAction.do" method="post" > 
				<input type="hidden" name="currcode" id="currcode" value="${CURRENCY_CODE}"/>
				<input type="hidden" name="currdesc" id="currdesc" value="${CURRENCY_CODE}"/>
				<input type="image"  src="images/delete.png" alt="submit Button" onclick="return ask_confirm();">
			</form>
		</td> --%>
	</tr>
</s:iterator>
</table>
</div>
