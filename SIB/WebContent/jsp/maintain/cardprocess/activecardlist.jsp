 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
<s:form action="cardActivationActionCardProcess.do"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
<table border='0' cellpadding='0' cellspacing='0' width='80%' class='viewtable'>
<s:iterator value="courierlist">
	<tr>
		<td> Courier Name : </td> <td> ${COURIER_NAME}</td>
		<td> Courier Id : </td> <td> ${COURIERID}</td>
		<td> Courier Date : </td> <td> ${COURIERDATE}</td>
	</tr>
	
	<tr>
		<td> Number of Cards Sent  : </td> <td> ${NOOFCARDS}</td>
		<td> Waiting For Activation : </td> <td> ${PENDINGCARDS}</td>
		<td> To Address/Agent : </td> <td> ${TOADDRESS}</td>
	</tr>
	
</s:iterator>
</table>
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
<s:hidden name="courierid" id="courierid" value="%{courierrefid}"/>
				 
					 
					<thead>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th> 
						<th> Card No. </th>
						<th> Order Ref No. </th> 
						<th> Emb Name </th>
						<th> Card Status </th>    
						<th> Issued Date </th> 
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
					 
				<s:iterator value="cardlist" var ="card"> 
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<s:if test='%{#card.CARD_STATUS == "09"}'>
							<td> <input type="checkbox" name="instorderrefnum"  id="orderrefnum<%=rowcount%>"  value="${CARD_NO}"/>  </td>
						</s:if>
						<s:else>
							<td></td>
						</s:else>
						
						<td> ${CARD_NO}  </td>
						<td> ${ORDER_REF_NO}  </td> 
						<td> ${EMB_NAME} </td>
						<td> ${CARD_STATUSDESC} </td>
						<td> ${ISSUE_DATE}   </td> 
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	 	<table>
	 			<tr> 	
	 					<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidation();"/> 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/> 
						</td>
					</tr>
	 	</table>
</div>
</s:form>
 