<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>

<style>
	table{
		border-collapse: collapse;
	}
	table.subdesc  {
		border:1px solid gray; 
	}
	table.subdesc td {
		border-left:1px solid gray; 
		border-bottom:1px solid gray; 
	}
	table.subprodname td{
		border:1px solid gray;
	}
	
	#textcolor
	{
	color: maroon;
	font-size: small;
	}
</style>
<script type="text/javascript">

function deleleteSubProduct(){
 
	 var reason = prompt("Reason For Delete");
	 
	var productcode = document.getElementById("product_code").value;
	var subproductcode = document.getElementById("sub_prod_id").value;
	
	var url = "deleteSubProducActionAddSubProdAction.do?productcode="+productcode+"&subproductcode="+subproductcode+"&reason="+reason;
	//alert("john : " + url );
	window.location=url;
	return false;
}

function authConfirm( doact ){
	 
	
	 var reason = "";
	if( doact == 'DEAUTH'){
		reason = prompt("Reason For Reject");
	} 
	var productcode = document.getElementById("product_code").value;
	var subproductcode = document.getElementById("sub_prod_id").value;
	
	var url = "deleteSubProducAuthActionAddSubProdAction.do?productcode="+productcode+"&subproductcode="+subproductcode+"&reason="+reason+"&doact="+doact;
	//alert(url)
	window.location=url;
	return false;
}




function getGlSchemeCode( prodcode ){ 
	 
	if( prodcode != -1 ){
		// document.getElementById('textbox_creater').innerHTML 
		var url = "glSchemeCodeAjaxSubProduct.do?prodcode="+prodcode; 
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
	 var product = document.SubProdAddFofm.product;
	 var subproductname = document.SubProdAddFofm.subproductname;
	 var rename_txt = document.SubProdAddFofm.rename_txt;
	 var scheme_scode = document.SubProdAddFofm.scheme_scode;
	 var gl_scheme_code = document.SubProdAddFofm.gl_scheme_code;
	 var bincurlist = document.SubProdAddFofm.bincurlist;
	 var scode = document.SubProdAddFofm.scode;
	 var amount = document.SubProdAddFofm.amount;
	 var limit_code = document.SubProdAddFofm.limit_code;
	 valid = true;			
    if(product){	   
	 if ( product.value == "-1" )
    	    {
    		   errMessage(product,"PLEASE SELECT THE PRODUCT" );
    	       return false;   
    	    }
    }
    if(subproductname)
    {
    	    if ( subproductname.value == "" )
    	    {	 errMessage(subproductname,"PLEASE ENTER THE SUB PRODUCT" );
    	    	return false;	   		 
    		}
    }  
    if(scheme_scode)
    {
    	    if( scheme_scode.value == "-1" )
	   		{
    	    	errMessage(scheme_scode,  "PLEASE SELECT THE SCHEME CODE" );
		        return false;
	   		}
    }   
    
    if( limit_code ){
    	if( limit_code.value == "-1"){
    		errMessage(limit_code,  "SELECT THE LIMIT " );
	        return false;
    	}
    }
    if(gl_scheme_code)
    {
    	   if(gl_scheme_code.value=="-1")
    	    {
    	    	errMessage(gl_scheme_code,  "PLEASE SELECT THE GL SCHEME CODE" );
		        return false;
    	    }
    }
    if(bincurlist)
    {
    	    if(bincurlist.value=="-1")
    	    {
    	    	errMessage(bincurlist,  "PLEASE SELECT THE CARD CURRENCY" );
		        return false;
    	    }
    }
    if(scode)
    {   
    	  if( scode.value == "" )
	   		{
    	    	errMessage(scode,"PLEASE ENTER THE SERVICE CODE" );
		        return false;
	   		}
    }
    if(amount)
    {  
    	   if ( amount.value == "" )
    	    {
    	    	errMessage(amount,"ENTER THE AMOUTN");
    	    	document.SubProdAddFofm.amount.focus();
    	    	return false;
    	    }
    }    	    
    	    var x = confirm( "Do you want to Submit ");
    		if ( x ) {
    			return valid;
    		}else{
    			return false;
    		}
    		
		} 

      
function nocard(){		
    	  if(document.SubProdAddFofm.personalized1.checked){
    		  document.SubProdAddFofm.no_nonper_cards.value=0;
    		  document.SubProdAddFofm.no_per_cards.value=1;
    		  document.SubProdAddFofm.no_per_cards.readonly=false;
    		  document.SubProdAddFofm.no_nonper_cards.readonly=true;    		  
    		  alert("PERSONALISED");
    	  }if(document.SubProdAddFofm.personalized0.checked){
	    	  document.SubProdAddFofm.no_nonper_cards.value=10;
	    	  document.SubProdAddFofm.no_per_cards.value=0;
			  document.SubProdAddFofm.no_per_cards.readonly=true;
			  document.SubProdAddFofm.no_nonper_cards.readonly=false;
		  alert("NON PERSONALISED");
		  } if(document.SubProdAddFofm.personalized2.checked){
			  document.SubProdAddFofm.no_per_cards.readonly=false;
			  document.SubProdAddFofm.no_nonper_cards.readonly=false;
			  document.SubProdAddFofm.no_nonper_cards.value=10;
			  document.SubProdAddFofm.no_per_cards.value=1;
			  alert("BOTH");
		  } 
}

function reset_confirm(){
 	var r=confirm("Do You Want To Reset");
 	return r;
}

function cardcurrency(){		 
		var productid=(document.getElementById('product').value);
		var instd=(document.getElementById('instid').value);
		var url = "callAJaxbinAddSubProdAction.do?prodid="+productid+" & instid ="+instd;		 
		var result = AjaxReturnValue(url);
		document.getElementById('card_ccy').innerHTML = result;
}

function goBack(){
	window.history.back()
}
function validation_authsubproduct(){
 	var auth = document.getElementById("auth0").value;
 	var sub_prod_id = document.getElementById("sub_prod_id").value;
 	//alert(sub_prod_id);
 	var product_code = document.getElementById("product_code").value;
 	//alert(product_code);
 	//alert(product_code);alert(sub_prod_id);
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		 var url = "authorizeActionAddSubProdAction.do?sub_prod_id="+sub_prod_id+"&product_code="+product_code+"&reason="+reason+"&auth="+auth;
		 window.location = url; 
	 }  
	 return false;
}


