<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
 .tbheader{
 	font-weight:bold;
 }
 .txt{
 	font-weight:bold;
 }
 </style>
<head>

<%
String instid = (String)session.getAttribute("Instname");
 
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 
<script>


function showProcessing(){
	parent.showprocessing();
}


function addAccountNo(value)
{
	var accountno;
	if(document.getElementById("tab2_barachcode").checked = true)
		{
		 accountno=document.getElementById("tab2_barachcode").value;
		}
	else
		{
		accountno='';
		}
	
	if(document.getElementById("tab2_productcode").checked = true)
	{
	 accountno=accountno+document.getElementById("tab2_productcode").value;
	}
	else
		{
		accountno='';
		}
	
	document.getElementById("accountnovalue_0").value=accountno;
}


	function confirmComplete(){
		var applicationid = document.getElementById("applicationid");
		
		if( !confirm("Do you want to submit ") ){
			return false;
		}
		var url = "applicationCompletedCreditCustRegisteration.do?reqfrom=6&applicationid="+applicationid.value;
		//alert( url );
		window.location = url;
	}
	function deleteSuppliment( customerid ){ 
		var applicationid = document.getElementById("applicationid");
		if( !confirm("Do you want to delete ") ){
			return false;
		}
		var url = "deleteSupplimentCardCreditCustRegisteration.do?applicationid="+applicationid.value+"&customerid="+customerid+"&reqfrom=6&gotoval=6"; 
		//alert( url )
		window.location=url;
	}
	
	function getSupRelation( val ){
		//alert( val );
	}
	
	function validateSuplimentDet(){
		var supfirstname  = document.getElementById("supfirstname");
		var supmidname= document.getElementById("supmidname");
		var suplastname= document.getElementById("suplastname");
		var supdob= document.getElementById("supdob");
		var supnationality= document.getElementById("supnationality");
		var supnameoncard= document.getElementById("supnameoncard");
		var supgender= document.getElementById("suprelationship");
		
		if( supfirstname ){ if( supfirstname.value == ""){errMessage(supfirstname,"Enter First Name !") ; return false; }}
	}
	
	
	function validateIncomeDetails(){
		var annualsal  = document.getElementById("annualsal");
		var annualbonus= document.getElementById("annualbonus");
		var annualbusinessincome= document.getElementById("annualbusinessincome");
		var rentalincome= document.getElementById("rentalincome");
		var agriculture= document.getElementById("agriculture");
		var otherincome= document.getElementById("otherincome");
		var totannualincome= document.getElementById("totannualincome");
		  
		var existyes= document.getElementById("existyes");
		var existno= document.getElementById("existno");
		
		var excrcardbank= document.getElementById("excrcardbank");
		var excrcardbranch= document.getElementById("excrcardbranch");
		var excrcardacctype= document.getElementById("excrcardacctype");
		 
		var validfrom= document.getElementById("validfrom");
		var validto= document.getElementById("validto");
		var acctwithprimarybank= document.getElementById("acctwithprimarybank");
		var primarybankacctno= document.getElementById("primarybankacctno");
		var primarybankbranch= document.getElementById("primarybankbranch");
		var primaryacctcur= document.getElementById("primaryacctcur");
		var paytype= document.getElementById("paytype");
		var payableamt= document.getElementById("payableamt");
		var payableday= document.getElementById("payableday");
		
		 
		
		if( annualsal ){ if( annualsal.value == ""){errMessage(annualsal,"Enter Annual Salary !") ; return false; }}
		if( totannualincome ){ if( totannualincome.value == "NaN"){errMessage(totannualincome,"Invalid Income Details, Invalid Amount Injected as Input  !") ; return false; }}
		
		if( existyes ){
			if( existyes.checked){
				if( excrcardbank ){ if( excrcardbank.value == ""){errMessage(excrcardbank,"Enter Existing Credit Card Bank Name !") ; return false; }}
				if( excrcardbranch ){ if( excrcardbranch.value == ""){errMessage(excrcardbranch,"Enter Existing Credit Branch Bank Name !") ; return false; }}
				if( excrcardacctype ){ if( excrcardacctype.value == "-1"){errMessage(excrcardacctype,"Select Existing Credit Card Account Type!") ; return false; }}
			}
		}
		
		
		if( validfrom ){ if( validfrom.value == ""){errMessage(validfrom,"Select Valid From Date !") ; return false; }}
		if( validto ){ if( validto.value == ""){errMessage(validto,"Select Valid To Date !") ; return false; }}
	 
		if( primarybankbranch ){ if( primarybankbranch.value == ""){errMessage(primarybankbranch,"Enter Primary Bank Branch Name !") ; return false; }}
		if( primarybankacctno ){ if( primarybankacctno.value == ""){errMessage(primarybankacctno,"Enter Primary Bank Account Number !") ; return false; }}
		
		if( primaryacctcur ){ if( primaryacctcur.value == "-1"){errMessage(primaryacctcur,"Select Currency Code!") ; return false; }}
		if( paytype ){ if( paytype.value == "-1"){errMessage(paytype,"Select Pay Type!") ; return false; }}
		if( paytype.value == "$FIXED"){
			 if( payableamt.value == ""){errMessage(payableamt,"Enter Payable Amount !") ; return false; }
		}
		if( payableday ){ if( payableday.value == ""){errMessage(payableday,"Enter Payable Day !") ; return false; }}
		if( payableday.value > 30 ){
			errMessage(payableday,"Enter The Payable Day Range 1 to 30 !") ; return false;
		}
		
	}
	
	function addTotalAnnual(){
		var annualsal  = document.getElementById("annualsal");
		var annualbonus= document.getElementById("annualbonus");
		var annualbusinessincome= document.getElementById("annualbusinessincome");
		var rentalincome= document.getElementById("rentalincome");
		var agriculture= document.getElementById("agriculture");
		var otherincome= document.getElementById("otherincome"); 
		var totannualincome= document.getElementById("totannualincome");
		
		totannualincome.value = Number(annualsal.value)+Number(annualbonus.value)+Number(annualbusinessincome.value)+Number(rentalincome.value)+Number(agriculture.value)+Number(otherincome.value);
	}
	
	function showBankDet( val ){
		
		var trcreditbankdet = document.getElementById("trcreditbankdet");
		if ( val == "$Y" ){
			trcreditbankdet.style.display ="table-row";
			var acctwithprimarybank = document.getElementById("acctwithprimarybank");
			acctwithprimarybank.focus();
		}else{
			trcreditbankdet.style.display ="none";
		}
		
	}
	
	
	function validateRerefenceForm(){ 
		var documentcount = document.getElementById("documentcount");
		var documentmastercount = document.getElementById("documentmastercount");
		var refcount = document.getElementById("refcount");
		var refmastercount = document.getElementById("refmastercount");
		var applicationid =  document.getElementById("applicationid");
		var customerid =  document.getElementById("customerid2"); 
		
		//alert( "customerid :" + customerid.value )
		//return false;
		if(    refcount.value < refmastercount.value ){
			 errMessage(documentcount, "Need " +refmastercount.value+ " References..... !");return false; 
		}
		
		if(    documentcount.value < documentmastercount.value ){
			 errMessage(documentcount, "Need " +documentmastercount.value+ " Documents..... !");return false; 
		}
		
		var url="customerDetailsAddCreditCustRegisteration.do?gotoval=5&reqfrom=4&applicationid="+applicationid.value+"&customerid="+customerid.value;
		//alert(url)
		window.location=url;
		return true;
	}

	
	function setContactAddress( checkedval ){
	 
		var p_poxbox = document.getElementById("p_poxbox");
		var p_houseno = document.getElementById("p_houseno");
		var p_streetname = document.getElementById("p_streetname");
		var p_wardnumber = document.getElementById("p_wardnumber");
		var p_city = document.getElementById("p_city");
		var p_district = document.getElementById("p_district");
		var p_phone1 = document.getElementById("p_phone1");
		var p_phone2 = document.getElementById("p_phone2");
		
		//sameasperm
		
		
		
		var c_poxbox = document.getElementById("c_poxbox");
		var c_houseno = document.getElementById("c_houseno");
		var c_streetname = document.getElementById("c_streetname");
		var c_wardnumber = document.getElementById("c_wardnumber");
		var c_city = document.getElementById("c_city");
		var c_district = document.getElementById("c_district");
		var c_phone1 = document.getElementById("c_phone1");
		var c_phone2 = document.getElementById("c_phone2");
		
		if( checkedval ){	
			 c_poxbox.value = p_poxbox.value;
			 c_houseno.value = p_houseno.value;
			 c_streetname.value = p_streetname.value;
			 c_wardnumber.value = p_wardnumber.value;
			 c_city.value = p_city.value;
			 c_district.value = p_district.value;
			 c_phone1.value = p_phone1.value;
			 c_phone2.value = p_phone2.value;
			}
	}
	
	function valiteContactDetails(){
		var prpobox = document.getElementById("prpobox");
		var prhouseno = document.getElementById("prhouseno");
		var prstreetname = document.getElementById("prstreetname");
		var prwardname = document.getElementById("prwardname"); 
		var prcity = document.getElementById("prcity");
		var prdistrict = document.getElementById("prdistrict");
		var prphone1 = document.getElementById("prphone1");
		var prphone2 = document.getElementById("prphone2");
		var prfax = document.getElementById("prfax");
		var primaryemail = document.getElementById("primaryemail");
		var prmobileno = document.getElementById("prmobileno");
		
		var sameasperm = document.getElementById("sameasperm");
		
		var copobox = document.getElementById("copobox");
		var cohouseno = document.getElementById("cohouseno");
		var costreetname = document.getElementById("costreetname");
		var cowardname = document.getElementById("cowardname"); 
		var cocity = document.getElementById("cocity");
		var codistrict = document.getElementById("codistrict");
		var cophone1 = document.getElementById("cophone1");
		var cophone2 = document.getElementById("cophone2");
		var cofax = document.getElementById("cofax");
		var secemail = document.getElementById("secemail");
		var comobileno = document.getElementById("comobileno");
		
		if( prpobox ){ if( prpobox.value == "") { errMessage(prpobox, "Enter Po Box No.!");return false; } }
		if( prhouseno ){ if( prhouseno.value == "") { errMessage(prhouseno, "Enter House No.!");return false; } }
		if( prstreetname ){ if( prstreetname.value == "") { errMessage(prstreetname, "Enter Street Name !");return false; } }
		if( prcity ){ if( prcity.value == "") { errMessage(prcity, "Enter City!");return false; } }
		if( prdistrict ){ if( prdistrict.value == "") { errMessage(prdistrict, "Enter District!");return false; } }
		if( prmobileno ){ if( prmobileno.value == "") { errMessage(prmobileno, "Enter Mobile No.!");return false; } }
	 
		if( !sameasperm.checked ){
			if( copobox ){ if( copobox.value == "") { errMessage(copobox, "Enter Contact Address Po Box No.!");return false; } }
			if( cohouseno ){ if( cohouseno.value == "") { errMessage(cohouseno, "Enter Contact Address House No.!");return false; } }
			if( costreetname ){ if( costreetname.value == "") { errMessage(costreetname, "Enter Contact Address Street Name !");return false; } }
			if( cocity ){ if( cocity.value == "") { errMessage(cocity, "Enter Contact Address City!");return false; } }
			if( codistrict ){ if( codistrict.value == "") { errMessage(codistrict, "Enter Contact Address District!");return false; } }
			if( comobileno ){ if( comobileno.value == "") { errMessage(comobileno, "Enter Conact Mobile No.!");return false; } }
		}
	}
	
	
	function validateBusiness(){
		var occupation = document.getElementById("occupation");
		var workfor = document.getElementById("workfor");
		var designation = document.getElementById("designation");
		var companyname = document.getElementById("companyname");
		var natofbusiness = document.getElementById("natofbusiness");
		var employedsince = document.getElementById("employedsince");
		
		var emppobox = document.getElementById("emppobox");
		var emphouseno = document.getElementById("emphouseno");
		var empstreetname = document.getElementById("empstreetname");
		var empwardname = document.getElementById("empwardname"); 
		var empcity = document.getElementById("empcity");
		var empdistrict = document.getElementById("empdistrict");
		var empphone1= document.getElementById("empphone1");
		var empphone2 = document.getElementById("empphone2");
		var empfax = document.getElementById("empfax");
		var empemail = document.getElementById("empemail");
		var empmobileno = document.getElementById("empmobileno");
		
		//alert( workfor.value + "--" + companyname.value );
		
		if( occupation ){ if( occupation.value == "-1") { errMessage(occupation, "Select Occupation !");return false; } }
		if( workfor ){ if( workfor.value == "-1") { errMessage(workfor, "Select WorkFor!");return false; } } 
		if( workfor ){ if( workfor.value == "-1") { errMessage(workfor, "Select WorkFor!");return false; } }
		
		if( workfor.value == "$PRIV"){
			if( companyname ){ if( companyname.value == "") { errMessage(companyname, "Enter Company Nmae !");return false; } }
		}
		
		if( designation ){ if( designation.value == "") { errMessage(designation, "Enter Designation !");return false; } }
		
		if( natofbusiness ){ if( natofbusiness.value == "") { errMessage(natofbusiness, "Enter Nature of Business !");return false; } }
		if( employedsince ){ if( natofbusiness.value == "") { errMessage(employedsince, "Select Employed Since !");return false; } }
		
		if( emppobox ){ if( emppobox.value == "") { errMessage(emppobox, "Enter Employee/Office Po Box !");return false; } }
		if( emphouseno ){ if( emphouseno.value == "") { errMessage(emphouseno, "Enter Employee/office No. !");return false; } }
		if( empstreetname ){ if( empstreetname.value == "") { errMessage(empstreetname, "Enter Employee/Office Street Name !");return false; } }
		if( empcity ){ if( empcity.value == "") { errMessage(empcity, "Enter Employee/Office City Name !");return false; } }
		if( empdistrict ){ if( empdistrict.value == "") { errMessage(empdistrict, "Enter Employee/Office District Name !");return false; } }
		if( empphone1 ){ if( empphone1.value == "") { errMessage( empphone1, "Enter Employee/Office Phone No. !");return false; } }
		if( empemail ){ if( empemail.value == "") { errMessage(empemail, "Enter Employee/Office E-Mail Id. !");return false; } }
		return true;
		
	}
	function showCompany( workfor ){ 
		var trcomp = document.getElementById("trcomp");
		if( workfor == "$PRIV" ){
			trcomp.style.display = "table-row"; 
		}else{
			trcomp.style.display = "none";
		}
	}
	
	function selectTab(){ 
		var jscurrenttab  = document.getElementById("jscurrenttab").value;   
		document.getElementById( jscurrenttab ).removeAttribute('disabled'); 
		
	 	 var reglevel =document.getElementById("reglevel").value;  
		 document.getElementById( jscurrenttab ).removeAttribute('disabled');
	  	 
		 for ( var i=reglevel; 1 <= i+1 ; i-- ){ 
			var prevtab = document.getElementById(i);
			prevtab.removeAttribute('disabled');
		 }   
	}
	
	function validatetab1()
	{
		var branchcode = document.getElementById("branchcode");
		if( branchcode ){ if( branchcode.value == "") { 
			errMessage(branchcode, "Select Branch Code !");
			document.getElementById("branchcode").focus;
			return false; 
		} 
		}
		
		var embname = document.getElementById("embname")
		if( embname ){ 
			if( embname.value == "") { 
				errMessage(embname, "Enter Embosing Name !");
				document.getElementById("embname").focus();
				return false; 
			}else if(embname.value.length > 25){
				errMessage(embname, "Embosing Name should contains 25 letters only !");
				document.getElementById("embname").focus();
				return false; 
			} 
		}
		//alert(embname.value.length);
		var encname = document.getElementById("encname");
		if( encname ){
			if( encname.value == "") 
			{ 
				errMessage(encname, "Enter Encoding Name !");
				document.getElementById("encname").focus();
				return false; 
			}else if(encname.value.length > 25){
				errMessage(encname, "Encoding Name should contains 25 letters only !");
				document.getElementById("encname").focus();
				return false; 
			} 
		}
		
		var productcode = document.getElementById("productcode");
		if( productcode )
		{ 
			if( productcode.value == "" || productcode.value == "-1") 
			{ 
				errMessage(productcode, "Select Product Code !");
				document.getElementById("productcode").focus();
				return false; 
			} 
			
		}
		
		var subproduct = document.getElementById("subproduct");
		if( subproduct )
		{ 
			if( subproduct.value == ""  || subproduct.value == "-1") 
			{ 
				errMessage(subproduct, "Select Sub Product Code !");
				document.getElementById("subproduct").focus();
				return false; 
				} 
		}
		
		/* var cinidbasedon = document.getElementById("cinidbasedon");
		var custidlength = document.getElementById("custidlength");
		var customeridno = document.getElementById("customeridno"); */
		
		//alert('cinidbasedon'+cinidbasedon.value);
		//alert('custidlength'+custidlength.value);
		//alert('customeridno'+customeridno.value);
		
		/* if( cinidbasedon ){ if( cinidbasedon.value == "CBS")
			{
			
				if( customeridno ){ if( customeridno.value == ""  || customeridno.value == "-1") { errMessage(customeridno, "Enter Customer Id !");return false; } }
				
				
				if(customeridno.value.length!=custidlength.value)
				{
					//alert(customeridno.value.length+"-----"+custidlength.value);
				errMessage(customeridno, "CustomerId Should Be "+custidlength.value+" Digit!");
				return false;
				}
				
				var url = "checkCustomerIdExistDebitCustomerRegister.do?customerid="+customeridno.value;
				var result = AjaxReturnValue(url);
				//alert(result);
				if(result!='NEW')
					{
					errMessage(customeridno, "Customer Id Alredy Exist Try Different !");
					return false; 
					
					}
				
			}
		} */
		  
		var limitid = document.getElementById("limitid");
		if( limitid ){ if( limitid.value == ""  || limitid.value == "-1") { errMessage(limitid, "Select Limit !");return false; } }
		
		var feecode = document.getElementById("feecode");
		if( feecode ){ if( feecode.value == ""  || feecode.value == "-1") { errMessage(feecode, "Select Fee  !");return false; } }
	
		var accounttypevalue = document.getElementById("accounttypevalue");
		if( accounttypevalue ){ if( accounttypevalue.value == ""  || accounttypevalue.value == "-1") { errMessage(accounttypevalue, "Select accounttypevalue !");return false; } }
		var accountsubtypevalue = document.getElementById("accountsubtypevalue");
		if( accountsubtypevalue ){ if( accountsubtypevalue.value == ""  || accountsubtypevalue.value == "-1") { errMessage(accountsubtypevalue, "Selct accountsubtypevalue !");return false; } }
		var tab2_currency = document.getElementById("tab2_currency");
		if( tab2_currency ){ if( tab2_currency.value == ""  || tab2_currency.value == "-1") { errMessage(tab2_currency, "Select Currency !");return false; } }
		 
		
		//tab2
		
		var firstname = document.getElementById("firstname");
		if( firstname ){ if( firstname.value == ""  || firstname.value == "-1") { errMessage(firstname, "Enter firstname !");return false; } }
		var middlename = document.getElementById("middlename");
		/*if( middlename ){ if( middlename.value == ""  || middlename.value == "-1") { errMessage(middlename, "Select middlename !");return false; } }*/
		var lastname = document.getElementById("lastname");
		/*if( lastname ){ if( lastname.value == ""  || lastname.value == "-1") { errMessage(lastname, "Enter lastname !");return false; } }*/
		/* var dob = document.getElementById("dob");
		if( dob ){ if( dob.value == ""  || dob.value == "-1") { errMessage(dob, "Select Date of Birth !");return false; } } */
		var gender = document.getElementById("gender");
		if( gender ){ if( gender.value == "") { errMessage(gender, "Enter Gender !");return false; }
		else if(gender.value.length > 1){errMessage(gender, "Gender should be one letter, its like M or F");return false;}}
		//var mstatus = document.getElementById("mstatus");
		//if( mstatus ){ if( mstatus.value == ""  || mstatus.value == "-1") { errMessage(mstatus, "Select merital Status !");return false; } }
		var nationality = document.getElementById("nationality");
		if( nationality ){ if( nationality.value == ""  || nationality.value == "-1") { errMessage(nationality, "Enter Nationality !");return false; } }
				
		var mobile = document.getElementById("mobile");
		if( mobile ){ if( mobile.value == ""  || mobile.value == "-1") { errMessage(mobile, "Enter Mobile no !"); return false; }
		else if(mobile.value.length > 11){ errMessage(mobile, "Mobile no length should be 10 !"); return false; } }
		var email = document.getElementById("email");
		
		if( email.value == ""  || email.value == "-1") { errMessage(email, "Enter Email id !");return false; } 
		else
		 {
			//alert('test valid');
			 if( !emailvalidator(email.value) )
			 {
			 	errMessage(email,"InValid E-Mail Address");
			 	return false;
			 }
		 }
		
		
		var p_poxbox = document.getElementById("p_poxbox");
		if( p_poxbox ){ if( p_poxbox.value == ""  || p_poxbox.value == "-1") { errMessage(p_poxbox, "Enter Address1 !");return false; } }
		var p_houseno = document.getElementById("p_houseno");
		if( p_houseno ){ if( p_houseno.value == ""  || p_houseno.value == "-1") { errMessage(p_houseno, "Enter Address2 !");return false; } }
		
		//tab4
		var collectbranch = document.getElementById("collectbranch");
		if( collectbranch ){ if( collectbranch.value == "") { errMessage(collectbranch, "Select Collection Branch !");return false; } }
		
		var ensure = document.getElementById("ensure");
		//alert("ensure::::"+ensure);
		if(ensure.checked==false)
			{
			errMessage(p_houseno, "Ensure All Data are Verified !");return false; 
			}
		
	}

	function validatetab2_customer()
	{
		//alert(document.getElementById("accountnovalue").value);
		var firstname = document.getElementById("firstname");
		if( firstname ){ if( firstname.value == ""  || firstname.value == "-1") { errMessage(firstname, "Enter firstname !");return false; } }
		var middlename = document.getElementById("middlename");
		/*if( middlename ){ if( middlename.value == ""  || middlename.value == "-1") { errMessage(middlename, "Select middlename !");return false; } }*/
		var lastname = document.getElementById("lastname");
		/*if( lastname ){ if( lastname.value == ""  || lastname.value == "-1") { errMessage(lastname, "Enter lastname !");return false; } }*/
		/* var dob = document.getElementById("dob");
		if( dob ){ if( dob.value == ""  || dob.value == "-1") { errMessage(dob, "Select Date of Birth !");return false; } } */
		var gender = document.getElementById("gender");
		if( gender ){ if( gender.value == ""  || gender.value == "-1") { errMessage(gender, "Select Gender !");return false; } }
		//var mstatus = document.getElementById("mstatus");
		//if( mstatus ){ if( mstatus.value == ""  || mstatus.value == "-1") { errMessage(mstatus, "Select merital Status !");return false; } }
		var nationality = document.getElementById("nationality");
		if( nationality ){ if( nationality.value == ""  || nationality.value == "-1") { errMessage(nationality, "Enter Nationality !");return false; } }
		var documentprovided = document.getElementById("documentprovided");
		if( documentprovided ){ if( documentprovided.value == ""  || documentprovided.value == "-1") { errMessage(documentprovided, "Select documentprovided !");return false; } }
		var documentnumber = document.getElementById("documentnumber");
		if( documentnumber ){ if( documentnumber.value == ""  || documentnumber.value == "-1") { errMessage(documentnumber, "Enter document number  !");return false; } }
		/* var spousename = document.getElementById("spousename");
		//alert(mstatus.value);
		if(mstatus.value=='M'){
		if( spousename ){ if( spousename.value == ""  || spousename.value == "-1") { errMessage(spousename, "Enter spousename !");return false; } }
		} */
		var mothername = document.getElementById("mothername");
		if( mothername ){ if( mothername.value == ""  || mothername.value == "-1") { errMessage(mothername, "Enter mothername !");return false; } }
		var fathername = document.getElementById("fathername");
			
		if( fathername ){ if( fathername.value == ""  || fathername.value == "-1") { errMessage(fathername, "Enter fathername !");return false; } }
	
		
		//tab3
		
		var mobile = document.getElementById("mobile");
		if( mobile ){ if( mobile.value == ""  || mobile.value == "-1") { errMessage(mobile, "Enter Mobile no !"); return false; } }
		var email = document.getElementById("email");
		
		if( email.value == ""  || email.value == "-1") { errMessage(email, "Enter Email id !");return false; } 
		else
		 {
			//alert('test valid');
			 if( !emailvalidator(email.value) )
			 {
			 	errMessage(email,"InValid E-Mail Address");
			 	return false;
			 }
		 }
		
		
		var p_city = document.getElementById("p_city");
		if( p_city ){ if( p_city.value == ""  || p_city.value == "-1") { errMessage(p_city, "Enter City !");return false; } }
		var p_district = document.getElementById("p_district");
		if( p_district ){ if( p_district.value == ""  || p_district.value == "-1") { errMessage(p_district, "Enter District !");return false; } }
		
		//tab4
		
		var ensure = document.getElementById("ensure");
		//alert("ensure::::"+ensure);
		if(ensure.checked==false)
			{
			errMessage(p_district, "Ensure All Data are Verified !");return false; 
			}
	
	
	
	}
	function tab3_validation(){
	var mobile = document.getElementById("mobile");
	if( mobile ){ if( mobile.value == ""  || mobile.value == "-1") { errMessage(mobile, "Enter Mobile no !"); return false; } }
	var email = document.getElementById("email");
	
	if( email.value == ""  || email.value == "-1") { errMessage(email, "Enter Email id !");return false; } 
	else
	 {
		//alert('test valid');
		 if( !emailvalidator(email.value) )
		 {
		 	errMessage(email,"InValid E-Mail Address");
		 	return false;
		 }
	 }
	
	
	var p_city = document.getElementById("p_city");
	if( p_city ){ if( p_city.value == ""  || p_city.value == "-1") { errMessage(p_city, "Enter City !");return false; } }
	var p_district = document.getElementById("p_district");
	if( p_district ){ if( p_district.value == ""  || p_district.value == "-1") { errMessage(p_district, "Enter District !");return false; } }
	
	
		
	
	}
	
	function tab4_validation()
	{
		var ensure = document.getElementById("ensure");
		//alert("ensure::::"+ensure);
		if(ensure.checked==false)
			{
			errMessage(p_district, "Ensure All Data are Verified !");return false; 
			}
	}
	
	function sameparam()
	{
		var sameasperm = document.getElementById("sameasperm");
		if( sameasperm.checked ){
		alert('yes');
		}
	}
	
