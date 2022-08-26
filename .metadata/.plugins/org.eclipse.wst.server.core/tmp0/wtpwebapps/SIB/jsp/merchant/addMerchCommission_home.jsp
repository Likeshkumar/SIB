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
<script type="text/javascript">
function validation()
{
	alert("welcome");
	valid=true;
	var err_msg = document.getElementById("errormsg");
	var txn = document.getElementsByName("txn");
	//alert("Transact==> "+txn.length);
	
	var allcount = 0;
	var merchant = "commissionname";
	////alert("limitvl====> "+limitvl);
	var merchant = document.getElementById(merchant);
	
	var commission_mode = "commission_mode";
	var commission_mode =document.getElementById(commission_mode);
	
	var scheme = "scheme";
	////alert("limitvl====> "+limitvl);
	var scheme = document.getElementById(scheme);
	
	var glscheme = "glscheme";
	////alert("limitvl====> "+limitvl);
	var glscheme = document.getElementById(glscheme);
	
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
	

	if (merchant.value == "" )
    {	
    	 ////alert ( "Please Enter Limit Amount" );
    	 errMessage(merchant, "Please Select the  Commission Name");
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
	/* 	if( lowtxncnt ){
		if (lowtxncnt.value == "" )
		    {	
				if(lowtxncnt.disabled == false)
				{////alert ( "Please Enter Limit Amount" );
		    	 errMessage(lowtxncnt, "Please Enter Low Transaction Count");
		    	 return false;
				}
			}
		}
		
		if( highcnt ) {
		if (highcnt.value == "")
	    {	
			if(highcnt.disabled == false)
			{ 
				////alert ( "Please Enter Limit Amount" );
	    	 	errMessage(highcnt, "Please Enter  High Transaction Count");
	    	 	 return false;
			}
		} */
	

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
		    	 errMessage(disamt, "Please Enter "+txndesc+" Commission Amount");
		    	 return false; 
			}
			if (dismode.value == "-1" )
		    {	
		    	 ////alert ( "Please Enter Limit Amount" );
		    	 errMessage(dismode, "Please Select "+txndesc+" Commission Mode");
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

function loaddiv(selctid)
{	var checkdiv = document.getElementById(selctid);	
 	
	if(checkdiv.value!="-1"){
		var NameDiv = document.getElementById("FeeNameDiv");
		var Existdiv = document.getElementById("FeeExistdiv");
		if(checkdiv.value=="new")
		{
			NameDiv.style.display = 'table-row';
			Existdiv.style.display = 'none';
		}
		else
		{
			Existdiv.style.display = 'table-row';
			NameDiv.style.display = 'none';
		}
	}
	
}

function validation()
{
	var commtype = document.getElementById("feetype").value;
	var commname = document.getElementById("feename").value;
	var commcode = document.getElementById("feecode").value;
	//alert(commtype);
	if(commtype=="-1")
	{
		errMessage(feetype,"Select Commission Type");
		return false;
	}
	if(commtype=="new")
	{
		if(commname=="")
		{
			errMessage(feename,"Enter Commission Name");
			return false;
		}
	}
	if(commtype=="exist")
	{
		if(commcode=="-1")
		{
			errMessage(feecode,"Select Commission");
			return false;		
			
		}
	}
	
	parent.showprocessing();
}
</script>
</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="nextcommissionMerchantProcess.do" method="get" autocomplete="off">
<table border="0" cellpadding="0" cellspacing="6" width="50%" class="formtable">
			<tr>
				<td align="right">
					SELECT COMMISSION TYPE
				</td>
				<td align="center">
					:					
				</td>
				<td>
					<select name="feetype" id="feetype" onchange="loaddiv(this.id);">
							<option value="-1">SELECT COMMISSION TYPE</option>								
							<option value="new">New Commission</option>
							<option value="exist">Add with existing Commission</option>								
					</select>
				</td>
				
			</tr>
			<tr style="display:none;" id="FeeExistdiv">
					<td align="right">SELECT COMMISSION</td>
				    <td align="center">:</td>
					<td> 
						<select name="feecode" id="feecode">
							<option value="-1">-SELECT COMMISSION-</option>
								<s:iterator  value="mprbean.runningSchemeList" >
							<option value="${COM_MASTERCODE}">${COM_DESC}</option>
								</s:iterator>
						</select>
					</td>
			</tr>
			<tr style="display:none;" id="FeeNameDiv">
					<td align="right">Enter Commisson Name</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="feename" id="feename" value=""/>						
					</td>
			</tr>
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="Next" onclick="return validation()"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
</form>
</body>
</html>