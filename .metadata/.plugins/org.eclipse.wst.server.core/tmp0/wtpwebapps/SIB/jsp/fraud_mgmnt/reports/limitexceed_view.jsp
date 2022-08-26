 
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
 
<s:form action="#"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				 
					 
					<thead>
					<tr>
						<th> Sl no </th> 
						<th> Card No. </th>
						<th> Txn Date </th>
						<th>Time</th>
						<th> Ref No </th>
						<th> Txn Desc  </th>
						<th> Amt  </th> 
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="limitexceedlist">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						
						<td> ${CARD_NO}  </td>
						<td> ${TRAN_DATE}  </td>
						<td> ${TRAN_TIME}  </td>
						<td> ${REF_NO} </td>
						<td> ${TXN_CODE}   </td>
						<td> ${ TRANSACTION_AMOUNT }  </td>
					 
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	 
</div>
</s:form>
 