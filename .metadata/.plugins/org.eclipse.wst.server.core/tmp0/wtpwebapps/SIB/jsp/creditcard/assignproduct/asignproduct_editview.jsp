<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
table.viewtable td{
	padding-top:5px;
}
</style>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script> 
 

<script>

function getSubProd( instid, selprodid ){  
	//alert("instid==> "+instid);	
	 
		var url = "getListOfSubProduct.do?prodid="+selprodid+"&status=1";   
		result = AjaxReturnValue(url);
		document.getElementById("subproduct").innerHTML = result;  
} 

function validateForm(){
	 
	var creditlimit  = document.getElementById("creditlimit");
	var cashlimit  = document.getElementById("cashlimit");
	var ecomlimit  = document.getElementById("ecomlimit");
	var transferlimit  = document.getElementById("transferlimit");
	var newproductcode   = document.getElementById("newproductcode");
	var newsubproduct   = document.getElementById("newsubproduct");
	
	if( creditlimit ){ if( creditlimit.value == "") {errMessage(creditlimit, "Enter Credit Limit !");return false;} }
	if( cashlimit ){ if( cashlimit.value == "") {errMessage(cashlimit, "Enter Cash Limit !");return false;} }
	if( ecomlimit ){ if( ecomlimit.value == "") {errMessage(ecomlimit, "Enter E-Com Limit!");return false;} }
	if( transferlimit ){ if( transferlimit.value == "") {errMessage(transferlimit, "Enter Transfer Limit!");return false;} } 
	
	//alert( Number(creditlimit.value) );
	//alert(  Number(cashlimit.value)+Number(ecomlimit.value)+Number(transferlimit.value) )
	if( Number(creditlimit.value) <  Number(cashlimit.value)+Number(ecomlimit.value)+Number(transferlimit.value)  ){
		errMessage(ecomlimit, "Sum of Limits Should not be greater than Credit Limit");
		return false;
	}	 
	
	//alert( newproductcode.value )
	if( newproductcode ){
		if( newproductcode.value != "-1"){
			if( newsubproduct.value == "-1"){
				errMessage(newsubproduct, "Select Sub-Product!");return false;
			}
		}
	}
	
	parent.showprocessing();
	return true;
}

function getSubProd( instid, selprodid ){  
		//alert("instid==> "+instid);	
		 
 		var url = "getListOfSubProduct.do?prodid="+selprodid+"&status=1";   
 		result = AjaxReturnValue(url);
 		document.getElementById("newsubproduct").innerHTML = result;  
	} 
</script>

<jsp:include page="/displayresult.jsp"></jsp:include> 
<%
String instid = (String)session.getAttribute("Instname");
 
%>
<form action="assingProductEditActionCreditProductLimit.do" onsubmit="return validateForm()"  autocomplete = "off">
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="formtable viewtable">	 
<s:hidden name="doact" id="doact" value="%{creditbean.doact}" />
 
<s:iterator value="crlimitbean.customerinfolist">
 
<tr> 
	<td class="txt">  First Name </td> <td> : ${FIRSTNAME}  </td>
	<td class="txt">  Middle Name </td> <td> : ${MIDDLENAME}  </td>
	<td class="txt">  Last Name </td> <td> : ${LASTNAME}  </td>
</tr>

 
<tr> 
	<td class="txt">   	Name On Card </td> <td> : ${ENCNAME}  </td>
	<td class="txt">  Nationality </td> <td> : ${NATIONALITY}  </td>
	<td class="txt"> Date of Birth </td> <td> : ${DOB}  </td>
</tr>

</s:iterator>
 
<s:iterator value="crlimitbean.applist" var="app">
 <tr> 
	<td class="txt">   	Added By  </td> <td> : ${ADDED_BY}  </td>
	<td class="txt">   	Added Date  </td> <td> : ${ADDED_DATE}  </td>
	<td class="txt">   	Status  </td> <td> : ${AUTH_STATUS}  </td>	 
</tr>
<tr> 
	<td class="txt">   	Auth By  </td> <td> : ${AUTH_BY}  </td>
	<td class="txt">   	Auth Date </td> <td> : ${AUTH_DATE}  </td>
	<td class="txt">   	Remarks </td> <td> : ${REMARKS}  </td>	 	 
</tr> 
<tr><th colspan="6"> Product &amp; Limit Details</th></tr>
<tr>
	 <s:hidden name="customerid" id="customerid" value="%{crlimitbean.customerid}"/>  
	<td class="txt">   Product </td> <td> : <s:property value="crlimitbean.productdesc"/> <s:hidden name="productcode" id="productcode" value="%{crlimitbean.productcode}"/> </td>
	<td class="txt">   Sub-Product </td> <td> : <s:property value="crlimitbean.subproductdesc"/>  <s:hidden name="subproductcode" id="subproductcode" value="%{crlimitbean.subproductcode}"/> </td>
	
</tr>

<tr> 
	<td class="txt">   Credit Limit  </td> <td> : <s:textfield name="creditlimit" id="creditlimit" maxlength="12" onkeypress="return numerals(event)" value="%{crlimitbean.creditlimit}" /> </td>
	<td class="txt">   Cash Limit   </td> <td> : <s:textfield name="cashlimit" id="cashlimit" maxlength="12" value="%{crlimitbean.cashlimit}" onkeypress="return numerals(event)"/> </td>	
	<s:if test="%{crlimitbean.cardgenerated==0}">
	 <td class="txt">   Product </td>   
		<td> : 
			<select onchange="getSubProd( '<%=instid%>', this.value );" id="newproductcode"  name="newproductcode"    >
				<option value="-1"> NO CHANGE </option>
				<s:iterator value="crlimitbean.productlist">
					<option value="${PRODUCT_CODE}"> ${CARD_TYPE_NAME} </option>
				</s:iterator>
			</select>
		 </td>
	  </s:if>
</tr>

<tr> 
	<td class="txt">   E-Com Limit  </td> <td> : <s:textfield name="ecomlimit" id="ecomlimit" maxlength="12" value="%{crlimitbean.ecomlimit}"  onkeypress="return numerals(event)"/> </td>
	<td class="txt">   Transfer Limit john <s:property value="crlimitbean.cardgenerated"/> </td> <td> : <s:textfield name="transferlimit" id="transferlimit" maxlength="12" value="%{crlimitbean.transferlimit}"   onkeypress="return numerals(event)"/> </td>	 
	<s:if test="%{crlimitbean.cardgenerated==0}">
	 <td class="txt">   Sub-Product </td> 
		<td> : 
			<select name="newsubproductcode" id="newsubproduct">
	    				<option value="-1"> NO CHANGE </option>
	    		</select>
	    			
		</td>
	</s:if>
	
</tr>
</s:iterator>



 
</table> 
  
  <table border="0" cellpadding="0" cellspacing="4" width="20%"  ">	 
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return showprocessing()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
 </form>