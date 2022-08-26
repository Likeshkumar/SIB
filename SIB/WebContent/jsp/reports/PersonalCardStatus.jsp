<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function coppyEmbosstoEncode(event,value){
	//alert("Event "+event+"  value===>"+value);
	var s = alphabets(event);
	//alert(" ssss "+s);
	if(s==true)
		{
			//alert("Value is Aplpha"+value);
			document.getElementById("encode").value = value;
			return true;
		}else{
			//alert("Invalid");
			return false;
		}
}
function validateValues()
{
	//alert("Validate Value Function");
	var branchcode 	= document.personalorderform.branchcode;
	var cardstatus 	= document.personalorderform.cardstatus;
	var cardtyp		= document.personalorderform.cardtype;
	var subproductlist= document.personalorderform.subproductlist;
	if(branchcode.value == "-1")
	{
		errMessage(branchcode,"The Please Select The Branch ");
		return false;
	}
	if(cardstatus.value == "-1")
	{
		errMessage(cardstatus,"The Please Select The Report Type ");
		return false;
	}
	if(cardtyp.value == "-1")
	{
		errMessage(cardtyp,"The Please Select The Card Type ");
		return false;
	}
	if(subproductlist.value == "-1")
	{
		errMessage(subproductlist,"The Please Select The Product");
		return false;
	}	
	
	var appdate = document.getElementById("fromdate");
	var appnum = document.getElementById("todate");
	if(appdate.value == "")
	{
	errMessage(appdate,"Please Select The From Date");
	return false;
	}
	if(appnum.value == "")
	{
	errMessage(appnum,"Please Enter the To Date");
	return false;
	}
	return true;
}
	
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="genCardReportStatusReportgenerationAction"  name="personalorderform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount" class="formtable" >
	<% 
	String usertype=(String)session.getAttribute("USERTYPE");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ"); 
		if(usertype.equals("INSTADMIN"))
		{
	%>
		<tr>
				<td class="fnt"> Select Branch   </td>
				<td> : <s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1" tooltip="Select Branch"/>
				
				</td>
			</tr>
			
	<%	
		}
		else
		{
	%>
			<tr>
			<td class="fnt"> Branch</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />				
			</tr>
	<%		
		}
	%>
	<tr>
	 <tr> 
    	<td>
    		Select Report Type
    	</td>
    	<td>    		
    			:  <select name="cardstatus" id="cardstatus">
    				<option value="-1" selected="selected">--Select Report Type--</option>
    				<option value="01">CARD GENERATED REPORT</option>
    				<option value="02">PIN GENERATED REPORT</option>
    				<option value="03">PRE GENERATED REPORT</option>
    				<option value="04">RECEIVED CARD REPORT</option>
    				<option value="05">ISSUED REPORT</option>
    			</select>    		
    	</td>
    </tr> 
		<td class="fnt">
		Select Product
		</td>
		<td>
 				: <select name="cardtype" id="cardtype" onchange="return Gettingsubproductlist('Y');" >
	 				<option value="-1" selected="selected">--Select Product--</option>
	 				<option  value="000" >ALL</option>
	 				<s:iterator  value="prodlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
   <tr> 
    	<td id="subname" class="fnt">
    		Select Sub-Product
    	</td>
    	<td class="fnt">
    		<div id="ajax"> :
    			<select name="subproductlist" id="subproductlist">
    				<option value="-1">--Select Product--</option>
    			</select>
    		</div>
    	</td>
    </tr>    
    <tr> 
    	<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.personalorderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
    </tr>	
    	
    <tr>	
		<td> 
		To Date
		</td>
		<td> : 
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.personalorderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> 


 <!-- 		<tr>
		    <td align="left">Application Date<b class="mand">*</b></td>
			<td>
				<input type="text" name="appdate" id="appdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:180px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.personalorderform.appdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
	   <tr>
		    <td align="left">Application No<b class="mand">*</b></td>
			<td><input type="text" name="appno" id="appno" maxlength="30" onKeyPress=" return alphanumerals(event);"></td>
	</tr>
	
	 -->
	
</table>
<!-- This " custno " hidden field only for validation,Same name use in KYC order also,this value should be 1  -->
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>			
			<s:submit value="Next" name="next_process" id="next_process" onclick="return validateValues();"/>
		</td>
		<td>
			<!-- <input type="button" name="cancle" value="Cancel" onclick="return forwardtoCardOrderPage();"> -->
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>

