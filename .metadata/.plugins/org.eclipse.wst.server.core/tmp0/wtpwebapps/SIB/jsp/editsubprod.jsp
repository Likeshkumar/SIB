<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript" >

function form_chck()
{
var subprod=document.editsubproductform.new_SubProductName.value;
//alert (subprod);
var r=confirm("Do You Want To Update");
if(r)
	{
		valid = true;
		
		if ( subprod == "" )
				 {
			 alert ( "'SUBPRODUCT CANNOT BE EMPTY'" );
			 return false;
				}
		if(document.editsubproductform.AMOUNT.value=="")
			{
			
			alert ( "'AMOUNT CANNOT BE EMPTY'" );
			return false;
			}
		if(document.editsubproductform.scheme_scode.value=="-1")
		{
		
		alert ( "'Please Select The Scheme'" );
		return false;
		}
			
		
	}
else 
	return r;
}
</script>

<br>

<div align="center">
<s:form action="saveEditSubprodAddSubProdAction" name="editsubproductform" autocomplete="off">
<table style="border:1px solid #efefef;"  border="0" cellpadding="0" align="center" width="90%" cellspacing="0"  >
	<s:iterator var="subproditr" value="subprod_detail">
		<s:set name="instproddetailsfeecode">${FEE_CODE}</s:set>
		<s:set name="instproddetailslimitid">${LIMIT_ID}</s:set>
		<tr><td><input type="hidden" name="cardtype" value="${CARDTYPE_ID}"></td></tr>
		<tr>
			<td>SUB PRODUCT NAME</td>
			<td>: <s:textfield name="new_SubProductName" id="new_SubProductName" value="%{SUB_PRODUCT_NAME}" maxlength="20" ></s:textfield></td>
			<td>
				FEE DESC
			</td>
			<td>:
				<select name="scheme_scode" id="scheme_scode">
					<option value="-1">--Select Fee--</option>
					<s:iterator value="scheme_list" var="var_scheme_list">
					<s:set name="feedescfeecode">${FEE_CODE}</s:set>
						<s:set name=""></s:set>
						<option value='${FEE_CODE}'
						<s:if test="%{#instproddetailsfeecode==#feedescfeecode}">
					 				 selected="${FEE_CODE}"
					 	</s:if>
						>${FEE_DESC}</option>
					</s:iterator>
				</select>	
			</td>
		</tr>
		<tr>
			<td>
				CARD CURRENCY
			</td>
			<td>:
				<s:textfield name="CARD_CCY" maxlength="6"></s:textfield>
			</td>
			<td>
				Card Generation Type
			</td>
			<td>:
				<s:radio list="#{'1':'Personalized','0':'Instant','2':'Both'}" id="personalized" name="personalized" value="%{PERSONALIZED}"/>
			</td>
		</tr>
		<tr>
			<td>
				RELOADABLE CARD
			</td>

			<td>:
				<s:radio list="#{'Y':'Yes','N':'No'}" id="reload" name="reload" value="%{RELOADABLE}"/>
			</td>
			<td>
				ATM TRANSACTION ALLOWED
			</td>
			<td>:
				<s:radio list="#{'Y':'Yes','N':'No'}" id="atmallow" name="atmallow" value="%{ATM_TRANS}"/>
			</td>
		</tr>
		
		<tr>
			<td>MINIMUM LOAD AMOUNT</td><td>: <s:textfield id="AMOUNT" name="AMOUNT" maxlength="8" value="%{MIN_LOAD_AMOUNT}"/></td>
			<td>MAXIMUM LOAD AMOUNT</td><td>: <s:textfield id="AMOUNT" name="AMOUNT" maxlength="8" value="%{MAX_LOAD_AMOUNT}"/></td>
		</tr>
		<tr>
			<td>LIMIT DESC</td>
			<td>:
				<select name="limit_code" id="limit_code">
					<option value="-1">--Select Fee--</option>
					<s:iterator value="limit_list" var="limit_list">
					<s:set name="limitdesclimitid">${LIMIT_ID}</s:set>
						<s:set name=""></s:set>
						<option value='${LIMIT_ID}'
						<s:if test="%{#instproddetailslimitid==#limitdesclimitid}">
					 				 selected="${LIMIT_ID}"
					 	</s:if>
						>${LIMIT_DESC}</option>
					</s:iterator>
				</select>	
			</td>
		</tr>
		<tr><td><s:hidden name="binno" id="binno" value="%{BIN}"/></td></tr>
	</s:iterator> 
</table>
<table border="0" cellspacing="0" cellpadding="0" width="30%">
	<tr align="center">
		<td><s:submit value=" UPDTAE " onclick="return form_chck();" /> </td>
		<td><s:reset name=" Reset " value="Reset" /> </td>
	</tr>
</table>
</s:form>
</div>