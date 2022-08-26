<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>
<%String act = request.getParameter("act")==null?"":request.getParameter("act"); %>

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
		if(type == 'E'){
			document.authrizeorder.action = "getExcelPingenerationAction.do";
			document.authrizeorder.submit;
		}else{
			var check = confirm("Are you sure, do you want to process, did you take excel report??? ");	
			if(check){
				document.authrizeorder.action = "preGenerationprocessPreprocessAction.do";
				document.authrizeorder.submit;
				document.getElementById("authorize").disabled = true;
			}else{
				document.getElementById("authorize").disabled = false;
				return false;
			}
		}
	}
	//parent.showprocessing();
	return valid;
}

</script>

<div align="center">
<s:form action="preGenerationprocessPreprocessAction"  name="authrizeorder"  autocomplete = "off"  namespace="/">
<input type="hidden" name="act" value="<%=act %>"/>
	<table width="90%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
	
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th> Card Number </th>
						<th> Account No </th>
						<th> Name </th>		
						<th> Mobile </th>				
						<th> Product Name </th>
						<th> Gen Date </th>
						<th> Collect Branch</th>
						<th> Gen by </th> 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="perspregenauthlist" status="incr" >
				<input type="hidden" name="Embosingname" value="${BRANCH_CODE}"/>
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum%{#incr.index}" fieldValue="%{CARD_NO}"/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ACCT_NO}  </td>
						<td> ${EMB_NAME}  </td>
						<td> ${MOBILENO}  </td>
						<td> ${ PNAME }  </td>
						<td> ${GENDATE} </td> 
						<td> ${CARD_COLLECT_BRANCH} </td> 
						<td> ${ USERNAME }</td> 
					</tr> 
					
			
					<input type="hidden" name="branchcode" id="branchcode" value="${BRANCH_CODE}">
					<input type="hidden" name="binno" id="binno" value="${PRODUCT_CODE}"> 
			 	</s:iterator>
	 	</table> 
	 	<br>
	 	<div align="center">
				<s:submit name="report" id="report" value=" GetExcel " onclick="return GetExcel('E');"/>
							
				<s:submit name="authorize" id="authorize" value=" Process " onclick="return GetExcel('P');"/>
			 
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
		</div>
	<s:hidden name="fromdate"/>
	<s:hidden name="todate"/> 
	<s:hidden name="type" value="PRE"/> 
</s:form>
</div>
 