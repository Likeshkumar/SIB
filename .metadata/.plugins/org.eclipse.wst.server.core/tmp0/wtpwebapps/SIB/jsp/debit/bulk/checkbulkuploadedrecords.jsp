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
 	
 		alert("fdsfs");
 		

 	 // document.orderform.action = "ViewRegisteredCustomerDataBulkCustomerRegister.do?BatchID="+BatchID+"&status=1";
 		document.orderform.action = "ViewBulklaodedcardsloststolenBulkCustomerRegister.do?cardtype="+cardtype.value+"&status=1";

 		document.orderform.submit;
 	}
 	function deleteregisteredbatch()
 	{
 		//alert("sardar");
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
<%-- 
<% 
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ"); 
%>
  --%>
<s:form action="issubulkcardactionBulkCustomerRegister.do"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">

<%-- <s:hidden name="acctrulecode" id="acctrulecode" value="%{Bulkcustregbean.batch_id}"/> --%>
 <%-- <input type="text" name="BatchID"id="acctrulecode"  value="${Bulkcustregbean.batch_id}"/>
 		 <s:hidden name="product_code" value="${Bulkcustregbean.blkproduct_code}" />
 		 <s:hidden name="subproduct" value="${Bulkcustregbean.blksubproduct}" /> --%>
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">
	<tr>
		<td class="fnt">
		Select Product
		</td>
		<td> :
 				<select name="cardtype" id="cardtype">
	 				<option value="ALL">ALL</option>
	 				<s:iterator  value="personalproductlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
    
</table>
		
	<table border="0" cellpadding="0" cellspacing="0" width="47%" align="center" >
				 <tr>
	  
				 <td>
	
	  <s:submit value="Search" name="order" id="order" onclick="return GetBulkRegisteredCustomerList(this.form)"/>
			
		
		<s:if test="%{!Bulkcustregbean.BulkRegCustlist.isEmpty()}">
		<s:submit value="Submit" name="submit" id="submit"  onclick="return submitvalidation(this.form)" />
	    <%--  <s:submit value="DELETE" name="delete" id="delete" onclick="return deleteregisteredbatch(this.form)" /> --%>
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
				 <th NoSortable="true" > <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th>
				<!-- <th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th> -->
				<th> ORDER_REF_NO </th>  
				<th>McardNo</th>
				<th> AccountNo </th>	
				<th> Cin </th>							
				
				<th> CustName </th>
				<th> Lost/Stolel Marked Date </th>
				
			</tr>
			</thead>
			<% int rowcnt = 0; Boolean alt=true; %> 
			<s:iterator value="Bulkcustregbean.BulkRegCustlist">
				<tr> 
					<%  int rowcount = ++rowcnt; %>
					<td> <%= rowcount %> </td> 
					<td> <s:checkbox name="personalrefnum"  id="personalrefnum<%=rowcount%>" fieldValue="%{HCARD_NO}"/>  </td>
					
					<%-- <td> <input type="hidden" name="hcardno"  id="hcardno<%=rowcount%>"  value="${HCARD_NO}"/>  </td> --%>
					<td> ${ORDER_REF_NO} </td> 
					<td> ${MCARD_NO} </td> 						
					<td> ${ACCOUNT_NO} </td> 
					<td> ${CIN} </td> 
				
					<td> ${EMB_NAME} </td>
					<td> ${LSDATE} </td>
					
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
 