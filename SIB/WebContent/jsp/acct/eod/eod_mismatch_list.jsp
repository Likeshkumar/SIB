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
<s:form action="authourizeInstCardInstCardProcessAction"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
 
    
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  > 
					 
				
				 
				<s:iterator value="eodbean.subglamountlist">
				<table width="90%" cellpadding="0"  border="0"  cellspacing="0"   class="formtable" style="text-align:left">
					<tr>
						<th>Sub-Gl Code </th> <th> : ${SCH_CODE} </th> <th>Sub-Gl Description </th> <th> : ${SCH_NAME} </th>
						<th> &nbsp; </th><th> &nbsp; </th>
					</tr> 
				 
					<s:iterator value="SUBGLAMT">
						<tr>
						 <td>Sum-of Debit Amt</td> <td> : ${DR_AMT} </td>
						 <td>Sum-of Credit Amt</td> <td> : ${CR_AMT} </td>
						 <td>Sum-of Total Amt</td> <td> : ${TOTAL_AMT} </td>
						</tr> 
					</s:iterator> 
				 
					<s:if test='ISSUBGLTXN == "1"'>
					
					<table border='0' cellspacing='0' cellpadding='0' width='90%'  class="formtable"  >
						<tr>
						<td><b>Card Number</b></td> <td><b>Txn-Desc</b></td>  <td> <b> Transactino Date </b> </td> <td> <b> TxnTime </b> </td> <td> <b> Terminal id </b> </td>
						 <td> <b> Terminal Location</b> </td> <td> <b> Merchant id </b> </td> <td> <b> Txn Amt </b> </td> <td> <b> Txn Type </b> </td>
						</tr> 
						<% int rowcnt = 0; Boolean alt=true; %> 
						<s:iterator value="SUBGLTXN">
							
							<tr
							<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
							>
							<td>${CHN}</td> <td> ${TXNDESC} </td> <td> ${TRANDATE} </td> <td> ${TRANTIME} </td> <td>  ${TERMINALID} </td>  <td> ${TERMLOC} </td> 
							<td> ${MERCHANTID}  </td> <td> ${TXNAMOUNT}  </td> <td> ${OPTYPE} </td>
						</tr>  
						</s:iterator>
					</table> 
					 
					<table border='0' cellspacing='0' cellpadding='0' width='40%'  class="formtable"  > 
						
							<tr> 
								<s:iterator value="SUBGLTXNSUM"> 
									<td>Transaction Sum of ${OPTYPE} </td> <td> : ${TXNAMOUNT} </td>  
								</s:iterator>
					</table> 
					
					
					</s:if>
					<tr> <td> &nbsp; </td> </tr>
				</table> 
			 	</s:iterator>
			 	 
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 