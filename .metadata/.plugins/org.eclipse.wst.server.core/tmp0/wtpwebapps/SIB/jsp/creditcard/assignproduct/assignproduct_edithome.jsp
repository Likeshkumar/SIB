<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
table.viewtable td{
	padding-top:20px;
}
</style>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script> 
 

<script>
function validateForm(){
	var applicationid  = document.getElementById("customerid");
	var productcode  = document.getElementById("productcode");
	var subproduct  = document.getElementById("subproduct");
	
	if( customerid ){ if( customerid.value == "-1") {errMessage(customerid, "Select Application !");return false;} }
	if( productcode ){ if( productcode.value == "-1") {errMessage(productcode, "Select Product !");return false;} }
	if( subproduct ){ if( subproduct.value == "-1") {errMessage(subproduct, "Select Sub Product !");return false;} }
	parent.showprocessing();
	return true;
}

function getSubProd( instid, selprodid ){  
		//alert("instid==> "+instid);	
		 
 		var url = "getListOfSubProduct.do?prodid="+selprodid+"&status=1";   
 		result = AjaxReturnValue(url);
 		document.getElementById("subproduct").innerHTML = result;  
	} 
</script>

<jsp:include page="/displayresult.jsp"></jsp:include> 
<%
String instid = (String)session.getAttribute("Instname");
 
%>
<form action="assingProductEditViewCreditProductLimit.do" onsubmit="return validateForm()" autocomplete="off" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">	 
<s:hidden name="doact" id="doact" value="%{creditbean.doact}" />
<tr> 
	<td>  Application </td>
	<td> : <s:select list="%{crlimitbean.applist}" id="customerid"  name="customerid"    headerKey="-1" headerValue="-SELECT-"  listKey="CUSTOMERID"  listValue="CUSTOMERNAME" value="%{crlimitbean.applicationid}"/> </td>
</tr>
<%-- 
<tr> 
	<td>  Product </td>
	<td> : 
		<select onchange="getSubProd( '<%=instid%>', this.value );" id="productcode"  name="productcode"    >
			<option value="-1"> -SELECT- </option>
			<s:iterator value="crlimitbean.productlist">
				<option value="${PRODUCT_CODE}"> ${CARD_TYPE_NAME} </option>
			</s:iterator>
		</select>
	 </td>
</tr>

<tr> 
	<td> Sub-Product </td>
	<td> : 
		<select name="subproduct" id="subproduct">
    				<option value="-1">-Select Sub Product-</option>
    		</select>
    			
	</td>
</tr>  --%>
</table> 
  
  <table border="0" cellpadding="0" cellspacing="4" width="20%"  ">	 
		<tr>
		<td>
			<s:submit value="Next" name="order" id="order" onclick="return customerinfo_checker()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
 </form>