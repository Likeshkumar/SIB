window.onload = function () {
	element = document.forms[0].elements
	for ( i=0; i<element.length;i++ ){
		if(document.forms[0].elements[i].type == "text" || document.forms[0].elements[i].type == "select-one" || document.forms[0].elements[i].type == "password" ){
		document.forms[0].elements[i].focus();
		return
		}
	}
}

function errMessage( fldid, msg ){  
	errmsg =  document.getElementById("errmsg");   
	errmsg.innerHTML = msg;
	fldid.focus();
	window.scrollTo(0, 0);
}

function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
}
	
function checkedData() {
	var count = 0;
	chkcnt =document.getElementsByTagName("input").length;
	  for ( var i=1; i<=chkcnt; i++ ) {  
			 if ( document.getElementById('orderrefnum'+i)) { 
				 if (document.getElementById('orderrefnum'+i).type == "checkbox" ) {
					if (document.getElementById('orderrefnum'+i).checked == true) {  
						count = count+1;
	                }
				 }
			 	} 
	   }

	if ( count == 0 ){
		alert( "select atlease one order");
		return false;
	}	      
}


function showSelect( rowid ){
		document.getElementById(rowid).style.background="#F0F0F0";
}
	
function showDeSelect( rowid ){
	document.getElementById(rowid).style.background="#fff";
}
	

function checkedAll ( myform ) {
	
    
	 check = document.getElementById('checkall').checked; 
		for (var i = 0; i < myform.elements.length; i++) { 
		     myform.elements[i].checked = check ;
	             
	        } 
}


function confirmCancel() {
   if ( confirm( "Do you want to cancel ? " ) )
    { 
      //history.go(-1)
       window.location = "jsp/emptyPage.jsp";
	   
      return true;
   }else {
     return false;
   }  
}

function confirmDelete() {
   if ( confirm( "Do you want to Delete ? " ) )
    {  
      return true;
   }else {
     return false;
   }  
}

function getHTTPObject() {
	var xmlhttp; try { xmlhttp=new ActiveXObject("Msxml2.XMLHTTP");  } catch (e) { try { xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); } catch (e) { xmlhttp=false; } } 
	if(!xmlhttp && typeof XMLHttpRequest !=undefined) { try { xmlhttp=new XMLHttpRequest(); } catch (e) { xmlhttp=false; } }
	if(!xmlhttp) { return; } else { return xmlhttp; }
}

function uncache(url) { var d=new Date(); var time=d.getTime(); return url+"&time="+time; }


function AjaxReturnValue(url)
{
	http=getHTTPObject();
	url=uncache(url);
	http.open("POST", url, false);
	http.send(null);
	return http.responseText;
}
function jsonAjax(url)
{
	http=getHTTPObject(); 
	http.open("POST",url,true);
	http.setRequestHeader("Content-type","application/json; charset=utf-8");
	http.send(null);
	return http.responseText;
}
function trim(str)
{
	return str.replace(/^\s*|\s*$/g,"");
}

// This function allow only numeric values AND NOT SPECIAL CHARS
function numerals(evt)
{
       //alert( evt.keyCode );
    //alert( evt.charCode );
	var unicode=evt.charCode? evt.charCode : evt.keyCode;
        if( evt.keyCode==35 || evt.keyCode==36 || evt.keyCode==37 || evt.keyCode==39 || evt.keyCode==46 )
              return false;
	if( ( unicode > 47 && unicode < 58 ) || unicode == 8 || unicode == 9 )
        {
		return true;
        }
	else
	{
		unicode=0;
		return false;
	}
}

// This function allow only alphabets values
function alphabets(evt)
{
	var unicode=evt.charCode? evt.charCode : evt.keyCode;
	//alert(unicode);
	if(((unicode >= 65) && (unicode <= 90)) || (unicode == 8) || (unicode == 9) || (unicode == 32) || ((unicode >= 97) && (unicode <= 122)))
	{
		//alert("Return true");
		return true;
	}
	else
	{
		unicode=0;
		//alert("Return False");
		return false;
	}
}


// Allow Alphabets and numeric and not special chars also
function alphanumerals(evt)
{
	//alert("test");
        var unicode=evt.charCode? evt.charCode : evt.keyCode;
        //alert(unicode);
	if((!(unicode<48||unicode>57)) || ((unicode >= 65) && (unicode <= 90)) || (unicode == 8) || (unicode == 9) || (unicode == 32)  || ((unicode >= 97) && (unicode <= 122)))
	{
		return true;
	}
	else
	{
		unicode=0;
		return false;
	}
}


