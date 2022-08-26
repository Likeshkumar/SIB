<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script>
	 
	
	function showEntertype( entertype ){
		 
		var cardrow = document.getElementById("cardrow");
		var customeridrow = document.getElementById("customeridrow");
		if( entertype=="cardno"){
			cardrow.style.display = 'table-row';
			customeridrow.style.display = 'none';
		}else{
			customeridrow.style.display = 'table-row';
			cardrow.style.display = 'none';
			
		}
	}
	
	function validateCard(){
		var cardno = document.getElementById("cardno");
		var customerid = document.getElementById("custid");
		if( cardno ){
			if( cardno.value == "" ){ errMessage(cardno, "Card Number is empty..."); return false; }
		}
		parent.showprocessing();
		return true; 
	}
</script>
<div align="center">
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="editViewCustomerActionInstCardRegisterProcess.do" method="post" autocomplete="off" onsubmit="return validateCard();">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef" cellpadding="0" align="center" width="40%" cellspacing="0"  >
		<tr >
			<td align="center" colspan="4" >
	         	
	         		<input type="radio" name="inputtype" id="cardno1" value="cardno" checked  onclick="showEntertype(this.value)"/> Card No
	          
	          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          		<input type="radio" name="inputtype" id="customerid" value="customerid" onclick="showEntertype(this.value)" />Customer id
	         </td>
			 	
		</tr>
		
		<tr id="cardrow">
			 <td align="center" colspan="2">&nbsp;
			 	<div style="width:400px;" id='divcard'>
			 		 Card No &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  <input type='text' name='cardno' id='cardno' maxlength='19' />
			 	</div>
			</td>  
		</tr>
		
		<tr id="customeridrow" style="display:none">
			 <td align="center" colspan="2" > &nbsp; 
			 	<div style="width:400px;" id='divcust'>
			 		  Customer Id :  <input type='text' name='custid' id='custid'   value="" />
			 	</div>
			</td>  
		</tr>
		
		<tr id="cardrow">
			 <td align="center" colspan="2">&nbsp;
			 	<div style="width:400px;" id='divcard'>
			 		 Action &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : 
					<select name="actiontype" id="actiontype">
					<option value="VIEW"> View </option>
					<option value="EDIT"> Edit  </option>
					</select>
			 	</div>
			</td>  
		</tr>
		
	 
	</table>

	<table border="0" cellpadding="0" cellspacing="0" width="15%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" /> 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</form>
</div>