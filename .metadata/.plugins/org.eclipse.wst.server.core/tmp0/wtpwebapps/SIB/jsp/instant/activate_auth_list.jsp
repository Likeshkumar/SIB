<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<script>
	function displayDetailedAmount( cardno ){
	//	alert(cardno);
	 	 
	 	 
		var wind = window.open('displayDetailedAmountInstCardActivateProcess.do?cardno='+cardno,'','left=350,top=150,width=500,height=200,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
		 
	}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<div align="center">
<s:form action="authActivateInstCardActionInstCardActivateProcess"   name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   
<table width="95%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  > 
		<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th>Card No</th>  
						<th> Card type </th>
						<th> Product </th>
						<th> Activated By </th>
						<th> Activated Date </th> 
						<th> Amount </th> 
					</tr>
		<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="activeauthlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="activecardno"  id="orderrefnum<%=rowcount%>" fieldValue="%{CARD_NO}"/>  </td>
						<td> <a href='#' onclick="displayDetailedAmount('${CARD_NO}')">${CARD_NO} </a> <input type='hidden' name='cardno' id='cardno' value='${CARD_NO}' /></td>
						<td> ${PROD_DESC}  </td>
						<td> ${SUB_PROD_DESC} </td>
						<td> ${USER_DESC}   </td>
						<td> ${ACTIVATED_DATE}  </td> 
					    <td> ${ LOADING_AMOUNT }  <input type='hidden' name='loadamt' id='loadamt' value='${LOADING_AMOUNT}' /> </td> 
					</tr> 
			 	</s:iterator>
					
		
		 
		<tr>	 <td colspan='10'>
		 	<table border="0" cellpadding="0" cellspacing="10" width="20%" >
					<tr>
					<td>
						<s:submit value="Authorize" name="order" id="order" onclick="return validFilter()"/>
					</td>
					<td>
						<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
						 
					</td>
					
					<td>
						<s:submit value="Reject" name="order" id="order" onclick="return validFilter()"/>
					</td>
					</tr>
			</table>
		 </td></tr>
	 
</table> 
 
</s:form>
</div>
 