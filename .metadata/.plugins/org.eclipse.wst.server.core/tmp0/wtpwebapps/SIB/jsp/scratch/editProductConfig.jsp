<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 
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

function selectProcuct(FLAG)
{
	var xmlhttp;
	var productCode = document.getElementById("productName").value;
	
	
		var url="fetchProductProductConfiguration.do?productCode="+productCode+"&act="+FLAG;
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
						document.getElementById("productDescription").innerHTML=xmlhttp.responseText; 
						//document.getElementById("loaderImage").style.display="none";
					}
				}
				xmlhttp.open("POST", url, true);
				xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				xmlhttp.send();
			} catch (e) {
				alert(e);
			}
			
	//document.getElementById("productDescription").style.display="";
	
	
}


function updateProductConfig(flag,productName)
{
	var productCode = document.getElementById("productName").value;
	var productDesc = document.getElementById("productDesc").value;
	document.fetchProductConfigForm.action="updateProductProductConfiguration.do?act="+flag+"&productCode="+productCode+"&productDesc="+productDesc;
	
	document.fetchProductConfigForm.submit();
	
	
	
	
}



</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="fetchProductProductConfiguration.do" autocomplete="off" name ="fetchProductConfigForm" method="post"> 
		<div align="center">
			<table align="center" border="0"  cellspacing="0" cellpadding="0" width="50%" class="formtable">
				  	<tr>
				  		<td  align="right">Select Product Name</td>
					  		<td width="50%" align="left">
								<select id="productName" name="productName" onchange = "selectProcuct('${scratchcardbeans.flag}')">
									<option value="">-Select Product Name-</option>
									<s:iterator  value="scratchcardbeans.productList">
										<option value="${SCHPROD_CODE}">${SCHPROD_NAME}</option>
									</s:iterator>
								</select>
							</td>
					</tr>
					<tr id = "productDescription" >
					
					</tr>
					
				</table>
				
			<table><tr><td>
				<input type="button" onclick="updateProductConfig('${scratchcardbeans.flag}','${SCHPROD_NAME}')" value="update"/></td></tr>
			</table>
		</div>
	</form>
	
	