function count_chars(s)
{
	if(s.length < 351)
	{
		return true;
	}
	else
	{
		alert("Only 350 characters Allowed here");
		return false;
	}
}

function emailvalidator( value )
{
	var tempemail = trim(value);
	var emailExp = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	if(tempemail.match(emailExp))
	{
		endchar = tempemail.charAt(tempemail.length - 1);
		if( endchar >= "a" || endchar <= "z" || endchar == "A" || endchar == "Z" )
			return true;
		return false;
	}
}

function validateIP(ipadrs)
{
	var filter = /^(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)$/;

	if (!filter.test(ipadrs))
	{	
  		return false;
	}
	
	return true;
}

function delConfirm(){
	var x = confirm( "Do you want to Delete ? ");
	if ( x ) {
		return true;
	}else{
		return false;
	}
}

//pavithra
function validFilter()
{
	valid= true;
	var branchsel=document.getElementById("branch");
	var cardtypesel=document.getElementById("cardtype");
	var fromdate=document.getElementById("fromdate");
	var todate=document.getElementById("todate");
	var status=document.getElementById("ordstatus");
	var prefilename=document.getElementById("prefilename");
        
if(branchsel){
	if (branchsel.value == "-1" )
		 {
		//alert("Select Branch");
		errMessage(branch,"Select Branch" );
	    return false;
		}		  
	}
	if( cardtypesel  )
	{	
		if( cardtypesel.value == "-1~-1" ){
		//alert("Select Card type");
		errMessage(cardtype,"Select Card type" );
		return false;
	}
	}
	if( fromdate  )
	{	
		if( fromdate.value == "" ){
		 //alert("Select From Date");
		 errMessage(fromdate,"Select From Date" );
		 return false;		 
		 }
	}
	if( todate  )
	{	 
			if( todate.value == "" ){
				//alert("Select To Date");
			 errMessage(todate,"Select To Date");
			 return false;		 
			 }
	} 
	if( prefilename ){
		if(prefilename.value=="-1"){
			 errMessage(prefilename,"Select File Name");
			 return false;
		}
	}
	var arrFrom=fromdate.value.split('-');
	var arrTo=todate.value.split('-');

	var strFromDate=arrFrom[0];
	var strFromMonth=arrFrom[1];
	var strFromYear=arrFrom[2];

	var strToDt=arrTo[0];
	var strToMonth=arrTo[1];
	var strToYear=arrTo[2];


	if(parseInt(arrFrom[0])<10)
	{
	strFromDate=arrFrom[0].replace('0','');
	}
	if(parseInt(arrFrom[1])<10)
	{
	strFromMonth=arrFrom[1].replace('0','');
	}
	
	
	if(parseInt(arrTo[0])<10)
	{
	strToDt=arrTo[0].replace('0','');
	}
	if(parseInt(arrTo[1])<10)
	{
	strToMonth=arrTo[1].replace('0','');
	}

	var mon1 = parseInt(strFromMonth)-1;
	var dt1 = parseInt(strFromDate);
	var yr1 = parseInt(arrFrom[2]);

	var mon2 = parseInt(strToMonth)-1;
	var dt2 = parseInt(strToDt);
	var yr2 = parseInt(arrTo[2]);


	var from_Date = new Date(yr1, mon1, dt1);
	var to_Date = new Date(yr2, mon2, dt2);
	if(to_Date < from_Date)
	{
	errMessage(document.getElementById("fromdate"),"From date should be lesser than to-date");
	return false;
	}
	
	if( status )
	{	 
		if(status.value == "-1" ){
			//alert("Select To Date");
		
		 errMessage(status,"Select the status");
		 return false;		 }
		if (fromdate.value == todate.value) 
		{
	        //alert("Error! ...");
	        // whatever you want to do here
	    }	  
	
	}
	
	if( prefilename )
	{	 
		if(prefilename.value == "-1" ){
			//alert("Select To Date");
		
		 errMessage(prefilename,"Select the status");
		 return false;		 
	}
	}
	parent.showprocessing();
    return valid;
}



