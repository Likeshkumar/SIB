<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript">

function getBasedonLimit(limitbasedvalue)
{
	if(limitbasedvalue=="CDTP")
		{
		document.getElementById("cardtypedetailsdiv").style.display ="table-row";
		document.getElementById("accttypedetailsdiv").style.display ="none";
		document.getElementById("cardnoDiv").style.display ="none";
		document.getElementById("accountnoDiv").style.display ="none";
		document.getElementById("FeeNameDiv").style.display ="table-row";
		document.getElementById("fromdateDiv").style.display ="none";
		document.getElementById("todateDiv").style.display ="none";
		
		}
	if(limitbasedvalue=="ACTP")
	{
		document.getElementById("cardtypedetailsdiv").style.display ="none";
		document.getElementById("accttypedetailsdiv").style.display ="table-row";
		document.getElementById("cardnoDiv").style.display ="none";
		document.getElementById("accountnoDiv").style.display ="none";
		document.getElementById("FeeNameDiv").style.display ="table-row";
		document.getElementById("fromdateDiv").style.display ="none";
		document.getElementById("todateDiv").style.display ="none";
	}
	
	if(limitbasedvalue=="CARD")
	{
	document.getElementById("cardtypedetailsdiv").style.display ="none";
	document.getElementById("accttypedetailsdiv").style.display ="none";
	document.getElementById("accountnoDiv").style.display ="none";
	document.getElementById("cardnoDiv").style.display ="table-row";
	document.getElementById("FeeNameDiv").style.display ="table-row";
	document.getElementById("fromdateDiv").style.display ="table-row";
	document.getElementById("todateDiv").style.display ="table-row";
	
	}
	if(limitbasedvalue=="ACCT")
	{
	document.getElementById("cardtypedetailsdiv").style.display ="none";
	document.getElementById("accttypedetailsdiv").style.display ="none";
	document.getElementById("cardnoDiv").style.display ="none";
	document.getElementById("accountnoDiv").style.display ="table-row";
	document.getElementById("FeeNameDiv").style.display ="table-row";
	document.getElementById("fromdateDiv").style.display ="table-row";
	document.getElementById("todateDiv").style.display ="table-row";
	}
	
	
	
	
}





function delConfirm()
{
var r=confirm("Are You Sure 'Delete'");
return r;
	
}
function selectall()
{
	valid=true;
	var selectbox = document.getElementById("limitid");
	if(selectbox.value=="-1")
	{
		errMessage(limitid,"SELECT LIMIT DESC");
		return false;
	}
	return valid;
}

function getListOfCurrency( limitid ){
	//alert( limitid );
	var url = "listofLimitCurrencyAddLimitAction.do?doaction=EDIT&limitid="+limitid;
	//alert( url );
	var result = AjaxReturnValue(url);
	document.getElementById("curcode").innerHTML=result;
	return false;
}

 

 function getListOfLimitType( curcode ){
		 
		var limitid = document.getElementById("limitid");
		 
		var url = "listofLimitTypeAddLimitAction.do?doaction=EDIT&limitid="+limitid.value+"&curcode="+curcode;
		 
		var result = AjaxReturnValue(url);
		document.getElementById("limittype").innerHTML=result;
		return false;
	 
	}   
  

 function getListOfPeriod( limittype ){
	 
		var limitid = document.getElementById("limitid");
		var curcode = document.getElementById("curcode");
		var url = "listofLimitPeriodsAddLimitAction.do?doaction=EDIT&limitid="+limitid.value+"&curcode="+curcode.value+"&limittype="+limittype;
		 
		var result = AjaxReturnValue(url);
		//alert( result )
		document.getElementById("period").innerHTML=result;
		return false;
	 
	}    
 
</script>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>

