<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/ordervalidation.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>

<div align="center">
<s:form action="confirmDamagecardreceivedDamageCardreceiveissueAction"  name="authrizeorder"  autocomplete = "off"  namespace="/">

	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th>No.of Cards</th>
						<th> Generated Date </th>
						<!-- <th> Card type </th> -->
						<th> Product Name </th>
						<th> Generated by </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="damagereceivelist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${COUNT}  </td>
						<td> ${GENDATE} </td>
						<!-- <td> ${CARDTYPEDESC}   </td> -->
						<td> ${ PNAME }  </td>
					    <td> ${ USERNAME }</td> 
					</tr> 
					<input type="hidden" name="binno" id="binno" value="${BIN}"> 
			 	</s:iterator>
			 	
			 		<tr>
						<td colspan="8">
							<s:submit name="authorize" id="authorize" value=" Process " onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
						</td>
					</tr>
	 	</table> 
	
</s:form>
</div>
 