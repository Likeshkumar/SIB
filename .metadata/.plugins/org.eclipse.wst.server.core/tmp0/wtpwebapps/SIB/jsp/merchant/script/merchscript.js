function addstore_validation(){
	//alert("welcome");
	valid = true;
	var storename = document.getElementById("storename");
	var phoneone = document.getElementById("phoneone");
	var address = document.getElementById("address");
	var city = document.getElementById("city");
	var citycode = document.getElementById("citycode");
	var state = document.getElementById("state");
	var country = document.getElementById("country");
	var openhour = document.getElementById("openhour");
	var openminute = document.getElementById("openminute");
	var closehour = document.getElementById("closehour");
	var closeminute = document.getElementById("closeminute");
	var storestatus = document.getElementById("storestatus");
	if(storename)
	{
		if(storename.value == "")
		{
			errMessage(storename, "Enter the Store Name");
			return false;
		}
	}
	if(phoneone)
	{
		 if(phoneone.value == "")
		 {
			 errMessage(phoneone, "Enter the Phone number");
			 return false;
		 }
		 else
		 {
		 	if(isNaN(phoneone.value)|| phoneone.value.indexOf(" ")!=-1)
			{
			errMessage(phoneone,"Enter the valid Phone Number");
			return false;
			}
		 }
	}	
	if(address)
	{
		if(address.value == "")
		{
			errMessage(address, "Enter the Address");
			return false;
		}
	}
	if(city)
	{
		if(city.value == "")
		{
			errMessage(city, "Enter the City Name");
			return false;
		}
	}
	if(citycode)
	{
		if(citycode.value == "")
		{
			errMessage(citycode, "Enter the City Code");
			return false;
		}
	}
	if(state)
	{
		if(state.value == "")
		{
			errMessage(state, "Enter the State Name");
			return false;
		}
	}
	if(country)
	{
		if(country.value == "")
		{
			errMessage(country, "Enter the Country Name");
			return false;
		}
	}
	if(openhour)
	{
		if(openhour.value == "-1")
		{
			errMessage(openhour, "Select the Close Time Hour");
			return false;
		}
	}
	if(openminute)
	{
		if(openminute.value == "-1")
		{
			errMessage(openminute, "Select the Open Time Minute");
			return false;
		}
	}
	if(closehour)
	{
		if(closehour.value == "-1")
		{
			errMessage(closehour, "Select the Close Time Hour");
			return false;
		}
	}
	if(closeminute)
	{
		if(closeminute.value == "-1")
		{
			errMessage(closeminute, "Select the Close Time Minute");
			return false;
		}
	}

	if(storestatus){
		if(storestatus.value == "-1")
		{
			errMessage(storestatus,"Select Store Status");
			return false;
		}
	}
	return true;	
}


