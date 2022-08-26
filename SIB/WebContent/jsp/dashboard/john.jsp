<%@taglib uri="/struts-tags" prefix="s"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>Dash Boards </title>
<link rel="stylesheet" href="style/dashboardcss/jquery-ui.css" />
<script src="js/jquery/jquery_1.9.1.js"></script>
<script src="js/dashboardjs/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css" />
<style>
 
.column { width: 400px; float: left; padding-bottom: 100px; }
.portlet { margin: 0 1em 1em 0; }
.portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em; }
.portlet-header .ui-icon { float: right; }
.portlet-content { padding: 0.4em;  }
.ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
.ui-sortable-placeholder * { visibility: hidden; }
table.formtable th { background:none; border 1px solid gray;}
</style>
<script>
$(function() {
$( ".column" ).sortable({
connectWith: ".column"
});
$( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
.find( ".portlet-header" )
.addClass( "ui-widget-header ui-corner-all" )
.prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
.end()
.find( ".portlet-content" );
$( ".portlet-header .ui-icon" ).click(function() {
$( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
});
$( ".column" ).disableSelection();
});
</script>
</head>
<body>
<table border='0' cellpadding='0' cellspacing='0' width=100%  style="margin:0 auto">
<tr><td>
<div class="column">
<div class="portlet">
<div class="portlet-header"> Top <s:property value="procedurereccount"/> Merchants By Sales Amount </div>
<div class="portlet-content"> 
<%-- 	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<tr> <th>Merchant Name</th> <th>Sale Amount</th>   </tr>
		<s:iterator value="merchsalebyamtlist">
		<tr> <td>${MERCH_NAME}</td> <td>${TXNAMOUNT}</td>   </tr>
		</s:iterator>
	</table>  --%> 
	
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<tr> <th>Merchant Name</th> <th>Sale Amount</th>   </tr> 
		<s:iterator value="dashbean.maxsalemerchantlist">
		<tr> <td>${MERCH_NAME}</td> <td>${TOTALAMT} ${SETTLECUR} </td>   </tr>
		</s:iterator>
	</table> 
	
	
</div>
</div>

<div class="portlet">
<div class="portlet-header"> Top <s:property value="procedurereccount"/> Merchants By Sales Count </div>
<div class="portlet-content">
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<tr> <th>Merchant Name</th> <th>Count</th>   </tr>
		<s:iterator value="dashbean.maxsalemerchantlist">
		<tr> <td>${MERCH_NAME}</td> <td>${TXNCNT}</td>   </tr>
		</s:iterator>
	</table>  
</div>
</div>


<div class="portlet">
<div class="portlet-header"> Cash Out Overview </div>
<div class="portlet-content">
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<tr> <th>Merchant Name</th> <th>Deposited Amount</th>   </tr>
		<s:iterator value="dashbean.cashinoutlist">
		<tr> <td>${MERCHANT_DESC}</td> <td>${VAS_AMOUNT}</td>   </tr>
		</s:iterator>
	</table>  
</div>
</div>


<div class="portlet">
<div class="portlet-header"> No.of Compliants </div>
<div class="portlet-content">
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<s:if test='dashbean.compliantcount== 0' >
			<tr> <td> No compliants </td>   </tr>  
		</s:if>
		<s:else>
			<tr> <td>   <s:property value="dashbean.compliantcount"/> compliants waiting for authorization </td>   </tr>   
		</s:else>
		
		
	</table>  
</div>
</div>



</div>


<div class="column">
<div class="portlet">
	<div class="portlet-header"> Merchant Monitoring</div>
	<div class="portlet-content">
		<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
			<tr> <th>Active Merchant Count </th> <td> 04 </td>   </tr>
			 <tr> <th>Active Inactive Count </th> <td> 15 </td>   </tr>
			  <tr> <th>Warning Count </th> <td> 11 </td>   </tr>
		</table>  
	
	</div>
</div>


<div class="portlet">
<div class="portlet-header"> Black Listed Merchants </div>
<div class="portlet-content">
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<s:if test='dashbean.blacklistedmerchantcount== "0" ' >
			<tr> <td> No black listed merchant </td>   </tr>  
		</s:if>
		<s:else>
			<tr> <td>   <s:property value="dashbean.blacklistedmerchantcount"/> Black Listed Merchant Waiting for authorization </td>   </tr>  
		</s:else>
	</table>  
</div>
</div>



<div class="portlet">
	<div class="portlet-header"> Last Transaction </div>
	<div class="portlet-content">
		
		<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable">  
		<tr> <th>Last Txn </th>  <th> Merchant Name </th> <th> Txn Amount</th>   </tr> 
			<s:iterator value="dashbean.lasttransactionlist">
				<tr> <td>Last ${TXNDESC}</td>
				 	<s:iterator value="%{ATTR}">
				 		 <td> ${MERCHANT_DESC}</td> <td>${TXNAMOUNT}</td>				 		 
				 	</s:iterator> 
				   </tr>
			</s:iterator> 
		</table>  
	
	</div>
</div>


</div>
<div class="column"> 

<div class="column">
<div class="portlet">
<div class="portlet-header"> Card Aactivated Count Merchant Wise</div>
<div class="portlet-content">
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<tr> <th>Merchant Name</th> <th>Sale Amount</th>   </tr>
		<s:iterator value="merchsalebyamtlist">
		<tr> <td>${TNAME}</td> <td>${TABTYPE}</td>   </tr>
		</s:iterator>
	</table>  

</div>
</div>

<div class="portlet">
<div class="portlet-header"> No.of Transaction Today </div>
<div class="portlet-content">
	<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"> 
		<tr> <th>Transaction</th>    <th>Amount</th>    </tr>
		<s:iterator value="dashbean.txngrptransactionlist">
		<tr> <td>${TXNDESC}</td> <td>${TXNAMOUNT}</td>   </tr>
		</s:iterator>
	</table>  
</div>

</div>
</div>
</tr>
</table>
</body>
</html>