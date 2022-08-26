<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="s" uri="/struts-tags" %>
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


select:disabled
{
 color:; 
 background:;
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
      alert("Browser does not support AJAX...........");
     return null;
    }
   }


function setAjaxOutput(){
    if(httpObject.readyState == 4){
    	
    	//alert(httpObject.responseText);
        document.getElementById('subproduct').innerHTML = httpObject.responseText;
       
       
    }
}

function numberOnly(evt){
	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
//	var keyvalue=evt.charCode;
//alert(keyvalue);
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
    	
    	//alert(httpObject.responseText);
       
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
				 	
				 	 //alert(subproduct);
				 	 httpObject.open("GET", "callReloadcount.do?subprodid="+subproduct, true);
				     httpObject.send(null);
			
				 httpObject.onreadystatechange = setAjaxOutputReload;
				// alert("dssd");    
				 }
	 
	 }
	 
}


function validateform ()
{
	//alert("welcome");
	valid=true;
	var err_msg = document.getElementById("errormsg");
	var txn = document.getElementsByName("txn");
	if ( document.ProductAddFofm.limitdesc.value == "" )
    {	
    	// alert ( "Please Select Product Type" );
    	 errMessage(err_msg, "Please Enter LIMIT DESC");
    	 return false;	 
	}
	for(var i=0;i<txn.length;i++)
	{	
		for(var j=0;j<4;j++)
		{
			var txnid = document.getElementById("txn"+j).value;
			var limitvl = "limitval"+txnid;
			//alert("limitvl====> "+limitvl);
			var limitval = document.getElementById(limitvl);
			
			var limitcnt = "limitcnt"+txnid;
			//alert("limitcnt====> "+limitcnt);
			var limitcnt = document.getElementById(limitcnt);
			
			var txnperiod = "txnperiod"+txnid;
			//alert("txnperiod====> "+txnperiod);
			var txnperiod = document.getElementById(txnperiod);
			
			var pertxnamt = "pertxnamt"+txnid;
			//alert("pertxnamt====> "+pertxnamt);
			var pertxnamt = document.getElementById(pertxnamt);
			
			if ( limitval.value == "" )
		    {	
		    	 //alert ( "Please Enter Limit Amount" );
		    	 errMessage(err_msg, "Please Enter Limit Amount ");
		    	 return false; 
			}
			
			else if ( limitcnt.value == "" )
		    {	
		    	 //alert ( "Please Enter Limit Count" );
		    	 errMessage(err_msg, "Please Enter Limit Count ");
		    	 return false;
			}

			else if ( txnperiod.value == "0" )
		    {	
		    	 //alert ( "Please select Transaction period" );
		    	 errMessage(err_msg, "Please select Transaction period ");
		    	 return false; 
			}
			else if ( pertxnamt.value == "" )
		    {	
		    	 //alert ( "Please Enter Per txn Amount period" );
		    	 errMessage(err_msg, "Please Enter Per txn Amount period ");
		    	 return false; 
			}
		}
		
		if ( confirm( 'Do you want to Submit' )){
	    	return true;
	    }else{
	    	return false;
	    }
		return true;	
	}

}

</script>
<body>
<% String eventvalue = (String)session.getAttribute("EVENT");
%> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="saveTxnLimitAddLimitAction" name="ProductAddFofm" autocomplete="off">
<div align="center">
	<table border="0" cellpadding="0" cellspacing="0" width="80%" align= "center" class="table">
		<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>
		<tr><td><input type="hidden" id="errormsg" name="errormsg"></td></tr> 
			<tr>
			<s:iterator value="viewlmtdesc">
				<td align="right">LIMIT DESC</td> 
				<td><input type='text' name='limitdesc' id='limitdesc' value='${LIMIT_DESC}' readonly="readonly"></td>
				<s:hidden name="limitid" id="limitid" value='%{LIMIT_ID}'/>
			</s:iterator>
			</tr>
			
			
			
		<tr>
			<td colspan='4' style='padding-top:10px'>
				
				<table border='0' cellpadding='0' cellspacing='0' width='100%' class='txnlimitstyle'>
					
					<tr>
						<th> Transaction Type</th> 
						<th align=right> Limit Amount </th>
						<th> Limit Count </th>
						<th> Period </th>
						<th> Per Txn Amount </th>
					</tr>
					<%int i=0; %>
					
					<s:iterator value="limitmasterlis">
						<s:action name="trxnsactioncodeAddLimitAction" executeResult="false"  var="transcode">
								<s:param name="txncode" >${TXN_CODE}</s:param>
						</s:action>
						<s:set name="trancode" value="%{TXN_CODE}"></s:set>
					<tr>
					
					
						<s:iterator value="#transcode.limitinfolist">
															
								<s:set name="period" value='%{PERIOD}'/>
								
								<td>
									${ACTION_DESC}
								</td>								 
								<td align=right> 
									<input type='text' id="limitval${TXN_CODE}" value="${LIMIT_AMOUNT}" style='width:100px;text-align:right' name='limitval_${TXN_CODE}' readonly="true"/> 
								</td>								
								<td align=right> 
									<input type='text' id="limitcnt${TXN_CODE}" value="${LIMIT_COUNT}" style='width:100px;text-align:right' name='limitcnt_${TXN_CODE}' readonly="true"/> 
								</td>								
								<td align=center>
									<input type='text' name="txnperiod_${TXN_CODE}" id="txnperiod${TXN_CODE}" value="${PERIOD}" style='width:100px;text-align:right' name='limitcnt_${TXN_CODE}' readonly="true"/>
								</td>								
								<td align=right>
									 <input type='text' id="pertxnamt${TXN_CODE}" value="${PERTXNAMT}" style='width:100px;text-align:right' name='pertxnamt_${TXN_CODE}' readonly="true"/> 
								</td>
							
						</s:iterator>
					</tr>
					</s:iterator>
										
					
				</table>
				
			</td>
		</tr>
		
 	
 		
		
</table>
</div>
</s:form>
