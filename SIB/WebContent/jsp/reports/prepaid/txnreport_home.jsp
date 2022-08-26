<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/customeselectbox.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/customselectbox.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<style>
 .disabled{
 	border-color: silver !important;
 }
</style>
<script type="text/javascript">
	function enableDateSelection( dateval ){
		 
		busdaterow = document.getElementById("busdaterow");
		txndate1 = document.getElementById("txndaterow1");
		txndate2 = document.getElementById("txndaterow2");
		
		if( dateval == "busdate"){
			busdaterow.style.display ='table-row'; 
			txndate1.style.display ='none';
			txndate2.style.display ='none';
			
		}else{
			busdaterow.style.display ='none';
			txndate1.style.display ='table-row';
			txndate2.style.display ='table-row';
		}
		return false;
	} 

function validate(){
	var busdate=document.getElementsByName("datefld");
	var businessdate = document.getElementById("businessdate");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");	
	for(var i=0;i<busdate.length;i++){
		if(busdate[i].checked){
			if(busdate[i].value == "busdate"){
				if(businessdate.value == ""){
					errMessage(businessdate,"Select Business date");
					return false;
				 }			
			}else{
				if(fromdate.value == ""){
					errMessage(fromdate,"Select from date");
					return false;
				 }
				if(todate.value == ""){
					errMessage(todate,"Select to date");
					return false;
				 }
			}
		}
	}
	valid=true;
	var oneselected = false;
	var orderreflen = document.getElementsByName("txnlist");
	//alert(orderreflen.length);
	for(var i=1;i<=orderreflen.length;i++){
		var glkeysid  = 	"txnlist"+i;
		//alert(glkeysid);
		var orderref = document.getElementById(glkeysid);
		//alert(orderref.checked);
		if(orderref.checked==true){
			oneselected = true;	 
			break;
		}
	}if(!oneselected){
		errMessage(orderref,"Select atleast one transactions");
		valid= false;
		return false;
	}
	return true;
	
}

  </script>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form name="transaction" id="transaction" autocomplete="off" action="genTxnReportTransactionReport.do">
	<table cellspacing="5" cellpadding="2" border="0" align="center" class="formtable" style="margin-top:30px">
		
		
		<tr>
		<td colspan='3'> <input type="radio" name="datefld" value="busdate" id="busdate" checked  onclick="enableDateSelection(this.id)"/> Business Date  &nbsp; &nbsp; <input type="radio" name="datefld" id="trandate" value="txndate" onclick="enableDateSelection(this.id)" /> Transaction Date  </td>
		</tr>
		
		<tr>
			<td>Select Bin </td>
			<td>:</td>
			<td style="text-align:left"> <s:select name="bin" id="bin" listKey="BIN" listValue="BIN_DESC" list="reconbean.binlist"  headerValue="ALL" headerKey="ALL" value="%{BIN_DESC}" tooltip="Select BIN"/>
		</td>
		</tr>
		<tr>	
	  	  <td> Select Transactions </td>	
	  	  <td>:</td>
		  <td class="textcolor" style="text-align:left">			  	
			  	<div class="cusmselect" style="width: 200px;">
	 				<a href="#" onclick="sildedown(this)"><p class="hida">Select</p></a>
				    <div class="mutliSelect">
					  <ul>	 		
				 	 	<% int x=0; %>
				 		<s:iterator value="txn_list">
				 			<% x++; %>  
				 			 <li><input type='checkbox' name="txnlist" value="${TXN_CODE}" id="txnlist<%=x%>"/><label>${ACTION_DESC}</label></li> 
				 		</s:iterator>			 
					  </ul>
					</div>	
			   </div>
			</td>
		</tr>	
		
		<tr id="busdaterow">
			<td> Business Date  </td>
			<td>:</td>
			<td> <input type="text" id="businessdate" name="businessdate" />
				 <img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transaction.businessdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr> 
		
		<tr id="txndaterow1" style="display:none">
			<td> From Date  </td>
			<td>:</td>
			<td> <input type="text" id="fromdate" name="fromdate" />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transaction.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr> 
		
		<tr id="txndaterow2"  style="display:none" >
			<td> To Date  </td>
			<td>:</td>
			<td> <input type="text" id="todate" name="todate" />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transaction.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr> 

		<tr>
			<td colspan="3">
				<input type="submit" name="generate" value="PDF" onclick="return validate();"/>
				<input type="submit" name="generate" value="Excel" class="cancelbtn" onclick="return validate();"/>							
			</td>
		</tr>
	</table>
  </form>
</body>
</html>