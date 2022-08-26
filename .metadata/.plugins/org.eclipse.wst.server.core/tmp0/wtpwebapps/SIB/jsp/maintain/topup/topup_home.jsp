<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<script>
	function checkFundtransfer( fundid ){
		
		/* var topup = document.getElementById("topup");
		var cardtocard = document.getElementById("cardtocard");
		
		var smalldt =  document.getElementById("smalldt"); */
		
		var productlistrow = document.getElementById("productlistrow");
		
		var text = " Upload the deposited amount to Consumer Wallet. File format .xls, .csv <br/>";
		if( fundid == "topup"){  
			 productlistrow.style.display="table-row"; 
			 text = text + "File name start with Corporate code ( Ex. xx_ddmmyyyy.xls )";
			 smalldt.innerHTML= text;
		}else if( fundid == "$MB-REV"){ 
			 productlistrow.style.display="table-row"; 
			 text = text + "File name start with Today date ( Ex. ddmmyyyy.xls )";
			 smalldt.innerHTML= text;
		}else{ 
			productlistrow.style.display="none";
			text = text + "File name start with Today date ( Ex. ddmmyyyy.xls )";
			smalldt.innerHTML= text;
		}
		
		/* if( topup.checked() ){
			alert( "topup");
			productlistrow.style.dispaly="table-row";
		}else if ( cardtocard.checked() ){
			productlistrow.style.dispaly="none";
			alert( "card to card ");
		}else{
			
			alert( "invalid selection");
		} */
		
	}
	
	function validateForm(){
		var bankcode = document.getElementById("corp_prod");
		var topup = document.getElementById("topup");
		var cardtocard = document.getElementById("cardtocard");
		var walletupload = document.getElementById("walletupload");
		
		
		if( topup.value == "-1" ){
			 errMessage(topup, "Select the action.....");return false;
		}
		
		 
		if( topup.value != "cardtocard" ){
			if( bankcode ){
				if( bankcode.value == "-1"){ errMessage(bankcode, "Select Bin ...");return false;}
			}  
		}
		
		if( walletupload.value == ""){
			errMessage(walletupload, "Upload file is empty...");return false;
		}
		
		parent.showprocessing();
	}
	
 
		
	 
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="uploadConsumerWalletTopUpCard.do"  name="orderform"  enctype="multipart/form-data"  onsubmit="return validateForm()"  autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="40%" class="formtable">	 
	
	<tr><td>
		 Select Action </td><td>	<s:select  list="#{'-1':'Select','topup':'Topup','cardtocard':'Card to card'}" 
		   name="fundtransfer"  id="topup"  onchange="checkFundtransfer(this.value)" ></s:select>
	<%-- ,'$MB-REV':'Bpesa-Revenue' --%> 
		<!--  Select Action </td><td><input type="radio" name="fundtransfer" id="topup" value="topup" onclick="checkFundtransfer(this.id)" > Topup &nbsp;&nbsp;&nbsp;
			<input type="radio" name="fundtransfer" id="cardtocard" value="cardtocard"  onclick="checkFundtransfer(this.id)" > Card to card -->
	 </td> 
		  
		
	 
	
	<tr id="productlistrow" style="display:none"><td> Bank </td><td> : 
		<%-- <s:select name="corp_prod" id="corp_prod"  list="topbean.coroporateproductlist" listKey="SUB_PROD_ID" listValue="SUB_PRODUCT_NAME" headerKey="-1" headerValue="-SELECT-" /> --%>
		<select  name="corp_prod" id="corp_prod" > 
			<option value="-1"> -SELECT - </option>
			<!-- <option value="$FLOAT"> 999-Corporate Card Load </option> -->
		<s:iterator value="topbean.binlist">
			
			<option value='${BIN}'> ${BIN_DESC} </option>
		</s:iterator>
		</select>
	</td>  
	
	
	<tr>
		<td> Upload File </td>
		<td> : <s:file name="walletupload" id="walletupload"  />  <br/>
		
			<small id="smalldt" class="dt"> 
			 </small>
		</td> 
	</tr>
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return validFilter()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 