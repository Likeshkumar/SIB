<%@taglib uri="/struts-tags" prefix="s" %>
<style>
div table td{
	padding-top:5px;
}
</style>
<script>
function nameOnly(evt){

	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
	 
	//alert(keyvalue);
	if((!(keyvalue<97 || keyvalue>122)) || (!(keyvalue<65 || keyvalue>90)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13) || (keyvalue==32))
	{
		return true;
	}
	else 
	{
		return false;
	} 
}

function numberOnly(evt){
var keyvalue=evt.charCode? evt.charCode : evt.keyCode
//alert(keyvalue);
	if((!(keyvalue<48 || keyvalue>57)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13))
	{
		return true;
	}
	else
	{ 
		return false;
	} 
}	
function validate_form ( )
{
	var product=document.ProductAddFofm.product.value;
	
	
    valid = true;
	
    if ( document.ProductAddFofm.deployment.value == "-1" )
    {	
    	 alert ( "'PLease Select Product Name' " );
    	 return false
   		 
	}
    else if ( product == "" )
   		 {
    	 alert ( "'Please Enter Sub Product Name'" );
    	 product.focus;
    	 return false;
   		}
   
    
   
}
</script>
<body>
<s:form name="ProductAddFofm" action="SaveSubProduct" autocomplete="off">


<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" align= "center" class="table">
 
 		
		<s:if test="%{#display !=''}">
		<tr><td></td><td align="center"><font color="red"><s:property value="display"></s:property></font></td></tr>
		</s:if>
		<tr><td>&nbsp;</td></tr>
		<tr>
 			<td>SELECT PRODUCT TYPE
 			</td>
    		<td>	
    	 		<h4>
				<s:select label="Select a Product" 
				headerKey="-1" headerValue="--- Select ---"
				list="productlist" 
				listValue="%{productid}"
    			listKey="%{productname}" 
   				name="deployment" id="deployment" />
				
				</h4>
			 </td>
		</tr>
		
 		<tr>
			<td>SUBPRODUCT NAME</td>
			<td><s:textfield name="product" id="product"  onkeypress='return nameOnly(event)' maxlength="25" ></s:textfield></td>
		</tr>
 		
 		<tr>
 		<td></td><td><s:submit value="submit" name="submit" onclick="return validate_form ( )" /></td></tr>
 		
		
</table>
</div>
 
</s:form>

<s:bean name="com.ifp.Action.AddSubProductAction" var="resd" >
<s:param name="display"></s:param>
</s:bean>
</body>
