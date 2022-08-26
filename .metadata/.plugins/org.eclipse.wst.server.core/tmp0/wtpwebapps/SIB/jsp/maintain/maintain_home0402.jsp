<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 

<title>Call Center</title>

<script>
 
function showCustomerDetails( cardno ){
	
	var url = "editViewCustomerActionInstCardRegisterProcess.do?actiontype=VIEW&inputtype=cardno&cardno="+cardno; 
	newwindow = window.open(url,'','left=350,top=150,width=800,height=400,location=no,menubar=no,menu=no,status=no,scrollbars=yes,resizable=yes,toolbar=no,titlebar=0,fullscreen = yes,directories=no');
	 
}

function showCustomerDetailsCredit( cin ){ 
	//alert("Creditview");
	var url = "viewCustomerDataForViewCreditCustRegisteration.do?doact=VIEW&customerid="+cin; 
	newwindow = window.open(url,'','left=350,top=150,width=800,height=400,location=no,menubar=no,menu=no,status=no,scrollbars=yes,resizable=yes,toolbar=no,titlebar=0,fullscreen = yes,directories=no');
	 
}

function sendSequrityQuestion( cardno, username ){ 
		if( username == "Not Registered"){
			alert("User not registered...");
			return false;
		} 
		
		if( !confirm("Do you want to continue??") ){
			return false;
		}
		
		var url="sendSequritiyQuestionsCallCenterService.do?cardno="+cardno+"&username="+username;
		var result = AjaxReturnValue(url);
		alert(result);
		
}
	
function sendUserNameSms( cardno, username ){ 
		if( username == "Not Registered"){
			alert("User not registered...");
			return false;
		} 
		
		if( !confirm("Do you want to continue??") ){
			return false;
		}
		
		
		var url="sendUsernameasSmsCallCenterService.do?cardno="+cardno+"&username="+username;
		alert( url )
		var result = AjaxReturnValue(url);
		alert(result);
		
}

function searchFinancialRecords(){
	var displayrecords = 10;
	var cardno = document.getElementById("cardno");
	var txndate = document.getElementById("txndate");
	var traceno = document.getElementById("traceno");
	var txnrefno = document.getElementById("txnrefno");
	
	if( txndate.value != ""){ 
		var IsoDateRe = new RegExp("^([0-9]{2})-([0-9]{2})-([0-9]{4})$");
		var matches = IsoDateRe.exec(txndate.value);
		 
		if (txndate.value.length !== 10) { 	errMessage( txndate, "Invalid date length");   return false;     } 
		
		if (!matches) { 	errMessage( txndate, "Invalid date "); return false; 	}  
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
	 }
	 document.getElementById("displayresult2").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";  
	 var url = "searchPLTransactionSearchTxn.do?cardno="+cardvalue+"&txndate="+txndateval+"&tracenoval="+tracenoval+"&txnrefno="+txnrefnoval;
		//alert(url);
	 var result = AjaxReturnValue(url); 
	 document.getElementById("displayresult2").innerHTML = result;
}

