function validateinstorder(){
	var branchid = document.getElementById("branch");
	var products = document.getElementById("products");
	var subproductlist = document.getElementById("subproductlist");
	var count = document.getElementById("count");
	
	
	if( branchid ){ 
			
	if( branchid.value == "-1" ){
			errMessage(branchid,"Please select branch name");
			return false;
		}
	}
	
	if( products.value == "-1" ){
		errMessage(products,"Please select Card");
		return false;
	}
	
	if( subproductlist.value == "-1" ){
		errMessage(subproductlist,"Please select sub product");
		return false;
	} 
	
	if( count.value == "" ){
		errMessage(count,"Enter the No.of cards ");
		return false;
	} 
	 
	alert("processing...");
	parent.showprocessing();
}
function customerinfo_checker()
{
 var firstname = document.custinfoform.firstname;
 if( firstname ){
 if(firstname.value == "")
	 {
		 errMessage(firstname," First Name should not be Empty ");
		 return false;
	 }
 }
 var lastname = document.custinfoform.lastname;
 if( lastname ){
 if(lastname.value == "")
	 {
		 errMessage(lastname,"Last Name Should Not Be Empty");
		 return false;
	 }
 }
var fathername = document.custinfoform.fathername;
if( fathername ){
	if(fathername.value == "")
	 {
		 errMessage(fathername,"Father Name Should Not Be Empty");
		 return false;
	 }
}

var gender = document.custinfoform.gender;
if( gender ){
	 if(gender.value == "-1")
	 {
			 errMessage(gender,"Please Enter Gender");
			 return false;
	
	 }
}

 var mstatus = document.custinfoform.mstatus;
if( mstatus ){
	 if( mstatus.value == "-1")
	 {
		 errMessage(mstatus,"Please Enter  Marital Status");
		 return false;
	 } 
}
	 var  spname = document.custinfoform.spname;
	if( mstatus.value == "M" ){
		 if(spname.value == "")
		 {
			 errMessage(spname,"Please Enter Spouse Name");
			 return false;
		 }
	}
	 
 var occupation  = document.custinfoform.occupation;
 if( occupation ){
 if(occupation.value == "")
 {
	 errMessage(occupation,"Please Enter the Occupation");
	 return false;
 }
 }
 var nationality = document.custinfoform.nationality;
 if( nationality ){
 if( nationality.value == "-1" )
 {
	 errMessage(nationality,"Please Select The Nationality" );
	 return false;
 }
 }
 var Bday = document.custinfoform.dob;
	 if( Bday ){
	 if(Bday.value == "")
	 {
		 errMessage(Bday,"Select Your Date of Birth");
		 return false;
	 }
 }
 var mobileno = document.custinfoform.mobileno;
 if( mobileno ){
 if(mobileno.value == "")
 {
	 errMessage(mobileno,"Enter Your Mobile Number");
	 return false;
 }
 else
 {
	 	if(isNaN(mobileno.value)|| mobileno.value.indexOf(" ")!=-1)
		{
		errMessage(mobileno,"Enter the valid Mobile Number");
		return false;
		}
	 	 
		 
 }
 }

 
 var phoneno = document.custinfoform.phoneno;
 if( phoneno ){
 if(phoneno.value == "")
 {
	 errMessage(phoneno,"Enter Your Phone Number");
	 return false;
 }
 else
 {
	 	if(isNaN(phoneno.value)|| phoneno.value.indexOf(" ")!=-1)
		{
		errMessage(phoneno,"Enter the valid Phone Number");
		return false;
		}
		 
  }
		
 }
 
  var email = document.custinfoform.email;
  if( email ){
	  if(email.value == "")
	  {
		  errMessage(email,"Please Enter E-Mail Address");
		  return false;
	  }
	  else
	  {
			 if( !emailvalidator( email.value ) )
			 {
				 errMessage(email,"InValid E-Mail Address" );
			 	return false;
			 }	
	  }
  }
 
  var reqdocuement = document.custinfoform.reqdocuement;
  if(reqdocuement){
  if(reqdocuement.value == "-1")
  {
 	 errMessage(reqdocuement,"Please Enter Identification Document");
 	 return false;
  } 
  }  
  
 var documentid = document.custinfoform.documentid;
 if( documentid ){
 if(documentid.value == "")
 {
	 errMessage(documentid,"Please Enter Identification Number");
	 return false;
 }
 }

 var s_ques = document.custinfoform.squestion;
 if( s_ques ){
 if(s_ques.value == "-1")
 {
	 errMessage(s_ques,"Please Select Your Question");
	 return false;
 }
 }
 
 var appdate = document.custinfoform.appdate;
 if( appdate ){
 if(appdate.value == "")
 {
	 errMessage(appdate,"Please Enter Application Date");
	 return false;
 }
 }
  
 var appno = document.custinfoform.appno;
 if( appno ){
 if(appno.value == "")
 {
	 errMessage(appno,"Please Enter Application Number");
	 return false;
 }
 }
 
 var q_answer = document.custinfoform.answer;
 if( q_answer ){
 if(q_answer.value == "")
 {
	 errMessage(q_answer,"Enter Your Answer");
	 return false;
 }
 }
var paddress1 = document.custinfoform.resaddress1;
if( paddress1 ){ 
if(paddress1.value == "")
 {
	 errMessage(paddress1,"Enter Your Resident Address1 ");
	 return false;
 }
}
 var paddress2 = document.custinfoform.resaddress2;
 if( paddress2 ){
	 if(paddress2.value == "")
	 {
		 errMessage(paddress2,"Enter Your Resident Address2 ");
		 return false;
	 }
 }
 var chck_same = document.custinfoform.residentreq.checked;
 if(!chck_same)
 {
	 var resaddress1 = document.custinfoform.paddress1;
	 if(resaddress1.value == "")
	 {
		 errMessage(resaddress1,"Enter Your Postal Address1");
		 return false;
	 }
	 var resaddress2 = document.custinfoform.paddress2;
	 if(resaddress2.value == "")
	 {
		 errMessage(resaddress2,"Enter Your  Postal Address2");
		 return false;
	 }	 
 }
 parent.showprocessing();
 return true;
}

