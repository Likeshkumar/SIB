<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>

 <script>
 	function validateFilter (){
 		parent.showprocessing();
 	}
 	
 	function branchwiseaction(type){
 		
 		
 		 valid=true;
 		 var Order_check = null;
 		 var Order_Ref_no = document.getElementsByName("cardno");
 		 var oneselected = false;
 		// alert("A1: "+Order_Ref_no.values);
 		// alert("A2: "+Order_Ref_no.value);
 		
 		
 		 for(var i=0;i<Order_Ref_no.length;i++)
 		 {
 			 
 		  Order_check = Order_Ref_no[i];
 		  //alert("For: "+Order_check);
 		  if(Order_check.checked==true)
 		  {
 		   oneselected = true; 
 		   break;
 		  }
 		   
 		 }
 		 if(!oneselected)
 		 {
 		  errMessage(Order_Ref_no[0],"Select any one Card..");
 		  return false;
 		 }
 		 
 		var crdcollectbranch = document.getElementById("collectbranch");
 		//alert("sada"+crdcollectbranch);
		if( crdcollectbranch ){ if( crdcollectbranch.value == "ALL") { errMessage(crdcollectbranch, "Select Card Collect Branch !");return false; } }

 	}
 </script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="postToCBSCbsCardLinkAction"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">

<div id="divScrollform" class="CardOrder">


<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				 
					
					<thead>
					<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<th> Card Number </th>
						<th> Account No </th>
						<th> Name </th>						
						<!-- <th> Uploaded Date </th>
						<th> Uploaded by </th> 
						<th> Uploaded Resp Code </th> 
						<th> Uploaded Status </th>  -->
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cbscardslinklist">
					<tr> 
						<% if (alt) { alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					
						<td> <%= rowcount %> </td>
						<s:if test="%{UPLOAD_RESPCODE=='000'}">
						<td></td>
						</s:if>
						<s:else>
						<td> <s:checkbox name="cardno"  id="personalrefnum%{#incr.index}" fieldValue="%{ORG_CHN}"/>  </td>
						</s:else>
						<td> ${ORDER_REF_NO}  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ACCOUNT_NO}  </td>
						<td> ${EMB_NAME}  </td>
						<%-- <td> ${ADDED_DATE} </td> 
						<td> ${ UPLOAD_BY }</td>
						<td> ${UPLOAD_RESPCODE} </td> 
						<td> ${ UPLOAD_STATUS }</td> --%>
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	<br>
	 	<div align="center">
	 		<s:submit name="authorize" id="authorize" value=" Submit " onclick="return branchwiseaction(this.form)" />
						 
			<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
	 	
	 	</div>
</div>

</div>
</s:form>
 
 