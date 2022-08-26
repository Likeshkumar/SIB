<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>
	function validateRecords(){
		 
		var merchname = document.getElementById("merchname");
		var cashinoutreq = document.getElementById("cashinoutreq");
		var loyaltyreq = document.getElementById("loyaltyreq");
		var discountreq = document.getElementById("discountreq");
		var commissionreq = document.getElementById("commissionreq");
		var feereq = document.getElementById("feereq");
		
		if( merchname ) { if( merchname.value == "" ) { errMessage(merchname, "Enter Merchant type name ") ; return false;    } }
		if( cashinoutreq ) { if( cashinoutreq.value == "-1" ) { errMessage(cashinoutreq, "Select Cash In/Out Required") ; return false;    } }
		if( loyaltyreq ) { if( loyaltyreq.value == "-1" ) { errMessage(loyaltyreq, "Select Loyalty  Required") ; return false;    } }
		if( discountreq ) { if( discountreq.value == "-1" ) { errMessage(discountreq, "Select Discount Required") ; return false;    } }
		if( commissionreq ) { if( commissionreq.value == "-1" ) { errMessage(commissionreq, "Select Commission Required") ; return false;    } }
		if( feereq ) { if( feereq.value == "-1" ) { errMessage(feereq, "Select Fee Required") ; return false;    } }
		
		parent.showprocessing();
		 
	}
</script>

<style>
a {
	color: blue;
}
</style>
 

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="deleteMerchantTypeMerchantRegister.do" name="orderform" onsubmit="return validateRecords()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="100%"
		align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="100%"
					align="center">
					<s:iterator value="mregbean.merchanttypeattr">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="80%">
							<s:hidden name="merchanttypeid" value="%{MERCHANT_TYPE_ID}" />
								<tr>
									<td colspan='2' style='text-align: center'>Merchant Type Name
									</td>
									<td> : 
										${MERCHANT_TYPE_NAME}
									  </td>
									<td colspan='2' style='text-align: center'>Cash in/out Txn Allowed 
									</td>
									
									<td> :  
										<s:if test='%{CASHINOUT_REQ =="Y"}'>Yes</s:if>
										<s:else>No</s:else>
									  </td>
								</tr>
							 
							 
								 <tr>
									 
									<td colspan='2' style='text-align: center'> Loyalty Required 
									</td>
									
									<td> :
										<s:if test='%{LOYALTY_REQ =="Y"}'>Yes</s:if>
										<s:else>No</s:else>
									
									</td>
									
									<td colspan='2' style='text-align: center'> Commission Required 
									</td>
									
									<td> :  
										<s:if test='%{COMMISSION_REQ =="Y"}'>Yes</s:if>
										<s:else>No</s:else>
									  </td>
									
								</tr>
								
								 <tr>
									 
									<td colspan='2' style='text-align: center'> Discount Required 
									</td>
									
									<td> : 
										<s:if test='%{DISCOUNT_REQ =="Y"}'>Yes</s:if>
										<s:else>No</s:else>
									 </td>
									
									<td colspan='2' style='text-align: center'> Fee Required
									</td>
									
									<td> :  
										<s:if test='%{FEE_REQ =="Y"}'>Yes</s:if>
										<s:else>No</s:else>
									 </td>
									
								</tr>
							</table>
						</td> 
					</tr>
					<tr>
						<table border="0" cellpadding="0" cellspacing="0" width="20%">
							<tr>
								<td><s:submit value="Delete" name="order" id="order" onclick="return validateConfiguration();"  /></td>
								<td><input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" /></td>
							</tr>
						</table>
					</tr>


				</table>
				</s:iterator>
			</td>
		</tr>


		<tr>
			<td>
				<div id="displayresult1">&nbsp;</div>
			</td>
		</tr>

	</table>

</s:form>