<form action="editLimitdetailsAddLimitAction.do" autocomplete="off" method="post">
	<div align="center">
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="50%" class="formtable">
			<!-- <tr>
					<td align="right">LIMIT: </td>
					<td>
						<select name="limitid" id="limitid"  onchange="getListOfCurrency(this.value);">
									<option value="-1">-SELECT LIMIT -</option>
											<s:iterator value="limitdesclist">
										<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
											</s:iterator>
						</select>
					</td>
				</tr> -->	
				<tr>
							<td align="right">
								LIMIT:
							</td>
							<td>
								<select id="limit_id" name="limit_id" >
									<option value="-1">-SELECT LIMIT-</option>
									<s:iterator  value="masterfeelist" >
									<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
									</s:iterator>
								</select>
							</td>
					    </tr>  
				<!--  <tr>
				<td align="right">
					Select Limit Type
				</td>
				
				<td>
					<select name="limittype" id="limittype" onchange="getBasedonLimit(this.value);">
					<option value="-1">-SELECT LIMIT TYPE-</option>
					<s:iterator  value="glLimitType" >
					<option value="${LIMIT_TYPE}">${LIMIT_DESC}</option>
					</s:iterator>
					</select>     
					
					</td>				
				    
				
				
			
			</tr>
			
			<tr style="display:none;" id="FeeExistdiv">
					<td align="right">SELECT LIMIT</td>
				   
					<td> 
						<select name="limitcode" id="limitcode">
							<option value="-1">-SELECT LIMIT-</option>
								<s:iterator  value="masterlimitlist" >
							<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
								</s:iterator>
						</select>
					</td>
			</tr>
			
			<tr id="cardtypedetailsdiv" style="display:none">
					<td align="right">SELECT CARDTYPE</td>
				    
					<td> 
						<select name="cardtypedetails" id="cardtypedetails">
							<option value="-1">-SELECT CARDTYPE-</option>
								<s:iterator  value="cardtypedetails" >
							<option value="${CARD_TYPE_ID}">${CARD_TYPE_DESC}</option>
								</s:iterator>
						</select>
					</td>
			</tr>
			
			<tr id="accttypedetailsdiv" style="display:none">
					<td align="right">SELECT ACCT TYPE</td>
				    
					<td> 
						<select name="acctypedetails" id="acctypedetails">
							<option value="-1">-SELECT ACCTTYPE-</option>
								<s:iterator  value="acctypedetails" >
							<option value="${ACCTTYPEID}">${ACCTTYPEDESC}</option>
								</s:iterator>
						</select>
					</td>
			</tr>
			
						
			<tr style="display:none;" id="FeeNameDiv">
					<td align="right">Enter Limit Name</td>
				    
					<td>
						<input type="text" name="limitname" id="limitname" value="" maxlength="22" onkeyup="chkChars('Limit Name',this.id,this.value)"/>						
					</td>
			</tr>
			
			<tr style="display:none;" id="cardnoDiv">
					<td align="right">Enter Card Number</td>
				    
					<td>
						<input type="text" name="cardnumber" id="cardnumber" value="" maxlength="19" onkeyup="validateNumber('Card No ',this.id,this.value)"/>						
					</td>
			</tr>
			<tr style="display:none;" id="accountnoDiv">
					<td align="right">Enter Account No</td>
				    
					<td>
						<input type="text" name="accountnumber" id="accountnumber" value="" maxlength="16" onkeyup="validateNumber('Account No ',this.id,this.value)"/>						
					</td>
			</tr>
			
			 <tr style="display:none;" id="fromdateDiv">
	   <td align="left" class="fnt">From Date<font class="mand">*</font>
		
		<td>
		<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.addLimitView.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
	<tr style="display:none;" id="todateDiv">
	    <td align="left" class="fnt">To Date <font class="mand">*</font>
		
		<td>
		<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.addLimitView.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
			
			
				<tr>
							<td align="right">
								CURRENCY :
							</td>
							<td>
								<select id="curcode" name="curcode" >
									<option value="-1">-SELECT CURRENCY-</option> 
								</select>
							</td>
					    </tr> -->
					    
			</table>
			<table  border="0"  cellspacing="0" cellpadding="0" width="90%">
				<tr align="center">
					<td>
						<%-- <s:submit value="View"  onclick="return selectall()"></s:submit> --%>
						<s:submit value="View" ></s:submit>
					</td>
				</tr>
			</table>
	</div>
</form>
</body>
</html>