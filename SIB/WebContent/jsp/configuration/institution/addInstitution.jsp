<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
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
    var orderrefval = document.addInstitution.orderref.value.substr(0,1);	
	var preferencevalue = document.addInstitution.preference.value.substr(0,1);
	var accnumlengvalue = document.addInstitution.accnumleng.value.substr(0,1);
	var cidvalue = document.addInstitution.cid.value.substr(0,1);
		
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
	else if ( document.addInstitution.orderref.value == "" )
    {	
		errMessage(orderref,"Please Enter Order Reference Number Length");
		return false;
   	}
	
	
	else if(orderrefval == "0"){
		errMessage(orderref," Order Reference Number Cannot be zero ");
		return false;
   	}
    else if ( document.addInstitution.branchattched.value == "-1" )
    {	
    	errMessage(branchattched,"Please select branch attched");
		return false;
   	}
   // else if ( document.addInstitution.preference.value == "" )
   // {	
   // 	errMessage(preference,"Please enter order preference");
//		return false;
   //	}
  // else if(preferencevalue == "0"){
//		errMessage(preference," Order preference Cannot be zero ");
///		return false;
 ///  	}
	else if ( document.addInstitution.countrycode.value == "" )
    {	
		errMessage(countrycode,"Please enter country code");
		return false;
   	}
	else if ( document.addInstitution.accnumleng.value == "" )
    {	
		errMessage(accnumleng,"Please Enter Account Number Length ");
		return false;
   	}
   	else if ( accnumlengvalue == "0")
    {	
		errMessage(accnumleng," Account Number Length cannot be zero ");
		return false;
   	}   
