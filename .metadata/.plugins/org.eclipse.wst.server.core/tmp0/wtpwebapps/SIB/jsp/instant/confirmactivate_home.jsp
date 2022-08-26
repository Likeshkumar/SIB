<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>

<script>
	function confirmActivation(){
		var conf = confirm( "Do you want to confirm ");
		if( !conf ){
			return false;
		}
	}
	
	function goBack()  {
	  window.history.back();
	}
</script>
<style>
  table.activatetable{
  	text-align:center;
  }
  table.activatetable, table.summerytable, table.innertable, table.curtable{
 	border:1px solid gray;
 	border-collapse: collapse;
 	
 }
 
 table.curtable td{
 	border:1px solid gray;
 	color:red;
 }
   table.summerytable, table.innertable, table.curtable{
 	border-left:none;
 	border-collapse: collapse;
 	
 }
 
 table.summerytable td, table.innertable td{
 	padding-top:5px;
 	border:1px solid gray;
 	 border-bottom:none;
 	  border-top:none;
 	  text-align:left;
 }
 
 table.summerytable td.amount{
 	text-align:right;
 }
 
</style>
 


<%
	 String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");  
%>

<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="confirmActivateInstCardActionInstCardActivateProcess"  name="orderform" onsubmit="return validateActive()" autocomplete = "off"  namespace="/">

 <%
 
 session.getAttribute("SUMMERYTABLE");
 
 %>

</s:form>
 
 	