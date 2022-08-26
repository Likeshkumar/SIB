<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">
/* $(function() {
	
	//alert( parseInt($("#actionhead option").length) )
    $("#actionhead").attr("size", parseInt($("#actionhead option").length)); 
});
 */
 
/*  $('#actionhead').change(function() {
	  alert('The option with value ' + $(this).val() + ' and text ' + $(this).text() + ' was selected.');
	});
  */
  function disablePage()
  {
   
     var controlType;
   
     for (var i = 0; i < document.forms[0].elements.length; i++)
   
    { 
         controlType = document.forms[0].elements[i].type;
        // alert(controlType);
          if (controlType == 'select-one' )
   
              {
   document.forms[0].elements[i].readOnly = true;
   document.forms[0].elements[i].disabled = true;
               }
   
                  if (controlType == 'text' || controlType=='hidden')
              {
                  document.forms[0].elements[i].readOnly = true;
               }
   
     if (controlType == 'checkbox')
               {
     document.forms[0].elements[i].disabled = true;
                }
                   if (controlType == 'textarea')
              {
  document.forms[0].elements[i].readOnly = true;
              }
      } 
     
  }
     
 function loadSubAction( actionhead ){
	//alert( actionhead ); 
         
  	var url = "getsubActionListCardMaintainReport.do?actionhead="+actionhead;
	var result = AjaxReturnValue(url);     
	document.getElementById("subaction").innerHTML=result;
} 
  

/*  function getSubProd(selprodid ){  
 		var url = "getaccountypeInstCardRegisterProcess.do?prodid="+selprodid+"&status=1";   
 		//alert(url);
 		result = AjaxReturnValue(url);   
 		//alert(result);
 		document.getElementById("accttype").innerHTML = result;  
 		
 		//var hidsubproduct = document.getElementById("hidsubproduct").value; 
 		//document.getElementById("subproduct").value=hidsubproduct;
 		//getLimitBySubProduct(hidsubproduct);
 		//getFeeBySubProduct(hidsubproduct);
 		   
 }  */
 
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

 function validateForm(form){
	 
	 var fromdate = document.getElementById("fromdate");
	  var todate = document.getElementById("todate");
	  var empmolid = document.getElementById("empmolid");
	  var legacy = document.getElementById("legacy");
	  var batchid = document.getElementById("batchid");
	  var compnyid = document.getElementById("compnyid");
	  
	  if( actbased.checked ){
		  if(  $("select[name='actionhead'] option:selected").length == 0 ){
			  errMessage(actionhead, "Select Any Action");  return false; 
		  } 
		  if(  $("select[name='actionhead'] option:selected").length > 0 ){
			  if(  $("select[name='subaction'] option:selected").length == 0 ){
				  errMessage(subaction, "Select Any Sub-Action");  return false; 
			  }
			   
		  } 
	  }

	 return true;
 }
 
 function getSubProd(accounttype ){   
	 //alert(accounttype);
		var url = "getaccountypeInstCardRegisterProcess.do?accountprodid="+accounttype+"&status=1";   
		//alert(url);
		result = AjaxReturnValue(url);   
		//alert(result);
		document.getElementById("accttype").innerHTML = result;  
		
		//var hidsubproduct = document.getElementById("hidsubproduct").value; 
		//document.getElementById("subproduct").value=hidsubproduct;
		//getLimitBySubProduct(hidsubproduct);
		//getFeeBySubProduct(hidsubproduct);
		   
	} 
 function enabledisbale(val) {
		
		if(val=='empmolopt'){
			
		    
			document.getElementById("legacy").disabled = true;
			document.getElementById("empmolid").disabled = false;
			
		    document.getElementById("empmolid").focus();
		    document.getElementById("batchid").disabled = true;	
		    document.getElementById("fromdate").disabled = true;	
		    document.getElementById("todate").disabled = true;	
		    document.getElementById("companyname").disabled = true;
		    document.getElementById("compnyid").disabled = true;
		   
		        
		}
	    
		if(val=='acctnoopt'){
			document.getElementById("empmolid").disabled = true;
		    document.getElementById("legacy").disabled = false;
		    document.getElementById("batchid").disabled = true;
		    document.getElementById("legacy").focus();
		    document.getElementById("fromdate").disabled = true;	
		    document.getElementById("todate").disabled = true;
		    document.getElementById("companyname").disabled = true;
		    document.getElementById("compnyid").disabled = true;
		}
		 if(val=='batchidopt'){
			 document.getElementById("companyname").disabled = true;
			    document.getElementById("compnyid").disabled = true;
			document.getElementById("batchid").disabled = false;
		    document.getElementById("empmolid").disabled = true;
		    document.getElementById("legacy").disabled = true;	
		    document.getElementById("batchid").focus();
		    document.getElementById("fromdate").disabled = false;	
		    document.getElementById("todate").disabled = false;	
		} 
		 if(val=='comapnyidopt'){
			 document.getElementById("companyname").disabled = true;
			    document.getElementById("compnyid").disabled = false;
			document.getElementById("batchid").disabled = true;
		    document.getElementById("empmolid").disabled = true;
		    document.getElementById("legacy").disabled = true;	
		    document.getElementById("compnyid").focus();
		    document.getElementById("fromdate").disabled = true;	
		    document.getElementById("todate").disabled = true;	
		} 
		 if(val=='companyopt'){
			 document.getElementById("companyname").disabled = false;
			    document.getElementById("compnyid").disabled = true;
			document.getElementById("batchid").disabled = true;
		    document.getElementById("empmolid").disabled = true;
		    document.getElementById("legacy").disabled = true;	
		    document.getElementById("companyname").focus();
		    document.getElementById("fromdate").disabled = false;	
		    document.getElementById("todate").disabled = false;	
		} 
	}

