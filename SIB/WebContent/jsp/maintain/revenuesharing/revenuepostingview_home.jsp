<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<script>
	
	function showCommissionType( commissionfor ){
		var optionval = "<option value='-1'>-SELECT-</option>";
		var commissiontype = document.getElementById("commissiontype");
		 
		if( commissionfor =="$FLOAT"){
			optionval += "<option value='$EXP'>Expense</option> <option value='$REV'>Revenue</option>";
		}else{
			optionval += "<option value='$REV'>Revenue</option>";
		}
		 
		commissiontype.innerHTML=optionval;
	}
	
	function validateForm( comfor ){
		 
		/* 
		var bankcode = document.getElementById("corp_prod");
		var topup = document.getElementById("topup");
		var cardtocard = document.getElementById("cardtocard");
		var walletupload = document.getElementById("walletupload");
		
		if( !topup.checked && !cardtocard.checked){
			 errMessage(topup, "Select the action.....");return false;
		}
		if( topup.checked  ){
			if( bankcode ){
				if( bankcode.value == "-1"){ errMessage(bankcode, "Select Corporate ...");return false;}
			}  
		}
		
		if( walletupload.value == ""){
			errMessage(walletupload, "Upload file is empty...");return false;
		} */
		
		parent.showprocessing();
	}
	
 
		
	 
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="revPostingViewRevenueSharing.do"  name="orderform"  enctype="multipart/form-data"  onsubmit="return validateForm()"  autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="40%" class="formtable">	 
	 
	 <tr> <td> Bank Name </td>
	 <td> : <s:select name="bin" id="bin"  list="revbean.binlist" listKey="BIN" listValue="BIN_DESC" headerKey="-1" headerValue="-SELECT-" /> </td></tr>
	
	 
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return validFilter()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 