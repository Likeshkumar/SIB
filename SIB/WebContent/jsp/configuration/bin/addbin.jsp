<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>

<style>
table{  
	padding-top:10px;
	padding:0 auto;
	width:750px;
}
</style>
<script type="text/javascript" >

function checkLic( binno ){
	var url = "checkBinLicenceAddBinAction.do?bin="+binno;
	//alert( url );
	var submitbtn = document.getElementById("submit");
	var result = AjaxReturnValue(url);
    if( result != "sucess"){
    	alert(result+" BIN");
    	document.getElementById("errmsg").innerHTML="BIN NUMBER DOES NOT MATCHED WITH LICENCE. ";
    	submitbtn.disabled=true;
    }else{
    	submitbtn.disabled=false;
    	document.getElementById("errmsg").innerHTML='';
    }
}
function multi_check( chkk ,chkid,chkvalue)
{
	var multi_chck=document.getElementsByName("curcode");
	for( var i=0; i<multi_chck.length; i++ ){ 
		min_id = "minamt"+chkvalue;
		max_id = "maxamt"+chkvalue;
		    if( chkk ){	
			document.getElementById(min_id).disabled = false ;
			document.getElementById(max_id).disabled = false;
			}
	        else{
	        	document.getElementById(min_id).disabled = true ;
	        	document.getElementById(min_id).value = "";
				document.getElementById(max_id).disabled = true;
				document.getElementById(max_id).value = "";
			 }
	}
}

function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;
   return true;
}

function multiCurrency( chck )
{
	
	if( chck ){
	 //var inst_id=document.getElementById("instid").value;
	 var url = "ajax_multiple_currencyAddBinAction.do?";
	 result = AjaxReturnValue(url);
	 //document.getElementById('sec_ccy').innerHTML = result;
	 var curlist = result.split("~");
	 var print="";
	 for( var i=0; i<curlist.length; i++ ){
		 if( curlist[i] != ""){
			 var curcode_desc = curlist[i].split("-");
			 //alert( "curcode_desc__"+ curcode_desc );
			 var curcode = curcode_desc[0];
			 var curdesc = curcode_desc[1]; 
			 //alert(curcode);
			 //alert(curdesc);
			 print+="<table border='0'><tr><td><input type=\"checkbox\" value='"+curcode+"' name=\"curcode\" id=\"curcode"+i+"\" onclick=\"multi_check(this.checked,this.id,this.value)\"></td><td width='30%'>"+curdesc+"</td>"+"<td width='30%'>Min Amount<input type=\"text\" name=\"minamt"+i+"\" id=\"minamt"+curcode+"\" style=\"width:120px\"disabled=\"true\" onkeypress=\"return isNumberKey(event)\"></td><td>Max Amount<input type=\"text\" name=\"maxamt"+i+"\" id=\"maxamt"+curcode+"\" style=\"width:120px\" disabled=\"true\" onkeypress=\"return isNumberKey(event)\"></td></tr></table>";
		 	
		 }
	 	}
	 document.getElementById('sec_ccy').innerHTML =print;
	}
	else
	{
		document.getElementById('sec_ccy').innerHTML ="";
	}
}


function aks_confermation()
{
var t=confirm("Do you want to reset");
return t;
}


