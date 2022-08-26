<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/popup.js"></script>
<%
String addfeename = (String)session.getAttribute("addfeename");	
String subfeename = (String)session.getAttribute("subfeename");	
String maxTotal = (String)session.getAttribute("maxTotal");
String feetype = (String)session.getAttribute("feetype");
%>
<script>

function showFeetable( feetype ){
	 
	var splitfeetable = document.getElementById("splitfeetable");
	var singlefeetable = document.getElementById("singlefeetable");
	if( feetype == "SINGLE" ){
		singlefeetable.style.display="table";
		splitfeetable.style.display="none";
	}else if( feetype == "SPLIT" ){
		splitfeetable.style.display="table";
		singlefeetable.style.display="table";
	}else{
		splitfeetable.style.display="none";
		singlefeetable.style.display="none";
	}
}


function checkSchemeExist()
{
	var feecode = document.getElementById("schemecode");
	if(feecode.value == "")
	{
		errMessage(schemecode,"Enter The Fee Code" );
		return false;
	}
	else
	{
		var url="checkFeecodeexsistSchemeConfigAction.do?checkfeecode="+feecode.value;
		var response=AjaxReturnValue(url);
		var ajax_resp = response;
		alert("Ajax Output===> "+ajax_resp);
		if(ajax_resp == "1")
		{
			document.getElementById('feecode_exsist').value="1";
			document.getElementById('errmsg').innerHTML = "Fee Code Already Exsist,Enter Different Code ";
		}else{
			document.getElementById('feecode_exsist').value="0";
			document.getElementById('errmsg').innerHTML = "";
		}
		return true;
	}
}

function validation_scheme()
{
	valid=true;
		var feelength = document.getElementsByName("hidaactionlist").length;
		var feecode = document.getElementById("schemecode");
		var schemedesc = document.getElementById("schemedesc");
		if(feecode.value == "")
		{
			errMessage(schemecode,"Enter The Fee Code" );
			return false;
		}
		if(schemedesc.value == "")
		{
			errMessage(schemedesc,"Enter The Fee Description" );
			return false;
		}
			for(var i=0;i<feelength;i++)
			{
				var actionname = document.getElementById("hidaactionname"+i);
				var actioncode = document.getElementById("action_code"+i);
				if(actioncode.value=="")
				{
					
					//alert("actionname--> "+actionname);
					//alert("Fee Cannot be Empty");
					errMessage(actioncode,"ENTER THE "+actionname.value+" FEE");
					return false;
				}
				if(actioncode.value.startsWith("0"))
				{
					//alert("ZERO At first index");
					errMessage(actioncode,"CANNOT ENTER ZERO AT FIRST PLACE");
					return false;
				}
				var modecode = document.getElementById("mode"+i);
				if(modecode.value=="-1")
				{
					//alert("Mode Cannot be Empty");
					errMessage(modecode,"SELECT THE "+actionname.value+" MODE");
					return false;		
				}
			}
	return valid;
}

function showSplFee( feetype ){
	if( feetype == "D"){
		
		var def_fee = document.getElementById("msterfeename").value;
		document.getElementById("subfeedecs").value = def_fee;
		document.getElementById("subfeedecs").readOnly = true;
		document.getElementById("spldaterow").style.display = 'none';
	}else{
		document.getElementById("subfeedecs").focus();
		document.getElementById("subfeedecs").readOnly = false;
		document.getElementById("subfeedecs").value = "";
		document.getElementById("spldaterow").style.display = 'table-row';
	}
}


function validation_scheme()
{
	var txn = document.getElementsByName("hidaactionlist"); 
	var allcount = 0;
	
	var childfeetype = document.getElementById("childfeetype"); 
	var subfeedecs = document.getElementById("subfeedecs"); 
	var fromdate = document.getElementById("fromdate"); 
	var todate = document.getElementById("todate"); 
	var terminaltype = document.getElementById("terminaltype");
	var txncode = document.getElementById("txncode");
	var feemodetype = document.getElementById("feemodetype");
	var minamount = document.getElementById("minamount");
	var maxamount = document.getElementById("maxamount");
	var amount = document.getElementById("amount");
	var feemode = document.getElementById("feemode");
	
	if(childfeetype.value == "-1")	{ errMessage(childfeetype,"Select Child fee Type");	return false;	}

	if(childfeetype.value=="S")
	{
		if(subfeedecs.value==""){errMessage(subfeedecs,"Enter Special fee Name");return false;}
		
		if(fromdate.value=="")	{errMessage(fromdate,"Select From-Date");return false;}
		
		if(todate.value==""){errMessage(todate,"select To-Date");return false;}
		
	}
	
	 
/* 	if( terminaltype ){
		if( terminaltype.value == "-1"){ errMessage(terminaltype,"Select the type of terminal");return false; }
	}
	 
	if( txncode ){
		if( txncode.value == "-1"){errMessage(txncode,"Select the transaction type ");return false; }
	}
	if( feemodetype ){
		if( feemodetype.value == "-1"){ errMessage(feemodetype,"Select the transaction type ");return false;  }
		if( feemodetype.value == "SPLIT" ){
			if( minamount.value ==  ""){ errMessage(minamount,"Enter minimum amount ");return false;   }
			if( maxamount.value ==  ""){ errMessage(maxamount,"Enter maximux amount ");return false;   }
		}
	}
	
	if( amount ){
		if( amount.value == ""){ errMessage(amount,"Enter the fee amount");return false;  }		
	}
	 
	if( feemode ){
		if ( feemode.value == "-1" ){ errMessage(feemode,"Select the fee mode ");return false;  }
	} */
	
	parent.showprocessing();
	return true;
		
}


