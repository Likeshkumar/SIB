<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript">
function validation()
{
	//alert("welcome");
	valid=true;
	var err_msg = document.getElementById("errormsg");
	var txn = document.getElementsByName("txn");
	//alert("Transact==> "+txn.length);
	
	var allcount = 0;
	var merchant = "merchant";
	////alert("limitvl====> "+limitvl);
	var merchant = document.getElementById(merchant);
	
	var scheme = "scheme";
	////alert("limitvl====> "+limitvl);
	var scheme = document.getElementById(scheme);
	
	var lowtxncnt = "lowtxncnt";
	////alert("limitvl====> "+limitvl);
	var lowtxncnt = document.getElementById(lowtxncnt);
	
	var highcnt = "highcnt";
	////alert("limitcnt====> "+limitcnt);
	var highcnt = document.getElementById(highcnt);
	
	var lowamt = "lowamt";
	////alert("txnperiod====> "+txnperiod);
	var lowamt = document.getElementById(lowamt);
	////alert ( "select Box value is==> " +txnperiod.value);
	var highamt = "highamt";
	////alert("pertxnamt====> "+pertxnamt);
	var highamt = document.getElementById(highamt);
	
	var onorset = "onorset";
	////alert("pertxnamt====> "+pertxnamt);
	var onorset = document.getElementById(onorset);	
	
	
	if (merchant.value == "-1" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(lowtxncnt, "Please Select the  Merchant ID");
    	 return false; 
	}
	
	if (scheme.value == "-1" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(scheme, "Please Select the Card Scheme ");
    	 return false; 
	}
	
	if (lowtxncnt.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(lowtxncnt, "Please Enter Low Transaction Count");
    	 return false; 
	}
	if (highcnt.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(highcnt, "Please Enter  High Transaction Count");
    	 return false; 
	}
	
	if (lowamt.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(lowamt, "Please Enter Low Transaction Amount");
    	 return false; 
	}
	if (highamt.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(highamt, "Please Enter High Transaction Amount");
    	 return false; 
	}
	if (onorset.value == "-1" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(onorset, "Please Select  Settlement Type");
    	 return false; 
	}	
	
	for(var i=0;i<txn.length;i++)
	{	
		allcount = allcount + 1;
		//alert("inside 1st for loop"+i);
		var txnid = document.getElementById("txn"+i).value;
		var txndesc = document.getElementById("txndesc"+i).value;
		//alert("Transaction id"+txndesc);			
			var disamt = "disamt"+txnid;
			////alert("pertxnamt====> "+pertxnamt);
			var disamt = document.getElementById(disamt);
			var dismode = "dismode"+txnid;
			////alert("pertxnamt====> "+pertxnamt);
			var dismode = document.getElementById(dismode);
			
			if (disamt.value == "" )
		    {	
		    	 ////alert ( "Please Enter Limit Amount" );
		    	 errMessage(disamt, "Please Enter "+txndesc+" Discount Amount");
		    	 return false; 
			}
			if (dismode.value == "-1" )
		    {	
		    	 ////alert ( "Please Enter Limit Amount" );
		    	 errMessage(dismode, "Please Select "+txndesc+" Discount Mode");
		    	 return false; 
			}			
			
	}
	if(txn.length == allcount)
	{
		////alert("All Cells Checked---> ");
		if ( confirm( 'Do you want to Submit' )){
    		return true;
		}else{
    		return false;
		}
	}
}

</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
	#textcolor
	{
	color: maroon;
	font-size: small;
	}
</style>
</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<table style="border:1px solid #efefef;"  border="0" cellpadding="0" align="center" width="60%" cellspacing="0" class='subaction'>
		<s:iterator value="discountdesc">
				<tr>
					<td>GL Name</td>
					<td>: </td><td  id="textcolor"> ${SCHEMEGL_DESC}</td>
					
					<td>Low Txn Count</td>
					<td>: </td>
					<td id="textcolor">
						 ${LOWTXNCNT}
					</td>
				</tr>		
				<tr>		

				<tr>
					<td>High Txn Count</td>							
					<td >: </td>
					<td id="textcolor">${HIGHTXNCNT}
					</td>	
					
					<td>Low Txn Amount</td>						
					<td>: </td>
					<td id="textcolor"> 
						${LOWTXNAMT}
					</td>
				</tr>

				<tr>
					<td>High Txn Amount</td>									
					<td>: </td>
					<td id="textcolor"> 
						${HIGHTXNAMT}
					</td>
					
					<td> Online or During Settlement</td>									
					<td>: </td>
					<td id="textcolor"> ${COMMISSION_PROCESS}
					</td>
				</tr>
				

				<tr>
					<td>From Date</td>
					<td>: </td>
					<td id="textcolor">
						${FROMDATE}
						
					</td>
					
					<td>To Date</td>
					<td>: </td>
					<td id="textcolor">
						${TODATE}
						
					</td>
				</tr>
	</s:iterator>	
	<table><tr><td></td></tr></table>
</table>
	<table style="border:1px solid #efefef;"  border="1" cellpadding="0" align="center" width="60%" cellspacing="0" class='subaction'>
		<tr>
			<th style="font: bold;font-size:14px; color: maroon;"> Transaction Type</th> 
			<th align="center" style="font: bold;font-size:14px; color: maroon;">Discount Amount</th>
			<th align="center" style="font: bold;font-size:14px; color: maroon;">Discount Mode</th>
		</tr>
		<%int i=0,j=0; %>
		<s:iterator value="actioncode">
		<s:set name="actionlist">${ACTION_CODE}</s:set>
			<input type='hidden' name='txn' id='txn<%=i++%>' value='${ACTION_CODE}'/>
			<input type='hidden' name='txndesc' id='txndesc<%=j++%>' value='${ACTION_DESC}' />
				<tr>
					<td id="textcolor" align="center">${ACTION_DESC}</td>
					
					<td id="textcolor" align="center">
							<s:iterator value="discount">
							<s:set name="discountlist">${TXN_CODE}</s:set>
								<s:if test="%{#actionlist==#discountlist}">
								${DISCOUNT_AMT}
								</s:if> 
							</s:iterator>											
					</td>
					<td id="textcolor" align="center">
							<s:iterator value="discount">
							<s:set name="discountlist">${TXN_CODE}</s:set>
								<s:if test="%{#actionlist==#discountlist}">
								${DISCOUNT_MODE}
								</s:if> 
							</s:iterator>											
					</td>
				</tr>
		</s:iterator>
	</table>
</body>
</html>