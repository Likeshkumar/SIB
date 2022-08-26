<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script>
function addProductConfig(FLAG)
{
	
	//alert(FLAG);
	var productName = document.getElementById("productName").value;
	var productDesc = document.getElementById("productDesc").value;	
	
	document.addProductConfigForm.action="saveProductProductConfiguration.do?productName="+productName+"&productDesc="+productDesc+"&act="+FLAG;
	//document.AcqChbAnalyzeFrm1.target="AcqChargeBack_Analyze_Dtl";		
	document.addProductConfigForm.submit();
}
</script>
<s:form name="addProductConfigForm" method="post" autocomplete="off">
<div align="center">
<jsp:include page="/displayresult.jsp"></jsp:include><br>

           
<table border='0' cellpadding='0' cellspacing='0' width='40%' >
<s:hidden name="act" id="act" value="%{scratchcardbeans.flag}"></s:hidden>	
			<tr>
	   		    <td>Product Name :<span><font color="red">*</font></span> </td>
				<td><input type="text" name="productName" id="productName" maxlength="16"/></td>
			</tr>
			<tr>
				<td>Product Description : <span><font color="red">*</font></span></td>
				<td><textarea name="productDesc" id="productDesc" maxlength="80" style="resize: none;max-width: 200px; max-height: 70px;"></textarea></td>
			</tr>
<tr>
<td></td>
<td>
<input type="button" onclick="addProductConfig('${scratchcardbeans.flag}')" value="save"/>
</td></tr>
</table>

</div>
</s:form>