function enableField(selectedval)
	{
		textcard = document.getElementById("textcard");
		
		if (selectedval == "cardnoopt")
		{
			//alert(selectedval);
			textcard.innerHTML="Branch";
			document.getElementById("cardno").value="";
			selectedtype.value="empmolid";
			
		}
		else if(selectedval =="acctnoopt"){
			
			//alert(selectedval);
			textcard.innerHTML="Product";
			document.getElementById("cardno").value="";								
			selectedtype.value="acctnum";
			
		}
		 else {
			//alert(selectedval);
			textcard.innerHTML="Overall";
			document.getElementById("cardno").value="";			
			selectedtype.value="custnum";
		} 
						
	}

/* function Ajex_CardsSearch(){
	  var fromdate = document.getElementById("fromdate").value;
	  var todate = document.getElementById("todate").value;
	  var cardno = document.getElementById("cardno").value;
	  var empmolid = document.getElementById("empmolid").value;
	  var legacy = document.getElementById("legacy").value;
	  var batchid = document.getElementById("batchid").value;
	  var compnyid = document.getElementById("compnyid").value;
	  var companyname = document.getElementById("companyname").value;
	  //alert(companyname);
	  
	  var url="",result="";
	  url = "authCardgenordersPersonalCardprocessAction.do?fromdate="+fromdate+"&todate="+todate+"&empmolid="+empmolid+"&legacy="+legacy+"&batchid="+batchid+"&compnyid="+compnyid+"&companyname="+companyname+"&cardno="+cardno+"";
	  result = AjaxReturnValue(url);
	  //alert(result);
	  $('.CardOrder').html(result);
} */

