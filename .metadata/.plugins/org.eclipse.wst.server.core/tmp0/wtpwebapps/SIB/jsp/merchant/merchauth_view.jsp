<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />
<style>
	table.merchviewtable td {
		border:1px solid #efefef;
	}
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/merchant/script/merchscript.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>


<script>

function editTerminal(storeid, terminalid){
	var merchid = document.getElementById("merchid").value;
	var merchname = document.getElementById("merchname").value;
	
	var url = "addNewTerminalMerchantRegister.do?merchid="+merchid+"&merchentname="+merchname+"&storesid="+storeid+"&terminalid="+terminalid+"&action=edit";	 
	
	if ( confirm( "Confirm to Edit Terminal ?") ){
		window.location = url;
	}else{
		return false;
	} 
}
function editStore( storeid )
{
	//alert(storeid);
	var merchid = document.getElementById("merchid").value;
	var merchname = document.getElementById("merchname").value;
	//var store_id = document.getElementById("storeid").value;
	var url = "addNewStoreMerchantRegister.do?merchid="+merchid+"&merchentname="+merchname+"&storesid="+storeid+"&action=edit";	 
	
	if ( confirm( "Confirm to Edit store ?") ){
		window.location = url;
	}else{
		return false;
	} 
}
function addNewStore(){ 
	var merchid = document.getElementById("merchid").value;
	var merchname = document.getElementById("merchname").value;
	
	var url = "addNewStoreMerchantRegister.do?merchid="+merchid+"&merchentname="+merchname+"&action=save";	 
	
	if ( confirm( "Confirm to add new store ?") ){
		window.location = url;
	}else{
		return false;
	} 
}


function addNewTerminal(){ 
	var merchid = document.getElementById("merchid").value;
	var merchname = document.getElementById("merchname").value;
	
	var url = "addNewTerminalMerchantRegister.do?merchid="+merchid+"&merchentname="+merchname+"&action=save";	 
	
	if ( confirm( "Confirm to add new Terminal ?") ){
		window.location = url;
	}else{
		return false;
	} 
}


function getReason( storeid, storename ){
	var merchid = document.getElementById("merchid").value;
	var delreason = prompt( "Enter the reason for deleting the store [ "+ storename + ']' );
	if( delreason ){
		  var url1 = "deleteStoreMerchantRegister.do?merchid="+merchid+"&storeid="+storeid+"&delreason="+delreason;
		  //var url1="saveStoreMerchantRegister.do?";
		  //alert(url1);
		  window.location = url1;
	}else{
		return false; 
	}
} 


function deleteTerminal( storeid, terminalid ){ 
	 var merchid = document.getElementById("merchid").value;
	 if( confirm ( 'Do you want to delete ' )){
		 var url = "deleteTerminalInfoMerchantRegister.do?merchid="+merchid+"&storeid="+storeid+"&terminalid="+terminalid;	   
		// alert(url);
		  window.location.href = url;
	 }else{
		 return false;
	 }
}

function deAuthorize(){
	var reason = prompt("Reason For Reject");
	if( reason == null ){
		return false;
	}
	document.getElementById("remarks").value = reason;	
}

</script>
<jsp:include page="/displayresult.jsp"></jsp:include>  

<body>
	<s:form action="authMerchangInfoMerchantRegister" name="custinfoform"
		id="custinfoform" autocomplete="off" namespace="/">
		<s:hidden name="remarks" id="remarks"/>
		<table border="0" cellpadding="0" cellspacing="0" width="100%"
			style='border: 1px solid #efefef' align="center" class="merchviewtable">
			
			<tr>
				<td>MERCHANT ID</td>
				<td class="textcolor">: <s:property value="mregbean.merchid" /> <input type='hidden'
					id='merchid' name='merchid'
					value='<s:property value="mregbean.merchid"/>' />
				</td>
				
				<td>FIRST NAME</td>
				<td class="textcolor">: <s:property value="mregbean.firstname" /> <input
					type='hidden' id='firstname' name='firstname'
					value="<s:property value='mregbean.firstname'/>" />
				</td>
				<td>LAST NAME</td>
				<td class="textcolor">: <s:property value="mregbean.lastname" /> <input
					type='hidden' id='lastname' name='lastname'
					value="<s:property value='mregbean.lastname'/>" />
				</td>
			</tr>

			<tr>
				<td>MERCHANT NAME</td>
				<td class="textcolor">: <s:property value="mregbean.merchname" /> <input
					type='hidden' id='merchname' name='merchname'
					value="<s:property value='mregbean.merchname'/>" />
				</td>
				
				<td>Date of Birth</td>
				<td class="textcolor">: <s:property value="mregbean.dob" /> <input type='hidden'
					id='merchid' name='merchid'
					value='<s:property value="mregbean.merchid"/>' />
				</td>
				
				
				<td>ACCT TYPE</td>
				<td class="textcolor">: <s:property value="mregbean.accttype" />
				</td>
				
			
			</tr>


			<tr>
				<td>ACCT NO</td>
				<td class="textcolor">: <s:property value="mregbean.cbsacctno" />
				</td>
				
				<td>MERCHANT TYPE</td>
				<td class="textcolor">: <s:property value="mregbean.merchtype" />
				</td>
				
				<td>MCC</td>
				<td class="textcolor">: <s:property value="mregbean.mcc" /></td>

				
				
			</tr>





			<tr>
				
				<td>Primary <br /> Mobile No
				</td>
				<td class="textcolor">: <s:property value="mregbean.primaymobile" />
				</td>
				
				
				<td>Secondary <br /> Mobile No
				</td>
				<td class="textcolor">: <s:property value="mregbean.secmobile" />
				</td>
				<td>Primary <br /> Mail Id
				</td>
				<td class="textcolor">: <s:property value="mregbean.primaymail" />
				</td>

				
			</tr> 
		  
			<tr>
			
				<td>Secondary <br /> Mail Id
				</td>
				<td class="textcolor">: <s:property value="mregbean.secmail" />
				</td> 
				 
				<td>Currency  </td>
				<td class="textcolor">: <s:property value="mregbean.curcode" />
				</td>
				 <td>Location  </td>
					<td class="textcolor">: <s:property value="mregbean.primarylocation" />
				</td>
			</tr>
			
			<%-- <tr>
				
				<td>Fee 
				</td>
				<td class="textcolor">: <s:property value="mregbean.feecode" />
				</td>
				
				
				<td>Commission
				</td>
				<td class="textcolor">: <s:property value="mregbean.commissioncode" />
				</td>
				<td>Discount
				</td>
				<td class="textcolor">: <s:property value="mregbean.discountcode" />
				</td>

				
			</tr>  --%>
			
			
			<tr>
				<td colspan="6">
					<fieldset>
						<legend> Address :</legend>
						<table border="0" cellpadding="0" cellspacing="0" width="60%">

							<tr>
 
								<td class="textcolor">  <s:property value="mregbean.addr1" />  &nbsp;&nbsp;&nbsp;  <s:property
										value="mregbean.addr2" />  &nbsp;&nbsp;&nbsp; <s:property value="mregbean.addr3" />

								</td>
							</tr>
						</table>

					</fieldset>
				</td>
			</tr>
		</table>

		<table border="0" cellpadding="0" cellspacing="4" width="30%">
		<tr>
			<td> <s:submit name="authorize" id="authorize" value="Authorize"/> &nbsp;&nbsp;&nbsp; </td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn"/> </td>
			<td> <s:submit name="deauthorize" id="deauthorize" value="Reject" onclick="deAuthorize()"/> </td>
		</tr>
		</table>

		 
	</s:form> 
</body>