<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />
<style>
table.pretty td{ 
	border-left:1px solid #0099ff;
	border-top:1px solid #0099ff;
}
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<%!
String maintdesc = null;
%>
<%
maintdesc=(String)session.getAttribute("maintdesc");
	
%>
<script>
document.getElementById("title").innerHTML="Card Maintenance  "+"<% out.print(maintdesc);%>";

    
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
		
		var selectedtype = document.getElementById("selectedtype");
		var cardno = document.getElementById("cardno");
		//var maintdesc=(String)session.getAttribute("maintdesc");
		//alert(maintdesc);
		var maintdesc = document.getElementById("role");
		//alert(maintcode.value);
		
		
		
		//var accountno = document.getElementById("accountno");
		//var custno = document.getElementById("custno");  
		
		//var dob = document.getElementById("dob");
		//var custname = document.getElementById("custname");
		//var mobileno = document.getElementById("mobileno");
		
		 
		
		
		
		 //if((cardno.value == "") ){//&& (custno.value == "") && (dob.value == "") && (custname.value == "") && (mobileno.value == "") && (accountno.value=="")){
		 if(cardno.value == "") 
		{	 
			 errMessage( displayresult1, " Please Enter Valid Input");
			 return false;
		 }  
		 
		 document.getElementById("displayresult1").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";
		 
		 //alert(selectedtype.value);
	 
		 var url = "viewsearchListCardMaintainAction.do?cardno="+cardno.value+"&selectedtype="+selectedtype.value+"&maintdesc="+maintdesc.value;
		 //alert(url);     
		 result = AjaxReturnValue(url);
		// alert(result);
		 document.getElementById("displayresult1").innerHTML = "<BR><BR>"+result;   
		
		return false;
			
	}
	
	function viewshowMaintain( instid, hcardno,mcardno,cardnumber ){
		//alert(cardno);
		var url = "viewshowMaintainCardMaintainAction.do?instid="+instid+"&hcardno="+hcardno+"&mcardno="+mcardno+"&cardnumber="+cardnumber;
		var x = confirm( "Do you want to continue the maintenance activity for the card  " + mcardno );
		if( !x ){
			return false;
		}
		window.location=url;
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
	
	function enableField(selectedval)
	{
		textcard = document.getElementById("textcard");
		
		if (selectedval == "cardnoopt")
		{
			//alert(selectedval);
			textcard.innerHTML="card number";
			document.getElementById("cardno").value="";
			selectedtype.value="cardnum";
			
			
		}
		else if(selectedval =="acctnoopt"){
			
			//alert(selectedval);
			textcard.innerHTML="acct number";
			document.getElementById("cardno").value="";								
			selectedtype.value="acctnum";
			
		}
		else {
			//alert(selectedval);
			textcard.innerHTML="cust number";
			document.getElementById("cardno").value="";			
			selectedtype.value="custnum";
		}
						
	}
	
	
	
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="viewsearchListCardMaintainAction.do" name="orderform"
	onsubmit="return searchRecords()" autocomplete="off" namespace="/">



	
<s:if test="%{#session.USERTYPE =='BRANCHUSER'}">
			<td class="txt"> Branch :</td>
			<td>  <s:property value="%{#session.BRANCH_DESC}"/></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
				
	</s:if>

	
	<table border="0" cellpadding="0" cellspacing="4" width="40%" align="center">
	
		<tr>
		
			<td>
				<input type="radio" name="checkedtype" id="cardnoopt" checked  onclick="enableField( this.value )" value="cardnoopt"/> Card Number 
			</td>
			
			<td>
				<input type="radio" name="checkedtype" id="acctnoopt"  onclick="enableField( this.value )" value="acctnoopt"/> Account Number 
			</td>
			
			<td>
				<input type="radio" name="checkedtype" id="custnoopt"  onclick="enableField( this.value )" value="custnoopt" /> Customer Id 
			</td>
			
			</tr>
	
	</table>
	<input type="hidden" id="role" value=<%= maintdesc%> />
	<table border="0" cellpadding="0" cellspacing="0" width="100%"	align="center">
		
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="40%"
					align="center">
					<tr align="center">
						<td id="textcard">Card No</td>
						<td><input type='text' name='cardno' id='cardno' maxlength="16" onkeypress="return numerals(event);"/>
						<input type="hidden" id="selectedtype" name="selectedtype" value="cardnum"/>
						<br /><small class='dt'><!-- Last digits of card number --></small></td>
						<%-- <td>Account No</td>
						<td><input type='text' name='accountno' id='accountno' maxlength="16"/>
						<br /><small class='dt'> Last digits of card number </small></td>
						<td>Customer No</td>
						<td><input type='text' name='custno' id='custno'/></td>  --%>  
						
					</tr> 

					<!-- <tr>
					<td>DOB</td>
						<td><input type='text' name='dob' id='dob' maxlength="10"
							onkeyup="addSeperator(this.id, this.value)" />
						<br /><small class='dt'>&nbsp;(dd-mm-yyyy)</small></td>
						
						<td>Customer Name</td>
						<td><input type='text' name='custname' id='custname' /></td>
						
						<td>Mobile Number</td>
						<td><input type='text' name='mobileno' id='mobileno' />
						<br /><small class='dt'> Last digits of mobile number </small>
						</td>

					</tr> -->
 

					<tr>
						<table border="0" cellpadding="0" cellspacing="0" width="20%">
							<tr>
								<td><s:submit value="Submit" name="order" id="order" /></td>
								<td><input type="button" name="cancel" id="cancel"
									value="Cancel" class="cancelbtn"
									onclick="return confirmCancel()" /></td>
							</tr>
						</table>
					</tr>


				</table>
			</td>
		</tr>


		<tr>
			<td  >
				<div style="width:100%;" id="displayresult1">&nbsp;</div>
			</td>
		</tr>

	</table>

</s:form>