function validatebin_form ( )
{
	//alert("WELCOME");
	var deslength=document.BinAddFofm.deslength;
	var binlen =  Number ( document.getElementById("binlenval").value );
	//alert(deslength.value);
	var binn=document.BinAddFofm.bin.value;
	var min=document.BinAddFofm.min;
	var bindesc=document.BinAddFofm.bindesc;
	var ser_code = document.getElementById("servicecode");	
	var max_pincnt = document.getElementById("maxpincnt");
	var floatglcode = document.getElementById("floatglcode");
	var bkrevenueglcode = document.getElementById("bkrevenueglcode");
	var mbrevglcode = document.getElementById("mbrevglcode");
	
    var valid = true;
    var conf = "";
    	
    /* 	if(document.BinAddFofm.multicurrency.checked){ 
    		var multi_currncy=document.getElementsByName("curcode");  
			var checking = false;
			for( var i=0; i<multi_currncy.length; i++ ){
			 	var checkbox_id = "curcode"+i;
			 	var checkbox_value=document.getElementById(checkbox_id);
			 	//alert(s.value);
			 	var min_name="minamt"+checkbox_value.value;
			 	//alert(min_name);
			 	var max_name="maxamt"+checkbox_value.value;
			 	//alert(max_name);
			 	var subcheck=document.getElementById(checkbox_id);
			 	if ( subcheck.checked )   	{
					checking = true;
					break;
			   	}
			} 
				
			if( checking )	{
				if(document.getElementById(min_name).value == ""){
					alert("Min Amount is empty");
					return false;					
				}
				if(document.getElementById(max_name).value == ""){
					alert("Max Amount is empty");
					return false;
					
				}
				if((parseInt(document.getElementById(min_name).value)) > (parseInt(document.getElementById(max_name).value))){
					alert("Max amount should be greater than Min amount");
					return false;
				} 
			}else{
				alert( "Please select atlease one curreny ");
				return false;
			} 
		}else 	{  
		    conf= "Multicurrency not configured" ; 
		}
    	 */
			
   				 if ( binn == "" )
			   		 {
			    	errMessage(bin, "BIN NUMBER CANNOT BE EMPTY" );
			    	document.getElementById("bin").focus();
					return false;
			    	
			   		}
			    
				   if (binn.length < binlen  )
					{
				    	
					     errMessage(bin,"BIN NUMBER SHOULD BE "+binlen+" DIGITS");
				    	 document.getElementById("bin").focus();
						 return false;
					}
				    
					 
				/*    if ( bindesc.value == "" )
				    {	
						 errMessage(bindesc,"BIN DESC CANNOT BE EMPTY" );
				    	 document.getElementById("bindesc").focus();
				    	 return false;
				   		 
					} */
				   
				    if(document.BinAddFofm.chnlen.value == "" )
					{
				    	 errMessage(chnlen,"PLEASE SELECT CHN Length");
						 document.getElementById("chnlen").focus();
						 return false;
						
					}
				    
				    if(ser_code)
				    {
				    	if(ser_code.value == "-1")
				    	{
				    		errMessage(ser_code,"PLEASE ENTER SERVICE CODE");
				    		return false;
				    	}	
				    }
				    if(max_pincnt)
				    {
				    	if(max_pincnt.value == "")
				    	{
				    		errMessage(max_pincnt,"PLEASE ENTER MAX PIN COUNT");	
				    		return false;
				    	}	
				    }
				    
				    if( floatglcode ){
						if( floatglcode.value == "-1"){  errMessage(floatglcode, "Select Bank Float GL "); return false;}
					}
					if( bkrevenueglcode ){
						if( bkrevenueglcode.value == "-1"){  errMessage(bkrevenueglcode, "Bank Revenue GL");  return false;}
					}
					if( mbrevglcode ){
						if( mbrevglcode.value == "-1"){  errMessage(mbrevglcode, "Select Member Revenue GL ");  return false; }
					}
					
					
				    /* if ( document.BinAddFofm.hsmid.value == "-1" )
				    {	
				    	 errMessage(hsmid,"PLEASE SELECT HSM" );
				    	 document.getElementById("hsmid").focus();
				    	 return false;

					} */
				    
				    if ( document.BinAddFofm.deslength.value == "-1" )
				    {	
				    	 errMessage(deslength, "PLEASE SELECT PIN ENCRYPTION KEY LENGTH " );
				    	 document.getElementById("deslength").focus();
				    	 return false;
				   	}
				    
				    if ( document.BinAddFofm.panoffset.value == "" )
					{	
				    	 errMessage(panoffset,"PANOFFSET CANNOT BE EMPTY" );
						 document.getElementById("panoffset").focus();
						 return false;
							 
					}
				    if ( document.BinAddFofm.panvalidationlenght.value == "" )
				    {	
				    	 errMessage(panvalidationlenght, "PAN VALIDATION LENGHT CANNOT BE EMPTY" );
				    	 document.getElementById("panvalidationlenght").focus();
				    	 return false;
				   		 
					}
				    
					if ( document.BinAddFofm.pinlenght.value == "" )
				    {	
						 errMessage(pinlenght, "PIN LENGHT CANNOT BE EMPTY" );
				    	 document.getElementById("pinlenght").focus();
				    	 return false;
				   		 
					}
						    
					if ( document.BinAddFofm.pinlenght.value<4 )
						{	
						     errMessage(pinlenght, "PIN LENGHT SHOULD BE MINIMUM 4" );
							// document.BinAddFofm.pinlenght.value="";
							 document.getElementById("pinlenght").focus();
							 return false;
						}
						    
					 if ( document.BinAddFofm.pinoffsetlenght.value == "" )
						{	
							 errMessage(pinoffsetlenght,"PINOFFSET LENGHT CANNOT BE EMPTY" );
							 document.BinAddFofm.pinoffsetlenght.value="";
							 document.getElementById("pinoffsetlenght").focus();
							 return false;
								 
						}
					 if ( document.BinAddFofm.decimilisation_table.value == "" )
						{	
							 errMessage(decimilisation_table, "DECIMILISATION TABLE CANNOT BE EMPTY" );
							 document.BinAddFofm.decimilisation_table.value="";
							 document.getElementById("decimilisation_table").focus();
							 return false;
								 
						}
					 if ( document.BinAddFofm.decimilisation_table.value.length<16 )
						{	
						 	 errMessage(decimilisation_table,"DECIMILISATION TABLE VALUE SHOULD BE 16 DIGITS " );
							 document.BinAddFofm.decimilisation_table.value="";
							 document.getElementById("decimilisation_table").focus();
							 return false;
								 
						}
					if ( document.BinAddFofm.panpadchar.value == "" )
						{	
						 	 errMessage(panpadchar,"PAN PAD CHARACTER CANNOT BE EMPTY" );
							 document.BinAddFofm.panpadchar.value="";
							 document.getElementById("panpadchar").focus();
							 return false;
								 
						}
					if ( document.BinAddFofm.pinmailer.value == "-1" )
					{	
						 errMessage(pinmailer,"SELECT PINMAILER" );
						 document.BinAddFofm.pinmailer.value="";
						 document.getElementById("pinmailer").focus();
						 return false;
							 
					}
					if(document.BinAddFofm.pin_methodIBMDES.checked)
					{
						if ( document.BinAddFofm.pvk.value == "" )
							{	
								 errMessage(pvk, "PVK CANNOT BE EMPTY" );
								 document.getElementById("pvk").focus();
								 return false;
							}
						if(document.BinAddFofm.pvk.value.length != deslength.value)
						 {
							//alert("PVK VALUE LENGTH"+pvk.value.length);
							//alert("PVK DIGIT"+deslength.value);
								 errMessage(pvk, "PVK LENGTH SHOULD BE "+deslength.value+" DIGITS");
								 document.getElementById("pvk").focus();
								 return false;
						 }	
					}
					if(document.BinAddFofm.pin_methodVISA.checked)
					{
						if ( document.BinAddFofm.pvki.value == "" )
						{	
							 errMessage(pvki, "PVK INDEX CANNOT BE EMPTY" );
							 document.getElementById("pvki").focus();
							 return false;
								 
						}
							
						if ( document.BinAddFofm.pvk1.value == "" )
						{	
							 errMessage(pvk1,"PVK1 CANNOT BE EMPTY" );
							 document.getElementById("pvk1").focus();
							 return false;
								 
						}
						
						if(document.BinAddFofm.pvk1.value.length != deslength.value)
						 {
								 //alert("DIGITS"+deslength.value);
								 errMessage(pvk1, "PVK1 LENGTH SHOULD BE "+deslength.value+" DIGITS");
								 document.getElementById("pvk1").focus();
								 return false;
						 }
						
						if ( document.BinAddFofm.pvk2.value == "" )
							{	
								 errMessage(pvk2, "PVK2 CANNOT BE EMPTY" );
								 return false;
							}
						
						if(document.BinAddFofm.pvk2.value.length != deslength.value)
						 {
								 errMessage(pvk2,  "PVK2 LENGTH SHOULD BE "+deslength.value+" DIGITS");
								 document.getElementById("pvk2").focus();
								 return false;
						 }
					
					}
					
					

					var servicecode = document.getElementById("servicecode");
					if( servicecode ){ if( servicecode.value == ""  || servicecode.value == "-1") { errMessage(servicecode, "Enter Service Code !");return false; } }

					
					var cardtypeM = document.getElementById("cardtypeM");
					var cardtypeC = document.getElementById("cardtypeC");
					
					if( (cardtypeM.checked == false ) && 
							(cardtypeC.checked == false  )) { errMessage(cardtypeM, "Select Card Type  !");return false; } 

					
					var cvv_reqP = document.getElementById("cvv_reqP");
					var cvv_reqPC = document.getElementById("cvv_reqPC");
					var cvv_reqC = document.getElementById("cvv_reqC");
					
					if( (cvv_reqP.checked == false) &&
							(cvv_reqPC.checked == false) && 
							(cvv_reqC.checked == false))
					  { errMessage(cvv_reqC, "Select Security Option  !");return false; } 
					
					
					if((cvv_reqPC.checked==true) || (cvv_reqC.checked==true))
						{
						
						var cvv1 = document.getElementById("cvv1");
						if( cvv1.value == "") { errMessage(cvv1, "Enter CVV1 Value !");return false; } 
						var cvv2 = document.getElementById("cvv2");
						if( cvv2.value == "") { errMessage(cvv2, "Enter CVV2 Value !");return false; }

						}
						
						
					
					/* if(document.BinAddFofm.multicurrency.checked == false)
					{
						valid = true;
					    var conf=confirm("Multicurrency not configured....Do you want to submit");
					    if(conf==false)
						{
					    	document.getElementById("multicurrency").focus();
							return false;
						}  else{
							return valid;
						}
					} */
				
			if( confirm ( "Do you want to submit ? ") ){
				return valid;	
			}
			else{
				return false;
			} 
					return false;
 
	}

