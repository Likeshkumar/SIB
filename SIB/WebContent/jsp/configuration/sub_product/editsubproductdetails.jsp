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
	 var product = document.SubProdAddFofm.product;
	 var subproductname = document.SubProdAddFofm.subproductname;
	 var rename_txt = document.SubProdAddFofm.rename_txt;
	 var scheme_scode = document.SubProdAddFofm.scheme_scode;
	 var gl_scheme_code = document.SubProdAddFofm.gl_scheme_code;
	 var bincurlist = document.SubProdAddFofm.bincurlist;
	 var scode = document.SubProdAddFofm.scode;
	 var expiry = document.SubProdAddFofm.expiry;
	 var pin_required = document.SubProdAddFofm.pin_required;
	 var min_loadamt = document.SubProdAddFofm.min_loadamt;
	 var max_loadamt = document.SubProdAddFofm.max_loadamt;
	 var register_required = document.SubProdAddFofm.register_required;
	 var maintain_required = document.SubProdAddFofm.maintain_required;
	 /* var mcc_required = document.getElementById("mcc_required1").value;
	 //alert(mcc_required);
	 var network_required = document.getElementById("network_required1");
	 var merchant_required = document.getElementById("merchant_required1"); */
	
	 var corprateproduct = document.getElementById("corprateproduct");
	 var corporatecardno = document.getElementById("corporatecardno");
	 var revenueglcode = document.getElementById("revenueglcode");
	 
	 var kyclimitlevel = document.getElementById("kyclimitlevel");
	 var maxallowamt = document.getElementById("maxallowamt");
	 var maxallowcnt = document.getElementById("maxallowcnt");
	 var shiftintervel = document.getElementById("shiftintervel");
	 
	 valid = true;			
    if(product)
    {	   
	 if( product.value == "-1" )
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
   	    	errMessage(scheme_scode,  "PLEASE SELECT THE FEE CODE" );
	        return false;
   		}
    }   
    
    if( limit_code )
    {
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
   	  if( scode.value == "-1" )
   		{
   	    	errMessage(scode,"Select The Service Code" );
	        return false;
   		}
    }
    if(expiry)
    {  
   	   if ( expiry.value == "" )
   	    {
   	    	errMessage(expiry,"ENTER THE Expiry Period");
   	    	document.SubProdAddFofm.expiry.focus();
   	    	return false;
   	    }
    }    
    
    if(pin_required)
    {  
   	   if ( pin_required.value == "-1" )
   	    {
   	    	errMessage(pin_required,"SELECT PIN Generation");
   	    	document.SubProdAddFofm.pin_required.focus();
   	    	return false;
   	    }
    }  
    if(min_loadamt)
    {  
   	   if ( min_loadamt.value == "" )
   	    {
   	    	errMessage(min_loadamt,"Enter Minumum Load Amount");
   	    	document.SubProdAddFofm.min_loadamt.focus();
   	    	return false;
   	    }
    }
    if(max_loadamt)
    {  
   	   if ( max_loadamt.value == "" )
   	    {
   	    	errMessage(max_loadamt,"Enter Maximum Load Amount");
   	    	document.SubProdAddFofm.max_loadamt.focus();
   	    	return false;
   	    }
    }
    if((parseInt(max_loadamt.value)) < (parseInt(min_loadamt.value)))
    {
   		errMessage(max_loadamt,"Min Load Amount should be lesser than Max Load Amount");
    	return false;
    }
    
    if(register_required)
    {  
   	   if ( register_required.value == "-1" )
   	    {
   	    	errMessage(register_required,"Select Registeration Required");
   	    	document.SubProdAddFofm.register_required.focus();
   	    	return false;
   	    }
    }
    if(maintain_required)
    {  
   	   if ( maintain_required.value == "-1" )
   	    {
   	    	errMessage(maintain_required,"Select Maintenance Required");
   	    	document.SubProdAddFofm.maintain_required.focus();
   	    	return false;
   	    }
    }
    
    if( corprateproduct ){
    	if( corprateproduct.value == "-1"){ errMessage(corprateproduct,"Select Corporate Product ");return false; }
    	if( corprateproduct.value == "$CORP"){ 
    		if( corporatecardno.value == ""){ errMessage(corporatecardno,"Enter Corporate Card Number.");return false; }
    	}
    }
    
    if( revenueglcode ){
    	if( revenueglcode.value == "-1"){ errMessage(revenueglcode,"Select Revenue GL ");return false; }
    }
     
	 
	 if( kyclimitlevel ){
		 if( kyclimitlevel.value == "-1"){ errMessage(kyclimitlevel,"Select KYC Limit  ");return false; }
	 }
	 if( maxallowamt ){
		 if( maxallowamt.value == ""){ errMessage(maxallowamt,"Enter Maximium Allowed Amount  ");return false; }
	 }
	 if( maxallowcnt ){
		 if( maxallowcnt.value == ""){ errMessage(maxallowcnt,"Enter Maximium Allowed Count  ");return false; }
	 }
	 if( shiftintervel ){
		 if( shiftintervel.value == ""){ errMessage(shiftintervel,"Enter Shifting Level Intervel");return false; }
	 }
    
	 if ( mcc_required == "1" )
   	 {
		 	var opt = "<option value='-1'>SELECT</option>";
 		   	var options = document.getElementById("mccreg").options, count = 0;
  			for (var i=0; i < options.length; i++) 
 			{
				if (options[i].selected) 
			    { 
				opt += "<option value='"+options[i].value+"'>"+options[i].value+"</option>";
			    count++;
			   
			    } 
 			}
			if( count == 0 )
			{
			errMessage( document.getElementById('mccreg'), "SELECT MCC LIST");
			return false;
			}
 		   	if( count > 5 )
 		   	{
 			errMessage( document.getElementById('mccreg'), "SHOULD NOT SELECT MCC LIST MORE THAN 5");
 			return false;	
 		   	}
   	 }
	if ( merchant_required.value == "1" )
    {
			var merchreg = document.getElementById("merchreg");
    		 if(merchreg.value == "" )	
 		 	 {
	    	    errMessage(merchreg,"Enter Merchant List");
	    	    return false;
 		 	 }
     }
 	if ( network_required.value == "1" )
    {
		var opt = "<option value='-1'>SELECT</option>";
	   	var options = document.getElementById("networkreg").options, count = 0;
		for (var i=0; i < options.length; i++) 
		{
		if (options[i].selected) 
	    { 
		opt += "<option value='"+options[i].value+"'>"+options[i].value+"</option>";
	    count++;
	   
	    } 
		}
	if( count == 0 )
	{
	errMessage( document.getElementById("networkreg"), "SELECT NETWORK LIST");
	return false;
	}
   	if( count > 5 )
   	{
	errMessage( document.getElementById("networkreg"), "SHOULD NOT SELECT NETWORK LIST MORE THAN 5");
	return false;	
   	}

    }

    return false;
  	    var x = confirm( "Do you want to Submit ");
  		if ( x ) {
  			return true;
  		}else{
  			return false;
  		}
    		
} 

      
function nocard()
{		
    	  if(document.SubProdAddFofm.personalized1.checked)
    		  {
    		  document.SubProdAddFofm.no_nonper_cards.value=0;
    		  document.SubProdAddFofm.no_per_cards.value=1;
    		  document.SubProdAddFofm.no_per_cards.disabled=false;
    		  document.SubProdAddFofm.no_nonper_cards.disabled=true;
    		  
    		  alert("PERSONALISED");
    		  }
    	  if(document.SubProdAddFofm.personalized0.checked)
		  {
    	  document.SubProdAddFofm.no_nonper_cards.value=10;
    	  document.SubProdAddFofm.no_per_cards.value=0;
		  document.SubProdAddFofm.no_per_cards.disabled=true;
		  document.SubProdAddFofm.no_nonper_cards.disabled=false;
		  alert("NON PERSONALISED");
		  }
    	  if(document.SubProdAddFofm.personalized2.checked)
		  {
		  document.SubProdAddFofm.no_per_cards.disabled=false;
		  document.SubProdAddFofm.no_nonper_cards.disabled=false;
		  document.SubProdAddFofm.no_nonper_cards.value=10;
		  document.SubProdAddFofm.no_per_cards.value=1;
		  alert("BOTH");
		  }  
     
}

