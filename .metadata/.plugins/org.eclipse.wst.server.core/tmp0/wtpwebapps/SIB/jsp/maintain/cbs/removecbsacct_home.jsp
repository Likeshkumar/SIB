<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">

function validation(){
	var cardno = document.getElementById("cardnum");
	
	if(cardno.value==""){
		errMessage(cardno, "Enter the Card Number...!");
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
function confirmRemove(){
	
	if(!selectallvalidationFromIndex()){
		return false;
	}
	if( !confirm("Do you want to Remove Account...") ){
		return false;
	}
	
}
</script>
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="removeAccountNumberCbsAccount.do"  name="orderform" autocomplete = "off"  namespace="/">
	<s:hidden name="act" id="act" value="%{act}"/>
	<table border="0" cellpadding="0" cellspacing="4" width="50%" class="formtable">
	
				<tr id="cardnumdiv">
					<td>Enter Card No. :</td>
					<td><input type="text" name="cardnum" id="cardnum" maxlength="19" onkeypress='return isNumberKey(event)'/></td>
				</tr>
	    
	</table>	
	
	<table width='20%'>
		<tr align="center">
			<!-- <td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" onclick="return validation();"/></td> -->
					  <input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search"/></td>
			
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
	</table>
</s:form>

<s:if test="!cbsbean.accounttypedetails.isEmpty()" >
<s:form action="removeAddonaccountnoCbsAccount.do"  name="orderform" autocomplete = "off"  namespace="/">
			<s:hidden value="5" name="" id="allowedaccount" />
			<s:hidden name="cardno" id="cardno" value="%{cbsbean.cardno}"/>
			<s:hidden name="act" id="act" value="%{act}"/>
			<s:hidden name="padssenable" id="padssenable" value="%{padssenable}"/>
				<div id='fw_container'>
				
				<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable" id="AccountInformation">
					<s:if test="!cbsbean.accounttypedetails.isEmpty()" >
		
				 	<tr>
				 		<th>S.no</th>
				 		<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
					 	<th>Account Type</th>  
					 	<th>Account SUB Type</th>
					 	<th>Currency</th>
					 	<th>Account Number</th>
					 	<th>Account Type</th>  
				 	</tr>
				 	
				 	<%int i=0; %>
		 			<s:iterator value="cbsbean.accounttypedetails" status="incr">
		 			<%i++; %>
		 				<tr>
		 					<td style="text-aligh:center"><%=i%></td>
		 					<s:if test='ACCOUNTFLAG!="Primary"'>
		 						<td> <s:checkbox name="personalrefnum"  id="personalrefnum%{#incr.index}" fieldValue="%{ACCOUNTNO}"/>  </td>
		 					</s:if><s:else>
		 						<td>&nbsp;</td>
		 					</s:else>
		 					<td style="text-aligh:center">${ACCOUNTTYPEDESC}</td>
							<td style="text-aligh:center">${PRODUCTCODEDESC}</td>
							<td style="text-aligh:center">${CURRCODEDESC}</td>
							<td style="text-aligh:center">${ACCOUNTNO} 
							<td style="text-aligh:center">${ACCOUNTFLAG}</td>
		 				</tr>
		 			</s:iterator>
		 			<br>
				<table width='20%'>
					<tr align="center">
					  <input type="hidden" name="token" id="csrfToken" value="${token}">
						<td colspan='2'><input type="submit" name="findcard" id="findcard" value="Remove Account" onclick="return confirmRemove();"/></td>
					</tr>
				</table>
	</s:if>
		<s:else>
			<tr>
				<th>NO ACCOUNTS CONFIGURED FOR GIVEN CARD NUMBER</th>
			</tr>
		</s:else>
	</table>
	</div>
</s:form>
</s:if>	
</body>
</html>