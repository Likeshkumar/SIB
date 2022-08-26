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
function showSplFee( feetype ){
	if( feetype == "D"){
		var def_fee = document.getElementById("msterdisname").value;
		document.getElementById("spldisname").value = def_fee;
		document.getElementById("spldisname").readOnly = true;
		document.getElementById("spldaterow").style.display = 'none';
	}else{
		document.getElementById("spldisname").focus();
		document.getElementById("spldisname").readOnly = false;
		document.getElementById("spldisname").value = "";
		document.getElementById("spldaterow").style.display = 'table-row';
	}
}
function commissionrequired(vals)
{
	if(vals=="0")
	{
		document.getElementById("lowtxncnt").disabled=true;
		document.getElementById("highcnt").disabled=true;
		document.getElementById("lowtxncnt").value="";
		document.getElementById("highcnt").value="";
		document.getElementById("lowamt").disabled=false;
		document.getElementById("highamt").disabled=false;
		document.getElementById("lowamt").focus();

	}
	else
	{
		document.getElementById("lowamt").disabled=true;
		document.getElementById("highamt").disabled=true;
		document.getElementById("lowamt").value="";
		document.getElementById("highamt").value="";
		document.getElementById("lowtxncnt").disabled=false;
		document.getElementById("highcnt").disabled=false;
		document.getElementById("lowtxncnt").focus();
	}
}

