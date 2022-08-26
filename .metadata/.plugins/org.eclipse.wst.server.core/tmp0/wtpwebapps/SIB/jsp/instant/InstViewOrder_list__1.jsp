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
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  > 
					 
					<tr>
						<th> Sl no </th> 
						<th> Order Ref No </th>
						<!-- <th>No.of Cards</th> -->
						<th> Emb Name </th> 
						<th> Product </th>
						<th> Branch </th>
						<th> Status </th>
						<th> Ordered Date </th>
						<th> Ordered by </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="instorderlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td> 
						<td> ${ORDER_REF_NO}  </td>
						<%-- <td> ${CARD_QUANTITY}  </td> --%>
						<td> ${EMBOSSING_NAME} </td> 
						<td> ${ PRODBINDESC }  </td>
						<td> ${ BRANCHDESC }  </td>
						<td> ${MKCK_STATUS} </td>
						
						<!-- <td>  
							<s:set name="org" >${MKCK_STATUS}</s:set>
							<s:if test='%{#org=="D"}'>Rejected</s:if>
							<s:elseif test='%{#org=="P"}'> Authorized </s:elseif>
							<s:else>Waiting for Auth</s:else>
						 </td> -->
						 
						<td> ${ ORDERED_DATE }</td> 
					    <td> ${ USERNAME }</td> 
					</tr> 
			 	</s:iterator> 
	 	</table> 
 
 
</div>
 