<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
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
	
	function validatetab1_Registration()
	{
	
	var productcode = document.getElementById("productcode");
		if( productcode ){ if( productcode.value == "" || productcode.value == "-1") { errMessage(productcode, "Select Product Code !");return false; } }
		
		 if ( ProductDescription .value == "" )
				    {	
						 errMessage(ProductDescription ,"Product Description  CANNOT BE EMPTY" );
				    	 document.getElementById("ProductDescription ").focus();
				    	 return false;
				   		 
					}
								
		if ( cardholdernumber .value == "" )
				    {	
						 errMessage(cardholdernumber ,"Card Holder Number  CANNOT BE EMPTY" );
				    	 document.getElementById("cardholdernumber ").focus();
				    	 return false;
				   		 
					}
		
		var typeofcard = document.getElementById("typeofcard");
		if( typeofcard ){ if( typeofcard.value == "" || typeofcard.value == "-1") { errMessage(typeofcard, "Select Type Of Card  !");return false; } }
		
		var subtype8digit = document.getElementById("subtype(8 digit)");
		if( subtype8digit ){ if( subtype8digit.value == "") { errMessage(subtype8digit, "Select Sub Type(8-digit)  !");return false; } }
		
		var RegistrationDate  = document.getElementById("Registration Date ")
		if( RegistrationDate  ){ if( RegistrationDate .value == "") { errMessage(RegistrationDate , "Enter Registration Date  !");return false; } }
		
		var Services = document.getElementById("Services");
		if( Services ){ if( Services.value == "" || Services.value == "-1") { errMessage(Services, "Select Services !");return false; } }
	
		var Limit BasedOn  = document.getElementById("Limit Based-On ")
		if( LimitBased-On  ){ if( LimitBased-On .value == "") { errMessage(LimitBased-On , "Select Limit Based-On  !");return false; } }
		
		var Application Date  = document.getElementById("Application Date ")
		if( ApplicationDate  ){ if( ApplicationDate .value == "") { errMessage(ApplicationDate , "Enter Application Date  !");return false; } }
		
		var Expiry Date  = document.getElementById("Expiry Date ")
		if( ExpiryDate  ){ if( ExpiryDate .value == "") { errMessage(ExpiryDate , "Enter Expiry Date  !");return false; } }
		
		var WDraw Amt  = document.getElementById("W.Draw Amt")
		if( W.DrawAmt  ){ if( W.DrawAmt .value == "") { errMessage(W.DrawAmt , "Enter W.Draw Amt  !");return false; } }
		
		var WDraw Cnt  = document.getElementById("W.Draw Cnt")
		if( W.DrawCnt ){ if( W.DrawCnt.value == "") { errMessage(W.DrawCnt , "Enter W.Draw Cnt  !");return false; } }
		
		var PurcLmt Amt  = document.getElementById("Purc.Lmt Amt")
		if( PurcLmtAmt ){ if( Purc.LmtAmt .value == "") { errMessage(Purc.LmtAmt, "Enter  Purc.Lmt Amt !");return false; } }
		
		var  PurcLmt Cnt = document.getElementById("Purc.Lmt Cnt")
		if( PurcLmtCnt ){ if( Purc.LmtCnt.value == "") { errMessage( Purc.LmtCnt , "Enter Purc.Lmt Cnt  !");return false; } }
		
		var TranLmtAmt   = document.getElementById("Tran Lmt Amt ")
		if(  TranLmtAmt  ){ if( TranLmtAmt .value == "") { errMessage( TranLmtAmt , "Enter Tran Lmt Amt  !");return false; } }
		
		var TranLmtCnt  = document.getElementById(" Tran Lmt Cnt ")
		if( TranLmtCnt  ){ if( TranLmtCnt  .value == "") { errMessage(TranLmtCnt , "Enter Tran Lmt Cnt  !");return false; } }
	
		return true;
		
	}
	
	function validatetab2_accountlevel()
	{
	var branchname = document.getElementById("branchname");
		if( branchname ){ if( branchname.value == "") { errMessage(branchname, "Select Branch Name !");return false; } }
		
		var Prod = document.getElementById("Prod.");
		if( Prod){ if( Prod.value == "") { errMessage(Prod, "Select Prod. !");return false; } }
		
		var accounttype = document.getElementById("accounttype");
		if( accounttype ){ if( accounttype.value == ""  || accounttype.value == "-1") { errMessage(accounttype, "Select accounttype !");return false; } }
		
		var accountnovalue = document.getElementById("accountnovalue");
		var accountnolength = document.getElementById("accountnolength");	
		//alert('accountno value -'+accountnovalue.value.length+":::::"+accountnolength.value);
		
		if(accountnovalue.value.length!=accountnolength.value)
			{
			errMessage(accountnovalue, "Account No Should be "+accountnolength.value+" Digit!");
			return false;
			}
		
		var url = "checkAccountNoExistDebitCustomerRegister.do?accountnovalue="+accountnovalue.value;
		var result = AjaxReturnValue(url);
		//alert(result);
		if(result!='NEW')
			{
			errMessage(accountnovalue, "Account No Alredy Exist Try Differant !");
			return false; 
			
			}
		
		var Currency  = document.getElementById("Currency ");
		if( Currency  ){ if( Currency .value == ""  || Currency .value == "-1") { errMessage(Currency , "Select Currency  !");return false; } }
		
		var ACID  = document.getElementById("A/C ID ");
		if( ACID  ){ if( ACID .value == ""  || ACID .value == "-1") { errMessage(ACID , "Enter A/C ID  !");return false; } }
		
		var ACLmt Type  = document.getElementById("A/C Lmt Type ");
		if( ACLmtType  ){ if( ACLmtType .value == ""  || ACLmtType .value == "-1") { errMessage(ACLmtType , "Enter A/C Lmt Type  !");return false; } }
		
		var WDL   = document.getElementById("WDL  ");
		if( WDL  ){ if( WDL  .value == ""  || WDL .value == "-1") { errMessage(WDL , "Enter WDL  !");return false; } }
		
		
		return true;
		
	}
	
		
		function validatetab3_customer()
	{
		var cinidbasedon = document.getElementById("cinidbasedon");
		var custidlength = document.getElementById("custidlength");
		var customeridno = document.getElementById("customeridno");
		
		if( cinidbasedon ){ if( cinidbasedon.value == "CBS")
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
		}
		
		
		var gender = document.getElementById("gender");
	if( gender ){ if( gender.value == ""  || gender.value == "-1") { errMessage(gender, "Select Gender !");return false; } }
		
		var embname = document.getElementById("embname")
		if( embname ){ if( embname.value == "") { errMessage(embname, "Enter Embosing Name !");return false; } }
		
		var encname = document.getElementById("encname");
		if( encname ){ if( encname.value == "") { errMessage(encname, "Enter Encoding Name !");return false; } }
		
		var annualincome = document.getElementById("annualincome");
		if( annualincome ){ if( annualincome.value == "") { errMessage(annualincome, "Enter Annual Income!");return false; } }
		
		var dateofbirth = document.getElementById("dateofbirth");
	if( dateofbirth ){ if( dateofbirth.value == ""  || dateofbirth.value == "-1") { errMessage(dateofbirth, "Select Date of Birth !");return false; } }
	
	var spousebirthdate = document.getElementById("spousebirthdate");
	var spouseannvdate = document.getElementById("spouseannvdate");
	var spousename = document.getElementById("spousename");
		
		function ismarried()
	{
		var mstatus= document.getElementById("mstatus");
		var spousename = document.getElementById("spousename");
		var spouseannvdate = document.getElementById("spouseannvdate");
		var spousebirthdate = document.getElementById("spousebirthdate");
		if(mstatus.value!='M'){
			spousename.readOnly = true;
			spousename.value="";
		}else
		{
			
			spousename.readOnly = false;
		}	
	}

	}
		function validatetab4_contact()
	{
		
		var residenceaddress = document.getElementById("residenceaddress");
		if( residenceaddress ){ if( resideceaddress.value == "") { errMessage(residenceaddress, "Enter Residence Address!");return false; } }
		
		var mailingaddress = document.getElementById("mailingaddress");
		if( mailingaddress ){ if( mailingaddress.value == "") { errMessage(mailingaddress, "Enter Mailing Address!");return false; } }
		
		var officeaddress = document.getElementById("officeaddress");
		if( officeaddress ){ if( officeaddress.value == "") { errMessage(officeaddress, "Enter Office Address!");return false; } }
		
		var mobileno = document.getElementById("mobileno");
	if( mobileno ){ if( mobileno.value == ""  || mobileno.value == "-1") { errMessage(mobileno, "Enter Mobile no !"); return false; } }
	
	var OffEMail = document.getElementById("Off.E-Mail");
	
	if( OffE-Mail.value == ""  || Off.E-Mail.value == "-1") { errMessage(email, "Enter Off.E-Mail !");return false; } 
	else
	 {
		//alert('test valid');
		 if( !Off.E-Mailvalidator(Off.E-Mail.value) )
		 {
		 	errMessage(email,"InValid Off.E-Mail Address");
		 	return false;
		 }
	 }
		
		var OffPhone = document.getElementById("Off.Phone");
		if( Off.Phone ){ if( Off.Phone.value == "") { errMessage(Off.Phone, "Enter Off.Phone!");return false; } }
		
		var OffPhone2  = document.getElementById("Off.Phone2 ");
		if( Off.Phone2  ){ if( Off.Phone2 .value == "") { errMessage(Off.Phone2 , "Enter Off.Phone2 !");return false; } }
		
		var OffFax = document.getElementById("Off.Fax");
		if( Off.Fax ){ if( Off.Fax.value == "") { errMessage(Off.Fax, "Enter Off.Fax!");return false; } }
		
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
	//alert(rowCount);
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

function getCurrency (subproductid){
	
	var  subproducid = document.getElementById("subproduct");
	if( subproduct.value == "-1"){
		errMessage(productcode, "Select Currency code");
		return false;
	} 
	var url = "getCurrencyconfigDebitCustomerRegister.do?SUBPRODUCTVAL="+subproducid.value+"&SUBPRODUCT="+subproductid;
	 
	var result = AjaxReturnValue(url);
	document.getElementById("tab2_currency").innerHTML=result;
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

function validateDate(selecteddate)
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
	
}

 
</script>


<title>Registration Of Cards</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body ><!-- selectTab(); -->

<sx:div id="default"  name="defaultValue"  value="default"   >
<s:if test ="%{dbtcustregbean.applicationid!=null}">
<!-- ORDER NO : <s:property value="%{dbtcustregbean.applicationid}" /> -->
</s:if>  
</sx:div>


<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 100%; margin:0 auto; height: 350px;" doLayout="true" 

selectedTab="%{dbtcustregbean.reglevel}" 

 >
<sx:div id="1"  name="tab" label="Registration Information" value="1"  disabled="%{dbtcustregbean.tab1Status}" >
	<s:form action="saveProductInformationDebitCustomerRegister.do"  id="regform" name="regform" onsubmit="return showProcessing()" autocomplete = "off" namespace="/">
	<s:hidden name="cinidbasedon" id="cinidbasedon" value="%{dbtcustregbean.cinidbasedon}"/>
	<s:hidden name="custidlength" id="custidlength" value="%{dbtcustregbean.cinnoLength}"/>
	
	
	
	
	<table border="0" cellpadding="0" cellspacing="5" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	
						
		<tr>
		
	
	<s:if test="%{#session.USERTYPE =='INSTADMIN'}">
		<!-- inst -->
			 <td class="txt"> Product Code<span class="mand">*</span> </td>
		  <td>
			 <s:if test ="%{dbtcustregbean.applicationid==null}">
			 		<select id="productcode" name="productcode" onchange="getproductList(this.value)">
					<s:if test ="%{dbtcustregbean.productlist.isEmpty()}">
					<option value="">No Product Found</option>
					</s:if>
					<s:else>
								<option value="" >Select Product</option>   
								<s:iterator value="dbtcustregbean.productlist">
								<option value="<s:property value="PRODUCT_CODE"/>"><s:property value="PRODUCT_NAME"/></option>
								</s:iterator>
					</s:else>
					</select> 
			</s:if>   
			<s:if test ="%{dbtcustregbean.applicationid!=null}">		
					<s:iterator value="dbtcustregbean.productlist">   
					<!--<s:property value="PRODUCT_CODE" /> == <s:property value="%{dbtcustregbean.productcode}"/> <s:property value="PRODUCT_NAME"/> <br>-->
					  
					 <s:if test="PRODUCT_CODE==dbtcustregbean.branchcode" >
					 					 <s:property value="PRODUCT_NAME"/>   
					 </s:if>  
					</s:iterator>   
					
					   
			</s:if>   
		 </td>  
		 
		 <!-- inst -->
		
		</s:if>
		<s:else>
			
			<td class="txt"> Product</td>
			<td>  <s:property value="%{#session.PRODUCT_DESC}"/></td>
				<s:hidden name="productcode" id="productcode" value="%{#session.PRODUCTCODE}" />
			
	</s:else>
		 
		 
			 <td class="txt"> Product Description <span class="mand">*</span> </td> 
			 <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="productdescription" id="productdescription" maxlength="25" 
			 value="%{dbtcustregbean.productdescription}" 
			 onkeyup="chkChars('Product Description',this.id,this.value)" />
			 </s:if>
			 <s:else>
			 <s:property 
			 value="%{dbtcustregbean.productdescription}" 
			 />
			 
			 </s:else>
			  </td>
			 <td class="txt"> Card Holder Number <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="cardholdernumber" id="cardholdernumber" maxlength="19" 
			 value="%{dbtcustregbean.cardholdernumber}" onkeyup="chkChars('Card Holder Number',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.cardholdernumber}" />
			 </s:else>
			
			 </td>  
			 
			 
			 
			 
		</tr>
		<tr>
		  <td class="txt"> Type Of Card  <span class="mand">*</span>   </td>   
		  <td> 
		  
		    <s:if test ="%{dbtcustregbean.applicationid==null}">
				 <select id="typeofcard"  name="typeofcard"  onchange="gettypeofcard(this.value)"  >
				 <option value="-1" >Select Type Of Card </option>
				<s:if test="%{#session.USERTYPE !='INSTADMIN'}"> 
				 <s:iterator value="dbtcustregbean.typeofcardlist">   
				 				<option value="<s:property value="Type Of Card"/>"><s:property value="Type Of Card"/></option>
				</s:iterator>   
				</s:if>	
				 
				 </select>
			</s:if>   
			
			
			<s:if test ="%{dbtcustregbean.applicationid!=null}">		
					<s:iterator value="dbtcustregbean.typeofcardlist">   
					<!--<s:property value="BRANCH_CODE" /> == <s:property value="%{dbtcustregbean.branchcode}"/> <s:property value="BRANCH_NAME"/> <br>-->
					  
					 <s:if test="TYPE_OF_CARD==dbtcustregbean.productcode" >
					 					 <s:property value="TYPE_OF_CARD"/>   
					 </s:if>  
					</s:iterator>   
					
					   
			</s:if>   
			       
			 
		 </td>
			   <td class="txt"> Sub Type(8-digit)  <span class="mand">*</span>  </td>
			    <td>
			     <s:if test ="%{dbtcustregbean.applicationid==null}">
			    	<select  id="Sub Type(8-digit)"  name="Sub Type(8-digit)" onchange="getSub Type(8-digit)(this.value);" >
			    	 <option value="-1" >Select Sub Type(8-digit)</option>
			    	</select>
			     </s:if>
			     <s:else>
			         <s:iterator value="dbtcustregbean.subproductlist">   
					<!--<s:property value="BRANCH_CODE" /> == <s:property value="%{dbtcustregbean.branchcode}"/> <s:property value="BRANCH_NAME"/> <br>-->
					  
					 <s:if test="Sub Type(8-digit)==dbtcustregbean.subproduct" >
					 					 <s:property value="Sub Type(8-digit)"/>   
					 </s:if>  
					</s:iterator>  
			     
			     </s:else>      
	
		 
		
			  <td class="txt"> Registration Date <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="registrationdate" id="registrationdate"
			 value="%{dbtcustregbean.registrationdate}" onkeyup="chkChars('Registration Date',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.registrationdate}" />
			 </s:else>
			    
		</tr> 
		 
		
		<tr>
		
			
			  <td class="txt"> Services  <span class="mand">*</span>   </td> 
			  
			  <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			  <select name="Services" id="Services">
			  <option value = "-1">Select Services</option>
			   </select>
			   </s:if>
			  <s:else>
			     <s:property value="%{dbtcustregbean.Services}" />
			  </s:else>
			  </td>   
			   <td class="txt" > Limit Based-On <span class="mand">*</span>  </td> 
			   <td > 
			    <s:if test ="%{dbtcustregbean.applicationid==null}">
			   <select name="Limit Based-On" id="Limit Based-On" >
			   <option value="-1">Select Limit Based-On</option>
			   </select> 
			    
			   </s:if>
			   <s:else>
			  
			      <s:property value="%{dbtcustregbean.Limit Based-On}" />
			   </s:else>
			   </td>
			    <td class="txt"> Application Date <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="applicationdate" id="applicationdate"
			 value="%{dbtcustregbean.applicationdate}" onkeyup="chkChars('Application Date',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.applicationdate}" />
			 </s:else>
			    
		</tr> 
		 
		<tr>
		<td class="txt"> W.Draw Amt  <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="W.Draw Amt " id="W.Draw Amt "  
			 value="%{dbtcustregbean.W.Draw Amt }" onkeyup="chkChars('W.Draw Amt ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.W.Draw Amt }" />
			 </s:else>
			
			 </td>  
			 
		<td class="txt"> W.Draw Cnt  <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="W.Draw Cnt " id="W.Draw Cnt "  
			 value="%{dbtcustregbean.W.Draw Cnt }" onkeyup="chkChars('W.Draw Cnt ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.W.Draw Cnt }" />
			 </s:else>
			
			 </td>  
			
			 <td class="txt"> Expiry Date <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="expirydate" id="expirydate"
			 value="%{dbtcustregbean.expirydate}" onkeyup="chkChars('Expiry Date',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.expirydate}" />
			 </s:else>
			     
			    
		</tr> 
		
		<tr>
		
		<td class="txt">Purc.Lmt Amt <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="Purc.Lmt Amt " id="Purc.Lmt Amt "  
			 value="%{dbtcustregbean.Purc.Lmt Amt}" onkeyup="chkChars('Purc.Lmt Amt ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.Purc.Lmt Amt}" />
			 </s:else>
			
			 </td>  
		
		<td class="txt">Purc.Lmt Cnt <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="Purc.Lmt Cnt " id="Purc.Lmt Cnt "  
			 value="%{dbtcustregbean.Purc.Lmt Cnt}" onkeyup="chkChars('Purc.Lmt Cnt ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.Purc.Lmt Cnt}" />
			 </s:else>
			
			 </td>  

			
			<td class="txt" > PIN GEN METHOD <span class="mand">*</span> </td> 
				   
					   <td > 
					    	<input type="radio" name="pingenmethod" id="pingenmethod" value="Y"> Green PIN
					    	<input type="radio" name="pingenmethod" id="pingenmethod" value="N"> PIN Mailer
					   </td>
			   		
				   <s:else>
				   <td>
				   		<s:property value="%{dbtcustregbean.pingenmethod}" />
				   	</td>
				   </s:else>
			
		</tr>
		
		<tr>
		
		<td class="txt">Tran Lmt Amt <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="Tran Lmt Amt " id="Tran Lmt Amt "  
			 value="%{dbtcustregbean.Tran Lmt Amt}" onkeyup="chkChars('Tran Lmt Amt ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.Tran Lmt Amt}" />
			 </s:else>
			
			 </td>  
			 
			 <td class="txt">Tran Lmt Cnt <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="Tran Lmt Cnt " id="Tran Lmt Cnt "  
			 value="%{dbtcustregbean.Tran Lmt Cnt}" onkeyup="chkChars('Tran Lmt Cnt ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.Tran Lmt Cnt}" />
			 </s:else>
			
			 </td>  
		
		<td class="txt" > Virtual Card Enabled <span class="mand">*</span>  </td> 
				  
					   <td > 
					    	<input type="radio" name="Virtual Card Enabled" id="Virtual Card Enabled" value="Y"> Yes
					    	<input type="radio" name="Virtual Card Enabled" id="Virtual Card Enabled" value="N"> No
					   </td>
			   		
				   <s:else>
				   <td>
				   		<s:property value="%{dbtcustregbean.Virtual Card Enabled}" />
				   	</td>
				   </s:else>
			</tr>
	
	</table>	
	
	 
	
 <s:if test ="%{dbtcustregbean.applicationid==null}">	 
  <table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return validatetab1()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>   
	</table> 
	</s:if>  
</s:form>
</sx:div>
  
 

  
<sx:div id="2" name="tab"  label="Account Level Information" value="2">
 	<s:form action="customerDetailsAddDebitCustomerRegister.do"  id="regform1" name="regform1" onsubmit="return showProcessing()" autocomplete = "off" namespace="/">
 		<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/> 
 		
	<table border="0" cellpadding="0" cellspacing="10" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	
	
	<tr>
		  <td class="txt"> Branch Name <span class="mand">*</span> </td>
		  <td>
			 <s:if test ="%{dbtcustregbean.applicationid==null}">
			 		<select id="branchname" name="branchname" onchange="getbranchList(this.value)">
					
					<option value="">No Branch Found</option>
					</s:if>
		
			 <s:else>
								<option value="" >Select Branch</option>   
								<s:iterator value="dbtcustregbean.branchlist">
								<option value="<s:property value="BRANCH_NAME"/>"><s:property value="BRANCH_NAME"/></option>
								</s:iterator>
					</s:else>
			
			  </td>
			  
			  
		 <td class="txt"> Prod. <span class="mand">*</span> </td>
		  <td>
			 <s:if test ="%{dbtcustregbean.applicationid==null}">
			 		<select id="product" name="product" onchange="getproductList(this.value)">
					
					<option value="">No product Found</option>
					</s:if>
		
			 <s:else>
								<option value="" >Select Product</option>   
								<s:iterator value="dbtcustregbean.productlist">
								<option value="<s:property value="PRODUCT"/>"><s:property value="PRODUCT"/></option>
								</s:iterator>
					</s:else>
			
			  </td>
		  <td class="txt"> Account Number <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="accountnumber" id="accountnumber" maxlength="19" 
			 value="%{dbtcustregbean.accountnumber}" onkeyup="chkChars('Account Number',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.accountnumber}" />
			 </s:else>
			
			 </td>  
	</tr> 
	
	
	<tr>
	<td class="txt"> Account Type <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="accounttype" id="accounttype"
			 value="%{dbtcustregbean.accounttype}" onkeyup="chkChars('Account Type',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.accountnumber}" />
			 </s:else>
			
			 </td>  
		 <td class="txt"> Currency <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="Currency" id="Currency"
			 value="%{dbtcustregbean.Currency}" onkeyup="chkChars('Currency',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.Currency}" />
			 </s:else>
			
			 </td>  
			 
			 <td class="txt"> A/C ID <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="A/C ID" id="A/C ID"
			 value="%{dbtcustregbean.A/C ID}" onkeyup="chkChars('A/C ID',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.A/C ID}" />
			 </s:else>
			
			 </td>  
		  
	</tr> 
	<tr>
		 <td class="txt"> A/C Lmt Type <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="A/C Lmt Type" id="A/C Lmt Type"
			 value="%{dbtcustregbean.A/C Lmt Type}" onkeyup="chkChars('A/C Lmt Type',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.A/C Lmt Type}" />
			 </s:else>
			
			 </td>  
			 
			  <td class="txt">WDL <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="WDL" id="WDL"
			 value="%{dbtcustregbean.WDL}" onkeyup="chkChars('WDL',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.WDL}" />
			 </s:else>
			
			 </td>  
	</tr> 
	
	</table>	
	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" id="button">
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return validatetab2_customer()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	 </s:form>
</sx:div>


<sx:div id="3"  name="tab" label="Customer Information" value="3" >
 	<s:form action="CustomertDetailsAddDebitCustomerRegister.do"  id="regform" onsubmit="return showProcessing()" name="regform" autocomplete = "off" namespace="/">
 	<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/> 
	<table border="0" cellpadding="0" cellspacing="10" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	
	
		<tr>   
		
			  <td class="txt"> Enter CustomerID <span class="mand">*</span> </td> <td> 
			 <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="customeridno" id="customeridno" value=""  maxlength="%{dbtcustregbean.cinnoLength}" onkeyup="validateNumber('Customer Id ',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.customeridno}" />
			 </s:else>
			
			 </td>  
			
			 
			 <td class="txt"> Gender <s:property value="dbtcustregbean.f"/> <span class="mand">*</span> </td> <td> 
		   <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="gender"  headerKey="-1" headerValue="-SELECT-" name="gender" value="%{dbtcustregbean.gender}" /> </td>
			 
			 <td class="txt"> Encoding Name <span class="mand">*</span> </td> <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="encname" id="encname" maxlength="25" 
			 value="%{dbtcustregbean.encname}" onkeyup="chkChars('Encoding Name',this.id,this.value)" /> 
			 </s:if>
			 <s:else>
			 <s:property value="%{dbtcustregbean.encname}" />
			 </s:else>
			
			 </td>  
		</tr>
		
		<tr>	 
			 <td class="txt"> Annual Income <s:property value="dbtcustregbean.f"/> <span class="mand">*</span> </td> <td> 
		   <s:select list="{10,000 - 50,000}" id="Annual Income"  headerKey="-1" headerValue="-SELECT-" name="Annual Income" value="%{dbtcustregbean.Annual Income}" /> </td>
		   
		   
		   <td class="txt"> Embossing Name <span class="mand">*</span> </td> 
			 <td> 
			  <s:if test ="%{dbtcustregbean.applicationid==null}">
			 <s:textfield name="embname" id="embname" maxlength="25" 
			 value="%{dbtcustregbean.embname}" 
			 onkeyup="chkChars('Embossing Name',this.id,this.value)" />
			 </s:if>
			 <s:else>
			 <s:property 
			 value="%{dbtcustregbean.embname}" 
			 />
			 
			 </s:else>
			  </td>
			  
			  
			  <td class="txt"> Spouse Birth'Date <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob" readonly="true"  value="%{dbtcustregbean.dob}"  style="width:160px" onchange="validateDate(this.value)"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		   
		</tr> 
		
		<tr>
		
	 
		 <td class="txt">Date Of Birth<span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob" readonly="true"  value="%{dbtcustregbean.dob}"  style="width:160px" onchange="validateDate(this.value)"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		 
			 <td class="txt"> Spouse Annv'Date <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob" readonly="true"  value="%{dbtcustregbean.dob}"  style="width:160px" onchange="validateDate(this.value)"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		
		
		<td class="txt"> Spouse Name <span class="mand">*</span> </td> 
		 <td> <s:textfield name="spousename" id="spousename" maxlength="32"  value="%{dbtcustregbean.spousename}" onkeyup="chkChars(' Spouse Name ',this.id,this.value)" /> </td>
	
</tr>
	</table>	 
	
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return tab3_validation()" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
</s:form>
</sx:div>

<sx:div id="4"  name="tab" label="Contact Information" value="4"  >
<s:form action="ContactDebitCustomerRegister.do"  id="regform" onsubmit="return tab4_validation()" name="regform" autocomplete = "off" namespace="/">

<input type="hidden" value="<s:property value="%{dbtcustregbean.applicationid}"/>" id="tab4_applicationid" name="tab4_applicationid" /> 

	<table border="0" cellpadding="0" cellspacing="10" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
				
								
		<tr>				
								
          <td class="txt"> Residence Address  
			  <td> <s:textfield name="Residence Address " id="Residence Address " maxlength="32" value="%{dbtcustregbean.Residence Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Residence Address " id="Residence Address " maxlength="32" value="%{dbtcustregbean.Residence Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Residence Address " id="Residence Address " maxlength="32" value="%{dbtcustregbean.Residence Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Residence Address " id="Residence Address " maxlength="32" value="%{dbtcustregbean.Residence Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Residence Address " id="Residence Address " maxlength="32" value="%{dbtcustregbean.Residence Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		</tr> 
		
		<tr>				
								
          <td class="txt"> Office Address  
			  <td> <s:textfield name="Office Address " id="Office Address " maxlength="32" value="%{dbtcustregbean.Office Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Office Address " id="Office Address " maxlength="32" value="%{dbtcustregbean.Office Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Office Address " id="Office Address " maxlength="32" value="%{dbtcustregbean.Office Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Office Address " id="Office Address " maxlength="32" value="%{dbtcustregbean.Office Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Office Address " id="Office Address " maxlength="32" value="%{dbtcustregbean.Office Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		</tr> 
		
<tr>				
								
          <td class="txt"> Mailing Address  
			  <td> <s:textfield name="Mailing Address " id="Mailing Address " maxlength="32" value="%{dbtcustregbean.Mailing Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Mailing Address " id="Mailing Address " maxlength="32" value="%{dbtcustregbean.Mailing Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Mailing Address " id="Mailing Address " maxlength="32" value="%{dbtcustregbean.Mailing Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Mailing Address " id="Mailing Address " maxlength="32" value="%{dbtcustregbean.Mailing Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		<td class=" "> 
		 <td> <s:textfield name="Mailing Address " id="Mailing Address " maxlength="32" value="%{dbtcustregbean.Mailing Address }" onkeyup="chkChars('Contact Street Name',this.id,this.value)"  /> </td>
		
		</tr> 
		</table>
		
<table border="50" cellpadding="20" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px; margin-left:11px; margin-right:5px"  >	
		
<tr>   
			 <td class="txt"> Off.Phone  <span class="mand">*</span>   </td> 
			 <td> 
			 <s:textfield name="Off.Phone" id="Off.Phone" maxlength="10"   value="%{dbtcustregbean.Off.Phone}"  onkeyup="validateNumber('Off.Phone',this.id,this.value)"/> 
			 </td>

             <td class="txt"> Off.Phone2  <span class="mand">*</span>   </td> 
			 <td> 
			 <s:textfield name="Off.Phone2" id="Off.Phone2" maxlength="10"   value="%{dbtcustregbean.Off.Phone2}"  onkeyup="validateNumber('Off.Phone2',this.id,this.value)"/> 
			 </td>
		   <td class="txt"> Off.Fax  <span class="mand">*</span>   </td> 
			 <td> 
			 <s:textfield name="Off.Fax" id="Off.Fax" maxlength="10"   value="%{dbtcustregbean.Off.Fax}"  onkeyup="validateNumber('Off.Fax',this.id,this.value)"/> 
			 </td>
		     
			 
			 </tr>
			 
		<tr>
	
			 <td class="txt"> Off.E-Mail  <span class="mand">*</span>   </td> 
			 <td> 
			 <s:textfield name="Off.E-Mail" id="Off.E-Mail" maxlength="25"   value="%{dbtcustregbean.Off.E-Mail }"  onkeyup="validateNumber('Off.E-Mail',this.id,this.value)"/> 
			 </td>
			 
			  
			 <td class="txt"> Mobile No  <span class="mand">*</span>   </td> 
			 <td> 
			 <s:textfield name="mobile" id="mobile" maxlength="10"   value="%{dbtcustregbean.mobile}"  onkeyup="validateNumber('Mobile',this.id,this.value)"/> 
			 </td>
	
	</tr>	</table>	 
	
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit"   name="fourthtab" id="fourthtab" onclick="return submit()" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 

	</s:form>

</sx:div>


  

<!-- --------------------- SUPPLIMENTARY DETAILS----------- -->

<%-- 
<sx:div id="4" name="tab"  label="Supplimentary Card Details" value="6"   disabled="true" >
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="supplimentform" onsubmit="return validateSuplimentDet()" name="supplimentform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Supplimentary Details
		<input type="hidden" id="reqfrom" name="reqfrom" value="6" /> 
		<input type="hidden" id="gotoval" name="gotoval" value="6" />
		<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{dbtcustregbean.customerid}"/>
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
		 <s:select list="%{dbtcustregbean.documentlist}"  id="idproof"  name="idproof"  listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" />  
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  /> </td>
	</tr>  
	
	

	<tr>
		 <td class="txt"> Gender <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="supgender" onchange = "getSupRelation(this.value)"  headerKey="-1" headerValue="-SELECT-" name="supgender" value="%{custregbean.gender}"/> </td>
		  <td class="txt"> Relationship <span class="mand">*</span> </td> <td> <s:select  list="%{dbtcustregbean.supplimentrelationlist}" id="suprelationship" listKey="RELATIONCODE" listValue="RELATIONDESC" name="suprelationship"  headerKey="-1" headerValue="-SELECT-" /> </td>
		   
	</tr> 
	 
	 <tr >
	 <td colspan="6" style="text-align:center">
	 		<s:submit value="Add"   name="suplimentadd" id="sixtab" onclick="return customerinfo_checker()"/>
	 </td>
	 </tr>
	 
	
	 <tr>
	 	<td colspan="6">
	 		<table border='0' cellpadding='0' cellspacing='0' width='100%'>
	 		 <s:if test="dbtcustregbean.hassupliment"> 
	 			<tr><th>First Name</th><th>Middle Name</th><th>Last Name</th><th>Name on Name</th><th> Gender </th><th>DOB</th> 
	 			<th>Nationality</th> <th>Citizenship / Passport </th>  <th>Document Number </th>  <th> Relationship </th>   <th> Delete </th> </tr>
			 </s:if>		
	 		<s:iterator value="dbtcustregbean.supplimentlist">
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

</sx:tabbedpanel>

 
	
	
 
 
</body>