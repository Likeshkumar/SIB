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
	//alert("Transact==> "+txn.length);
	var allcount = 0;
	
	var childfeetype = document.getElementById("childfeetype");
	//alert(childfeetype.value);
	var subfeedecs = document.getElementById("subfeedecs");
	//alert(subfeedecs);
	var fromdate = document.getElementById("fromdate");
	//alert(fromdate);
	var todate = document.getElementById("todate");
	//alert(todate);
	//alert(highcnt);
	if(childfeetype.value == "-1")
	{
		//alert("HAI");
		errMessage(fromdate,"Select Child fee Type");
		return false;
	}

	if(childfeetype.value=="S")
	{
		if(subfeedecs.value=="")
		{
			errMessage(subfeedecs,"Enter Special fee Name");
			return false;	
		}
		if(fromdate.value=="")
		{
			errMessage(fromdate,"Select From-Date");
			return false;	
		}
		if(todate.value=="")
		{
			errMessage(todate,"select To-Date");
			return false;	
		}
	}

		

		for(var i=0;i<txn.length;i++)
		{	
			allcount = allcount + 1;
			//alert("inside 1st for loop"+i);
			var txnid = document.getElementById("hidaactionlist"+i).value;
			var txndesc = document.getElementById("hidaactionname"+i).value;
			//alert("Transaction id"+txndesc);			
				var disamt = "action_code"+txnid;
				////alert("pertxnamt====> "+pertxnamt);
				var disamt = document.getElementById(disamt);
				var dismode = "mode"+txnid;
				////alert("pertxnamt====> "+pertxnamt);
				var dismode = document.getElementById(dismode);
				
				if (disamt.value == "" )
			    {	
			    	 ////alert ( "Please Enter Limit Amount" );
			    	 errMessage(disamt, "Please Enter "+txndesc+" fee Amount");
			    	 return false; 
				}
				if (dismode.value == "-1" )
			    {	
			    	 ////alert ( "Please Enter Limit Amount" );
			    	 errMessage(dismode, "Please Select "+txndesc+" fee Mode");
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
	var url = "deleteFeeSchemeConfigAction.do?feeid="+feeid;
	AjaxReturnValue(url);
	document.schemeprocess.action="feehomeSchemeConfigAction.do";
	document.getElementById("schemeprocess").submit();
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

<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="configSchemeSchemeConfigAction" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">
		<table border='0' cellpadding='0' cellspacing='0' width='90%' class="formtable">
 
	<tr> 
		
		<td> Fee Name </td>
		<td  align="left">
			:<s:property value="feenamebean"/>
			<s:hidden name="msterfeename" id="msterfeename"  value="%{feenamebean}"  />
		</td>
	
	</tr>
	<tr>  
	<td> Child Fee Type </td>
		<td>
			:
			<% if(feetype.equals("new")){%>
					<s:select id="childfeetype"  name="childfeetype" list="#{'D':'DEFAULT FEE','S':'SPECIAL FEE'}" headerKey="-1" headerValue="-SELECT-" onchange="showSplFee(this.value)"/>
			<% }else{ %>
					<s:select id="childfeetype"  name="childfeetype" list="#{'S':'SPECIAL FEE'}" headerKey="-1" headerValue="-SELECT-" onchange="showSplFee(this.value)"/>
			<%} %>
		</td> 
	
	<td> Special Fee Name </td>
	<td>
	 	<s:textfield name="subfeedecs" id="subfeedecs"/>
	</td> 
</tr>

<tr id="spldaterow" style="display:none">  
		<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.schemeprocess.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
        
        <td> 
		To Date
		</td>
		<td> :  
				<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.schemeprocess.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	 
</tr>
	  	<tr>
		<td colspan='4' style='padding-top:30px'>
			<table border='0' cellpadding='0' cellspacing='0' width='60%' >
			
			<%int i=0,j=0,k=0,l=0; %>
			<s:iterator value="schemeactionlist">
				 <input type='hidden' name='hidaactionlist' id='hidaactionlist<%=l++%>' value='${ACTION_CODE}' />
				 <input type='hidden' name='hidaactionname' id='hidaactionname<%=k++%>' value='${ACTION_DESC}' />
				<tr>
					<td style="text-align:left">${ACTION_DESC}</td> 
					<td> : <input type='text' name='${ACTION_CODE}' id='action_code${ACTION_CODE}'    style='width:70px'  maxlength='4'/></td>
					
					<td>
						<select name="mode"  id='mode${ACTION_CODE}' style='width:120px'>
							<option value="-1">Select Mode</option>
							<option value="F">FLAT</option>
							<option value="P">PERCENTAGE</option>
						</select>
					</td> 
				</tr> 
			</s:iterator> 
				
				<tr>
					<td colspan='3' style='text-align:center'> 
						<input type="submit" name="funsubmit" id="funsubmit" value="Add" onclick="return validation_scheme();"/>
						<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
					</td>
				</tr>
			</table>
		</td> 
	</tr>

			<s:if test="%{issubfeeexist}">
	<tr>
		<td colspan='4' style='padding-top:30px'>
			<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable">
			<tr> 
				<th>Fee Name</th>
				<th>From Date </th>
				<th>To Date </th>
				<th> Fee type </th>
				<th>Fee Configured date</th>
				<th>Configured by</th>
				<th>Delete</th>
			</tr>
		 
			<s:iterator value="subfeelist">
				<tr> 
					<td><a  href="javascript:void(0);" id="viewinfo" link="getViewDetailsSchemeConfigAction.do" findby="${FEE_SUBCODE}">${SUBFEE_DESC}</a></td>
					<td>${FROMDATE}</td>
					<td>${TODATE}</td>
					<td> 
						${SUBFEE_TYPE}
					</td>
					<td>${CONFIG_DATE}</td>
					<td>${USERNAME}</td>
					<td>
						<a onclick="return getDelete(${FEE_SUBCODE});"><img src="images/delete.png" alt="submit Button"/></a>
					</td>
				</tr>
			</s:iterator> 
			</table>
		</td>
	</tr>
	 </s:if>

	</table>
	<input type="hidden" name="feecode_exsist" id="feecode_exsist" value="0">

</s:form>
</body>
</html>