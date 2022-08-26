<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<!--<script type="text/javascript" src="jsp/personalize/script/ordervalidation.js"></script>-->
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script> 
 <jsp:include page="/displayresult.jsp"></jsp:include>

<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
 <script>
 	function validateFilter (){
 		parent.showprocessing();
 	}
 </script>


<script>
function GetExcel(type){
	//alert(type);
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
			document.authrizeorder.submit();
		}else{
			var check = confirm("Are you sure, do you want to process, did you take excel report??? ");	
			if(check){
				document.authrizeorder.action = "confirmCardissueordersPersonalCardReceiveIssueAction.do";
				document.authrizeorder.submit();
				document.getElementById("authorize").disabled = true;
				parent.showprocessing();
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

 
<s:form action="confirmCardissueordersPersonalCardReceiveIssueAction"  name="authrizeorder"  autocomplete = "off"  namespace="/" onsubmit="parent.showprocessing()">
<%String act = request.getParameter("act")==null?"":request.getParameter("act"); %>
<input type="hidden" value="<%=act%>" name="act"/>

<!-- cardIssueorderauthorize -->
	<table width="100%" cellpadding="0"  border="0"  cellspacing="0">
	
	
			<s:if test="iscouriertrackenabled">
			 

					<tr>
						<td> Courier  : <s:select name="couriercompanyid" id="couriercompanyid" listKey="COURIERMASTER_ID" listValue="COURIER_NAME" list="courierlist"  headerValue="Select Courier" headerKey="-1"  /> </td>
					
						<td> Courier Id : <s:textfield  name="courierid" id="courierid"  /> </td>
					</tr>
					<tr valign="top">
						<td > To Agent/Address  : <br/> <s:textarea name="sendingaddress" id="sendingaddress" style="height:50px"/> </td>
					
						<td > Courier Date  :  
							<input type="text" name="courierdate" id="courierdate"  readonly="readonly" value="" onchange="return yearvalidation(this.id);" style="width:160px">
							<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.authrizeorder.courierdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
						</td>
					</tr>
					 
					 
			</s:if>
			</table>

	<div id="fw_container">	

	<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="formtable">
				 
			<input type="hidden" name="bin" value="<%= request.getParameter("cardtype") %>" />
			<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
					<thead>
			<s:if test="iscouriertrackenabled">
				<tr>
					<td colspan='9'>
					
					</td>
				</tr>
			</s:if>
					<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th> Card Number </th>
						<th> Account No </th>
						<th> Cin </th>
						<th> Name </th>						
						<th> Product Name </th>
						<th> Generated Date </th>
						<th> Generated by </th> 
					</tr>
					</thead>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cardissueauthlist" status="incr">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						<td> <%= rowcount %> </td>
						<s:if test='padssenabled=="Y"' >
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum%{#incr.index}" fieldValue="%{ORG_CHN}"/>  </td>
						</s:if>
						<s:else>
						<td> <s:checkbox name="personalrefnum"  id="personalrefnum%{#incr.index}" fieldValue="%{ORG_CHN}"/>  </td>
						</s:else>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ACCT_NO}  </td>
						<td> ${CIN}  </td>
						<td> ${EMB_NAME}  </td>
						<td> ${ PNAME }  </td>
						<td> ${GENDATE} </td> 
						<td> ${ USERNAME }</td>
					</tr> 
					</s:iterator>
					 
					
	 	</table>   
	 	
	 	<table>
	 			<tr>
						<td colspan="8">
							<s:submit name="report" id="report" value=" GetExcel " onclick="return GetExcel('E');"/>	
						
							<s:submit value="Authorize" name="authorize" id="authorize" onclick="return GetExcel('A');"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Reject" name="deauthorize" id="deauthorize" onclick="return selectallvalidation();"/>
				 
						</td>
					</tr>
	 	</table>
</div>
<s:hidden name="fromdate"/>
<s:hidden name="todate"/>
<s:hidden name="type" value="ISSUE"/>
</s:form>


 