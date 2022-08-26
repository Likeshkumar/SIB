<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script type="text/javascript"
	src="jsp/personalize/script/PersonalCardOrder.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<style>
</style>
<div align="center">
	<s:form action="personalCardgenerationprocessPersonalCardprocessAction"
		name="cardgen" autocomplete="off" namespace="/" onsubmit="parent.showprocessing()">

		<table width="90%" cellpadding="0" border="0" cellspacing="0"
			class="formtable">
			<tr>
				<th>Sl no</th>
				<th><input type='checkbox' onclick="checkedAll(this.form)"
					id="checkall" /></th>
				<th style='text-align: center;'>Order Ref No</th>
				<th style='text-align: center;'>No.of Cards</th>
				<th style='text-align: center;'>Emb Name</th>
				<th style='text-align: center;'>Bin</th>
				<th style='text-align: center;'>Product Name</th>
				<th style='text-align: center;'>Sub-Product Name</th>
				<th style='text-align: center;'>Ordered Date</th>
				<th style='text-align: center;'>Ordered by</th>
				<!-- <th style='text-align:center;'> Status </th> -->
			</tr>
			<% int rowcnt = 0; Boolean alt=true; %>
			<s:iterator value="perscardgenlist">
				<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
					<td><%= rowcount %></td>
					<td><s:checkbox name="personalrefnum"
							id="personalrefnum<%=rowcount%>" fieldValue="%{CIN}" /></td>
					<td>${ORDER_REF_NO}</td>
					<td>${CARD_QUANTITY}</td>
					<td>${EMBOSSING_NAME}</td>
					<td>${CARDTYPEDESC}</td>
					<td>${ PRODBINDESC }</td>
					<td>${ SUBPRODDESC }</td>
					<td>${ ORDERED_DATE }</td>
					<td>${ USERNAME }</td>
					<%-- <td> ${ STATUS }      </td> --%>
				</tr>
				<input type="hidden" name="branchcode" id="branchcode"
					value="${BRANCH_CODE}">
				<input type="hidden" name="binno" id="binno" value="${BIN}">
			</s:iterator>
			<tr>
				<td colspan="8"><s:submit value="Submit" name="authorize"
						id="authorize" onclick="return selectallvalidation();" /> <input
					type="button" name="cancel" id="cancel" class="cancelbtn"
					value="Cancel" onclick="return confirmCancel()" /></td>
			</tr>
		</table>

	</s:form>
</div>