function set_baselen()
{
	
 
	var configuredchnlen=document.getElementById("chnlen").value;
	//alert (configuredchnlen);      
	var brcodelength=document.getElementById("brcodlen").value;
	var appcodelen = 1;
	var subproductlen = 2;
	var cardtypelen = Number (  document.getElementById("cardtypelenval").value );
	var binlen = Number ( document.getElementById("binlenval").value );
	var chkdigitlen = 1;
	//alert (binlen);
	
	//var accttypelen=document.getElementById("accttypelen").value;
	
	if(document.BinAddFofm.chnlen.value == "" )
	{
		alert("Please Select CHN Length");
		return 0;
		
	}
	//alert(" binlen "+ binlen  +" chkdigitlen "+chkdigitlen);
	
	//alert("first  configuredchnlen  ==  "+configuredchnlen);
	
	configuredchnlen = configuredchnlen-( binlen+chkdigitlen ); // for checksum and bin
	//10
	/* if(document.getElementById("attach_appcodeY").checked) // sub product
	{
		configuredchnlen=configuredchnlen-appcodelen;// for subproduct lent
		if(document.getElementById("attach_brcodeY").checked)	{
			configuredchnlen=configuredchnlen-brcodelength;//for branch code
		}
				
	}  */
	
	//alert("2  configuredchnlen  ==  "+configuredchnlen);
	
	if(document.getElementById("attach_prodtype_cardtypeP").checked) // sub product
	{
		alert("3  configuredchnlen  ==  "+configuredchnlen);
		configuredchnlen=configuredchnlen-subproductlen;// for subproduct lent
		if(document.getElementById("attach_brcodeY").checked){
			configuredchnlen=configuredchnlen-brcodelength;//for branch code
		}
		/* if(document.getElementById("attach_accttypeY").checked){
			configuredchnlen=configuredchnlen-accttypelen; //for acct type len
		} */
				
	}
	
	if(document.getElementById("attach_prodtype_cardtypeC").checked)
	{validatebin_form
		
		//alert("4 configuredchnlen  ==  "+configuredchnlen);
		configuredchnlen=configuredchnlen-cardtypelen;// for card type
		
		//alert("res=== "+configuredchnlen);
		//8
		/*  if(document.getElementById("attach_brcodeY").checked)	{
			 configuredchnlen=configuredchnlen-brcodelength;//for branch code
			 
			 //7
		} */
		/*  if(document.getElementById("attach_accttypeY").checked){
				configuredchnlen=configuredchnlen-accttypelen; //for acct type len
			} */
				
	}
	
	if(document.getElementById("attach_prodtype_cardtypeN").checked)
	{
		 if(document.getElementById("attach_brcodeY").checked){
			 configuredchnlen=configuredchnlen-brcodelength;//for branch code
		 }
		/*  if(document.getElementById("attach_accttypeY").checked){
				configuredchnlen=configuredchnlen-accttypelen; //for acct type len
			} */
				
	}
	//alert("hhh  "+configuredchnlen);
	var no=10,i=0;
	no=no--;
	if( configuredchnlen < 1 ){
		errMessage(document.getElementById("chnlen"),"Invalid Configuration. This Bin Cannot Genrate Cards.");
		//return false;
	}else{
		errMessage(document.getElementById("chnlen"),"");
	}
	document.BinAddFofm.baselen.value=(configuredchnlen);
	for (i=0;i<(configuredchnlen-1);i=i+1)
		{
			no=no*10;
		}
	//alert("hhh "+no);
	
	no-=1;
	document.BinAddFofm.nos_cards_gen.value=(no);
	//alert ("YOU CAN GENERATE '"+(no)+"' CARDS ");
	
	/* if(document.getElementById("attach_accttypeY").checked){
		document.getElementById("accttype").style.display = 'block';
	}
	
	if(document.getElementById("attach_accttypeN").checked){
		document.getElementById("accttype").style.display = 'none';
	} */
	
}