//	else if ( document.addInstitution.glcode.value == "-1" )
//    {	
//		errMessage(glcode,"Please Select GL Code Generation mode");
//	return false;
//   	}
	    
	else if ( document.addInstitution.cid.value == "" )
    {	
		errMessage(cid,"Please Enter Customer Id Length ");
		return false;
   	}else if ( cidvalue == "0" )
    {	
		errMessage(cid,"Customer Id Length cannot be zero");
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
	else if ( document.addInstitution.cardtypelength.value == "" )
    {	
		errMessage(cardtypelength,"Please enter Card Type Length");
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
    

    
    
    var maxaddoncards = document.getElementById("maxaddoncards");
    if( maxaddoncards ){ if( maxaddoncards.value == ""  || maxaddoncards.value == "-1") { errMessage(maxaddoncards, "Enter Max Addon Card !");return false; } }
    var maxaddonaccounts = document.getElementById("maxaddonaccounts");
    if( maxaddonaccounts ){ if( maxaddonaccounts.value == ""  || maxaddonaccounts.value == "-1") { errMessage(maxaddonaccounts, "Enter Max Addon Accounts !");return false; } }
    var pcadssenble = document.getElementById("pcadssenble");
    if( pcadssenble ){ if( pcadssenble.value == ""  || pcadssenble.value == "-1") { errMessage(pcadssenble, "Select PCADSS Enable !");return false; } }
    
    if(pcadssenble.value=='Y'){
    var seqkeyvalue = document.getElementById("seqkeyvalue");
    if( seqkeyvalue ){ if( seqkeyvalue.value == ""  || seqkeyvalue.value == "-1") { errMessage(seqkeyvalue, "Select Security Key !");return false; } }
    }
 
    
    if ( document.addInstitution.currencytype.value == "-1" )
    {	
    	errMessage(currencytype,"Please Select Institution Base Currency ");
	return false;
   	}

	var flag = true;
	var chckbox_len = document.getElementsByName("seccurrency").length;
	
	for(var i=0;i<chckbox_len;i++)
	{
		//alert(i);
		var chcked = document.getElementById("sec_currency"+i).checked;
		//alert("chcked--> "+chcked);
		if(chcked)
		{
			flag = false;
			break;
		}
		if(!(chcked))
		{
			flag = true;
		}
	}
	//if(flag)
	//{
		//alert("SELECT ATLEAST ONE CHKBOX --> "+chcked);
	//	errMessage(errmsg," Select Atleast One Secondary Currency ");
	//	return false;	
	//}    
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
		
		document.addInstitution.brcodelen.value=3;
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
    else
    	{
    	document.getElementById('ajax').innerHTML = "";
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

function getsecurityKey(val)
{
	if(val=='Y'){
	var url = "getsecurityKeyAddInstitutionAction.do?";
	var result = AjaxReturnValue(url);
	
	//alert( result );
	document.getElementById("padsskeylist").innerHTML="    PA DSS KEY: " +result;
	}
}
    
</script>

	<div align="center">
	<table border="0" cellpadding="0" cellspacing="0" width="90%"><tr> <td  align="center"> 
		<tr align="center">
			<td colspan="2">
				<%
					String addInstErrormsg = null;
					addInstErrormsg = (String) session.getAttribute("addInstErrormsg");
					session.removeAttribute("addInstErrormsg");
					
					String addInstErrorStatus = null;
					addInstErrorStatus = (String) session.getAttribute("addInstErrorStatus");
					session.removeAttribute("addInstErrorStatus");
				%>
				<%
					if (addInstErrormsg != null) 
					{
				%>
					<font color="Red"><b><%=addInstErrormsg%></b></font>
				<%
					} 
				%>	
			</td>
		</tr>
	</table>
	</div>
	 
		<%
			if(addInstErrorStatus != null)
			{
				if(!(addInstErrorStatus.equals("E")))
					{
		%>

 
<jsp:include page="/displayresult.jsp"></jsp:include> 
<div align="center">
<s:form action="addInstAddInstitutionAction" name="addInstitution" onsubmit="parent.showprocessing()" autocomplete="off">
<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
 <div id="ajax" style="color:red;width:850px;"></div>
<fieldset style="width:850px;">
	<table id="errmsg"></table>
    <legend><b>GENERAL  CONFIGURATION</b></legend>
    <div align="center">
   
	<table border="0" cellpadding="0" cellspacing="0" width="100%" >
	
		
			
		<tr>
			<td>INSTITUTION ID </td>
			<%-- <td> <s:textfield name="instid" id="instid" onchange="doAjaxCall();" maxlength="6"  value="%{bean.instid}"/></td> --%>
			<td> <s:textfield name="instid" id="instid" onchange="doAjaxCall();" maxlength="6" />
			<s:fielderror fieldName="instid" cssClass="errmsg" /></td>
			
			</td>
			<td>INSTITUTION NAME </td> 
			<td><s:textfield name="instname" id="instname" maxlength="30" onkeypress='return alphanumerals(event)' value="%{bean.instname}"/>
			<s:fielderror fieldName="instname" cssClass="errmsg" /></td>
			</td>
		</tr>
		
		<tr>
			<%-- <td>DEPLOYMENT TYPE </td><td> <s:select label="DEPLOYMENT TYPE"  headerKey="-1" headerValue="-- Select --"
			list="deploymentlist" 
				listValue="DEPLOYTYPE"
    			listKey="DEPLOYID" 
    			id="deploymenttype"
   				name="deploymenttype" />
   				<s:fielderror fieldName="deploymenttype" cssClass="errmsg" />
			</td> --%>
			
			
			
			<td>DEPLOYMENT TYPE </td><td> <s:select name="deploymenttype" 	id="deploymenttype" headerKey="-1" headerValue="-- Select --" list="deploymentlist" listValue="DEPLOYTYPE"  listKey="DEPLOYID" value="%{bean.deploymenttype}"/>
   				<s:fielderror fieldName="deploymenttype" cssClass="errmsg" />
			</td>
			
			<td>ORDER REFERENCE LENGTH </td><td> <s:textfield id="orderref"  name="orderref" maxlength="2" onkeypress='return numerals(event)'  value="%{bean.orderref}"></s:textfield>
			<s:fielderror fieldName="orderref" cssClass="errmsg" />
			 </td>
						
			<!--
			TOTAL NO OF BIN </td><td>: <s:textfield name="bincount" id="bincount" maxlength="3" onkeypress='return numerals(event)'/> 
			-->
			</tr>
		<tr>
				<td>BRANCH ATTACHED</td>
				<td><s:select headerKey="-1" headerValue="--Select  --" id="branchattched"  name="branchattched"  list="#{'Y':'Yes','N':'No' }" onchange="br_attach();"  value="%{bean.branchattched}"></s:select>
				<s:fielderror fieldName="branchattched" cssClass="errmsg" />
				 </td>
				
				<td style="width:233px">BRANCH CODE LENGTH</td>
				<td>
				<s:textfield name="brcodelen" id="brcodelen" maxlength="1" onkeypress='return numerals(event)' value="%{bean.brcodelen}"/>
					<s:fielderror fieldName="brcodelen" cssClass="errmsg" />
				</td>
	 	</tr>
		
		<tr>
				<td>COUNTRY CODE </td><td><s:textfield id="countrycode"  name="countrycode" maxlength="5"  onkeypress='return numerals(event)'  value="%{bean.countrycode}"></s:textfield> 
					<s:fielderror fieldName="countrycode" cssClass="errmsg" />
				</td>
				<!-- <td>BASE CURRENCY</td><td>: <s:textfield  id="basecurrency"  name="basecurrency" maxlength="3" ></s:textfield> </td> -->
				<td>ACC NUM LENGTH</td><td> <s:textfield id="accnumleng"  name="accnumleng" maxlength="2" onkeypress='return numerals(event)'  value="%{bean.accnumleng}"></s:textfield> 
				<s:fielderror fieldName="accnumleng" cssClass="errmsg" />
				</td>		
		</tr>
		<tr>
				<!--  <td>GL CODE GENERATION </td><td>: <s:select headerKey="-1" headerValue="--Select  --" id="glcode"  name="glcode"  list="#{'AUTO':'AUTOMATIC','MAN':'MANUAL'}"></s:select></td>-->
				<td>CUST ID LENGTH </td><td> <s:textfield id="cid"  name="cid" maxlength="2" onkeypress='return numerals(event)' value="%{bean.cid}"></s:textfield> 
					<s:fielderror fieldName="cid" cssClass="errmsg" />
				</td>		
				<td>CUST ID BASED ON </td>
				<td><s:select headerKey="-1" headerValue="--Select  --" id="cidbasedon"  name="cidbasedon"  list="#{'AUTO':'Auto','CBS':'CBS' }"  value="%{bean.cidbasedon}"></s:select>
					<s:fielderror fieldName="cidbasedon" cssClass="errmsg" />
				 </td>
		</tr>
		<tr>
				<td>EMAIL ALERT REQUIRED </td>
				<td><s:select headerKey="-1" headerValue="--Select  --" id="mailalertreq"  name="mailalertreq"  list="#{'Y':'YES','N':'NO'}"  value="%{bean.mailalertreq}"></s:select>
					<s:fielderror fieldName="mailalertreq" cssClass="errmsg" />
				</td>
				<td>SMS ALERT REQUIRED </td>
				<td><s:select headerKey="-1" headerValue="--Select  --" id="smsalertreq"  name="smsalertreq"  list="#{'Y':'YES','N':'NO'}"  value="%{bean.smsalertreq}"></s:select>
					<s:fielderror fieldName="smsalertreq" cssClass="errmsg" />
				</td>
		</tr>
		<tr>
				<td>ACCOUNTTYPE LENGTH </td>
				<td><s:textfield id="accttypelength"  name="accttypelength" maxlength="1" onkeypress='return numerals(event)' value="%{bean.accttypelength}"></s:textfield>
					<s:fielderror fieldName="accttypelength" cssClass="errmsg" />
				</td>
				<td>ACCOUNT SUBTYPE LENGTH</td>
				<td><s:textfield id="acctsubtypelength"  name="acctsubtypelength" maxlength="1" onkeypress='return numerals(event)' value="%{bean.acctsubtypelength}"></s:textfield>
						<s:fielderror fieldName="acctsubtypelength" cssClass="errmsg" />
				</td>
			
		
		</tr>
		<tr>
		<td>CARD TYPE LENGTH</td>
				<td><s:textfield id="cardtypelength"  name="cardtypelength" maxlength="1" onkeypress='return numerals(event)' value="%{bean.cardtypelength}"></s:textfield>
					<s:fielderror fieldName="cardtypelength" cssClass="errmsg" />
				</td>
	
	
		</tr>
		
		<!-- 
		<tr>
				<td>ORDER REFERENCE LENGTH </td><td>: <s:textfield id="orderref"  name="orderref" maxlength="2" onkeypress='return numerals(event)' ></s:textfield> </td>
		</tr> 
		-->
	</table></div><br>
</fieldset><br>		
<fieldset style="width:850px;">
<s:hidden id="preference"  name="preference" maxlength="1" onkeypress='return numerals(event)' value="%{bean.deploymenttype}"></s:hidden>
    <legend><b>OTHER CONFIGURATION</b></legend>	
		<table border="0" cellpadding="0" cellspacing="0" width="70%" >
			<tr>
				<td>LOGIN RETRY COUNT </td>
				<%-- <td><s:textfield id="login_retry_cnt"  name="login_retry_cnt" value="3" maxlength="1" maxLength="1" onkeypress='return numerals(event)' value="%{bean.login_retry_cnt}"></s:textfield> --%>
				<td><s:textfield id="login_retry_cnt"  name="login_retry_cnt" maxlength="1" maxLength="1" onkeypress='return numerals(event)' value="%{bean.login_retry_cnt}"></s:textfield>
				<s:fielderror fieldName="login_retry_cnt" cssClass="errmsg" />
				 </td>		
			</tr>
			<tr>
				<td>PIN RETRY COUNT </td>
				<%-- <td><s:textfield id="pin_retry_cnt"  name="pin_retry_cnt"  value="3" maxlength="1" onkeypress='return numerals(event)' value="%{bean.pin_retry_cnt}"></s:textfield> --%>
				<td><s:textfield id="pin_retry_cnt"  name="pin_retry_cnt" maxlength="1" onkeypress='return numerals(event)' value="%{bean.pin_retry_cnt}"></s:textfield>
				<s:fielderror fieldName="pin_retry_cnt" cssClass="errmsg" />
				 </td>		
			</tr>
			<tr>
					<td>Max Add On CARDs </td><td>
					<s:textfield id="maxaddoncards"  name="maxaddoncards" maxlength="1" onkeypress='return numerals(event)' value="%{bean.maxaddoncards}"></s:textfield> 
					<s:fielderror fieldName="maxaddoncards" cssClass="errmsg" />
					</td>
			</tr>
			<tr>
					<td>Max Add On Accounts </td><td>
					<s:textfield id="maxaddonaccounts"  name="maxaddonaccounts" maxlength="1" onkeypress='return numerals(event)' value="%{bean.maxaddonaccounts}"></s:textfield>
					<s:fielderror fieldName="maxaddonaccounts" cssClass="errmsg" />
					 </td>
			</tr>
			<tr>
					<td>Renewal Periods(In Months) </td><td>
					<s:textfield id="renewalperiods"  name="renewalperiods" maxlength="2" onkeypress='return numerals(event)' value="%{bean.renewalperiods}"></s:textfield> 
						<s:fielderror fieldName="renewalperiods" cssClass="errmsg" />
					</td>
			</tr>		
			</table>
		
		<br>
</fieldset>

<fieldset style="width:850px;">
    <legend><b>PA DSS </b></legend>	
		<table border="0" cellpadding="0" cellspacing="0" width="100%" >
			<tr>
				<td>PA DSS ENABLE: </td>
				<td> <select id="pcadssenble" name="pcadssenble" onchange="getsecurityKey(this.value)" >
					<option value="">Select PA DSS ENABLE</option>
								<option value="Y" >YES</option>   
								<option value="N">NO </option>
					</select>   
					<s:fielderror fieldName="pcadssenble" cssClass="errmsg" />
					</td>		
			
				
				<td id="padsskeylist">
				
				</td>		
			</tr>
			</table>
		
		<br>
</fieldset>
	
<fieldset style="width:850px;">
    <legend><b>CURRENCY</b></legend>	
	<table border="0" cellpadding="0" cellspacing="0" width="70%" >
	<tr>
	<td>BASE CURRENCY:</td><td>: <s:select label="CURRENCY TYPE"  
				headerKey="-1" headerValue="--Select CURRENCY TYPE --"
				list="Currencylist" 
				listValue="CURRENCY_DESC"
    			listKey="NUMERIC_CODE" 
    			id="currencytype"
   				name="defaultcurrency"  value="%{bean.currencytype}"/> 
   				
   				<s:fielderror fieldName="currencytype" cssClass="errmsg" />
   				</td>
   	</tr>
	<!--<tr>
	 <td>SECONDARY CURRENCY:</td>
	<td>
		<div id="sec_ccy"></div>
	</td>
	</tr>		 -->
			
	</table>
		<br>
</fieldset>
<br>		

		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="20%" >	
				<tr>
					<%-- <td> <s:submit name="submit"   id="submit" value="SUBMIT" onclick="return validate_form ( );"  ></s:submit></td>  --%>
					<td> <s:submit name="submit"   id="submit" value="SUBMIT" ></s:submit></td> 
					<td>
						<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>		 
					</td>
				</tr>
			</table>
		</div>
	
		

</s:form>
</div>
<% } } %>
