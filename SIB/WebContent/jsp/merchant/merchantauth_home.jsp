<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<script>
function printPassword(){ 
	var recordidlist = document.getElementsByName("recordid");  
	var printContents = "";
	var checkedcnt = 0 ;
	for( var i=1; i<=recordidlist.length; i++){
		var recordidfld = "orderrefnum"+i; 
		if( document.getElementById(recordidfld).checked  ){
			var recordid = document.getElementById(recordidfld).value;
			var printableid = "printable"+recordid;
				printContents += document.getElementById(printableid).innerHTML;
			 	checkedcnt++;
		} 
	} 
	if( checkedcnt == 0 ){
		errMessage("orderrefnum1", "Select the check box and then print...");
		return false;
	}
	
	if( !confirm("Do you want to print the password") ){
		return false;
	}
	var originalContents = document.body.innerHTML; 
    document.body.innerHTML = printContents; 
    window.print(); 
    document.body.innerHTML = originalContents;  
	 
}

function deleteTerminalUser( recordid ){
	var merchantid = document.getElementById("merchantid").value;
	var url = "deleteTerminalUserMerchantRegister.do?merchantid="+merchantid+"&recordid="+recordid; 
	if( !confirm("Do you want to Delete the user") ){
		return false;
	}
	window.location=url;		
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<div align="center">
<s:form action="#"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
 
   
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				 
					 
			<s:if test="mregbean.merchantlist.size()!=0"> 
					
					</td></tr>
					<tr>
						<th width="10%"> Sl no </th> 
						<th> Merchant Id</th>
						<th>Merchant Name</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th> Mobile Number </th> 
						<th> Settlement Acct Number </th>  
						<th> Authorize </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="mregbean.merchantlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td> 
					 
					<td><a href='viewMechantInfoMerchantRegister.do?action=authorize&merchid=${MERCH_ID}&methodtype=MERCHANT' > ${MERCH_ID} </a> </td>
					<td>${MERCH_NAME}</td>
					<td>${FIRST_NAME}</td>
					<td>${FIRST_NAME}</td>
					<td> ${PRIM_MOBILE_NO} </td> 
					<td> ${SETTLEMENT_ACCTNO} </td>  
					<td> <s:submit value="Authorize" name="submit" id="submit" /> </td>
					 
					</tr> 
			 	</s:iterator>
		</s:if>
		<s:else>  <tr><td colspan="6"> NO RECORDS AVAILABLE FOR AUTHORIZATION </td></tr> </s:else> 	 
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 