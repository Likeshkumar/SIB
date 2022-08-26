<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>

<link rel="stylesheet" href="style/sort/sortable.css" />
<script type="text/javascript" src="js/jquery/jquery_1.9.1.js"></script> 
<script type="text/javascript" src="js/jquery/jquery_ui.js"></script> 
 <style>
#sortable { list-style-type: none; margin: 0 auto; padding: 0 auto;  text-align:center; display: block; }
#sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 11px; height: 18px; width:300px; text-align:left }
#sortable li span { position: absolute; margin-left: -1.3em; }
</style>
<script>
	$(function() {
	$( "#sortable" ).sortable();
	$( "#sortable" ).disableSelection();
	}); 

	function getSubProductList(  prodid ){
		var instid = document.getElementById("instid").value;
		 
		var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid; 
		var result = AjaxReturnValue(url);  	
		document.getElementById("subprodid").innerHTML = result;
		
	}
	
	function validateAcctRules(){
		var recordid = document.getElementById("recordid");
		var rulename = document.getElementById("acctrulename");
		var product = document.getElementById("product");
		var subproduct = document.getElementById("subprodid");
		var msgtype = document.getElementById("msgtypeid");
		var respcode = document.getElementById("respcode");
		var txncode = document.getElementById("txncode");
		var orginchannel = document.getElementById("orginchannel");
		var devicetype = document.getElementById("devicetype");
		var fintxn = document.getElementById("fintxn");
		var txncatorogry = document.getElementById("txncatorogry");
		var revstype = document.getElementById("revstype");
		var mappingtxncode = document.getElementById("mappingtxncode");
		
		
		if( recordid ){
			if( trim(recordid.value) == ""){
				errMessage(recordid, "Enter Rule Name");
				return false;
			}
		}
		
		if( rulename ){
			if( trim(rulename.value) == ""){
				errMessage(rulename, "Enter the GL Rule Name");
				return false;
			}
		}
		
		if( product ){
			if( product.value == "-1"){
				errMessage(product, "Select the Product");
				return false;
			}
		}
		
		if( subproduct ){
			if( subproduct.value == "-1"){
				errMessage(subproduct, "Select the Sub Product");
				return false;
			}
		}
		
		 
		if( msgtype ){
			if( msgtype.value == "-1"){
				errMessage(msgtype, "Select the Msg type");
				return false;
			}
		}
		
		
		if( respcode ){
			if( respcode.value == "-1"){
				errMessage(respcode, "Select the Response Code") ;
				return false;
			}
		}
		
		
		if( txncode ){
			if( txncode.value == "-1"){
				errMessage(txncode, "Select the Txn Code ");
				return false;
			}
		}
		
		
		if( orginchannel ){
			if( orginchannel.value == "-1"){
				errMessage(orginchannel, "Select the Orgin Channel");
				return false;
			}
		}
		
		
		if( devicetype ){
			if( devicetype.value == "-1"){
				errMessage(devicetype, "Select the Device type ");
				return false;
			}
		} 
		 
		if( fintxn ){
			if( fintxn.value == "-1"){
				errMessage(fintxn, "Select the Financial Transaction ");
				return false;
			}
			if(fintxn.value == "Y"){
				if(txncatorogry){
					if(txncatorogry.value=="-1"){
						errMessage(fintxn, "Select the Transaction Catagory");
						return false;
					}
					if(txncatorogry.value == "REVTXN"){
							if(revstype){
								if(revstype.value == "-1"){
									errMessage(revstype, "Select the Reversal Type");
									return false;									
								}
							}
					}					
				}
			}
		} 
/* 		if(mappingtxncode){
			if(mappingtxncode.value == "-1"){
				errMessage(mappingtxncode,"Select Mapping Txn code");
				return false;
			}
		}
		var auth_code = document.getElementsByName("authcode");
		alert(auth_code.length);
		for(var j=0;j <= auth_code.length;j++){
			var auth =	document.getElementById("authcode"+j);
			alert("auth --> "+auth.value);
			if(auth.checked){
				alert("checked"); 
				return false;
			}
		} */
		
		parent.showprocessing();
		return true;
		
	}
	
	function setFinancialTxn( fintxn ){
		if ( fintxn == "Y" ){
			document.getElementById("txncatorogry").disabled=false;
		}else{
			document.getElementById("txncatorogry").disabled=true;
			document.getElementById("revstype").disabled=true;
		}
	}
	
	function enableReversaltype ( revvalue ){ 
		if ( revvalue == "REVTXN" ){
			document.getElementById("revstype").disabled=false;
		}else{
			document.getElementById("revstype").disabled=true;
		}
		
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname"); 
 
%>

<s:form action="saveAccountRuleAcctRule.do" name="orderform"
	onsubmit="return validateAcctRules()" autocomplete="off" namespace="/">

		<table border="0" cellpadding="0" cellspacing="4" width="20%">
		<tr>
			<td><s:submit value="Submit" name="order" id="order"
					onclick="return validFilter()" /></td>
			<td><input type="button" name="cancel" id="cancel"
				value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

			</td>
		</tr>
	</table>
	
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="formtable">
	<tr>
		<td>Rule Name </td>
		<td>:
			 <s:textfield name='recordid' id='recordid'
					maxlength="32" />
		 </td>	
	</tr>
	<tr>	
		<td>GL Rule Name </td>
			<td>: <s:textfield name='acctrulename' id='acctrulename'
					maxlength="32" />
			</td>
			<td>Product</td>
			<td>: <s:select list="%{accrulebean.prodlist}" id="product" name="product"
					listKey="PRODUCT_CODE" listValue="CARD_TYPE_NAME" headerKey="*"
					headerValue="*" onchange="getSubProductList( this.value)" />
			</td>
			<td><s:property value="accrulebean.subproductlistbean" /> Sub Product</td>
			<td>: <select name="subprodid" id="subprodid">
					<option value="-1">--Select --</option>
					<option value="*"> * </option> 
			</select>
	</tr>
	
	<tr>
			<td>Txn Code</td>
			<td>: <s:select list="%{accrulebean.txncodelist}" id="txncode" name="txncode"
					listKey="TXN_CODE" listValue="ACTION_DESC" headerKey="-1"
					headerValue="-SELECT-" />

			</td>
			
			<td>Message Type</td>
			<td>: <s:select list="%{accrulebean.msgtypelist}" id="msgtypeid"
					name="msgtypeid" listKey="MSGTYPE" listValue="MSGTYPE"
					headerKey="-1" headerValue="-SELECT-" />

			</td>

			<td>Response Code</td>
			<td>: <s:select list="%{accrulebean.respcodelist}" id="respcode" name="respcode"
					listKey="RESPCODE" listValue="RESPCODE" headerKey="-1"
					headerValue="-SELECT-" />

			</td> 
		</tr> 
		
		<tr> 
			<td>Orgin Channel</td>
			<td>: <s:select list="%{accrulebean.orginchannel_list}" id="orginchannel"
					name="orginchannel" listKey="ORGIN_CHANNEL"
					listValue="ORGIN_CHANNEL" headerKey="-1" headerValue="-SELECT-" /> 
			</td>
			<td>Device Type</td>
			<td>: <s:select list="%{accrulebean.devicetypelist}" id="devicetype"
					name="devicetype" listKey="DEVICETYPECODE" listValue="DEVICETYPE"
					headerKey="-1" headerValue="-SELECT-" /> 
			</td>
			<td>Financial Transaction</td>
			<td>: 
				  <select name="fintxn" id="fintxn" onchange="setFinancialTxn(this.value)" >
					<option value="-1">--Select --</option>
					<option value="Y"> YES </option>
					<option value="N"> N0 </option>
			</select> 
			</td>
		</tr> 
		<tr>
			<td>Transaction Category</td>
			<td>:  <select name="txncatorogry" id="txncatorogry" disabled=true onchange="enableReversaltype(this.value)">
					<option value="-1">--Select --</option>
					<option value="NEWTXN"> New Transaction </option>
					<option value="REVTXN"> Reversal Transaction </option>
			</select> 
			</td>
			
			<td>Reversal Type</td>
			<td>:  <select name="revstype" id="revstype" disabled=true>
					<option value="-1">--Select --</option>
					<option value="FULL"> Full Reversal </option>
					<option value="PART"> Partial Reversal </option>
			</select> 
			</td>
		</tr>
		
		<tr> 
		 
		 	<td> AUTH LIST  </td>
		 	<td style="text-align:center;"  align="center"> 
	 		<ul id="sortable">
	 		
	 	 	<% int x=0; %>
	 		<s:iterator value="accrulebean.authlist">
	 			<% x++; %> 
	 			 <li class="ui-state-default" > <input type='checkbox' name="authcode" value="${AUTH_ID}" id="authcode<%=x%>"  />  ${AUTH_NAME}   </li> 
	 		</s:iterator> 
			 
			</ul>
			
			<small style='font-size:9px'>* Drag the field up for Make Order Wise</small>
	 	  </td> 
	 	  
	 	  <td valign="top">Mapping Txn Code</td>
			<td valign="top">: <s:select list="%{accrulebean.txncodelist}" id="mappingtxncode" name="mappingtxncode"		listKey="TXN_CODE" listValue="ACTION_DESC" headerKey="*"
					headerValue="*" />

			</td>
	 	
		 </tr>
		
		
	</table> 

</s:form>

