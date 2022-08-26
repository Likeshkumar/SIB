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


<s:form action="viewTerminalListMerchMonitor.do" name="orderform"  method="get" onsubmit="return validateRecords()" autocomplete="off" namespace="/"> 
	<table border="0" cellpadding="0" cellspacing="0" width="60%" class="formtable"	align="center">
		<tr>
			 <td> Merchant Type : </td>
			 <td> <s:select list="monitorbean.merchanttypelist" name="merchanttypeid" id="merchanttypeid"
			 		listKey="MERCHANT_TYPE_ID" listValue="MERCHANT_TYPE_NAME" headerKey="ALL" headerValue="ALL"
			 		></s:select> </td>
	</tr>
	<tr>		 		
			 <td> Terminal Status : </td>
			 <td>
			 	  
			 	<s:select list="#{'$ALL':'ALL-Terminals','$ACTIVE':'Active-Terminals','$INACTIVE':'In-Active Terminals'}" id="termstatus" name="termstatus" value="%{cardregbean.mstatus}" onchange="showSpouse( this.id )"/> 
			 </td>
		</tr> 
	</table> 
	
	<table border="0" cellpadding="0" cellspacing="0" width="20%" class="submittable">
		<tr>
			<td><s:submit value="Submit" name="order" id="order" onclick="return validateConfiguration();"  /></td>
			<td><input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" /></td>
		</tr>
	</table>

</s:form>

