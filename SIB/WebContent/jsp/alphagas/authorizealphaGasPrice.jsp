<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
<script type="text/javascript">
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
	return true;
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="authListalphaGasPriceAlphaGasPriceAction.do" autocomplete="off" name="price">
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
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="Submit" onclick="return validation()"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>		
</form>