<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>
<style type="text/css">
	*{
		font-weight:normal;
	}
	table.formtable td{
		text-align:left;
		padding:10px 0;
	}
	table{
		border-collapse: collapse;
		
	}
	table.subdesc  {
		border:1px solid gray; 
	}
	table.subdesc td {
		border-left:1px solid gray; 
		border-bottom:1px solid gray; 
		padding:10px 0;
	}
	table.subprodname td{
		border:1px solid gray;
	}
</style>

	<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
<script type="text/javascript" >
	function enableCorparate( corparate ){
		 
		var corp = document.getElementById(corparate);
		var corporatecardno = document.getElementById("corporatecardno"); 
		if( corp.value == "$CORP"){
			corporatecardno.disabled=false;
			corporatecardno.focus();
		}else{
			corporatecardno.disabled=true;
		}
		return false;
	}
function getGlSchemeCode( prodcode ){ 
	 
	if( prodcode != -1 ){
		// document.getElementById('textbox_creater').innerHTML 
		//var url = "glSchemeCodeAjaxSubProduct.do?prodcode="+prodcode; 
		var url = "glSchemeCodeAddSubProdAction.do?prodcode="+prodcode;
		var result = AjaxReturnValue(url); 
		
	}else{
		var result = "<option value='-1'> SELECT </option>";	
	}
	document.getElementById("gl_scheme_code").innerHTML = result;
	return false;
}
function rename_subproduct()
{
   	//var ren=document.getElementById("ren_vis");
   	document.getElementById('ren_vis').innerHTML = " Do You Want To Rename Sub Product <input type=\"checkbox\" name=\"rename_subtype\" id=\"rename_subtype\" onclick=\" return sme_funct();\" > ";
   	document.getElementById('textbox_creater').innerHTML = " <input type=\"hidden\" name=\"rename_txt\" id=\"rename_txt\" value=\"defaultvalue\"> ";
}
function numberOnly(evt)
{
	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
	//	var keyvalue=evt.charCode;
	//alert(keyvalue);
	if((!(keyvalue<48 || keyvalue>57)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13))
	{
		return true;
	}
	else
	{ 
		return false;
	} 
}	
    
function sme_funct()
{
	if (document.SubProdAddFofm.rename_subtype.checked == false)
	{
		//alert ("You Will Have Same Name for Sub-Product");
		document.getElementById('textbox_creater').innerHTML =  " <input type=\"hidden\" name=\"rename_txt\" id=\"rename_txt\" value=\"defaultvalue\" maxlength=\"20\">";
		//document.SubProdAddFofm.rename_subtype.checked = false;
		return true;
	}
	else
	{
		document.getElementById('textbox_creater').innerHTML =" <table border=\"0\" cellpadding=\"3\" cellspacing=\"3\" width=\"40%\"><tr><td align=\"left\" width=\"75%\">New Product Name</td><td><h1><input type=\"text\" name=\"rename_txt\" id=\"rename_txt\" maxlength=\"20\"></h1></td><tr></table>";
		return true;
	}
		
	
}

var httpObject = null;
 
// Get the HTTP Object
 function getHTTPObjectForBrowser()
 {
         if (window.ActiveXObject)
             return new ActiveXObject("Microsoft.XMLHTTP");
         else if (window.XMLHttpRequest) return new XMLHttpRequest();
         else 
         {
             	alert("Browser does not support AJAX...........");
        	 	return null;
		}
}
// Change the value of the outputText field
function setAjaxOutput()
{
    if(httpObject.readyState == 4)
    {
    	//alert(httpObject.responseText);
        document.getElementById('ajax').innerHTML = httpObject.responseText;
        document.getElementById('test').innerHTML = "SUB PRODUCT";
    }
}
// Implement business logic
function doAjaxCall()
{
	document.getElementById('ren_vis').innerHTML = "";
	httpObject = getHTTPObjectForBrowser();
    if (httpObject != null) 
    {
    	// alert(document.getElementById('product').value);
		var productid=(document.getElementById('product').value);
		var instd=(document.getElementById('instid').value);
        httpObject.open("GET", "callAJaxAddSubProdAction.do?prodid="+productid+" & instid ="+instd, true);
        httpObject.send(null);
		httpObject.onreadystatechange = setAjaxOutput;
	}
}

