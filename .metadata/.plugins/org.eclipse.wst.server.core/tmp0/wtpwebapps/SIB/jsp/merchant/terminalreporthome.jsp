<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>

<script type="text/javascript">
	function getStoreslist()
	{
		var merchantid=(document.getElementById('merchantid'));
		
		if(merchantid.value == "-1")
		{
			errMessage( merchantid, "Please Select Merchant " );
			return false;
		}
		else
		{
			var url="merchStorelistMerchantSettlementReportsAction.do?merchantid="+merchantid.value+"&terminalreq='T'";
			var response=AjaxReturnValue(url);
			document.getElementById('ajax_stores').innerHTML = response;
			document.getElementById('storeslect').innerHTML = "Select Store";
			return true;
		}
	}
	function getTerminals()
	{
		var merchantid=(document.getElementById('merchantid'));
		var storeid=(document.getElementById('storeid'));
		if(merchantid.value == "-1")
		{
			errMessage( merchantid, "Please Select Merchant " );
			return false;
		}
		else if(storeid.value == "-1")
		{
			errMessage( storeid, "Please Select Store " );
			return false;
		}
		else
		{
			var url="merchTerminallistMerchantSettlementReportsAction.do?merchantid="+merchantid.value+"&store_id="+storeid.value;
			var response=AjaxReturnValue(url);
			document.getElementById('ajax_terminal').innerHTML = response;
			document.getElementById('terminalselect').innerHTML = "Select Store";
			return true;			
		}
		
		
	}
	
	function terminalreportValidation()
	{
		var merchantid=(document.getElementById('merchantid'));
		var storeid=(document.getElementById('storeid'));
		var terminalid=(document.getElementById('terminalid'));
		if(merchantid.value == "-1")
		{
			errMessage( merchantid, "Please Select Merchant" );
			return false;
		}
		if(storeid.value == "-1")
		{
			errMessage( storeid, "Please Select Store" );
			return false;
		}
		if(terminalid.value == "-1")
		{
			errMessage( terminalid, "Please Select Terminal" );
			return false;
		}		
		return true;
	}
	
	
</script>
 
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="terminalreporthome" action="generateTerminalbaseReportMerchantSettlementReportsAction" namespace="/" autocomplete="off">

<div align="center" >
	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
		<tr>
			<td>
				Select Merchant
			</td>
			<td> :
				<s:select	name="merchantid"
							id="merchantid"
							list="%{merchantidlist}" 
							listKey="MERCH_ID"  listValue="MERCH_NAME" 
							headerKey="-1" 
							headerValue="-- Select Merchant --"
							onchange="return getStoreslist();"
							>
				</s:select>
			</td>
		</tr>

	   <tr> 
	    	<td id="storeslect" class="fnt">
	    		Select Store
	    	</td>
	    	<td class="fnt">  
	    		<div id="ajax_stores">
	    			 <select name="storeid" id="storeid">
	    				<option value="-1">--Select Store--</option>
	    			</select>
	    		</div>
	    	</td>
	    </tr>

	   <tr> 
	    	<td id="terminalselect" class="fnt">
	    		Select Terminal
	    	</td> 
	    	<td class="fnt">  
	    		<div id="ajax_terminal">
	    			 <select name="terminalid" id="terminalid">
	    				<option value="-1">--Select Terminal--</option>
	    			</select>
	    		</div>
	    	</td>
	    </tr>	
	   
	   <tr>
			<td>From Date   </td>
			 
			<td> : <input type="text" id="fromdate" name="fromdate" />
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.terminalreporthome.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
	  </tr>
			
    <tr>
		<td>To Date  </td>
		 
		<td> : <input type="text" id="todate" name="todate" />
			<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.terminalreporthome.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
</tr>

	</table>

 	<table border="0" cellpadding="0" cellspacing="0" width="40%">
 		<tr align="center">
 			<td>
				<s:submit value="Submit" name="storreport" id="storreport" onclick="return terminalreportValidation();"/>
				<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
 			</td>
 		</tr>
 	</table>


</div>


</s:form>