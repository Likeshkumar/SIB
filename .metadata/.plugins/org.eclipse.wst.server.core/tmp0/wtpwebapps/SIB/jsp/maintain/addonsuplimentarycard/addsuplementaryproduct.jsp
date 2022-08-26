<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>
<style type="text/css">
	*{
		font-weight:normal;
	}
	table.formtable td{
		text-align:left;
		padding:10px 0;
	}
	table{
		border-collapse: collapse;
		
	}
	table.subdesc  {
		border:1px solid gray; 
	}
	table.subdesc td {
		border-left:1px solid gray; 
		border-bottom:1px solid gray; 
		padding:10px 0;
	}
	table.subprodname td{
		border:1px solid gray;
	}
</style>
<script type="text/javascript" >


function getsuppdetails(){ 
	//alert("etest");
	var suppname = document.getElementById("suppname").value;
	//alert(suppname.length);
	//alert(suppname.trim().length);
	
	if(suppname.trim().length<=0){
		alert("Enter the Embossing Name...");
		return false;
	}
	
	var cardnumber = document.getElementById("cardnumber").value;
	
	var productcode = document.getElementById("productcode").value;
	var subproductcode = document.getElementById("subproductcode").value;	
	var accountno = document.getElementById("accountno").value;
	var maskedcardno = document.getElementById("maskedcardno").value;
	//addondetails = cardnumber="+cardnumber+"&username="+username;
	//addondetails = "username="+username+"&cardnumber="+cardnumber+"&productcode="+productcode+"&accountno="+accountno+"&maskedcardno="+maskedcardno;
	
	suppname=suppname.trim();
	//alert(suppname.length);
	//alert(addonname.length);
	var url="generateAddoncardSuplimentActionAddonCardSuplAction.do?suppname="+suppname+"&cardnumber="+cardnumber+"&productcode="+productcode+"&accountno="+accountno+"&mcardnumber="+mcardnumber+"&subproductcode="+subproductcode;
	//alert( url );
	var result = AjaxReturnValue(url);
	//alert(result);		
}

function getSubProd(selprodid ){   
	var url = "getSubProductListByProductAddonCardSuplAction.do?productcode="+selprodid+"&status=1";   
	//alert(url);
	result = AjaxReturnValue(url);   
	//alert(result);
	document.getElementById("subproduct").innerHTML = result;  
	
	//var hidsubproduct = document.getElementById("hidsubproduct").value; 
	//document.getElementById("subproduct").value=hidsubproduct;
	//getLimitBySubProduct(hidsubproduct);
	//getFeeBySubProduct(hidsubproduct);
	   
} 

function validation()
{
	var productcode = document.getElementById("productcode");
	if( productcode ){ if( productcode.value == ""  || productcode.value == "-1") { errMessage(productcode, "Select Product  !");return false; } }
	var subproduct = document.getElementById("subproduct");
	if( subproduct ){ if( subproduct.value == ""  || subproduct.value == "-1") { errMessage(subproduct, "Select Sub-Product  !");return false; } }
	var collectbranch = document.getElementById("collectbranch");
	if( collectbranch ){ if( collectbranch.value == "") { errMessage(collectbranch, "Select Collection Branch !");return false; } }
	
}