function yearvalidation(currid)
{
//alert("calinf fucntio year validation"+currid);
	var today = getTodayDate();
	//alert("today"+today);
	//var dob = document.custinfoform.currid.value;
	var dob = document.getElementById(currid).value;
	//alert("dob"+dob);
	var year = dob.split("-");
	if (year[2] >= "1950")
	{
		//alert("year"+year[2]);
		if ( DateDiff(today, dob ) > 0 ) 
		{
			document.getElementById(currid).value="";
			errMessage(document.getElementById(currid),"Date Selected is invalid" );
			return false;
		}
	}

	else
	{
		document.getElementById(currid).value="";
		errMessage(document.getElementById(currid),"Please Select the Year Less than 1950");
	    return false;	
	}	
	 
return true;
}
function getTodayDate() 
{
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){
		dd='0'+dd;
	} 
	if(mm<10){
		mm='0'+mm;
	} 
	var today = dd+'-'+mm+'-'+yyyy;
	return today;
 
}

function DateDiff( date1, date2 ) 
{
	//alert("date1"+date1);
	//alert("date2"+date2);
    MonthArray = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
	date1 = date1.split("-");
	month1 = ( date1[1]  - 1 );
	//alert("month1 ==> "+month1);
	newdate1 = MonthArray[ month1 ]+", "+ date1[0] + ", "+ date1[2];
	//alert(" newdate1==> "+newdate1);
	date2 = date2.split("-");
	//alert("date2==> "+date2);
	month2 = ( date2[1]  - 1 );
	//alert(" month2==> "+month2);
	newdate2 = MonthArray[ month2 ]+", "+ date2[0] + ", "+ date2[2];
	//alert(" newdate2==> "+newdate2);
	var d1 = new Date(newdate1);
	//alert("d1===> "+d1);
	var d2 = new Date(newdate2);
	//alert("d2 ==> "+d2);
	var t1 = d1.getTime();
	//alert("t1===> "+t1);
	var t2 = d2.getTime();
	//alert("t2===> "+t2);
	//alert("parseInt((t2-t1)/(24*3600*1000))======> "+parseInt((t2-t1)/(24*3600*1000)));
	return parseInt((t2-t1)/(24*3600*1000));
}

