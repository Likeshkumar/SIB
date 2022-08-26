<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">

function aks_confermation()
{
var t=confirm("Do you want to reset");
return t;
}
function validate_form ( )
{
    valid = true;
    if ( document.addInstitution.instid.value == "" )
    {
    	errMessage(instid,"Please enter Institution ID");
		return false;
        
    }
    else if ( document.addInstitution.instname.value == "" )
    {	
    	errMessage(instname,"Please enter Institution name");
	return false;
   		 
	}
    else if ( document.addInstitution.deploymenttype.value == "-1" )
    {	
    	errMessage(deploymenttype,"Please select Deployment type");
	return false;
   	}
    else if ( document.addInstitution.bincount.value == "" )
    {	
    	errMessage(bincount,"Please enter bincount");
	return false;
   	}
   
    else if ( document.addInstitution.branchattched.value == "-1" )
    {	
    	errMessage(branchattched,"Please select branch attched");
	return false;
   	}
    
    else if ( document.addInstitution.preference.value == "" )
    {	
    	errMessage(preference,"Please enter order preference");
	return false;
   	}
  
		else if ( document.addInstitution.basecurrency.value == "" )
	    {	
			errMessage(basecurrency,"Please enter base currency");
		return false;
	   	}
	
		else if ( document.addInstitution.countrycode.value == "" )
	    {	
			errMessage(countrycode,"Please enter country code");
		return false;
	   	}
	    
    else if( document.addInstitution.login_retry_cnt.value == "" )
    {	
    	errMessage(login_retry_cnt,"Please enter login retry count");
	return false;
   	}
    
    else if ( document.addInstitution.pin_retry_cnt.value == "" )
    {	
    	errMessage(pin_retry_cnt,"Please enter pin retry count");
	return false;
   	}
    return valid;
}

function set_baselen()
{
	var len=document.getElementById("chnlen").value;
	if(len=="")
		{
		document.addInstitution.baselen.value=0;
		}
	if(len!="")
	{
			var no=10,i=0;
			len=len-11;
			no=no--;
	document.addInstitution.baselen.value=(len);
	
		for (i=0;i<(len-1);i=i+1)
			{
				no=no*10;
			}
			no-=1;
	alert ("YOU CAN GENERATE '"+(no)+"' CARDS ");
	}
}

function br_attach()
{
	
	var att=document.getElementById("branchattched").value;
	if(att=='N')
	{
		
		
		document.addInstitution.brcodelen.value="3";
		document.getElementById("brcodlenghttext").style.visibility = 'hidden';
	
	}
	if(att=='Y')
		{
		
		document.addInstitution.brcodelen.value=4;
		document.getElementById("brcodlenghttext").style.visibility = "visible";
	
		}
}


var httpObject = null;
//Get the HTTP Object
function getHTTPObjectForBrowser(){
        if (window.ActiveXObject)
            return new ActiveXObject("Microsoft.XMLHTTP");
        else if (window.XMLHttpRequest) return new XMLHttpRequest();
        else {
            alert("Browser does not support AJAX...........");
        return null;
}
}
//Change the value of the outputText field
function setAjaxOutput(){
    if(httpObject.readyState == 4){
    	
    	//alert("Reponse is "+httpObject.responseText);
        document.getElementById('ajax').innerHTML = httpObject.responseText;
        
    }
}
//Implement business logic
    function doAjaxCall(){
		
   		 var inst_id=document.getElementById('instid').value;
   		
        httpObject = getHTTPObjectForBrowser();
        if (httpObject != null) {
        	// alert(document.getElementById('product').value);
        	//alert(inst_id);       	 
            httpObject.open("GET", "ajax_InstLicence_HandlerAddInstitutionAction.do?inst_id="+inst_id, true);
            
            httpObject.send(null);
       
        httpObject.onreadystatechange = setAjaxOutput;
      
    }
}