function searchRecords(){
	
	var displayrecords = 10;
	var cardno = document.getElementById("cardno1");
	var txndate = document.getElementById("txndate1");
	var traceno = document.getElementById("traceno1");
	var txnrefno = document.getElementById("txnrefno1");
	
	//alert(txndate.value);
	
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
	 }
	 document.getElementById("displayresult1").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";  
	 var url = "searchTransactionSearchTxn.do?cardno="+cardvalue+"&txndate="+txndateval+"&tracenoval="+tracenoval+"&txnrefno="+txnrefnoval;
	//alert(url);
	 var result = AjaxReturnValue(url); 
	// alert(result);
	 var jsonobj = JSON.parse(result);  
	  var tablerec = "<table border='0' cellpadding='0' cellspacing='0' width='100%' class='formtable'>";
	 if( jsonobj["RESP"] != 0 ){
		 tablerec +="<tr><td colspan='7' style='text-align:center'><b>"+ jsonobj["REASON"] +"</b></td></tr>";
	}else{
		
			 var innerset = jsonobj["SET0"];  
		  var count = jsonobj["RECORDCNT"] ; 
		 
		
		if( count > 0 ){
			tablerec += "<tr><td colspan='8' style='text-align:left'>  No.of Records : " + count + " ...Showing last "+displayrecords+" </td></tr>";
		}
		tablerec += "<tr><th>Card No</th> <th>Txn Description</th> <th>Txn Ref no</th> <th>Txn Date</th>   <th style='text-align:right'>Txn Amount</th>  <th>Merchant name</th> <th>Response</th> <th>Reason</th></tr>";
		<!--  <th>VAS Response</th> --> 
		 /* <th>Trace No</th>  */
		
		for (var i=0; i<count; i++ ){
			var innersetkey = "SET"+i;
			var innerset = jsonobj[innersetkey]; 
			 var CARDNO = innerset['CHN'];
			 var REFNO = innerset['TXNREFNUM'];
			 var TRACENO = innerset['TRACENO']
			 tablerec +="<input  type ='hidden' name='cardno' id='cardno"+i+"' value="+CARDNO+" />";
			 tablerec += "<tr  class='rowrec' id='recordrow_"+i+"'  onmouseover='showSelect(this.id)'  onmouseout='showDeSelect(this.id)'>";
			 tablerec +="<td onclick=\'showComplaints("+i+", "+REFNO.toString()+", "+TRACENO.toString()+")\' >"+CARDNO+"</td>";
			 /* style='color:red;font-weight:bold' onclick=\'showComplaints("+i+", "+REFNO.toString()+", "+TRACENO.toString()+")\' */
			 tablerec += "<td style='text-align:center'>"+innerset['TXNDESC']+"</td>";
			 tablerec += "<td>"+REFNO+"</td>"; 
			 tablerec += "<td>"+innerset['TRANDATE']+"</td>";  
			 tablerec += "<td  style='text-align:right' >"+innerset['TXNAMOUNT']+"</td>";
			 tablerec += "<td>"+innerset['ACCEPTORNAME']+"</td>"; 
			// tablerec += "<td>"+innerset['TXNTYPE']+"</td>";
			// tablerec += "<td>"+innerset['TXNCURRENCY']+"</td>";
			
			 tablerec += "<td>"+innerset['RESPCODE']+"</td>"; 
			 tablerec += "<td>"+innerset['REASONCODE']+"</td>"; 
			/*  tablerec += "<td>"+innerset['ERSVFLD2']+"</td>";  */
			 
			/*  tablerec += "<td>"+TRACENO+"</td>"; */
			
			// tablerec += "<td>"+innerset['TERMLOC']+"</td>";
			 tablerec += "</tr>";
			 
			 if( i==displayrecords-1){
				 break;
			 }
		}  
	} 
	tablerec += "</table>";
	 
	
   document.getElementById("displayresult1").innerHTML = tablerec;
	 
	return false;
		
}

function confirmAction()
{
var statuscode = document.getElementById("statudcode");
//alert(statuscode);
if(statuscode.value=='04'){
if(confirm("Are Sure Want to Close this card , Once Close You cant able to make any changes further?"))
	{
	//alert('goes retur true');
	}
}
}



function checkstatuscode()
{
	var statuscode = document.getElementById("statudcode");
	if(statuscode.value=='10'){
		document.getElementById("changeexpiryDiv").style.display = 'block';
	}else{
		document.getElementById("changeexpiryDiv").style.display = 'none';
	}
}

</script>



<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

 

<sx:tabbedpanel id="test" cssClass="d" cssStyle="width:95%; margin:0 auto;" doLayout="false">