function addterminal_validation() {
	//alert("Welcome to terminal");
	var storeid = document.getElementById("storeid");
	var machineid = document.getElementById("machineid");
	var terminalid = document.getElementById("terminalid");
	var terminalname = document.getElementById("terminalname");
	var termlocation = document.getElementById("termlocation");
	var chipenabled = document.getElementById("chipenabled");
	var terminalmasterkey = document.getElementById("terminalmasterkey");
	var pinkey = document.getElementById("pinkey");
	var mackey = document.getElementById("mackey");
	var keyintvl = document.getElementById("keyintvl");
	var echointvl = document.getElementById("echointvl");
	var txnkeyintvl = document.getElementById("txnkeyintvl");
	var termstatus = document.getElementById("termstatus");
	var merchmcc = document.getElementById("merchmcc");
	 
	valid=true;
	if(storeid)
	{
		if(storeid.value == "-1-")
		{
		errMessage(storeid,"Select Store Name");
		return false;
		}
	}
	
	if(machineid)
	{
		if(machineid.value == "")
		{
		errMessage(machineid,"Enter Machine ID");
		return false;
		}
	}
	
	if(terminalid)
	{
		if(terminalid.value == "")
		{
		errMessage(terminalid,"Enter Terminal Id");
		return false;
		}
		
		if( terminalid.value.length != 8 ){
			errMessage(terminalid,"Terminal id shoud be 8 digits...");
			return false;
		}
	}
	if(terminalname)
	{
		if(terminalname.value == "")
		{
		errMessage(terminalname,"Enter Terminal Name");
		return false;
		}
	}
	if(termlocation)
	{
		if(termlocation.value == "")
		{
		errMessage(termlocation,"Enter Terminal location");
		return false;
		}
	}
	if(chipenabled)
	{
		if(chipenabled.value == "-1")
		{
		errMessage(chipenabled,"Select Chip Enabled");
		return false;
		}
	}
	if(terminalmasterkey)
	{
		if(terminalmasterkey.value == "")
		{
		errMessage(terminalmasterkey,"Enter Master Key");
		return false;
		}
	}
	if(pinkey)
	{
		if(pinkey.value == "")
		{
		errMessage(pinkey,"Enter Pin Key");
		return false;
		}
	}
	if(mackey)
	{
		if(mackey.value == "")
		{
		errMessage(mackey,"Enter MAC Key");
		return false;
		}
	}
	if(keyintvl)
	{
		if(keyintvl.value == "")
		{
		errMessage(keyintvl,"Enter Key Intervel");
		return false;
		}
	}
	if(echointvl)
	{
		if(echointvl.value == "")
		{
		errMessage(echointvl,"Enter Echo Intervel");
		return false;
		}
	}
	if(txnkeyintvl)
	{
		if(txnkeyintvl.value == "")
		{
		errMessage(txnkeyintvl,"Txn Key Intervel");
		return false;
		}
	}
	
	if(merchmcc)
	{
		if(merchmcc.value == "-1")
		{
			errMessage(merchmcc,"Select MCC ");
			return false;
		}
	}
	
	
	if(termstatus)
	{
		if(termstatus.value == "-1")
		{
		errMessage(termstatus,"Select Terminal status");
		return false;
		}
	} 
	
	
	return valid;
}

