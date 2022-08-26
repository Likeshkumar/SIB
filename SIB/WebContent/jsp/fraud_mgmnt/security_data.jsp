<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<link rel="stylesheet" type="text/css" href="../../style/ifps.css"/>
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
	 
    
     <tr>	
		<td> Card group id </td>
		<td> : <s:textfield name="cardgrpid" id="cardgrpid"/> </td>
	</tr>
	
	
    <tr>	
		<td> Transaction Code </td>
		<td> : <s:textfield name="txncode" id="txncode"/> </td>
	</tr>
	 
	<tr>	
		<td> SMS REQUIRED </td>	<td> : <s:checkbox name="smsrequired" id="smsrequired" value="SMSREQ"/> </td>
	</tr>
	
	<tr>	
		<td> SMS REQUIRED </td>	<td> : s </td>
	</tr>
	
	
</table>


 
</s:form>
</div>
 