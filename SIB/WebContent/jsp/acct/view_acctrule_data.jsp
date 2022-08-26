<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script>
	function validateAcctRules ( delkey ){
	 
		if( !confirm ( "Do you want to delete this Rule ")){
			return false;
		} 
		 
	 	parent.showprocessing(); 
	}
</script>
  

<jsp:include page="/displayresult.jsp"></jsp:include>
  
<s:form action="viewAccountRuleHomeDeleteAcctRule.do" name="orderform"	onsubmit="return validateAcctRules()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>	
		<s:hidden name="acctrulecode" id="acctrulecode" value="%{accrulebean.acctrulecode}"/>
		<td>Rule Name</td>
			<td class="textcolor" >: <s:property value="accrulebean.acctrulename"/>
			</td>
			<td>Product</td>
			<td class="textcolor" >: <s:property value="accrulebean.productdesc"/>
			</td>
			<td>  Sub Product</td>
			<td class="textcolor" >:  <s:property value="accrulebean.subproductdesc"/> 
	</tr> 
	<tr>
			<td>Txn Code</td>
			<td class="textcolor" >:  <s:property value="accrulebean.strtxncode"/>  
			</td> 
			<td>Message Type</td>
			<td class="textcolor" >:  <s:property value="accrulebean.strmsgtype"/>  
			</td> 
			<td>Response Code</td>
			<td class="textcolor" >:  <s:property value="accrulebean.strrespcode"/>  
			</td> 
	</tr> 
		
	<tr> 
			<td>Orgin Channel</td>
			<td class="textcolor" >:  <s:property value="accrulebean.strorgchannel"/> 
			</td>
			<td>Device Type</td>
			<td class="textcolor" >:  <s:property value="accrulebean.strdevicetype"/> 
			<td>Financial Transactioin</td>
			<td class="textcolor" >:   <s:property value="accrulebean.fintxndesc"/> 
			</select> 
			</td>
	</tr> 
	<tr>
			<td>Transaction Category</td>
			<td class="textcolor" >:  <s:property value="accrulebean.revtxn"/>  
			</td> 
			<td>Reversal Type</td>
			<td class="textcolor" >:   <s:property value="accrulebean.revtxntype"/>  
			</td>
	</tr>
		
		 
		
		
	</table>


	<table border="0" cellpadding="0" cellspacing="4" width="20%">
		<tr>
			<td><s:submit value="Delete" name="delete" id="delete"/></td>
			<td><input type="button" name="cancel" id="cancel"		value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

			</td>
		</tr>
	</table>
</s:form>

