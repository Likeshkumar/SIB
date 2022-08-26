<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function coppyEmbosstoEncode(event,value){
	//alert("Event "+event+"  value===>"+value);
	var s = alphabets(event);
	//alert(" ssss "+s);
	if(s==true)
		{
			//alert("Value is Aplpha"+value);
			document.getElementById("encode").value = value;
			return true;
		}else{
			//alert("Invalid");
			return false;
		}
}
function validateValues()
{
	var product  = document.getElementById("cardtype");
	var subproductlist = document.getElementById("subproductlist");
	var feelist = document.getElementById("feelist");
	
	if(product.value == "-1")
	{
		errMessage(product,"The Please select the product");
		return false;
	}	
	if(subproductlist.value == "-1")
	{
	errMessage(subproductlist,"The Please select the Sub-product");
	return false;
	}
	if(feelist.value == "-1")
	{
	errMessage(feelist,"The Please select the Fee");
	return false;
	}
	return true;
}


</script>
<script>

function getListOfSubProudcts(cardtype)
{	
	 
	var parentcard=(document.getElementById('cardtype'));
	//alert("THe Card Type is ---> "+cardtype.value);
	if(parentcard.value == "-1")
	{
		errMessage( parentcard, "Please Select Card " );
		return false;
	}
	else
	{			
		var url="getPersonalSubProductetailsNewReportgenerationAction.do?prodid="+parentcard.value+"&cardtype="+cardtype;
		var response=AjaxReturnValue(url); 
		//alert(response);
		document.getElementById('subproductlist').innerHTML=response;
	}
}

function Gettingfeecodelist()
{	
	//alert("HAi");
	var subproduct=(document.getElementById('subproductlist'));
	//alert("THe subproductlist is ---> "+subproduct.value);
	if(subproduct.value == "-1")
	{
		errMessage( subproduct, "Please Select Subproduct " );
		return false;
	}
	else
	{			
		var url="getFeecodeReportgenerationAction.do?subprodid="+subproduct.value;
		var response=AjaxReturnValue(url);
		document.getElementById('feeajax').innerHTML = " : "+response;
		document.getElementById('feename').innerHTML = "Select Fee";
		document.getElementById('feelist').onchange = "";
		// var list = document.getElementById('feelist');		
		return true;
	}
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="feeReportpdfgeneratorReportgenerationAction"  name="personalorderform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount" class="formtable" >
    <tr>
		<td class="fnt">
		Select Product
		</td>
		<td>
 				: <select name="cardtype" id="cardtype" onchange="return getListOfSubProudcts(this.value)" >
	 				<option value="-1" selected="selected">--Select Product--</option>
	 				<option  value="000" >ALL</option>
	 				<s:iterator  value="prodlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
   <tr> 
    	<td id="subname" class="fnt">
    		Select Sub-Product
    	</td>
    	<td class="fnt">
			<div id="ajax"> :
    			<select name="subproductlist" id="subproductlist" onchange="Gettingfeecodelist()">
    				<option value="-1">--Select Sub-Product--</option>
    			</select>
    		</div>
    	</td>
    </tr>    
    <tr> 
    	<td id="feename" class="fnt">
    		 Select Fee 
    	</td>
    	<td class="fnt"> 
 			<div id="feeajax"> :
    			<select name="feelist" id="feelist">
    				<option value="-1">--Select Fee--</option>
    			</select>
    		</div>
    		
 		</td>
    </tr>	
</table>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>			
			<s:submit value="Generate PDF" name="next_process" id="next_process" onclick="return validateValues();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>