function Ajex_CardOrderSubmit(type){
	var branchcode  = document.getElementById("branchcode");
	if( branchcode ){ if( branchcode.value == "ALL"){errMessage(branchcode,"Select Branch Code!") ; return false; }}
	var gender  = document.getElementById("gender");
	//alert(gender.value);
	if( gender ){ if( gender.value == "-1"){errMessage(gender,"Select Gender !") ; return false; }}
	
	var cusid  = document.getElementById("customerid");
	if( cusid ){ if( cusid.value == ""){errMessage(cusid,"Enter Customer Id !") ; return false; }}

	var encname  = document.getElementById("customername");
	if( encname ){ if( encname.value == ""){errMessage(encname,"Enter Encoding Name !") ; return false; }}
	var mailadd1  = document.getElementById("address1");
	if( mailadd1 ){ if( mailadd1.value == ""){errMessage(mailadd1,"Enter Mail Address1 !") ; return false; }}
	var dob  = document.getElementById("dob");
	if( dob ){ if( dob.value == ""){errMessage(dob,"Enter DOB !") ; return false; }}
	var maritalstatus  = document.getElementById("maritalstatus");
	
	if( maritalstatus )
	{ 
		alert(maritalstatus);
		if( maritalstatus.value == "-1")
		{
			errMessage(maritalstatus,"Select Merital Status !") ; 
			return false;
			}
		if(maritalstatus.value=="UnMarried"){
			//alert("sardar");
		}
		}
	var acctprodcode  = document.getElementById("acctprodcode");
	if( acctprodcode ){ if( acctprodcode.value == "ALL"){errMessage(acctprodcode,"Select Account Type!") ; return false; }}
	var accnumber  = document.getElementById("accountno");
	if( accnumber ){ if( accnumber.value == ""){errMessage(accnumber,"Enter Account Number !") ; return false; }}
	var currcode  = document.getElementById("currcode");
	if( currcode ){ if( currcode.value == "ALL"){errMessage(currcode,"Select Currency Code!") ; return false; }}


	
}
function getcardslist()
{
	document.reportsgen.action = "manualreginstantInstCardRegisterProcess.do";
	document.reportsgen.submit;}
</script>
<style>
#divScrollform{
    position: absolute;
    border: 1px solid orange;
    padding: 1px;
    background: white;
    width: 1200px;
    height: 550px;
    overflow-y: scroll;
}
</style>
<%-- <script type="text/javascript">

     $(document).ready(function() {

        $("input").click(function(){

            var favorite = [];

            $.each($("input[name='OrderRefno']:checked"), function(){            

                favorite.push($(this).val());

            });

            alert("My favourite sports are: " + favorite.join(", "));
            
      	  var url="",result="";
    	  url = "personalCardgenerationprocessPersonalCardprocessAction.do?Order_Ref_no="+favorite+" ";
    	  result = AjaxReturnValue(url);
    	  alert(result);
        });

    }); 

</script> --%>
</head>
 <jsp:include page="/displayresult.jsp"/>
 <% String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ")==null?"":(String)session.getAttribute("DATEFILTER_REQ"); 
String act = request.getParameter("act")==null?"":request.getParameter("act");
%>



	<s:form action="SaveCustomerRegInstCardRegisterProcess.do" autocomplete="off"  name="reportsgen"  namespace="/">
		 
		 <input type="hidden" name="act" value="<%=act %>"/>
		
		

	<table border='0' cellpadding='0' cellspacing='0' width='80%' class="" align="center">
		


          <s:hidden name="insttypereg" value="%{cardregbean.insttypereg}" /> 
				
		
		 <tr  align="center">
          <td>
          
          </td>
          <%-- <td>
           <td >Card Reg Type: </td><td class="textcolor"><s:property value="cardregbean.insttypereg"/></td>
         
          <td>
           &nbsp;&nbsp;&nbsp;&nbsp;
          </td>
       <td>
           &nbsp;&nbsp;&nbsp;&nbsp;
          </td>
          <td>
           &nbsp;&nbsp;&nbsp;&nbsp;
          </td>
          <td>
           &nbsp;&nbsp;&nbsp;&nbsp;
          </td>
          <td>
           &nbsp;&nbsp;&nbsp;&nbsp;
          </td> --%>
			 <td>  Card No :	<input type='text' id='cardno' name='cardno'  value="${cardregbean.cardno}"/>
			
			</td>
			</tr>
	
	</table>

		