function setmethod()
{
	var ibmdes=document.getElementById("pin_methodIBMDES");
	var visa=document.getElementById("pin_methodVISA");
	var pvk=document.getElementById("pvk");
	var pvki=document.getElementById("pvki");
	var pvk1=document.getElementById("pvk1");
	var pvk2=document.getElementById("pvk2");
	
	if(document.BinAddFofm.pin_methodIBMDES.checked)
	{
		pvki.value="";
		pvk1.value="";
		pvk2.value="";
		pvk.value=1111111111111111;
		pvk.readOnly=false;
		pvki.readOnly=true;
		pvk1.readOnly=true;
		pvk2.readOnly=true;
	
	}
	
	
	if(document.BinAddFofm.pin_methodVISA.checked)
	{
		pvki.value=1;
		pvk1.value=1111111111111111;
		pvk2.value=1111111111111111;
		pvk.value="";
		pvk.readOnly=true;
		pvki.readOnly=false;
		pvk1.readOnly=false;
		pvk2.readOnly=false;

	
	}
}

function setcvvreg(val)
{
	
	if(val =='C' || val=='PC')
		{
		document.getElementById("cvv1").disabled = false;
		document.getElementById("cvv2").disabled = false;
		}
	else
		{
		document.getElementById("cvv1").disabled = true;
		document.getElementById("cvv2").disabled = true;
		document.getElementById("cvv1").value="";
		document.getElementById("cvv2").value="";
		}
		
}


