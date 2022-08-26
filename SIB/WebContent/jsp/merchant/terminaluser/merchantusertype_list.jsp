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
				 
					<tr><td colspan="7"> Merchant Name : <s:property value="mregbean.merchname"/> 
						<s:hidden type="hidden" name="merchantid" id="merchantid" value="%{mregbean.merchid}" /> 
					</td></tr>
					<tr>
						<th width="10%"> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Store Name </th>
						<th>Terminal Name</th>
						<th> User Name </th> 
						<th> Print password </th>  
						<th> Delete </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="mregbean.terminaluserlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name="recordid"  id="orderrefnum<%=rowcount%>"  value="${RECORD_ID}"/>  </td>
						<td> ${STOREDESC} <s:hidden type="hidden" name="STOREID" value="%{STOREID}" /> </td>
						<td> ${TERMINALDESC} <s:hidden type="hidden" name="TERMINALID" value="%{TERMINALID}" />  </td>
						<td> ${USERNAME} <s:hidden type="hidden" name="eupassword" value="%{EUSERPASSWORD}" />
					</td> 
						
						<td>  <img src="images/print.jpg" onclick="return printPassword();" alt="Print Password" /> </td> 
						<td> <img src="images/delete.png" onclick="return deleteTerminalUser(${RECORD_ID});" alt="Delete User" />  </td> 
				 <td>	
						<div id="printable${RECORD_ID}" style="text-align:center; color: blue; border: 1px solid gray;display:none">
						<div style="padding:5px;">
						<div class="divcont"  style="text-align:left; color: blue;" > Merchant id  : <s:property value="mregbean.merchid"/>  </div>  
						<div class="divcont"  style="text-align:left; color: blue;" > Merchant Name  : <s:property value="mregbean.merchname"/>  </div> 
						<div class="divcont"  style="text-align:left; color: blue;" >  Store Name   : <s:property value="%{STOREDESC}"/> </div>
						<div class="divcont"  style="text-align:left; color: blue;" > Terminal id    : <s:property value="%{TERMINALDESC}"/>  </div>
						<div class="divcont"  style="text-align:left; color: blue;" >  User Name  :  <s:property value="%{USERNAME}"/> </div>
						<div class="divcont"  style="text-align:left; color: blue;" >   passowrd    : <s:property value="%{PASSWORD}"/></div>
					</div>
				</td>  
			</div>
			
					</tr> 
			 	</s:iterator>
			 		<tr>
						<td colspan="8">
							<s:submit value="Print" name="submit" id="submit" onclick="return printPassword();"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							 
				 
						</td>
					</tr>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 