function reset_confirm()
{
 	var r=confirm("Do You Want To Reset");
 	return r;
}

function cardcurrency()
{
		 
		var productid=(document.getElementById('product').value);
		if( productid != "-1"){
			var instd=(document.getElementById('instid').value);
			var url = "callAJaxbinAddSubProdAction.do?prodid="+productid+"&instid="+instd; 
			var result = AjaxReturnValue(url);
			document.getElementById('card_ccy').innerHTML = result;
		} 
		

}

function mccdisplayrequired(mccreq){
	//alert(mccreq);
	if(mccreq==0){
		document.getElementById('mccreg').style.display = 'none';
		document.getElementById('mcclabel').style.display = 'none';
	}else{
		document.getElementById('mccreg').style.display = 'block';
		document.getElementById('mcclabel').style.display = 'block';
	}
}

function merchantreg(merchantreq)
{
	if(merchantreq==1){
		document.getElementById('merchreg').style.display = 'block';
		document.getElementById('merchlabel').style.display = 'block';
	}
	else{
		document.getElementById('merchreg').style.display = 'none';
		document.getElementById('merchlabel').style.display = 'none';
	}
}

	function networkrequired(netwrk)
	{
		var instd=(document.getElementById('instid').value);
		var url = "retrivenetworklistAddSubProdAction.do?networkrequired="+netwrk;
		//alert("url   "+url);
		var result = AjaxReturnValue(url);
		//alert(result);
		if(netwrk==1)
		{
			document.getElementById('netwrkreq_list').innerHTML = result;
			document.getElementById("networklists").style.display = 'block';
		}
		else
		{
			document.getElementById('netwrkreq_list').innerHTML = result;
			document.getElementById("networklists").style.display = 'none';
		}
	}
