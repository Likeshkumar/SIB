//=========================== PERSONAL CARD ORDER JSP VALIDATION STARTS=======================

function Gettingsubproduct()
{
	//var cardtype="940401666";
	var cardtype=(document.getElementById('cardtype'));
    if(cardtype.value == "940401999" ){
		
		document.getElementById("vehicleno").style.display = 'none';
		
	}else{
		
		document.getElementById("vehicleno").style.display = 'block';
	}
	//alert("THe Card Type is ---> "+cardtype);
	if(cardtype == "-1")
	{
		errMessage( cardtype, "Please Select Card " );
		return false;
	}
	else
	{
		var url="ajaxgettingpersonalsubproducts.do?prodid="+cardtype.value;
		var response=AjaxReturnValue(url);
		document.getElementById('ajax').innerHTML = response;
		document.getElementById('subname').innerHTML = "Select Sub Product";
		return true;
	}
	
}



function getClientname(custid){

//var custno = document.getElementById("custno");
var result;
	var url="getClientDetails.do?custid="+custid;
result = AjaxReturnValue(url).split("~");	 
clientname  	 = result[0];
subprodid  = result[1];
//alert("--clientname --"+clientname);
//alert(" -- odometerpresent -- "+odometerpresent);
document.getElementById("encode").value = clientname;
document.getElementById("subproductlist").value  = subprodid;
} 

function Gettingsubproductlist(cardtype)
{	
	var parentcard=(document.getElementById('cardtype'));
	//alert("THe Card Type is ---> "+cardtype.value);
	if(parentcard.value == "-1")
	{
		errMessage( parentcard, "Please Select Card " );
		return false;
	}
	else
	{			
		var url="getPersonalSubProductetailsReportgenerationAction.do?prodid="+parentcard.value+"&cardtype="+cardtype;
		var response=AjaxReturnValue(url);
		document.getElementById('ajax').innerHTML = " : "+response;
		//document.getElementById('subname').innerHTML = "Select Product";
		document.getElementById('subproductlist').onchange = "";
		var list = document.getElementById('subproductlist');		
		return true;
	}
}

function subprodcalaings()
{
	alert("Calling subprodcalaings");
	var subProduct=document.personalorderform.subproductlist.options[document.personalorderform.subproductlist.selectedIndex].value;
	var productid=(document.getElementById('cardtype').value);
	alert("subProduct==> "+subProduct);
	alert("productid==> "+productid);
	
	if (subProduct == "-1")
	{
	  	document.personalorderform.Count.value = "";
	  	document.personalorderform.emposs.value = "";
	  	errMessage(document.getElementById('subproductlist'), "Please Select Product" );
		return false;
	}
	else
	{
		if(productid == "-1")
		{
			errMessage(document.getElementById('cardtype'), "Please Select Card" );
			////alert("Plesee Select Card TYpe ----> ");
			return false;
		}
		//alert("subProduct====> "+subProduct+"  productid===> "+productid);		
		var url="ajaxgettingpersonalsubproductscount.do?subprodid="+subProduct+"&productid="+productid;
		//alert("URL is ==>"+url);
		var response=AjaxReturnValue(url);
		alert("REsponse ==> "+response);
		 if(response == "N")
		 {	
			 var table=document.getElementById("maxcount");
			 var rowcount = table.rows.length;
			 alert("NO CUSTOMRT REGISTRATIOn "+rowcount);
			 var count = table.getElementsByTagName("tr");
			 var row7=table.insertRow(count.length);
			 var cell71=row7.insertCell(0);
			 var cell72=row7.insertCell(1);
			 cell71.innerHTML="";
			 cell72.innerHTML="<input type='hidden' name='appdate' id='appdate' value='01-01-1900'>";
			 var row8=table.insertRow(count.length);
			 var cell81=row8.insertCell(0);
			 var cell82=row8.insertCell(1);
			 cell81.innerHTML="";
			 cell82.innerHTML="<input type='hidden' name='appno' id='appno' value='000000000000'>";
			 document.getElementById("cust_reg_req").value="N";
			 document.getElementById("next_process").value=" Order ";
		 }
		 else
		 {
			 var table=document.getElementById("maxcount");
			 var rowcount = table.rows.length;
			 alert("Total Rows isn==> "+rowcount);
			 var count = table.getElementsByTagName("tr");
			 var row7=table.insertRow(count.length);
			 var cell71=row7.insertCell(0);
			 var cell72=row7.insertCell(1);
			 cell71.innerHTML="Application Date";
			 cell72.innerHTML="<input type='text' name='appdate' id='appdate'  readonly='readonly' onchange='return yearvalidation(this.id);' style='width:200px'><img src='style/images/cal.gif' id='image' onclick=\"displayCalendar(document.personalorderform.appdate,'dd-mm-yy',this);\" title='Click Here to Pick up the date' border='0' width='15' height='17'>";
			 var row8=table.insertRow(count.length);
			 var cell81=row8.insertCell(0);
			 var cell82=row8.insertCell(1);
			 cell81.innerHTML="Application No";
			 cell82.innerHTML="<input type='text' name='appno' id='appno'>";
		 }
		return true;
	}
	
}

