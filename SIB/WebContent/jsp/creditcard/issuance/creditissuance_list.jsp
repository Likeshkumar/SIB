<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<jsp:include page="/displayresult.jsp"></jsp:include>
<script>
	function validateCurrentForm(){
		var btnchecked = false;
		var btnlen = document.getElementsByName("cardnumber").length;
		 
		for( var i=1; i<=btnlen; i++ ){
			var cardid = document.getElementById("orderrefnum"+i);  
			 if( cardid.checked  ){
				 btnchecked = true;
				 break;
			 } 
		}
		
		if( !btnchecked ){
			errMessage(cardid, "Select any Card....");
			return false;
		}
	 
		 
	}
</script>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<div align="center">
<s:form action="%{crissuebean.formaction}"  name="orderform"  autocomplete = "off"  namespace="/">
   
	<table width="100%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  > 
				 
					<tr>
						<th> Sl no </th>
						<th>  Select </th>
						<th>Card No</th>
						<th> Order Ref No </th> 
						<th> Emb Name </th>
						<th> Card type </th>
						<th> Product Bin </th>
						<th> Registered Date </th> 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="crissuebean.viewcardlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td> 
						<td> <input  type="radio" name="cardnumber"  id="orderrefnum<%=rowcount%>"  value="${CARD_NO}"/>  </td>
						<td> ${CARD_NO}  </td>
						<td> ${ORDER_REF_NO}  </td> 
						<td> ${EMB_NAME} </td>
						<td> ${CARDTYPEDESC}   </td>
						<td> ${ PRODBINDESC }  </td>
						<td> ${ ORDERED_DATE }</td>  
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Submit" name="submit" id="submit" onclick="return validateCurrentForm();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							 
				 
						</td>
					</tr>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 