</script>

<script type="text/javascript">
var rowNum = 0;
function addRow(frm) {
	var accttypelist = getAcctTypeList();
	alert("addrow::"+accttypelist);
	
	var table = document.getElementById("AccountInformation");
	
	//table.style.overflow = "scroll";
	
	
	
	
	//document.getElementById("button").style.overflow = "scroll";
	
	var rowCount = table.rows.length;
	alert(rowCount);
	var row = table.insertRow(rowCount);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
   
    alert("<select>"+accttypelist+"</select>");
    
    cell1.innerHTML = "Account Type : <select id='accounttypevalue_"+rowCount+"' name='accounttypevalue'>"+accttypelist+"</select>";
    cell2.innerHTML = "Primary Account Number : <input type='text' name = 'accountnovalue' id='accountnovalue_"+rowCount+"' value=''/>";
}

function removeRow(rnum) {
	var allowedaccount = document.getElementById("allowedaccount").value;
	var  acctcnt = document.getElementsByName("acctno").length;
	 
	if( acctcnt+1 != allowedaccount ){
		errMessage(document.getElementById("acctno0"), "");
	}
	
	jQuery('#rowNum'+rnum).remove();
}

function getProductList( branchcode ){  
	var url = "getProductListByBranchDebitCustomerRegister.do?branchcode="+branchcode; 
	var result = AjaxReturnValue(url);
	//alert('result'+result);
	document.getElementById("productcode").innerHTML=result;
}