</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<s:form  action="addSuplCardDetailsAddonCardSuplAction.do" name="SubProdAddFofm" namespace="/" autocomplete="off"  onsubmit="return validation()">
	 <s:hidden name="cardnumber" id="cardnumber" value="%{cardnumber}"/>
	 <s:hidden name="cardnumberlength" id="cardnumberlength" value="16"/>
				
	 <table border="0" cellpadding="0" cellspacing="0" class='formtable' width="50%" > 
 		<tr>
			<td align="left" >INSTID</td>
			<td> : 	<s:textfield readonly="true" name="instid" id="instid" value="%{#session.Instname}" />
			</td>
 		</tr>
 		<tr>
			<td align="left">PRODUCT<span class="mand">*</span></td>
			<td> : 
				 <select name="productcode" id="productcode" onchange="getSubProd(this.value)" >
	 				<option value="-1">--Select Product--</option>
	 				<s:iterator  value="productlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>     
			</td>
 		</tr>   
 		<tr>
 		<td class="txt"> Sub Product   <span class="mand">*</span>  </td>
			    <td>:
			     	<select  id="subproduct"  name="subproduct" >
			    	 <option value="-1" >Select Sub Product</option>
			    	</select>
			      
		</td>	     
		</tr>
		
		<tr>
					<td>Collection Branch <span class="mand">*</span></td>
					<td>: <select name="collectbranch" id="collectbranch">
								<option value="">-SELECT-</option>
								<s:iterator value="cardcollectbranchlist">
									<option value="${BRANCH_CODE}">${BRANCH_NAME}</option>
								</s:iterator>
						</select>
					</td>
		</tr>
		     
 	</table >
 
  


<table border="0" cellpadding="0" cellspacing="4" width="20%" class="formtable" >
		<tr>
		<td>
			<s:submit value="Submit" name="submit" id="submit"  />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
	
</s:form>

<br><br>
<s:form action="generateAddoncardSuplimentActionAddonCardSuplAction.do"  name="orderform" autocomplete = "off"  namespace="/" onsubmit="return getsuppdetails();">
	<div id='fw_container'>	
	
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='pretty' style='border: 1px solid #454454' >
	<s:if test ="%{!carddetlist.isEmpty()}">
	<tr> 
	<th>Card No</th>      
	<th>ACCOUNT NO</th>
	<th>Mobile No.</th>  
	<th>CUST NAME</th> 
	<th>DOB </th> 
	<th>CUSTOMER ID </th> 
	<th>BIN</th>  
	<th>CARD STATUS</th> 
	<th>PRODUCT</th>  
	<th>SUB PRODUCT</th>  
	<!-- <th>ORIGINAL CARDNO</th> -->  </tr>
	</s:if>
	<s:iterator value="carddetlist">	
	
	<s:if test='padssenabled=="Y"' >
	<input type="hidden" name="hcard" value="${HCARD_NO}"/>
	</s:if>
	<s:else>
	<input type="hidden" name="card" value="${CARD_NO}"/>
	
	</s:else>
	<input type="hidden" name="mcardnumber" value="${MCARD_NO}"/>
	<input type="hidden" name="productcode" value="${PRODUCT_CODE}"/>
	<input type="hidden" name="subproductcode" value="${SUBPRODUCT_CODE}"/>
	 
	
	<input type="hidden" name="accountno" value="${ACCOUNT_NO}"/>  
							<tr> 
								<td style="text-align:center">${MCARD_NO}</td>
								<td style="text-align:center">${ACCOUNT_NO}</td>
								<td style="text-align:center">${MOBILE}</td>
								<!--  <td style="text-align:center">${NAME}</td>-->
								<td style="text-align:center"><input type="text" id="suppname" name="suppname" value="${NAME}"/></td>
								<td style="text-align:center">${DOB}</td>
								<td style="text-align:center">${CIN}</td>
								<td style="text-align:center">${BIN}</td>
								<td style="text-align:center">${STATUS_DESC}</td>
								<td style="text-align:center">${PRODUCT_DESC}</td>
								<td style="text-align:center">${SUBPRODUCT_DESC}</td>
								<%-- <td style="text-align:center">${ORG_CARDNO}</td> --%>
								
							</tr>
						</s:iterator> 
						<s:hidden name="collectbranch"/>	
	</table>
	
	<table width='20%'>
	<s:if test ="%{!carddetlist.isEmpty()}">
		<tr align="center">
			<td colspan='2'><input type="submit" name="generatecard" id="generatecard" value="Generate Suplimentary Card" /></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
		</s:if>
	</table>
	
	
</div>


</s:form>
 