var httpObject = null;

//Get the HTTP Object
function getHTTPObjectForBrowser(){
      if (window.ActiveXObject)
          return new ActiveXObject("Microsoft.XMLHTTP");
      else if (window.XMLHttpRequest) return new XMLHttpRequest();
      else {
          alert("Browser does not support AJAX...........");
      return null;
}
}
//Change the value of the outputText field
function setAjaxOutput(){
  if(httpObject.readyState == 4){
  	
  //	alert(httpObject.responseText);
      document.getElementById('ajax').innerHTML = httpObject.responseText;
      
  }
}
//Implement business logic
  function doAjaxCall(){
		
	//alert ("SURESH");
 		 var inst_id=document.getElementById("instid").value;
 		 var bin_id=document.getElementById('bin').value;
 		 var product=document.getElementById('product').value;
 		 
 		
 		
      httpObject = getHTTPObjectForBrowser();
      if (httpObject != null) {
      	// alert(document.getElementById('product').value);
      	//alert(bin_id);       	 
          httpObject.open("GET", "ajax_BinLicence_HandlerAddBinAction.do?inst_id="+inst_id+"&bin="+bin_id+"&product="+product, true);
          httpObject.send(null);
     	  httpObject.onreadystatechange = setAjaxOutput;
    
  }
}


/*
function multicurrency()
{
	alert("ha");
		//var default_curr=document.getElementById('currencytype').value;
		//alert("default_curr"+default_curr);
		httpObject = getHTTPObjectForBrowser();
	    if (httpObject != null) {
	    	
	    	// alert(document.getElementById('product').value);
	    	//alert("============ in side if "+default_curr);       	 
	        httpObject.open("GET", "ajax_multiple_currencyAddBinAction.do", true);
	        //alert(default_curr);
	        httpObject.send(null);
	   		httpObject.onreadystatechange = setsubCcy;
	        }
}
function setsubCcy()
{
	 if(httpObject.readyState == 4)
	 {

	    //alert("Reponse is "+httpObject.responseText);
	        document.getElementById('sec_ccy').innerHTML = httpObject.responseText;
	 }
}
*/
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


