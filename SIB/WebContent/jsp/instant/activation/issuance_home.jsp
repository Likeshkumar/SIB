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
 	function getcardslist()
 	{
 		
 		var branchcode = document.getElementById("branchcode");
 		//alert("fdsfs"+branchcode);
		if( branchcode ){ if( branchcode.value == "ALL") { errMessage(branchcode, "Select Branch !");return false; } }

 		document.orderform.action = "listofCardActivCardsCardActivation.do";
 		document.orderform.submit;}
 	
 	
 	
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
 
<s:form action="activattCardActionCardActivation"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="80%">

			<tr bgcolor="#3197e0" align="center">
                    <td > Select Branch</td>
                    <td > Product</td>
                    <td >From Date</td>
                    <td > To Date</td>
                   
                    <td > Card Collect Branch</td>
                   
                    </tr>
	 <tr>
	 
	
    		<% 	if(usertype.equals("INSTADMIN"))
				{
			%>
		 
				 
				<%-- <td> :&nbsp;<s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1" value="%{BRANCH_CODE}" tooltip="Select Branch"/> --%>
				
								<td>
					<select name="branchcode" id="branch">
					<option value="ALL">--Select -- </option>
					<s:iterator  value="branchlist">
					<option value="${BRANCH_CODE}">${BRANCH_CODE} - ${BRANCH_NAME}</option>
					</s:iterator>
					</select>
				</td>
			 
			
			<%	
				}
				else
				{
			%>
			 
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			 
			<%		
				}
			%>
    
    
    
	 
		<td> :
				
 				<select name="cardtype" id="cardtype" onchange="getSubProd( '<%= instid %>', this.value );">
 				<option value="ALL">--Select -- </option>
 				<s:iterator  value="prodlist">
 				<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				</select>
		</td>
     
    <%  if( datefilter_req.equals("1")) { %>   
     
    	 
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate" value="${fromdate}" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
     
		<td> : <input type="hidden" name="Ordertype" value="I">
			<input type="text" name="todate" id="todate" value="${todate}"onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
<!-- card collect branch -->		
		 <s:if test="%{!cardstobranchlist.isEmpty()}">
			<td>
				<select id="collectbranch" name="collectbranch">
					<s:if test ="%{cardstobranchlist.isEmpty()}">
					<option value="">No Branch Found</option>
					</s:if>
					<s:else>
								<option value="ALL" >Select Card Collect Branch</option>   
								<s:iterator value="cardstobranchlist">
								<option value="<s:property value="BRANCH_CODE"/>"><s:property value="BRANCH_NAME"/></option>
								</s:iterator>
					</s:else>
					</select> 
			</td>
			</s:if>
	</tr> 
	<% }  %>  	
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Search" name="order" id="order" onclick="return getcardslist(this.form)"/>
		</td>
		 <s:if test="%{!cardactbean.cardawaitinglist.isEmpty()}">
		<td>
			<s:submit value="Submit" name="submit" onClick=" return branchwiseaction(this.form)"/>
		</td>
		</s:if>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
<div id="divScrollform" class="CardOrder">

    <s:if test="%{!cardactbean.cardawaitinglist.isEmpty()}">
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
						<th> Emb Name </th>						
						<th> Product  </th>
						<th> Sub Product  </th>
						<th> Pre Date </th> 
					</tr>
					</thead>
					<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="cardactbean.cardawaitinglist">
					<tr> 
						<%  int rowcount = ++rowcnt; %>
						<td> <%= rowcount %> </td> 
						<td> <input type="checkbox" name="instorderrefnum"  id="orderrefnum<%=rowcount%>"  value="${ORG_CHN}"/>  </td>
						<td> ${MCARD_NO}  </td>
						<td> ${ORDER_REF_NO}  </td> 
						<td> ${EMB_NAME} </td> 
						<td> ${ PRODUCUT_DESC }  </td>
						<td> ${ SUBPRODUCUT_DESC }  </td>
						<td> ${ PRE_DATE }</td>  
					</tr> 
			 	</s:iterator> 
	 	</table>   
	 	
	 	
</div>
</s:if>
	 	<s:else>

<table border='0' cellpadding='0' cellspacing='0' width='80%'
	style='text-align: center;'>
	<tr>
		<td style='text-align: center;'>
			<font color="green"><b>	get No of cards to allocate to the branches..</b></font>	
		</td>
	</tr>
</table>  
</s:else>
</div>
</s:form>
 
 