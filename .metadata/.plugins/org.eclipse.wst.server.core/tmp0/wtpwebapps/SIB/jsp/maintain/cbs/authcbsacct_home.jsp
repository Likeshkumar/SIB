<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>

<script type="text/javascript">
	function authorizeCard(index){
		 
		cardno = document.getElementById("card"+index).value;
		 var cbsacctno = document.getElementById("acctno"+index).value;
		if( !confirm("Do you want to continue") ){
			return false;
		}  
		var url = "authAcctDetailsCbsAccount.do?cardno="+cardno+"&cbsacctno="+cbsacctno+"&actionvalue=1";  
		var result = AjaxReturnValue(url); 
		 alert( result )
		getCbsAccountNumbers();
	}

	
	function deAuthorizeCard(index){
		 
		cardno = document.getElementById("card"+index).value;
		 var cbsacctno = document.getElementById("acctno"+index).value;
		if( !confirm("Do you want to continue") ){
			return false;
		}  
		var url = "authAcctDetailsCbsAccount.do?cardno="+cardno+"&cbsacctno="+cbsacctno+"&actionvalue=9";  
		var result = AjaxReturnValue(url); 
		 alert( result )
		getCbsAccountNumbers();
	}
	
	function getCbsAccountNumbers(){
		 document.getElementById("cbsview").innerHTML="Processing.....";
		var cardno = document.getElementById("cardno");
		var url = "getCbsAcctDetailsCbsAccount.do?cardno="+cardno.value+"&type=AUTH";
		 var tablerec = ""; 
		var result = AjaxReturnValue(url);
		 
		
		
		var jsonobj = JSON.parse(result);  
		
		 
		 if( jsonobj["RESPONSE"] != 0 ){
			 tablerec +="<tr><td colspan='7' style='text-align:center'><b>"+ jsonobj["REASON"] +"</b></td></tr>";
			 document.getElementById("cbsview").innerHTML=tablerec;
			 return;
		}else{  
			  var count = jsonobj["RECORDCNT"] ;  
			  
			  tablerec += "<tr><th>Card No</th> <th>BANK</th> <th>ACCOUNT NUMBER</th>   <th style='text-align:right'>ACCT HOLDER NAME</th>  ";
			  tablerec += "<th>ADDED DATE</th> <th> ADDED BY</th> <th> AUTH STATUS </th><th> AUTH BY</th> <th> AUTH DATE </th> ";
			  
				for (var i=1; i<=count; i++ ){
					var innersetkey = "RECORD"+i;
					var innerset = jsonobj[innersetkey];  
					 var CARDNO = innerset['CARD_NO'];  
					 
					 tablerec += "<tr  class='rowrec' id='recordrow_"+i+"'  onmouseover='showSelect(this.id)'  onmouseout='showDeSelect(this.id)'>";
					 tablerec +="<input type='hidden' id='card"+i+"' value='"+CARDNO+"' /> <input type='hidden' id='acctno"+i+"' value='"+innerset['CBS_ACCTNO']+"' />  <td title='Click here to authorize'  onclick=\'authorizeCard("+i+")\' >"+CARDNO+"</td>"; 
					 tablerec += "<td>"+innerset['BANK_CODE']+"</td>";  
					 tablerec += "<td>"+innerset['CBS_ACCTNO']+"</td>";   
					 tablerec += "<td>"+innerset['ACCTHOLDER_NAME']+"</td>";
					 tablerec += "<td>"+innerset['LINKED_DATE']+"</td>";  
					 tablerec += "<td>"+innerset['LINKED_BY']+"</td>";   
					 tablerec += "<td>"+innerset['AUTH_STATUS']+"</td>";
					 tablerec += "<td>"+innerset['AUTH_BY']+"</td>";
					 tablerec += "<td>"+innerset['AUTH_DATE']+"</td>";
					 tablerec +=" <td  onclick=\'authorizeCard("+i+","+innerset['CBS_ACCTNO']+"),'"+innerset['DOACTIONKEY']+")\' ><input type='button' value='"+innerset['DOACTION']+"' /></td>";
				     tablerec +=" <td  onclick=\'authorizeCard("+i+","+innerset['CBS_ACCTNO']+","+innerset['DOACTIONKEY']+")\' ><input type='button' value='"+innerset['DOACTION']+"' /></td>";
					 
					 tablerec += "</tr>";
				}   
			}  
		  
		 document.getElementById("cbsview").innerHTML=result;
		 return false;
	}
<!--

//-->
</script>
<s:form action="#" name="orderrm" onsubmit="return getCbsAccountNumbers()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="90%"
		align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="40%" align="center">
					<tr>
						<td>Card No</td>
						<td> : <input type='text' name='cardno' id='cardno'   maxlength=19/>
						 <td><s:submit value="Submit" name="order" id="order" /></td>
					</tr> 
				</table>
			</td>
		</tr> 
	</table>

	<table border="0" id="cbsview" cellpadding="0" cellspacing="0" width="100%"	style="text-align:center;border:1px solid gray">
		
	</table>
	
</s:form>

