<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<style>
	input{
		border:1px solid red;
		width:100px;
	}
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>

	function addSeperator( dateid, dateval ){
		var newval = dateval.replace("-","");		
		var radios = document.getElementsByName('checkedtype');
		if(radios[1].checked){
			if( dateval.length == 2 ){
				if ( newval.length == 2 ){
				document.getElementById(dateid).value  = dateval+"-";
				}
			}
			 
			if( dateval.length == 5 ){
				 
					document.getElementById(dateid).value  = dateval+"-";
				 
			}
		}		
		
	}
	
	function enableField(selectedval)
	{
		textcard = document.getElementById("textcard");
		
		if (selectedval == "cardno")
		{
			//alert(selectedval);
			textcard.innerHTML="card number";
			document.getElementById("searchtxnvalue").value="";
			document.getElementById("searchtxnvalue").maxLength="19";
			selectedtype.value="cardno";
			
			
		}
		
		else if (selectedval == "acctno")
		{
			//alert(selectedval);
			textcard.innerHTML="Account Number";
			document.getElementById("searchtxnvalue").value="";
			document.getElementById("searchtxnvalue").maxLength="19";
			selectedtype.value="acctno";
			
			
		}
		
		else if(selectedval =="txndate"){
			
			 
			textcard.innerHTML="txn date (dd-mm-yyyy)";			
			document.getElementById("searchtxnvalue").value="";
			document.getElementById("searchtxnvalue").maxLength="10";
			selectedtype.value="txndate";
			
		
		}
		else if(selectedval =="traceno"){
			
			//alert(selectedval);
			textcard.innerHTML="trace number";
			document.getElementById("searchtxnvalue").value="";
			document.getElementById("searchtxnvalue").maxLength="30";
			selectedtype.value="traceno";
			
		}
		else if(selectedval =="txnrefno"){
			
			//alert(selectedval);
			textcard.innerHTML="txn ref number";
			document.getElementById("searchtxnvalue").value="";	
			document.getElementById("searchtxnvalue").maxLength="30";
			selectedtype.value="txnrefno";
			
		}
		else {
			//alert(selectedval);
			textcard.innerHTML="No values";
			return false;
			
		}
						
	}
	
	function searchRecords(){
		
		var selectedtype = document.getElementById("selectedtype");
		//alert(selectedtype.value);
		var searchtxnvalue = document.getElementById("searchtxnvalue");
		
		
		if( searchtxnvalue.value==""){
			 errMessage(searchtxnvalue,"Enter any keywords for search txn");
			 return false;
		}
		/* alert(selectedtype.value);
		alert(searchtxnvalue.value); */
		if (selectedtype.value=="txndate")
		{
			
			var IsoDateRe = new RegExp("^([0-9]{2})-([0-9]{2})-([0-9]{4})$");
			var matches = IsoDateRe.exec(searchtxnvalue.value);
			//alert(matches); 
			if (searchtxnvalue.value.length != 10) {
				errMessage( searchtxnvalue, "Invalid date length");
		        return false; 
		    } 
			
			if (!matches) {
				errMessage( searchtxnvalue, "Invalid date ");
				return false;
			} 
			
			errMessage( searchtxnvalue, "&nbsp;");
		}
				
		/* alert(searchtxnvalue.value);
		 
		var cardno = document.getElementById("cardno");
		var txndate = document.getElementById("txndate");
		var traceno = document.getElementById("traceno");
		var txnrefno = document.getElementById("txnrefno");
		
		if( txndate.value != ""){
		
			var IsoDateRe = new RegExp("^([0-9]{2})-([0-9]{2})-([0-9]{4})$");
			var matches = IsoDateRe.exec(txndate.value);
			 
			if (txndate.value.length !== 10) {
				errMessage( txndate, "Invalid date length");
		        return false; 
		    } 
			
			if (!matches) {
				errMessage( txndate, "Invalid date ");
				return false;
			} 
			
			errMessage( txndate, "&nbsp;");
		}
		 
		
		
		var custname = document.getElementById("custname");
		var orderrefno =document.getElementById("orderrefno"); 
		var cardvalue = null;
		var tracenoval = null;
		var txndateval = null;
		var txnrefnoval = null; 
		
	     if( cardno.value != ""){
	    	 if( cardno.value.length  < 4 ){ errMessage(cardno,"Enter Cardnumber minimum 4 digit..");return false;}
	    	 
			 cardvalue = cardno.value; 
		 } 
		 if( traceno.value != ""){
			 tracenoval = traceno.value;			 
		 } 
		 if( txndate.value != ""){
			 txndateval = txndate.value;			 
		 } 
		 if( txnrefno.value != ""){
			 txnrefnoval = txnrefno.value;			 
		 } 
		 
		 if( cardno.value=="" && traceno.value=="" &&  txndate.value=="" && txnrefno.value=="" ){
			 errMessage(cardno,"Enter any keywords for search txn");
			 return false;
		 } */
		 //document.getElementById("displayresult1").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";
		 
		 var xmlhttp = new XMLHttpRequest();
		 var url = "searchTransactionSearchTxn.do?searchtxnvalue="+searchtxnvalue.value+"&selectedtype="+selectedtype.value; 
		 //alert(url);
		 xmlhttp.onreadystatechange = function() {
			    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			        var jsonobj = JSON.parse(xmlhttp.responseText);
			        //alert(jsonobj);
			       	myFunc(jsonobj);
			    }
			};
			xmlhttp.open("GET", url, true);
			xmlhttp.send();
			
		 function myFunc(jsonobj){
			 var innerset = jsonobj["SET0"]; 
			 var count = jsonobj["RECORDCNT"];
			 //alert(count);
			 if (count > 0)
			 {	 
				 var tablerec = "<table border='0' cellpadding='0' cellspacing='0' width='100%' class='formtable'>";
				 tablerec += "<tr><td colspan='8' style='text-align:left'>  No.of Records : " + count+ " </td></tr>";
				 tablerec += "<tr><th>Card No</th> <th>Cust Name</th> <th>Txn Desc</th> <th>Acc_No</th><th>Terminal ID</th>  <th>Txn Ref No</th> <th>Txn Date</th> <th>Txn Amt</th> <th>Currency</th> <th>Billing AMT</th>  <th>Terminal Loc</th> <th>Response</th></tr>";
				 for(var i=0;i<count;i++){
					 var innersetkey = "SET"+i;
					 var innerset = jsonobj[innersetkey]; 
					 var CARDNO = innerset['CHN'];
					 var REFNO = innerset['TXNREFNUM'];
					 var TRACENO = innerset['TRACENO'];
					 var TCHN = innerset['TCHN'];
					 
					 tablerec += "<input  type ='hidden' name='cardno' id='cardno"+i+"' value="+CARDNO+" />";
					 tablerec += "<input  type ='hidden' name='tchn' id='tchn"+i+"' value="+TCHN+" />";
					 tablerec += "<tr class='rowrec' id='recordrow_"+i+"'  onmouseover='showSelect(this.id)'  onmouseout='showDeSelect(this.id)'>";
					 tablerec += "<td onclick=\'showComplaints("+i+", "+REFNO.toString()+", "+TRACENO.toString()+")\'>"+CARDNO+"</td>";
					 tablerec += "<td>"+innerset['CUST_NAME']+"</td>";
					 if(innerset['TXNDESC'] != undefined){
						 tablerec += "<td >"+innerset['TXNDESC']+"</td>";
					 }else{
						 tablerec += "<td >"+"--"+"</td>";
					 }
					 //tablerec += "<td>"+innerset['TRACENO']+"</td>";
					 	 tablerec += "<td>"+innerset['FROMACCOUNT']+"</td>";
					 	 tablerec += "<td>"+innerset['TERMINALID']+"</td>";
					 tablerec += "<td>"+REFNO+"</td>"; 
					 tablerec += "<td>"+innerset['TRANDATE']+"</td>";
					 tablerec += "<td  style='text-align:right' >"+innerset['TXNAMOUNT']+"</td>";
					 tablerec += "<td>"+innerset['acqcurrencycode']+"</td>"; 
					 tablerec += "<td>"+innerset['billingamount']+"</td>";
					 tablerec += "<td>"+innerset['ACCEPTORNAME']+"</td>";
					 if(innerset['RESPCODE'] != undefined ){
						 tablerec += "<td>"+innerset['RESPCODE']+"</td>";
					 }else{
						 tablerec += "<td >"+"--"+"</td>";
					 }
					/*  tablerec += "<td>"+innerset['REASONCODE']+"</td>";  */
					 tablerec += "</tr>";
				 }
				 tablerec += "</table>"; 
				 document.getElementById("displayresult1").innerHTML = tablerec;
				 
			 }else{
				 
				 document.getElementById("displayresult1").innerHTML = "<div style=font-weight:bold;color:red;text-align:center;>No Records Found";
				 return false;
			 }
			 
		 }
		return false; 
		
	}
	
	function showComplaints(index, refno, traceno){
		var cardno = document.getElementById("cardno"+index).value;
		var tchn = document.getElementById("tchn"+index).value;
		
		var url = "${pageContext.request.contextPath}/viewTransDetailsDispute.do?CARDNO="+cardno+"&REFNO="+refno+"&TRACENO="+traceno+"&TCHN="+tchn; 
		newwindow = window.open(url,'','left=350,top=150,width=800,height=650,scrollbars=yes,resizable=yes,fullscreen = yes');
		
		/*var conf = confirm( "Do you want to register complient for the Card No ["+cardno.toString()+"], Ref No ["+refno+"] ");
		if( conf ){
			window.location = "complientRegHomeDispute.do?CARDNO="+cardno+"&REFNO="+refno+"&TRACENO="+traceno+"&TCHN="+tchn;
		} */
	}
	
	function showMaintain( instid, cardno ){
		var url = "showMaintainCardMaintainAction.do?instid="+instid+"&cardno="+cardno;
		var x = confirm( "Do you want to continue the maintenance activity for the card  " + cardno );
		if( !x ){
			return false;
		}
		window.location=url;
	}
	
	function showSelect( rowid ){
		document.getElementById(rowid).style.background="#F0F0F0";
	}
	
	function showDeSelect( rowid ){
		document.getElementById(rowid).style.background="#fff";
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="searchTransactionSearchTxn.do" name="orderform"	onsubmit="return searchRecords()" autocomplete="off" namespace="/" method="post">

	<table border="0" cellpadding="0" cellspacing="0" width="100%"
		align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="100%"
					align="center">
						<tr>
		
			<td>
				<input type="radio" name="checkedtype" id="cardno" checked  onclick="enableField( this.value )" value="cardno"/> Card Number 
			</td>
			
			<td>
				<input type="radio" name="checkedtype" id="acctno"  onclick="enableField( this.value )" value="acctno"/> Account Number 
			</td>
			
			<td>
				<input type="radio" name="checkedtype" id="txndate"  onclick="enableField( this.value )" value="txndate"/> txndate 
			</td>
			
			<td>
				<input type="radio" name="checkedtype" id="traceno"  onclick="enableField( this.value )" value="traceno" /> traceno 
			</td>
			<td>
				<input type="radio" name="checkedtype" id="txnrefno"  onclick="enableField( this.value )" value="txnrefno" /> txnrefno
			</td>
			
			</tr>
			
			
			<tr>
				<table border="0" cellpadding="0" cellspacing="0" width="30%">
				<tr>
					<td id="textcard">Card No</td>
					<td><input type='text' name='searchtxnvalue' id='searchtxnvalue' maxlength="19" />
					<input type="hidden" id="selectedtype" name="selectedtype"  value="cardno"/>
					 </td>
				</tr> 
				</table>
				<br>			
			</tr> 
			<tr>
			<br>
				<table border="0" cellpadding="0" cellspacing="0" width="20%">
				<br>
					<tr>
						<td><s:submit value="Search" name="order" id="order" /></td>
						<td><input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" /></td>
					</tr>
				</table>
			</tr>

				</table>
			</td>
		</tr>

		
		<tr>
			<td>
				<div style="width: 188ex;" id="displayresult1">&nbsp;</div>
			</td>
		</tr>

	</table>

</s:form>

