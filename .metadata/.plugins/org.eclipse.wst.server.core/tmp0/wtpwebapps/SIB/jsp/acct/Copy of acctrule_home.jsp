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
		var respcode = document.getElementById("respcode");
		var txncode = document.getElementById("txncode");
		var orginchannel = document.getElementById("orginchannel");
		var devicetype = document.getElementById("devicetype");
		
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
		 
		return true;
		
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname"); 
 
%>

<s:form action="saveAccountRuleAcctRule.do" name="orderform"
	onsubmit="return validateAcctRules()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="80%"
		align="center" class="filtertab">
		<input type="hidden" name="instid" id="instid" value="<%=instid%>" />

		<tr>
			<td>Rule Name</td>
			<td>: <s:textfield name='acctrulename' id='acctrulename'
					maxlength="32" />
			</td>
			<TD></TD>
			<TD></TD>
		</tr>



		<tr>
			<td>Product</td>
			<td>: <s:select list="%{prodlist}" id="product" name="product"
					listKey="PRODUCT_CODE" listValue="CARD_TYPE_NAME" headerKey="-1"
					headerValue="-SELECT-" onchange="getSubProductList( this.value)" />
			</td>

			<td><s:property value="subproductlistbean" /> Sub Product</td>
			<td>: <select name="subprodid" id="subprodid">
					<option value="-1">--Select --</option>
					<s:iterator value="prodlist">

					</s:iterator>
			</select>
			</td>

		</tr>



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


			<td>Orgin Channel</td>
			<td>: <s:select list="%{orginchannel}" id="orginchannel"
					name="orginchannel" listKey="ORGIN_CHANNEL"
					listValue="ORGIN_CHANNEL" headerKey="-1" headerValue="-SELECT-" />

			</td>


		</tr>




		<tr>
			<td>Device Type</td>
			<td>: <s:select list="%{devicetype}" id="devicetype"
					name="devicetype" listKey="DEVICETYPE" listValue="DEVICETYPE"
					headerKey="-1" headerValue="-SELECT-" />

			</td>

			<td></td>
			<td></td>
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

