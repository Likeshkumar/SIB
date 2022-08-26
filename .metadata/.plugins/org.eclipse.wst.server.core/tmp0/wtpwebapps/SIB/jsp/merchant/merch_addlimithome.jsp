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
	valid=true;
	var err_msg = document.getElementById("errormsg");
	var txn = document.getElementsByName("txn");
	//alert("Transact==> "+txn.length);
	if ( document.ProductAddFofm.limitdesc.value == "" )
    {	
    	// //alert ( "Please Select Product Type" );
    	 errMessage(err_msg, "Please Enter LIMIT DESC");
    	 return false;	 
	}
	var allcount = 0;
	for(var i=0;i<txn.length;i++)
	{	
		allcount = allcount + 1;
		//alert("inside 1st for loop"+i);
		var txnid = document.getElementById("txn"+i).value;
		var txndesc = document.getElementById("txndesc"+i).value;
		//alert("Transaction id"+txndesc);
			var limitvl = "limitval"+txnid;
			////alert("limitvl====> "+limitvl);
			var limitval = document.getElementById(limitvl);
			
			var limitcnt = "limitcnt"+txnid;
			////alert("limitcnt====> "+limitcnt);
			var limitcnt = document.getElementById(limitcnt);
			
			var txnperiod = "txnperiod"+txnid;
			////alert("txnperiod====> "+txnperiod);
			var txnperiod = document.getElementById(txnperiod);
			////alert ( "select Box value is==> " +txnperiod.value);
			var pertxnamt = "pertxnamt"+txnid;
			////alert("pertxnamt====> "+pertxnamt);
			var pertxnamt = document.getElementById(pertxnamt);
			
			if ( limitval.value == "" )
		    {	
		    	 ////alert ( "Please Enter Limit Amount" );
		    	 errMessage(err_msg, "Please Enter "+txndesc+" Limit Amount ");
		    	 return false; 
			}
			/* if(limitval.value == "0")
			{
				errMessage(err_msg, "Limit Amount is Invalid.Enter valid Amount in "+txndesc+"");
		    	return false;
			}
			if (limitval.value.startsWith("0")) // true
			{
				errMessage(err_msg, "Cannot enter zero at first place in "+txndesc+"");
		    	return false;
			} */
			if ( limitcnt.value == "" )
		    {	
		    	 ////alert ( "Please Enter Limit Count" );
		    	 errMessage(err_msg, "Please Enter "+txndesc+" Limit Count ");
		    	 return false;
			}
			/* if(limitcnt.value == "0")
			{
				errMessage(err_msg, "Limit Count is Invalid.Enter valid Amount in "+txndesc+"");
		    	return false;
			}
			if (limitcnt.value.startsWith("0")) // true
			{
				errMessage(err_msg, "Cannot enter zero at first place in "+txndesc+"");
		    	return false;
			} */
			if((parseInt(limitcnt.value)>parseInt(limitval.value)))
			{
				errMessage(err_msg, "Limit Count is greater than Limit Amount in "+txndesc+"");
		    	return false;
			}
			if ( txnperiod.value == "0" )
		    {	
		    	 ////alert ( "Please select Transaction period" );
		    	 errMessage(err_msg, "Please select "+txndesc+" Transaction period ");
		    	 return false; 
			}
			if ( pertxnamt.value == "" )
		    {	
		    	 ////alert ( "Please Enter Per txn Amount period" );
		    	 errMessage(err_msg, "Please Enter "+txndesc+" Per txn Amount period ");
		    	 return false; 
			}
			/* if( pertxnamt.value == "0" )
			{
				errMessage(err_msg, "Per Txn Amount is Invalid.Enter valid Amount in "+txndesc+"");
		    	return false;
			}
			if (pertxnamt.value.startsWith("0")) // true
			{
				errMessage(err_msg, "Cannot enter zero at first place in "+txndesc+"");
		    	return false;
			} */
			if((parseInt(pertxnamt.value)>parseInt(limitval.value)))
			{
				errMessage(err_msg, "Per Txn Amount is greater than Limit Amount in "+txndesc+"");
		    	return false;
			}
	}
	if(txn.length == allcount)
	{
		////alert("All Cells Checked---> ");
		if ( confirm( 'Do you want to Submit' )){
    		return true;
		}else{
    		return false;
		}
	}
}

</script>
<body> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="saveTxnLimitMerchantProcess" name="ProductAddFofm" autocomplete="off">
<div align="center">
	<table border="0" cellpadding="0" cellspacing="0" width="80%" align= "center" class="table">
		<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>
		<tr><td><input type="hidden" id="errormsg" name="errormsg"></td></tr> 
		
	
		<tr>
			<td align="right">LIMIT DESC</td>
			<td>:</td>
			<td><input type='text' name='limitdesc' id='limitdesc' maxlength="24"></td>
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
					<%int i=0,j=0; %>
					<s:iterator value="limitmasterlis">
					
						<input type='hidden' name='txn' id='txn<%=i++%>' value='${TXN_CODE}' />
						<input type='hidden' name='txndesc' id='txndesc<%=j++%>' value='${TXN_DESC}' />
						<tr> 
								<td>
									${TXN_DESC}
								</td>								 
								<td align=right> 
									<input type='text' id="limitval${TXN_CODE}"  style='width:100px;text-align:right' name='limitval_${TXN_CODE}' maxlength="11" onKeyPress="return numerals(event);"/> 
								</td>								
								<td align=right> 
									<input type='text' id="limitcnt${TXN_CODE}"  style='width:100px;text-align:right' name='limitcnt_${TXN_CODE}' maxlength="2" onKeyPress="return numerals(event);"/> 
								</td>								
								<td align=center>
									<select name="txnperiod_${TXN_CODE}" id="txnperiod${TXN_CODE}" style="width:100px">
										<option value='0'> --SELECT-- </option>
										<option value='D' > Daily </option>
										<option value='W'> Weekly </option>
										<option value='M'> Monthly </option> 
									</select>
								</td>								
								<td align=right>
									 <input type='text' id="pertxnamt${TXN_CODE}" style='width:100px;text-align:right' name='pertxnamt_${TXN_CODE}' maxlength="7" onKeyPress="return numerals(event);"/> 
								</td>
						</tr>
					</s:iterator>					
					<tr>
 		  				<td colspan='5' style='text-align:center;'><s:submit value="Add Limit" name="submit" onclick="return validateform()" /></td> 		  
 		  			</tr>
				</table>
				
			</td>
		</tr>
		
 	
 		
		
</table>
</div>
</s:form>
