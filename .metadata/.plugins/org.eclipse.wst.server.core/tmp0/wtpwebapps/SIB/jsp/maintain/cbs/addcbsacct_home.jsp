<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/table.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">
	
var rowNum = 0;

function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;
   return true;
}
function addRow(val) {
	var accttypelist = getAcctTypeList();
	var currencylist = getCurrencyList();
	
	var table = document.getElementById("AccountInformation");
	var allowedaccount = document.getElementById("allowedaccount");
	var accountnolength = document.getElementById("accountnolength");
	//table.style.overflow = "scroll";
	
	
	    
	
	//document.getElementById("button").style.overflow = "scroll";
	
	var rowCount = table.rows.length; 
	var lessrow = parseInt(rowCount-2);
	
	if(lessrow>=6)
		{
		errMessage(allowedaccount,"You Cant able to add More than 5 Secondary Account") ; 
		return false; 
		}
	
	var row = table.insertRow(rowCount);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);  
    var cell5 = row.insertCell(4);  
    var cell6 = row.insertCell(5); 
       
    
    cell1.innerHTML = "<select id='accounttypevalue_"+lessrow+"' name='accounttypevalue' onchange='getAcctSubTypeList("+lessrow+")'>"+accttypelist+"</select>";
    cell2.innerHTML = "<select id='accountsubtypevalue_"+lessrow+"' name='accountsubtypevalue'><option value='-1'>-Select AccSubType-</option></select>";
    cell3.innerHTML = "<select id='accountccyvalue_"+lessrow+"' name='accountccyvalue'>"+currencylist+"</select>";
    cell4.innerHTML = "<input type='text' name = 'accountnovalue' id='accountnovalue_"+lessrow+"' maxlength="+accountnolength.value+" value='' onkeypress='return numerals(event)' />";   
    cell5.innerHTML = "<input type='text' name = 'primarysec' id='primarysec_"+lessrow+"' value='Secondary' readonly='true'/>";
    cell6.innerHTML = "<input type='button' name = 'delete' id='delete_"+lessrow+"' value='Delete' onclick='removeRow("+rowCount+")'/>";    
}

function removeRow(rnum) {
	var table = document.getElementById("AccountInformation");

	var rowCount = table.rows.length;
	//alert('rowCount'+rowCount+rnum);
	var lessrow = parseInt(rowCount-1);
	//alert(rowCount);  
	table.deleteRow(lessrow);	
}


function getProductList( branchcode ){  
	var url = "getProductListByBranchDebitCustomerRegister.do?branchcode="+branchcode; 
	var result = AjaxReturnValue(url);
	document.getElementById("productcode").innerHTML=result;
}


function getSubProd(selprodid ){   
		var url = "getSubProductListByProductDebitCustomerRegister.do?prodid="+selprodid+"&status=1";   
		//alert(url);
		result = AjaxReturnValue(url);   
		//alert(result);
		document.getElementById("subproduct").innerHTML = result;  
		
		//var hidsubproduct = document.getElementById("hidsubproduct").value; 
		//document.getElementById("subproduct").value=hidsubproduct;
		//getLimitBySubProduct(hidsubproduct);
		//getFeeBySubProduct(hidsubproduct);
		   
} 

function getAcctTypeList(){ 
	var url = "getAccountTypeListCbsAccount.do?";   
	   
	result = AjaxReturnValue(url);
	//document.getElementById("accttype0").innerHTML=result;
	
	
	return result;  
}

function getAcctSubTypeList(val){ 
	var accounttypeid =  document.getElementById("accounttypevalue_"+val).value;
	var url = "getAccutSubTypeListCbsAccount.do?accounttypeid="+accounttypeid;   
	result = AjaxReturnValue(url);
	//alert("getAcctsubTypeList"+result);
	document.getElementById("accountsubtypevalue_"+val).innerHTML=result;
	return result;  
}

function getCurrencyList(){    
	var url = "getCurrencyListCbsAccount.do?";   
	result = AjaxReturnValue(url);
	//document.getElementById("accountsubtypevalue_"+val).innerHTML=result;
	return result;  
}

