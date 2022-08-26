function errMessage( fldid, msg ){  
	errmsg =  document.getElementById("errmsg");   
	errmsg.innerHTML = msg;
	fldid.focus();
	window.scrollTo(0, 0);
}

function errMessage(  msg ){  
	errmsg =  document.getElementById("errmsg");   
	errmsg.innerHTML = msg;	
	window.scrollTo(0, 0);
}

function validation_denomination(){	
	var productcode=document.getElementById("productcode");
	var denomvalue=document.getElementById("denomvalue");
	var denomdesc=document.getElementById("denomdesc");
	var expiryperiod=document.getElementById("expiryperiod");
	var batchlen =  document.getElementById("batchlen");
	if(productcode){
		if(productcode.value == "-1"){
			errMessage(productcode, " Select Product ");
			return false;
		}
	}
	if(denomvalue){
		if(denomvalue.value == ""){
			errMessage(denomvalue, " Enter Denom Value ");
			return false;
		}
	}
	if(denomdesc){
		if(denomdesc.value == ""){
			errMessage(denomdesc, " Enter Denom Description ");
			return false;
		}
	}
	if(expiryperiod){
		if(expiryperiod.value == ""){
			errMessage(expiryperiod, " Enter Expiry Period ");
			return false;
		}
	}
	
	if(batchlen){
		if(batchlen.value == ""){
			errMessage(batchlen, " Enter Batch ID Length ");
			return false;
		}
	}
	if(batchlen){
		if(batchlen.value <= 0){
			errMessage(batchlen, " Batch ID Length '0' Not Accepted");
			return false;
		}
	}
	
	var denomurl = "validateDenominationDenomConfiguration.do?denomvalue="+denomvalue.value;
	var denomresult = AjaxReturnValue(denomurl);
	//alert(result);
		if(denomresult!='DENOMVALETRUE'){
				errMessage(denomvalue, denomresult);
				return false;}
	var expiredateurl = "validateExpireDateDenomConfiguration.do?expiryperiod="+expiryperiod.value;
	var expiredateresult = AjaxReturnValue(expiredateurl);
			if(expiredateresult!='EXPIREVALETRUE'){
				errMessage(expiryperiod, expiredateresult);
			return false;}
	
	return true;
	
	
}


function viewDenominationvalidation(){
	var productcode = document.getElementById("productcode");
	var denomvalue  = document.getElementById("denomvalue");
	var noofcards   = document.getElementById("noofcards");
	
	if(productcode){
		if(productcode.value == "-1"){
			errMessage(productcode, " Select Product ");
			return false;
		}
	}
	if(denomvalue){
		if(denomvalue.value == "-1"){
			errMessage(denomvalue, " Select Denom Value ");
			return false;
		}
	}
	if(noofcards){
		if(noofcards.value == ""){
			errMessage(noofcards, " Enter Number of cards ");
			return false;
		}
	}
	
	var cardcounteurl = "validateCardCountBatchGenerationProcess.do?noofcards="+noofcards.value;
	var cardcounteurlresult = AjaxReturnValue(cardcounteurl);
			if(cardcounteurlresult!='CARDCOUNTTRUE'){
				errMessage(noofcards, cardcounteurlresult);
			return false;}
	
	return true;
}
	/*function validation_generatebatch(){	
		
		var productcodeid = document.getElementById("productcode").value;
		alert(productcodeid);
		if( productcode != -1 ){
			var url = "getAuthDenomValue.do?productcodeid="+productcodeid+"&instid="+instid;
			var result = AjaxReturnValue(url);
			document.getElementById("denomvalue").innerHTML = result;
		}else{
			errMessage(productcode,"Select Product");
		}
		return false;
	}*/


///kumar addedd this function on 02dec14
function changeDenomDesc()
{
	var denomvalue = document.getElementById("denomvalue").value;
	if(denomvalue.length>0){
		document.getElementById("denomdesc").value = denomvalue+' TZS';
	}else{
		document.getElementById("denomdesc").value = "";	
	}
}