<sx:div id="one" label="Personal Details" labelposition="top" >
	<s:form action="changeStatusCardMaintainAction.do" name="orderform"
	 autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="formtable1" style="text-align:left">
		<tr> 
		<td colspan="2"  style="text-align:center;font-weight:bold;color:maroon"> <div> Personal Details </div> </td>
		</tr>
		<tr>
			<td width="50%" style="text-align:left"  >Card No</td>
			<td>: <s:property value="mcardno" />
			</td>
		</tr>         
		<tr>
			<td width="50%" style="text-align:left"  >Account No</td>
			<td>: <s:property  value="acccountno" />  
			</td>
		</tr>
		<s:hidden name="enccardno" id="enccardno" value="%{enccardno}"/>
		<s:hidden name="cardno" id="cardno" value="%{cardno}" />
		<s:hidden name="hcardno" id="hcardno" value="%{hcardno}" />
		<s:hidden name="mcardno" id="mcardno" value="%{mcardno}" />
		<s:hidden name="accountno" id="accountno" value="%{acccountno}" />
		<s:hidden name="padssenable" id="accountno" value="%{padssenable}" />
		  
		<tr>
			<td>Customer Name</td>
			<td>: <s:property value="customername" />
			</td>
		</tr>
		<tr>
			<td>Customer No ( <% out.println( session.getAttribute("APPLICATIONTYPE")); %> ) </td>
			<td>: 
				<% 
					String apptype = (String)session.getAttribute("APPLICATIONTYPE");
					if( apptype.equals("CREDIT")){
				%>
					<s:property value="custid" />  <a href="" style="color:red" onclick="showCustomerDetailsCredit('<s:property value="custid" />')"> View customer detail </a>
					
				<% 		
					}else{
				%>
				 <s:property value="custid" />  <a href="" style="color:red" onclick="showCustomerDetails('<s:property value="cardno" />'	)"> View customer detail </a>  		
				<% } %>
			</td>
		</tr>
		<tr>
			<td>Order Ref No</td>
			<td>: <s:property value="order_refno" />
			</td>
		</tr>
		<tr>
			<td>Bin</td>
			<td>: <s:property value="bindesc" />
			</td>
		</tr>
		<tr>
			<td>Product</td>
			<td>: <s:property value="product_code" />
			</td>
		</tr>
		<tr>
			<td>Expiry Date</td>
			<td>: <s:property value="expdate" />
			</td>
		</tr>
		<tr>
			<td>Current Status</td>
			<td>: <s:property value="crd_status_desc" />
			</td>
		</tr>

		<s:set name="maintain">${maintainallowed}</s:set>
		
	
 
	<s:if test="%{maintainrequired}">
			<tr>
				<td>Maitenence Activity</td>
				<td>: <select name="statuscode" id="statudcode" onchange="checkstatuscode()">
						<option value="-1">-SELECT-</option>
						<s:iterator value="applicable_act_list">
							<option value="${APPLICABLE_ACTION}">${CODE_DESC}</option>
						</s:iterator>
				</select>
				</td>
			</tr>
			<tr >
			<td></td>
				<td ><div id="changeexpiryDiv" style="display: none;">
					 Do You want To Change Expiry Date <br> <input type='radio' id='changeExpiryDate' value='Y' checked=checked name = 'changeExpiryDate' /> Yes <input type='radio' value='N' id='changeExpiryDate' name = 'changeExpiryDate' /> No
				
				</div></td>
			</tr>
		</s:if> 
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="40%" style="text-align:center">
		<tr>
			<s:if test="%{ maintainallowed }">
				<td><s:submit value="Submit" name="order" id="order" onclick="return confirmAction()"/></td>
			</s:if>
			<td><input type="button" name="cancel" id="cancel" value="Back"
				class="cancelbtn" onclick="history.go(-1); return false;" /></td>
		</tr> 
	<s:else>
			<tr>
				<td colspan="2"><span style='color: red;text-align:center'> No Maintenance Activity allowed for this card </span></td>
			</tr>
		</s:else>    
	<s:else>
		<tr>
				<td colspan="2"><span style='color: red;text-align:center'> MAINTENANCE ACTIVITY NOT REQUIRED FOR THIS CARD </span></td>
			</tr>	
	</s:else>
	</table>
</s:form>
</sx:div>

 <%-- <sx:div id="two" label="Limit Details"  >

     
	<table border="1" cellpadding="0" cellspacing="0" RULES="NONE" width="75%"  style="border-color: gray;">
	<tr> 
		<td   style="text-align:center;font-weight:bold;color:maroon"> <div> Limit Details cscasca</div> </td>
	</tr>
					
	<tr><td>
	  <table border='0' cellpadding='0' cellspacing='0' width="80%" class='formtable'>
	    <tr>
	    	<td align="center"> Limit id : <s:property value="callbean.limitid"/> </td>
	    	<td align="center"> Limit Name : <s:property value="callbean.limitdesc"/> </td>
	    </tr>
	  </table>
	 </td></tr>
	 
	 <tr><td>
	  <table border='0' cellpadding='0' cellspacing='0' class='formtable' width="80%">
	    <tr> <th > Limit Type </th><th> Transaction </th><th> Limit Period </th><th>Limit Amount </th><th> Per Transaction Limit  </th> <th> Limit Count </th>  <th style='color:red'> Today Txn Amount </th>  <th style='color:red'> Today Txn Count </th> </tr>
	    <s:if test='callbean.limitdetails.size() == 0'>
	    	<tr><td colspan="8">No Records Available</td></tr>
	    </s:if>
	    <s:else>
		    <s:iterator value="callbean.limitdetails">
		    		<tr> <td> ${LIMIT_TYPE}  </td><td> ${TXNDESC} </td><td> ${PERIOD} </td><td> ${LIMIT_AMOUNT} </td><td> ${LIMIT_AMOUNT}   </td> <td> ${LIMIT_COUNT}   </td><td tyle='color:red'> ${TODAYTXNAMT}   </td> <td style='color:red'> ${TODAYTXNCNT}   </td>   </tr>
		    </s:iterator>
	    </s:else>
	   </table>
	 </td></tr>
	    
	</table>

</sx:div> --%>


 


</sx:tabbedpanel>

 