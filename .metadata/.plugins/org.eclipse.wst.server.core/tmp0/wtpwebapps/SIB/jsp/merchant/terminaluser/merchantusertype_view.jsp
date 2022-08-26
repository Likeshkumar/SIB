<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" /> 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>
	function getMerchantDetails(){
		 
		var merchantcode = document.getElementById("merchantcode");
		if( merchantcode.value == "" ){
			errMessage(merchantcode, "Merchant code should not be empty");
			return false;
		} 
		errMessage(merchantcode, "Processing....");  
	}
	 
</script>

<style>
	div.divcont{
		clear:left;"build/classes/com/ifp/util/Licensemanager.class"
	}
a {
	color: blue;
}
 
	@media print {
      body {
          width: 100%;
      }
	}
 
</style>
 

<jsp:include page="/displayresult.jsp"></jsp:include>
<table border='0' class="orderform" width="30%" align="center"> 
<s:form action="viewmerchantUserTypeMerchantRegister.do" name="orderform" onsubmit="return validateForm()"  autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="100%"  align="center">
		
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="4" width="100%" class="formtable"
					align="center">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="50%">
								<tr>
									<td colspan='2' style='text-align: center'>Mernchant Code 
									</td>
									<td> : <input type='text' name='merchantid' id='merchantcode' maxlength="16" /></td> 
									
									<td> <input type="submit" name="submit" value="Get Details" onclick="getMerchantDetails();"/>  </td>
									 
								</tr>
							 
							 </table> 
				</table>
			</td>
		</tr>


		<tr>
			<td>
				<div id="displayresult1">&nbsp;</div>
			</td>
		</tr>

	</table>

</s:form>

