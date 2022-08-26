<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/script.js " ></script>

	<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>

<script>


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
			//alert("limitbasedvalues:: "+limitbasedvalue); //newly added
		document.getElementById("cardtypedetailsdiv").style.display ="none";
		document.getElementById("accttypedetailsdiv").style.display ="none";
		document.getElementById("accountnoDiv").style.display ="none";
		document.getElementById("cardnoDiv").style.display ="table-row";
		document.getElementById("FeeNameDiv").style.display ="table-row";
		document.getElementById("fromdateDiv").style.display ="table-row";
		document.getElementById("todateDiv").style.display ="table-row";
		//var limittype = document.getElementById("limittype");
		//alert(limittype.value);
		
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
	
	function confEdit(){
		var valid = false;
		if ( confirm('Do you want to Edit ? ') ){
			valid = true;	
		}
		 
		return valid;		
	}
	function confDel(){
		var valid = false;
		if ( confirm('Do you want to Delete ? ') ){
			valid = true;	
		}
		return valid;	
	}
	function selectall()
	{
		valid=true;
		var selectbox = document.getElementById("feecode");
		if(selectbox.value=="-1")
		{
			errMessage(feecode,"SELECT LIMIT");
			return false;
		}
		return valid;
	}
	
	function loaddiv(selctid)
	{	
		
		var checkdiv = document.getElementById(selctid);
		 
		if(checkdiv.value!="-1"){
			var NameDiv = document.getElementById("FeeNameDiv");
			var Existdiv = document.getElementById("FeeExistdiv");
			var cardnoDiv= document.getElementById("cardnoDiv");
			if(checkdiv.value=="new")
			{
				NameDiv.style.display = 'table-row';
				Existdiv.style.display = 'none';
				cardnoDiv.style.display = 'none';
			}
			else if ( checkdiv.value=="ind" ){
				cardnoDiv.style.display = 'table-row';
				Existdiv.style.display = 'none';
				NameDiv.style.display = 'none';
			}
			else
			{
				Existdiv.style.display = 'table-row';
				NameDiv.style.display = 'none';
				cardnoDiv.style.display = 'none';
			}
		}
		
	}
	
	
	
	
	function validation()
	{
		var limittype = document.getElementById("limittype");
		
		//alert(limittype.value);
		
		if(limittype.value=="-1")
		{
			errMessage(limittype,"Select Limit Type");
			return false;
		}
		if(limittype.value=="CDTP")
		{
			var cardtypedetails = document.getElementById("cardtypedetails");
			if( cardtypedetails ){ if( cardtypedetails.value == ""  || cardtypedetails.value == "-1") { errMessage(cardtypedetails, "Select Card Type !");return false; } }
			var limitname = document.getElementById("limitname");
			if( limitname ){ if( limitname.value == ""  || limitname.value == "-1") { errMessage(limitname, "Enter Limit Name !");return false; } }
			var limitcurrncy = document.getElementById("limitcurrncy");
			if( limitcurrncy ){ if( limitcurrncy.value == ""  || limitcurrncy.value == "-1") { errMessage(limitcurrncy, "Select Limit Currency !");return false; } }
		}	
		
		if(limittype.value=="ACTP")
		{
			var acctypedetails = document.getElementById("acctypedetails");
			if( acctypedetails ){ if( acctypedetails.value == ""  || acctypedetails.value == "-1") { errMessage(acctypedetails, "Select Account Type !");return false; } }
			var limitname = document.getElementById("limitname");
			if( limitname ){ if( limitname.value == ""  || limitname.value == "-1") { errMessage(limitname, "Enter Limit Name !");return false; } }
			var limitcurrncy = document.getElementById("limitcurrncy");
			if( limitcurrncy ){ if( limitcurrncy.value == ""  || limitcurrncy.value == "-1") { errMessage(limitcurrncy, "Select Limit Currency !");return false; } }
		}	
			
		if(limittype.value=="CARD")  
		{
			var limitname = document.getElementById("limitname");
			//alert(limitname.value);
			if( limitname ){ if( limitname.value == ""  || limitname.value == "-1") { errMessage(limitname, "Enter Limit Name !");return false; } }
			var cardnumber = document.getElementById("cardnumber");
			if( cardnumber ){ if( cardnumber.value == ""  || cardnumber.value == "-1") { errMessage(cardnumber, "Enter Card No !");return false; } }
			var fromdate = document.getElementById("fromdate");
			if( fromdate ){ if( fromdate.value == ""  || fromdate.value == "-1") { errMessage(fromdate, "Select From Date !");return false; } }
			var todate = document.getElementById("todate");
			if( todate ){ if( todate.value == ""  || todate.value == "-1") { errMessage(todate, "Select To Date !");return false; } }
			
			var url = "checkCardNoExistAddLimitAction.do?cardno="+cardnumber.value;
			var result = AjaxReturnValue(url);
			alert(result);
			if(result!='EXIST')
				{
				errMessage(cardnumber, "Enter Valid Card No. !");
				return false; 
				
				}
			
			var limitcurrncy = document.getElementById("limitcurrncy");
			if( limitcurrncy ){ if( limitcurrncy.value == ""  || limitcurrncy.value == "-1") { errMessage(limitcurrncy, "Select Limit Currency !");return false; } }
		}
		if(limittype.value=="ACCT")
		{
			var limitname = document.getElementById("limitname");
			if( limitname ){ if( limitname.value == ""  || limitname.value == "-1") { errMessage(limitname, "Enter Limit Name !");return false; } }
			var accountnumber = document.getElementById("accountnumber");
			if( accountnumber ){ if( accountnumber.value == ""  || accountnumber.value == "-1") { errMessage(accountnumber, "Enter Account Number !");return false; } }
			
			var fromdate = document.getElementById("fromdate");
			if( fromdate ){ if( fromdate.value == ""  || fromdate.value == "-1") { errMessage(fromdate, "Select From Date !");return false; } }
			var todate = document.getElementById("todate");
			if( todate ){ if( todate.value == ""  || todate.value == "-1") { errMessage(todate, "Select To Date !");return false; } }
			
			
			var url = "checkAccountNoExistAddLimitAction.do?accountnumber="+accountnumber.value;
			var result = AjaxReturnValue(url);
			//alert(result);
			if(result!='EXIST')
				{
				errMessage(accountnumber, "Entered Account no Not Activated ...Enter Valid Account No. !");
				return false; 
				
				}
			var limitcurrncy = document.getElementById("limitcurrncy");
			if( limitcurrncy ){ if( limitcurrncy.value == ""  || limitcurrncy.value == "-1") { errMessage(limitcurrncy, "Select Limit Currency !");return false; } }
			
		}
			
			
			
		parent.showprocessing();
	}
	
	function chkChars(field,id,enteredchar)
	{
		//alert('1:'+field+'2:'+id+'3:'+enteredchar);
		charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
		//str = document.getElementById(id).value;
	    for (var i = 0; i < document.getElementById(id).value.length; i++) {
	       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
	    	//alert(document.getElementById(id).value.charAt(i));   
	    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
	    	document.getElementById(id).value = '';
	    	return false;
	    	}
	    }
	}
	function validateNumber (field,id,enteredchar)
	{
		charvalue= "0123456789";
		//str = document.getElementById(id).value;
	    for (var i = 0; i < document.getElementById(id).value.length; i++) {
	    	
	       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
	       	//alert(document.getElementById(id).value.charAt(i));
	       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
	       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
	    	document.getElementById(id).value = '';
	    	return false;
	    	}
	    }
	}
