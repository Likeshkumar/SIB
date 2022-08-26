<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
  
 
<script type="text/javascript" src="js/jquery/jquery-1.10.1.min.js"></script> 
<!--script src="js/jquery.lint.js" type="text/javascript" charset="utf-8"></script-->
 <script type="text/javascript" src="js/fantacyimage/jquery.fancybox.js?v=2.1.5"></script>
	<link rel="stylesheet" type="text/css" href="js/fantacyimage/jquery.fancybox.css?v=2.1.5" media="screen" />
	<script type="text/javascript">
		$(document).ready(function() { 
			
			
			$("#fancybox-manual-a").click(function() {
				var imgpath = document.getElementById('imagephotopath').value;  
				$.fancybox.open( imgpath );
			}); 
			
			$("#fancybox-manual1-a").click(function() {
				var imgpath = document.getElementById('signaturepath').value;  
				$.fancybox.open( imgpath );
			}); 
			
			
			$("#fancybox-manual2-a").click(function() {
				var imgpath = document.getElementById('idproofpath').value;  
				$.fancybox.open( imgpath );
			}); 
			
		});
	</script>
	<style type="text/css">
		.fancybox-custom .fancybox-skin {
			box-shadow: 0 0 50px #222;
		}

		body {
			max-width: 700px;
			margin: 0 auto;
		}
	</style>
 
 <!--  onsubmit="return validateFilter()" -->
<s:form action="SaveCustomerRegInstCardRegisterProcess" enctype="multipart/form-data"   name="custinfoform" id="custinfoform" autocomplete = "off"  namespace="/">

 <s:hidden name="customername" value="%{cardregbean.custname}" />

 <s:hidden name="cardnumber" value="%{cardregbean.Chn}" />  
 <s:hidden name="orderreferno" value="%{cardregbean.order_ref_no}" />
              <s:hidden name="hashcard" value="%{cardregbean.hascard}" /> 
            <s:hidden name="chnmask" value="%{cardregbean.chn_msk}" /> 
 <s:hidden name="customerid" value="%{cardregbean.custid}" /> 
 
  <s:hidden name="mothername" value="%{cardregbean.mothername}" />
 
 <s:hidden name="branchcode" value="%{cardregbean.cardcollbranch}" /> 
  <s:hidden name="address1" value="%{cardregbean.paddress1}" /> 
   <s:hidden name="address2" value="%{cardregbean.paddress2}" /> 
      <s:hidden name="address3" value="%{cardregbean.paddress3}" />
      <s:hidden name="address4" value="%{cardregbean.paddress4}" />
      <s:hidden name="address5" value="%{cardregbean.paddress5}" /> 
       <s:hidden name="address6" value="%{cardregbean.paddress6}" />
        
            <s:hidden name="DOB" value="%{cardregbean.dob}" /> 
           
           
           
            <s:hidden name="cardcurr" value="%{cardregbean.card_ccy}" />
            
             <s:hidden name="city" value="%{cardregbean.city}" />
        
         <s:hidden name="sex" value="%{cardregbean.gender}" /> 
         <s:hidden name="phonenumber" value="%{cardregbean.phoneno}" /> 
           <s:hidden name="emailid" value="%{cardregbean.email}" /> 
            
           
            
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
              
         
<table border="0" cellpadding="" cellspacing="0" width="75%" align="center" class="filtertab">

		<tr>  <td > CARD NUMBER: </td><td class="textcolor"><s:property value="cardregbean.chn_msk"/></td><td></td></td>
		 <td> ACC  NUMBER: </td><td class="textcolor"><s:property value="cardregbean.accountno"/></td>
		 
		</tr>
		
</table>
<br>

<table border="1" cellpadding="0" cellspacing="0" width="120%" align="center" class="filtertab">	 
 
	
	 	<tr>
			<td>Customer Name</td>
			<td class="textcolor"> <s:property value="cardregbean.custname"/>   </td>
			
			     
	   	 <td> Customer Id </td>
			  <td class="textcolor" > <s:property value="cardregbean.custid"/>   </td>
			
			
			<td>Branch Code</td>
			 <td class="textcolor" > <s:property value="cardregbean.cardcollbranch"/>   </td>
			 
							 
			 	
			<%-- <td>Account Product Code</td>
			 <td class="textcolor" > <s:property value="cardregbean.cOD_PROD"/>   </td> --%>
					 
					
			<%-- 		 <td>Mother Name </td>
					  <td class="textcolor" > <s:property value="cardregbean.mothername"/>   </td> --%>
		 	 
			
	   </tr>
	 	 	 
	    
	 
	 	<tr>
	 	
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
			 
 						 
			  
	    </tr> 
		 
		
		<tr>
		
			
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
			  				 			 			
			
		</tr>
		<tr>
			 			 	   		  			 
			
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
		</tr>	 
					
				
		<td >CARD TYPE</td>
			<td class="textcolor" > <s:property value="cardregbean.cardtypedesc"/>   </td>
		</tr>	   
		
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order"  onclick="return customerinfo_checker()" /><!--  -->
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>


 
</s:form>
 
 