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
	function showProcessing(){
		parent.showprocessing();
	}
	
	function showDebitUser( debituser ){
	 
		
		var trdebituser = document.getElementById("trdebituser");
		var trcardno = document.getElementById("trcardno");
		
		if( debituser == "$EXIST"){
			trdebituser.style.display = "table-row";
		}else if( debituser == "$NEW"){ 
			trdebituser.style.display = "none";
			trcardno.style.display = "none";
		}
		
		if( debituser == "$DEBITUSER"){
			trdebituser.style.display = "table-row";
			trcardno.style.display = "table-row";
		}if( debituser == "$NOTDEBITUSER"){
			trdebituser.style.display = "table-row";
			trcardno.style.display = "none";
		} 
	}
	
	function validateForm( ){
		var newcust =  document.getElementById("newcust");
		var existcust =  document.getElementById("existcust");
		var debituser =  document.getElementById("debituser");
		var notdebituser =  document.getElementById("notdebituser"); 
		var debitcardno =  document.getElementById("debitcardno");
		
		 
		if( newcust.checked == false && existcust.checked == false ){
			errMessage(newcust, "Please check customer type option");
			return false;
		}
		
		if( existcust.checked   ){ 
			if( debituser.checked == false && notdebituser.checked == false ){
				errMessage(debituser, "Please check already debit card user ");
				return false;
			}
		}
		
		if( debituser.checked   ){ 
			if( debitcardno.value == ""){
				errMessage(debitcardno, "Enter debit card number");
				return false;
			} 
			if( debitcardno.value.length < 16 ){
				errMessage(debitcardno, "Invalid debit card number");
				return false;
			} 
		}
		
		return true;
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
 
<s:form action="customerAddDataCreditCustRegisteration.do"  name="orderform" onsubmit="return showProcessing()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="10" cellspacing="0" width="60%" class="formtable">	 
<tr>
	<td> Customer type </td>
	<td>  : <input type="radio" name="customertype" id="newcust" value="$NEW" onclick="showDebitUser(this.value)" /> New Customer  </td>
	<td>  <input type="radio" name="customertype" id="existcust" value="$EXIST"  onclick="showDebitUser(this.value)"  /> Partial Entry Customer  </td>
</tr>

<tr id="trdebituser" style="display:none" >
	<td> Already Debit Card User </td>
	<td>   : <input type="radio" name="debitcarduser" id="debituser" value="$DEBITUSER"   onclick="showDebitUser(this.value)"/> Yes  </td>
	<td>  <input type="radio" name="debitcarduser" id="notdebituser" value="$NOTDEBITUSER"  onclick="showDebitUser(this.value)" /> No  </td>
</tr>

<tr id="trcardno" style="display:none">
	<td> Enter Debit Card Number </td>
	<td colspan="2"> :  <s:textfield name="debitcardno" id="debitcardno" onkeypress="return numerals(event)" maxlength="19" />  </td>
</tr> 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Next" name="order" id="order" onclick="return validateForm()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 