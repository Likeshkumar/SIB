<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<div align="center">
<s:form action="generateInstCardInstCardProcessAction"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   <input type="hidden" name="productcode" value="<%= request.getParameter("cardtype") %>" />
	<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
					<tr>
						<th> Sl no </th>
						<th> Order Ref No </th>
						<th>No.of Cards</th>
						<th> Emb Name </th>
						<th> Card type </th>
						<th> Product Bin </th>
						<th> Card Ordered Date </th>
						<th> Card Ordered by </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="instorderlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td><a href="editcarddetailsInstCardorderAction.do?refname=${ORDER_REF_NO}"> ${ORDER_REF_NO} </a> </td>
						<td> ${CARD_QUANTITY}  </td>
						<td> ${EMBOSSING_NAME} </td>
						<td> ${CARDTYPEDESC}   </td>
						<td> ${ PRODBINDESC }  </td>
						<td> ${ ORDERED_DATE }</td> 
					    <td> ${ USERNAME }</td> 
					</tr> 
			 	</s:iterator>

	 	</table> 
	 	
	 	

 
</s:form>
</div>
 