</script>
<%String act = (String)session.getAttribute("act"); %>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="addLimitViewAddLimitAction.do" name="addLimitView"  onsubmit="">
	<input type="hidden" name="act"  id="act" value="<%=act%>"> 
		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
			<tr>
				<td align="right">
					Select Limit Type
				</td>
				<td align="center">
					:					
				</td>
				 <td>  <s:select name="limittype" id="limittype" onchange="getBasedonLimit(this.value);" listKey="LIMIT_TYPE" listValue="LIMIT_DESC" list="glLimitType"  headerValue="-SELECT LIMIT TYPE-" headerKey="-1" />
				 <s:fielderror fieldName="limittype" cssClass="errmsg" />
				 </td> 
				<%-- <td>
					<select name="limittype" id="limittype" onchange="getBasedonLimit(this.value);">
					<option value="-1">-SELECT LIMIT TYPE-</option>
					<s:iterator  value="glLimitType" >
					<option value="${LIMIT_TYPE}">${LIMIT_DESC}</option>
					</s:iterator>
					</select>     
					<s:fielderror fieldName="limittype" cssClass="errmsg" />
					<td>				
				    
				
				</td>
			
			 --%>
			</tr>
			
			
			<tr style="display:none;" id="FeeNameDiv">
					<td align="right">Enter Limit Name</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="limitname" id="limitname" value="" maxlength="22" onkeyup="chkChars('Limit Name',this.id,this.value)" value="%{Limitname}"/>						
						<fielderror fieldName="limitname" cssClass="errmsg" />
					</td>
			</tr>
			 
			<tr style="display:none;" id="FeeExistdiv">
				
				
					<td align="right">SELECT LIMIT</td>
				    <td align="center">:</td>
				<%-- <td>  <s:select name="limitcode" id="limitcode" listKey="LIMIT_ID" listValue="LIMIT_DESC" list="masterlimitlist"  headerValue="-SELECT LIMIT-" headerKey="-1" value ="%{limitype}"/></td> --%>
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
				    <td align="center">:</td>
					<td> 
						<select name="cardtypedetails" id="cardtypedetails">
							<option value="-1">-SELECT CARDTYPE-</option>
								<s:iterator  value="cardtypedetails" >
							<option value="${CARD_TYPE_ID}">${CARD_TYPE_DESC}</option>
								</s:iterator>
						</select>
						<s:fielderror fieldName="limitname" cssClass="errmsg" />
					</td>
			</tr>
			
			<tr id="accttypedetailsdiv" style="display:none">
					<td align="right">SELECT ACCT TYPE</td>
				    <td align="center">:</td>
					<td> 
						<select name="acctypedetails" id="acctypedetails">
							<option value="-1">-SELECT ACCTTYPE-</option>
								<s:iterator  value="acctypedetails" >
							<option value="${ACCTTYPEID}">${ACCTTYPEDESC}</option>
								</s:iterator>
						</select>
					</td>
			</tr>
			
						
			<!-- <tr style="display:none;" id="FeeNameDiv">
					<td align="right">Enter Limit Name</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="limitname" id="limitname" value="" maxlength="22" onkeyup="chkChars('Limit Name',this.id,this.value)"/>						
						<fielderror fieldName="limitname" cssClass="errmsg" />
					</td>
			</tr> -->
			
			<tr style="display:none;" id="cardnoDiv">
					<td align="right">Enter Card Number</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="cardnumber" id="cardnumber" value="" maxlength="19" onkeyup="validateNumber('Card No ',this.id,this.value)"/>						
					</td>
			</tr>
			<tr style="display:none;" id="accountnoDiv">
					<td align="right">Enter Account No</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="accountnumber" id="accountnumber" value="" maxlength="16" onkeyup="validateNumber('Account No ',this.id,this.value)"/>						
					</td>
			</tr>
			
			 <tr style="display:none;" id="fromdateDiv">
	   <td align="left" class="fnt">From Date<font class="mand">*</font>
		<td align="center">:</td>
		<td>
		<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.addLimitView.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
	<tr style="display:none;" id="todateDiv">
	    <td align="left" class="fnt">To Date <font class="mand">*</font>
		<td align="center">:</td>
		<td>
		<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.addLimitView.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
	
			<tr>
			<td align="center" colspan="0">Limit Currency  : 		
			 <td align="center">:</td>
			 <td>				
				<select name="limitcurrncy" id="limitcurrncy">   
					<option value="-1">-SELECT LIMIT CURRENCY-</option>    
						<s:iterator  value="globalcurrcy" >
							<option value="${NUMERIC_CODE}">${CURRENCY_DESC}</option>
						</s:iterator>
				</select>         
			</td>
			</tr>
	 
			
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="Next" onclick="return validation();"/>
						<%-- <s:submit value="Next"/> --%>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
	</form>  