function addCode(code){
    var JS= document.createElement('script');
    JS.text= code;
    parent.document.getElementById("content").appendChild(JS);
}
// Wait until the DOM has loaded before querying the document
$(document).ready(function(){

	$('a#viewinfo').click(function(e){
	//alert($("#comcode").val());
		modal.open({content: 
			$("#comcode").val()
		},$(this).attr("findby"),$(this).attr("link"));
		
		e.preventDefault();
		$f=$(this).parents("#popdivpos").attr("id");
		
		$(this).parents('#modal').toggle(false);		
		//$( "#modal",document).resizable();					
	});
});

function getDelete(feeid)
{
	if( confirm( "Do you want to delete...") ){
		var act = document.getElementById("act").value; 
		var masterfeecode = document.getElementById("masterfeecode"); 
		var url = "deleteFeeFeeConfig.do?feeid="+feeid+"&act="+act+"&masterfeecode="+masterfeecode.value;
		window.location = url;
	}
	
	return false;
	
}

function authorizeFee(){
	if( confirm( "Do you want to authorize...") ){
		var url = "authFeeFeeConfig.do?feeid="+feeid;
		window.location = url;
	}
	
	return false;
}

function editSubfee( subfeeid ){	 
	var masterfeecode = document.getElementById("masterfeecode"); 
	var act = document.getElementById("act").value; 
	var url = "editSubFeeHomeFeeConfig.do?masterfeecode="+masterfeecode.value+"&subfeecode="+subfeeid+"&flag="+"Add"+"&act="+act;
	if( confirm( "Do you want to continue...") ){
		window.location=url;
		parent.showprocessing();
	}else{
		return false;
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
.textcolor
{
color: maroon;
font-size: small;
}
.textcolor1
{
color: black;
font-size: small;
}
.bordercl td,th
{
  border:1px solid #CCCCCC;
}
</style>
</head>
<script>

function validateform()
{

var txn = document.getElementsByName("txncode");
var txndesc = document.getElementsByName("txndesc");
var allcount = 0;
for(var i=0;i<txn.length;i++) {	
	allcount = allcount + 1; 
	var txnamt = document.getElementById("feeamount_"+txn[i].value).value;
	

			if(txnamt=="")
			{
			errMessage(document.getElementById("feeamount_"+txn[i].value),"<b>"+txndesc[i].value+"</b> :  Should not empty");
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

<body>
<%String act = (String)session.getAttribute("act");%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addFeeActionFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">
		<input type="hidden" name="act" id="act" value="<%=act%>"> 

		<s:hidden name="currencycode" id="currencycode"  value="%{feebean.numericCode}"  />
		<table border='0' cellpadding='0' cellspacing='0' width='30%' class="formtable"> 
 		
	<tr> 
		
		<td> Fee Name </td>
		<td  align="left">
			:<s:property value="feebean.feename"/>
			<s:hidden name="masterfeename" id="masterfeename"  value="%{feebean.feename}"  />
		</td>
	
	</tr>
	<tr>  
	

	</table>

	
	
	<table border='0' cellpadding='0' cellspacing='0' width='90%'> 
			<tr>
				<td colspan='4' style='padding-top:30px'>
					<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
						<tr> 
							<th style="text-align:center">Fee Desc</th>
							<th style="text-align:center">Fee Amount</th>
																				
						</tr>
						<% int rowcnt = 0; Boolean alt=true; %> <%int i=0,j=0; %>
						<s:iterator value="feebean.feeLisConfig">	
						<input type='hidden' name='txncode' id='txncode_<%=i++%>' value='${TXN_CODE}' />
						<input type='hidden' name='txndesc' id='txndesc<%=j++%>' value='${ACTION_DESC}' />
						
						
				
				
							<tr	<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>	> 
								<td>${ACTION_DESC}</td>
								<td ><input style="text-align:right" id = "feeamount_${TXN_CODE}" type="text" name="feeamount_${TXN_CODE}" onkeyup="validateNumber('Fee Amount',this.id,this.value)" value=""/>&nbsp; <s:property value="feebean.currecyCode"  /></td>
								
							</tr>
						</s:iterator> 
					</table>
				</td>
			</tr>
			<tr>
				<td colspan='1' style='text-align:center'> 
					<input type="submit" name="auth" id="auth1" value="Save" onclick="return validateform()"/>
					<input type="reset" name="Cancel" id="Cancel" value="Cancel">
				</td>
			</tr>
		</table>	
		

</s:form>
</body>
</html>