function validateinstorder(){
	var branchid = document.getElementById("branch");
	var card = document.getElementById("cardtype");
	var subproductlist = document.getElementById("subproductlist");
	var count = document.getElementById("count");
	
	valid= true;
	if( branchid ){ 
		
	if( branchid.value == "-1" ){
			errMessage(branchid,"Please select branch name");
			return false;}
	}
	if( card ){ 
	if( card.value == "-1~-1" ){
		errMessage(card,"Please select Card");
		return false;}
	}
	if( subproductlist ){ 
	if( subproductlist.value == "-1" ){
		errMessage(subproductlist,"Please select product");
		return false;} 
	}
	if( count ){ 
	if( count.value == "" ){
		errMessage(count,"Enter the No.of cards ");
		return false;} 
	}
	parent.showprocessing();
	return valid;
}


function hideuser( hidevalue )
{
	
	var glmsg = "Cash On Hand";
	//alert(hidevalue);
	if(hidevalue == "Enter User Name")
	{
		document.getElementById("username").value="";
	}
	else if(hidevalue == glmsg )
	{
		document.getElementById("hoamount").value="";
	}
	
}

function bluruser( blurvalue, fldid )
{
	var glmsg = "Cash On Hand";
	if (blurvalue == "") 
	{
		document.getElementById(fldid).value= glmsg;
		 
		
		
	}	
}
	
function bluruser( blurvalue )
{
	//alert(blurvalue);
	if (blurvalue == "") 
	{
	document.getElementById("username").value= "Enter User Name";
	}	
}
function validate_form ( )
{
	//var deployment=document.ProductAddFofm.deployment;
	var globcardtype = document.ProductAddFofm.globcardtype;
	var cardtypedesc = document.ProductAddFofm.cardtypedesc;
	var prodsubtype = document.ProductAddFofm.prodsubtype;
	var bin = document.ProductAddFofm.bin;
	var glcode = document.ProductAddFofm.glcode;

	valid = true;

	if( globcardtype ){ 
		if ( globcardtype.value == "-1" )
		 {
		 errMessage(globcardtype,"Please Select Card Type Name");
		 return false;
		 }
	}
	if( bin ){ 
		 if( bin.value == "-1" )
		 {
		  errMessage(bin,"Please Select BIN");
		  return false;		
		 }
	}
	if( cardtypedesc )
	{ 
		if ( cardtypedesc.value == "" )
		{
		errMessage(cardtypedesc,"Please Enter Card Type Description");
		return false;
	    }
	}
	if( prodsubtype ){ 
		if ( prodsubtype.value == "" )
		{
    	errMessage(prodsubtype,"Please Enter No Of Subtype");
		return false;
		}
	}
	if( glcode ){ 
	   if( glcode.value == "-1" )
	    {
	    errMessage(glcode,"Please Select GL");
		return false;	
		}
	}
	
	if( confirm("Do you want to continue ") ){
		return valid;
	}else{
		return false;
	}
	
  }

function goBack()
{
	window.history.back();
}

function passwordvalidator()
{
	//alert("WELCOME");
	var oldpassword=document.getElementById("oldpassword");
	var newpassword=document.getElementById("newpassword");
	var cnewpassword=document.getElementById("cnewpassword");
    var re=  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{6,}$/;   
	
	var validPassword = re.test(newpassword.value);
	var ConfirmvalidPassword = re.test(cnewpassword.value);
	var oldvalidPassword = re.test(oldpassword.value);
	if(oldpassword.value=="")
	{
	 //alert(" Old Password Cannot be Empty");
	 errMessage(oldpassword," Old Password Cannot be Empty");
	 return false;
	}
	
	if(newpassword.value=="")
	{
	 errMessage(newpassword," New Password Cannot be Empty");
	 return false;
	}

	if(cnewpassword.value=="")
	{
	 errMessage(cnewpassword," Confirm New Password Cannot be Empty");
	 return false;
	}
 	 
	 if( validPassword==false)
	 {
	  //alert("Invalid Password");
	  errMessage(newpassword,"New password should contain atleast one Capital letter, atleast one special character and length minimum 6 digit " );
	  return false;
 	}

	 if(ConfirmvalidPassword==false)
	 {
	  //alert("Invalid Password");
	  errMessage(cnewpassword,"Invalid Confirm Password" );
	  return false;
 	}
	if(oldpassword.value==newpassword.value)
		{
		errMessage(cnewpassword," Old Password and New Password Should Not Be Same ");
		return false;
		}
	
	if(cnewpassword.value!=newpassword.value)
	{
		errMessage(cnewpassword," New Password and Confirm Password Should Be Same ");
		return false;
	}

return true;
}

