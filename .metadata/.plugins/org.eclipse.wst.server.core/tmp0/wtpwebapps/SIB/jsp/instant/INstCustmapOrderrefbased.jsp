<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script>
	function kycEnable( kyc ){
		document.getElementById("order").hidden=false;
		document.getElementById("cancel").hidden=false;
		var cardno = document.getElementById("orderno");
		var accountno = document.getElementById("accountno");
		var divcard = document.getElementById("divcard");
		var divcust =  document.getElementById("divcust");
	  
		if( kyc =='new'){
			divcard.style.display="block"; 
			divcust.style.display="none"; 
			document.getElementById("regtype").value="new";
			 
		}else{
			divcard.style.display="block"; 
			divcust.style.display="block"; 
			document.getElementById("regtype").value="kyc"; 
		}
		cardno.focus();
		 
		return false;
	}
	
	function validateCard(){
		var cardno = document.getElementById("orderno");
		var accountno = document.getElementById("accountno");
		if( cardno ){
			if( cardno.value == "" ){ 
				errMessage(cardno, "Order Ref Number is empty..."); 
				return false; 
				}
		}
		if( accountno ){
			if( accountno.value == "" ){ 
				errMessage(accountno, "Account  Number is empty..."); 
				return false; 
				}
		}
		parent.showprocessing();
		return true; 
	}
</script>
<div align="center">
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:if test="hasActionErrors()">
   <div style="color:red">
      <s:actionerror/>
   </div>
</s:if>
<form action="newCustRegInstCardRegisterProcess.do" method="post" autocomplete="off" onsubmit="return validateCard();">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef" cellpadding="0" align="center" width="40%" cellspacing="0"  >
		<!-- <tr >
			<td align="center" colspan="4" >
	         	
	         		<input type="radio" name="inputtype" id="cardno1" value="cardno" checked  onclick="showEntertype(this.value)"/> New Customer
	          
	          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          		<input type="radio" name="inputtype" id="customerid" value="customerid" onclick="showEntertype(this.value)" />Existing Customer
	         </td>
			 	
		</tr> -->
		
		<tr id="cardrow">
			 <td align="center" colspan="2">&nbsp;
			 	<div style="width:400px;" id='divcard'>
			 		 Account No &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  <input type='text' name='accountno' id='accountno' maxlength='19' />
			 	</div>
			 	
			 	
			 	
			</td>  
		</tr>
		
		
		<tr id="cardrow">
			 <td align="center" colspan="2">&nbsp;
			 	<div style="width:400px;" id='divcard'>
			 		 Order Ref Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  <input type='text' name='orderno' id='orderno' maxlength='19' />
			 	</div>
			 	
			 	
			 	
			</td>  
		</tr>
		
		
	 
	</table>

	<table border="0" cellpadding="0" cellspacing="0" width="15%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return validateCard()"/> 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>


</form>
<%-- 
<form action="" method="post" autocomplete="off" onsubmit="return validateCard();">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef" cellpadding="0" align="center" width="40%" cellspacing="0"  >
		<tr >
			<td align="center" >
	         	
	         		<input type="submit" value="&nbsp;&nbsp;&nbsp;&nbsp;KYC&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" alt="New Customer" onclick="return kycEnable('new')" style="background:blue;font-weight:bold;color:#fff" title="If the customer New. Click here ">
	         		
	          
	         </td>
			 <td align="center" >
			 	
			 		<input type="submit" value="Existing Customer" alt="KYC Customer" onclick="return kycEnable('kyc')" style="background:blue;font-weight:bold;color:#fff"  title="If Existing customer. Click here ">
			  
			 </td>		
		</tr>
		
		<tr >
			 <td align="center" colspan="2">&nbsp;
			 	 
			 		 Card No &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  <input type='text' name='cardno' id='cardno' maxlength='19' />
			  
			</td>  
		</tr>
		
		<!-- <tr >
			 <td align="center" colspan="2" > &nbsp; 
			 	<div style="width:400px;display:none" id='divcust'>
			 		  Customer Id :  <input type='text' name='custid' id='custid'   value="" />
			 	</div>
			</td>  
		</tr> -->
	 
	</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" hidden="true"/> 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  hidden="true" class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</form> --%>
</div>