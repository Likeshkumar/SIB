<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<head>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<script>
function GetPdf(){
	//alert("1");
	var orderref = null; 
	valid=true;
	var oneselected = false; 
	var orderreflen = document.getElementsByName("instorderrefnum");
	for(var i=1;i<=orderreflen.length;i++)
	{ 
		var orderid = "orderrefnum"+i;
		orderref = document.getElementById(orderid); 
		if(orderref.checked==true)
		{
			oneselected = true;	 
			break; 
		} 
	} 
	if(!oneselected)
	{
		errMessage(orderref,"Select any one Order");
		valid= false;
		return false;
	}else{
		document.orderform.action = "getfeereportFeeReport.do";
		document.orderform.submit;
	}
	return valid;
}


function feevalidation(){
	//alert("1");
	var orderref = null; 
	valid=true;
	var oneselected = false; 
	var orderreflen = document.getElementsByName("instorderrefnum");
	for(var i=1;i<=orderreflen.length;i++)
	{ 
		var orderid = "orderrefnum"+i;
		orderref = document.getElementById(orderid); 
		if(orderref.checked==true)
		{
			oneselected = true;	 
			break; 
		} 
	} 
	if(!oneselected)
	{
		errMessage(orderref,"Select any one Order");
		valid= false;
		return false;
	}else{
		document.orderform.action = "deleteFeeFeeReport.do";
		document.orderform.submit;
	}
	return valid;
}




function selectallvalidation(){
	//alert("welcome");
	valid=true;
	var orderref = null; 
	var orderreflen = document.getElementsByName("instorderrefnum");
	var oneselected = false; 
		for(var i=1;i<=orderreflen.length;i++)
		{ 
			var orderid = "orderrefnum"+i;
		 
			orderref = document.getElementById(orderid); 
			if(orderref.checked==true)
			{
				oneselected = true;	 
				break; 
			} 
		} 
		if(!oneselected)
			{
			errMessage(orderref,"Select any one Order");
			valid= false;
			return false;
			}else{
				document.orderform.action = "generateReportFeeReport.do";
				document.orderform.submit;
			}
	//parent.showprocessing();
	return valid;
 }
</script>
</head>  
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="#" id="orderform" name="orderform" autocomplete = "off"  namespace="/">
<div align="center">   
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				 
					
					<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Card No </th>
						<th> Account No</th>
						<th> Branch </th>
						<th> Fee Amount </th>
						<th> Currency </th>
						<th> Activity </th>
						<th> Generated Date </th>
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="feelist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<%--<td> <s:checkbox name="instorderrefnum"  id="orderrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"/>  </td> --%>
						<td> <input type="checkbox" name="instorderrefnum"  id="orderrefnum<%=rowcount%>"  value="${CARDNO}"/>  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ACCOUNT_NO}  </td>
						<td> ${BRANCH_CODE}  </td>
						<td> ${FEE_AMOUNT} </td>
						<td> ${CURRENCY}   </td>
						<td> ${CARDACTIVITY}   </td>
						<td> ${ FEE_GEN_DATE }  </td>
						
					</tr> 
			 	</s:iterator>
			 	
			 	<s:hidden name="fromdate"/>	 
			 	<s:hidden name="todate"/>
			 	<s:hidden name="actioncode"/>
			 	<s:hidden name="batchno"/>
	 	</table> 
	 	
	 	<table border=0 cellpadding=0 cellspacing=0 width=25%>
	 	<tr>
			<td colspan="8">
				
				<input type ="submit" value="PDF" onclick="return GetPdf();" /> 
				
				<s:submit value="Submit" name="submit" id="submit" onclick="return selectallvalidation();" /> 
				
				<s:submit value="Delete Fee" onclick="return feevalidation();" />
				
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							  
			</td>
		</tr>
	 	</table>
	 	
	 	<s:hidden name="cardtype_sel" id="cardtype_sel" value="%{cardtype_selected}"></s:hidden>

 </div>
</s:form>

 