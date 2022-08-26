<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>

<script>

	function getSubProductList(  prodid ){
		var instid = document.getElementById("instid").value;
		 
		var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid; 
		var result = AjaxReturnValue(url); 
		document.getElementById("subprodid").innerHTML = result;
		
	}
	
	function validateAcctRules(){
		var rulename = document.getElementById("acctrulename");
		var product = document.getElementById("product");
		var subproduct = document.getElementById("subprodid");
		var msgtype = document.getElementById("msgtypeid");
		var merchanttype = document.getElementById("merchanttype");
		var respcode = document.getElementById("respcode");
		var txncode = document.getElementById("txncode");
		var orginchannel = document.getElementById("orginchannel");
		var devicetype = document.getElementById("devicetype");
		var merchantcommission = document.getElementById("merchantcommission");
		var feeid = document.getElementById("feeid");
		var txnsrc = document.getElementById("txnsrc");
		if( rulename ){
			if( trim(rulename.value) == ""){
				errMessage(rulename, "Enter the Rule Name");
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
		
		if( merchanttype ){
			if( merchanttype.value == "-1"){
				errMessage(merchanttype, "Select the Merchant Description");
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
		
		if( txnsrc ){
			if( txnsrc.value == "-1"){
				errMessage(txnsrc, "Select the Txn Src ");
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
		if( merchantcommission ){
			if( merchantcommission.value == "-1"){
				errMessage(merchantcommission, "Select the merchant commission type ");
				return false;
			}
		} 
		if( feeid ){
			if( feeid.value == "-1"){
				errMessage(feeid, "Select the fee type ");
				return false;
			}
		}  
		return true;
		
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname"); 
 
%>

<s:form action="saveAcctRuleMerchantAccountRule.do" name="orderform"	onsubmit="return validateAcctRules()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="80%"
		align="center" class="filtertab">
		<input type="hidden" name="instid" id="instid" value="<%=instid%>" />
		<tr>
			<td>Message Type</td>
			<td>: <s:select list="%{msgtype}" id="msgtypeid"
					name="msgtypeid" listKey="MSGTYPE" listValue="MSGTYPE"
					headerKey="-1" headerValue="-SELECT-" />

			</td>

			<td>Response Code</td>
			<td>: <s:select list="%{respcode}" id="respcode" name="respcode"
					listKey="RESPCODE" listValue="RESPCODE" headerKey="-1"
					headerValue="-SELECT-" />

			</td>

		</tr>

		<tr>


			<td>Txn Code</td>
			<td>: <s:select list="%{txncode}" id="txncode" name="txncode"
					listKey="TXN_CODE" listValue="ACTION_DESC" headerKey="-1"
					headerValue="-SELECT-" />

			</td>


			<td>Merchant Description</td>
			<td>: <s:select list="%{merchanttypedesc}" id="merchanttype"
					name="merchanttype" listKey="MERCHANT_TYPE_ID"
					listValue="MERCHANT_TYPE_DESC" headerKey="-1" headerValue="-SELECT-" />

			</td>


		</tr>




		<tr>
			<td>Txn Source</td>
			<td>: <s:select list="%{orginchannel}" id="txnsrc"
					name="txnsrc" listKey="ORGIN_CHANNEL"
					listValue="ORGIN_CHANNEL" headerKey="-1" headerValue="-SELECT-" />

			</td>
			<td>Merchant Commision</td>
			<td>: <s:select list="%{commision}" id="merchantcommission"
					name="merchantcommission" listKey="COMMISSION_CODE"
					listValue="COMMISSION_DESC" headerKey="-1" headerValue="-SELECT-" />

			</td>
		</tr>

		<tr>
			<td>Fee</td>
			<td>: <s:select list="%{fee}" id="feeid"
					name="feeid" listKey="FEE_CODE"
					listValue="FEE_DESC" headerKey="-1" headerValue="-SELECT-" />

			</td>
			<td>Transaction Indicator</td>
			<td> : <s:radio id="txn_indicator"  name="txn_indicator" list="#{'0':'Old Txn','1':'New Txn'}"  value="'0'" ></s:radio></td>
		</tr>
		
		<tr>
			<td>Merchant Discount</td>
			<td>: <s:select list="%{discount}" id="discountcode"
					name="discountcode" listKey="DISCOUNT_CODE"
					listValue="DISCOUNT_DESC" headerKey="-1" headerValue="-SELECT-" />

			</td>
		</tr>
	</table>


	<table border="0" cellpadding="0" cellspacing="4" width="20%">
		<tr>
			<td><s:submit value="Submit" name="order" id="order"
					onclick="return validFilter()" /></td>
			<td><input type="button" name="cancel" id="cancel"
				value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

			</td>
		</tr>
	</table>
</s:form>