function merchreg_validation() {
	//alert("merchreg_validation");
	//var actbutton = document.getElementById("actbutton");
	//alert("actbutton==> "+actbutton.value);
	var merchantid = document.getElementById("merchantid");
	var merchantname = document.getElementById("merchantname");
	var firstname = document.getElementById("firstname");
	var lastname = document.getElementById("lastname");	
	var merchenttype = document.getElementById("merchenttype");
	var mcc = document.getElementById("mcc");
	var primary_mobileno = document.getElementById("primary_mobileno");
	var sec_mobileno = document.getElementById("sec_mobileno");
	var primary_mailid = document.getElementById("primary_mailid");
	var sec_mailid = document.getElementById("sec_mailid");
	var dob = document.getElementById("dob");
	var accttype = document.getElementById("accttype");
	//alert(accttype.value);
	var curcode = document.getElementById("curcode");
	var limitcode = document.getElementById("limitcode");
	var txngrpid = document.getElementById("txngrpid");
	var ubbgrpid = document.getElementById("ubbgrpid");
	var primarycur = document.getElementById("primarycur");
	var primcurfld = document.getElementById("primcurfld");
	var allowanceid = document.getElementById("allowanceid");
	var commissiongrpid = document.getElementById("commissiongrpid");
	var loytaltygrpid = document.getElementById("loytaltygrpid");
	var paddress1 = document.getElementById("paddress1");
	var paddress2 = document.getElementById("paddress2");
	
	var cbsacctno = document.getElementById("cbsacctno");
	var commissionbased = document.getElementById("commissionbased");
	var discountbased = document.getElementById("discountbased");
	var pricingmode = document.getElementById("pricingmode");
	valid=true;
	 
	if( merchantid ){
		if( merchantid.value == ""){ errMessage(merchantid,"ENTER MERCHANT ID"); return false; }
	}
 
	if( merchantid.value.length != 15 ){
		 errMessage(merchantid,"ENTERED MERCHANT ID IS NOT VALID.."); return false; 
	}
	
	
	if(merchantname)
	{
		if(merchantname.value == "")
		{
		errMessage(merchantname,"ENTER MERCHANT NAME");
		return false;
		}
	}
	
	if( firstname ){
		if(firstname.value == "")
		{
		errMessage(firstname,"ENTER MERCHANT FIRST NAME");
		return false;
		}
	}

	if( lastname ){
		if(lastname.value == "")
		{
		errMessage(lastname,"ENTER MERCHANT LAST NAME");
		return false;
		}
	}
	
	if(merchenttype)
	{
		if(merchenttype.value == "-1")
		{
		errMessage(merchenttype,"SELECT MERCHANT TYPE");
		return false;
		}
	}
	
	if(mcc)
	{
		
		if(mcc.value == "-1")
		{
			errMessage(mcc,"SELECT MCC");
			return false;
		}
		
		/*var opt = "<option value='-1'>SELECT</option>";
		var options = document.getElementById('mcc').options, count = 0;
		for (var i=0; i < options.length; i++) {
		  if (options[i].selected) { 
			  
			  opt += "<option value='"+options[i].value+"'>"+options[i].value+"</option>";
			  count++;	
			  
		  }
		 
		}
		
		if( count == 0 ){
			//alert(count);
			errMessage( document.getElementById('mcc'), "SELECT MCC");
			return false;
		}
		 
		 */
	}
	
	if(primary_mobileno)
	{
		if(primary_mobileno.value == "")
		{
		errMessage(primary_mobileno,"ENTER PRIMARY MOBILE NUMBER");
		return false;
		}
		 else
		 {
			 	if(isNaN(primary_mobileno.value)|| primary_mobileno.value.indexOf(" ")!=-1)
				{
				errMessage(primary_mobileno,"Enter the valid Mobile Number");
				return false;
				}
		 }
	}
	
	
	/*if(sec_mobileno)
	{
		if(sec_mobileno.value == "")
		{
		errMessage(sec_mobileno,"ENTER SECONDARY MOBILE NUMBER");
		return false;
		}
		else
		 {
			 	if(isNaN(sec_mobileno.value)|| sec_mobileno.value.indexOf(" ")!=-1)
				{
				errMessage(sec_mobileno,"Enter the valid Mobile Number");
				return false;
				}
		 }
	}*/
	
	if(primary_mailid)
	{
		if(primary_mailid.value == "")
		{
		errMessage(primary_mailid,"ENTER PRIMARY MAIL ID");
		return false;
		}
	  else
	  {
			 if( !emailvalidator( primary_mailid.value ) )
			 {
				 errMessage(primary_mailid,"InValid E-Mail Address" );
			 	return false;
			 }	
	  }
	}
	
	/*if(sec_mailid)
	{
		if(sec_mailid.value == "")
		{
		errMessage(sec_mailid,"ENTER SECONDARY MAIL ID");
		return false;
		}
	  else
	  {
			 if( !emailvalidator( sec_mailid.value ) )
			 {
				errMessage(sec_mailid,"InValid E-Mail Address" );
			 	return false;
			 }	
	  }
	}*/
	
	if(dob)
	{
		if(dob.value == "")
		{
		errMessage(dob,"ENTER DOB");
		return false;
		}
	}
	
	if(accttype)
	{
		if(accttype.value == "-1")
		{
			errMessage(accttype,"SELECT ACCOUNT TYPE");
			return false;
		}
	}
	
	if( cbsacctno ){
		if(cbsacctno.value == "")
		{
			errMessage(accttype,"ENTER ACCTNO / CARD NO");
			return false;
		}
	}
	
	if(curcode)
	{
		var opt = "<option value='-1'>SELECT</option>";
		var options = document.getElementById('curcode').options, count = 0;
		for (var i=0; i < options.length; i++) {
		  if (options[i].selected) { 
			  
			  opt += "<option value='"+options[i].value+"'>"+options[i].value+"</option>";
			  count++;	
			  
		  }
		 
		}
		
		if( count == 0 ){
			//alert(count);
			errMessage( document.getElementById('curcode'), "SELECT CURRENCY");
			return false;
		}
		/*if( count == 1 ){ 
			//alert(count);
			errMessage( document.getElementById('curcode'), "SELECT ATLEAST TWO CURRENCY");
			return false;
		}*/
		
	}
	if(primcurfld.style.visibility == 'visible' && primarycur.style.visibility == 'visible')
	{
		  if(primarycur.value == "-1")
		  {
			errMessage(primarycur,"SELECT PRIMARY CURRENCY");
			return false;
		  }
	}
	
	if(limitcode)
	{
		if(limitcode.value == "-1-")
		{
		errMessage(limitcode,"SELECT LIMIT FLAG");
		return false;
		}
	}
	
	if(txngrpid)
	{
		if(txngrpid.value == "-1")
		{
		errMessage(txngrpid,"SELECT TRANSACTION GROUP ID");
		return false;
		}
	}
	
	if(ubbgrpid)
	{
		if(ubbgrpid.value == "-1")
		{
		errMessage(ubbgrpid,"SELECT UBP GROUP ID");
		return false;
		}
	}
	
	if(allowanceid)
	{
		if(allowanceid.value == "-1")
		{
		errMessage(allowanceid,"SELECT ALLOWANCE GROUP ID");
		return false;
		}
	}
	if(commissiongrpid)
	{
		if(commissiongrpid.value == "-1")
		{
		errMessage(commissiongrpid,"SELECT COMMISSION GROUP ID");
		return false;
		}
	}
	if(loytaltygrpid)
	{
		if(loytaltygrpid.value == "-1")
		{
		errMessage(loytaltygrpid,"SELECT LOYALTY GROUP ID");
		return false;
		}
	}
	 
	
	if(commissionbased)
	{
		if( commissionbased.disabled == false ){
			if(commissionbased.value == "-1")
			{
			errMessage(commissionbased,"SELECT COMMISSION BASED NO");
			return false;
			}
		}
	}
	
	if(discountbased)
	{
		if(discountbased.disabled == false ){
			if(discountbased.value == "-1")
			{
			errMessage(discountbased,"SELECT DISCOUNT BASED NO");
			return false;
			}
		}
	}
	
	
	if(paddress1)
	{
		if(paddress1.value == "")
		{
		errMessage(paddress1,"ENTER ADDRESS1");
		return false;
		}
	}
	
	if(paddress2)
	{
		if(paddress2.value == "")
		{
		errMessage(paddress2,"ENTER ADDRESS2");
		return false;
		}
	}

	return valid;

	}

function addmerchhome_validation() {
	//alert("welcome");
	valid=true;
	var merchname = document.getElementById("merchname");
	//alert("merchname==> "+merchname.value);
	if(merchname)
	{
		if(merchname.value == "")
		{
		errMessage(merchname,"Enter Merchant Name");
		return false;	
		}
	}
	return valid;
}


function validateMerchantwalletReport(){
	var bankname = document.getElementById("bankname");
	var status = document.getElementById("status");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	
	if(bankname.value=="-1"){
		errMessage(bankname,"Select Bank Name");
		return false;
	}
	if(status.value=="-1"){
		errMessage(status,"Select Status");
		return false;
	}
	if(fromdate.value==""){
		errMessage(fromdate,"Select From date");
		return false;
	}
	if(todate.value==""){
		errMessage(todate,"Select To date");
		return false;
	}
	return true;
}
function validateMerchantdizeReport(){
	var businessdate = document.getElementById("businessdate");
	var merchid = document.getElementById("merchid");
	if(businessdate.value==""){
		errMessage(businessdate,"Business date is empty.");
		return false;
	}
	if(merchid.value==""){
		errMessage(merchid,"Enter MerchantID.");
		return false;
	}
	return true;
}

