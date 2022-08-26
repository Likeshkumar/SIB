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
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="saveintosessionPersonalCardOrderAction"  name="personalorderform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount">
	<% String usertype=(String)session.getAttribute("USERTYPE");
		if(usertype.equals("INSTADMIN"))
		{
	%>
		<tr>
				<td class="fnt"> Select Branch</td>
				<td> <s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branch_list"  headerValue="Select Branch" headerKey="-1" tooltip="Select Branch"/>
				
				</td>
			</tr>
			
	<%	
		}
		else
		{
	%>
			<tr>
			<td class="fnt"> Branch</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
	<%		
		}
	%>
	<tr>
		<td class="fnt">
		Select Card
		</td>
		<td>
 				<select name="cardtype" id="cardtype" onchange="return Gettingsubproduct();" >
	 				<option value="-1">--Select Card--</option>
	 				<s:iterator  value="personalproductlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
   <tr> 
    	<td id="subname" class="fnt">
    		Select Product
    	</td>
    	<td class="fnt">
    		<div id="ajax">
    			<select name="subproductlist" id="subproductlist">
    				<option value="-1">--Select Product--</option>
    			</select>
    		</div>
    	</td>
    </tr>

    	
    <tr>	
		<td>
		No. Of Cards
		</td>
		<td>
   	   		<input type="text" name="Count" id="Count"  maxlength="5" onKeyPress="return numerals(event);">
		</td>
	</tr>
	<tr>
		<td>Embossing Name </td>
		<td>
			<input type="text" name="emposs" id="emposs"  maxlength="25"  onKeyPress="return alphabets(event);" onkeyup="return coppyEmbosstoEncode(event,this.value);" >
		</td>
	</tr>
	<tr>
		<td>Encoding Name</td>
		<td>
			<input type="text" name="encode" id="encode"  maxlength="25" onKeyPress="return alphabets(event);">
		</td>
	</tr>

 <!-- 		<tr>
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
	
	 -->
	
</table>
	<input type="hidden" id="cust_reg_req" name="cust_reg_req" value="Y">
	<!-- This " custno " hidden field only for validation,Same name use in KYC order also,this value should be 1  -->
	<input type="hidden"  name="custno" id="custno"  maxlength="25" value="1">
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

