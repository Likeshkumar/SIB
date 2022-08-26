<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 
<script>
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
		var url = "deleteSupplimentCardCreditCustRegisteration.do?applicationid="+applicationid.value+"&customerid="+customerid+"&reqfrom=6&goto=6"; 
		alert( url )
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
		
		var url="customerDetailsAddCreditCustRegisteration.do?goto=5&reqfrom=4&applicationid="+applicationid.value+"&customerid="+customerid.value;
		//alert(url)
		window.location=url;
		return true;
	}
	function validateReference(){
		var referncename = document.getElementById("referncename");
		var referencephone = document.getElementById("referencephone");
		
		if( referncename ){ if( referncename.value=="") { errMessage(referncename, "Enter Reference Name !");return false;} }
		if( referencephone ){ if( referencephone.value=="") { errMessage(referencephone, "Enter Reference Phone No.!");return false;} }
		if( referencephone.value.length < 10 ){	errMessage(referencephone, "Enter valid Phone No.!");return false;}  
		
		return true;
		
	}
	
	
	function validateDocType(){ 
		var documenttype = document.getElementById("documenttype");
		var documentnumber= document.getElementById("documentnumber");
		
		if( documenttype ){ if( documenttype.value=="-1") { errMessage(documenttype, "Select Document Type !");return false;} }
		if( documentnumber ){ if( documentnumber.value=="") { errMessage(documentnumber, "Enter Document Number !");return false;} }		
		return true;
	}
	
	function deleteReference( refid ){
		if( confirm("Do you want to delete ? ") ){
			var url = "deleteReferenceCreditCustRegisteration.do?refid="+refid;
			//alert( url );
			window.location = url;
		}
	}
	
	function deleteDocument( docid ){
		if( confirm("Do you want to delete ? ") ){
			var url = "deleteDocumentCreditCustRegisteration.do?docid="+docid;
			//alert( url );
			window.location = url;
		}
	}
	
	function validateForm(){  
		var firstname = document.getElementById("firstname");
		var middlename = document.getElementById("middlename");
		var lastname = document.getElementById("lastname");
		var embname = document.getElementById("embname"); 
		var nationality = document.getElementById("nationality");
		var dob = document.getElementById("dob");
		var gender = document.getElementById("gender");
		var mstatus = document.getElementById("mstatus");
		var residence = document.getElementById("residence");
		var vehicle = document.getElementById("vehicle");
		var vehicleno = document.getElementById("vehicleno");
		var idproof = document.getElementById("idproof");
		var idproofno = document.getElementById("idproofno");
		var dateofissue = document.getElementById("dateofissue");
		var districofissue = document.getElementById("districofissue");
		var typeofcard = document.getElementById("typeofcard");
		var spousename = document.getElementById("spousename");
		var mothername = document.getElementById("mothername");
		var fathername = document.getElementById("fathername");
		var grandfathername = document.getElementById("grandfathername");
		var stmtdelivery = document.getElementById("stmtdelivery");
		var smsalert= document.getElementById("smsalert");
		
		if( firstname ){ if( firstname.value == "" ) {errMessage(firstname, "Enter First Name !");return false;} }
		if( embname ){ if( embname.value == "" ) {errMessage(embname, "Enter Name on Card  !");return false;} }
		if( nationality ){ if( nationality.value == "-1" ) {errMessage(nationality, "Select nationality!");return false;} }
		if( dob ){ if( dob.value == "" ) {errMessage(dob, "Enter Date of Birth !");return false;} }
		if( gender ){ if( gender.value == "-1" ) {errMessage(gender, "Select Gender !");return false;} }
		if( mstatus ){ if( mstatus.value == "-1" ) {errMessage(mstatus, "Select marital status!");return false;} }
		if( residence ){ if( residence.value == "-1" ) {errMessage(residence, "Select Residence !");return false;} }
		if( vehicle ){ if( vehicle.value == "-1" ) {errMessage(vehicle, "Select Own vehicle available !");return false;} }
		if( vehicle.value == "Y" ){ 
			if( vehicleno ){ if( vehicleno.value == "" ) {errMessage(vehicleno, "Enter Vehicle Number !"); return false;}  }
		}
		 
		if( idproof ){ if( idproof.value == "-1" ) {errMessage(idproof, "Select Citizen/Passport Details !");return false;} }
		if( idproofno ){ if( idproofno.value == "" ) {errMessage(idproofno, "Enter Document Number ");return false;} }
		if( dateofissue ){ if( dateofissue.value == "" ) {errMessage(dateofissue, "Enter Date of Issue Date!");return false;} }
		if( districofissue ){ if( districofissue.value == "" ) {errMessage(districofissue, "Enter District of Issue!");return false;} }		
		if( typeofcard ){ if( typeofcard.value == "-1" ) {errMessage(typeofcard, "Select Type of Card!");return false;} }
		if( stmtdelivery ){ if( stmtdelivery.value == "-1" ) {errMessage(stmtdelivery, "Select Statement Delivery Type !");return false;} }
		if( smsalert ){ if( smsalert.value == "-1" ) {errMessage(smsalert, "Select Sms Alert Required !");return false;} }
		if( mstatus.value == "M"){
			if( spousename ){ if( spousename.value == "" ) {errMessage(spousename, "Enter Spouse Name!");return false;} }
		}
		if( mothername ){ if( mothername.value == "" ) {errMessage(mothername, "Enter Mother Name!");return false;} }  
		return true; 
		 
	}
	
	function ismarried()
	{
		var mstatus= document.getElementById("mstatus");
		var spousename= document.getElementById("spousename");
		if( mstatus.value != "M"){
			spousename.readOnly = true;
	}
	
	
	
	function setContactAddress( checkedval ){
	 
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
			if( checkedval ){	
				copobox.value = prpobox.value;
				cohouseno.value = prhouseno.value;
				costreetname.value = prstreetname.value;
				cowardname.value = prwardname.value;
				cocity.value = prcity.value;
				codistrict.value = prpobox.value;
				cophone1.value = prphone1.value;
				cophone2.value = prphone2.value;
				comobileno.value = prmobileno.value;
				cofax.value = prfax.value;
				secemail.value = primaryemail.value;
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
</script>
<title>Insert title here</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body onload="selectTab()">
<s:hidden name="reglevel" id="reglevel" value="%{creditbean.reglevel}"/>
<s:hidden name="jscurrenttab" id="jscurrenttab" value="%{creditbean.gototab}"/> 

<s:property value="creditbean.valid"/>
<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 100%; margin:0 auto; height: 500px;" doLayout="true" selectedTab="%{creditbean.gototab}"  >

<sx:div id="1" name="tab"  label="Personal Details" value="1"  disabled="true"  >
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="regform" onsubmit="return validateForm()" name="regform1" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Personal Information
		<input type="hidden" id="reqfrom" name="reqfrom" value="1" /> 
		<input type="hidden" id="goto" name="goto" value="2" />
		<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
		<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		<s:hidden name="addnew" id="addnew" value="%{creditbean.addnew}"/>
	</th><tr> 
	<tr>
		 <td class="txt"> First Name <span class="mand">*</span> </td> <td> <s:textfield name="firstname" id="firstname" maxlength="32"  value="%{creditbean.firstname}" /> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="middlename" id="middlename" maxlength="32" value="%{creditbean.middlename}"  /> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="lastname" id="lastname" maxlength="32" value="%{creditbean.lastname}"  /> </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob" readonly="true"  value="%{creditbean.dob}"  style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		  <td class="txt"> Nationality <span class="mand">*</span>  </td> <td> <s:textfield name="nationality" id="nationality" maxlength="32" value="%{creditbean.nationality}"/> </td>
		   <td class="txt"> Name On Card <span class="mand">*</span> </td> <td> <s:textfield name="embname" id="embname" maxlength="32" value="%{creditbean.embname}" /> </td>
	</tr> 
	
	
	
	<tr>
		 <td class="txt"> Gender <s:property value="creditbean.f"/> <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="gender"  headerKey="-1" headerValue="-SELECT-" name="gender" value="%{creditbean.gender}" /> </td>
		  <td class="txt"> Marital Status <span class="mand">*</span> </td> <td> 
		  <s:select list="#{'M':'Married','U':'Un-Married','O':'Other'}" id="mstatus"  headerKey="-1" headerValue="-SELECT-" 
		  name="mstatus" value="%{creditbean.mstatus}" onchange="ismarried()"/> </td>
		   
	</tr> 
	
	<tr>
		 <td class="txt"> Residence <span class="mand">*</span> </td> <td> <s:select list="#{'W':'Own','R':'Rental','O':'Other'}" id="residence"  name="residence"  headerKey="-1" headerValue="-SELECT-"  value="%{creditbean.residence}"/> </td>
		  <td class="txt"> Own Vehicle <span class="mand">*</span> </td> <td> <s:select list="#{'Y':'Yes','N':'No'}" id="vehicle"   name="vehicle" headerKey="-1" headerValue="-SELECT-" value="%{creditbean.vehicle}"/> </td>
		   <td class="txt"> Vehicle Number   </td> <td> <s:textfield name="vehicleno" id="vehicleno" maxlength="32"  value="%{creditbean.vehicleno}" /> </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Citizenship / Passport <span class="mand">*</span> </td> <td> 
		  	<s:select list="%{creditbean.documentlist}"  id="idproof"  name="idproof"  listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" value="%{creditbean.idproof}" />
		  </td>
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  value="%{creditbean.idproofno}" /> </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Date of Issue <span class="mand">*</span> </td> 
		  <td> 
		 	<s:textfield name="dateofissue" id="dateofissue" readonly="true" style="width:160px"  value="%{creditbean.dateofissue}"  />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dateofissue,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
			 </td>
		 
		   <td class="txt"> District of Issue  <span class="mand">*</span>  </td> <td> <s:textfield name="districofissue" id="districofissue" maxlength="32" value="%{creditbean.districofissue}"  /> </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Type of Card <span class="mand">*</span> </td> <td> <s:select list="#{'$10':'10%','$100':'100%'}" id="typeofcard"  name="typeofcard"  headerKey="-1" headerValue="-SELECT-"   value="%{creditbean.typeofcard}"/> </td>
		 
		 <td class="txt"> Statement Delivery <span class="mand">*</span>  </td> <td> <s:select list="#{'$DIREC':'Collect','$MAIL':'E-mail'}"  name="stmtdelivery" id="stmtdelivery" headerKey="-1" headerValue="-SELECT-"  value="%{creditbean.stmtdelivery}" />  </td>
		 
		 <td class="txt"> Sms Alert Required <span class="mand">*</span>  </td> <td> <s:select list="#{'Y':'Yes','N':'No'}"  name="smsalert" id="smsalert" headerKey="-1" headerValue="-SELECT-" value="%{creditbean.smsalert}"  />  </td>
	</tr>  
	
	
	 <tr><th colspan="6" style="text-align:left" class="tbheader"> Family Details </th><tr> 
	
	<tr>
		<td class="txt"> Spouce Name   </td> <td> <s:textfield name="spousename" id="spousename" maxlength="32" value="%{creditbean.spousename}"   /> </td>
		 <td class="txt"> Mother's Name <span class="mand">*</span> </td> <td> <s:textfield name="mothername" id="mothername" maxlength="32" value="%{creditbean.mothername}"  /> </td>
		  <td class="txt"> Father Name  </td> <td> <s:textfield name="fathername" id="fathername" maxlength="32"  value="%{creditbean.fathername}"  /> </td> 
	</tr> 
	
	<tr> 
		   <td class="txt"> Grand Father Name </td> <td> <s:textfield name="grandfathername" id="grandfathername" maxlength="32" value="%{creditbean.grandfathername}"  /> </td>
	</tr> 
	
	
	</table>	
	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue" name="firsttab" id="firsttab" onclick="return customerinfo_checker()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table>

	</s:form>
</sx:div>

 

<sx:div id="2"  name="tab" label="Contact Details" value="2"  disabled="true" >
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="regform" onsubmit="return valiteContactDetails()" name="regform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Permenent Address
			<input type="hidden" id="reqfrom" name="reqfrom" value="2" /> 
			<input type="hidden" id="goto" name="goto" value="3" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Po.Box <s:property value="creditbean.prphone1"/> <span class="mand">*</span> </td> <td> <s:textfield name="prpobox" id="prpobox" maxlength="32" value="%{creditbean.prpobox}" /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td> <s:textfield name="prhouseno" id="prhouseno" maxlength="32" value="%{creditbean.prhouseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td> <s:textfield name="prstreetname" id="prstreetname" maxlength="32" value="%{creditbean.prstreetname}" /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="prwardname" id="prwardname" maxlength="32" value="%{creditbean.prwardname}" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:textfield name="prcity" id="prcity" maxlength="32" value="%{creditbean.prcity}"/> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td> <s:textfield name="prdistrict" id="prdistrict" maxlength="32" value="%{creditbean.prdistrict}"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td> <s:textfield name="prphone1" id="prphone1" maxlength="16"  value="%{creditbean.prphone1}" /> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="prphone2" id="prphone2" maxlength="16"  value="%{creditbean.prphone2}" /> </td>
			   <td class="txt"> Fax  </td> <td> <s:textfield name="prfax" id="prfax" maxlength="16" value="%{creditbean.prfax}" /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> <td> <s:textfield name="prmobileno" id="prmobileno" maxlength="16"  value="%{creditbean.prmobileno}"  /> </td>
			 <td class="txt"> E-Mail  <span class="mand">*</span>   </td> <td> <s:textfield name="primaryemail" id="primaryemail" maxlength="32"  value="%{creditbean.primaryemail}"  /> </td>
			 
		</tr> 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> <s:checkbox name="sameasperm" id="sameasperm" onchange="setContactAddress( this.checked )"/> Contact Address </th>
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> <td> <s:textfield name="copobox" id="copobox" maxlength="32" value="%{creditbean.copobox}"   /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td> <s:textfield name="cohouseno" id="cohouseno" maxlength="32" value="%{creditbean.cohouseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td> <s:textfield name="costreetname" id="costreetname" maxlength="32" value="%{creditbean.costreetname}"  /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="cowardname" id="cowardname" maxlength="32" value="%{creditbean.cowardname}" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:textfield name="cocity" id="cocity" maxlength="32" value="%{creditbean.cocity}"/> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td> <s:textfield name="codistrict" id="codistrict" maxlength="32" value="%{creditbean.codistrict}"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td> <s:textfield name="cophone1" id="cophone1" maxlength="16"  value="%{creditbean.cophone1}"  /> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="cophone2" id="cophone2" maxlength="16" value="%{creditbean.cophone2}"  /> </td>
			   <td class="txt"> Fax  </td> <td> <s:textfield name="cofax" id="cofax" maxlength="16" value="%{creditbean.cofax}"  /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> <td> <s:textfield name="comobileno" id="comobileno" maxlength="16"   value="%{creditbean.comobileno}"  /> </td>
			 <td class="txt"> E-Mail      </td> <td> <s:textfield name="secemail" id="secemail" maxlength="32"   value="%{creditbean.secemail}"   /> </td>
		 
		</tr> 
		
		 
		
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div>

<sx:div id="3"  name="tab" label="Occupation Details" value="3"  disabled="true" >
	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="regform" onsubmit="return validateBusiness()" name="regform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px">	 
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Occupation Details
			<input type="hidden" id="reqfrom" name="reqfrom" value="3" /> 
			<input type="hidden" id="goto" name="goto" value="4" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Occupation <span class="mand">*</span> </td> 
			 <td> 
			 	<td> <s:select list="#{'$SAL':'Salaried','$SELF':'Self-Employed','$BUS':'Business'}"  name="occupation" id="occupation" headerKey="-1" headerValue="-SELECT-"  value="%{creditbean.occupation}" /> 
			 </td>
			  <td class="txt"> Work For <span class="mand">*</span> </td> 
			 <td> 
			 	<td> <s:select list="#{'$GOVT':'Government','$PRIV':'Private','$BUS':'Business'}"  name="workfor" id="workfor" headerKey="-1" headerValue="-SELECT-"  value="%{creditbean.workfor}"  onchange="showCompany(this.value)"/> 
			 </td> 
			 
		</tr>
		<tr id="trcomp" style="display:none">
			 <td class="txt"> Company Name  <span class="mand">*</span>   </td> <td> <s:textfield name="companyname" id="companyname" maxlength="32" value="%{creditbean.companyname}" /> </td>
			 
		</tr>
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Name of the Employer/Company/Firm</th></tr> 
		<tr>
			 <td class="txt"> Designation  <span class="mand">*</span>   </td> <td> <s:textfield name="designation" id="designation" maxlength="32" value="%{creditbean.designation}" /> </td>
			 <td class="txt"> Nature of Business      </td> <td> <s:textfield name="natofbusiness" id="natofbusiness" maxlength="32"  value="%{creditbean.natofbusiness}"  /> </td>
		 	 <td class="txt"> Employed/Business Since     </td> <td> <s:select list="%{creditbean.listofyears}"  name="employedsince" id="employedsince" headerKey="-1" headerValue="-SELECT-"  value="%{creditbean.employedsince}"/>  </td>
		</tr> 
		
		 <tr><th colspan="6" style="text-align:left" class="tbheader"> Employer / Business Address </th></tr> 
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> <td> <s:textfield name="emppobox" id="emppobox" maxlength="32"  value="%{creditbean.emppobox}" /> </td>
			  <td class="txt"> Office/Business/Firm No.  <span class="mand">*</span>   </td> <td> <s:textfield name="emphouseno" id="emphouseno" maxlength="32"  value="%{creditbean.emphouseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td> <s:textfield name="empstreetname" id="empstreetname" maxlength="32"  value="%{creditbean.empstreetname}"  /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="empwardname" id="empwardname" maxlength="32"   value="%{creditbean.empwardname}"  /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:textfield name="empcity" id="empcity" maxlength="32" value="%{creditbean.empcity}"  /> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td> <s:textfield name="empdistrict" id="empdistrict" maxlength="32" value="%{creditbean.empdistrict}"  /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1 <span class="mand">*</span>  </td> <td> <s:textfield name="empphone1" id="empphone1" maxlength="16" value="%{creditbean.empphone1}" /> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="empphone2" id="empphone2" maxlength="16" value="%{creditbean.empphone2}"  /> </td>
			   <td class="txt"> Fax  </td> <td> <s:textfield name="empfax" id="empfax" maxlength="16" value="%{creditbean.empfax}"  /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Mobile  </td> <td> <s:textfield name="empmobileno" id="empmobileno" maxlength="16" value="%{creditbean.empmobileno}"  /> </td>
			 <td class="txt"> E-Mail  <span class="mand">*</span>   </td> <td> <s:textfield name="empemail" id="empemail" maxlength="32" value="%{creditbean.empemail}"  /> </td>
			 
		</tr> 
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return customerinfo_checker()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div> 

<!-- ---------------------- -->

<sx:div id="4"  name="tab" label="Document Details" value="4" disabled="true"> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px">	 
	<tr><th colspan="4" style="text-align:left" class="tbheader"> Reference
			<input type="hidden" id="reqfrom" name="reqfrom" value="4" /> 
			<input type="hidden" id="goto" name="goto" value="5" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid3" value="%{creditbean.customerid}"/>
		</th>
	</tr> 
		<s:form action="referenceAddCreditCustRegisteration.do" onsubmit="return validateReference()" id="refform"  name="refform" autocomplete = "off" namespace="/">
			<s:hidden name="applicationid" id="applicationid2" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid2" value="%{creditbean.customerid}"/>
			<input type="hidden" id="reqfrom" name="reqfrom" value="4" /> 
		<tr>
			<td width="50%" valign="top">
				<table border="0" cellpadding="0" cellspacing="0" width="100%"  style="border:none;padding-top:10px">	 
					<tr>
						  <td class="txt"> Reference  <span class="mand">*</span>   </td> <td> <s:textfield name="referncename" id="referncename" maxlength="32"  /> </td>
						  <td class="txt"> Phone  <span class="mand">*</span>   </td> <td> <s:textfield name="referencephone" id="referencephone" maxlength="16"  /> </td> 
					</tr>
					<tr>
						<td colspan="4" style="text-align:center"> <s:submit name="addref" id="addref" value="Add"/> </td>
					</tr>
				</table>
			</td>
			<td valign="top">
				<table border="1" cellpadding="0" cellspacing="0" width="100%"  style="border:none;padding-top:10px">
					<input type="hidden"  name="refmastercount" id="refmastercount" value='2' />
						<input type="hidden"  name="refcount" id="refcount" value='<s:property value="creditbean.reflist.size()"/>' />  
					<tr>
						<s:if test="creditbean.hasreference">
						  <th> Reference  </th> <th> Phone</th> <th> Delete </th> 
						</s:if>
						<s:iterator value="creditbean.reflist">
						
							<tr><td> ${REFERNCENAME}  </td> <td> ${REFERENCEPHONE} </td> <td> <img src="images/delete.png" onclick="deleteReference( '${REFERENCEID}' )" /> </td>  </tr> 
						</s:iterator>
					</tr>
				</table>
			</td>
		</tr>  
	</s:form>
	
	
		<tr><th colspan="4" style="text-align:left" class="tbheader"> Documents </th> 
		<s:form action="documentAddCreditCustRegisteration.do"  id="doc" onsubmit="return validateDocType()" name="doc" autocomplete = "off" namespace="/">
			<s:hidden name="applicationid" id="applicationid2" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid2" value="%{creditbean.customerid}"/>
			<input type="hidden" id="reqfrom" name="reqfrom" value="4" /> 
		<tr>
			<td width="50%" valign="top">
				<table border="0" cellpadding="0" cellspacing="0" width="100%"  style="border:none;padding-top:10px">	 
					<tr>
						  <td class="txt"> Document  <span class="mand">*</span>   </td> 
						  <td> 
						  		<s:select list="%{creditbean.documentlist}"  name="documenttype" id="documenttype" listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" /> 
						  </td>
						  <td class="txt"> Document Number  <span class="mand">*</span>   </td> <td> <s:textfield name="documentnumber" id="documentnumber" maxlength="16"  /> </td> 
					</tr>
					<tr>
						<td colspan="4" style="text-align:center"> <s:submit name="adddoc" id="adddoc" value="Add"/> </td>
					</tr>
				</table>
			</td>
			<td valign="top">
				<table border="1" cellpadding="0" cellspacing="0" width="100%"  style="border:none;padding-top:10px">	 
					<tr>
						<input type="hidden"  name="documentmastercount" id="documentmastercount" value='2' />
						<input type="hidden"  name="documentcount" id="documentcount" value='<s:property value="creditbean.configdocumentlist.size()"/>' /> 
						<s:if test="creditbean.hasdocument">
						  <th> Document Name  </th> <th> Document Number </th> <th> Delete </th> 
						</s:if>
						<s:iterator value="creditbean.configdocumentlist">
							 
							<tr><td> ${DOCUMENTTYPE}  </td> <td> ${DOCUMENTNUMBER} </td> <td> <img src="images/delete.png" onclick="deleteDocument( '${DOCUMENTSEQ}' )" /> </td>  </tr> 
						</s:iterator>
					</tr>
				</table>
			</td>
		</tr>
		</s:form>
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" style="padding-top:20px" >
		<tr>
		<td>
			<s:submit value="Continue"   name="fourthtable" id="fourth" onclick="return validateRerefenceForm()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table>  
	
</sx:div> 


<!-- ----------------INCOME DETAILS-------------- -->

<sx:div id="5"  name="tab" label="Income Details" value="5"   disabled="true">
	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="incomeform" onsubmit="return validateIncomeDetails()" name="incomeform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px">	 
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Income Details
			<input type="hidden" id="reqfrom" name="reqfrom" value="5" /> 
			<input type="hidden" id="goto" name="goto" value="6" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Annual Salary <span class="mand">*</span> </td> 
			  <td> <s:textfield name="annualsal" id="annualsal" maxlength="16" onchange="addTotalAnnual()" onkeypress="return numerals(event)"  value="%{creditbean.annualsal}" /> </td>
			  
			 <td class="txt"> Annual Bonus/Incentive   </td> 
			  <td> <s:textfield name="annualbonus" id="annualbonus" maxlength="16" onchange="addTotalAnnual()"   onkeypress="return numerals(event)" value="%{creditbean.annualbonus}" /> </td>
			 
			 <td class="txt"> Annual Business Income  </td> 
			  <td> <s:textfield name="annualbusinessincome" id="annualbusinessincome" maxlength="16" onchange="addTotalAnnual()"  onkeypress="return numerals(event)" value="%{creditbean.annualbusinessincome}"   /> </td> 
		</tr>
		
		<tr>
			 <td class="txt"> Rental Income </td> 
			  <td> <s:textfield name="rentalincome" id="rentalincome" maxlength="16" onchange="addTotalAnnual()" onkeypress="return numerals(event)"  value="%{creditbean.rentalincome}"  /> </td>
			  
			 <td class="txt"> Agriculture Income  </td> 
			  <td> <s:textfield name="agriculture" id="agriculture" maxlength="16" onchange="addTotalAnnual()"  onkeypress="return numerals(event)" value="%{creditbean.agriculture}" /> </td>
			 
			 <td class="txt"> Other Income  </td> 
			 <td> <s:textfield name="otherincome" id="otherincome" maxlength="16" onchange="addTotalAnnual()" onkeypress="return numerals(event)" value="%{creditbean.otherincome}" /> </td> 
		</tr>
		<tr>
			 <td class="txt"> Total Annual Income </td> 
			  <td> <s:textfield name="totannualincome" id="totannualincome" maxlength="16" readonly="true" onclick="addTotalAnnual()"  value="%{creditbean.totannualincome}"  /> </td>
		</tr>
		
		<tr>
			 <td colspan="3"> Existing Credit Card of Other Bank  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	 <s:radio list="#{'$Y':'Yes','$N':'No'}"  name="existcreditcard"   value="%{creditbean.existcreditcard}"  onclick="showBankDet(this.value)"  />
			 	 
			 <!-- 	<input type="radio" name="existcreditcard" id="existyes" onclick="showBankDet(this.value)" value="$Y"/> Yes  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	<input type="radio" name="existcreditcard" id="existno" checked="true" value="$N"  value="%{creditbean.existcreditcard}" />
			 		 	 No   
			 	 -->
			 	 
			 	 
			</td>  
			 	
		</tr>
		
		<tr id="trcreditbankdet">  <!-- style="display:none"  -->
			 <td class="txt"> Name of the Bank  <span class="mand">*</span>   </td> <td> <s:textfield name="excrcardbank" id="excrcardbank" maxlength="32" value="%{creditbean.excrcardbank}" /> </td>
			  <td class="txt"> Name of the Branch  <span class="mand">*</span>   </td> <td> <s:textfield name="excrcardbranch" id="excrcardbranch" maxlength="32"  value="%{creditbean.excrcardbranch}" /> </td>
			  <td class="txt"> Account Type  <span class="mand">*</span>   </td> 
			  <td> <s:select list="%{creditbean.accttypelist}"  name="excrcardacctype" id="excrcardacctype" listKey="ACCTTYPEID" listValue="ACCTTYPEDESC" headerKey="-1" headerValue="-SELECT-" value="%{creditbean.excrcardacctype}" /></td>
		</tr>
		<tr><th colspan="6" style="text-align:left" class="tbheader"> <s:checkbox name="$STANDIING" id="$STANDING" value="$STANDING" /> Standing Instruction </th></tr> 
		<tr>
			 <td  > Account With <%=session.getAttribute("Instname")%>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			 <td>	
			 <s:radio list="#{'$Y':'Yes','$N':'No'}"  name="acctwithprimarybank"  value="%{creditbean.acctwithprimarybank}"  />
			 	<!-- <input type="radio" name="acctwithprimarybank" id="acctyes"  checked="true"  value="$Y" value="%{creditbean.acctyes}" /> Yes  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 	<input type="radio" name="acctwithprimarybank" id="acctno" value="$N"   /> No   -->
			  </td> 
			 	 
			 <td class="txt"> Valid From  <span class="mand">*</span> </td> 
				 <td> 
		 			<s:textfield name="validfrom" id="validfrom" readonly="true"   style="width:160px"  value="%{creditbean.validfrom}"  />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.incomeform.validfrom,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 		</td>
			   
			 <td class="txt"> Valid To     <span class="mand">*</span>  </td> 
		 	<td> 
	 			<s:textfield name="validto" id="validto"  readonly="true"   style="width:160px"   value="%{creditbean.validto}"  />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.incomeform.validto,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
	 		</td>
	 
		</tr> 
		
		<tr>
		 	<td class="txt"> Branch  <span class="mand">*</span> </td> <td> <s:textfield name="primarybankbranch" id="primarybankbranch" maxlength="32"   value="%{creditbean.primarybankbranch}"  /> </td>
			 <td class="txt"> Debit Acct No.  <span class="mand">*</span> </td> <td> <s:textfield name="primarybankacctno" id="primarybankacctno" maxlength="32"   value="%{creditbean.primarybankacctno}"  /> </td>
			 <td class="txt"> Currency   <span class="mand">*</span>  </td> <td>  <s:select list="%{creditbean.curmasterlist}"  name="primaryacctcur" id="primaryacctcur" listKey="NUMERIC_CODE" listValue="CURRENCY_CODE" headerKey="-1" headerValue="-SELECT-" value="%{creditbean.primaryacctcur}"  /> </td> 
		</tr> 
		
		<tr>
			 <td class="txt"> Pay Details   <span class="mand">*</span>  </td> <td>  <s:select list="%{creditbean.creditmasterlimit}"  name="paytype" id="paytype" listKey="PAYTYPEID" listValue="PAYTYPEVALUE" headerKey="-1" headerValue="-SELECT-"   value="%{creditbean.paytype}"  /> </td>
			 <td class="txt"> Payable Amount  </td> <td> <s:textfield name="payableamt" id="payableamt" maxlength="16" onkeypress="return numerals(event)" value="%{creditbean.payableamt}"   /> </td>
			  <td class="txt"> Pay Day  <span class="mand">*</span>  </td> <td> <s:textfield name="payableday" id="payableday" maxlength="2"  onkeypress="return numerals(event)"    value="%{creditbean.payableday}"  /> </td>
		</tr> 
		
		 
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return customerinfo_checker()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div> 



<!-- --------------------- SUPPLIMENTARY DETAILS----------- -->


<sx:div id="6" name="tab"  label="Supplimentary Card Details" value="6"   disabled="true">
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="supplimentform" onsubmit="return validateSuplimentDet()" name="supplimentform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Supplimentary Details
		<input type="hidden" id="reqfrom" name="reqfrom" value="6" /> 
		<input type="hidden" id="goto" name="goto" value="6" />
		<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
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
		 <td class="txt"> Citizenship / Passport <span class="mand">*</span> </td> <td>
		 <s:select list="%{creditbean.documentlist}"  id="idproof"  name="idproof"  listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" />  
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  /> </td>
	</tr>  
	
	

	<tr>
		 <td class="txt"> Gender <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="supgender" onchange = "getSupRelation(this.value)"  headerKey="-1" headerValue="-SELECT-" name="supgender" value="%{custregbean.gender}"/> </td>
		  <td class="txt"> Relationship <span class="mand">*</span> </td> <td> <s:select  list="%{creditbean.supplimentrelationlist}" id="suprelationship" listKey="RELATIONCODE" listValue="RELATIONDESC" name="suprelationship"  headerKey="-1" headerValue="-SELECT-" /> </td>
		   
	</tr> 
	 
	 <tr >
	 <td colspan="6" style="text-align:center">
	 		<s:submit value="Add"   name="suplimentadd" id="sixtab" onclick="return customerinfo_checker()"/>
	 </td>
	 </tr>
	 
	
	 <tr>
	 	<td colspan="6">
	 		<table border='0' cellpadding='0' cellspacing='0' width='100%'>
	 		 <s:if test="creditbean.hassupliment"> 
	 			<tr><th>First Name</th><th>Middle Name</th><th>Last Name</th><th>Name on Name</th><th> Gender </th><th>DOB</th> 
	 			<th>Nationality</th> <th>Citizenship / Passport </th>  <th>Document Number </th>  <th> Relationship </th>   <th> Delete </th> </tr>
			 </s:if>		
	 		<s:iterator value="creditbean.supplimentlist">
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


</sx:tabbedpanel>

 
</body>