function copytoEncodename(event,id){
	var unicode=evt.charCode? evt.charCode : evt.keyCode;
	//alert(unicode);
	if(((unicode >= 65) && (unicode <= 90)) || (unicode == 46) || (unicode == 8) || (unicode == 9) || (unicode == 32) || (unicode == 35) || (unicode == 36) || (unicode == 37) || (unicode == 39) || ((unicode >= 97) && (unicode <= 122)))
	{
		//alert("document.getElementById(\"emposs\").value==>"+document.getElementById("emposs").value);
		document.getElementById("encode").value = document.getElementById("emposs").value;
		return true;
	}
	else
	{
		unicode=0;
		return false;
	}
}

function validateValues()
{
	//alert("Validate Value Function");
	var branchcode = document.personalorderform.branchcode;
	var Product=document.personalorderform.cardtype.options[document.personalorderform.cardtype.selectedIndex];
	var subProduct=document.personalorderform.subproductlist.options[document.personalorderform.subproductlist.selectedIndex];
	var count=document.personalorderform.Count;
	var e_ename = document.personalorderform.emposs;
	var encodename = document.personalorderform.encode;
	var cinno = document.personalorderform.custno;
	//alert("encodename ===> "+encodename.value+" CIN NO ====> "+cinno.value);
	//var cust_reg = document.getElementById("cust_reg_req").value;
	//alert("THIS IS REQUIRED VALUES --> "+cust_reg);
	if(branchcode.value == "-1")
	{
		errMessage(branchcode,"The Please Select The Branch ");
		  	return false;
 	}
	if(Product.value == "-1")
	{
		errMessage(Product,"The Please Select Card ");
	  	return false;
 	}
    if(subProduct.value == "-1")
	{
    	errMessage(subProduct,"The Please Select Product");
	  	document.personalorderform.Count.value = "";
	  	document.personalorderform.emposs.value = "";
		return false;
 	}
    
	if(count.value == "")
	{
		errMessage(count,"Please Enter the Quantity");
		return false;
	}
	/*else
		{
		var p_count = document.personalorderform.maxpcount.value;
		//var n_count = document.personalorderform.maxncount.value;
		var ordertype = document.personalorderform.ordertype.value;
		////alert("p_count   "+p_count+" ordertype  "+ordertype);
		if(trim(ordertype) == "Y" || trim(ordertype) == "B")
		{
			////alert("count===> "+count.value);
			var fistchar = count.value.substr(0,1);
			////alert("  fistchar "+fistchar);
			if(fistchar != "0")
			{
				var ordercount = parseInt(count.value);
				if(ordercount>p_count)
				{
					document.personalorderform.Count.value="";
					errMessage(count,"Please Enter the Quantity '"+p_count+"' or Below");
					return false;
				}
			}
			else{
				document.personalorderform.Count.value="";
				errMessage(count,"Please Enter the Quantity Without Start with Zero");
				return false;
			}
		}
		}*/
	
	if(e_ename.value == "")
	{
		errMessage(e_ename,"Please Enter the Emboss Name");
		return false;
	}
	if(encodename.value == "")
	{
		errMessage(encodename,"Please Enter the Encoding Name");
		return false;
	}
	if(cinno.value == "")
	{
		errMessage(cinno,"Please Enter the Customer Number ");
		return false;
	}
	/*
	var appdate = document.getElementById("appdate");
	var appnum = document.getElementById("appno");
	if(appdate.value == "")
	{
		errMessage(appdate,"Please Select The Application Date");
		return false;
	}
	if(appnum.value == "")
	{
		errMessage(appnum,"Please Enter the Application Number ");
		return false;
	}
	*/
	
	return true;
}
function forwardtoCardOrderPage()
{
	if ( confirm("Do You Want To Cancel")  ) 
	{
		window.location="RedirectLink.do";
	}else 
	{
		return false;
	}
}

