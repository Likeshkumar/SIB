<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/ordervalidation.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>

<div align="center">
<s:form action="orderAuthdeauthPersonalCardOrderAction"  name="authrizeorder"  autocomplete = "off"  namespace="/">

	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th>No.of Cards</th>
						<th> Emb Name </th>
						<!-- <th> Card type </th> -->
						<th> Product Name </th>
						<th> Ordered Date </th>
						<th> Ordered by </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="persauthorderlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${CARD_QUANTITY}  </td>
						<td> ${EMBOSSING_NAME} </td>
						<!-- <td> ${CARDTYPEDESC}   </td> -->
						<td> ${ PRODBINDESC }  </td>
						<td> ${ ORDERED_DATE }</td> 
					    <td> ${ USERNAME }</td> 
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Authorize" name="authorize" id="authorize" onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Reject" name="deauthorize" id="deauthorize" onclick="return selectallvalidation();"/>
				 
						</td>
					</tr>
	 	</table> 
	
</s:form>
</div>
 