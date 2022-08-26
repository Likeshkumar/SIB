

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

function validatedeleteorderrefnum()
{
	var refnum = document.nonpersonaldetails.delrefnum.value;
	
	
	if(refnum == "")
	{
		alert("Please Enter the Reference Number");
		document.getElementById("delrefnum").focus();
		return false;
	}

	return true;
	
}
function confirmdeleteorder()
{
	if ( confirm("Do You Want To Delete")  ) 
	{
		return true;
	}
	else
	{
		return false;
	}
}
function Backtoeditpage()
{
	window.location="EditinstcardorderCardorderAction.do";
}
function Backtopersonaleditpage()
{
	window.location="EditpersonalcardorderCardorderAction.do";
}
function Backtodeleteorder()
{
	window.location="DeleteinstcardorderLink.do";
}
//=========================== PERSONAL CARD ORDER CUSTOMER INFORMATION VALIDATION START=======================



function readiobuttonactionForLimit()
{
	//alert("Insdie the Radio Buttom acction");
	 var mstat = document.instcardactiveform.cardbased.checked;
	 if(mstat)
	 {
		 
	 }
	 else
	 {
		
	 }	 
	 return false;
	
}







//=========================== PERSONAL CARD ORDER JSP VALIDATION STARTS=======================
function validateValues()
{
	var Product=document.personalorderform.products.options[document.personalorderform.products.selectedIndex].value;
	var subProduct=document.personalorderform.subproductlist.options[document.personalorderform.subproductlist.selectedIndex].value;
	var count=document.personalorderform.Count.value;
	var e_ename = document.personalorderform.emposs.value;
	var branchcode = document.personalorderform.branchcode.value;
	if(branchcode == "-1")
	{
		alert("The Please Select The Branch ");
		  	return false;
 	}
	if(Product == "-1")
    	{
			alert("The Please Select a Product ");
  		  	return false;
     	}
    if(subProduct == "-1")
	{
	  	alert("The Please Select a Sub-Product");
	  	document.personalorderform.Count.value = "";
	  	document.personalorderform.emposs.value = "";
		return false;
 	}
    
	if(count == "")
		{
			alert("Please Enter the Quantity");
			var txtbox=document.getElementById("Count");
			txtbox.focus();
			return false;
		}
	else
		{
		var p_count = document.personalorderform.maxpcount.value;
		//var n_count = document.personalorderform.maxncount.value;
		var ordertype = document.personalorderform.ordertype.value;
		if(trim(ordertype) == "Y" || trim(ordertype) == "B")
		{

			var fistchar = count.substr(0,1);
			if(fistchar != "0")
			{
				var ordercount = parseInt(count);
				if(ordercount>p_count)
				{
					alert("Please Enter No.of cards '"+p_count+"' or Below");
					document.personalorderform.Count.value="";
					var txtbox=document.getElementById("Count");
					txtbox.focus();
					return false;
				}
			}
			else{
				alert("Please Enter the No.of cards Without Start with Zero");
				document.personalorderform.Count.value="";
				var txtbox=document.getElementById("Count");
				txtbox.focus();
				return false;
			}
		}
		}
	if(e_ename == "")
		{
			alert("Please Enter the Name");
			var ename = document.getElementById("emposs");
			ename.focus();
			return false;
		}
	parent.showprocessing();
	return true;
	
}
function calaings()
{

	var subProduct=document.orderform.subproductlist.options[document.orderform.subproductlist.selectedIndex].value;
	
	
	if (subProduct == "-1")
	{
	  	document.orderform.Count.value = "";
	  	alert("Please Select Sub-Product");
		return false;
	}
	else{
		
		httpObject = getHTTPObjectForBrowser();
		if(httpObject != null)
		{
			httpObject.open("GET", "ajaxgettinginstsubproductscount.do?subprodid="+subProduct, true);
			httpObject.send(null);
			httpObject.onreadystatechange = setinstsubproductcount;
			return true;
		}
		else{
    		alert("Browser Not Support");
    		document.personalorderform.products.options[document.personalorderform.products.selectedIndex].value ="";
    		return false;
		}
		
	}
	return true;

	
}

