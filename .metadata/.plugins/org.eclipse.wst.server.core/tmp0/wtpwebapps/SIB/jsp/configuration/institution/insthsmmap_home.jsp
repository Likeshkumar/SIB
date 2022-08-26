<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script>
$(function() {
	$( "#sortable" ).sortable();
	$( "#sortable" ).disableSelection();
	});

function kycEnable( kyc ){
	document.getElementById("order").hidden=false;
	document.getElementById("cancel").hidden=false;
	var cardno = document.getElementById("cardno");
	var custid = document.getElementById("custid");
	var divcard = document.getElementById("divcard");
	var divcust =  document.getElementById("divcust");
  
	if( kyc =='new'){
		divcard.style.display="block"; 
		divcust.style.display="none"; 
		document.getElementById("regtype").value="new";
		 
	}else{
		divcard.style.display="block"; 
		divcust.style.display="block"; 
		document.getElementById("regtype").value="kyc"; 
	}
	cardno.focus();
	 
	return false;
}

	
	function validateCard(){
		
		var chked = true;
		var accountno = document.getElementsByName("accountno");
		var regtype = document.getElementById("regtype");
		alert("hsm.length-->"+accountno.length+"daws"+regtype.value);
		var chked = true;
	
		var regtype = document.getElementById("regtype");
		alert("hsm.length-->"+accountno.length);
		for(var i=1;i<=accountno.length;i++)
		{
			var checkbox = document.getElementById("accountnos"+i).checked;
			alert("checkbox--> "+checkbox);
			
			if(checkbox)
			{
				chked = false;
				break;
			}
			else
			{
				alert("jigjgjhgjjgh");
				chked = true;
			}
		}
		
		alert("select atleast one check box");
		
		if( chked )
		{
			alert("select atleast one check box");
			errMessage(regtype,"select atleast one check box");
			return false;
		}
		return true;
	}

	
	function getcardslist()
	{
		//alert("dsds");
		document.reportsgen.action = "InsataddoncarddetailsInstCardRegisterProcess.do";
		document.reportsgen.submit;}
	
</script>
<style>
#divScrollform{
    position: absolute;
    border: 1px solid gray;
    padding: 1px;
    background: white;
    width: 1230px;
    height: 250px;
    overflow-y: scroll;
}
</style>
<div align="center">
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:if test="hasActionErrors()">
   <div style="color:red">
      <s:actionerror/>
   </div>
