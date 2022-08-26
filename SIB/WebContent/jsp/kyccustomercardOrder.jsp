<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">

function getKycCarddetails()
{
	//alert(" Getting KYC Cust Exsist Cards Details ");
	//var cardno = "NODATA";
	//var mobileno = "NODATA";
	//var emailid = "NODATA";
	var cardno = document.getElementById("exist_card").value;
	var mobileno = document.getElementById("exist_mobileno").value;
	var emailid = document.getElementById("exist_mailid").value;
	
	var ajaxArqument = null;
	
	//alert("The Card Number Entered Is === "+cardno+" ====");
	//alert("The Mobile Number Entered Is === "+mobileno+" ===");
	//alert("The Email ID Entered Is === "+emailid+" ====");
	
	if( cardno != "")
	{
		if( mobileno != "" || emailid != "")
		{
		alert(" Enter Any One Value ");
		return false;
		}
	}
	if(mobileno != "")
	{
		if( cardno != "" || emailid != "")
		{
			alert(" Enter Any One Value ");
			return false;
		}
	}
	if(emailid != "")
	{
		if( cardno != "" || mobileno != "")
		{
			alert(" Enter Any One Value ");
			return false;
		}
	}
	
	if( cardno != ""  )
	{
		ajaxArqument = cardno+"~CARDNO";
	}
	if(mobileno != "")
	{
		ajaxArqument = mobileno+"~MOBILE";
	}
	if(emailid != "")
	{
		ajaxArqument = emailid+"~EMAILID";
	}
	//alert("ajaxArqument Passiong is "+ajaxArqument);
	if( ajaxArqument != null)
	{
		//alert(" Argument we Got is "+ajaxArqument);
	   	httpObject = getHTTPObjectForBrowser();
		if (httpObject != null) 
		{
			//alert(" Passing Argument is =====>"+ajaxArqument);
	   		httpObject.open("GET", "ajaxgettingKyccarddetailscardorderAction.do?searchArgument="+ajaxArqument, true);
			httpObject.send(null);
			httpObject.onreadystatechange = setkycdetailsAjaxOutput;
			return true;
		}
		else
		{
			alert("Browser Not Support");
			return false;
		}
	}
	else
	{
		alert(" Enter Valid Value ");
		return false;
	}
	
	return true;
}

function GettingproductCount()
{
	//alert("Getting Product Count ");
	var Product_binid = document.kycorderform.kyc_products.options[document.kycorderform.kyc_products.selectedIndex].value;
	if(Product_binid == -1)
	{
		alert(" Select Product ");
		return false;
	}
	if(Product_binid != -1)
	{
		
	   	httpObject = getHTTPObjectForBrowser();
		if (httpObject != null) 
		{
			//alert(" Passing Argument is =====>"+ajaxArqument);
	   		httpObject.open("GET", "ajaxgettingproductcountdetailscardorderAction.do?productbin="+Product_binid, true);
			httpObject.send(null);
			httpObject.onreadystatechange = setproductcountdetailsAjaxOutput;
			return true;
		}
		else
		{
			alert("Browser Not Support");
			return false;
		}
		
	}
}

function getHTTPObjectForBrowser()
{
    if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest) return new XMLHttpRequest();
    else{
       		alert("Browser does not support AJAX...........");
    		return null;
		}
}
function setkycdetailsAjaxOutput()
{
	if(httpObject.readyState == 4) 
	{
	    document.getElementById('carddetails').innerHTML = httpObject.responseText;
	}
}

function setproductcountdetailsAjaxOutput()
{
	if(httpObject.readyState == 4) 
	{
	    document.getElementById('productcount').innerHTML = httpObject.responseText;
	}
}