function setinstsubproductcount()
{
if(httpObject.readyState == 4) 
{
    document.getElementById('maxcount').innerHTML = httpObject.responseText;
}
}

function Gettinginstsubproduct()
{
	
	var productid=(document.getElementById('products').value);
	if(productid == "-1")
	{
		alert("Please Select Product ");
		return false;
	}
   	httpObject = getHTTPObjectForBrowser();
	if (httpObject != null) 
	{
   		
   		httpObject.open("GET", "ajaxgettinginstsubproducts.do?prodid="+productid, true);
		httpObject.send(null);
		httpObject.onreadystatechange = setinstAjaxOutput;
		return true;
	}
	else
	{
		alert("Browser Not Support");
		document.personalorderform.products.options[document.personalorderform.products.selectedIndex].value ="";
		return false;
	}

}



function setinstAjaxOutput()
{
	if(httpObject.readyState == 4) 
	{
	    document.getElementById('ajax').innerHTML = httpObject.responseText;
	    document.getElementById('subname').innerHTML = "Select Sub-Product";
	}
}

function nonpersonlcount()
{
	var subProduct=document.orderform.subproductlist.options[document.orderform.subproductlist.selectedIndex].value;
	
	
	if (subProduct == "-1")
	{
		alert("Please Select Sub-Product");
	  	document.orderform.Count.value = "";
	  	document.orderform.emposs.value = "";
		return false;
	}
	else{
		
		httpObject = getHTTPObjectForBrowser();
		if(httpObject != null)
		{
			httpObject.open("GET", "ajaxgettingnonpersonalsubproductscount.do?subproductid="+subProduct, true);
			httpObject.send(null);
			httpObject.onreadystatechange = setsubproductOutput;
			return true;
		}
		else{
    		alert("Browser Not Support");
    		document.orderform.products.options[document.orderform.products.selectedIndex].value ="";
    		return false;
		}
		
	}
	return true;
}

function Gettingsubproduct()
{
	var productid=(document.getElementById('products').value);
	if(productid == "-1")
	{
		alert("Please Select Product ");
		return false;
	}
	   	httpObject = getHTTPObjectForBrowser();
    	if (httpObject != null) 
    	{
       		
       		httpObject.open("GET", "ajaxgettingpersonalsubproducts.do?prodid="+productid, true);
			httpObject.send(null);
			httpObject.onreadystatechange = setAjaxOutput;
			return true;
    	}
    	else
    	{
    		alert("Browser Not Support");
    		document.personalorderform.products.options[document.personalorderform.products.selectedIndex].value ="";
    		return false;
    	}
}
function getHTTPObjectForBrowser()
{
    if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest) return new XMLHttpRequest();
    else{
       		alert("Browser does not support AJAX...........");
    		return null;
		}
}

function setAjaxOutput()
{
if(httpObject.readyState == 4) 
{
    document.getElementById('ajax').innerHTML = httpObject.responseText;
    document.getElementById('subname').innerHTML = "Select Sub-Product";
}
}
function setsubproductOutput()
{
if(httpObject.readyState == 4) 
{
    document.getElementById('maxcount').innerHTML = httpObject.responseText;
}
}
//=========================== PERSONAL CARD ORDER JSP VALIDATION ENDS=======================


