<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<head>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
</head>  
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC"); 
%>
<div align="center">
<s:form action="generateInstCardPinInstCardPinProcess"  name="orderform" autocomplete = "off"  namespace="/">   
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				 
					
					<tr>
						<th> Sl no </th>
						<th> <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th> Card NO </th>
						<th> Emb Name </th>
						<th> Card type </th>
						<th> Product Bin </th>
						<th> Authorized Date </th> 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="instorderlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<%--<td> <s:checkbox name="instorderrefnum"  id="orderrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"/>  </td> --%>
						<td> <input type="checkbox" name="instorderrefnum"  id="orderrefnum<%=rowcount%>"  value="${ORDER_REF_NO}"/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${EMB_NAME} </td>
						<td> ${CARDTYPEDESC}   </td>
						<td> ${ PRODBINDESC }  </td>
						<td> ${ ORDERED_DATE } </td>
						<s:hidden name='bin' value='%{ BIN }'/>  
						<s:hidden name='brch_code' value='%{BRANCH_CODE}'/>
						<s:hidden name='CARD_NO' value='%{CARD_NO}'/>
						<s:hidden name='generationtypeforonlycvv' value='YES'/>
					</tr> 
			 	</s:iterator>
			 		 
	 	</table> 
	 	
	 	<table border=0 cellpadding=0 cellspacing=0 width=20%>
	 	<tr>
			<td colspan="8">
				<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidation();" /> 
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							  
			</td>
		</tr>
	 	</table>
	 	
	 	<s:hidden name="cardtype_sel" id="cardtype_sel" value="%{cardtype_selected}"></s:hidden>

 
</s:form>
</div>
 