function validateKycdetails()
{
	//alert(" Validation Of KYC Form");
	var Product = document.kycorderform.kyc_products.options[document.kycorderform.kyc_products.selectedIndex].value;
	//alert(" Product Details "+Product);
	if(Product == "-1")
	{
		alert(" Please Select Any Product ");
		return false;
	}
	
	var count=document.kycorderform.cardcount.value;
	//alert(" Count Entered is "+count);
	if(count == "")
	{
		alert("Please Enter the Quantity");
		var txtbox=document.getElementById("cardcount");
		txtbox.focus();
		return false;
	}
	else
	{
		//alert("Count Entered ############ "+count);
		var p_count = document.kycorderform.maxpcount.value;
		var n_count = document.kycorderform.maxncount.value;
		var ordertype = document.kycorderform.ordertype.value;
		//alert(" Personal Count is "+p_count);
		//alert(" Non-Personal Count is "+n_count);
		//alert(" Order Type  is ====### "+ordertype);
		if((ordertype) == "Y" || (ordertype) == "B")
		{
			//alert(" ordertype mAtched &&&&&&& "+ordertype);
			var fistchar = count.substr(0,1);
			//alert(" First Charts is !!!!!!!!"+fistchar);
			if(fistchar != "0")
			{
				//alert(" fistchar char Not Statys with Zero 00000000 ");
				var ordercount = parseInt(count);
				//alert(" Inter ordercount is "+ordercount);
				if(ordercount>p_count)
				{
					alert("Please Enter the Quantity '"+p_count+"' or Below");
					document.kycorderform.cardcount.value="";
					var txtbox=document.getElementById("cardcount");
					txtbox.focus();
					return false;
				}
			}
			else
			{
				alert("Please Enter the Quantity Without Start with Zero");
				document.kycorderform.cardcount.value="";
				var txtbox=document.getElementById("cardcount");
				txtbox.focus();
				return false;
			}
		}
	}
	
	var Name_print =document.getElementById("printcard").value;
	//alert(" Name_print On Card Is ===> "+Name_print);
	if( Name_print == "")
	{
		alert(" Please Enter The Name To Print On Card ");
		document.kycorderform.printcard.value="";
		var namebox=document.getElementById("printcard");
		namebox.focus();
		return false;
	}
	
	var cardno = document.getElementById("exist_card").value;
	var mobileno = document.getElementById("exist_mobileno").value;
	var emailid = document.getElementById("exist_mailid").value;
	//alert(" Card Number Values is "+cardno);
	//alert(" Mobile Number Values is "+mobileno);
	//alert(" Emails Number Values is "+emailid);
	
	if(cardno == "" && mobileno == "" && emailid == "")
	{
		alert(" Enter Any One Value ");
		//document.kycorderform.exist_card.value="";
		var exsistNO=document.getElementById("exist_card");
		exsistNO.focus();		
		return false;
	}
	
	if( cardno != "")
	{
		if( mobileno != "" || emailid != "")
		{
			alert(" Enter Any One Value ");
			document.kycorderform.exist_mobileno.value="";
			document.kycorderform.exist_mailid.value="";
			//var exsistNO=document.getElementById("exist_card");
			//exsistNO.focus();
			return false;
		}
	}
	if(mobileno != "")
	{
		if( cardno != "" || emailid != "")
		{
			alert(" Enter Any One Value ");
			document.kycorderform.exist_card.value="";
			document.kycorderform.exist_mailid.value="";
			return false;
		}
	}
	if(emailid != "")
	{
		if( cardno != "" || mobileno != "")
		{
			alert(" Enter Any One Value ");
			document.kycorderform.exist_card.value="";
			document.kycorderform.exist_mobileno.value="";
			return false;
		}
	}
	//alert(" Validation Of KYC Form Success ");
	return true;	
}
</script>


<div align="center">
	<s:form action="kyccardOrdersaveCardorderAction"  name="kycorderform" autocomplete = "off"  namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="50%">
	<tr>
		<td>
			Select Product
		</td>
		<td>
			<select name="kyc_products" id="kyc_products" onchange="GettingproductCount();">
				<option value="-1">--Select Product</option>
					<s:iterator  value="kycproductlist">
						<option value="${PROD_ID}">${PRODNAME}</option>
					</s:iterator>
			</select>			
		</td>
	</tr>
	<tr>
		<td>
			Number Of Cards
		</td>
		<td>
			<input type="text" name="cardcount" id="cardcount">
		</td>
	</tr>
	<tr>
		<td>
			Name Print On Card
		</td>
		<td>
			<input type="text" name="printcard" id="printcard">
		</td>
	</tr>
		<tr>
			<td>
				Card Number
			</td>
			<td>
				<input type="text" name="exist_card" id="exist_card">
			</td>
		</tr>
		<tr>
			<td>
				Mobile Number
			</td>
			<td>
				<input type="text" name="exist_mobileno" id="exist_mobileno">
			</td>
		</tr>
		<tr>
			<td>
				Email-Id
			</td>
			<td>
				<input type="text" name="exist_mailid" id="exist_mailid">
			</td>
		</tr>
	</table>
	<br>
	
	<div id="productcount">
	</div>	
	<table border="0" cellpadding="0" cellspacing="0" width="50%">
		<tr align="center">
			<td>
				<input type="button" name="getdetails" id="getdetails" value="Get Card Details" onclick="return getKycCarddetails();">
			</td>
		</tr>
	</table>
	
	<div id="carddetails">
	</div>
	<br>
	
	<table border="0" cellpadding="0" cellspacing="0" width="20%">
		<tr align="center">
			<td>
				<s:submit name="kycorder" value=" Order " onclick="return validateKycdetails();"/>
			</td>
			<td>
				<s:reset name="clear" value=" Clear "/>
			</td>
		</tr>
	</table>
	</s:form>
</div>