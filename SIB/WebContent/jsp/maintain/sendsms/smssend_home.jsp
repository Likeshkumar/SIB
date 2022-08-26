<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="cache-control" content="no-cache" />
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function showExceptBlock( checkedval ){
	
	var exceprowid = document.getElementById("exceprowid");
	if( checkedval ){
		exceprowid.style.display="table-row";	
	}else{
		exceprowid.style.display="none";
	}
	
}

function selectMessageGroup( grpmsg){
	
	
	var cardrow = document.getElementById("cardrowid");
	var phonerow = document.getElementById("phonerowid");
	var binrow = document.getElementById("binrow");
	var productrow = document.getElementById("productrow");
	var subproductrow = document.getElementById("subproductrow");
	
	if( grpmsg == "$PRODUCTBASED"){
		cardrow.style.display="none";
		phonerow.style.display="none"; 
		binrow.style.display="table-row";
		productrow.style.display="table-row";
		subproductrow.style.display="table-row"; 
	}else if( grpmsg == "$CARDBASED"){
		cardrow.style.display="table-row";
		phonerow.style.display="none"; 
		binrow.style.display="none";
		productrow.style.display="none";
		subproductrow.style.display="none";
	}else if( grpmsg == "$MOBILEBASED"){
		phonerow.style.display="table-row";
		cardrow.style.display="none";		 
		binrow.style.display="none";
		productrow.style.display="none";
		subproductrow.style.display="none";
	}
	
}


function getSubProductList(prodid){
	var instid = document.getElementById("instid").value;	 
	var url = "getSubProductListReportgenerationAction.do?instid="+instid+"&prodid="+prodid.value; 
	var result = AjaxReturnValue(url); 
	document.getElementById("subprodid").innerHTML = result;
	
}

function Gettingproductlist(binvalue){
	//alert(binvalue);
	var instid = document.getElementById("instid").value;
	//alert(instid);
	var url = "getProductListbybinReportgenerationAction.do?binvalue="+binvalue.value+"&instid="+instid;
	//alert(url);
	var result = AjaxReturnValue(url); 
	//alert(result);
	document.getElementById("prodid").innerHTML = result;
	
}


function validateValues()
{
	
	 
	var bin = document.getElementById("bin");
	var subprodid = document.getElementById("subprodid");
	var prodid = document.getElementById("prodid");
	
	

	if(bin.value==""){
		errMessage(chn,"Please Enter the Bin Number");
		return false;
	}	
	if(prodid==""){
		errMessage(prodid,"Please Select the product");
		return false;
	}
	if(subprodid==""){
		errMessage(subprodid,"Please Select the Subproduct");
		return false;
	}
	 
	
	return true;
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="sendSMSToCardActionSMSSender.do"  name="transanalysesform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" class="formtable" cellspacing="0" width="80%" id="maxcount"  >
<input type='hidden' name='instid' id='instid' value='<%=(String)session.getAttribute("Instname")%>'/>
<tr>
	<td style="width:50%;color:red">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"  >
			<tr>
				<td class="fnt">
					Message Group
					</td>
					<td>
			 				: <select name="groupmsg" id="groupmsg" onchange="return selectMessageGroup(this.value)" >
				 				<option value="-1" selected="selected">--Select Card--</option>
				 				<option  value="$PRODUCTBASED" > Product Based </option>
				 				 <option  value="$CARDBASED" > Card Number </option>
				 				 <option  value="$MOBILEBASED" > Mobile Number </option>
			 				</select>
					</td>
			</tr> 
			<tr id="cardrowid" style="display:none">
					<td> Enter Card Number </td>
					<td>  &nbsp; <s:textarea name="cardnumber" id="cardnumber" maxlength="1024"/> <br/><small class="dt"> Comma(,) separate for mulitple card numbers</small>  	</td>
			</tr>
			
			<tr id="phonerowid" style="display:none">
					<td> Enter Mobile Numbers </td>
					<td>  &nbsp; <s:textarea name="mobilenumber" id="mobilenumber" maxlength="1024" /> <br/> <small class="dt"> Comma(,) separate for mulitple mobile numbers</small>  	</td>
			</tr>
			
			<tr  id="binrow">
				<td class="fnt">
					Select Bin 
					</td>
					<td>
			 				: <select name="bin" id="bin" onchange="return Gettingproductlist(this);" >
				 				<option value="-1" selected="selected">--Select Card--</option>
				 				<option  value="ALL" >ALL</option>
				 				<s:iterator  value="smsbean.binlist">
				 					<option value="${BIN}">${BIN_DESC}</option>
				 				</s:iterator>
			 				</select>
					</td>
			</tr>
			<tr id="productrow">
				<td >
					Select Product 
					</td>
					<td>
			 				: <select name="prodid" id="prodid" onchange="return getSubProductList(this);" >
			 				</select>
					</td>
			</tr>
			<tr id="subproductrow" >
				<td>
					Select Sub Product
					</td>
					<td>
			 				: <select name="subprodid" id="subprodid" >
			 				</select>
					</td>
			</tr>
			
			 <tr id="subjectrow" >
					<td> Enter SMS Subject </td>
					<td> : <s:textfield name="subject" id="subject" />  </td>
			</tr>
			
			 <tr id="smscontentrow" >
					<td> Enter SMS Content </td>
					<td>  &nbsp; <s:textarea name="smscontent" id="smscontent" />  </td>
			</tr> 
		</table> 
	</td> 
	<td>
		<table border='0' cellpadding='0' cellspacing='0' width='100%'  valign="top"  >
					<tr>
					<td colspan="2" style="text-align:left;color:red"> <s:checkbox name="exceptid" id="exceptid" onclick="showExceptBlock( this.checked )" /> <b>Except </b></td>
					</tr>
			<tr id="exceprowid" style="display:none">
				<td  >
				<table border='0' cellpadding='0' cellspacing='0' width='100%' >
					<tr><td colspan="2"><small class="dt"> Comma(,) separate for mulitple values</small>  </td></tr>
								<tr id="cardnoexcept">
							<td> Except Card Number </td>
							<td>  &nbsp; <s:textarea name="exceptcardno" id="exceptcardno" maxlength="1024" />	</td>
					</tr>
					
					<tr id="phonerowidexcept">
							<td> Enter Mobile Numbers </td>
							<td>  &nbsp; <s:textarea name="exceptmobilenumber" id="exceptmobilenumber" maxlength="1024" />	</td>
					</tr>
					
					<tr  id="binrowexcept ">
						<td class="fnt">
							Enter Bin 
							</td>
							<td> : <s:textfield name="exceptbin" id="exceptbin" />  </td>
					</tr>
					<tr id="productrowexcept">
						<td >
							Enter Product id 
							</td>
							<td> : <s:textfield name="exceptproduct" id="exceptproduct" />  </td>
					</tr>
					<tr id="subproductrowexcept" >
						<td>
							Enter Sub Product
							</td>
							<td> : <s:textfield name="subprodidexcept" id="subprodidexcept" />  </td>
					</tr> 
				</table> 
				</td>
			</tr> 
		</table> 
	</td> 
</tr> 
</table>

<table border="0" cellpadding="0" cellspacing="0" width="20%"  >
	<tr>  
			<td>	<s:submit value="Submit" name="submit" id="submit" onclick="return validateValues();"/>	</td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td> 
	
	</tr>
</table>
</s:form>

 