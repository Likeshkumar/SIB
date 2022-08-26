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
 	function getcardslist(type)
 	{
 		var cardno = document.getElementById("cardno");
 		var otptype = document.getElementById("otptype");
 		//alert("fdsfs"+otptype.values);
 		/* if( cardno ){ if( cardno.value == "") { errMessage(otptype, "Please enter cardno !");return false; } } */
		if( otptype ){ if( otptype.value == "-1") { errMessage(otptype, "Select OTP Type !");return false; } }

 		document.orderform.action = "otpcardslistrepinAndActiveInstCardRegisterProcess.do";
 		document.orderform.submit;}
 	
 	
 	
 	function branchwiseaction(type){
 		
 		
 		 valid=true;
 		 var Order_check = null;
 		 var Order_Ref_no = document.getElementsByName("orgchn");
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
 		 
 		/* var crdcollectbranch = document.getElementById("collectbranch");
 		//alert("sada"+crdcollectbranch);
		if( crdcollectbranch ){ if( crdcollectbranch.value == "ALL") { errMessage(crdcollectbranch, "Select Card Collect Branch !");return false; } } */

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
 
<s:form action="confrimotpchangestatusInstCardRegisterProcess"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="80%">

			<tr>
	 
	  <td class="txt"> Enter CardNumber  </td> <td> 
			
			 <s:textfield name="cardno" id="cardno"  maxlength="19" value= "%{cardregbean.cardno}" onkeyup="validateNumber('cardno  ',this.id,this.value)" /> 
		</td>
	 
		 <td class="txt"> OTP TYPE  <span class="mand">*</span> </td> <td> 
		   <s:select list="#{'RO':'RePinCardsForOTP','NO':'NewCardsForOTP'}" id="otptype"  headerKey="-1" headerValue="-SELECT-" name="otptype" /> </td>
     <%-- <s:select list="#{'NO':'NewCardsForOTP'}" id="otptype"  headerKey="-1" headerValue="-SELECT-" name="otptype" /> </td> --%>
    	
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" align="center">
		<tr>
		<td>
			<s:submit value="Search" name="order" id="order" onclick="return getcardslist(this.form)"/>
		</td>
		 <s:if test="%{!cardregbean.instorderlist.isEmpty()}">
		<td>
			<s:submit value="Submit" name="submit" onClick=" return branchwiseaction(this.form)"/>
		</td>
		</s:if>
		
		</tr>
</table>
<div id="divScrollform" class="CardOrder">

    <s:if test="%{!cardregbean.instorderlist.isEmpty()}">
<div id="fw_container">	
<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
				 
					<input type="hidden" name="bin" value="<%= request.getParameter("cardtype") %>" />
					<input type="hidden" name="branchcode" value="<%= request.getParameter("branch") %>" />
					<thead>
					<tr>
						<th> Sl no </th>
						<th NoSortable="true" > 	  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/>  </th> 
						<th> Card No. </th>
						<th> Order Ref No. </th> 
											
						<th> BIN  </th>
						<th> MobileNo </th>	
						<th> Emb Name </th>	
						
						<th> AccountNO </th>	
						<th>CustID </th>
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cardregbean.instorderlist">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<%-- <td> <input type="checkbox" name="hcardno"  id="hcardno<%=rowcount%>"  value="${HCARD_NO}"/>  </td> --%>
						<td> <input type="checkbox" name="orgchn"  id="orgchn<%=rowcount%>"  value="${ORG_CHN}"/>  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ORDER_REF_NO}  </td> 
						
						<td> ${BIN} </td> 
						<td> ${MOBILENO} </td> 
						<td> ${EMB_NAME} </td> 
						<td> ${ACCOUNT_NO} </td> 
						<td> ${CIN} </td> 
					
						
						
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
 
 