<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>

<div align="center">
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
					<tr>
						<th> Sl no </th>
						<!-- <th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th> -->
						<th> Order Ref No </th>
						<th>No.of Cards</th>
						<th> Embossing Name </th>
						<th> Product Name </th>
						<th> Ordered Date </th>
						<th> Status </th>
						<th> Remarks </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="perscardgenlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<!-- <td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"></td> -->
						<td><a href="personalCardetailsPersonalCardOrderAction.do?refname=${ORDER_REF_NO}"> ${ORDER_REF_NO} </a> </td>
						<td> ${CARD_QUANTITY}  </td>
						<td> ${EMBOSSING_NAME} </td>
						<td> ${ PRODBINDESC }  </td>
						<td> ${ ORDERED_DATE }</td> 
					    <td> ${ STATUS }</td>
					     <td> ${ REMARKS }</td>
					    <!--</s:checkbox>-->
					</tr>
					
    			 </s:iterator>
			 		
	 	</table> 
	

</div>
 