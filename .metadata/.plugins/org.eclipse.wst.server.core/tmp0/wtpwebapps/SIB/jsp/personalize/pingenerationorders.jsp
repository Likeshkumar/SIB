<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/ordervalidation.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>
<script>
function GetExcel(type){
	valid=true;
	var orderref = null;
	var orderreflen = document.getElementsByName("personalrefnum");
	var oneselected = false;
	for(var i=0;i<orderreflen.length;i++)
	{ 
		orderref = orderreflen[i];
		if(orderref.checked==true)
		{
			oneselected = true;	
			break;

		}
			
	}
	if(!oneselected)
	{
		errMessage(orderreflen[0],"Select any one Order");
		return false;
	}else{
		//if(type == 'E'){
			document.authrizeorder.action = "getExcelPingenerationAction.do";
			document.authrizeorder.submit;
		/* }
		else if(type == 'P'){
			var check = confirm("Are you sure, do you want to process, did you take excel report??? ");	
			if(check){
				document.authrizeorder.action = "pinGenerationprocessPingenerationAction.do";
				document.authrizeorder.submit;
				document.getElementById("authorize").disabled = true;
			}else{
				document.getElementById("authorize").disabled = false;
				return false;
			}
		}*/
	} 
	//parent.showprocessing();
	return valid;
}

	function printpin(type){
		//alert("tetttt")
		valid=true;
		var orderref = null;
		var orderreflen = document.getElementsByName("personalrefnum");
		var oneselected = false;
		for(var i=0;i<orderreflen.length;i++)
		{ 
			orderref = orderreflen[i];
			if(orderref.checked==true)
			{
				oneselected = true;	
				break;

			}
				
		}
		if(!oneselected)
		{
			errMessage(orderreflen[0],"Select any one Order");
			return false;
		}else{
			var check = confirm("Are you sure, do you want to process, did you take excel report??? ");	
			if(check){
				document.getElementById("authorize").disabled = true;
				document.authrizeorder.action = "pinGenerationprocessPingenerationAction.do";
				document.authrizeorder.submit();
				parent.showprocessing();
			}else{
				document.getElementById("authorize").disabled = false;
				return false;
			}
		}
		return valid;
	}
function callEdit(embname,mobileno,cardno){
	//alert("Hii");
	document.authrizeorder.action = "getCustomerDetailsCustomerDetailsAction.do?embname="+embname+"&mobileno="+mobileno+"&cardno="+cardno;
	document.authrizeorder.submit();
}
</script>
<div align="center">
<s:form action="#"  name="authrizeorder"  autocomplete = "off" onsubmit="parent.showprocessing()" namespace="/">

	<table width="100%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th> Card Number </th>
						<th> Account No </th>
						<th> Name </th>		
						<th> Mobile </th>				
						<th> Product </th>
						<th> Gen Date </th>
						<th> To Collect</th>
						<th> Gen by </th>
						<th> Auth by </th>  
						<!-- <th> Edit </th> -->
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="perspingenorders">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{ORDER_REF_NO}"/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ACCT_NO}  </td>
						<td> ${EMB_NAME}  </td>
						<td> ${MOBILENO}  </td>
						<td> ${ PNAME }  </td>
						<td> ${GENDATE} </td> 
						<td> ${CARD_COLLECT_BRANCH} </td> 
						<td> ${ USERNAME }</td> 
						<td> ${ CHECKER }</td> 
						<s:hidden name='generationtypeforonlycvv' value='NO'/>
						<%-- <td> <s:submit name="edit" value="Edit" onclick="return callEdit('%{EMB_NAME}','%{MOBILENO}','%{CARD_NO}')"/> </td> --%>  
					</tr> 
					<input type="hidden" name="binno" id="binno" value="${BIN}">
					<input type="hidden" name="branch_code" id="branch_code" value="${BRANCH_CODE}">
					<input type="hidden" name="cardtype_sel" id="cardtype_sel" value="${PRODUCT_CODE}">
			 		<%-- <s:hidden name='generationtypeforonlycvv' value='NO'/> --%>
			 	
			 	</s:iterator>
	 	</table>
	 	<br>
	 	<div align="center">
				<s:submit name="report" id="report" value=" GetExcel " onclick="return GetExcel('E');"/>
				
				<s:submit name="authorize" id="authorize" value=" Process " onclick="return printpin('P');"/>
			 
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
		</div>
	 	<s:hidden name="fromdate"/>
		<s:hidden name="todate"/>
		<s:hidden name="type" value="PIN"/>  
	<%-- <s:hidden name="cardtype_sel" id="cardtype_sel" value="%{cardtype_selected}"></s:hidden> --%>	
</s:form>
</div>
 