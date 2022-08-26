<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>




function getSubProductList(prodid){
	var instid = document.getElementById("instid").value;	 
	var url = "getSubProductListReportgenerationAction.do?instid="+instid+"&prodid="+prodid.value; 
	var result = AjaxReturnValue(url); 
	document.getElementById("subprodid").innerHTML = result;
	
}

function Gettingproductlist(binvalue){
	//alert(binvalue);
	var instid = document.getElementById("instid").value;
	//alert(instid);
	var url = "getProductListbybinReportgenerationAction.do?binvalue="+binvalue.value+"&instid="+instid;
	//alert(url);
	var result = AjaxReturnValue(url); 
	//alert(result);
	document.getElementById("prodid").innerHTML = result;
	
}


function validateValues()
{
	
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	var bin = document.getElementById("bin");
	var subprodid = document.getElementById("subprodid");
	var prodid = document.getElementById("prodid");

	if(bin.value=="-1"){
		errMessage(bin,"Please Select the Bin Number");
		return false;
	}	
	if(prodid.value=="-1"){
		errMessage(prodid,"Please Select the product");
		return false;
	}
	if(subprodid.value=="-1"){
		errMessage(subprodid,"Please Select the Subproduct");
		return false;
	}
	if(fromdate.value == "")
	{
		errMessage(fromdate,"Please Select The From Date");
		return false;
	}
	if(todate.value == "")
	{
		errMessage(todate,"Please Enter the To Date");
		return false;
	}
	
	return true;
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="downloadCardProcessReportReportgenerationAction"  name="transanalysesform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount">
<input type='hidden' name='instid' id='instid' value='<%=(String)session.getAttribute("Instname")%>'/>
<tr>
	<td class="fnt">
		Select Bin
		</td>
		<td>
 				: <select name="bin" id="bin" onchange="return Gettingproductlist(this);" >
	 				<option value="-1" selected="selected">--Select Bin--</option>
	 				<option  value="ALL" >ALL</option>
	 				<s:iterator  value="binlist">
	 					<option value="${BIN}">${BIN_DESC}</option>
	 				</s:iterator>
 				</select>
		</td>
</tr>
<tr>
	<td >
		Select Product
		</td>
		<td>
 				: <select name="prodid" id="prodid" onchange="return getSubProductList(this);" >
 				</select>
		</td>
</tr>
<tr>
	<td>
		Select Sub Product
		</td>
		<td>
 				: <select name="subprodid" id="subprodid" >
 				</select>
		</td>
</tr>

    <tr>
    	<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transanalysesform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
    </tr>	
    	
    <tr>	
		<td> 
		To Date
		</td>
		<td> : 
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.transanalysesform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> 
	<tr>	
		<td> 
		Card Type
		</td>
		<td> : 
			<select name="reporttype" id="reporttype" >
				<option value="ALL" selected>ALL</option>
				<option value="50">Activated</option>
				<option value="16"> Not-Activated</option>
			</select>
		</td>
	</tr> 	 
</table>
<table border="0" cellpadding="0" cellspacing="4" width="40%" >
	<tr>
	
		<td>	
			<s:submit value="PDF" name="submit" id="next_process" onclick="return validateValues();"/>
		</td>
		<td>	
			<s:submit value="Excel" name="submit" id="xl_process" onclick="return validateValues();"/>
		</td>
		<td>
			<!-- <input type="button" name="cancle" value="Cancel" onclick="return forwardtoCardOrderPage();"> -->
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	
	</tr>
</table>
</s:form>

 