//================= CUTOMER DETAILS STARTS =======================


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
	////alert("date1"+date1);
	////alert("date2"+date2);
    MonthArray = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
	date1 = date1.split("-");
	month1 = ( date1[1]  - 1 );
	////alert("month1 ==> "+month1);
	newdate1 = MonthArray[ month1 ]+", "+ date1[0] + ", "+ date1[2];
	////alert(" newdate1==> "+newdate1);
	date2 = date2.split("-");
	////alert("date2==> "+date2);
	month2 = ( date2[1]  - 1 );
	////alert(" month2==> "+month2);
	newdate2 = MonthArray[ month2 ]+", "+ date2[0] + ", "+ date2[2];
	////alert(" newdate2==> "+newdate2);
	var d1 = new Date(newdate1);
	////alert("d1===> "+d1);
	var d2 = new Date(newdate2);
	////alert("d2 ==> "+d2);
	var t1 = d1.getTime();
	////alert("t1===> "+t1);
	var t2 = d2.getTime();
	////alert("t2===> "+t2);
	////alert("parseInt((t2-t1)/(24*3600*1000))======> "+parseInt((t2-t1)/(24*3600*1000)));
	return parseInt((t2-t1)/(24*3600*1000));
}

function yearvalidation(currid)
{
////alert("calinf fucntio year validation"+currid);
	var today = getTodayDate();
	////alert("today"+today);
	//var dob = document.custinfoform.currid.value;
	var dob = document.getElementById(currid).value;
	////alert("dob"+dob);
	var year = dob.split("-");
	/*if (year[2] >= "1990")
	{
		////alert("year"+year[2]);
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
		errMessage(document.getElementById(currid),"Please Select the Year Less than 1990");
	    return false;	
	}*/
	
	
	
	
	
	return true;
}

function readiobuttonaction()
{
	////alert("Insdie the Radio Buttom acction");
	 var mstat = document.custinfoform.unmarried.checked;
	 if(mstat){
		 document.custinfoform.spname.value = "";
		 document.custinfoform.spname.disabled = true;
		 //document.orderform.spname.focus();
	 }
	 else{
		 document.custinfoform.spname.disabled = false;
	 }	 
	 return false;
	
}



function disabletheResidetnaddress()
{
	////alert("Calling The Disbale Text box");
	var check = document.custinfoform.same.checked;
	////alert("  check  "+check);
	if(check)
		{
		if(document.custinfoform.paddress1.value )
			{
				errMessage(document.custinfoform.paddress1,"Enter Postal Address");
				return false;
			}
		}
	
	
	
	
	if(!check)
	{
			////alert(" In Side IF ");
			//document.orderform.raddress1.value = document.orderform.paddress1.value ;
			document.custinfoform.paddress1.value = "";
			document.custinfoform.paddress1.disabled = false;
			/*document.custinfoform.raddress2.value = "";
			document.custinfoform.raddress2.disabled = false;
			document.custinfoform.raddress3.value = "";
			document.custinfoform.raddress3.disabled = false;
			document.custinfoform.raddress4.value = "";
			document.custinfoform.raddress4.disabled = false;*/
	}
	else
	{
		////alert(" In Side Else ");
		 
		document.custinfoform.paddress1.value = document.custinfoform.raddress1.value ;
		/*document.custinfoform.paddress1.value = document.custinfoform.raddress2.value; 
		document.custinfoform.paddress1.value = document.custinfoform.raddress3.value ;
		document.custinfoform.paddress1.value = document.custinfoform.raddress4.value ;*/
		
		
		document.custinfoform.raddress1.paddress1 = true;
		/*document.custinfoform.raddress2.paddress1 = true;
		document.custinfoform.raddress3.paddress1 = true;
		document.custinfoform.raddress4.paddress1 = true;*/
	}
}