function showBInDetails( instid, cardno ){
	//	alert(binvalue);
		 
		var sec_cur = document.getElementById("sec_cur");
		var sec_curamt = document.getElementById("seccur_amt");
		if( cardno != "-1"){
		 
			document.getElementById("cardno").focus();
			url = "getCurrencydetailsInstCardActivateProcess.do?instid="+instid+"&cardno="+cardno;
			result = AjaxReturnValue(url);
			 
			if( result != "" ) {
				var printcode = "";
				var curamt = ""; 
				var curlist = result.split("~");
				for( var x=0; x<curlist.length; x++ ){
					var curcode_desc = curlist[x].split("-"); 
					if( curcode_desc != ""){
						var curcode =  curcode_desc[0].toString();
						var curdesc =  curcode_desc[1].toString();   
						printcode += "<td> <input type='checkbox' value='"+curcode+"' id='"+curdesc+"' name='curcode' onclick='showAmt(this.checked, this.id, this.value)' />   " + curdesc + "  &nbsp;&nbsp; </td>";
						curamt += " <div id='"+curdesc+"div' style='display:none' > Loading Amt " +  curdesc + " <br/> <input  type='text' name='"+curdesc+"amt' id='"+curcode+"amt' maxlength='6'  style='text-align:right;width:150px'/>  <input  type='hidden' name=\'"+curdesc+"minamt\' id='"+curcode+"minamt'  />  <input  type='hidden' name=\'"+curdesc+"maxamt\' id='"+curcode+"maxamt'  /> </div> <br/>";						
					}
				}
				sec_cur.innerHTML= printcode ;
				sec_curamt.innerHTML = curamt;
				
			}else{
				sec_cur.innerHTML='';
				sec_curamt.innerHTML = '';
			}
			
		}else{
			document.getElementById("binspanid").innerHTML='';
			sec_cur.innerHTML='';
			sec_curamt.innerHTML = '';
		}
	}
	
	function showAmt( checkstatus, amtfld, curcode  ){
		var bin = document.getElementById("cardtype");
		var curamt = document.getElementById(curcode+"amt");
		var curmaxamt = document.getElementById(curcode+"maxamt");
		var curminamt = document.getElementById(curcode+"minamt");
		
	 
	 
		amtfldval = document.getElementById(amtfld+"div");
	 
		if( checkstatus ){
			 amtfldval.style.display="block";
			// document.getElementById(curcode+"amt").focus();
			 url = "getCurrencyAmountInstCardActivateProcess.do?bin="+bin.value+"&curcode="+curcode;
			 result = AjaxReturnValue(url);
			 
			minmaxamt = result.split("~"); 
			curamt.value = minmaxamt[0];
			 curminamt.value = minmaxamt[0];
			 
			 curmaxamt.value =  minmaxamt[1];
		}else{
			amtfldval.style.display="none"; 
		}
		 
	}
	
	function getSubprodDet(){
	 
		
		document.getElementById("entrytab").visibility="hidden";
		var cardseqno = document.getElementById("cardno").value;
		if( cardseqno == ""){
			document.getElementById("entrytab").style.visibility="hidden";
			errMessage(document.getElementById("loadamt"), "");
			document.getElementById("loadamt").disabled=true;
			
			document.getElementById("dynacont").innerHTML = "CARD NO COULD NOT BE EMPTY";
			
			return false;
		}
		
		document.getElementById("dynacont").innerHTML = "PROCESSING...";
		 
		var cardno = cardseqno;
		errMessage(document.getElementById("cardno"), " ");
		
		url = "getSubProdDetailsHomeInstCardActivateProcess.do?activetype=CARDBASED&entityno="+cardno;
		 
		result = AjaxReturnValue(url);
		//alert(result);
		document.getElementById("dynacont").innerHTML = result;
		
		/*
		alert(result);
		if( result != "" ){
			 
			document.getElementById("loadamt").disabled=false;
			document.getElementById("order").disabled=false;
			
			 document.getElementById("entrytab").style.visibility="visible";
			
			document.getElementById("loadamt").value = result;
			errMessage(document.getElementById("loadamt"), "");
			showBInDetails(instid,cardseqno);
		}else{
			document.getElementById("loadamt").value = "";
			errMessage(document.getElementById("loadamt"), "Invalid card no");
			document.getElementById("loadamt").disabled=true;
			document.getElementById("order").disabled=true;
			
			document.getElementById("entrytab").style.visibility="hidden";
		}
		 */
		 
	}
	
	function validateActive() {
		//alert("Calinf validateActive");
		var def_loadamt = document.getElementById("defloadamt").value;
		var min_amount = document.getElementById("minamount").value;
		var max_amount = document.getElementById("maxamount").value;
		//alert("defloadamt===> "+def_loadamt);
		//alert("min_amount===> "+min_amount);
		//alert("max_amount===> "+max_amount);
		
		if(parseInt(def_loadamt)<parseInt(min_amount))
		{
			alert("Load Amount Should Not Be Less The Minimum Amount "+min_amount);
			return false;
		}
		if(parseInt(def_loadamt)>parseInt(max_amount))
		{
			alert("Load Amount Should Not Exceed Maxmimum Amount "+max_amount);
			return false;
		}
		
		var curcodelist = document.getElementsByName("curcode");
		for( var i=0; i<curcodelist.length; i++ ){
			if( curcodelist[i].checked ){
				var curcode = curcodelist[i].value;
				
				var givenamt = document.getElementById(curcode+"amt");
				
				var curminamt = document.getElementById(curcode+"minamt");
				var curmaxamt = document.getElementById(curcode+"maxamt");
			 
				 
				 
				if( ! isNaN( givenamt.value ) ){
					 
				 
					if( parseInt(givenamt.value) < parseInt(curminamt.value)    ){
						errMessage( givenamt, "Minmum Amount is " + curminamt.value );
						return false;
					}
					
					if ( parseInt(givenamt.value) > parseInt(curmaxamt.value)    ){
						errMessage(givenamt, "Amount should not be more than " + curmaxamt.value );
						return false;
					} 
					
				}else{
					errMessage( givenamt, "Entered value is invalid ");
					return false;
				}
			}
			 
		}
		
		
		if ( confirm( "Do you want to activate this card ")){ 
			return true;	
		}else{
			
			return false;
		}
		
 	}
	
	 function selectallvalidation(){
			//alert("welcome");
			valid=true;
			var orderref = null; 
			var orderreflen = document.getElementsByName("instorderrefnum");
			var oneselected = false; 
				for(var i=1;i<=orderreflen.length;i++)
				{ 
					var orderid = "orderrefnum"+i;
				 
					orderref = document.getElementById(orderid); 
					if(orderref.checked==true)
					{
						oneselected = true;	 
						break; 
					} 
				} 
				if(!oneselected)
					{
					errMessage(orderref,"Select any one Order");
					valid= false;
					return false;
					}
			parent.showprocessing();
			return valid;
		 }
	 
	 function selectallvalidationpre(){
			//alert("welcome");
			valid=true;
			var orderref = null; 
			var orderreflen = document.getElementsByName("instorderrefnum");
			var oneselected = false; 
				for(var i=1;i<=orderreflen.length;i++)
				{ 
					var orderid = "orderrefnum"+i;
				 
					orderref = document.getElementById(orderid); 
					if(orderref.checked==true)
					{
						oneselected = true;	 
						break; 
					} 
				} 
				/*if(!oneselected)
					{
					errMessage(orderref,"Select any one Order");
					valid= false;
					return false;
					}*/
			parent.showprocessing();
			return valid;
		 }
	
	
	