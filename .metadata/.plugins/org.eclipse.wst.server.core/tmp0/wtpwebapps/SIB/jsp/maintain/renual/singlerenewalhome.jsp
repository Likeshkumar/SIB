<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>

function validation(){		
	var cardtype = document.getElementById("cardtype");
	var branchcode = document.getElementById("branchcode");
	var cardgentype = document.getElementById("cardgentype");
	/* var searchtype = document.getElementById("searchtype");
	alert(searchtype.checked.length);
	var cardno = document.getElementById("cardno");
	var acctno = document.getElementById("acctno");
	var custid = document.getElementById("custid"); */
	if(cardtype.value=='-1' || cardtype.value==''){
		 errMessage(cardtype,"Select Product !");return false;
	}
	if(branchcode.value=='-1' || branchcode.value==''){
		 errMessage(branchcode,"Select The Branch!");return false;
	}
	if(cardgentype.value=='-1' || cardgentype.value==''){
		 errMessage(cardgentype,"Select Generation Type !");return false;
	}
	var collectbranch = document.getElementById("collectbranch");
	if( collectbranch ){ if( collectbranch.value == "") { errMessage(collectbranch, "Select Collection Branch !");return false; } }
	
	/* if(cardno.value==''){
		 errMessage(cardno,"Please Enter Card No !");return false;
	}
	if(acctno.value==''){
		 errMessage(acctno,"Please Enter Account No !");return false;
	}
	if(custid.value==''){
		 errMessage(custid,"Please Enter Customer Id No !");return false;
	} */
}
function Searchtype(val){
	//alert(val);
	//var searchtype = document.getElementById("searchtype").value;
	if(val == '1'){
		document.getElementById("showcardno").style.display = 'block';
		document.getElementById("showacctno").style.display = 'none';
		document.getElementById("showcustid").style.display = 'none';
	}else if(val == '2'){
		document.getElementById("showacctno").style.display = 'block';
		document.getElementById("showcardno").style.display = 'none';
		document.getElementById("showcustid").style.display = 'none';
	}else if(val == '3'){
		document.getElementById("showcustid").style.display = 'block';
		document.getElementById("showcardno").style.display = 'none';
		document.getElementById("showacctno").style.display = 'none';
	}
	else if(val == '4'){
		document.getElementById("showphoneno").style.display = 'block';
		document.getElementById("showcardno").style.display = 'none';
		document.getElementById("showacctno").style.display = 'none';
	}
}
</script> 
</head>
 
<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="bulkrenualListBulkRenual.do"  name="perscardgen" autocomplete = "off" namespace="/">
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">
	<%-- <% 
	String usertype=(String)session.getAttribute("USERTYPE");   
		if(usertype.equals("INSTADMIN"))
		{
	%>
			<tr>
				<td class="fnt"> Select Branch</td>
				<td> : <s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1"/></td>
			</tr>
	<%	
		}
		else
		{
	%>
			<tr>
			<td class="fnt"> Branch</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
	<%		
		}
	%>
	<tr>
		<td class="fnt">
		Select Product
		</td>
		<td> :
 				<select name="cardtype" id="cardtype">
	 				<option value="-1">--Select Product--</option>
	 				<s:iterator  value="personalproductlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
    
    <tr>
		<td class="fnt">
		Generation Type
		</td>
		<td> :
 				<select name="cardgentype" id="cardgentype">
	 				<option value="-1">--Select Generation Type--</option>
	 				<option value="NEWCARD">New Card</option>
	 				<!-- <option value="EXISTCARD">Same Card</option> -->
	 				
 				</select>
		</td>
    </tr>
    
   	 <tr>
					<td>Collection Branch <span class="mand">*</span></td>
					<td>: <select name="collectbranch" id="collectbranch">
								<option value="">-SELECT-</option>
								<s:iterator value="branchlist">
									<option value="${BRANCH_CODE}">${BRANCH_NAME}</option>
								</s:iterator>
						</select>
					</td>
		</tr>
	
    --%>
    <tr> 
	<td class="fnt"> Choose Search Type </td>
		<td class="fnt"><input type="radio" name="searchtype" id="searchtype" value="1" onclick="return Searchtype(this.value)"/> &nbsp; Card No &nbsp;
		    <input type="radio" name="searchtype" id="searchtype" value="2" onclick="return Searchtype(this.value)"/> &nbsp; Account No &nbsp;
		    <input type="radio" name="searchtype" id="searchtype" value="3" onclick="return Searchtype(this.value)"> &nbsp; Customer Id
			    <!-- <input type="radio" name="searchtype" id="searchtype" value="4" onclick="return Searchtype(this.value)"> &nbsp; Phone Number </td> -->
	
	</tr>
	
	</table>
	<br>
	<table border="0" cellpadding="2" cellspacing="0" width="40%">
	    <tr id="showcardno" style="display: none;" class="fnt">
		<td class="fnt"> Card Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
			<td><input type="text" name="cardno" maxlength="19" id="cardno" onkeypress="return numerals(event);" /></td>
		</tr>
		<tr id="showacctno" style="display: none;">
		<td> Account Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><input type="text" name="acctno" maxlength="15" id="acctno" onkeypress="return numerals(event);" /></td>
		</tr>
		<tr id="showcustid" style="display: none;">
		<td> Customer Id &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
			<td><input type="text" name="custid" maxlength="16" id="custid" onkeypress="return numerals(event);" /></td>
			
		</tr>
		<!-- <tr id="showphoneno" style="display: none;">
		<td> Phone Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><input type="text" name="phonno" maxlength="15" id="phonno" onkeypress="return numerals(event);" /></td>
		</tr> -->
  	</table>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="20%">
	<tr>
		<td>
			<s:submit value="Submit" name="Next" onclick="return validation();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
<s:hidden name="flag" value="S"/>

</s:form>
