 
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 <script>
 
 function validation_authkey(){
	 	var reason = prompt('Enter the Reason for Reject?');
		 if( reason!=null ){
			 document.getElementById("remarks").value = reason;
			 return true;
		 }
		 return false;
	}
 
 </script>
   
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
<s:form action="InstconfirmCardissueordersInstCardRegisterProcess.do"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
<s:hidden name="remarks" id="remarks" value=""></s:hidden>   
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				 
					<input type="hidden" name="bin" value="<%= request.getParameter("cardtype") %>" />
					<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
					<thead>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th> 
						<th> Card No. </th>
						<th> Order Ref No. </th> 
						<th> Emb Name </th>						
						<th> Product  </th>
						<th> Cust Id  </th>
						<th>Account No</th>
						<th> MakerID  </th>
						<th>Cus Mapped Date </th> 
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cardregbean.instorderlist">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name="instorderrefnum"  id="orderrefnum<%=rowcount%>"  value="${ORG_CHN}"/>  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ORDER_REF_NO}  </td> 
						<td> ${EMB_NAME} </td> 
						<td> ${ PRODUCUT_DESC }  </td>
						<td> ${ CIN}  </td>
						<td>${accountno}</td>
						<td> ${ mkuser }  </td>
						<td> ${ISSUE_DATE }</td>  
					</tr> 
					
						<%-- <input type="hidden" name="ecard" id="ecard" value="${ORG_CHN}"> --%>
						<%-- <input type="mcard" name="mcard" id="orderno" value="${MCARD_NO}"> --%>
					
			 	</s:iterator> 
	 	</table>   
	 	
	 	<table>
	 			<tr>
						<td colspan="8">
							<s:submit value="Authorize" name="authorize" id="authorize" onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Reject" name="deauthorize" id="deauthorize" onclick="return validation_authkey();"/>
				 
						</td>
					</tr>
	 	</table>
</div>
</s:form>
 