function setMerchantCard( regtype ){
	 var father_name = document.custinfoform.f_name;
	 var mother_name= document.custinfoform.m_name;
	 var e_mail = document.custinfoform.email;
	 var phone_no = document.custinfoform.phone;
	 var id_num = document.custinfoform.idno;
	if( regtype == "$MC"){ 
		document.getElementById("corp").style.display = 'block';
		document.getElementById("personal").style.display = 'none';
		document.getElementById("id").style.display = 'none';
		document.getElementById("photo").style.display = 'none';
		document.getElementById("sign").style.display = 'none';
		document.getElementById("reg").style.display = 'block';
		document.getElementById("pin").style.display = 'block';
		document.getElementById("vat").style.display = 'block';
		father_name.value = "NA";
		mother_name.value = "NA";
		//e_mail.value = "NA@NA.COM";
		//phone_no.value = "0000"; 
		//id_num.value = "0000";
	}else if(regtype == "$FC"){
		document.getElementById("corp").style.display = 'none';
		document.getElementById("personal").style.display = 'block';
		document.getElementById("id").style.display = 'block';
		document.getElementById("photo").style.display = 'block';
		document.getElementById("sign").style.display = 'block';
		document.getElementById("reg").style.display = 'none';
		document.getElementById("pin").style.display = 'none';
		document.getElementById("vat").style.display = 'none';
		father_name.value = "";
		mother_name.value = "";
		e_mail.value = "";
		phone_no.value = "";
		id_num.value = "";
	}else{
		
		
		
	}
	
	
	return false;
}

