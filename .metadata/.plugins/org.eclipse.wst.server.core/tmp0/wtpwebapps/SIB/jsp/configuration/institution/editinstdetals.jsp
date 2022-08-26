<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript">

function aks_confermation()
{
var t=confirm("Do you want to reset");
return t;
}
function validate_form ( )
{
    valid = true;
    if ( document.addInstitution.instname.value == "" )
    {
    	errMessage(instname,"Please enter Institution name");
		return false;
        
    }
	else if ( document.addInstitution.countrycode.value == "" )
    {	
		errMessage(countrycode,"Please enter country code");
		return false;
   	}
	else if ( document.addInstitution.mailalertreq.value == "-1" )
    {	
		errMessage(mailalertreq,"Please Select Mail Required");
		return false;
   	} 
	else if ( document.addInstitution.smsalertreq.value == "-1" )
    {	
		errMessage(smsalertreq,"Please Select SMS Required");
		return false;
   	}  	    
    else if( document.addInstitution.inststatus.value == "-1" )
    {	
    	errMessage(inststatus,"Please enter status");
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
</script>
<jsp:include page="/displayresult.jsp"></jsp:include> 
<div align="center">
<s:form action="editInstValuesAddInstitutionAction" name="addInstitution" autocomplete="off">
<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
<input type="hidden" name="instid" id="instid" value="${instid}">
<s:iterator  value="institutiondetail">
<fieldset style="width:850px;">
	<table id="errmsg"></table>
    <legend><b>GENERAL  CONFIGURATION</b></legend>
    <div align="center">
	<table border="0" cellpadding="0" cellspacing="0" width="90%" >
	 

	<s:iterator value="institutiondetail">
		<tr align="center"><td colspan="4"><b><font color="red"><div id="ajax"></div></font></b></td></tr>
			
		<tr>
			<td>INSTITUTION NAME </td> <td>: <s:textfield name="instname" id="instname" value="%{INST_NAME}"  maxlength="30" onkeypress='return alphanumerals(event)'/></td>
			<td>COUNTRY CODE </td><td>: <s:textfield id="countrycode"  name="countrycode"  value="%{COUNTRY_CODE}" maxlength="5" onkeypress='return numerals(event)'></s:textfield> </td>
		</tr>
		
		<tr>
				<td>EMAIL ALERT REQUIRED </td><td>: <s:select headerKey="-1" headerValue="--Select  --" id="mailalertreq"  name="mailalertreq"  list="#{'Y':'YES','N':'NO'}" value="%{MAIL_ALERT_REQ}"></s:select></td>
				<td>SMS ALERT REQUIRED </td><td>: <s:select headerKey="-1" headerValue="--Select  --" id="smsalertreq"  name="smsalertreq"  list="#{'Y':'YES','N':'NO'}" value="%{SMS_ALERT_REQ}"></s:select></td>
		</tr>
		  <tr>
				<td>STATUS   </td><td>: <s:select headerKey="-1" headerValue="--Select  --" id="inststatus"  name="inststatus"  list="#{'1':'ACTIVE','0':'IN-ACTIVE','2':'DELETE'}" value="%{STATUS}"></s:select></td>
				<td></td>
		</tr>  
	</s:iterator>
	</table></div><br>
</fieldset><br>		
<br>		

		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="20%" >	
				<tr>
					<td> <s:submit name="submit"   id="submit" value="Update" onclick="return validate_form ( );"  ></s:submit></td>
					<td>
						<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>		 
					</td>
				</tr>
			</table>
		</div>
	
		
</s:iterator>
</s:form>
</div>
