<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 <link rel="stylesheet" type="text/css" href="style/calendar.css"/>
 <script type="text/javascript" src="js/calendar.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script>
function fetchProductConfig(FLAG)
{
	alert(FLAG);
	//var productName = document.getElementById("productName").value;
	//var productDesc = document.getElementById("productDesc").value;	
	var productName = document.getElementById("productName").value;
	
	document.fetchProductConfigForm.action="fetchProductProductConfiguration.do?productName="+productName+"&act="+FLAG;
	document.fetchProductConfigForm.submit();
}

function selectScratchCard(FLAG)
{
	alert(FLAG);
	var xmlhttp;
	var productName = document.getElementById("productName").value;
	alert(productName);
		var url="fetchScratchCardScratchCardProduction.do?act="+FLAG+"&productName="+productName;
			try {
				if (window.XMLHttpRequest)
				{// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
				}
				else
				{// code for IE6, IE5
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange=function()
				{
					if (xmlhttp.readyState==4 && xmlhttp.status==200)
					{
						alert(xmlhttp.responseText);
						document.getElementById("scratch_denomValue").innerHTML=xmlhttp.responseText; 
						//document.getElementById("scratchCardExpiryDate").style.display="";
					}
				}
				xmlhttp.open("POST", url, true);
				xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				xmlhttp.send();
			} catch (e) {
				alert(e);
			}
	
	
}


function addScratchCard(flag)
{
	var productName = document.getElementById("productName").value;
	var denomValue = document.getElementById("scratch_denom_value").value;
	alert(denomValue);
	var expiredate = document.getElementById("todate").value;
	document.addScratchCardForm.action="saveScratchCardScratchCardProduction.do?act="+flag+"&expiredate="+expiredate+"&productName="+productName+"&denomValue="+denomValue;
	document.addScratchCardForm.submit();
	
}



</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form  name ="addScratchCardForm" method="post" autocomplete="off"> 
		<div align="center">
			<table align="center" border="0"  cellspacing="0" cellpadding="0" width="50%" class="formtable">
				  	<tr>
				  		<td  align="right">Select Product Name</td>
					  		<td width="50%" align="left">
								<select id="productName" name="productName" onchange = "selectScratchCard('${scratchcardbeans.flag}')">
									<option value="">-Select Product Name-</option>
									<s:iterator  value="scratchcardbeans.productList">
										<option value="${SCHPROD_CODE}">${SCHPROD_NAME}</option>
									</s:iterator>
								</select>
							</td>
					</tr>
					
					</table>
					<div id = "scratch_denomValue">
					
					
					
					</div>
					
				
			<table><tr><td>
				<input type="button" onclick="addScratchCard('${scratchcardbeans.flag}')" value="Add"/></td></tr>
			</table>
		</div>
	</form>
	
	