function validatenonpersonalValues()
{
	
	var Product=document.orderform.products.options[document.orderform.products.selectedIndex].value;
	var subProduct=document.orderform.subproductlist.options[document.orderform.subproductlist.selectedIndex].value;
	var count=document.orderform.Count.value;
	
	if(Product == "-1")
    	{
  		  	alert("The Product Select a Product");
    		return false;
     	}
    if(subProduct == "-1")
	{
		  	alert("The Product Select a Sub-Product");
		return false;
 	}
	if(count == "")
	{
		alert("Please Enter the Quantity");
		var txtbox=document.getElementById("Count");
		txtbox.focus();
		return false;
	}
	else
	{
		
	//var p_count = document.orderform.maxpcount.value;
	var n_count = document.orderform.maxncount.value;
	var ordertype = document.orderform.ordertype.value;
	
	if(trim(ordertype) == "N" || trim(ordertype) == "B")
	{

		var fistchar = count.substr(0,1);
		if(fistchar != "0")
		{
			var ordercount = parseInt(count);

			if(ordercount>n_count)
			{
				alert("Please Enter the Quantity '"+n_count+"' or Below");
				document.orderform.Count.value="";
				var txtbox=document.getElementById("Count");
				txtbox.focus();
				return false;
			}
		}
		else{
			alert("Please Enter the Quantity Without Start with Zero");
			document.orderform.Count.value="";
			var txtbox=document.getElementById("Count");
			txtbox.focus();
			return false;
		}
	}
	}
	return true;
}
function BacktoCardOrderPage()
{
	if ( confirm("Do You Want To Go Back")  ) 
	{
		window.location="RedirectLink.do";
	}else 
	{
		return false;
	}
}


function validateordercount()
{
	var order_type=document.editnonpersonal.otype.value;
	//var P_count=document.editnonpersonal.maxpocount.value;
	var N_count=document.editnonpersonal.maxnocount.value;
	var Cuur_count=document.editnonpersonal.ordercount.value;
	var rmarks = document.editnonpersonal.rmark.value;
	if(Cuur_count == "")
	{
		alert("Please Enter the Quantity");
		var txtbox=document.getElementById("ordercount");
		txtbox.focus();
		return false;
	}
	else
	{
		if(Cuur_count == "0")
		{
			
			alert("Please Enter the Valid Quantity");
			var txtbox=document.getElementById("ordercount");
			document.editnonpersonal.ordercount.value="";
			txtbox.focus();
			return false;

		}
		else{
			
			if(trim(order_type) == "N" || trim(order_type) == "B")
			{
				var ordercount = parseInt(Cuur_count);
				if(ordercount>N_count)
				{
					alert("Please Enter the Quantity '"+N_count+"' or Below");
					document.editnonpersonal.ordercount.value="";
					var txtbox=document.getElementById("ordercount");	
					txtbox.focus();
					return false;
				}
			}
		}
	}
	if(rmarks == "")
	{
		alert("Please Enter the Remakrs");
		return false;
	}
	return true;
}

//====================Personal cards Edit starts===========


function validatepordercount()
{
	var order_type=document.orderform.otype.value;
	var P_count=document.orderform.maxpocount.value;
	//var N_count=document.editnonpersonal.maxnocount.value;
	var Cuur_count=document.orderform.ordercount.value;
	var rmarks = document.orderform.rmark.value;
	if(Cuur_count == "")
	{
		alert("Please Enter the Quantity");
		var txtbox=document.getElementById("ordercount");
		txtbox.focus();
		return false;
	}
	else
	{
		if(Cuur_count == "0")
		{
			
			alert("Please Enter the Valid Quantity");
			var txtbox=document.getElementById("ordercount");
			document.editnonpersonal.ordercount.value="";
			txtbox.focus();
			return false;

		}
		else{
			
			if(trim(order_type) == "P" || trim(order_type) == "B")
			{
				var ordercount = parseInt(Cuur_count);
				if(ordercount>P_count)
				{
					alert("Please Enter the Quantity '"+P_count+"' or Below");
					document.orderform.ordercount.value="";
					var txtbox=document.getElementById("ordercount");	
					txtbox.focus();
					return false;
				}
			}
		}
	}
	if(rmarks == "")
	{
		alert("Please Enter the Remakrs");
		return false;
	}
	return true;
}

//====================Personal Cards Edit Ends============

