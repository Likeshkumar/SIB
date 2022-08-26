<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script>
 	function getSubProd( instid, selprodid ){  
 		alert("instid==> "+instid);	
 		prodid = selprodid.split("~"); 
	 		var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid[0];  
	 		result = AjaxReturnValue(url).split("~");
	 		 
	 		subprodlist = result[0];
	 		maxallcard = result[1];
	 		document.getElementById("subproductlist").innerHTML = result; 
	 		 
	 		
 	}
 	function maxAllowedCards( instid, subprodid ){
 		var bin = document.getElementById("cardtype").value;
 		if( bin != "-1~-1"){
 			var binval = bin.split("~");
 		var url = "getMaxCardCount.do?instid="+instid+"&subprodid="+subprodid+"&bin="+binval[0];
 		document.getElementById("count").value ="";
 		var result = AjaxReturnValue(url);
 		 
 		document.getElementById("maxallowedcard").value = result;
 		}else{
 			alert("select bin");
 		}
 	}
 	function checkMaxcards( givenmaxcards ){
 		var maxallcards = 1;
 		if ( document.getElementById("maxallowedcard").value != "" ){
 		 		maxallcards = document.getElementById("maxallowedcard").value;
 		} 
 		 
 		
 		if( parseInt (givenmaxcards) > parseInt(maxallcards) ){ 
 			document.getElementById("order").disabled=true; 			
 			errMessage(maxallcards, "Maximum No.of cards allowed " + maxallcards );		
 			
 		}else{
 			document.getElementById("order").disabled=false;
 		}
 	}
 </script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String acttype = (String)session.getAttribute("act_type");
	System.out.println("acttype===> "+acttype);
	String btnval=null;
	if( acttype.equals("edit")){
		btnval = "Update";
	}else if( acttype.equals("order")){
		btnval = "Order";
	}
%>
<div align="center">
<s:form action="updateProcessedOrderInstCardorderAction"  name="orderform"  autocomplete = "off"  namespace="/">
<table border="0" cellpadding="0" cellspacing="0" width="40%" >	
<s:iterator value="editcarddetails">
	<tr><td><input type="hidden" name="orderref" id="orderref" value="${ORDER_REF_NO}"></td></tr>
	
	<s:set name="PRODUCTCODE" >${PRODUCT_CODE}</s:set>
	
	<% 	if(usertype.equals("INSTADMIN"))
		{
	%>
		<tr>
				<td> Select Branch</td>
				<td> :&nbsp;<s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1" value="%{BRANCH_CODE}" tooltip="Select Branch"/>
				
				</td>
			</tr>
			
	<%	
		}
		else
		{
	%>
			<tr>
			<td class="fnt"> Branch</td>
			<td> <s:textfield name="temp" readonly="true" value="%{#session.BRANCH_DESC}"/></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
	<%		
		}
	%>
    
	<tr>
		<td>
		  Card 
		</td>
		<td> :
				
 				<select name="cardtype" id="cardtype" onchange="getSubProd( '<%= instid %>', this.value );">
 				<option value="-1~-1">--Select -- </option>
 				<s:iterator  value="prodlist">
 				<s:set name="prod_id">${PRODUCT_CODE}</s:set>
 				<option value="${PRODUCT_CODE}"
 				
 				<s:if test="%{#PRODUCTCODE==#prod_id}">
 				 selected="${PRODUCT_CODE}"
 				</s:if>
 				
 				
 				>${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				
 				</select>
		</td>
    </tr>
    	
    <tr>
    </tr>
    <tr> 
    	<td id="subname">
    		 Product
    	</td>
    	<td> :
    		 
    			<select name="subproduct" id="subproductlist">
    				<option value="-1">--Select Sub-Product--</option>
    				<option value="${SUB_PROD_ID}" selected="${SUB_PROD_ID}">${SUBPRODDESC}</option>
    			</select>
    	 
    	</td>
    </tr>	
    	
    <tr>	
		<td> 
		No. Of Cards
		</td>
		<td> : <input type="hidden" name="Ordertype" value="I">
			<input type="hidden" name="maxallowedcard" id="maxallowedcard" />
   	   		<input type="text" name="count" id="count"    value="${CARD_QUANTITY}" maxlength="5" onKeyPress="return numerals(event);"/>
		</td>
	</tr>
	</s:iterator> 
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Update" name="order" id="order" onclick="return validateinstorder()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
</div>
 