</s:if>
<form action="saveinstantcardInstCardRegisterProcess.do" method="post" autocomplete="off"  name="reportsgen"    namespace="/">
<input type="hidden" name="regtype" id="regtype" />
<s:hidden name="customername" value="%{cardregbean.custname}" />

 <s:hidden name="cardnumber" value="%{cardregbean.chn}" />  

 <s:hidden name="customerid" value="%{cardregbean.custidno}" /> 
 
  <s:hidden name="mothername" value="%{cardregbean.mothername}" />
  <s:hidden name="orgchn" value="%{cardregbean.org_chn}" />
 
 <s:hidden name="branchcode" value="%{cardregbean.branch_code}" /> 
  <s:hidden name="address1" value="%{cardregbean.paddress1}" /> 
   <s:hidden name="address2" value="%{cardregbean.paddress2}" /> 
      <s:hidden name="address3" value="%{cardregbean.paddress3}" />
      <s:hidden name="address4" value="%{cardregbean.paddress4}" />
      <s:hidden name="address5" value="%{cardregbean.paddress5}" /> 
       <s:hidden name="address6" value="%{cardregbean.paddress6}" />
        
            <s:hidden name="DOB" value="%{cardregbean.dob}" /> 
            <s:hidden name="orderreferno" value="%{cardregbean.order_ref_no}" />
            <s:hidden name="cardcurr" value="%{cardregbean.card_ccy}" />
            
             <s:hidden name="city" value="%{cardregbean.city}" />
        
         <s:hidden name="sex" value="%{cardregbean.gender}" /> 
         <s:hidden name="phonenumber" value="%{cardregbean.phoneno}" /> 
           <s:hidden name="emailid" value="%{cardregbean.email}" /> 
            
            <s:hidden name="hashcard" value="%{cardregbean.hascard}" /> 
            <s:hidden name="chnmask" value="%{cardregbean.chn_msk}" /> 
           <s:hidden name="msterorprobbin" value="%{cardregbean.msterbin}" /> 
            
            
             <s:hidden name="accountno" value="%{cardregbean.accountno}" /> 
              <s:hidden name="codeprod" value="%{cardregbean.cod_prod}" />
              
              <s:hidden name="productcode" value="%{cardregbean.prod_code}" /> 
      
          
             <s:hidden name="wdl_lmt_amt" value="%{cardregbean.wdl_lmt_amt}" /> 
         <s:hidden name="wdl_lmt_cnt" value="%{cardregbean.wdl_lmt_cnt}" /> 
           <s:hidden name="pur_lmt_amt" value="%{cardregbean.pur_lmt_amt}" /> 
            <s:hidden name="pur_lmt_cnt" value="%{cardregbean.pur_lmt_cnt}" /> 
             
              <s:hidden name="lmt_based_on" value="%{cardregbean.lmt_based_on}" />
               <s:hidden name="encrptchn" value="%{cardregbean.encrptchn}" />
               
               <s:hidden name="cardtype" value="%{cardregbean.cardtype}" />
               <s:hidden name="subprodid" value="%{cardregbean.cardtype}" /> 
                 <s:hidden name="card_ccy" value="%{cardregbean.card_ccy}" /> 
              
              
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef" cellpadding="0" align="center" width="40%" cellspacing="0"  >
		<!-- <tr >
			<td align="center" colspan="4" >
	         	
	         		<input type="radio" name="inputtype" id="cardno1" value="cardno" checked  onclick="showEntertype(this.value)"/> New Customer
	          
	          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          		<input type="radio" name="inputtype" id="customerid" value="customerid" onclick="showEntertype(this.value)" />Existing Customer
	         </td>
			 	
		</tr> -->
		
		
		
		
		<tr id="cardrow">
			 <td align="left" colspan="2">&nbsp;
			 	<div style="width:400px;" id='divcard'>
			 		Mapping Card Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  <input type='text' name='cardno' id='cardno' value="${cardno}" maxlength='19' />
			 	</div>
			 	 <s:if test="%{!cardno.isEmpty()}">
			 	 <td>
			 	<div style="width:400px;" id='divcard'>
			 		New Card Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :  <input type='text' name='newcardno' id='newcardno'  maxlength='19' />
			 	</div>
			 	</td>
			 	</s:if>
			</td>  
		</tr>
		
		
		
	</table>

	<table border="0" cellpadding="0" cellspacing="0" width="15%" >
		<tr>
		<td>
			<s:submit type="Submit" name="Search" id="Search" value="Search" onclick="return getcardslist(this.form)"/>
		</td>
			<%-- <td>
				<s:submit value="Submit" name="order" id="order" onclick="return validateCard()"/> 
				</td> --%>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		  <s:if test="%{!cardno.isEmpty()}">
		<td>
			<s:submit type="Submit" name="Submit" id="Submit" value="AddCard" onClick=" return validateCard(this.form)"/>
		</td>
		</s:if>
		</tr>
</table>

<div id="divScrollform" class="CardOrder">

<%--  <s:if test="%{cardregbean.Chn}"> --%>
  <s:if test="%{!cardno.isEmpty()}">
<!-- <table width="100%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  > -->
	
		<table border="0" cellpadding="" cellspacing="0" width="25%" align="center" class="filtertab">

<%-- 		<tr>  <td > CARD NUMBER: </td><td class="textcolor"><s:property value="cardregbean.chn_msk"/></td><td></td></td>
 --%>		<%--  <td> ACC  NUMBER: </td><td class="textcolor"><s:property value="cardregbean.accountno"/></td><td></td></td> --%>
		<tr>
	 	<td> Acc NumberS </td>
	 	<td style="text-align:center;"  align="center"> 
	 		<ul id="sortable">
	 		
	 	 	<% int x=0; %>
	 		<s:iterator value="allacounts">
	 			<% x++; %> 
	 			 <li class="ui-state-default" > <input type='checkbox' name="accountno" value="${ACCOUNTNO}~${ACCOUNTNO}" id="accountnos<%=x%>"  />  ${ACCOUNTNO}   </li> 
	 		</s:iterator>
			  
			 
			</ul>
			