function customerinfo_checker()
{
	var reg_type = document.getElementById("regtype");
	var branchcode = document.getElementById("branchcode");
	var cardtype = document.getElementById("cardtype");
	var subproductlist = document.getElementById("subproductlist");
	var Count = document.getElementById("Count");
	var emposs = document.getElementById("emposs");
	var encode = document.getElementById("encode");
	var appdate = document.getElementById("appdate");
	var appno = document.getElementById("appno");
	var remark = document.getElementById("remark");
	 var f_name = document.custinfoform.fname;
	 var l_name = document.custinfoform.lname;
	 var father_name = document.custinfoform.f_name;
	 var mother_name= document.custinfoform.m_name;
	 var marri = document.custinfoform.mstatus;
	 var occupation  = document.custinfoform.work;
	 var nations = document.custinfoform.nations;
	 var Bday = document.custinfoform.dob;
	 var e_mail = document.custinfoform.email;
	 var ce_mail = document.custinfoform.cemail;
	 var mobile_no = document.custinfoform.mobile;
	 var cmobile_no = document.custinfoform.cmobile;
	 var phone_no = document.custinfoform.phone;
	 var id_doc = document.custinfoform.iddoc;
	 var id_num = document.custinfoform.idno;
	 var p_address1 = document.custinfoform.paddress1;
	 var p_address2 = document.custinfoform.paddress2;
	 var chck_same = document.custinfoform.same.checked;
 
	 
	    var cname = document.getElementById("cname");
		var pinno = document.getElementById("pinno");
		var regno = document.getElementById("regno");
		var ftitle = document.getElementById("ftitle");
		var fperson = document.getElementById("fperson");
		var position1 = document.getElementById("position1");
		var stitle = document.getElementById("stitle");
		var sperson = document.getElementById("sperson");
		var position2 = document.getElementById("position2");
		var bustype = document.getElementById("bustype");
		var natofbuss = document.getElementById("natofbuss");
		var vatno = document.getElementById("vatno");
		var d1title = document.getElementById("d1title");
		var nameofdir1 = document.getElementById("nameofdir1");
		var nofn1 = document.getElementById("nofn1");
		var d2title = document.getElementById("d2title");
		var nameofdir2 = document.getElementById("nameofdir2");
		var nofn2 = document.getElementById("nofn2");
		var d3title = document.getElementById("d3title");
		var nameofdir3 = document.getElementById("nameofdir3");
		var nofn3 = document.getElementById("nofn3");
		var d4title = document.getElementById("d4title");
		var nameofdir4 = document.getElementById("nameofdir4");
		var nofn4 = document.getElementById("nofn4");
		var d5title = document.getElementById("d5title");
		var nameofdir5 = document.getElementById("nameofdir5");
		var nofn5 = document.getElementById("nofn5");
		
	 
	 //Corporate Information
	 
	 if(reg_type == "$MC"){
		
		  if(cname.value == "")  {  errMessage(cname," Company Name Should Not Be Empty ");  return false;  }


		  if(pinno.value == "") {  errMessage(pinno,"PIN NO Should Not Be Empty");  return false; }
		  if(regno.value == "")  {  errMessage(regno," Regno Should Not Be Empty ");  return false;  }


		  if(ftitle.value == "") {  errMessage(ftitle,"Title Should Not Be Empty");  return false; }
		  if(fperson.value == "")  {  errMessage(fperson," First Contact Person Name Should Not Be Empty ");  return false;  }
		  if(position1.value == "") {  errMessage(position1,"First Contact Person Position Should Not Be Empty");  return false; }
		  
		  if(stitle.value == "") {  errMessage(stitle,"Title Should Not Be Empty");  return false; }
		  if(sperson.value == "")  {  errMessage(sperson," Second Contact Person Name Should Not Be Empty ");  return false;  }
		  if(position2.value == "") {  errMessage(position2,"Second Contact Person Position Should Not Be Empty");  return false; }

		  if(bustype.value == "" || bustype.value == "-1" ) {  errMessage(bustype,"Bussiness Type Should Not Be Empty");  return false; }
		  if(natofbuss.value == "")  {  errMessage(natofbuss,"  Nature of Bussiness Should Not be Empty ");  return false;  }


		  if(vatno.value == "") {  errMessage(vatno,"VAT NO  Should Not Be Empty");  return false; }
		  if(d1title.value == "")  {  errMessage(d1title," Title Should Not Be Empty ");  return false;  }


		  if(nameofdir1.value == "") {  errMessage(nameofdir1,"Name Of Director Should Not Be Empty");  return false; }
		  if(nofn1.value == "")  {  errMessage(nofn1," Nationality for Director Should Not Be Empty ");  return false;  }


		  if(d2title.value == "")  {  errMessage(d2title," Title Should Not Be Empty ");  return false;  }


		  if(nameofdir2.value == "") {  errMessage(nameofdir2,"Name Of Director Should Not Be Empty");  return false; }
		  if(nofn2.value == "")  {  errMessage(nofn2," Nationality for Director Should Not Be Empty ");  return false;  }
		 
		 
		 
		 
		 
	 }
	 
	// alert("regtype :" + regtype.value);
	
	 
	if( reg_type.value == "-1"){
		errMessage(reg_type,"Select Registeration type" );
	    return false;
	} 
	
	
	if(branchcode)
	{
		if (branchcode.value == "-1" ) { errMessage(branchcode,"Select Branch" );  return false; }		  
	}
	if(cardtype)
	{
		if (cardtype.value == "-1" ) { errMessage(cardtype,"Select Cardtype" ); return false; }		  
	}
	if(subproductlist)
	{
		if (subproductlist.value == "-1" )  {  errMessage(subproductlist,"Select subproductlist" );  return false; }		  
	}
	if(Count)
	{
		if (Count.value == "" )  { errMessage(Count,"Select Count" );  return false; }		  
	}
	if(emposs)
	{
		if (emposs.value == "" )  { errMessage(emposs,"Enter Name Of The Card" );  return false; }		  
	}
	if(encode)
	{
		if (encode.value == "" )  { errMessage(encode,"Enter Encode Name" );  return false; }		  
	}

	if(remark)
	{
		if (remark.value == "" )  { errMessage(remark,"Enter remark" );  return false; }		  
	}	
	
	

  if(f_name.value == "")  {  errMessage(f_name," First Name Should Not Be Empty ");  return false;  }


 if(l_name.value == "") {  errMessage(l_name,"Last Name Should Not Be Empty");  return false; }
 
 //if(father_name.value == "") {  errMessage(father_name,"Father Name Should Not Be Empty");  return false;  }
 
 /*if(marri[0].checked)  {
	 var  spouse_name = document.custinfoform.spname;
	 if(spouse_name.value == "")  {  errMessage(spouse_name,"Please Enter Spouse Name");  return false;  }
 }*/
 
 /*if(occupation.value == "")  {  errMessage(occupation,"Please Enter the Occupation");  return false;  }*/
 
 var uploadreq = document.getElementById("uploadreq").value;
 if(reg_type == '$FC'){
			 if(nations.value == "-1")  { errMessage(nations,"Please Select The Nationality" );  return false; }
			 
			 if(Bday.value == "")  {  errMessage(Bday,"Select Your Birth Date");  return false;  }
			 
			/* if (Bday.value.match(/^(?:(0[1-9]|1[012])[\- \/.](0[1-9]|[12][0-9]|3[01])[\- \/.](19|20)[0-9]{2})$/)){ 
			}else{
				errMessage(Bday,"InValid Date format" );
				    return false;
			}*/
			 
			 
			 if(e_mail.value != "")  {    if( !emailvalidator( e_mail.value ) )  {  errMessage(e_mail,"InValid E-Mail Address" ); return false; }  }
			 
			 if(e_mail.value != ce_mail.value)  {  errMessage(e_mail,"Email And Confirm Email mismathched");  return false;  }
			
			 if(mobile_no.value == "")  {  errMessage(mobile_no,"Enter Your Mobile Number");  return false;  }
			 
			 if(cmobile_no.value != mobile_no.value)  {  errMessage(mobile_no,"Mobile No And Confirm Mobile No Mismathched");  return false;  }
			 
			
			 
			 //if(phone_no.value == "")  {  errMessage(phone_no,"Enter Your Phone Number");  return false; }
			 
			 if(id_doc.value == "-1")  {  errMessage(id_doc,"Please Select Identification Document");  return false;  } 
			
			 if(id_num.value == "")  {  errMessage(id_num,"Please Enter Identification Number");  return false;  }
			
			 if(appdate) {
					if (appdate.value == "" )  { errMessage(appdate,"Enter Application date" ); return false; }		  
			 }
			 
			 if(appno) {
					if (appno.value == "" )  { errMessage(appno,"Enter Application Number" );   return false; }		  
			 }
			 
 }		 
 
 if( uploadreq == "1"){
		 var photoupload = document.getElementById("uploadphoto"); 
		 var signatureupload = document.getElementById("uploadsignature");
		 var idproofupload = document.getElementById("uploadidproof");
		 if( photoupload ){
			 if( photoupload.value==""){ errMessage(photoupload, "Please upload customer photo");return false;}
		 }
		 
		 if( signatureupload ){
			 if( signatureupload.value==""){ errMessage(signatureupload, "Please upload Signature");return false;}
		 }
		 
		 if( idproofupload ){
			 if( idproofupload.value==""){ errMessage(idproofupload, "Please upload ID-Proof");return false;}
		 }
	 }
	 
 
 //if(raddress1.value == "")  {  errMessage(raddress1,"Enter Your Resident Address ");  return false;  }
  
//if(p_address2.value == "")  {  errMessage(p_address2,"Enter Your Postal Address2 ");  return false;  }

/* if(!chck_same)  {
	// var r_address1 = document.custinfoform.raddress1;
	 //if(r_address1.value == "")  { 	 errMessage(r_address1,"Enter Your Resident Address1");  return false;  }
	 var r_address2 = document.custinfoform.raddress2;
	 if(r_address2.value == "")  {  errMessage(r_address2,"Enter Your Resident Address2");  return false;  }	 
 } 
 */
 return true;
}


