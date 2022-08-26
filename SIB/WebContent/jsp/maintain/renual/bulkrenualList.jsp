<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>
<style>
 
</style>    
<div align="center">
<s:form action="changeCardRenualBulkRenual"  name="cardgen"  autocomplete = "off"  namespace="/">
<s:if test="%{!bulkrenualList.isEmpty()}"> 
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th style='text-align:center;'> Order Ref No </th>
						<th  style='text-align:center;'>Card no</th>
						<th  style='text-align:center;'> Emb Name </th>
						  <th  style='text-align:center;'> Bin </th> 
						<th style='text-align:center;'> Ordered Date </th>
						<th style='text-align:center;'> Ordered by </th>
						<th style='text-align:center;'> Expired in </th>
						
						
						<th style='text-align:center;'> Status </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %>
				<%-- <s:textfield value="%{cardgentype}" name = "cardgentype" />  
				<s:textfield value="%{productcode}" name = "productcode" />   --%>
				     
				<s:iterator value="bulkrenualList">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<s:if test='padssenabled=="Y"' >
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{HCARD_NO}"/>  </td>
						</s:if>
						<s:else>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{CARD_NO}"/>  </td>
						</s:else>
											<td> ${ORDER_REF_NO}   </td>
						<td> ${MCARD_NO} </td>
						<td> ${EMB_NAME}</td>
						<td> ${BIN}  </td> 
						<td> ${ GENDATE }</td> 
					    <td> ${ USERNAME }    </td>
					    <td> ${EXPIRY_COUNT_DAYS} days    </td>
					    
					    <td> ${ STATUS_DESC }      </td>
					</tr>
					<input type="hidden" name="branchcode" id="branchcode" value="${BRANCH_CODE}">
					<input type="hidden" name="binno" id="binno" value="${BIN}">
					
					<input type="hidden" name="org_chn" id="org_chn" value="${ORG_CHN}">
					<input type="hidden" name="hcard_no" id="hcard_no" value="${HCARD_NO}">
					<input type="hidden" name="mcard_no" id="mcard_no" value="${MCARD_NO}">
					
					<s:hidden name="collectbranch"/> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Submit" name="authorize" id="authorize" onclick="return selectallvalidation();"/>
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
						</td>
					</tr>
	 	</table> 
</s:if>
<s:else>

<table border='0' cellpadding='0' cellspacing='0' width='80%'
	style='text-align: center;'>
	<tr>
		<td style='text-align: center;'>   
<font color="red"><b>	No Records Found .....</b></font>	
</td>
</tr>
</table> 

</s:else>	
</s:form>
</div>
 