<table border="0" cellpadding="0" cellspacing="0" width="20%" align="center">
	<tr>
		<td>
			<%-- <s:submit value="Next" name="Next" onClick=" return validateForm(this.form)"/> --%>
			<s:submit type="Submit" name="Search" id="Search" value="Search" onclick="return getcardslist(this.form)"/>
		</td>
		<%-- <td>
			<s:submit value="Submit" name="authorize" onClick=" return Ajex_CardOrderSubmit(this.form)"/>
			<!-- <input type="button" name="Submit" id="Submit" value="Submit" onclick="return Ajex_CardOrderSubmit()"/> -->
		</td>--%>
	
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td> 
	</tr>
</table>
	

<div id="divScrollform" class="CardOrder">

 <s:if test="%{cardregbean.cardeligibility=='ELIGIBLE'}">
<table width="100%" cellpadding="0"  border="0"  cellspacing="0" align="center"  >
	
		
		 
		
        <tr>
        
         <s:hidden name="cardnumber" value="%{cardregbean.Chn}" />  
 <s:hidden name="orderreferno" value="%{cardregbean.order_ref_no}" />
              <s:hidden name="hashcard" value="%{cardregbean.hascard}" /> 
            <s:hidden name="chnmask" value="%{cardregbean.chn_msk}" /> 
             <s:hidden name="orgchn" value="%{cardregbean.cardno}" />
       
         <s:hidden name="encrychn" value="%{cardregbean.encrptchn}" />
         
        
          <s:hidden name="prodcode" value="%{cardregbean.prod_code}" />
           <s:hidden name="lmt_based_on" value="%{cardregbean.lmt_based_on}" />
           <s:hidden name="branchcode" value="%{cardregbean.branchcode}" />
          
                   
           <s:hidden name="productcode" value="%{cardregbean.prod_code}" />
            <s:hidden name="cardtype" value="%{cardregbean.cardtype}" />
             <s:hidden name="codeprod" value="%{cardregbean.cod_prod}" />
           
           
         
		<td>
		
		Branch Code 
					
					
		 				  <td class="textcolor"> <s:property value="cardregbean.branchcode"/>   </td>
					</tr>
		
		<tr>
		<td>
		 	Card</td>
			  <td class="textcolor"> <s:property value="cardregbean.cardno"/>   </td>
	 			
		   <td class="txt"> Gender <s:property value="dbtcustregbean.f"/> <span class="mand">*</span> </td> <td> 
		   <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="sex"   headerValue="-SELECT-" name="sex" value="%{dbtcustregbean.gender}" /> </td>
		 
		<td>	     
	   	Email</td>
	 			 <td> <s:textfield  id="emailid" name="emailid"  />
	 			 </td> 
		</tr>	 
				
				<tr>
				<%-- 
				<td>	     
	   	Lmt Based-on</td>
	   				  <td class="textcolor"> <s:property value="cardregbean.lmt_based_on"/>   </td> --%>
	   	<%-- 
	 			 <td>	     
	   	CustID <span class="mand">*</span></td>
	 			 <td> <s:textfield  id="customerid" name="customerid"  value="%{dbtcustregbean.customerid}" onkeyup="validateNumber('customerid',this.id,this.value)" />
	 			 </td>  --%>
	 			 <td>	     
	   	Enc Name <span class="mand">*</span></td>
	 			 <td> <s:textfield  id="customername" name="customername"  value="%{dbtcustregbean.customername}" onkeyup="chkChars('Street Name',this.id,this.value)" />
	 			 </td> 
	 			 <td>	
	   	Mail Add1 <span class="mand">*</span></td>
	 			 <td> <s:textfield  id="address1" name="address1" maxlength="17" value="%{dbtcustregbean.mailadd1}" onkeyup="chkChars('mailadd1',this.id,this.value)" />
	 			 </td> 
	 			 
	 			 </tr>	
	 			 <tr>
				
				<td>	     
	   	Card gen Date </td>
				<td class="textcolor" > <s:property value="cardregbean.cardgendate"/> (DD/MM/YY)  </td>
	 			
	 			 <td>	     
	   	Emb Name</td>
	 			 <td> <s:textfield  id="embname" name="embname"  value="%{dbtcustregbean.embname}" onkeyup="chkChars('embname',this.id,this.value)" />
	 			 </td> 
	 			 <td>	     
	   	Mail.Address2</td>
	 			 <td> <s:textfield  id="address2" name="address2"  value="%{dbtcustregbean.address2}" onkeyup="chkChars('mail2 ',this.id,this.value)" />
	 			 </td> 
	 			 
	 			 </tr>	
	 			 <tr>
				
				<td>	     
	   	TypeOfCard</td>
				<td class="textcolor" > <s:property value="cardregbean.cardtypedesc"/>   </td>
	 		
	 			 <td> Dob <span class="mand">*</span></td>
			<td><input type='text' id='dob' name='dob' value="${TimeToBeginningOfDay}" maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
				
			</td>
	 			 <td>	     
	   	Mail Add3</td>
	 			 <td> <s:textfield  id="address3" name="address3"  value="%{dbtcustregbean.address3}" onkeyup="validateNumber('address3',this.id,this.value)" />
	 			 </td> 
	 			 
	 			 </tr>	
	 			 <tr>
				
				<td>	     
	   	WithDrawAmt</td>
				<td class="textcolor" > <s:property value="cardregbean.wdl_lmt_amt"/>   </td>
	 			 
	 			 <td>	     
	   	Mother's/Spouse Name</td>
	 			 <td> <s:textfield  id="mothername" name="mothername"  value="%{dbtcustregbean.MotherORspouse}" onkeyup="chkChars('MotherORspouse',this.id,this.value)" />
	 			 </td> 
	 			 <td>	     
	   	Mail Add4</td>
	 			 <td> <s:textfield  id="address4" name="address4"  value="%{dbtcustregbean.mailadd4}" onkeyup="chkChars('mailadd4',this.id,this.value)" />
	 			 </td> 
	 			
	 			 </tr>	
	 			 <tr>
				
				<td>	     
	   	WithDrawCnt</td>
				<td class="textcolor" > <s:property value="cardregbean.wdl_lmt_cnt"/>   </td>
	 			 
	 			 <td>	     
	   	city</td>
	 			 <td> <s:textfield  id="city" name="city"  value="%{dbtcustregbean.city}" onkeyup="chkChars('city',this.id,this.value)" />
	 			 </td> 
	 			 <td>	     
	   Mail Add5</td>
	 			 <td> <s:textfield  id="address5" name="address5"  value="%{dbtcustregbean.mailadd5}" onkeyup="validachkCharsteNumber('mailadd5',this.id,this.value)" />
	 			 </td> 
	 			 
	 			 </tr>	
	 			 <tr>
				
				<td>	     
	   Expiry Date</td>
				<td class="textcolor" > <s:property value="cardregbean.expdate"/> (DD/MM/YY)</td>
 
	 			 <td>	     
	   Country</td>
	 			 <td> <s:textfield  id="encname" name="encname"  value="LAO" onkeyup="chkChars('encname',this.id,this.value)" />
	 			 </td> 
	 			 <td>	     
	   	 	Mobile/Res Ph.No</td>
	 			 <td> <s:textfield  id="phonenumber" name="phonenumber"  value="%{dbtcustregbean.phonenumber}" onkeyup="validateNumber('phonenumber ',this.id,this.value)" />
	 			 </td> 
	 			 
	 			 </tr>	
	 			<tr>
				
				<td>	     
	   	Product code</td>
	 			<td class="textcolor" > <s:property value="cardregbean.prod_code"/>   </td>

	 			 <td>	     
	   	Father Name</td>
	 			 <td> <s:textfield  id="fathername" name="fathername"  value="%{dbtcustregbean.fathername}" onkeyup="chkChars('fathername',this.id,this.value)" />
	 			 </td> 
	 			
	 			 
	 			 </tr>	
	 			  <tr>
				
				 <td class="txt"> Marital Status <s:property value="dbtcustregbean.f"/> <span class="mand">*</span> </td> <td> 
		   <s:select list="#{'M':'Married','UM':'UnMarried'}" id="maritalstatus"  headerKey="-1" headerValue="-SELECT-" name="maritalstatus" value="%{dbtcustregbean.maritalstatus}"  /> </td>
		 
	 			 <td>	     
	Spouce Db</td>
	 			 <td><input type='text' id='spoucedb' name='spoucedb' value="${TimeToBeginningOfDay}" maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.spoucedb,'dd-mm-yy',this);" title="Click Here to Pick up the date" align="left" border="0" width="15" height="17">

			</td>
	   
	   
	 			<td> Spouse Avry DOB </td>
			<td><input type='text' id='Spouseanvdob' name='Spouseanvdo' value="${TimeToBeginningOfDay}" maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.Spouseanvdob,'dd-mm-yy',this);" title="Click Here to Pick up the date"  align="left"  border="0" width="15" height="17">
				
			</td>
	 			 </tr>	
	 			 <tr>
	 	</table > 
	 	<table width="50%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable" >
	 	 <tr>
				
			
				<td>
		
		Acc Type
					<span class="mand">*</span>
					<td>
		 				<select name="acctprodcode" id="acctprodcode">
			 				<option value="ALL">ALL</option>
			 				<s:iterator  value="cardregbean.acctproduct">
			 					<option value="${ACCTTYPEID}">${ACCTTYPEID}-${ACCTTYPEDESC}</option>
			 				</s:iterator>
		 				</select>
				</td>
	 			 <td>	     
	AC.Number <span class="mand">*</span> </td>
	 			 <td> <s:textfield  id="accountno" name="accountno"  maxlength="18" value="%{dbtcustregbean.accountno}" onkeyup="validateNumber('accountno',this.id,this.value)" />
	 			 </td> 
	 			 
	 			 
	<%--  			 <td >	     
	   AC.Type </td>
	 			 <td> <s:textfield  id="accttype" name="accttype"  value="%{ACCT_PROD_DESC}" onkeyup="validateNumber('accttype',this.id,this.value)" />
	 			 </td>  --%>
	 			 
	 		
			 
	 			 
	 			
	 			 </tr>	
	 			 <tr>
	 			  <td>
		
		Currency
					<span class="mand">*</span>
					<td>
		 				<select name="currcode" id="currcode" >
			 				<option value="ALL">ALL</option>
			 				<s:iterator  value="cardregbean.currency">
			 					<option value="${NUMERIC_CODE}">${NUMERIC_CODE}-${CURRENCY_DESC}</option>
			 				</s:iterator>
		 				</select>
					</td>
	 			   <td>	     
	  Acct Id</td>
	 			 <td> <s:textfield  id="acctid" name="acctid"  value="PRIMARY" onkeyup="validateNumber('acctid',this.id,this.value)" />
	 			 </td>
	 			 </tr>
	 	
	 	</table>
	 	
	 	
	 	<table border="0" cellpadding="0" cellspacing="0" width="20%" align="center">
	<tr>
		
		<td>
			<s:submit value="Submit" name="authorize" onClick=" return Ajex_CardOrderSubmit(this.form)"/>
		</td>
	
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
	 	
			 	</s:if>
	 	<s:else>
 
<table border='0' cellpadding='0' cellspacing='0' width='80%'
	style='text-align: center;'>
	<tr>
		<td style='text-align: center;'>
			<font color="red"><b>	Search for Card Availability....</b></font>	
		</td>
	</tr>
</table>  
</s:else>
</div>
</s:form>