function display_Sec_Cur()
{

		var default_curr=document.getElementById('currencytype').value;
		//alert("default_curr"+default_curr);
		httpObject = getHTTPObjectForBrowser();
	    if (httpObject != null) {
	    	//alert("hai");
	    	// alert(document.getElementById('product').value);
	    	//alert("============ in side if "+default_curr);       	 
	        httpObject.open("GET", "ajax_default_currencyAddInstitutionAction.do?default_curr="+default_curr, true);
	        //alert(default_curr);
	        httpObject.send(null);
	   		httpObject.onreadystatechange = setsubCcy;
	        }
}
function setsubCcy()
{
	 if(httpObject.readyState == 4)
	 {

	    //alert("Reponse is "+httpObject.responseText);
	        document.getElementById('sec_ccy').innerHTML = httpObject.responseText;
	 }
}
function goBack()
{
	window.history.back();
}
function validation_authinst(){
 	var auth = document.getElementById("auth0").value;
 	var instid = document.getElementById("instid").value;
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		 var url = "authdeauthinstlistAddInstitutionAction.do?instid="+instid+"&reason="+reason+"&auth="+auth;
		 window.location = url; 
	 }  
	 return false;
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="authdeauthinstlistAddInstitutionAction.do" method="post" autocomplete="off">
<div align="center">
	 
	<s:iterator  value="institutiondetail">
		<fieldset style="width:850px;">
			<table id="errmsg"></table>
		    <legend><b>GENERAL  CONFIGURATION</b></legend>
		    <div align="center">
			<table border="0" cellpadding="0" cellspacing="0" width="100%" >
				
					
				<tr>
					<td>INSTITUTION ID </td><td>: <s:textfield name="instid" id="instid" value="%{INST_ID}" onchange="doAjaxCall();" readonly="true" maxlength="15"/></td>
					<td>INSTITUTION NAME </td> <td>: <s:textfield name="instname" id="instname" value="%{INST_NAME}" maxlength="15" readonly="true" /></td>
				</tr>
				
				<tr>
					<td>DEPLOYMENT TYPE </td><td>: 
						<s:textfield id="deploymenttype" name="deploymenttype" value="%{DEPLOYMENT_TYPE}" readonly="true"/>
					</td>
					<%-- <td>TOTAL NO OF BIN </td><td>: <s:textfield name="bincount" id="bincount" value="%{BIN_COUNT}" maxlength="3" onkeypress='return numerals(event)' readonly="true" /></td> --%>
					<td>ORDER REF LENGTH </td><td>: <s:textfield id="orderref"  name="orderref" value="%{ORD_REF_LEN}" maxlength="2" onkeypress='return numerals(event)' readonly="true"></s:textfield> </td>
				</tr>
				<tr>
						<td>BRANCH ATTACHED</td><td>: <s:textfield id="branchattched"  name="branchattched" value="%{BRANCHATTCHED}" maxlength="1" onkeypress='return numerals(event)' readonly="true"></s:textfield> </td>
						<td style="width:233px">BRANCH CODE LENGTH</td>
						<td>: <s:textfield id="brcodelen" readonly="true" name="brcodelen" value="%{BRCODELEN} Digits"></s:textfield></td>
								
				</tr>
				
				<tr>
						<td>COUNTRY CODE </td><td>: <s:textfield id="countrycode"  name="countrycode" value="%{COUNTRY_CODE}" maxlength="5" readonly="true" ></s:textfield> </td>
						<td>ACC NUM LENGTH</td><td>: <s:textfield id="accnumleng"  name="accnumleng" value="%{ACCT_LEN}" maxlength="2" readonly="true" onkeypress='return numerals(event)' ></s:textfield> </td>		
				</tr>
				<tr>
						<td>CUS ID LENGTH </td><td>: <s:textfield id="cid"  name="cid" value="%{CIN_LEN}" maxlength="2" onkeypress='return numerals(event)' readonly="true"></s:textfield> </td>
						<td>CUST ID BASED ON </td>
				<td>: <s:textfield id="cidbasedon"  name="cidbasedon" value="%{CUSTID_BASEDON}" maxlength="2" onkeypress='return numerals(event)' readonly="true" /> </td>		
				</tr>
				<tr>
						<td>EMAIL ALERT REQUIRED </td><td>: <s:textfield id="mailalertreq"  name="mailalertreq"  value="%{MAIL_ALERT_REQ}" readonly="true"></s:textfield></td>
						<td>SMS ALERT REQUIRED </td><td>: <s:textfield id="smsalertreq"  name="smsalertreq"  value="%{SMS_ALERT_REQ}" readonly="true"></s:textfield></td>
				</tr>
				
				<tr>
				<td>ACCOUNTTYPE LENGTH </td>
				<td>: <s:textfield id="accttypelength"  name="accttypelength" maxlength="1"  value="%{ACCOUNT_TYPE_LENGTH}"  ></s:textfield></td>
				<td>ACCOUNT SUBTYPE LENGTH</td>
				<td>: <s:textfield id="acctsubtypelength"  name="acctsubtypelength" maxlength="1"  value="%{ACCTSUBTYPE_LENGTH}"  ></s:textfield></td>
		</tr>
			</table>
			
			</div><br>
		</fieldset><br>		
		<fieldset style="width:850px;">
		    <legend><b>OTHER  CONFIGURATION</b></legend>	
				<table border="0" cellpadding="0" cellspacing="0" width="100%" >
					<tr>
						<td>LOGIN RETRY COUNT </td><td>: <s:textfield readonly="true"  id="login_retry_cnt"  name="login_retry_cnt"  value="%{LOGIN_RETRY_CNT}" maxlength="1" maxLength="1" ></s:textfield> </td>		
					</tr>
					<tr>
						<td>PIN RETRY COUNT </td><td>: <s:textfield id="pin_retry_cnt" readonly="true"  name="pin_retry_cnt"   value="%{PIN_RETRY_CNT}" maxlength="1"  ></s:textfield> </td>		
					</tr>
					<tr>
					<td>Max Add On CARDs </td><td>
					: <s:textfield id="maxaddoncards"  name="maxaddoncards" readonly="true" value="%{MAXALWD_ADDACC}" ></s:textfield> </td>
			</tr>
			<tr>
					<td>Max Add On Accounts </td><td>
					: <s:textfield id="maxaddonaccounts" readonly="true"  name="maxaddonaccounts"   value="%{MAXALWD_ADDCARD}"   ></s:textfield> </td>
					
			</tr>	
			<tr>
					<td>Renewal Periods(In Months) </td><td>
					: <s:textfield id="renewalperiods" readonly="true"  name="renewalperiods"   value="%{RENEWAL_PERIODS}"   ></s:textfield> </td>
					
			</tr>
				</table>
				
				<br>
		</fieldset><br>		
		
		<fieldset style="width:850px;">
    <legend><b>PA DSS </b></legend>	
		<table border="0" cellpadding="0" cellspacing="0" width="70%" >
			<tr>
					<td>PA DSS ENABLE</td><td>
					: <s:textfield id="padssenable" readonly="true"  name="padssenable"   value="%{PADSS_ENABLE}"   ></s:textfield> </td>
					
					
					<s:if test='PADSS_ENABLEVALUE=="Y"'>
					<td>PA DSS KEY </td><td>
					: <s:textfield id="padsskey" readonly="true"  name="padsskey"   value="%{PADSS_KEY}"   ></s:textfield> </td>
					</s:if>
			</tr>
			
			</table>
		
		<br>
</fieldset>

		<fieldset style="width:850px;">
			<legend><b>CURRENCY</b></legend>	
				<table border="0" cellpadding="0" cellspacing="0" width="70%" >
						<tr>
							<td>BASE CURRENCY:</td><td><s:textfield id="currencytype" name="defaultcurrency" value="%{BASECCY}" readonly="true"/></td>
					   	</tr>
					   	
				</table>
				
		</fieldset>
	</s:iterator>
</div>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" >
			<tr align="center">
				<td>
					<input type="submit" name="auth" id="auth1" value="Authorize"/>
					<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authinst()"/>						
				</td>
			</tr>
		</table>

</s:form>