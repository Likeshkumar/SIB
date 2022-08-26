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
		if( dateval.length == 2 ){
			if ( newval.length == 2 ){
			document.getElementById(dateid).value  = dateval+"-";
			}
		}
		 
		if( dateval.length == 5 ){
			 
				document.getElementById(dateid).value  = dateval+"-";
			 
		}
		 
		
	}
	function searchRecords(){
		
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
		 document.getElementById("displayresult1").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";  
		 var url = "searchTransactionDispute.do?cardno="+cardvalue+"&txndate="+txndateval+"&tracenoval="+tracenoval+"&txnrefno="+txnrefnoval; 
		 var result = AjaxReturnValue(url); 
		 var jsonobj = JSON.parse(result);  
		  var tablerec = "<table border='0' cellpadding='0' cellspacing='0' width='100%' class='formtable'>";
		 if( jsonobj["RESP"] != 0 ){
			 tablerec +="<tr><td colspan='7' style='text-align:center'><b>"+ jsonobj["REASON"] +"</b></td></tr>";
		}else{
			
			 var innerset = jsonobj["SET0"];  
			  var count = jsonobj["RECORDCNT"] ; 
			
			if( count > 0 ){
				tablerec += "<tr><td colspan='8' style='text-align:left'> <small class='dt'> * For register compliant click on the card number</small></td></tr>";
			}
			tablerec += "<tr><th>Card No</th> <th>Txn Description</th>  <th>Txn Amount</th> <th>Txn Currency</th> <th>Txn Date</th> <th>Trace No</th>  <th>Txn Ref no</th>  <th>Location</th> </tr>";
			
			for (var i=0; i<count; i++ ){
				var innersetkey = "SET"+i;
				var innerset = jsonobj[innersetkey]; 
				 var CARDNO = innerset['CHN'];
				 var REFNO = innerset['TXNREFNUM'];
				 var TRACENO = innerset['TRACENO']
				 tablerec +="<input type ='hidden' name='cardno' id='cardno"+i+"' value="+CARDNO+" />";
				 tablerec += "<tr  class='rowrec' id='recordrow_"+i+"'  onmouseover='showSelect(this.id)'  onmouseout='showDeSelect(this.id)'>";
				 tablerec +="<td  style='color:red;font-weight:bold' onclick=\'showComplaints("+i+", "+REFNO.toString()+", "+TRACENO.toString()+")\'>"+CARDNO+"</td>"; 
				 tablerec += "<td style='text-align:left'>"+innerset['TXNDESC']+"</td>";
				 tablerec += "<td  style='text-align:right' >"+innerset['TXNAMOUNT']+"</td>";
				 tablerec += "<td>"+innerset['TXNCURRENCY']+"</td>";
				 tablerec += "<td>"+innerset['TRANDATE']+"</td>"; 
				 tablerec += "<td>"+TRACENO+"</td>";
				 tablerec += "<td>"+REFNO+"</td>"; 
				 tablerec += "<td>"+innerset['TERMLOC']+"</td>";
				 tablerec += "</tr>";
			}  
		} 
		tablerec += "</table>";
		 
		
	   document.getElementById("displayresult1").innerHTML = tablerec;
		 
		return false;
			
	}
	
	function showComplaints(index, refno, traceno){
		var cardno = document.getElementById("cardno"+index).value;
		var conf = confirm( "Do you want to register complient for the Card No ["+cardno.toString()+"], Ref No ["+refno+"], Trace no["+traceno+"]  ");
		if( conf ){
			window.location = "complientRegHomeDispute.do?CARDNO="+cardno+"&REFNO="+refno+"&TRACENO="+traceno;
		}
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


<s:form action="#" name="orderform"	onsubmit="return searchRecords()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="100%"
		align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="100%"
					align="center">
					<tr>
						<td>Card No</td>
						<td><input type='text' name='cardno' id='cardno' />
						<span class="mand">*</span>
						</td>
						
						<td>Transaction Date</td>
						<td><input type='text' name='txndate' id='txndate' maxlength="10"
							onkeyup="addSeperator(this.id, this.value)" />
							<span class="mand">*</span>
							<br />
						<small class='dt'>&nbsp;(dd-mm-yyyy)</small></td>
						
						
						<td>Trace No</td>
						<td><input type='text' name='traceno' id='traceno' /></td>
						
						
						
						<td>Txn Ref no</td>
						<td><input type='text' name='txnrefno' id='txnrefno' /></td>
				 
					</tr> 
				 
					<tr>
						<table border="0" cellpadding="0" cellspacing="0" width="20%">
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
				<div id="displayresult1">&nbsp;</div>
			</td>
		</tr>

	</table>

</s:form>