function getSubProd(selprodid ){   
		var url = "getSubProductListByProductDebitCustomerRegister.do?prodid="+selprodid+"&status=1";   
		//alert(url);
		result = AjaxReturnValue(url);   
		//alert(result);
		document.getElementById("subproduct").innerHTML = result;  
		
		//var hidsubproduct = document.getElementById("hidsubproduct").value; 
		//document.getElementById("subproduct").value=hidsubproduct;
		//getLimitBySubProduct(hidsubproduct);
		//getFeeBySubProduct(hidsubproduct);
		   
} 

function getAcctTypeList(){ 
	var url = "getAccutSubTypeListDebitCustomerRegister.do?";   
	 
	result = AjaxReturnValue(url);
	alert("getAcctTypeList"+result);
	document.getElementById("accttype0").innerHTML=result;
	
	
	return result;  
}

function getAcctSubTypeList(){ 
	
	var accounttypeid =  document.getElementById("accounttypevalue").value;
	var url = "getAccutSubTypeListDebitCustomerRegister.do?accounttypeid="+accounttypeid;   
	result = AjaxReturnValue(url);
	//alert("getAcctsubTypeList"+result);
	document.getElementById("accountsubtypevalue").innerHTML=result;
	return result;  
}


function getLimitBySubProduct( subproductid ){
	//alert('getLimitBySubProduct'+subproductid);
	var  productid = document.getElementById("productcode");
	//alert('getting productid'+ productid);
	if( productcode.value == "-1"){
		errMessage(productcode, "Select Product code");
		return false;
	} 
	var url = "getLimitBySubProductDebitCustomerRegister.do?productid="+productid.value+"&subproductid="+subproductid;
	//alert(url); 
	var result = AjaxReturnValue(url);
	document.getElementById("limitid").innerHTML=result;
	
	
	
	
}