function viewfilterValidation(){
	////alert("viewfilterValidation");
	var branchcode = document.getElementById("branchcode");
	var card=document.getElementById("cardtype");
	var fdate = document.getElementById("fromdate").value;
	var tdate = document.getElementById("todate").value;
	
	
	////alert("branchcode"+branchcode.value+" card==>"+card.value+"  fdate==> "+fdate.value+" tdate==> "+tdate.value);
	if(branchcode.value == "-1")
	{
		 errMessage(branchcode,"Select The Branch ");
		 return false;
	}if(card.value == "-1")
	{
		 errMessage(branchcode,"Select Card");
		 return false;
	}if(fdate == "")
	{
		 errMessage(branchcode,"Select From Date ");
		 return false;
	}if(tdate == "")
	{
		 errMessage(branchcode,"Select To Date ");
		 return false;
	}
	var arrFrom=fdate.split('-');
	////alert("arrFrom"+arrFrom);
	var arrTo=tdate.split('-');
	////alert(arrTo);

	var strFromDate=arrFrom[0];
	////alert("Date"+strFromDate);
	var strFromMonth=arrFrom[1];
	////alert("Month"+strFromMonth);
	var strFromYear=arrFrom[2];
	////alert("Year"+strFromYear);

	var strToDt=arrTo[0];
	////alert(strToDt);
	var strToMonth=arrTo[1];
	////alert(strToMonth);
	var strToYear=arrTo[2];
	////alert(strToYear);


	if(parseInt(arrFrom[0])<10)
	{
	strFromDate=arrFrom[0].replace('0','');
	////alert("From Date less than 10==>"+strFromDate);
	}
	if(parseInt(arrFrom[1])<10)
	{
	strFromMonth=arrFrom[1].replace('0','');
	////alert("From month less than 10==>"+strFromMonth);
	}
	
	
	if(parseInt(arrTo[0])<10)
	{
	strToDt=arrTo[0].replace('0','');
	////alert("To-Date less than 10==>"+strToDt);
	}
	if(parseInt(arrTo[1])<10)
	{
	strToMonth=arrTo[1].replace('0','');
	////alert("To-Date less than 10==>"+strToMonth);
	}

	var mon1 = parseInt(strFromMonth)-1;
	////alert("month=="+mon1);
	var dt1 = parseInt(strFromDate);
	////alert("date=="+dt1);
	var yr1 = parseInt(arrFrom[2]);
	////alert("year=="+yr1);

	var mon2 = parseInt(strToMonth)-1;
	////alert("month=="+mon2);
	var dt2 = parseInt(strToDt);
	////alert("date=="+dt2);
	var yr2 = parseInt(arrTo[2]);
	////alert("year=="+yr2);

	////alert("month=="+mon1);

	var from_Date = new Date(yr1, mon1, dt1);
	////alert("date1==>"+date1);
	var to_Date = new Date(yr2, mon2, dt2);
	////alert("date2==>"+date2);

		if(to_Date < from_Date)
		{
		errMessage(document.getElementById("fromdate"),"From date should be lesser than to-date");
		return false;
		}
	
	return true;
}

