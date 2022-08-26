<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>

 <script>
 	function submitvalidation (){
 		var subproduct = document.getElementById("BatchID");
 		var product_code = document.getElementById("product_code");
 		var subproduct = document.getElementById("subproduct");
 		//alert("fdsfs"+BatchID.values);
		if( BatchID ){ if( BatchID.value == "-1") { errMessage(BatchID, "Select batch ID !");return false; } }
		if( product_code ){ if( product_code.value == "-1") { errMessage(product_code, "Select Productcode !");return false; } }
		if( BatchID ){ if( subproduct.value == "-1") { errMessage(subproduct, "Select subproduct !");return false; } }

 		
 		
 	}
 	function validateFilter (){
 		
 		parent.showprocessing();
 	}

 	function getSubProd(selprodid ){   
 			var url = "getSubProductListByProductDebitCustomerRegister.do?prodid="+selprodid+"&status=1";   
 			//alert(url);
 			result = AjaxReturnValue(url);   
 			//alert(result);
 			document.getElementById("subproduct").innerHTML = result;  
 			
 			//var hidsubproduct = document.getElementById("hidsubproduct").value; 
 			//document.getElementById("subproduct").value=hidsubproduct;
 			//getLimitBySubProduct(hidsubproduct);
 			//getFeeBySubProduct(hidsubproduct);
 			   
 	} 
 	
 	function GetBulkRegisteredCustomerList()
 	{
 		var BulkRegType = document.getElementById("BulkRegType");
 		var subproduct = document.getElementById("BatchID");
 		var product_code = document.getElementById("product_code");
 		var subproduct = document.getElementById("subproduct");
 		//alert("fdsfs"+BatchID.values);
 		if( BulkRegType ){ if( BulkRegType.value == "-1") { errMessage(BulkRegType, "Select BulkRegType  !");return false; } }
		if( BatchID ){ if( BatchID.value == "-1") { errMessage(BatchID, "Select batch ID !");return false; } }
		if( product_code ){ if( product_code.value == "-1") { errMessage(product_code, "Select Productcode !");return false; } }
		if( BatchID ){ if( subproduct.value == "-1") { errMessage(subproduct, "Select subproduct !");return false; } }

 		

 	 // document.orderform.action = "ViewRegisteredCustomerDataBulkCustomerRegister.do?BatchID="+BatchID+"&status=1";
 		document.orderform.action = "ViewBulklaodedcardsBulkCustomerRegister.do?BulkRegType="+BulkRegType.value+"&BatchID="+BatchID.value+"&product_code="+product_code.value+"&subproduct="+subproduct.value+"&status=1";

 		document.orderform.submit;
 	}
 	function deleteregisteredbatch()
 	{
 		alert("sardar");
 		var subproduct = document.getElementById("BatchID");
 			if( BatchID ){ if( BatchID.value == "-1") { errMessage(BatchID, "Select batch ID !");return false; } }
		

 	 // document.orderform.action = "ViewRegisteredCustomerDataBulkCustomerRegister.do?BatchID="+BatchID+"&status=1";
 		document.orderform.action = "deletebatchBulkCustomerRegister.do?BatchID="+BatchID.value+"&product_code="+product_code.value+"&subproduct="+subproduct.value+"&status=1";

 		document.orderform.submit;
 	}
 	
 	
 	function branchwiseaction(type){
 		 		
 		 valid=true;
 		 var Order_check = null;
 		 var Order_Ref_no = document.getElementsByName("instorderrefnum");
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

<% 
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ"); 
%>
 
<s:form action="checkuploadedcardsforreissueBulkCustomerRegister.do"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">

<%-- <s:hidden name="acctrulecode" id="acctrulecode" value="%{Bulkcustregbean.batch_id}"/> --%>
 <%-- <input type="text" name="BatchID"id="acctrulecode"  value="${Bulkcustregbean.batch_id}"/>
 		 <s:hidden name="product_code" value="${Bulkcustregbean.blkproduct_code}" />
 		 <s:hidden name="subproduct" value="${Bulkcustregbean.blksubproduct}" /> --%>
<table border="0" cellpadding="0" cellspacing="0" width="47%" >

	<tr align="left">
	 <td class="txt"> BulkRegType  <span class="mand">*</span>   </td><td> 
 		 <select name="BulkRegType" id="BulkRegType" headerKey="-1" headerValue="-SELECT-" >
	 		
	 		<s:if test="%{!Bulkcustregbean.regbulkchecktype}">
	 		<s:iterator  value="regbulktype">
	 				<option value="${REPLACEMENT_TYPE}">${REPLACEMENT_TYPE}</option>
	 		</s:iterator>
	 		</s:if>
	 		
	 		<s:else>
	 		<s:iterator  value="%{Bulkcustregbean.regbulkchecktype}">
	 				<option value="${Bulkcustregbean.regbulkchecktype}">${Bulkcustregbean.regbulkchecktype}</option>
	 		</s:iterator>
	 		</s:else>
 		 </select> 
 		 <%-- <td> :&nbsp;<s:select name="BatchID" id="BatchID" listKey="BATCH_ID" listValue="BATCH_ID" list="Bulkcustregbean.BatchID"  headerValue="Select Batch" headerKey="-1" value="%{BATCH_ID}" tooltip="Select Batch"/> --%>
	  </td>
	  
	  <td class="txt"> BATCH ID  <span class="mand">*</span>   </td><td> 
 		 <select name="BatchID" id="BatchID" headerKey="-1" headerValue="-SELECT-"value="%{Bulkcustregbean.batch_id}" >
	 		
	 		<s:if test="%{!Bulkcustregbean.batch_id}">
	 		<s:iterator  value="BatchID">
	 				<option value="${BATCH_ID}">${BATCH_ID}</option>
	 		</s:iterator>
	 		</s:if>
	 		
	 		<s:else>
	 		<s:iterator  value="%{Bulkcustregbean.batch_id}">
	 				<option value="${Bulkcustregbean.batch_id}">${Bulkcustregbean.batch_id}</option>
	 		</s:iterator>
	 		</s:else>
 		 </select> 
 		 <%-- <td> :&nbsp;<s:select name="BatchID" id="BatchID" listKey="BATCH_ID" listValue="BATCH_ID" list="Bulkcustregbean.BatchID"  headerValue="Select Batch" headerKey="-1" value="%{BATCH_ID}" tooltip="Select Batch"/> --%>
	  </td>
	  	 
	  </tr>
	 
	 <%--  <tr>
	
	  
	   <td class="txt"> Product  <span class="mand">*</span>  </td>
	   <td>
	  
	  <select id="product_code"  name="product_code"  onchange="getSubProd(this.value)"  >
			 <option value="-1" >Select Product</option>
			<s:if test="%{!Bulkcustregbean.productlist.isEmpty()}">
			 <s:iterator  value="Bulkcustregbean.productlist">
	 				<option value="${PRODUCT_CODE}">${PRODUCT_CODE}</option>
	 		</s:iterator>  
				</s:if>
				
	 		
				<s:else>
				
	 				<option value="${Bulkcustregbean.blkproduct_code}">${Bulkcustregbean.blkproduct_code}</option>
	 				
				</s:else>
			 
			 </select>
			 
					
		</td>
		
				  <td class="txt"> Sub Product   <span class="mand">*</span></td>
				  <td>
				 
				  <select  id="subproduct"  name="subproduct" onchange="getFeeBySubProduct(this.value);getCurrency(this.value);getLimitBySubProduct(this.value);" >
			    	 <option value="-1" >Select Sub Product</option>
			    	</select>
			    	
			    	  
					  <s:if  test="SUB_PROD_ID==dbtcustregbean.subproduct" >
					 	<s:property value="SUB_PRODUCT_NAME"/> </s:if> 
	               </td>
		</tr> 
		--%>
		
		</table>
		
	<table border="0" cellpadding="0" cellspacing="0" width="47%" align="center" >
				 <tr>
	  
				 <td>
	
	  <s:submit value="Search" name="order" id="order" onclick="return GetBulkRegisteredCustomerList(this.form)"/>
			
		
		<s:if test="%{!Bulkcustregbean.BulkRegCustlist.isEmpty()}">
		<s:submit value="Submit" name="submit" id="submit"  onclick="return submitvalidation(this.form)" />
	     <s:submit value="DELETE" name="delete" id="delete" onclick="return deleteregisteredbatch(this.form)" />
		</s:if>
	
</td>
</tr>
</table>


<div id="divScrollform" class="CardOrder">

<s:if test="%{!Bulkcustregbean.BulkRegCustlist.isEmpty()}">
	<div id="fw_container">	
		<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
						 
			<input type="hidden" name="RegistredDate" value="<%= request.getParameter("RegistredDate") %>" />
			<thead>
			<tr>
				<th> Sl no </th>
				<th NoSortable="true" > <input type='hidden' onclick="checkedAll(this.form)"  id="checkall"/>  </th>
				<th> Register Ref No. </th>  
				<th> BATCH_ID</th>
				<th> CARD_NUMBER </th>	
				<th> REPLACEMENT_TYPE </th>							
				
				<th> Reg Status </th>
				
			</tr>
			</thead>
			<% int rowcnt = 0; Boolean alt=true; %> 
			<s:iterator value="Bulkcustregbean.BulkRegCustlist">
				<tr> 
					<%  int rowcount = ++rowcnt; %>
					<td> <%= rowcount %> </td> 
					<td> <input type="hidden" name="bulkregrefno"  id="bulkregrefno<%=rowcount%>"  value="${BULK_REG_REF_ID}"/>  </td>
					<td> ${BULK_REG_REF_ID} </td> 
					<td> ${BATCH_ID} </td> 						
					<td> ${CARD_NUMBER} </td> 
					<td> ${REPLACEMENT_TYPE} </td> 
				
					<td> ${REG_STATUS} </td>
					
				</tr> 
			</s:iterator> 
			
		</table>   
			 	
	</div>
</s:if>
<s:else>
	<table border='0' cellpadding='0' cellspacing='0' width='80%'
		style='text-align: center;'>
		<tr>
			
		</tr>
	</table>  
</s:else>
</div>
</s:form>
 