function yearvalidationforregistration()
{

	var today = getTodayDate();
	var dob = document.cardregis.dob.value;
	var year = dob.split("-");
	if (year[2] >= "1900")
	{
		
		if ( DateDiff(today, dob ) > 0 ) 
		{
			alert("Birthday Date is invalid" );
			document.cardregis.dob.value = "";
			return false;
		}
	}
	else
	{
		alert("Please Select the Year Less than 1950");
		document.getElementById('dob').value = "";
	    return false;	
	}
return true;
}
function disabletheResidetnaddressforregistration()
{
	
	var check = document.cardregis.same.checked;
	if(check)
	{
		
			//document.cardregis.raddress1.value = document.cardregis.paddress1.value ;
			document.cardregis.raddress1.value = "";
			document.cardregis.raddress1.disabled = true;
			document.cardregis.raddress2.value = "";
			document.cardregis.raddress2.disabled = true;
			document.cardregis.raddress3.value = "";
			document.cardregis.raddress3.disabled = true;
			document.cardregis.raddress4.value = "";
			document.cardregis.raddress4.disabled = true;
	}
	else
	{
		
		document.cardregis.raddress1.disabled = false;
		document.cardregis.raddress2.disabled = false;
		document.cardregis.raddress3.disabled = false;
		document.cardregis.raddress4.disabled = false;
	}
}

function cardregistarionchecks()
{
	 
	 
	var card_no = document.cardregis.cardno.value;
	//var cardlen = card_no.length;
	//alert("==========="+cardlen+"=============");
	if(card_no == "")
	{
		alert("Please Enter the Card Number");
		document.getElementById("cardno").focus();
		return false;
	}
	/*else if(cardlen < 16)
	{
		document.cardregis.cardno.value ="";
		document.getElementById("cardno").focus();
		alert("Please Enter valid Card Number");
		return false;
	}
	else if(cardlen < 19)
		{
		document.cardregis.cardno.value ="";
		document.getElementById("cardno").focus();
		alert("Please Enter valid Card Number");
		return false;
		}*/
	
	
	

	 var f_name = document.cardregis.fname.value;
	 if(f_name == "")
	 {
		 alert("First Name Should Not Be Empty");
		 return false;
	 }

	 var l_name = document.cardregis.lname.value;
	 if(l_name == "")
	 {
		 alert("Last Name Should Not Be Empty");
		 return false;
	 }
	var father_name = document.cardregis.f_name.value;
	 if(father_name == "")
	 {
		 alert("Father Name Should Not Be Empty");
		 return false;
	 }
	 var marri = document.cardregis.married.checked;
	 if(marri)
	 {
		 var  spouse_name = document.cardregis.spname.value;
		 if(spouse_name == "")
		 {
			 alert("Please Enter Spouse Name");
			 return false;
		 }
	 }
	 var occupation  = document.cardregis.work.value;
	 if(occupation == "")
	 {
		 alert("Please Enter the Occupation");
		 return false;
	 }
	 var nations = document.cardregis.nations.value;
	 if(nations == "-1")
	 {
		 alert("Please Select The Nationality" );
		 return false;
	 }
	 var Bday = document.cardregis.dob.value;
	 if(Bday == "")
	 {
		 alert("Select Your Bith Day Date");
		 return false;
	 }
	 var mobile_no = document.cardregis.mobile.value;
	 if(mobile_no == "")
	 {
		 alert("Enter Your Mobile Number");
		 return false;
	 }
	 var phone_no = document.cardregis.phone.value;
	 if(phone_no == "")
	 {
		 alert("Enter Your Phone Number");
		 return false;
	 }
	  var e_mail = document.cardregis.email.value;
	  if(e_mail == "")
	  {
		  alert("Please Enter E-Mail Address");
		  return false;

	  }
	  else
	  {
			 if( !emailvalidator( e_mail ) )
			 {
			 	alert( "InValid E-Mail Address" );
			 	return false;
			 }

	  }
	 var id_num = document.cardregis.idno.value;
	 if(id_num == "")
	 {
		 alert("Please Enter Identification Number");
		 return false;
	 }
	 var id_doc = document.cardregis.iddoc.value;
	 if(id_doc == "")
	 {
		 alert("Please Enter Identification Document");
		 return false;
	 } 
	 var s_ques = document.cardregis.squestion.value;
	 if(s_ques == "-1")
	 {
		 alert("Please Select Your Question");
		 return false;
	 }
	 var q_answer = document.cardregis.answer.value;
	 if(q_answer == "")
	 {
		 alert("Enter Your Answer");
		 return false;
	 }
	var p_address1 = document.cardregis.paddress1.value;
	 if(p_address1 == "")
	 {
		 alert("Enter Your Postal Address1 ");
		 return false;
	 }
	 var p_address2 = document.cardregis.paddress2.value;
	 if(p_address2 == "")
	 {
		 alert("Enter Your Postal Address2 ");
		 return false;
	 }
	 var chck_same = document.cardregis.same.checked;
	 if(!chck_same)
	 {
		 var r_address1 = document.cardregis.raddress1.value;
		 if(r_address1 == "")
		 {
			 alert("Enter Your Resident Address1");
			 return false;
		 }
		 var r_address2 = document.cardregis.raddress2.value;
		 if(r_address2 == "")
		 {
			 alert("Enter Your Resident Address2");
			 return false;
		 }	 
	 }
	
	
	 
	 
	return true;
}