function Datecompare()
{
	var fdate = document.getElementById("fromdate").value;
	////alert("fdate"+fdate);
	var tdate = document.getElementById("todate").value;
	////alert("tdate"+tdate);
	
var arrFrom=fdate.split('-');
////alert("arrFrom"+arrFrom);
var arrTo=tdate.split('-');
////alert(arrTo);

var strFromDate=arrFrom[0];
////alert("Date"+strFromDate);
var strFromMonth=arrFrom[1];
////alert("Month"+strFromMonth);
var strFromYear=arrFrom[2];
////alert("Year"+strFromYear);

var strToDt=arrTo[0];
////alert(strToDt);
var strToMonth=arrTo[1];
////alert(strToMonth);
var strToYear=arrTo[2];
//alert(strToYear);


if(parseInt(arrFrom[0])<10)
{
strFromDate=arrFrom[0].replace('0','');
////alert("From Date less than 10==>"+strFromDate);
}
if(parseInt(arrFrom[1])<10)
{
strFromMonth=arrFrom[1].replace('0','');
////alert("From month less than 10==>"+strFromMonth);
}


if(parseInt(arrTo[0])<10)
{
strToDt=arrTo[0].replace('0','');
////alert("To-Date less than 10==>"+strToDt);
}
if(parseInt(arrTo[1])<10)
{
strToMonth=arrTo[1].replace('0','');
////alert("To-Date less than 10==>"+strToMonth);
}

var mon1 = parseInt(strFromMonth)-1;
////alert("month=="+mon1);
var dt1 = parseInt(strFromDate);
////alert("date=="+dt1);
var yr1 = parseInt(arrFrom[2]);
////alert("year=="+yr1);

var mon2 = parseInt(strToMonth)-1;
////alert("month=="+mon2);
var dt2 = parseInt(strToDt);
////alert("date=="+dt2);
var yr2 = parseInt(arrTo[2]);
////alert("year=="+yr2);

////alert("month=="+mon1);

var date1 = new Date(yr1, mon1, dt1);
////alert("date1==>"+date1);
var date2 = new Date(yr2, mon2, dt2);
////alert("date2==>"+date2);

//var today = new Date();
////alert("today"+today);

	if(date2 < date1)
	{
	errMessage(document.getElementById("fromdate"),"From date should be lesser than to-date");
	return false;
	}
}