function  checkform()
{
	var product = document.getElementById("product");
	if( product ){ if( product.value == ""  || product.value == "-1") { errMessage(product, "Select Product !");return false; } }
	var subproductname = document.getElementById("subproductname");
	if( subproductname ){ if( subproductname.value == ""  || subproductname.value == "-1") { errMessage(subproductname, "Enter Sub Product Name !");return false; } }
	var scheme_scode = document.getElementById("scheme_scode");
	if( scheme_scode ){ if( scheme_scode.value == ""  || scheme_scode.value == "-1") { errMessage(scheme_scode, "Select Fee Code !");return false; } }
	
	var currencymulti = document.getElementById("currencymulti");
	if( currencymulti ){ if( currencymulti.value == ""  || currencymulti.value == "-1") { errMessage(currencymulti, "Select Currency Code !");return false; } }
	
	var limit_code = document.getElementById("limit_code");
	if( limit_code ){ if( limit_code.value == ""  || limit_code.value == "-1") { errMessage(limit_code, "Select Limit Code !");return false; } }
	
	var scode = document.getElementById("scode");
	if( scode ){ if( scode.value == ""  || scode.value == "-1") { errMessage(scode, "Select Service Code !");return false; } }
	var expiry = document.getElementById("expiry");
	if( expiry ){ if( expiry.value == ""  || expiry.value == "-1") { errMessage(expiry, "Enter Expiry Date in Months !");return false; } }
	
		
	var personalized = document.getElementsByName("personalized");
	var personalizedvali = document.getElementById("personalized1");
	
	if (  (personalized[0].checked == false)  && ( personalized[1].checked == false ) && (personalized[3].checked == false))
		{
		errMessage(personalizedvali, "Select Persionalized !");return false;
		}
	
	var pin_required = document.getElementById("pin_required");
	if( pin_required ){ if( pin_required.value == ""  || pin_required.value == "-1") { errMessage(pin_required, "Select Pin Required !");return false; } }
	
	var maintain_required = document.getElementById("maintain_required");
	if( maintain_required ){ if( maintain_required.value == ""  || maintain_required.value == "-1") { errMessage(maintain_required, "Select Maintain Required !");return false; } }
	
	
}


