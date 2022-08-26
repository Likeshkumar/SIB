<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/table.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">
	

function validation(){	
	   var cardnum = document.getElementById("cardnum");
	   var cardnumberlength = document.getElementById("cardnmberlength");
	  if(cardnum.value=="")
	          		{
	          			errMessage(cardnum,"Enter card number");
	          			return false;
	          		}
	  if(cardnum.value.length!=cardnumberlength.value)
		{
			//alert(customeridno.value.length+"-----"+custidlength.value);
		errMessage(cardnum, "Card number Should Be "+cardnumberlength.value+" Digit!");
		return false;
		}
}
function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;
   return true;
}
function validateNumber (field,id,enteredchar)
{
	charvalue= "0123456789";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
    	
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
       	//alert(document.getElementById(id).value.charAt(i));
       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

</script>
<style>
.textcolor
{
color: maroon;
font-size: small;
align:center;
}

</style>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ");  
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addonCardSuplActionAddonCardSuplAction.do"  name="orderform" autocomplete = "off"  namespace="/" >
<input type="hidden" id = "cardnmberlength" value="16" />
	
	<table border="0" cellpadding="0" cellspacing="4" width="50%" class="formtable" >
	
				<tr id="cardnumdiv" >
					<td>Enter Card No. :</td>
					<td><input type="text" name="cardnum" id="cardnum" maxlength="16" onkeypress='return isNumberKey(event)'/></td>
				</tr>
	  
	</table>	   
	
	<table width='20%'>
		<tr align="center">
			<!-- <td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" /></td> -->
				  <input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" /></td>
			
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="confirmCancel()"/></td>
		</tr>
	</table>
	</s:form>
	
	<br><br>
	
	<s:form action="generateAddoncardActionAddonCardAction.do"  name="orderform" autocomplete = "off"  namespace="/">
	<div id='fw_container'>	
	
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='pretty' style='border: 1px solid #454454' >
	<s:if test ="%{!carddetlist.isEmpty()}">
	<tr> 
	<th>Card No</th> 
	<th>ACCOUNT NO</th>
	<th>Mobile No.</th>  
	<th>CUST NAME</th> 
	<th>DOB </th> 
	<th>CUSTOMER ID </th> 
	<th>BIN</th>  
	<th>CARD STATUS</th> 
	<th>PRODUCT</th>  
	<!-- <th>ORIGINAL CARDNO</th> -->  </tr>
	</s:if>
	<s:iterator value="carddetlist">	
	<s:if test='padssenabled=="Y"' >
	<input type="hidden" name="cardnumber" value="${HCARD_NO}"/>
	</s:if>
	<s:else>
	<input type="hidden" name="cardnumber" value="${CARD_NO}"/>
	</s:else>
	<input type="hidden" name="productcode" value="${PRODUCT_CODE}"/>
	<input type="hidden" name="accountno" value="${ACCOUNT_NO}"/>
							<tr> 
								<td style="text-align:center">${MCARD_NO}</td>
								<td style="text-align:center">${ACCOUNT_NO}</td>
								<td style="text-align:center">${MOBILE}</td>
								<td style="text-align:center">${NAME}</td>
								<td style="text-align:center">${DOB}</td>
								<td style="text-align:center">${CIN}</td>
								<td style="text-align:center">${BIN}</td>
								<td style="text-align:center">${STATUS_DESC}</td>
								<td style="text-align:center">${PRODUCT_DESC}</td>
								<%-- <td style="text-align:center">${ORG_CARDNO}</td> --%>
								
							</tr>
						</s:iterator> 
	</table>
	
	<table width='20%'>
	<s:if test ="%{!carddetlist.isEmpty()}">
		<tr align="center">
		<input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="generatecard" id="generatecard" value="Generate Add-on Card" /></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
		</s:if>
	</table>
	
	
</div>


</s:form>