<%-- 			<small style='font-size:9px'>* Drag the field up for the primary HSM</small>
 --%>	 	</td>
	 </tr>
	 
		
		
		
</table>
<br>

<table border="1" cellpadding="0" cellspacing="0" width="80%" align="center" class="filtertab">	 
 
	
	 	<tr>
			<td>Customer Name</td>
			<td class="textcolor"> <s:property value="cardregbean.custname"/>   </td>
			
			     
	   	 <td> Customer Id </td>
			  <td class="textcolor" > <s:property value="cardregbean.custidno"/>   </td>
			
			
			<td>Branch Code</td>
			 <td class="textcolor" > <s:property value="cardregbean.branchcode"/>   </td>
			 
				
	 	
	 		 <td  > Address1 </td>
			  <td class="textcolor"> <s:property value="cardregbean.paddress1"/>   </td>
	 	
			<td  >Address 2</td>
			  <td class="textcolor"> <s:property value="cardregbean.paddress2"/>   </td>
			 
			
			 <td>Address 3</td> 
			 <td class="textcolor" > <s:property value="cardregbean.paddress3"/>   </td> 
			
			  						 
		</tr>	  
	    
	 
	 
	 <tr>
			
			 
			 <td  >Address 4</td>
			  <td class="textcolor"> <s:property value="cardregbean.paddress4"/>   </td>
			
			 <td>Address 5</td> 
			 <td class="textcolor" > <s:property value="cardregbean.paddress5"/>   </td> 
			  						 
			 <td>Address 6</td> 
			 <td class="textcolor" > <s:property value="cardregbean.paddress6"/>   </td>
			 
 						 
			   <%--  <td>Gender</td>
			  <td class="textcolor" > <s:property value="cardregbean.gender"/>   </td> --%>
			  
		    <td>Date Of Birth</td>
			   <td class="textcolor" > <s:property value="cardregbean.dob"/>   </td>
			   	 
			 
			<td >Mobile</td>
			<td class="textcolor" > <s:property value="cardregbean.phoneno"/>   </td>
			 

			<td >E Mailid </td>
				<td class="textcolor" > <s:property value="cardregbean.email"/>   </td>
												
			 
	    </tr> 
		 
		
		<tr>
		
			
	
	
		<td >WDL_LMT_AMT</td>
			<td class="textcolor" > <s:property value="cardregbean.wdl_lmt_amt"/>   </td>

		<%-- <tr>
			  <td>APP_DATE</td>
			  <td class="textcolor" > <s:property value="cardregbean.APP_DATE"/>   </td> --%>
			  
					 
	  
	   
	     <td >WDL_LMT_CNT</td>
			<td class="textcolor" > <s:property value="cardregbean.wdl_amt_cnt"/>   </td>
			
			
			 <td>PUR_LMT_AMT</td>
			  <td class="textcolor" > <s:property value="cardregbean.pur_lmt_amt"/>   </td>
			  				 			 			
			<td >PUR_LMT_CNT</td>
			<td class="textcolor" > <s:property value="cardregbean.pur_lmt_cnt"/>   </td> 
				 
			 
			<td >LMT_BASED_ON</td>
			<td class="textcolor" > <s:property value="cardregbean.lmt_based_on"/>   </td>
			
			<td >PRODUCT DESC</td>
			<td class="textcolor" > <s:property value="cardregbean.productdesc"/>   </td>
		</tr>
		<tr>
			 			 	   		  			 
		
		<td >CURRENCY</td>
			<td class="textcolor" > <s:property value="cardregbean.card_ccy"/>   </td>
			 
					
				
		<td >CARD TYPE</td>
			<td class="textcolor" > <s:property value="cardregbean.cardtypedesc"/>   </td>
		</tr>	   
		
</table>
      </s:if>
			 	
	 	
		 	
	 	<s:else>

<table border='0' cellpadding='0' cellspacing='0' width='80%'
	style='text-align: center;'>
	<tr>
		<td style='text-align: center;'>
			<font color="green"><b>Get the card details ...</b></font>	
		</td>
	</tr>
</table>  
</s:else> 
</div>
</form>

</div>