function chkChars(field,id,enteredchar)
{
	//alert('1:'+field+'2:'+id+'3:'+enteredchar);
	charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
    	//alert(document.getElementById(id).value.charAt(i));   
    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

function validateNumber (field,id,enteredchar)
{
	charvalue= "0123456789";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
    	
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
       	//alert(document.getElementById(id).value.charAt(i));
       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

</script>


		
					<%
					String act = (String)session.getAttribute("act");
					%>	 
<jsp:include page="/displayresult.jsp"></jsp:include>
	<s:form  action="saveDataAddSubProdAction" name="SubProdAddFofm" namespace="/" autocomplete="off"  >
		 <input type="hidden" name="act"  id="act" value="<%=act%>"> 
	  
	 
	 <s:url id="act"   action="displayAddSubProdAction" ></s:url>
	 
	 <fieldset>
      	<legend><b> Sub Product Name </b></legend> 
      	
 	<table border="0" cellpadding="0" cellspacing="0" width="50%" > 
 		<tr>
			<td align="left" >INSTID</td>
			<td> : 
				 
					<s:textfield readonly="true" name="instid" id="instid" value="%{#session.Instname}" />
				 
			</td>
 		</tr>
 		<tr>
			<td align="left">PRODUCT</td>
			
			<td> : <s:select name="product" id="product" listKey="productname" listValue="productid" list="productlist"  headerValue="--- Select PRODUCT---" headerKey="-1" value ="%{bean.product}"/>
			<%-- <td> : 
				 <s:select label="PRODUCT"
				headerKey="-1" headerValue="--- Select PRODUCT---"
				list="productlist" 
				listValue="%{productid}"
    			listKey="%{productname}" 
    			name="product" id="product" > 
   				</s:select> --%>
		<s:fielderror fieldName="product" cssClass="errmsg" />		 
			</td>
 		
 		</tr >   
 
  		<tr>
 			<td id="test" align="left">SUB PRODUCT NAME
 			</td>
 			<td> : 
 				<s:textfield name='subproductname' id='subproductname' value="%{bean.subproductname}" onkeyup="chkChars('SUB PRODUCT NAME',this.id,this.value)"/>
 				<s:fielderror fieldName="subproductname" cssClass="errmsg" /> 
 			</td>
 			
 			<td >
				<b>CURRENCY</b>
			</td>
			 <td> : <s:select name="currencymulti" id="currencymulti" listKey="CUR_CODE" listValue="CURRENCY_DESC" list ="Curencyvalues" headerValue="--- Select Currency---" headerKey="-1" value ="%{bean.currencymulti}"/> 
			<s:fielderror fieldName="currencymulti" cssClass="errmsg" />
			 </td> 
			<%--  <td > : 
				<select name="currencymulti" id="currencymulti" >
					<option value="-1">--Select Currency--</option>
				<s:iterator value="Curencyvalues" var="Curencyvalues">
					<option value="${CUR_CODE}">${CURRENCY_DESC}</option>
				</s:iterator>
				</select>
				<s:fielderror fieldName="currencymulti" cssClass="errmsg" />
			 </td> --%>
 		</tr>	
	</table >
 
 </fieldset>
 
  <fieldset>
      	<legend><b> Sub Product Details </b></legend> 
<table border="0" cellpadding="0" cellspacing="0" style='margin-top:10px' width="90%">
		<tr>
			
			
			<td >
				<b>Limit  Code</b>
			</td>
			
				<td> : <s:select name="limit_code" id="limit_code" listKey="LIMIT_ID" listValue="LIMIT_DESC" list="limit_list"  headerValue="--- Select LIMIT---" headerKey="-1" value ="%{bean.limit_code}"/>
			<s:fielderror fieldName="limit_code" cssClass="errmsg" />
</td>
			<%-- <td > : 
				<select name="limit_code" id="limit_code" >
					<option value="-1">--Select Limit--</option>
				<s:iterator value="limit_list" var="var_limit_list">
					<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
				</s:iterator>
				</select>
				
			</td>
			 --%>
			<td >
				<b>Fee Code</b>
			</td>
				<td> : <s:select name="scheme_scode" id="scheme_scode" listKey="FEE_CODE" listValue="FEE_DESC" list="scheme_list"  headerValue="--- Select FEE---" headerKey="-1" value ="%{bean.scheme_scode}"/>
			<s:fielderror fieldName="scheme_scode" cssClass="errmsg" />
</td>
	
	<%-- 
	
			<td > : 
				<select name="scheme_scode" id="scheme_scode" >
					<option value="-1">--Select Fee--</option>
				<s:iterator value="scheme_list" var="var_scheme_list">
					<option value="${FEE_CODE}">${FEE_DESC}</option>
				</s:iterator>
				</select>
				<s:fielderror fieldName="scheme_scode" cssClass="errmsg" />
			</td> 
			
	 --%>		<td>
				<b>Expiry Period(In Months)</b>
			</td>
			<td> : 
				<s:textfield name="expiry" id="expiry" maxlength="3" value="%{bean.expiry}"  onkeyup="validateNumber('Expiry Period(In Months)',this.id,this.value)" />
				<s:fielderror fieldName="expiry" cssClass="errmsg" /> 
			</td>
		</tr>
			 
		<tr>
			<td>
				<b>Card Generation Type</b>
			</td>
			<td>
				 : <s:radio list="#{'1':'Personalized','0':'Instant','2':'Both'}" id="personalized" name="personalized"  onclick="return nocard();" value="0"/>
				 <s:fielderror fieldName="personalized" cssClass="errmsg" />
			</td>
		<td></td>
			<td> <b>Maintenance Required</b> </td>
			<td> : <s:select name="maintain_required" id="maintain_required" list="#{'Y':'YES', 'N':'NO'}" headerKey="-1" headerValue="SELECT" value="%{bean.maintain_required}"/>
			<s:fielderror fieldName="maintain_required" cssClass="errmsg" /></td>
			
		</tr>
</table>
</fieldset>


<table border="0" cellpadding="0" cellspacing="4" width="20%" class="formtable" >
		<tr>
		<td>
<%-- 			<s:submit value="Submit" name="submit" id="submit"  onclick="return checkform();"/> --%>
<s:submit value="Submit" name="submit" id="submit"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
	
</s:form>
 
