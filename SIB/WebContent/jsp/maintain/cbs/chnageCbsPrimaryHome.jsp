<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/table.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">
	

function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;
   return true;
}
function chnagePrimaryAccount()
{
	var primarysec = document.getElementsByName("primarysec");
	var allowedaccount = document.getElementById("allowedaccount");
	
	var val = 0;
	for(var i=0;i<primarysec.length;i++){
	if(primarysec[i].value=='P')
		{
		val=val+1;
		}
	}
	if(val>1){
	errMessage(allowedaccount,"You Cant able to select More than one Primary Account ") ; 
	return false; 
	}
	if(val<1){
		errMessage(allowedaccount,"Atleast one Account should be Primary Account ") ; 
		return false; 
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
<s:form action="getAccountnoDetailschnageCbsPrimary.do"  name="orderform" autocomplete = "off"  namespace="/">
	<table border="0" cellpadding="0" cellspacing="4" width="50%" class="formtable">
	  
				<tr id="cardnumdiv" >
					<td>Enter Card No. :</td>
					<td><input type="text" name="cardnum" id="cardnum" maxlength="19" onkeypress='return isNumberKey(event)'/></td>
				</tr>
	    
	</table>	
	
	<table width='20%'>
		<tr align="center">
			<!-- <td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" onclick="return findcard_validation();"/></td> -->
			<input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" /></td>
			
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
	</table>
	</s:form>
	
	<s:if test="!accounttypedetails.isEmpty()" >	   
	<s:form action="chengeprimchnageCbsPrimary.do"  name="orderform" autocomplete = "off"  namespace="/">
	<s:hidden value="5" name="" id="allowedaccount" />
	<s:hidden name="cardno" id="allowedaccount" value="%{cardno}"/><div id='fw_container'>	
	
	<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable" id="AccountInformation">
	
			<tr>
			<td>CUSTOMER NAME :</td>
				<td style="text-aligh:center">
				${custname}
				</td>
			</tr>
	
		 	<tr>
		 	<th>Account Type</th>  
		 	<th>Account SUB Type</th>
		 	<th>Currency</th>
		 	<th>Account Number</th>     
		 	<th>Account Type</th>
		 	<th>
		 	
		 	</th>  
		 	</tr>
		 	<%int i=0; %>
		 	<s:iterator value="accounttypedetails">   
		 	
		 	<%i++; %>
		 	<tr> 
		 	<input type="hidden" name = "accounttypevalue" id="accounttypedesc_${ACCOUNTNO}" value='${ACCOUNTTYPE}'/> 
		 	<input type="hidden" name = "productcodevalue" id="accounttypedesc_${ACCOUNTNO}" value='${PRODUCTCODE}'/>
		 	<input type="hidden" name = "currencycodevalue" id="accounttypedesc_${ACCOUNTNO}" value='${CURRCODE}'/>
		 	<input type="hidden" name = "accountnovalue" id="accounttypedesc_${ACCOUNTNO}" value='${ACCOUNTNO}'/>
		 		<td style="text-aligh:center">${ACCOUNTTYPEDESC}</td>
				<td style="text-aligh:center">${PRODUCTCODEDESC}</td>
				<td style="text-aligh:center">${CURRCODEDESC}</td>
				<td style="text-aligh:center">${ACCOUNTNO}</td>    
				
				 
					
				
				<td style="text-aligh:center">
					
				<select id="primarysec_${ACCOUNTNO}" name="primarysec" onchange="chnagePrimaryAccount(this.id,'${ACCOUNTNO}')">
								<option value="P"
								<s:if test='ACCOUNTFLAG_FLAG=="P"'>
								selected
								</s:if>
								>Primary</option>
								<option value="S"
								<s:if test='ACCOUNTFLAG_FLAG=="S"'>
								selected
								</s:if>
								>Secondary</option>
					</select>
				${ACCOUNTFLAG_FLAG}</td>
				<th>
		 		
		 		</th> 
 			</tr>
 			</s:iterator>
 			
 			</table>
 			
 			<table width='20%'>
		<tr align="center">
		         <tr><input type="hidden" name="token" id="csrfToken" value="${token}"></tr>
			<td colspan='2'><input type="submit" name="saveAccount" id="saveAccount" value="Submit" onclick="return chnagePrimaryAccount();"/></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
	</table>
	
	
</div>


</s:form>
</s:if>
