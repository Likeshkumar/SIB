<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<style>
td.radiotd { 
	text-align:left
}

td.radiotd div {
	padding-top:5px;
}

</style>

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
 
<script>

	function addSeperator( dateid, dateval ){
		var newval = dateval.replace("-","");
		if( dateval.length == 2 ){
			if ( newval.length == 2 ){
			document.getElementById(dateid).value  = dateval+"-";
			}
		}
		 
		if( dateval.length == 5 ){
			 
				document.getElementById(dateid).value  = dateval+"-";
			 
		}
		 
		
	}
	
	 
	function searchRecords(){
		 
		var cardno = document.getElementById("cardno");
		//alert( cardno.value.length );
		if(cardno.value == ""){
			errMessage(cardno, "Enter card number");
			return false;
		}
		var displayresult1 = document.getElementById("displayresult1");
		if( cardno.value.length < 16 ){
			displayresult1.innerHTML = "<div style='text-align:center;color:red'>Invalid Card Number. Card Number should be 16 or 19 digit </div>";
			return false;
		}
		
		displayresult1.innerHTML = "<div style='text-align:center'>PROCESSING...</div>"; 
		 
		 var url = "searchAcitationCardsCardMaintainAction.do?cardno="+cardno.value+"&orderrefno=null&custno=null&dob=null&custname=null&orderrefno=null&mobileno=null";
		 
		 result = AjaxReturnValue(url);  
		 document.getElementById("displayresult1").innerHTML = result;
 		 if(! result.contains("NO RECORDS FOUND") ){	 
 			 document.getElementById("mobileblock").style.display="block";  
 		}
 		document.getElementById("originalcardno").value=cardno.value;
 		 
 
		 
		return false;
			
	}
	
	function showMaintain( instid, cardno ){
		var url = "showMaintainCardMaintainAction.do?instid="+instid+"&cardno="+cardno;
		var x = confirm( "Do you want to continue the maintenance activity for the card  " + cardno );
		if( !x ){
			return false;
		}
		window.location=url;
	}
	
	function displayMobileNo( value ){
		 document.getElementById("printmobileno").innerHTML = value;
	}
	
	function validateActivation(){
			var cardno = document.getElementById("originalcardno").value;
			var mobileno = document.getElementById("mobileno");
			var remobileno = document.getElementById("remobileno");
			//var yespinreq = document.getElementById("yespinreq");
			var nopinreq = document.getElementById("nopinreq");
			var cvvreq = document.getElementById("cvvreq");
			var msg = "";
			if( mobileno.value.length < 8 ){
				errMessage(mobileno, "Invalid Mobile Number");
				return false;
			}
			
			if( mobileno.value != remobileno.value ){
				errMessage(mobileno, "Mobile Number and Re-entered mobile number is not match");
				return false;
			}
			
			if( nopinreq.checked ){
				msg = "  Make Activate, Pin Not Required ....continue ?";
			}else if( cvvreq.checked ){
				msg = " Activate, Send Cvv info to mobile ....continue? ";
			}
			if( !confirm(msg) ){
				return false;
			} 
			
			return true;
	}
	 
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="listOfActivationCardsCardProcess.do" name="orderform" onsubmit="return validateActivation()"	autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="100%"	align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="100%"
					align="center">
					<!-- <tr>
						<td><input type='radio' id='cardbase' value="$CARDBASED" name='selecttype'/> Card No</td>
						<td><input type='text' name='cardno' id='cardno' maxlenght=19 onkeypress="return numerals(event);"/> 
						<td><input type='radio' id='entercourieridbase' value="$ENTCOURIERIDBASED" name='selecttype'/>Enter Courier Id</td>
						<td><input type='text' name='entcourierid' id='entcourierid' maxlenght=19 onkeypress="return numerals(event);"/> 
						
						<td><input type='radio' id='merchantbase' value="$MERCHANTIDBASED" name='selecttype'/>Enter Merchant/Agent Id</td>
						<td><input type='text' name='merchantid' id='merchantid' maxlenght=19 onkeypress="return numerals(event);"/>
					 
						
					</tr>    -->
		 
					<tr style="text-align:center">
						<td>
							<td><s:select  listKey="COURIERTRACKID" listValue="COURIERNAME" name='selectedcourierid' id='selectedcourierid' list="courierlist" headerKey="-1" headerValue="-SELECT-"/>
						<input type="submit"  value="Get Details" name="details" id="details"  /></td>  <!-- onclick="return searchRecords()"  -->
					</tr>
				</table>
			</td>
		</tr>  
		<tr>
			<td >
				<div style="width:100%;" id="displayresult1">&nbsp;</div>
			</td>
		</tr>
		
		<%-- <tr><td style="display:none" id="mobileblock">
				<table border="0" cellpadding="0" cellspacing="4" width="50%" >
					<input type="hidden" name="originalcardno" id="originalcardno" value=""  />
					<tr>
						<td>Mobile Number </td>
						<td> : <input type='password' maxlength='16' name='mobileno' id="mobileno" onpaste="return false;"/> </td> 
					</tr>  
					<tr> 
						<td>Re-Mobile Number </td>
						<td> : <input type='text' maxlength='16' name='remobileno' id="remobileno" onpaste="return false;" /> 
							<br/> <br/> <span id="printmobileno"></span> </td> 
					</tr> 
					<tr>
						 <td> &nbsp; </td>
						<td class="radiotd"> 
							<!-- <div> <input type="radio" name="pinoption" id="yespinreq" value="Y" checked="true" /> Send new pin to customer </div>   -->
						   <div>  <input type="radio" name="pinoption" id="nopinreq" value="NOPIN"  checked="true"   /> Make Activate, Pin Not Required </div>
						   <div>  <input type="radio" name="pinoption" id="cvvreq" value="CVVREQ"  /> Activate, Send Cvv info to mobile </div>
					    </td>
						
					</tr>  
					<tr> 
						<td colspan="2" style="text-align:center"> <s:submit name="submit" id="submit"/>  </td>
					</tr>
					
				</table>
		
		 </td></tr> --%>
		
	 
		
		 
		 

	</table>

</s:form>