function getFeeBySubProduct( subproductid ){
	//alert('getFeeBySubProduct'+subproductid);
	var  productid = document.getElementById("productcode");
	if( productcode.value == "-1"){
		errMessage(productcode, "Select Product code");
		return false;
	} 
	var url = "getFeeBySubProductDebitCustomerRegister.do?PRODUCT="+productid.value+"&SUBPRODUCT="+subproductid;
	 
	var result = AjaxReturnValue(url);
	document.getElementById("feecode").innerHTML=result;
	
}




function chkChars(field,id,enteredchar)
{
	//alert('1:'+field+'2:'+id+'3:'+enteredchar);
	charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
    	//alert(document.getElementById(id).value.charAt(i));   
    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
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

/* function validateDate(selecteddate)
{
	//alert(selecteddate);
	var url = "validateSelectedDateDebitCustomerRegister.do?selecteddate="+selecteddate;
	 
	var result = AjaxReturnValue(url);
	//alert(result);
	
	if(result.trim()!='TRUE')
		{
		errMessage(document.getElementById("dob"), result);
		document.getElementById("dob").value='';
		return false;
		}
	
} */

 
</script>


<title>Customer Registration</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body ><!-- selectTab(); -->

<%-- <sx:div id="default"  name="defaultValue"  value="default"   >
<s:if test ="%{cbscustregbean.applicationid!=null}">
<!-- ORDER NO : <s:property value="%{cbscustregbean.applicationid}" /> -->
</s:if>  
</sx:div> --%>



<s:form action="saveProductInformationCbsCustomerReg.do"  id="regform" name="regform" onsubmit="return showProcessing()" autocomplete = "off" namespace="/">
 

<br><br>	


	<%-- <s:hidden name="cinidbasedon" id="cinidbasedon" value="%{cbscustregbean.cinidbasedon}"/>
	<s:hidden name="custidlength" id="custidlength" value="%{cbscustregbean.cinnoLength}"/> --%>
	<s:hidden name="customerid" id="customerid" value="%{cbscustregbean.customerid}"/> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:5px"  >	
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Product Information
		</th></tr> 
		<tr>
		
	 
	 
			 <td class="txt"> Branch<span class="mand">*</span> </td>
		  <td>
		  
		  	 		
					<select id="branchcode" name="branchcode" onchange="getProductList(this.value)">
				 					
						<option value="" >Select Branch</option>   
						<s:iterator value="cardcollectbranchlist">
							<option value="<s:property value="BRANCH_CODE"/>"><s:property value="BRANCH_NAME"/></option>
						</s:iterator>					
				</select>   
					
					   
			 
			 
			  
		 </td>  
		 
		 <!-- inst -->
		
		 
		 
		 
		 	
			 <td class="txt"> Embossing Name <span class="mand">*</span> </td> 
			 <td> 
			  
				 <s:textfield name="embname" id="embname" maxlength="25" 
				 value="%{cbscustregbean.embname}" 
				 onkeyup="chkChars('Embossing Name',this.id,this.value)" />			 
			 
			 	 
			  </td>
			  
			 <td class="txt"> Encoding Name <span class="mand">*</span> </td> <td> 
			 
			  
				 <s:textfield name="encname" id="encname" maxlength="25"
				 value="%{cbscustregbean.encname}" onkeyup="chkChars('Encoding Name',this.id,this.value)" /> 			  
			 	 
			 </td>  
			 
			 
			 
		</tr>
		<tr> 
		
		  <td class="txt"> Product  <span class="mand">*</span>   </td>   
		  <td> 
		  
				   		    
				<select id="productcode"  name="productcode"  onchange="getSubProd(this.value)"  >
					 <option value="-1" >Select Product</option>
					 <s:iterator value="cbscustregbean.productlist">   
					 				<option value="<s:property value="PRODUCT_CODE"/>"><s:property value="CARD_TYPE_NAME"/></option>
					</s:iterator>   
					
				 
				 </select>
			    							   
			 
		 </td>
			   <td class="txt"> Sub Product   <span class="mand">*</span>  </td>
			    <td>
			      
			    	<select  id="subproduct"  name="subproduct" onchange="getFeeBySubProduct(this.value);getLimitBySubProduct(this.value);" >
			    	 <option value="-1" >Select Sub Product</option>
			    	</select>
			           
	
		 </td>
		<%-- <s:if test ="%{cbscustregbean.cinidbasedon=='CBS'}">
			  <td class="txt"> Enter CustomerID <span class="mand">*</span> </td> <td> 
			 <s:if test ="%{cbscustregbean.applicationid==null}">
			 <s:textfield name="customeridno" id="customeridno" value=""  maxlength="%{cbscustregbean.cinnoLength}" onkeyup="validateNumber('Customer Id ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{cbscustregbean.customeridno}" />
			 </s:else>
			
			 </td>  
			 </s:if>
			    
		</tr>  --%>
		 
		 
			    
			 
		
		
		<!-- 
			 <td class="txt"> Limit Based on    <span class="mand">*</span> </td> 
			 <td> 
			  <s:if test ="%{cbscustregbean.applicationid==null}">
				 <select id="limitbasedon"  
				  name="limitbasedon" > 
				  <option value="CDTP">Card Type</option>
				  <option value="ACTP">Account Type</option>
				  </select>
			 </s:if>1
			 <s:else>
			 <s:property  value="%{cbscustregbean.limitbasedon}" />  
			     <s:if test ="cbscustregbean.limitbasedon=='CDTP'">
			        <s:property value="Card Type"/>  
			     </s:if>
			      <s:if test ="cbscustregbean.limitbasedon=='ACTP'">
			        <s:property value="Account Type"/>  
			     </s:if>
			 </s:else>     
			 </td>   -->
			
			  <td class="txt"> Limit  <span class="mand">*</span>   </td> 
			  
			  <td> 
			  
				  <select name="limitid" id="limitid">
				  <option value = "-1">Select Limit</option>
				   </select>
			   
			   
			   
			  
			     <s:property value="%{cbscustregbean.limitname}" />
			  		  
			  </td> 
			</tr> 
			<tr> 
			    
			   <td class="txt" > Fee <span class="mand">*</span>  </td> 
			   <td > 
			    
				   <select name="feecode" id="feecode" >
				   <option value="-1">Select Fee</option>
				   </select> 
			    
			   
			    
			  
			      <s:property value="%{cbscustregbean.feename}" />
			    
			   </td>
			   
			   <td class="txt" > Renewal Card <span class="mand">*</span>  </td> 
			   <td > 
			    	<input type="radio" name="renewalflag" id="renewalflag" value="Y"> Yes
			    	<input type="radio" name="renewalflag" id="renewalflag" value="N" checked="checked"> No
			   </td>
			   
					<td class="txt">Collection Branch <span class="mand">*</span></td>
					<td>: <select name="collectbranch" id="collectbranch">
								<option value="">-SELECT-</option>
								<s:iterator value="cardcollectbranchlist">
									<option value="${BRANCH_CODE}">${BRANCH_NAME}</option>
								</s:iterator>
						</select>
					</td>
			   
			</tr> 
			
		    
		  <tr><th colspan="6" style="text-align:left" class="tbheader"> Account Information
		 </th>
		 <tr>
		 <td colspan="6" width="100%" >
		 	<table border='1' id="AccountInformation"  style='display:block' >
		 	<tr>
		 		
		 		<td>
		 		<!-- accttypelist -->
		 		Account Type :
		 		 
		 		  
						<select id="accounttypevalue" name="accounttypevalue">					 
						 								
								<s:iterator value="%{cbscustregbean.accttypelist}">
									<option value="<s:property value="ACCTTYPEID"/>"><s:property value="ACCTTYPEDESC"/></option>								 						 								 								
								</s:iterator>
						 
						</select>
				
				 	 
					
		 		</td>
		 		<td>
		 		
		 		<td>
		 		<!-- acctsubtypelist subaccounttypelist -->
		 			 		
		 		Account Description :
		 		 
			 		 
		 		 
		 		
		 		 
		 			<select id="accountsubtypevalue" name="accountsubtypevalue" >				 		
				 		<s:iterator value="%{cbscustregbean.accuntsubtypelist}">		 		
				 		<option value="<s:property value="ACCTSUBTYPEID"/>"><s:property value="ACCTSUBTYPEDESC"/></option>
				 		</s:iterator>
			 		
					</select>
		 		
		 		 
		 		</td>
		 		<td>
		 		
		 		
		 		</td>
		 		<td>Currency:
		 		
		 		  
		 		 
		 			<select id="tab2_currency" name="tab2_currency" onchange="">						 															   
						<s:iterator value="%{cbscustregbean.currencylist}">
							<option value="<s:property value="NUMERIC_CODE"/>"><s:property value="CURRENCY_DESC"/></option>  
									
						</s:iterator>
						 
					</select> 
		 		 
		 		
		 		</td>
		 		<td> Primary Account Number : 
		 		<s:hidden name = "accountnolength" id="accountnolength" value="%{cbscustregbean.accountnolength}"/>
		 		<s:textfield  id="accountnovalue" name="accountnovalue" readonly="true" maxlength="%{cbscustregbean.accountnolength}" value="%{cbscustregbean.accountnovalue}" onkeyup="validateNumber('Account No ',this.id,this.value)" /> 
		 		<!-- <input onclick="addRow(this.form);" type="button" value="Add Account" />  --></td> 
 			</tr>
 			
 			</table>
 			
 			
 			
		 </td>
		 </tr>
		
	</table>	
	


 	<%-- <s:form action="customerDetailsAddDebitCustomerRegister.do"  id="regform1" name="regform1" onsubmit="return showProcessing()" autocomplete = "off" namespace="/">
 		<s:hidden name="applicationid" id="applicationid" value="%{cbscustregbean.applicationid}"/> 
 --%> 		
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:5px"  >	
	<tr><th colspan="8" style="text-align:left" class="tbheader"> Personal Information

	</th>
	</tr>
	<tr> 
	
	
	
	 
		 <td class="txt"> First Name <span class="mand">*</span> </td> 
		 <td> <s:textfield name="firstname" id="firstname" maxlength="30"  value="%{cbscustregbean.firstname}" onkeyup="chkChars('First Name',this.id,this.value)" /> </td>
		 <td class="txt"> Middle Name   </td> 
		 <td> <s:textfield name="middlename" id="middlename" maxlength="32"  value="%{cbscustregbean.middlename}"  onkeyup="chkChars('Middle Name',this.id,this.value)" /> </td>
		 <td class="txt"> Last Name </td> 
		 <td> <s:textfield name="lastname" id="lastname" maxlength="32"   value="%{cbscustregbean.lastname}" onkeyup="chkChars('Last Name',this.id,this.value)"  /> </td>
	
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob"   value="%{cbscustregbean.dob}"  style="width:160px"/>
				
		 </td>
	</tr> 
	
	
	<tr>
		   <td class="txt"> Gender <span class="mand">*</span> </td> <td> 		  
		   <s:textfield name="gender" id="gender"   value="%{cbscustregbean.gender}" maxlength="1" style="width:160px"/>		  		  	
		  </td>
	
		 <td class="txt"> Nationality <span class="mand">*</span>  </td> 
		 <td> <s:textfield name="nationality" id="nationality"  maxlength="50" value="%{cbscustregbean.nationality}" onkeyup="chkChars('Nationality',this.id,this.value)" /> </td>
		 
		 <td class="txt"> Document Number  <span class="mand">*</span>  </td> 
		   <td> <s:textfield name="documentnumber" id="documentnumber" maxlength="32"  value="%{cbscustregbean.documentnumber}" /> </td>
		 
		 <%-- <td class="txt"> Customer Id <span class="mand">*</span>  </td> 
		 <td> <s:textfield name="customerid" id="customerid"  maxlength="20" value="%{cbscustregbean.customerid}" /> </td> --%>
	</tr> 
<%-- 	<tr>
		   <td class="txt"> Document Provided <span class="mand">*</span> </td> <td> 
		  	 		<select id="documentprovided" name="documentprovided" >
					<s:if test ="%{cbscustregbean.documentlist.isEmpty()}">
					<option value="">No Document Type Found</option>
					</s:if>
					<s:else>
								<option value="" >Select Document</option>   
								<s:iterator value="cbscustregbean.documentlist">
								<option value="<s:property value="DOC_ID"/>" 
								<s:if test="DOC_ID==cbscustregbean.documentprovided" >
								selected
								</s:if>
								> 
								<s:property value="DOC_TYPE"/></option>
								</s:iterator>
					</s:else>
					</select> 
		  </td>
	
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> 
		   <td> <s:textfield name="documentnumber" id="documentnumber" maxlength="32"  value="%{cbscustregbean.documentnumber}" onkeyup="chkChars('Document No',this.id,this.value)"/> </td>
	
		<td class="txt"> Spouce Name   </td> <td> 
		<input type="text" name="spousename" id="spousename" maxlength="32" value="<s:property value="%{cbscustregbean.spousename}"/>" onkeyup="chkChars('Spouse Name',this.id,this.value)"  
		<s:if test="M==cbscustregbean.mstatus" >readonly</s:if>
		/> </td>
		
	</tr> 
	<tr>
		 <td class="txt"> Mother's Name <span class="mand">*</span> </td> <td> 
		 <s:textfield name="mothername" id="mothername" maxlength="32" value="%{cbscustregbean.mothername}" onkeyup="chkChars('Mother Name',this.id,this.value)"  /> </td>
		  <td class="txt"> Father Name  </td> 
		  <td> <s:textfield name="fathername" id="fathername" maxlength="32"  value="%{cbscustregbean.fathername}" onkeyup="chkChars('F',this.id,this.value)"  /> </td> 
	</tr>  --%>
	
	</table>	
	
 
 	<%-- <s:form action="contactDetailsAddDebitCustomerRegister.do"  id="regform" onsubmit="return showProcessing()" name="regform" autocomplete = "off" namespace="/"> --%>
 	<%-- <s:hidden name="applicationid" id="applicationid" value="%{cbscustregbean.applicationid}"/> --%> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:5px">	
		<tr><th colspan="8" style="text-align:left" class="tbheader"> Contact Information

		</th><tr> 
		<tr>   
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> 
			 <td> 
			 <s:textfield name="mobile" id="mobile" maxlength="15"   value="%{cbscustregbean.mobile}"  onkeyup="validateNumber('Mobile',this.id,this.value)"/> 
			 </td>
			 <td class="txt"> E-Mail      </td> <td> <s:textfield name="email" id="email"  maxlength="50"   value="%{cbscustregbean.email}" /> </td>		 
		
			<td class="txt"> Address1  <span class="mand">*</span>  </td> 
			<td> 
				<s:textfield name="p_poxbox" id="p_poxbox" maxlength="50" value="%{cbscustregbean.p_poxbox}"/> 
			</td>
		
		 
			<td class="txt"> Address2 <span class="mand">*</span>  </td>
			<td>
				<s:textfield name="p_houseno" id="p_houseno" maxlength="50"  value="%{cbscustregbean.p_houseno}"/>
			</td>			
		</tr>
		
	<%-- 	<tr><th colspan="6" style="text-align:left" class="tbheader"> Permenent Address
			
		</th><tr> 
		<tr>
			 <td class="txt"> Po.Box  <span class="mand">*</span> </td> 
			 <td> <s:textfield name="p_poxbox" id="p_poxbox" maxlength="32" value="%{cbscustregbean.p_poxbox}" onkeyup="validateNumber('Po.Box ',this.id,this.value)"/> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> 
			  <td> <s:textfield name="p_houseno" id="p_houseno" maxlength="32" value="%{cbscustregbean.p_houseno}" onkeyup="validateNumber('House No. ',this.id,this.value)" /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="p_streetname" id="p_streetname" maxlength="32" value="%{cbscustregbean.p_streetname}" onkeyup="chkChars('Street Name',this.id,this.value)" /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="p_wardnumber" id="p_wardnumber" maxlength="32" value="%{cbscustregbean.p_wardnumber}" onkeyup="chkChars('Ward Name ',this.id,this.value)" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> 
			  <td> <s:textfield name="p_city" id="p_city" maxlength="32" value="%{cbscustregbean.p_city}" onkeyup="chkChars('City ',this.id,this.value)" /> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="p_district" id="p_district" maxlength="32" value="%{cbscustregbean.p_district}" onkeyup="chkChars('District ',this.id,this.value)" /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td> <s:textfield name="p_phone1" id="p_phone1" maxlength="10"  value="%{cbscustregbean.p_phone1}" onkeyup="validateNumber('Phone1 ',this.id,this.value)"/> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="p_phone2" id="p_phone2" maxlength="10"  value="%{cbscustregbean.p_phone2}" onkeyup="validateNumber('Phone2 ',this.id,this.value)"/> </td>
			   
		</tr> 
		
		 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> <s:checkbox name="sameasperm" id="sameasperm" onchange="setContactAddress( this.checked )"/> Contact Address </th>
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> <td> <s:textfield name="c_poxbox" id="c_poxbox" maxlength="32" value="%{cbscustregbean.c_poxbox}"  onkeyup="validateNumber('Contact Po.Box no ',this.id,this.value)"  /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td> <s:textfield name="c_houseno" id="c_houseno" maxlength="32" value="%{cbscustregbean.c_houseno}"  onkeyup="validateNumber('Contact House No ',this.id,this.value)"/> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="c_streetname" id="c_streetname" maxlength="32" value="%{cbscustregbean.c_streetname}" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="c_wardnumber" id="c_wardnumber" maxlength="32" value="%{cbscustregbean.c_wardnumber}" onkeyup="chkChars('Contact Ward Name',this.id,this.value)" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:textfield name="c_city" id="c_city" maxlength="32" value="%{cbscustregbean.c_city}" onkeyup="chkChars('Contact City',this.id,this.value)" /> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td> <s:textfield name="c_district" id="c_district" maxlength="32" value="%{cbscustregbean.c_district}" onkeyup="chkChars('Contact District',this.id,this.value)" /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> 
			 <td> <s:textfield name="c_phone1" id="c_phone1" maxlength="16"  value="%{cbscustregbean.c_phone1}" onkeyup="validateNumber('Contact Phone1 ',this.id,this.value)" /> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="c_phone2" id="c_phone2" maxlength="16" value="%{cbscustregbean.c_phone2}" onkeyup="validateNumber('Contact Phone2 ',this.id,this.value)" /> </td>
			   
		</tr>  --%>
		
		 
		
		 
		
	</table>	 
	<table border="0" cellpadding="10" cellspacing="0" width="40%" class="formtable">	 
<tr>
	<td>  <input type="checkbox" id = "ensure" name="ensure" > </td>
	<td  class="tbheader" style="text-align:left">  Ensure All are verified....  </td>
</tr>
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="SUBMIT"   name="fourthtab" id="fourthtab" onclick="return validatetab1();"/>
		</td>
		
		</tr>
</table>
  
 
 

 
<%-- <s:form action="submitcardOrderDebitCustomerRegister.do"  id="regform" onsubmit="return tab4_validation()" name="regform" autocomplete = "off" namespace="/"> --%>

<%-- <input type="hidden" value="<s:property value="%{cbscustregbean.applicationid}"/>" id="tab4_applicationid" name="tab4_applicationid" /> --%> 






 


  

<!-- --------------------- SUPPLIMENTARY DETAILS----------- -->

<%-- 
<sx:div id="4" name="tab"  label="Supplimentary Card Details" value="6"   disabled="true" >
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="supplimentform" onsubmit="return validateSuplimentDet()" name="supplimentform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Supplimentary Details
		<input type="hidden" id="reqfrom" name="reqfrom" value="6" /> 
		<input type="hidden" id="gotoval" name="gotoval" value="6" />
		<s:hidden name="applicationid" id="applicationid" value="%{cbscustregbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{cbscustregbean.customerid}"/>
	</th><tr> 
	<tr>
		 <td class="txt"> First Name <span class="mand">*</span> </td> <td> <s:textfield name="supfirstname" id="supfirstname" maxlength="32"  /> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="supmidname" id="supmidname" maxlength="32"/> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="suplastname" id="suplastname" maxlength="32"/> </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="supdob" id="supdob" value="%{custregbean.supdob}" readonly="true"   style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.supplimentform.supdob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		  <td class="txt"> Nationality <span class="mand">*</span>  </td> <td> <s:textfield name="supnationality" id="supnationality" maxlength="32"/> </td>
		   <td class="txt"> Name On Card <span class="mand">*</span> </td> <td> <s:textfield name="supnameoncard" id="supnameoncard" maxlength="32"/> </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Document Provided <span class="mand">*</span> </td> <td>
		 <s:select list="%{cbscustregbean.documentlist}"  id="idproof"  name="idproof"  listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" />  
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  /> </td>
	</tr>  
	
	

	<tr>
		 <td class="txt"> Gender <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="supgender" onchange = "getSupRelation(this.value)"  headerKey="-1" headerValue="-SELECT-" name="supgender" value="%{custregbean.gender}"/> </td>
		  <td class="txt"> Relationship <span class="mand">*</span> </td> <td> <s:select  list="%{cbscustregbean.supplimentrelationlist}" id="suprelationship" listKey="RELATIONCODE" listValue="RELATIONDESC" name="suprelationship"  headerKey="-1" headerValue="-SELECT-" /> </td>
		   
	</tr> 
	 
	 <tr >
	 <td colspan="6" style="text-align:center">
	 		<s:submit value="Add"   name="suplimentadd" id="sixtab" onclick="return customerinfo_checker()"/>
	 </td>
	 </tr>
	 
	
	 <tr>
	 	<td colspan="6">
	 		<table border='0' cellpadding='0' cellspacing='0' width='100%'>
	 		 <s:if test="cbscustregbean.hassupliment"> 
	 			<tr><th>First Name</th><th>Middle Name</th><th>Last Name</th><th>Name on Name</th><th> Gender </th><th>DOB</th> 
	 			<th>Nationality</th> <th>Citizenship / Passport </th>  <th>Document Number </th>  <th> Relationship </th>   <th> Delete </th> </tr>
			 </s:if>		
	 		<s:iterator value="cbscustregbean.supplimentlist">
	 			<tr><td>${FIRSTNAME}</td><td>${MIDDLENAME}</td><td>${LASTNAME}</td><td>${ENCNAME}</td><td> ${GENDER} </td><td>${DOB}</td> 
	 			<td>${NATIONALITY}</td> <td>${IDPROOF} </td>  <td>${IDPROOFNO} </td>  <td> ${IDPROOF} </td> 
	 			<td>  <img src="images/delete.png" onclick="deleteSuppliment( '${CUSTOMERID}' )" />   </td> </tr>
	 			
	 		</s:iterator>
	 		</table>	 		
	 	</td>
	 </tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr><td>&nbsp;</td></tr>
		<tr>
		<td>
			<input type="button"   value="Complete"     name="sixtab" id="sixtab" onclick="return confirmComplete()"/>
		</td>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div>
 --%>
</s:form>



	
	
 
 
</body>