function submit_validation(val)
{
	//alert('welcome'+val);
	var accounttypevalue = document.getElementsByName("accounttypevalue");
	var accountsubtypevalue = document.getElementsByName("accountsubtypevalue");
	var accountccyvalue = document.getElementsByName("accountccyvalue");
	var accountnovalue = document.getElementsByName("accountnovalue");
	//alert(accountnovalue.length);
	//alert(accounttypevalue.length);
	//alert('asdf:::'+document.getElementById("accountnovalue_"+parseInt(val)).value);
	
for(var i=parseInt(val);i<=accounttypevalue.length+parseInt(val);i++) {
	//alert('ivalue'+i);
	if(document.getElementById("accounttypevalue_"+i).value=="" || document.getElementById("accounttypevalue_"+i).value=="-1")
	{
	errMessage(document.getElementById("accounttypevalue_"+i),"Select Account Type ") ; 
	return false; 
	}
	//alert("test1");
	
	if(document.getElementById("accountsubtypevalue_"+i).value=="" || document.getElementById("accountsubtypevalue_"+i).value=="-1")
	{
	errMessage(document.getElementById("accountsubtypevalue_"+i),"Select Account Sub Type ") ; 
	return false; 
	}
	//alert("Test2");
	
	if(document.getElementById("accountccyvalue_"+i).value=="" || document.getElementById("accountccyvalue_"+i).value=="-1")
	{
	errMessage(document.getElementById("accountccyvalue_"+i),"Select Currency ") ; 
	return false; 
	}
		
	
	//alert("test3");
	
	if(document.getElementById("accountnovalue_"+i).value=="")
	{
	  
	  errMessage(document.getElementById("accountnovalue_"+i),"Enter Account No ") ; 
	  return false; 
	}
	
	
	var accountnolength = document.getElementById("accountnolength");	
	 
	if(document.getElementById("accountnovalue_"+i).value.length!=accountnolength.value)
	{
		 //alert("teststst");
	errMessage(document.getElementById("accountnovalue_"+i), "Account No Should be "+accountnolength.value+" Digit!");
	return false;
	}
	//alert(document.getElementById("accountnovalue_"+i).value);
	var url = "checkAccountNoExistCbsAccount.do?accountnumber="+document.getElementById("accountnovalue_"+i).value;
	//alert(url);
	var result = AjaxReturnValue(url);
	
	if(result=="EXIST")
		{
		
		errMessage(document.getElementById("accountnovalue_"+i), "Entered Account no Already Registered !");
		return false; 
		
		} 
		
	else if(result=='ACCTLINK'){
    	
		errMessage(document.getElementById("accountnovalue_"+i), "Entered Account no Registered Waiting for Authorization.");
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
<%-- <%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ");  
%> --%>

     <input type="hidden" name="Instname" value="${Instname}" />
     <input type="hidden" name="BRANCHCODE" value="${BRANCHCODE}" />
     <input type="hidden" name="USERTYPE" value="${USERTYPE}" />
     <input type="hidden" name="BRANCH_DESC" value="${BRANCH_DESC}" />
     <input type="hidden" name="DATEFILTER_REQ" value="${DATEFILTER_REQ}" />


<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addAccountNumberCbsAccount.do"  name="orderform" autocomplete = "off"  namespace="/">
	<%-- <%String act = request.getParameter("act")==null?"":request.getParameter("act"); %> --%>
	
	 <%-- <c:out value="${act}"></c:out> --%>
	<input type="hidden" value="${fn:escapeXml(act)}">
	<table border="0" cellpadding="0" cellspacing="4" width="50%" class="formtable">
	
				<tr id="cardnumdiv" >
					<td>Enter Card No. :</td>
					<td><input type="text" name="cardnum" id="cardnum" maxlength="19" onkeypress='return isNumberKey(event)'/></td>
				</tr>
	    
	</table>	
	
	<table width='20%'>
		<tr align="center">
		  <input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" onclick="return findcard_validation();"/></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
	</table>
	</s:form>
	
	<s:if test="!cbsbean.accounttypedetails.isEmpty()" >	
	<s:form action="generateAddonaccountActionCbsAccount.do"  name="orderform" autocomplete = "off"  namespace="/">
	<s:hidden value="2" name="" id="allowedaccount" />
	<s:hidden name="cardno" id="allowedaccount" value="%{cbsbean.cardno}"/><div id='fw_container'>	
	

	
	<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable" id="AccountInformation">
	<tr>
	<td>CUSTOMER NAME :</td>
	<td style="text-aligh:center">
		${cbsbean.custname}
	</td>
	</tr>
	
	
		 	<tr>
		 	<th>Account Type</th>  
		 	<th>AcctSub Type</th>
		 	<th>Currency</th>
		 	<th>Account Number</th>
		 	<th>Account Priority</th>
		 	<th>
		 	<%-- <s:if test="%{cbsbean.accounttypedetails.size()<6}">
		 	<input onclick="addRow('');" type="button" value="Add Account" /> 
		 	</s:if> --%>
		 	</th>  
		 	</tr>
		 	<%int i=0; i++;%>
		 	<s:iterator value="cbsbean.accounttypedetails">   
		 	
		 	<% %>
		 	<tr>    
		 		<td style="text-aligh:center">${ACCOUNTTYPEDESC}</td>
				<td style="text-aligh:center">${PRODUCTCODEDESC}</td>
				<td style="text-aligh:center">${CURRCODEDESC}</td>
				<td style="text-aligh:center">${ACCOUNTNO}</td>    
				<td style="text-aligh:center">${ACCOUNTFLAG}</td>
				<th>
		 		
		 		</th> 
 			</tr>
 			</s:iterator>
 			
 			<tr>
		 		<s:if test="%{cbsbean.accounttypedetails.size()<2}">
		 		
		 		<td>
		 		<!-- accttypelist -->
		 		<select id="accounttypevalue_<%=i%>" name="accounttypevalue" onchange="getAcctSubTypeList('<%=i%>')">
					<s:if test ="%{cbsbean.accttypelist.isEmpty()}">
					<option value="">No Account Type Found</option>
					</s:if>
					<s:else>
								<option value="" >-Select AccountType-</option>   
								<s:iterator value="cbsbean.accttypelist">
								<option value="<s:property value="ACCTTYPEID"/>" > 
								<s:property value="ACCTTYPEDESC"/></option>
								</s:iterator>
					</s:else>
					</select> 
					
		 		</td>
		 		</s:if>
		 		<td>
		 		<!-- acctsubtypelist subaccounttypelist -->
		 		<select id="accountsubtypevalue_<%=i%>" name="accountsubtypevalue" >
		 		<option>-Select AccountSubType-</option>
		 		</select> 
		 		</td>
		 		
		 		<td>
		 		<select id="accountccyvalue_<%=i%>" name="accountccyvalue" onchange="">
					<s:if test ="%{cbsbean.currencylist.isEmpty()}">
					<option value="">No Currency Found</option>
					</s:if>
					<s:else>
								<option value="" >Select Currency</option>   
								<s:iterator value="cbsbean.currencylist">  
								<option value="<s:property value="NUMERIC_CODE"/>">
								<s:property value="CURRENCY_DESC"/></option>
								</s:iterator>
					</s:else>
					</select> 
		 		
		 		</td>
		 		<td> 
		 		
		 		<%-- <s:hidden name = "accountnolength" id="accountnolength" value="%{cbscustregbean.accountnolength}"/> --%> 
		 		<s:hidden id="accountnolength" maxlength="%{accountnolength}" value="%{cbsbean.accountnolength}"/>
		 		<s:textfield  id="accountnovalue_1" name="accountnovalue" maxlength="%{cbsbean.accountnolength}" onkeypress="return numerals(event);"/>
		 		 </td>
		 		 <td >
		 		 <s:textfield   id="primarysec_1" name="primarysec" value="Secondary"  readonly = "true"  />
		 		 </td>
		 		 <td>
		 		
		 		 </td>
		 		
		 		
		 		</tr>
 			</table>
 			
 			<table width='20%'>
		<tr align="center">
		<s:if test="%{cbsbean.accounttypedetails.size()<2}">
		  <input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="saveAccount" id="saveAccount" value="Submit" onclick="return submit_validation('<%=i%>');"/></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
			</s:if>
		</tr>
	</table>
	
	
</div>
</s:form>
</s:if>
