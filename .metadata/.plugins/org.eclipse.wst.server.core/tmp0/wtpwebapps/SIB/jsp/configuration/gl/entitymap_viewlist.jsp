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
<s:form action="authInstCustomerInstCardRegisterProcess"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   
	<table width="100%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				 
					<input type="hidden" name="bin" value="<%= request.getParameter("cardtype") %>" />
					<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
					<tr>
						<th > Sl no </th> 
						<th style="text-align:left" >Entity Type</th>
						<th style="text-align:left" > Entity Id </th> 
						<th style="text-align:left" > Card No. </th>
						<th style="text-align:left" > Customer Name </th>
						<th> Added Date  </th>
						<th style="text-align:left" > Added By </th> 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<%-- <s:property value="glbeans.glkeyslist"/> --%>
				<s:iterator value="glbeans.glkeyslist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<%--<td> <s:checkbox name="instorderrefnum"  id="orderrefnum<%=rowcount%>" fieldValue="%{CARD_NO}"/>  </td> --%> 
						<td style="text-align:left"> ${ENTITY_TYPE}  </td>
						<td style="text-align:left" > ${ENTITY_ID}  </td> 
						<td style="text-align:left" > ${CARDNO} </td>
						<td style="text-align:left" > ${CUSTOMERNAME} </td>
						<td> ${ADDED_DATE}   </td>
						<td style="text-align:left" > ${ ADDED_BY }  </td> 
					</tr> 
			 	</s:iterator>
			 		<%-- <tr>
						<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidation();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							 
				 
						</td>
					</tr> --%>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 