var popupWindow = null;

function getcustomerinfo(refno)
{
	popupWindow = window.open('viewpersonalordercustomerinfo.do?orderrefnum='+refno,'','left=350,top=150,width=600,height=400,location=no,menubar=no,menu=no,status=no,scrollbars=yes,resizable=yes,toolbar=no,titlebar=0,fullscreen = yes,directories=no');
	/*var url = "viewpersonalordercustomerinfo.do?orderrefnum="+refno;
	result = AjaxReturnValue(url);
	document.getElementById('cutomer_details').innerHTML = response;*/
	return true;
}

function parent_disable() {
	if(popupWindow && !popupWindow.closed)
	popupWindow.focus();
	//document.getElementById("disablediv").disable = true;
	}



function enableSpoucenamefiled(vlaues)
{
	////alert("Callinfg Enavble Function "+vlaues);
	if(vlaues == "M"){
		////alert("Enable Spouse Name ");
		document.getElementById("spname").disabled = false;
		
	}else{
		////alert("Disable Spouse Name ");
		document.getElementById("spname").disabled = true;
		document.getElementById("spname").value = "";
	}
}


// ================= CUTOMER DETAILS ENDS =======================

// Selecte ALL Validation  Starts 
function selectallvalidation(){
	//alert("welcome");
	valid=true;
	var orderref = null;
	//alert("orderref==> "+orderref.value);
	var orderreflen = document.getElementsByName("personalrefnum");
	//alert(orderreflen.length);
	var oneselected = false;
	//alert("orderreflen==> "+orderreflen.length);
	for(var i=0;i<orderreflen.length;i++)
	{ 
		//var orderid = "personalrefnum"+i;
		//alert("orderid==> "+orderreflen[i]);
		//orderref = document.getElementById(orderid);
		//alert("orderref==> "+orderref);
		orderref = orderreflen[i];
		//alert(orderref.checked);
		if(orderref.checked==true)
		{
			oneselected = true;	
			//alert("One checkbox is selected Breaking for loop");
			break;

		}
			
	}
	//alert(orderref);
	//alert("Outside For LOOP if(!oneselected)");
	//alert(orderreflen[0]);
	if(!oneselected)
	{
		errMessage(orderreflen[0],"Select any one Order");
		return false;
	}
	return valid;
 }

//Added By Senthil

function selectallvalidationFromIndex(){
	//alert("welcome");
	valid=true;
	var orderref = null;
	//alert("orderref==> "+orderref.value);
	var orderreflen = document.getElementsByName("personalrefnum");
	//alert(orderreflen.length);
	var oneselected = false;
	//alert("orderreflen==> "+orderreflen.length);
	for(var i=0;i<orderreflen.length;i++)
	{ 
		var orderid = "personalrefnum"+i;
		//alert("orderid==> "+orderreflen[i]);
		orderref = document.getElementById(orderid);
		//alert("orderref==> "+orderref);
		orderref = orderreflen[i];
		//alert(orderref.checked);
		if(orderref.checked==true)
		{
			oneselected = true;	
			//alert("One checkbox is selected Breaking for loop");
			break;

		}
			
	}
	//alert(orderref);
	//alert("Outside For LOOP if(!oneselected)");
	//alert(orderreflen[0]);
	if(!oneselected)
	{
		errMessage(orderreflen[0],"Select any one Order");
		return false;
	}
	return valid;
 }