</script>


		
				<%
					
					String act = (String)session.getAttribute("act");
					
					%>	 
<jsp:include page="/displayresult.jsp"></jsp:include>
	<s:form  action="updateSubproductDetailsAddSubProdAction" name="SubProdAddFofm" namespace="/" autocomplete="off"  >
	<s:hidden name="actflag" id="actflag" value="%{actsubprod}"></s:hidden>
	<s:hidden name="sub_product" id="sub_product" value="%{subprodname}"></s:hidden>
	<s:iterator value="editdetailsList">  
	<s:hidden name="productid" id="productid" value="%{PRODUCT_CODE}"></s:hidden>
	<s:set name="feecodemaster">${FEE_CODE}</s:set>
	<s:set name="limitidmaster">${LIMIT_ID}</s:set>
	<s:set name="servicecodemaster">${SERVICE_CODE}</s:set>
	<s:set name="currencymaster">${CARD_CCY}</s:set>
	 <input type="hidden" name="act"  id="act" value="<%=act%>"> 
	 <s:url id="act"   action="displayAddSubProdAction" ></s:url>
	 
	 <fieldset>
      	<legend><b> Sub Product Name </b></legend> 
      	
 	<table border="0" cellpadding="0" cellspacing="0" class='formtable' width="50%" > 
 		<tr>
			<td align="left" >INSTID</td>
			<td> : 
				 
					<s:textfield readonly="true" name="instid" id="instid" value="%{#session.Instname}" />
				 
			</td>
 		</tr>
 		<tr>
			<td align="left">PRODUCT</td>
			<td> : 
			<s:textfield name="product" id="product" value="%{PRODUCT_NAME}"  readonly="true" /> 				
			</td>
 		</tr>
 
  		<tr>
 			<td id="test" align="left">SUB PRODUCT NAME
 			</td>
 			<td> : 
 				<s:textfield name='subproductname' id='subproductname' value="%{SUB_PRODUCT_NAME}"/> 
 			</td>
 		</tr>	
	</table >
 
 </fieldset>
 
  <fieldset>
      	<legend><b> Sub Product Details </b></legend> 
