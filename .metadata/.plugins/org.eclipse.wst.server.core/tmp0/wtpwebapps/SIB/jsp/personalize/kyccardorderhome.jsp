<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function coppyEmbosstoEncode(event,value){
	//alert("Event "+event+"  value===>"+value);
	var s = alphabets(event);
	//alert(" ssss "+s);
	if(s==true)
		{
			//alert("Value is Aplpha"+value);
			document.getElementById("encode").value = value;
			return true;
		}else{
			//alert("Invalid");
			return false;
		}
}

function getProductDetails( cardno ){ 
	var url = "getCardProductDetailsPersonalCardOrderAction.do?cardno="+cardno; 
	var result = AjaxReturnValue(url);
	cardobj = JSON.parse( result );
	var respcode = cardobj.RESPCODE ;
	
	var branchcode =  document.getElementById("branchcode");
	var product = document.getElementById("cardtype");
	var subproductlist = document.getElementById("subproductlist");
	
	if( respcode == 0 ){ 
		
		
		branchcode.innerHTML=cardobj.BRANCHOPTION;
		product.innerHTML=cardobj.PRODUCTOPTION;
		subproductlist.innerHTML=cardobj.SUBPRODUCTOPTION;
		errMessage(branchcode,  "");
	}else{
		errMessage(branchcode,  cardobj.RESPREASON );
		branchcode.innerHTML="<option value='-1'>--Select Branch--</option>";
		product.innerHTML="<option value='-1'>--Select Product--</option>";
		subproductlist.innerHTML="<option value='-1'>--Select Sub-Prodiuct--</option>";
		 return false;
	}
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="supplimentCardDetailsPersonalCardOrderAction"  name="personalorderform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<!-- saveKYCCardorderPersonalCardOrderAction -->
<table border="0" cellpadding="0" cellspacing="5" width="40%" id="maxcount" class="viewtable formtable">
	
	<tr>
		<td>Existing Card Number </td>
		<td> :
			<input type="text" name="existingcardno" id="existingcardno"  maxlength="25" onchange="return getProductDetails( this.value )">
		</td>
	</tr>
	
	<tr>
				<td class="fnt"> Select Branch</td>
				<td> : 
					<select  name="branchcode" id="branchcode" >
						<option value="-1">--Select Branch--</option>
					</select>
				 
				
				</td>
	</tr>
	
	
	<tr>
		<td class="fnt">
		Select Product
		</td>
		<td> :
 				<select name="cardtype" id="cardtype" onchange="return Gettingsubproduct();" >
	 				<option value="-1">--Select Product--</option>
	 			 
 				</select>
		</td>
    </tr>
    <tr>
    </tr>
    <tr> 
    	<td id="subname" class="fnt">
    		Select Sub-Product
    	</td>
    	<td class="fnt"> :
    		 
   			<select name="subproductlist" id="subproductlist">
   				<option value="-1">--Select Product--</option>
   			</select>
   		 
    	</td>
    </tr>

    	
   <!--  <tr>	
		<td>
		No. Of Cards
		</td>
		<td>
   	   		<input type="text" name="Count" id="Count" value="1" readonly="true"  maxlength="5" onKeyPress="return numerals(event);">
		</td>
	</tr> -->
	<input type="hidden" name="Count" id="Count" value="1" readonly="true"  maxlength="5" onKeyPress="return numerals(event);">
	<tr>
		<td>Embossing Name </td>
		<td> :
			<input type="text" name="emposs" id="emposs"  maxlength="25" onKeyPress="return alphabets(event);" onkeyup="return coppyEmbosstoEncode(event,this.value);" >
		</td>
	</tr>
	<tr>
		<td>Encoding Name   </td>
		<td> :
			<input type="text" name="encode" id="encode"  maxlength="25" onKeyPress="return alphabets(event);">
		</td>
	</tr>
	

		
<!--  	
		<tr>
		    <td align="left">Application Date<b class="mand">*</b></td>
			<td>
				<input type="text" name="appdate" id="appdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:180px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.personalorderform.appdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
	   <tr>
		    <td align="left">Application No<b class="mand">*</b></td>
			<td><input type="text" name="appno" id="appno" maxlength="30" onKeyPress=" return alphanumerals(event);"></td>
	</tr>	
	
	<tr> 
    	<td>
    		<div id="maxcount">
 				
    		</div>
    	</td>
    </tr>	
    -->
    
    
</table>
 <!-- <input type="hidden" id="cust_reg_req" name="cust_reg_req" value="Y">  -->
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>
			<s:submit value="Next" name="next_process" id="next_process" onclick="return validateValues();"/>
		</td>
		<td>
			<!-- <input type="button" name="cancle" value="Cancel" onclick="return forwardtoCardOrderPage();"> -->
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>