</script>


		
				<%
					String localError=null;
					String localMessage=null;
					localError=(String) session.getAttribute("dispsubprod_localError");
					localMessage=(String) session.getAttribute("dispsubprod_localMessage");
					String act = (String)session.getAttribute("act");
					String addSubProdmessgae = null;
					String addSubProdErrorSatus = null;
					addSubProdmessgae = (String) session.getAttribute("addSubProdmessgae");
					addSubProdErrorSatus=(String) session.getAttribute("addSubProdErrorSatus");
					session.removeAttribute("dispsubprod_localError");
					session.removeAttribute("dispsubprod_localMessage");
					session.removeAttribute("addSubProdmessgae");
					session.removeAttribute("addSubProdErrorSatus");
					if (addSubProdErrorSatus != null ) 
					{
						%>
						<div align="center">
						<table border='0' cellpadding='0' cellspacing='0' width='40%' >
						<tr align="center">
							<td colspan="2">
								<font color="Red"><b><%=addSubProdmessgae%></b></font>
							</td>
						</tr>
						</table>
						</div>
						<%
					} 
					%>	 
<jsp:include page="/displayresult.jsp"></jsp:include>			
<div align="center"> 
<form action="authorizeActionAddSubProdAction.do" method="post" autocomplete="off">
	<s:iterator  value="subprod_list">	
	<s:hidden name="product_code" id="product_code" value="%{PRODUCT_CODE}"/>
	<s:hidden name="sub_prod_id" id="sub_prod_id" value="%{SUB_PROD_ID}"/>
		<table style="border:1px solid #efefef;"  border="0" cellpadding="0" align="center" width="90%" cellspacing="0"  >
				<%--<tr><td> <b>Sub Product id:</b> </td><td id="textcolor"> :  <s:textfield name="sub_product" id="sub_product" value="%{SUB_PROD_ID}" readonly="true"/></td><td> <b>Product id:</b> </td><td id="textcolor"> :  <s:textfield name="sub_product" id="sub_product" value="%{PRODUCT_CODE}" readonly="true"/></td></tr> --%>
				<tr>
					<td><b>Product Name:</b></td>
					<td id="textcolor"> :<s:textfield name="sub_product" id="sub_product" value="%{CARD_TYPE_NAME}" readonly="true"/></td>
					
					<td><b>Sub Product Name: </b></td>
					<td id="textcolor"> :<s:textfield name="subproductname" id="subproductname" value="%{SUB_PRODUCT_NAME}" readonly="true"/></td>
				</tr>
				<!-- <tr style="visibility:hidden">
					<td><b>Fee Code</b></td>
					<td id="textcolor"> :<s:textfield name="scheme_scode" id="scheme_scode" value="%{FEE_DESC}" readonly="true"/></td>
					
					<td><b>Limit  Code</b></td>
					<td id="textcolor"> :<s:textfield name="limit_code" id="limit_code" value="%{LIMIT_DESC}" readonly="true"/></td>
				</tr> -->
				<tr>
					<td><b>Expiry Period(In Months)</b></td>
					<td id="textcolor"> :<s:textfield name="expiry" id="expiry" value="%{EXPIRY_PERIOD}" readonly="true"/></td>
					
					<td><b>Card Generation Type</b></td>
					<td id="textcolor">: <s:textfield name="personalized" id="personalized" value="%{PERSONALIZED}" readonly="true"/></td>
				</tr>				 
				
				
				<tr>
					<td> <b>Maintenance Required</b> </td>
					<td id="textcolor"> : <s:textfield name="maintain_required" id="maintain_required" value="%{MAINTAIN_REQ}" readonly="true"/></td>		
					
				
					<td> <b>Configured By </b> </td>
					<td id="textcolor"> : <s:textfield name="sub_product" id="sub_product" value="%{CONFIG_BY}" readonly="true"/></td>		
				</tr>
				
				
				<tr>
					
				</tr>
				
				<tr>			
					<td> <b>Configured Date </b> </td>
					<td id="textcolor"> : <s:textfield name="sub_product" id="sub_product" value="%{CONFIG_DATE}" readonly="true"/></td>
				
					<td> <b>Configured Status </b> </td>
					<td id="textcolor"> : <s:textfield name="sub_product" id="sub_product" value="%{AUTH_STATUS}" readonly="true"/></td>
				</tr>
				<tr>
					<td> <b>Remarks </b> </td>
					<td id="textcolor"> : <s:property value="%{REMARKS}"/> <%-- <s:textfield name="sub_product" id="sub_product" value="%{REMARKS}" readonly="true"/> --%></td>
				</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="4" width="20%" >
				<tr>
				<s:if test="%{subproductbean.doact=='$DEL'}">
						<input type='button' value="Delete" name="delete" id="delete" onclick="return deleleteSubProduct()" />
						&nbsp;&nbsp;&nbsp;
						<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
				</s:if>
				<s:elseif test="%{subproductbean.doact=='$DELAUTH'}">
						<input type="button" value="Authorize" name="authdel" id="authdel" onclick="return authConfirm('AUTH')"/>
						<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"   onclick="return authConfirm('DEAUTH')"/>
				</s:elseif>
				<s:else>
					<td> 
						<s:if test="enableauth">
							<s:submit value="Authorize" name="auth" id="auth1"/>
							<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authsubproduct()"/>
						</s:if>
							<%-- <s:else><s:submit value="Edit" name="submit" id="submit"  /></s:else> --%>				
					</td>
				</s:else>
				</tr>
		</table>
	</s:iterator>
</form>	
</div>
 
