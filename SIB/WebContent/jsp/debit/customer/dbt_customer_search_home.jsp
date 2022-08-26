<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
table.viewtable td{
	padding-top:20px;
}
</style>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script> 
 

<script>
	function viewCustomerData(){
		var customers = document.getElementsByName('customerid'); 
		var customerid;
		for(var i = 0; i < customers.length; i++){
		    if(customers[i].checked){
		    	customerid = customers[i].value;
		    }
		} 
		var url = "viewCustomerDataForViewCreditCustRegisteration.do?doact=VIEW&customerid="+customerid;
		alert( url );
		window.location = url;
		
	}
	
	function authCustomerData(){
		var customers = document.getElementsByName('customerid'); 
		var customerid;
		for(var i = 0; i < customers.length; i++){
		    if(customers[i].checked){
		    	customerid = customers[i].value;
		    }
		} 
		var url = "viewCustomerDataForViewCreditCustRegisteration.do?doact=AUTH&customerid="+customerid;
		//alert( url );
		window.location = url;
		
	}
	
	function validteCheckButton(){		
		var customerid = document.getElementsByName("applicationid");
		var valchecked = false; 
		for(var i=0; i< customerid.length; i++ ){
			var cust =  document.getElementById("customerid"+i); 
			if( cust.checked ){
				valchecked = true;
				break;
			}
		}
		if( !valchecked ){
			 errMessage(customerid0,"Select Any Record !");return false;
		}
	}
	
	function showProcessing(){
		parent.showprocessing();
	}
	
	function searchCustomer(){
		
		var applicationidcheck = document.getElementById("applicationidcheck");
		var dobcheck = document.getElementById("dobcheck");
		var customernamecheck = document.getElementById("namecheck");
		
		var applicationid = document.getElementById("applicationid");
		
		//alert("Searching...."+applicationid.value + applicationidcheck.checked );
		
		
		var dob = document.getElementById("dob");
		var customername = document.getElementById("customername");
		
		
		
		var doact = document.getElementById("doact"); 
		var qrystr = "customerSearchDebitCustomerRegister.do?doact="+doact.value;
		if( applicationidcheck.checked ){
			if( applicationid.value == "-1"){ errMessage(applicationid,"Select Application Id !");return false;}
			qrystr += "&applicationid="+applicationid.value;
		}else if( dobcheck.checked ){
			if( dob.value == ""){ errMessage(dob,"Select Date of Birth !");return false;}
			qrystr += "&dob="+dob.value;
		}else if( customernamecheck.checked ){
			if( customername.value == ""){ errMessage(customername,"Enter Customer Name !");return false;}
			qrystr += "&customername="+customername.value;
		}else{
			 errMessage(applicationid,"Select Any Catogory !");
			 return false;
		}
		
	 
		 
		
		var result = AjaxReturnValue(qrystr);
		var res = result.split("~");
		if( res[1] == "0"){
			var submitbtntable = document.getElementById("submitbtntable");
			submitbtntable.style.display="table";
		}
		document.getElementById("resultrec").innerHTML=res[0];
		return false;
	}
	
	
	function enableField( key ){
		var applicationid = document.getElementById("applicationid");
		var dob = document.getElementById("dob");
		var customername = document.getElementById("customername");
		
		if( key == "$APPID"){ 
			applicationid.disabled = false;
			applicationid.focus();
			dob.disabled=true;
			customername.disabled=true;
		}else if( key == "$DOB"){
			dob.disabled=false;
			dob.focus();
			applicationid.disabled = true;			
			customername.disabled=true;
		}else if( key == "$NAME"){
			customername.disabled=false;
			customername.focus();
			applicationid.disabled = true;
			dob.disabled=true;
			
		}
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include> 
 
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable">	 
<tr>
	<s:hidden name="doact" id="doact" value="ALTER" />
	<td>   <input type="radio" name="selecttype" id="applicationidcheck" value="$APPID"  onclick="enableField( this.value )" /> Application Number </td>
	<td>   <s:select list="%{dbtcustregbean.applicationlist}" id="applicationid"  name="applicationid" disabled="true"    headerKey="-1" headerValue="-SELECT-"  listKey="ORDER_REF_NO"  listValue="APPLICANT" value="%{custregbean.applicationid}"/> </td>
	
	<td>  <input type="radio" name="selecttype" id="dobcheck" value="$DOB"    onclick="enableField( this.value )" /> Date Of Birth  </td>
	<td> 
		 	<s:textfield name="dob" id="dob"  readonly="true"  disabled="true"  style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
	</td>
	
</tr>

 <tr>
	<td> <input type="radio" name="selecttype" id="namecheck" value="$NAME" onclick="enableField( this.value )" /> Name </td>
	<td> <s:textfield name="customername" id="customername" maxlength="25" disabled="true" /> </td>
	  
	<td colspan="2">  &nbsp; </td>
	
</tr>
 
</table> 

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<input type="button" value="Search" name="order" id="order" onclick="return searchCustomer()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>


<form action='getcustomerDataDebitCustomerRegister.do' name='form1' id='form1'> 
<table border="0" cellpadding="0" cellspacing="4" width="100%" class="formtable viewtable" method="post" >

<tr>
	<td style='text-align:center;font-weight:bold' id='resultrec'></td>
</tr>

<table border="0" cellpadding="0" cellspacing="4" width="20%" id="submitbtntable" style='display:none'>
		<tr>
	 
		
		<s:if test="%{dbtcustregbean.doact=='EDIT'}">
			<td> 
				<s:submit value="Submit" name="order" id="order" onclick="return validteCheckButton()"/>
			
			</td>
		</s:if>
		
		<s:elseif  test="%{dbtcustregbean.doact=='AUTH'}">
			<td> 
				<input type="button"  value="Authorize" name="order" id="order" onclick="return authCustomerData()"/>
			
			</td>
		</s:elseif>
		
		<s:elseif  test="%{dbtcustregbean.doact=='ALTER'}">
			<td> 
				<s:submit value="Submit" name="order" id="order" onclick="return validteCheckButton()"/>
			
			</td>
		</s:elseif>
		
		
			
		<td>
				<s:submit value="Submit" name="order" id="order" onclick="return validteCheckButton()"/>
				<input type="button" value="View" name="view" id="view" onclick="return viewCustomerData()"/>
		</td>  
		 
		</tr>
</table>
</form>

</table>
 
 
 