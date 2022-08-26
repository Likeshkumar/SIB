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
<s:form action="activateInstantCardActionHomeInstCardActivateProcess.do"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   <s:hidden name="defloadamt" id="defloadamt" value="%{defaultloadamtbean}" />
   <input type="hidden" name="bin" value="<%= request.getParameter("cardtype") %>" />
	<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
	<input type="hidden" name="activationtype" value="ORDERBASED" />
	<s:hidden name="curamountfield" value="%{curamountfieldbean}" /> 
	<s:hidden name="feeamountfield" value="%{feeamountbean}" />
	
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  > 
					<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th>Card No</th>  
						<th> Received Date </th> 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="activatebeans.orderedcardlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						 
						<td> <input type="checkbox" name="cardnolist"  id="orderrefnum<%=rowcount%>"  value="${CARD_NO}"/>  </td>
						<td> ${CARD_NO}  </td> 
						<td> ${ RECV_DATE }</td>  
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							 
				 
						</td>
					</tr>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 