<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="js/alpahnumeric.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>


<style>
div table td{
	padding-top:5px;
}
table{
	margin:0 auto;
}
table.txnlimitstyle td{
	padding-top:15px;
	border:1px solid #ededed;
}
.resmsg{
	text-align:center;
}
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
 
 var httpObject = null;
 function getHTTPObjectForBrowser(){
 if (window.ActiveXObject)
       return new ActiveXObject("Microsoft.XMLHTTP");
 else if (window.XMLHttpRequest) return new XMLHttpRequest();
 else {
      //alert("Browser does not support AJAX...........");
     return null;
    }
   }


function setAjaxOutput(){
    if(httpObject.readyState == 4){
    	
    	////alert(httpObject.responseText);
        document.getElementById('subproduct').innerHTML = httpObject.responseText;
       
       
    }
}

function numberOnly(evt){
	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
//	var keyvalue=evt.charCode;
////alert(keyvalue);
	if((!(keyvalue<48 || keyvalue>57)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13))
	{
		return true;
	}
	else
	{ 
		return false;
	} 
}	

function setAjaxOutputReload(){
    if(httpObject.readyState == 4){
    	
    	////alert(httpObject.responseText);
       
        document.getElementById('reloadcount').innerHTML = httpObject.responseText;
        
       
    }
}

function subProduct(){

 httpObject = getHTTPObjectForBrowser();
 if (httpObject != null) {
	 document.getElementById('reloadcount').innerHTML = "";
 	 var productid=(document.getElementById('deployment').value);
     httpObject.open("GET", "callSubProduct.do?prodid="+productid, true);
     
     httpObject.send(null);

 httpObject.onreadystatechange = setAjaxOutput;
      
    }
}

function reloadCount(){
	httpObject = getHTTPObjectForBrowser();
	 var subproduct=(document.ProductAddFofm.subproduct.value);
	 if(subproduct!='00')
	 {		if (httpObject != null) {
					 var productid=(document.getElementById('deployment').value);
				 	
				 	 ////alert(subproduct);
				 	 httpObject.open("GET", "callReloadcount.do?subprodid="+subproduct, true);
				     httpObject.send(null);
			
				 httpObject.onreadystatechange = setAjaxOutputReload;
				// //alert("dssd");    
				 }
	 
	 }
	 
}