function validation()
{
	//alert("welcome");
	valid=true;
	var err_msg = document.getElementById("errormsg");
	var txn = document.getElementsByName("txn");
	//alert("Transact==> "+txn.length);
	
	var allcount = 0;
	var discountname = "discountname";
	var discountname = document.getElementById(discountname);
	
	var merchant = "merchant";
	////alert("limitvl====> "+limitvl);
	var merchant = document.getElementById(merchant);
	
	var discount_mode = "discount_mode";
	var discount_mode =document.getElementById(discount_mode);
	
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
	
	var fromdate = "fromdate";
	////alert("pertxnamt====> "+pertxnamt);
	var fromdate = document.getElementById(fromdate);	
	
	var todate = "todate";
	////alert("pertxnamt====> "+pertxnamt);
	var todate = document.getElementById(todate);	
	
	if(discountname.value == "")
	{
   	 errMessage(discountname, "Please Select the discount Name");
	 return false; 	
	}
	
	if (scheme.value == "-1" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(scheme, "Please Select the Card Scheme ");
    	 return false; 
	}
	if (glscheme.value == "-1" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(glscheme, "Please Select GL List ");
    	 return false; 
    }
	if (lowamt.value == "")
    {	
    	 if(lowamt.disabled == false)
    	 {
    	 	errMessage(lowamt, "Please Enter Low Transaction Amount");
    	 	return false; 
    	 }
	}
	if (highamt.value == "")
    {	
		if(highamt.disabled == false)
		{
    	 	errMessage(highamt, "Please Enter High Transaction Amount");
    	 	return false;
    	 } 
	}

	if (lowtxncnt.value == "" )
    {	
		if(lowtxncnt.disabled == false)
		{////alert ( "Please Enter Limit Amount" );
    	 errMessage(lowtxncnt, "Please Enter Low Transaction Count");
    	 return false;
		}
	}
	if (highcnt.value == "")
    {	
		if(highcnt.disabled == false)
		{ 
			////alert ( "Please Enter Limit Amount" );
    	 	errMessage(highcnt, "Please Enter  High Transaction Count");
    	 	return false;
		}
	}
	
	if (fromdate.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(fromdate, "Please enter From date");
    	 return false; 
	}
	
	if (todate.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(todate, "Please enter To date");
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


function commissionrequired(vals)
{
	if(vals=="0")
	{
		document.getElementById("lowtxncnt").disabled=true;
		document.getElementById("highcnt").disabled=true;
		document.getElementById("lowtxncnt").value="";
		document.getElementById("highcnt").value="";
		document.getElementById("lowamt").disabled=false;
		document.getElementById("highamt").disabled=false;
		document.getElementById("lowamt").focus();

	}
	else
	{
		document.getElementById("lowamt").disabled=true;
		document.getElementById("highamt").disabled=true;
		document.getElementById("lowamt").value="";
		document.getElementById("highamt").value="";
		document.getElementById("lowtxncnt").disabled=false;
		document.getElementById("highcnt").disabled=false;
		document.getElementById("lowtxncnt").focus();
	}
}

function validation()
{
	var txn = document.getElementsByName("txn");
	//alert("Transact==> "+txn.length);
	var allcount = 0;
	var childfeetype = document.getElementById("childdistype");
	//alert(childfeetype.value);
	var subfeedecs = document.getElementById("spldisname");
	//alert(subfeedecs);
	var fromdate = document.getElementById("fromdate");
	//alert(fromdate);
	var todate = document.getElementById("todate");
	//alert(todate);
	var lowamt = document.getElementById("lowamt");
	//alert(lowamt);
	var highamt = document.getElementById("highamt");
	//alert(highamt);
	var lowtxncnt = document.getElementById("lowtxncnt");
	//alert(lowtxncnt);
	var highcnt = document.getElementById("highcnt");
	//alert(highcnt);
	if(childfeetype.value == "-1")
	{
		//alert("HAI");
		errMessage(fromdate,"Select Child Discount Type");
		return false;
	}

	if(childfeetype.value=="S")
	{
		if(subfeedecs.value=="")
		{
			errMessage(subfeedecs,"Enter Special Discount Name");
			return false;	
		}
		if(fromdate.value=="")
		{
			errMessage(fromdate,"Select From-Date");
			return false;	
		}
		if(todate.value=="")
		{
			errMessage(todate,"Select To-Date");
			return false;	
		}
	}

		if(lowamt.value=="")
		{
			if(lowamt.disabled == false)
			{
				errMessage(lowamt,"Enter Low Txn Amount");
				return false;
			} 				
		}
		if(highamt.value=="")
		{
			if(highamt.disabled == false)
			{
				errMessage(highamt,"Enter High Txn Amount");
				return false;
			}
		}

	if( lowtxncnt ){
		if(lowtxncnt.value=="")
		{
			if(lowtxncnt.disabled == false)
			{
				errMessage(lowtxncnt,"Enter Low Txn Count");
				return false;
			}
		}
	}
	if( highcnt ){
		if(highcnt.value=="")
		{
			if(highcnt.disabled == false)
			{
				errMessage(highcnt,"Enter High Txn Count");
				return false;
			}
		}
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
			    	 errMessage(disamt, "Please Enter "+txndesc+"  Amount");
			    	 return false; 
				}
				if (dismode.value == "-1" )
			    {	
			    	 ////alert ( "Please Enter Limit Amount" );
			    	 errMessage(dismode, "Please Select "+txndesc+"  Mode");
			    	 return false; 
				}			
				
		}
		if(txn.length == allcount)
		{
			////alert("All Cells Checked---> ");
			if ( confirm( 'Do you want to Submit' )){
				parent.showprocessing();
	    		return true;
			}else{
	    		return false;
			}
		}
		
}

</script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/popup.js"></script>
<script language="javascript" type="text/javascript">
$(document).ready(function(){
	$('a#viewinfo').click(function(e){	
		modal.open({content: 
			$("#comcode").val()
		},$(this).attr("findby"),$(this).attr("link"));		
		e.preventDefault();
		$f=$(this).parents("#popdivpos").attr("id");		
		$(this).parents('#modal').toggle(false);		
		//$( "#modal",document).resizable();					
	});
});
function getDelete(discountid){
	var url = "deleteDiscountMerchantProcess.do?disid="+discountid;	
	var con = confirm("Are you want to delete?");
	if(con){
		alert(url);
		voidAjaxReturnValue(url); 
		document.merchdiscount.action="addMerchDiscounthomeMerchantProcess.do";
		document.merchdiscount.submit();
	}
}
</script>

</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<%String exist= (String)session.getAttribute("isexist"); %>
<form action="saveMerchDiscountMerchantProcess.do" method="get" name="merchdiscount" autocomplete="off">
	<table  border="0" cellpadding="0" cellspacing="6" width="80%" class="formtable">
	
		<tr>
			<td>Discount Name</td>
			<td colspan="4" class="brown"><s:property value="feenamebean"/>
			
			<s:hidden name="msterdisname" id="msterdisname"  value="%{mprbean.feenamebean}"  />
			<s:hidden name="mastercodebean" id="mastercodebean"  value="%{mprbean.mastercodebean}"  />
			</td>
		</tr>
		<tr>
			<td> Child Discount Type</td>
			<td>
				: <select onchange="showSplFee(this.value)" id="childdistype" name="childdistype" onchange="showSplFee(this.value)">
				    <option value="-1">-SELECT-</option>
				    <%if(exist.equals("A")){ %>
				    <option value="D">DEFAULT Discount</option>
				    <%}else{ %>
				    <option value="S">SPECIAL Discount</option>
				    <%} %>
				</select>
			</td>
			<td>Special Discount Name</td>
			<td>
				: <input type="text" id="spldisname" value="" name="spldisname" readonly  />
			</td> 
		</tr>		
		<tr id="spldaterow" style="display:none">
			<td>From Date</td>
			<td>
				: <input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.merchdiscount.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">												
			</td>

			<td>To Date</td>
			<td>
				: <input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.merchdiscount.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">												
			</td>
		</tr>
		<tr>
			<td>Discount Mode</td>
			<td colspan="4">  <s:radio name="discount_mode" id="discount_mode" list="#{'0':' Amount'}" value="0" onclick="commissionrequired(this.value);"/></td>
																				<!-- ,'1':' Count' #commented by pritto-->
			
		</tr>
		<!-- <tr>
		<td>Low Txn Count</td>
			
			<td>
				 : <input type='text' id="lowtxncnt"  style='width:100px;text-align:right' name='lowtxncnt' maxlength="16" disabled="disabled" onKeyPress="return numerals(event);"/> 
			</td>	

			<td>High Txn Count</td>							
			<td> 
				: <input type='text' id="highcnt"  style='width:100px;text-align:right' name='highcnt' maxlength="11" disabled="disabled" onKeyPress="return numerals(event);"/> 
			</td>	
		</tr> -->
		<tr>	
			<td>Low Txn Amount</td>						
			<td> 
				: <input type='text' id="lowamt"  style='width:100px;text-align:right' name='lowamt' maxlength="11"   onKeyPress="return numerals(event);"/>
			</td>

			<td>High Txn Amount</td>									
			<td> 
				: <input type='text' id="highamt" style='width:100px;text-align:right' name='highamt' maxlength="11"  onKeyPress="return numerals(event);"/> 
			</td>
		</tr>
</table>
<table><tr><td></td></tr></table>
	<table   border="0" cellpadding="0" align="center" width="60%" cellspacing="0" align="center" class="formtable">
						<tr>
							<td style="border:0px" align="center"> TRANSACTION TYPE </td> 
							<td style="border:0px" align="center"> DISCOUNT AMOUNT </td>
							<td style="border:0px" align="center"> DISCOUNT MODE </td>
						</tr>
						<%int i=0,j=0; %>
						<s:iterator value="mprbean.ActionCodeList">
							<input type='hidden' name='txn' id='txn<%=i++%>' value='${ACTION_CODE}'/>
							<input type='hidden' name='txndesc' id='txndesc<%=j++%>' value='${ACTION_DESC}' />
							<tr> 
									<td id="textcolor" align="center" class="brown">
										${ACTION_DESC}
									</td>							
									<td id="textcolor" align="center"> 
										&nbsp; <input type='text' id="disamt${ACTION_CODE}"  style='width:100px;text-align:right' name='disamt${ACTION_CODE}' maxlength="6" /> 
									</td>
									<td id="textcolor" align="center">
										&nbsp;<select name="dismode${ACTION_CODE}" id="dismode${ACTION_CODE}" style="width: 150px">
											<option value="-1">-Select Discount Mode-</option>
											<option value='F'>Flat</option>
											<option value='P'>Percentage</option>					
										</select>
									</td>								
							</tr>
						</s:iterator>
	</table>
<br/>	
	<table>
		<tr>
			<td>
				<s:submit value="Submit" onclick="return validation()"/>
			&nbsp;<input type="button" onclick="return confirmCancel()" class="cancelbtn" value="Cancel" id="cancel" name="cancel">
			</td>
		</tr>
	</table>
	<br/><br/>
  
	  <s:if test="%{mprbean.issubdiscountexist}">  
			<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable"  id="tooltipcontainer">
			<tr> 
				<th align="center"> DISCOUNT NAME </th>
				<th align="center"> FROM DATE </th>
				<th align="center"> TO DATE </th>
				<th align="center"> DISCOUNT TYPE </th>
				<th align="center"> DISCOUNT CONFIGURED DATE </th>
				<th align="center"> CONFIGURED BY </th>
			</tr>
		
			<s:iterator value="mprbean.subdiscountlist">
				<tr> 
					<td><a  href="javascript:void(0);" id="viewinfo" link="showdetaildiscountMerchantProcess.do" findby="${DISC_SUBCODE}">${SUB_DISC_DESC}</a></td>
					<td>${FROMDATE}</td>
					<td>${TODATE}</td>
					<td> 
						${SUBDIS_TYPE}
					</td>
					<td>${ADDEDDATE}</td>
					<td>${USER_CODE}</td>
					<td><a onclick="return getDelete(${DISC_SUBCODE});"><img  src="images/delete.png" alt="submit Button"/></a></td>
				</tr>
			</s:iterator> 
			</table>
	 </s:if>  
</form>
</body>
</html>