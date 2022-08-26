<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
<script type="text/javascript">
function getPrice(){
	var Existdiv = document.getElementById("typeExistdiv");
	var type = document.getElementById("type");
	if(type){
		if(type.value == "N"){
			Existdiv.style.display = 'table-row';
		}else{
			Existdiv.style.display = 'none';
		}
	}
}
function getType(){
	var productid = document.getElementById("productid");
	var url="countUnitPriceExistAlphaGasPriceAction.do?productid="+productid.value;
	var response=AjaxReturnValue(url);
	alert("response "+response);
	if(response == 1){
			var selectbox = "<select name='type' id='type' onchange='return getPrice()'><option value='-1'>--Select Type--</option><option value='E'> Exist </option></select>";
	}else{
		var selectbox = "<select name='type' id='type' onchange='return getPrice()'><option value='-1'>--Select Type--</option><option value='N'>Add New</option></select>";
	}
	document.getElementById('ajax').innerHTML = selectbox;
	return false;
}

function getpromtvalue(productid){
	var unitprice = prompt("Enter New Unit Price ", "");
	var sample = isNumber(unitprice);
	if(sample == false){
		errMessage(unitprice, ' Enter Numeric digits for unit price ');
		return false;
	}
	if((unitprice.length) > 20){
		errMessage(unitprice, ' Enter value less than 20 digits ');
		return false;
	}
	 var url = "insertpriceAlphaGasPriceAction.do?unitprice="+unitprice+"&productid="+productid+"&type="+'U';
	 window.location = url; 
}

function isNumber(n) {
	return !isNaN(parseFloat(n)) && isFinite(n);
}

function validation(){
	var product = document.getElementById("productid");
	var type = document.getElementById("type");
	var unitprice = document.getElementById("unitprice");
	if(productid){
		if(productid.value == "-1"){
			errMessage(product," Select Product ");
			return false;
		}
	}
	if(type){
		if(type.value == "-1"){
			errMessage(type," Select type ");
			return false;
		}
		if(type.value == "N"){
			if( unitprice ){
				if(unitprice.value == ""){
					errMessage(unitprice," Enter Unit price ");
					return false;
				}
			}
		}
	}
	return true;
}
</script>
<style>
.calgn{
	text-align:center !important;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="insertpriceAlphaGasPriceAction.do" autocomplete="off" name="price">
		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
			<tr>
					<td class="fnt">Select Product</td>
					<td> :</td>
					<td>
		 				<select name="productid" id="productid" onchange="return getType()">
			 				<option value="-1">--Select Product--</option>
			 				<s:iterator  value="productlist">
			 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
			 				</s:iterator>
		 				</select>
					</td>
   			</tr>
			<tr>
					<td class="fnt">Select Type</td>
					<td> :</td>
					<td><div id="ajax">
				 				<select name="type" id="type" onchange="return getPrice()">
					 				<option value="-1">--Select Type--</option>									
				 				</select>
		 				</div>
					</td>
   			</tr>
   			
			<tr style="display:none;" id="typeExistdiv">
					<td align="right">Enter Price</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="unitprice" id="unitprice" value="" onKeyPress="return numerals(event);" maxlength="20"/>						
					</td>
			</tr>					
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="Submit" onclick="return validation()"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>		
	<s:if test="%{insertflag=='Y'}">
		<tr>
			<td>
				<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable">
				<tr>
					<th class="calgn"> Sl no </th>
					<th class="calgn"> UNIT PRICE </th>
					<th class="calgn"> PRODUCT DESC</th>
					<th class="calgn"> CONFIGURED BY </th>
					<th class="calgn"> CONFIGURED DATE TIME</th>
					<th class="calgn"> STATUS  </th>
					<th class="calgn"> Add/Edit</th>
				</tr>
			 
			<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="authList">
				<tr
				<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
				>
						<td class="calgn"> <%= rowcount %> </td>
						<td class="calgn"> ${UNITPRICE}  </td>
						<td class="calgn"> ${PRODUCTDESC}  </td>
						<td class="calgn"> ${CONFIGBY} </td>
						<td class="calgn"> ${ CONFIG_DATE }  </td>
						<td class="calgn"> ${ACTIVESTATUS}  </td>
						<td class="calgn">
							<a onclick="return getpromtvalue(${PRODUCTID});"><img src="images/addnew.png" alt="Edit"/></a>
						</td>
					</tr>
				</s:iterator> 
				</table>
			</td>
		</tr>
	</s:if>	
	<s:elseif test="%{insertflag=='N'}"><tr><td align="center"><font color="red">No records found</font></td></tr></s:elseif>
	<s:if test="%{statusflag == 'Y'}">
		<tr><td style="padding-top: 25px;text-align:center;"><b> HISTORY </b></td></tr>
			<tr>
				<td>
					<table border='0' cellpadding='0' cellspacing='0' width='80%' class="formtable">
					<tr> 
					<tr>
						<th  class="calgn"> Sl no </th>
						<th  class="calgn"> UNIT PRICE </th>
						<th  class="calgn"> PRODUCT DESC</th>
						<th  class="calgn"> CONFIGURED BY </th>
						<th  class="calgn"> CONFIGURED DATE TIME</th>
					<!--<th> STATUS </th>
						<th> DELETE STATUS</th> -->
					</tr>
				 
				<% int rowcnt = 0; Boolean alt=true; %> 
					<s:iterator value="historyList">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
							<td class="calgn"> <%= rowcount %> </td>
							<td class="calgn"> ${UNITPRICE}  </td>
							<td class="calgn"> ${PRODUCTDESC}  </td>
							<td class="calgn"> ${CONFIGBY} </td>
							<td class="calgn"> ${ CONFIG_DATE }  </td>
					<%--		<td> ${STATUS}  </td>
							<td> ${DELETE_STATUS}  </td> --%>
					</tr>
					</s:iterator> 
					</table>
				</td>
		</tr>
	</s:if>	
	<s:elseif test="%{statusflag=='N'}">
		<tr><td style="padding-top: 25px"><b> HISTORY </b></td></tr>	
		<tr><td align="center"><font color="red">No records found</font></td></tr>
	</s:elseif>
</form>