</script>
	
	 
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form name="BinAddFofm" autocomplete="off" action="%{binbean.formaction}"  namespace="/" onsubmit="parent.showprocessing()"  >
	<s:hidden name="brcodlen"  id="brcodlen" ></s:hidden>
  <s:hidden type="hidden" name="act"  id="act" value="%{binbean.act}" /> 
  <s:hidden type="hidden" name="binlenval"  id="binlenval" value="%{binbean.binlen}" />
  <s:hidden type="hidden" name="cardtypelenval"  id="cardtypelenval" value="%{binbean.cardtypelenval}" />
  <%-- <s:hidden name="accttypelen"  id="accttypelen" value="%{binbean.accttypelen}"/> --%>
   	 
   	 <%
   		String acttype = (String)session.getAttribute("apptype");
   	 	if( acttype  == null ){
   	 		acttype = ""; 
   	 	}
   	 %>
	   
	     <table border="1" cellpadding="0" cellspacing="0" width="100%" RULES="NONE" align='center'>
			<TR><TD ID="errmsg" colspan="2"></TD></TR>
			<tr align="center"><td colspan="4"><b><font color="red"><div id="ajax"/></font></b></td></tr>
			<tr>
					<td>INSTITUTION ID</td>
						
					<td> : <s:textfield id="instid" name="instid" readonly="true" value="%{#session.Instname}" maxlength="10"/> </td>
					
				</tr>
				<tr>
					<td> BIN NUMBER</td> <!-- /*onchange="checkLic(this.value)" */ -->
					<td> : <s:textfield name="bin" id="bin" onkeypress='return isNumberKey(event)'  maxlength="%{binbean.binlen}"  value="%{binbean.binvalue}" onchange="checkLic(this.value)"></s:textfield></td>
			   </tr>
 	
 				 
 				<tr>
 					<td>BIN DESC</td>
		    		<td>	
		    	 		:  <s:textfield name='bindesc' id='bindesc' maxlength="25" value="%{binbean.bindesc}" onkeyup="chkChars('BIN DESC',this.id,this.value)"/>
						 
					 </td>
		
				</tr>
				 
				 
	     </table>
      <br/><br/>
      <b> CHN DETAILS</b><br>
      
      
       <table border="1" cellpadding="0" cellspacing="0" width="100%" align="center" RULES="NONE" FRAME="BOX">
		    <tr><td>&nbsp;</td></tr>
		    <tr>
					<td>CHN LENGTH </td><td> : <s:select headerKey="" headerValue="--Select CHN LENGTH --" id="chnlen"  name="chnlen"   list="#{'16':'16 DIGIT','19':'19 DIGIT' }" onchange="set_baselen();" value="%{binbean.chnlenght}" /> </td>		
			
			</tr>
			
			<%-- <tr>
					<td align="left">ATTACH APPLICATION CODE </td>
					<td> : <s:radio id="attach_appcode"  name="attach_appcode" list="#{'Y':'Yes','N':'NO'}" onclick="set_baselen();"  value="%{binbean.attacheappcode}"></s:radio></td>
			</tr> --%>
			
			
			<tr>
					<td align="left">ATTACH CARD TYPE/SUB PRODUCT CODE  </td>
					<td> : <s:radio id="attach_prodtype_cardtype"  name="attach_prodtype_cardtype" list="#{'C':'CARD TYPE','P':'SUB PRODUCT','N':'NO'}" onclick="set_baselen();" value="%{binbean.attachproductcode}" ></s:radio></td>
			</tr>
			
			<tr>
					<td align="left">ATTACH  BRANCH CODE  </td>
					<td> : <s:radio id="attach_brcode"  name="attach_brcode" list="#{'Y':'YES','N':'NO'}" onclick="set_baselen();"   value="%{binbean.attachbranchcode}"  ></s:radio>
						 
			</tr>
			
			<%-- <tr>
					<td align="left">ATTACH  ACCOUNT TYPE  </td>
					<td> : <s:radio id="attach_accttype"  name="attach_accttype" list="#{'Y':'YES','N':'NO'}" onclick="set_baselen();"   value="%{binbean.attachaccttype}"  ></s:radio>
						 
			</tr> --%>
			
			<%-- <tr id="accttype" style="display: none;" align="right">
					<td>ACCOUNT TYPE  </td>
					<td><!-- &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  --> 
					  <s:radio id="attached_accttype"  name="attached_accttype" list="#{'S':'SAVINGS','C':'CURRENT'}"   value="%{binbean.attachedaccttype}" />  
						: <s:select 
						list="%{binbean.accttypeslist}" 
						listValue="ACCTTYPEDESC"
		    			listKey="ACCT_FLAG" 
		    			id="attached_accttype" 
		    			value="%{binbean.attachedaccttype}" 
		   				name="attached_accttype" style="width:120px" />
					</td>
			</tr> --%>
			
			<tr>
				<td>BASE LENGTH </td><td> : <s:textfield id="baselen"  name="baselen" readonly="true"   value="%{binbean.baselength}"  /> </td>		
			</tr>
			
			<tr>
				<td>NO CARDS CAN BE GENERATED </td><td> : <s:textfield id="nos_cards_gen"  name="nos_cards_gen" readonly="true" value="%{binbean.numberofcardsgen}"  /> </td>		
			</tr>
			
			 <s:hidden  name="cardnumber" id="cardnumber" value="00" maxlength="19"/>
			<%--  <tr>
				<td>CARD NUMBER </td><td> : <s:textfield name="cardnumber" id="cardnumber" maxlength="19"/> </td>		
			</tr> --%>
			
			<tr><td>SERVICE CODE</td><td> : <s:textfield id="servicecode"  name="servicecode"  value="%{binbean.servicecode}"  /> </td></tr>
			<% 
			
			if(acttype.equals("MERCH"))
			{
			%>
			<tr><td>SERVICE CODE</td><td> : <s:select list="%{servicecode}"  listKey="SERVICE_CODE"  listValue="SERVICE_CODE"  id="servicecode"  name="servicecode"  headerValue="SELECT"  headerKey="-1"></s:select> </td></tr>
			<tr><td>MAX PIN COUNT</td><td> : <s:textfield id="maxpincnt"  name="maxpincnt" maxlength="1" onkeypress='return isNumberKey(event)'></s:textfield> </td></tr>
			<% 
			}
			%>
			<tr><td>&nbsp;</td></tr>
		
			
		 </table>
      
      <br>
       <B>SECURITY OPTION</B><br>
    <table border="1" cellpadding="0" cellspacing="0" width="100%" align="center" RULES="NONE" FRAME="BOX">
      <tr><td>&nbsp;</td></tr>
					    
			<tr>	<td>CARD TYPE</td>
					<td colspan="1"> : <s:radio name="cardtype"  id="cardtype"  list="#{'M':'Magnetic Card','C':'Chip Card'}" value="%{binbean.magcardorchip}" >   </s:radio> </td>									
			</tr>
			<tr>
			<td> Security Type :</td>
			<td colspan="1"> : <s:radio name="seqoption"  id="cvv_req" onclick="return setcvvreg(this.value);" list="#{'P':'Only Pin','PC':'Pin and CVV','C':'Only CVV'}" value="%{binbean.secoption}" >   </s:radio> </td>									
					
			</tr>
			
			<tr><td>&nbsp;</td></tr>
	</table>		
     
      <br/><br/>
       <br>
       <B>INSTANTCARD MAPPING OPTION</B><br>
    <table border="1" cellpadding="0" cellspacing="0" width="98%" align="center" RULES="NONE" FRAME="BOX">
      <tr><td>&nbsp;</td></tr>
					    
			
			<tr>
			<td> Insta card Map :</td>
			</tr>
			<tr>
			<td colspan="1">  <s:radio name="instantmap1"  id="instantmap"  list="#{'WEBSRV':' FETCH DATA USING WEB SERVICE ','CBSLINK':' FETCH DATA USING CBS LINK ','SCHEMA':' FETCH DATA USING OTHER SCHEMA','MAN':'ENTER MANUALLY'}" >   </s:radio> </td>									
					
			</tr>
			
			<tr><td>&nbsp;</td></tr>
	</table>		
     
      <br/><br/>
      <B>SECURITY PARAMETERS</B><br>
        

            <table border="1" cellpadding="0" cellspacing="0" width="100%" align="center" RULES="NONE" FRAME="BOX">
	        <tr><td>&nbsp;</td></tr>
		   <tr>
		
					<td>SELECT HSM </td>
					<td>  
						: <s:select label="HSM"  
						headerKey="1" headerValue="Select"
						list="hsmlist" 
						listValue="HSMNAME"
		    			listKey="HSM_ID" 
		    			id="hsmid" 
		    			value="%{binbean.hsmid}" 
		   				name="hsmid" style="width:120px" />
					</td>
			
					
					<td> PIN ENCRYPTION KEY LENGTH  </td>
					<td>  
						: <s:select label="deslength"  
						headerKey="-1" headerValue="Select"
						list="#{'2':'Index Length -> 2 ','16':'Single Length -> 16 ','32':'Double Length-> 32','48':' Triple Length -> 48' }" 
						id="deslength" 
		   				name="deslength" style="width:120px"
		   				value="%{binbean.pinenclength}" 
		   				/>
					</td>
			<tr>
					<td>PAN VALIDATION <br>OFFSET</td>
					<td> : <s:textfield name="panoffset" id="panoffset" onkeypress='return  isNumberKey(event)'  maxlength="1" style="width:120px" value="%{binbean.panoffset}"  /></td>
					
					
					<td>PAN VALIDATION <br>LENGTH</td>
					<td> : <s:textfield name="panvalidationlenght" id="panvalidationlenght" onkeypress='return  isNumberKey(event)'  maxlength="2" style="width:120px" value="%{binbean.panvalidationlength}"  ></s:textfield></td>
					</tr>
			<tr>
					<td >PIN LENGTH</td>
					<td> : <s:textfield name="pinlenght" id="pinlenght" onkeypress='return  isNumberKey(event)'  maxlength="1" style="width:120px" value="%{binbean.pinlength}"  /></td>
			
					<td>PIN OFFSET <br>LENGTH</td>
					<td> : <s:textfield name="pinoffsetlenght" id="pinoffsetlenght" onkeypress='return  isNumberKey(event)'  maxlength="1" style="width:120px"  value="%{binbean.pinoffsetlength}"/></td>
					
			</tr>
			<tr>
					<td>DECIMILISATION TABLE</td>
					<td> : <s:textfield name="decimilisation_table" id="decimilisation_table" onkeypress='return isNumberKey(event)'  maxlength="16"  style="width:120px"   value="%{binbean.decimilsationtable}" ></s:textfield></td>
			
					<td>PAN PAD CHARACTER </td>
					<td> : <s:textfield name="panpadchar" id="panpadchar"  maxlength="1" value="%{binbean.pinpadchar}"  style="width:120px"></s:textfield></td>
						
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
					
					
					<td>PIN GENERATION TYPE</td>
					<td> : <s:radio name="pin_method"  id="pin_method" onclick="return setmethod();" list="#{'IBMDES':'IBMDES','VISA':'VISA'}"  value="%{binbean.pingentype}"  >   </s:radio> </td>
					<td>
						PINMAILER
					</td>
					<td>  
						: <s:select label="Pinmailer"  
						headerKey="-1" headerValue="Select"
						list="pinmailerlist" 
						listValue="PINMAILER_NAME"
		    			listKey="PINMAILER_ID" 
		    			id="pinmailer" 
		    			value="%{binbean.pinmailer}" 
		   				name="pinmailer" style="width:120px" />
					</td>
			</tr>		
			<tr><td>&nbsp;</td></tr>
					
			
			<tr>
					<td>PIN VERIFICATION KEY INDEX</td>
					<td> : <s:textfield name="pvki" id="pvki" onkeypress='return  isNumberKey(event)' readonly="true" maxlength="1" style="width:120px" value="%{binbean.pinverificationkeyindex}"  /></td>
					
					<td>PIN VERIFICATION KEY1</td>
					<td> : <s:textfield name="pvk1" id="pvk1" onkeypress='return isNumberKey(event)' readonly="true" autocomplete="off" maxlength="48" style="width:120px"  value="%{binbean.pinverificationkey1}"  /></td>
			</tr>
			<tr>
					<td>PIN VERIFICATION KEY2</td>
					<td> : <s:textfield name="pvk2" id="pvk2" onkeypress='return isNumberKey(event)' readonly="true" autocomplete="off"  maxlength="48" style="width:120px"  value="%{binbean.pinverificationkey2}"    ></s:textfield></td>
					
					<td>PIN VERIFICATION KEY</td>
					<td> : <s:textfield name="pvk" id="pvk" onkeypress='return isNumberKey(event)'  autocomplete="off"  maxlength="48" style="width:120px"  value="%{binbean.pinverificationkey}"   ></s:textfield></td>
			
			</tr>
			<tr><td>&nbsp;</td></tr>
			
			<tr>
					<td>CVV KEY 1</td>
					<td> : <s:textfield name="cvv1" id="cvv1" onkeypress='return isNumberKey(event)'  autocomplete="off"  maxlength="48" style="width:120px" value="%{binbean.cvk1}" ></s:textfield></td>
					
					<td>CVV KEY 2</td>
					<td> : <s:textfield name="cvv2" id="cvv2" onkeypress='return isNumberKey(event)' autocomplete="off" maxlength="48" style="width:120px" value="%{binbean.cvk2}"  ></s:textfield></td>
			
			</tr>
			
			
			
		 </table>	
        
 

 <table border="0" cellpadding="0" cellspacing="0" width="25%" align="center">
			<tr align="center">
				<%-- <td><s:submit value="Submit" id="submit" name="submit" onclick="return validatebin_form ()" /> </td> --%>     
				<td><s:submit value="Submit" id="submit" name="submit"  /> </td>
				<td>
					<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>	 
				</td>
			</tr>

</table>
 

</s:form> 
 