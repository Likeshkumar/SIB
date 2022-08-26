<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
<script>
 	function getSubProd( instid, selprodid ){  
 			prodid = selprodid.split("~"); 
	 		var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid[0];  
	 		result = AjaxReturnValue(url).split("~");
	 		 
	 		subprodlist = result[0];
	 		maxallcard = result[1];
	 		document.getElementById("subproductlist").innerHTML = result; 
	 		 
	 		
 	}
 	function maxAllowedCards( instid, subprodid ){
 		var bin = document.getElementById("cardtype").value;
 		if( bin != "-1~-1"){
 			var binval = bin.split("~");
 		var url = "getMaxCardCount.do?instid="+instid+"&subprodid="+subprodid+"&bin="+binval[0];
 		document.getElementById("count").value ="";
 		var result = AjaxReturnValue(url);
 		 
 		document.getElementById("maxallowedcard").value = result;
 		}else{
 			alert("select bin");
 		}
 	}
 	function checkMaxcards( givenmaxcards ){
 		var maxallcards = 1;
 		if ( document.getElementById("maxallowedcard").value != "" ){
 		 		maxallcards = document.getElementById("maxallowedcard").value;
 		} 
 		 
 		
 		if( parseInt (givenmaxcards) > parseInt(maxallcards) ){ 
 			document.getElementById("order").disabled=true; 			
 			errMessage(maxallcards, "Maximum No.of cards allowed " + maxallcards );		
 			
 		}else{
 			document.getElementById("order").disabled=false;
 		}
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
<div align="center">
<s:form action="viewDeleteOrderInstCardorderAction"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">
		<% 	if(usertype.equals("INSTADMIN"))
		{
	%>
		<tr>
				<td class="fnt"> Select Branch</td>
				<td> :&nbsp;<s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1" value="%{BRANCH_CODE}" tooltip="Select Branch"/>
				
				</td>
			</tr>
			
	<%	
		}
		else
		{
	%>
			<tr>
			<td class="fnt"> Branch:</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
	<%		
		}
	%>
	<tr>
		<td>
		  Card 
		</td>
		<td> :
				
 				<select name="cardtype" id="cardtype" onchange="getSubProd( '<%= instid %>', this.value );">
 				<option value="-1~-1">--Select -- </option>
 				<s:iterator  value="prodlist">
 				<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				</select>
		</td>
    </tr>
    <tr>
    </tr>

	 <%  if( datefilter_req.equals("1")) { %>
    <tr> 
    	<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
    </tr>	
    	
    <tr>	
		<td> 
		To Date
		</td>
		<td> : <input type="hidden" name="Ordertype" value="I">
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> 
	<% }  %> 
	 
</table>


<table border='0' cellpadding='0' cellspacing='4' width='20%' >
		<tr>
		<td>
			<s:submit value="Next" name="order" id="order" onclick="return validateinstorder()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
</div>
 