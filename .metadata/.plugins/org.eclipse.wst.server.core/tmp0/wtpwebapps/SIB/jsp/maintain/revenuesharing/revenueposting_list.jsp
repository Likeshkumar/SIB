 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page autoFlush="true" %> 
<script>
	function validateinstorder(){
		if( !confirm("Do you want to continue....") ){
			return false;
		}
	}
</script>
 
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
 

 
<s:form action="revPostingActionRevenueSharing.do"  name="orderform" onsubmit="return validateinstorder()"  autocomplete = "off"  namespace="/">
<table border="0" width="70%" style="border:solid 1px gray;" >
		<tr>
			<td>Bank :  <s:property value="revbean.bindesc"/> 
				<s:hidden name="bincode" id="bincode"  value='%{revbean.bincode}' /> 
				<s:hidden name="curcode" id="curcode"  value='%{revbean.currency}' /> 
			</td>
			 	 
		</tr>
		<tr>
			<td colspan="2" style="text-align:center"><input type="button" class="cancelbtn" value="Back" onclick="goBack()" /></td>
		</tr>
</table>
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				  
					<thead>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th>  
						<th> Card Number </th>						  
						<th style='text-align:left' > Name </th>
						
						<s:if test="%{revbean.commissiontype=='$REV'}">
							<th  style='text-align:right'> Commission Amount </th>
						</s:if>
						<s:else>						 
							<th  style='text-align:right'> Expense Amount </th>
					  	</s:else>
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
			 
				<s:iterator value="revbean.commissionexpenselist">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name=cardlist  id="orderrefnum<%=rowcount%>"  value="${CARDNO}"/>  </td> 
						<td> ${ CARDNO }</td>
						<td style='text-align:left' > ${ CARDHOLDERNAME }</td>
						<s:if test="%{revbean.commissiontype=='$REV'}">
							<td  style='text-align:right'  > ${COMMISSIONAMOUNT}  </td>
						</s:if>
						<s:else>						 
							<td  style='text-align:right' > ${EXPENSEAMOUNT}  </td> 
					  	</s:else>   
						 
						
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	 	<table>
	 			<tr> 	
	 					<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="parent.showprocessing()"/> 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>  
						</td>
					</tr>
	 	</table>
</div>
</s:form>
 