<table border="0" cellpadding="0" cellspacing="0" class='formtable'  style='margin-top:10px' width="90%">
		<tr>
			<td style="visibility:hidden">
				<b>Fee Code</b>
			</td>
			<td style="visibility:hidden"> : 
				<select name="scheme_scode" id="scheme_scode" >
					<option value="-1">--Select Fee--</option>
				<s:iterator value="scheme_list" var="var_scheme_list">
				<s:set name="feecodeinstitution">${FEE_CODE}</s:set>
					<option value="${FEE_CODE}"
					<s:if test="%{#feecodemaster==#feecodeinstitution}">
					 				 selected="${FEE_CODE}"
					</s:if>					
					>${FEE_DESC}</option>
				</s:iterator>
				</select>
				
			</td>
			
			<td>
				<b>Limit  Code</b>
			</td>
			<td> : 
				<select name="limit_code" id="limit_code" >
					<option value="-1">--Select Limit--</option>
				<s:iterator value="limit_list" var="var_limit_list">
				<s:set name="limitidinstitution">${LIMIT_ID}</s:set>
					<option value="${LIMIT_ID}"
					<s:if test="%{#limitidmaster==#limitidinstitution}">
					 				 selected="${LIMIT_ID}"
					</s:if>
					>${LIMIT_DESC}</option>
				</s:iterator>
				</select>				
			</td>
		</tr>		
		<tr> 		 
			<td>
				<b>Service Code</b>
			</td>
			<td> : 
				<select name="scode" id="scode" >
					<option value="-1">--Select service code--</option>
				<s:iterator value="servicecodelist" var="var_service_code">
				<s:set name="servicecodeinstitution">${SERVICE_CODE}</s:set>
					<option value="${SERVICE_CODE}"
					<s:if test="%{#servicecodemaster==#servicecodeinstitution}">
					 				 selected="${SERVICE_CODE}"
					</s:if>
					>${SERVICE_CODE}</option>
				</s:iterator>
				</select>
				 
				
			</td>
			
			<td>
				<b>Expiry Period(In Months)</b>
			</td>
			<td> : 
				<s:textfield name="expiry" id="expiry" maxlength="3" value="%{EXPIRY_PERIOD}" onkeypress='return  numberOnly(event)'/> 
			</td>
		</tr>
			 
		<tr>
			<td>
				<b>Card Generation Type</b>
			</td>
			<td>
				 : <s:radio list="#{'1':'Personalized','0':'Instant','2':'Both'}" id="personalized" name="personalized" onclick="return nocard();" value="%{PERSONALIZED}"/>
			</td>
			
			<td> <b>PIN Generation Required</b> </td>
			<td> : <s:select name="pin_required" id="pin_required" list="#{'Y':'YES', 'N':'NO'}" headerKey="-1" headerValue="SELECT" value="%{PIN_GEN_REQ}"/></td>
		 </tr>
		
		<tr>
			<td> <b>Maintenance Required</b> </td>
			<td> : <s:select name="maintain_required" id="maintain_required" list="#{'Y':'YES', 'N':'NO'}" headerKey="-1" headerValue="SELECT" value="%{MAINTAIN_REQ}"/></td>
			
		</tr>
</table>
</fieldset>


    
 	   
   
<table border="0" cellpadding="0" cellspacing="4" width="20%" class="formtable" >
		<tr>
		<td>
			<s:submit value="Update" name="submit" id="submit"  onclick="return checkform();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
	</s:iterator>
</s:form>
 
