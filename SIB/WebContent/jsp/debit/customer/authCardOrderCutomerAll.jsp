<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<head>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<script>
function selectallvalidation(){
	//alert("welcome");
	valid=true;
	var orderref = null; 
	var orderreflen = document.getElementsByName("instorderrefnum");
	var oneselected = false; 
		for(var i=1;i<=orderreflen.length;i++)
		{ 
			var orderid = "orderrefnum"+i;
		 
			orderref = document.getElementById(orderid); 
			if(orderref.checked==true)
			{
				oneselected = true;	 
				break; 
			} 
		} 
		if(!oneselected)
			{
			errMessage(orderref,"Select any one Order");
			valid= false;
			return false;
			}
	//parent.showprocessing();
	return valid;
 }
</script>
</head>  
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="authorizeAllCardOrderDebitCustomerRegister.do" id="orderform" name="orderform" autocomplete = "off"  namespace="/">
<div align="center">   
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				 
					
					<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th> Account No</th>
						<th> Branch </th>
						<th> Name</th>
						<th> Acct Type </th>
						<!-- <th> Acct Sub Type </th> -->
						<th> Product </th>
						<th> Ordered Date </th>
						<th> Renewal </th>
						<th> Gen By </th>
						<th> Collect Branch </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="authorizeall">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<%--<td> <s:checkbox name="instorderrefnum"  id="orderrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"/>  </td> --%>
						<td> <input type="checkbox" name="instorderrefnum"  id="orderrefnum<%=rowcount%>"  value="${ORDER_REF_NO}"/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${ACCOUNT_NO}  </td>
						<td> ${BRANCH_CODE}  </td>
						<td> ${EMBOSSING_NAME}  </td>
						<td> ${ACCTTYPE_ID} </td>
						<%-- <td> ${ACCTSUB_TYPE_ID}   </td> --%>
						<td> ${PRODUCT_CODE}   </td>
						<td> ${ ORDERED_DATE }  </td>
						<td> ${ RENEWALFLAG }  </td>
						<td> ${ MAKER_ID }  </td>
						<td> ${ CARD_COLLECT_BRANCH }  </td>
					</tr> 
			 	</s:iterator>
			 	<s:hidden name="applicationid"/>	 
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

 </div>
</s:form>

 