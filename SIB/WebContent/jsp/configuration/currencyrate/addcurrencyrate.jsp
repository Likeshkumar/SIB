<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>

<script>
function validateCurrency(){
	var currencycode = document.getElementById("currencycode");
	var buyingrate = document.getElementById("buyingrate");
	var sellingrate = document.getElementById("sellingrate");
	
	if( currencycode.value == "-1"){
		errMessage(currencycode, "Select Currency");
		return false;
	} 
	if( buyingrate.value == "" ){
		errMessage(buyingrate, "Enter Buying Rate value ");
		return false;
	}
	
	if( sellingrate.value == "" ){
		errMessage(sellingrate, "Enter Selling Rate value ");
		return false;
	}
	
	return true;
}

function extractNumber(obj, decimalPlaces, allowNegative)
{
	var temp = obj.value;
	
	// avoid changing things if already formatted correctly
	var reg0Str = '[0-9]*';
	if (decimalPlaces > 0) {
		reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
	} else if (decimalPlaces < 0) {
		reg0Str += '\\.?[0-9]*';
	}
	reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
	reg0Str = reg0Str + '$';
	var reg0 = new RegExp(reg0Str);
	if (reg0.test(temp)) return true;

	// first replace all non numbers
	var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
	var reg1 = new RegExp(reg1Str, 'g');
	temp = temp.replace(reg1, '');

	if (allowNegative) {
		// replace extra negative
		var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
		var reg2 = /-/g;
		temp = temp.replace(reg2, '');
		if (hasNegative) temp = '-' + temp;
	}
	
	if (decimalPlaces != 0) {
		var reg3 = /\./g;
		var reg3Array = reg3.exec(temp);
		if (reg3Array != null) {
			// keep only first occurrence of .
			//  and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
			var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
			reg3Right = reg3Right.replace(reg3, '');
			reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
			temp = temp.substring(0,reg3Array.index) + '.' + reg3Right;
		}
	}
	
	obj.value = temp;
}
function blockNonNumbers(obj, e, allowDecimal, allowNegative)
{
	var key;
	var isCtrl = false;
	var keychar;
	var reg;
		
	if(window.event) {
		key = e.keyCode;
		isCtrl = window.event.ctrlKey
	}
	else if(e.which) {
		key = e.which;
		isCtrl = e.ctrlKey;
	}
	
	if (isNaN(key)) return true;
	
	keychar = String.fromCharCode(key);
	
	// check for backspace or delete, or if Ctrl was pressed
	if (key == 8 || isCtrl)
	{
		return true;
	}

	reg = /\d/;
	var isFirstN = allowNegative ? keychar == '-' && obj.value.indexOf('-') == -1 : false;
	var isFirstD = allowDecimal ? keychar == '.' && obj.value.indexOf('.') == -1 : false;
	
	return isFirstN || isFirstD || reg.test(keychar);
}
function getPreviousRate(currencycode){
	var url = "getPreviousRateCurrencyRateAction.do?currencycode="+currencycode;
	var result = AjaxReturnValue(url); 
	document.getElementById('prevrate').innerHTML =result;
	document.getElementById('prevrate').style.display = 'table-row';
	
}
</script>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="currencyrateform" action="saveCurrencyCurrencyRateAction.do" autocomplete="off" onsubmit="return validateCurrency()" >
<%-- <input type="hidden" name="act"  id="act" value="<%=act%>">  --%>
  
 	<table border="0" cellpadding="0" cellspacing="0" width="80%" class="table">
 		<tr><td width="70%" align="right">
	 		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="right">
	 		<tr>
	 		 	<td> Currency</td>
	 			<td><select id="currencycode"  name="currencycode" onchange="getPreviousRate(this.value)">
	 					<option value="-1">Select Currency</option>
						 <s:iterator value="currencylist">   
						 	<option value="<s:property value="NUMERIC_CODE"/>"><s:property value="CURRENCY_CODE"/></option>
						</s:iterator>   
					 </select>
				</td>	 
	 		</tr>
			<tr>
	 			<td>Buying Rate</td> 
				<td><s:textfield name="buyingrate" id="buyingrate" maxlength="12" onblur="extractNumber(this,2,false);" onkeyup="extractNumber(this,2,false);" onkeypress="return blockNonNumbers(this, event, true, false);" /></td> 
				 
			</tr> 
			<tr>
	 			<td>Selling Rate</td> 
				<td><s:textfield name="sellingrate" id="sellingrate" maxlength="12"  onblur="extractNumber(this,2,false);" onkeyup="extractNumber(this,2,false);" onkeypress="return blockNonNumbers(this, event, true, false);" /></td> 
			</tr> 
			</table>
		    </td>
			 <td>
				 <table style="display: none;" id="prevrate" border="0" cellpadding="0" cellspacing="0" width="30%" class="table" align="left">
				 <s:iterator value="prevratelist"/>
				 	<tr>
				 		<td>${BUYINGRATE}--><s:property value="prevratelist" /></td>
				 	</tr>
				 	<tr>
				 		<td><s:property value="sellrate" /></td>
				 	</tr>
				 </table>
			 </td>
		 </tr>
	</table>

	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
			<td> <s:submit value="Update Rate" name="submit" id="submit" /> </td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td>
		</tr>
	</table>
</s:form> 
</body>