function validateform()
{
	//alert("welcome");
	 
	var allcount = 0;
	var txn = document.getElementsByName("txncode");
	var txndesc = document.getElementsByName("txndesc");
	
	
	var checkdaily = document.getElementById("checkdaily");
	var checkweekly = document.getElementById("checkweekly");
	var checkmonthly = document.getElementById("checkmonthly");
	var checkyearly = document.getElementById("checkyearly");
	
	
	if((checkdaily.checked==false) && (checkweekly.checked==false) && (checkmonthly.checked==false) && (checkyearly.checked==false))
		{
		
		errMessage(checkdaily,"<b>Select Atleast Any One Transaction Type ...");
		return false;
		}
	
	
	
	//alert(txn.length);
	for(var i=0;i<txn.length;i++) {	
		//alert(document.getElementById("txndesc"+[i]).value;
		allcount = allcount + 1; 
		//alert(txn[i].value);
		var d_perday = document.getElementById("d_perday"+txn[i].value).value;
		//alert('d_perday'+d_perday);
		var d_count = document.getElementById("d_count"+txn[i].value).value;
		//alert('d_count'+d_count);
		
		var w_perday = document.getElementById("w_perday"+txn[i].value).value;
		//alert('w_perday'+w_perday);
		var w_count = document.getElementById("w_count"+txn[i].value).value;
		//alert('w_count'+w_count);
		
		var m_perday = document.getElementById("m_perday"+txn[i].value).value;
	//	alert('m_perday'+d_perday);
		var m_count = document.getElementById("m_count"+txn[i].value).value;
		//alert('m_count'+d_count);
		
		var y_perday = document.getElementById("y_perday"+txn[i].value).value;
	//	alert('y_perday'+d_perday);
		var y_count = document.getElementById("y_count"+txn[i].value).value;
	//	alert('y_count'+d_count);
	
if(checkdaily.checked)
		{
				if(d_perday=="")
				{
				errMessage(document.getElementById("d_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Amt per Day Should not empty");
				return false;	
				}
				
				if(d_count=="")
				{
				errMessage(document.getElementById("d_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Count per Day Should not empty");
				return false;	
				}
		}
if(checkweekly.checked)
{		
		if(w_perday=="")
		{
		errMessage(document.getElementById("w_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Amt per Week Should not empty");
		return false;	
		}
		
		if(w_count=="")
		{
		errMessage(document.getElementById("w_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Count per Week Should not empty");
		return false;	
		}
}		

if(checkmonthly.checked)
{
		if(m_perday=="")
		{
		errMessage(document.getElementById("m_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Amt per Month Should not empty");
		return false;	
		}
		
		if(m_count=="")
		{
		errMessage(document.getElementById("m_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Count per Month Should not empty");
		return false;	
		}
}

if(checkyearly.checked)
{
		if(y_perday=="")
		{
		errMessage(document.getElementById("y_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Amt per Year Should not empty");
		return false;	
		}
		
		if(y_count=="")
		{
		errMessage(document.getElementById("y_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> : Max Txn Count per Year Should not empty");
		return false;	
		}
		
}	   
	
	
if((checkweekly.checked) &&(checkdaily.checked))
{	
		
		
		if(parseInt(w_perday) < parseInt(d_perday))
			{
			errMessage(document.getElementById("w_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> Weekly AMT Should be Greater then Daily AMT");
			return false;	
			}   
		if(parseInt(w_count) < parseInt(d_count))
			{
			errMessage(document.getElementById("w_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> Weekly Count Should be Greater then Daily Count");
			return false;	
			}
}

if((checkmonthly.checked) &&(checkweekly.checked))
{		
		if(parseInt(m_perday) < parseInt(w_perday))
			{
			errMessage(document.getElementById("m_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> Monthly AMT Should be Greater then Daily AMT");
			return false;	
			}   
		if(parseInt(m_count) < parseInt(w_count))
			{
			errMessage(document.getElementById("m_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> Monthly Count Should be Greater then Daily Count");
			return false;	
			}  
}
if((checkyearly.checked) &&(checkmonthly.checked))
{
		if(parseInt(y_perday) < parseInt(m_perday))
			{
			errMessage(document.getElementById("y_perday"+txn[i].value),"<b>"+txndesc[i].value+"</b> Yearly AMT Should be Greater then Daily AMT");
			return false;	
			}   
		if(parseInt(y_count) < parseInt(y_count))
			{
			errMessage(document.getElementById("y_count"+txn[i].value),"<b>"+txndesc[i].value+"</b> Yearly Count Should be Greater then Daily Count");
			return false;	
			}  
		
}		
		
		
	}  
	//alert(allcount);
	if(txn.length == allcount) {
		
		////alert("All Cells Checked---> ");
		if ( confirm( 'Do you want to Submit' )){
			parent.showprocessing();
    		return true;
		}else{
    		return false;
		}
	}
}

function showLimitTypeDate( limittype ){ 
	var daterow = document.getElementById("daterow");
	if( limittype == "S"){
		daterow.style.display = "table-row";
		txnperiod.value="-1";
		txnperiod.disabled=true;
		
	}else{
		daterow.style.display = "none";
		txnperiod.disabled=false;
	} 
}

function deleteSubLimit( keycode ){
	var url = "deleteSubLimitAddLimitAction.do?keycode="+keycode;
	var result = AjaxReturnValue(url);
	alert( result )
}


function validation_deauthLimit(){
	//alert("Hai");
	var limit_id = document.getElementById("limitidid");
 	var auth = document.getElementById("auth0").value;
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		// alert("EMPTY");
		 var url = "authorizeDeauthorizeAddLimitAction.do?reason="+reason+"&auth="+auth+"&limitname="+limit_id.value;
		 window.location = url; 
	 }  
	 return false;
}



</script>

<% 	String limt = (String)session.getAttribute("limitype");
	String act = (String)session.getAttribute("act");
%>
<body> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="updateLimitAddLimitAction" id="limitform" name="limitform" autocomplete="off">
<input type="hidden" name="act"  id="act" value="<%=act%>">   
<s:hidden name="limitcurrencycode" id="limitcurrencycode" value="%{limitcurrencycode}" />
<s:hidden name="Limitnamedesc" id="Limitnamedesc" value="%{Limitname}" />
	
	<s:hidden value="%{limitidid}" id="limitidid" name = "limitidid"/>
		<s:hidden value="%{cardtypeid}" id="cardtypeid" name = "cardtypeid"/>
		<s:hidden value="%{cardtypename}" id="cardtypename" name = "cardtypename"/>
		<s:hidden value="%{accttypeid}" id="accttypeid" name = "accttypeid"/>
		<s:hidden value="%{accttypename}" id="accttypename" name = "accttypename"/>
		<s:hidden value="%{limitype}" id="limitype" name = "limitype"/>
		<s:hidden value="%{limitname}" id="limitname" name = "limitname"/>



<div align="center">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" align= "center" class="table">
		<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>
		<tr><td><input type="hidden" id="errormsg" name="errormsg"></td></tr> 
		<tr>
		<th>LIMITYPE : <s:property value="%{limittypedesc}" /> 
		</th>
		<s:if test="%{limitype=='CDTP'}">
		<th>CARDTYPE LIMIT : <s:property value="%{cardtypename}" /></th>
		</s:if>
		<s:elseif test="%{limitype=='ACTP'}">
		<th>ACCTTYPE LIMIT : <s:property value="%{accttypename}" /></th>
		</s:elseif>
		<s:elseif test="%{limitype=='CARD'}">
		<th>
		<th>CARDNO : <s:property value="%{cardno}" /></th>
		<th>NAME : <s:property value="%{custName}" /></th>
		<th>FROMDATE : <s:property value="%{fromdate}" /></th>
		<th>TODATE : <s:property value="%{todate}" /></th>
		</s:elseif>
		<s:elseif test="%{limitype=='ACCT'}">
		<th>ACCOUNT NO : <s:property value="%{accountnumber}" /></th>
		<th>FROMDATE : <s:property value="%{fromdate}" /></th>
		<th>TODATE : <s:property value="%{todate}" /></th>
		</s:elseif>   
		<th>LIMIT CURRENCY : <s:property value="%{limitcurrency}" /></th>
		</tr>
		
		
		
		
		
		</table>
		
			
		
				<table border='1' cellpadding='0' cellspacing='0' width='100%' class="formtable">
					<tr>
						
						
						<tr>
						<th colspan="1" rowspan="2" style='text-align:center'> Transaction Type</th> 
						<th colspan="2" style='text-align:center'> 
						<input type="checkbox" name="checkdaily" id="checkdaily" value="1" style='text-align:left' 
						<s:property value="%{dailyaccflag}" /> 
						>DAILY</th>
						<th colspan="2" style='text-align:center'>
						<input type="checkbox" name="checkweekly" id="checkweekly" value="1" style='text-align:left' 
						<s:property value="%{weekaccflag}" /> 
						>WEEK</th>
						<th colspan="2" style='text-align:center'>
						<input type="checkbox" name="checkmonthly"  id="checkmonthly" value="1" style='text-align:left' 
						<s:property value="%{monthaccflag}" /> 
						>MONTH </th>
						<th colspan="2" style='text-align:center'>
						<input type="checkbox"  name="checkyearly"  id="checkyearly" value="1" style='text-align:left' 
						<s:property value="%{yearaccflag}" /> 
						>YEAR</th>
						</tr>
						<tr>  
						<th> Max Txn Amt per Day</th> 
						<th> Max Txn Count per Day</th> 
						
						<th> Max Txn Amt per Week</th> 
						<th> Max Txn Count per Week</th> 
						
						<th> Max Txn Amt per Month</th> 
						<th> Max Txn Count per Month</th> 
						
						<th> Max Txn Amt per Year</th>    
						<th> Max Txn Count per Year</th> 

					</tr>
					<%int i=0,j=0; %>    
					<s:iterator value="limitvalueDetails">
					
						<input type='hidden' name='txncode' id='txncode_<%=i++%>' value='${TXNCODE}' />
						<input type='hidden' name='txndesc' id='txndesc<%=j++%>' value='${ACTION_DESC}' />
						<tr> 
								<td>
									${ACTION_DESC}
								</td>								 
								<td align=right> 
									<input type='text' name='d_perday${TXNCODE}' id='d_perday${TXNCODE}' style='width:100px;text-align:right' value="${AMOUNT}"/> 
								</td>	
								<td align=right> 
									<input type='text' name='d_count${TXNCODE}' id='d_count${TXNCODE}' style='width:100px;text-align:right' value="${COUNT}"/>  
								</td>
								<td align=right> 
									<input type='text' name='w_perday${TXNCODE}' id='w_perday${TXNCODE}'  style='width:100px;text-align:right' value="${WAMOUNT}"/>  
								</td>
								<td align=right> 
									<input type='text' name='w_count${TXNCODE}' id='w_count${TXNCODE}' style='width:100px;text-align:right' value="${WAMOUNT}"/>  
								</td>
								<td align=right> 
									<input type='text' name='m_perday${TXNCODE}' id='m_perday${TXNCODE}' style='width:100px;text-align:right' value="${MAMOUNT}"/>  
								</td>
								<td align=right> 
									<input type='text' name='m_count${TXNCODE}' id='m_count${TXNCODE}' style='width:100px;text-align:right' value="${MAMOUNT}"/>  
								</td>
								<td align=right> 
									<input type='text' name='y_perday${TXNCODE}' id='y_perday${TXNCODE}' style='width:100px;text-align:right' value="${YAMOUNT}"/>  
								</td>
								<td align=right> 
									<input type='text' name='y_count${TXNCODE}' id='y_count${TXNCODE}' style='width:100px;text-align:right' value="${YAMOUNT}"/>  
								</td>
								
						</tr>
					</s:iterator>					
					<tr>
 		  				<td colspan='9' style='text-align:center;'>
 		  				
 		  				<input type="submit" name="auth" id="auth1" value="Update" onclick="return validateform()"/>
						<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_deauthLimit()"/>
 		  				
 		  				</td> 		  
 		  			</tr>
 		  			
 		  			
				</table>
			
</div>
</s:form>