function validateactivationcardno()
{
	var cardno = document.instcardactiveform.card_no.value;
	if(cardno == "")
		{
		alert("Please Enter the Card Number");
		document.getElementById("card_no").focus();
		return false;
		}
	return true;
}
function validatereloadcard()
{
	var crds = document.cardreloadform.card_no.value;
	if(crds == "")
	{
		alert("Please Enter the Card Number");
		document.getElementById("card_no").focus();
		return false;
	}
	
	var amt = document.cardreloadform.amount.value;
	//alert("Amount Entered is "+amt);
	if(amt == "")
	{
		alert("Please Enter the Amount");
		document.getElementById("amount").focus();
		return false;
	}else
	{
		var amout = parseInt(amt);
		//alert("Amount parseint "+amout);
		var maxamt = document.cardreloadform.maxamount.value;
		//alert("maximum amount "+maxamt);
		var mamt = parseInt(maxamt);
		//alert("max amount parse int "+mamt);
		if(amout > mamt)
		{
			alert("Please Enter the amount with in "+maxamt);
			document.cardreloadform.amount.value = "";
			document.getElementById("amount").focus();
			return false;
		}
		
	}
	
	return true;
}



function gettingtopupamountdetails()
{
	
		var cardnum=(document.getElementById('card_no').value);
		var card_type=(document.getElementById('cardtype').value);
		
		if(cardnum == "")
		{
			alert("Please Enter the Card Number");
			return false;
		}
		if(card_type == "-1")
		{
			alert("Please select the Prosuct Name");
			return false;
		}
   	httpObject = getHTTPObjectForBrowser();
	if (httpObject != null) 
	{

   		
   		httpObject.open("GET", "ajaxgettingproducttopupamount.do?cardtype="+card_type+"&cardno="+cardnum, true);
		httpObject.send(null);
		httpObject.onreadystatechange = setamountAjaxOutput;
		return true;
	}
	else
	{
		alert("Browser Not Support");
		document.cardreloadform.card_type.options[document.cardreloadform.card_type.selectedIndex].value ="";
		return false;
	}

	
}
function setamountAjaxOutput()
{
	if(httpObject.readyState == 4) 
	{
	    document.getElementById('amount').innerHTML = httpObject.responseText;
	}
}

function validateaddoncards()
{
	var parent_card = document.addoncardform.pcardno.value;
	if(parent_card == "")
	{
		alert(" Please Enter the Parent Card Number ");
		document.getElementById("pcardno").focus();
		return false;		
	}
	var addcard = document.addoncardform.add_card.value;
	if(addcard == "")
	{
		alert(" Please Enter the Add-on Card Number ");
		document.getElementById("add_card").focus();
		return false;		
	}	
	return true;
}

//=======================Inst Card Generationlist.jsp validation Starts=======================================
/*
function checkallorders()
{
	alert("Calling allorders functon");
	
	var checklist =  document.getElementsByName('instorderrefnum').length;
	alert(checklist);
	if(checklist > 0)
	{
		alert("Inside if");
		document.getElementsByName('instorderrefnum').checked;
		return false;
	}
	
	return true;
}

function selectallorders()
{
	alert("Selecting all orders");
	var checklist =  document.getElementsByName('instorderrefnum').length;
	
}*/


//========================Inst Card Generationlist.jsp validation Ends=======================================
