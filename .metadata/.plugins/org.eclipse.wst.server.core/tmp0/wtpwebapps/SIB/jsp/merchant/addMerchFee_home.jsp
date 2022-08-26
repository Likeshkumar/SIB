<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script>

function showSplFee( feetype ){
	if( feetype == "D"){
		var def_fee = document.getElementById("msterfeename").value;
		document.getElementById("splfeename").value = def_fee;
		document.getElementById("splfeename").readOnly = true;
		document.getElementById("spldaterow").style.display = 'none';
	}else{
		document.getElementById("splfeename").focus();
		document.getElementById("splfeename").readOnly = false;
		document.getElementById("splfeename").value = "";
		document.getElementById("spldaterow").style.display = 'table-row';
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
		var url="checkFeecodeexsistMerchantProcess.do?checkfeecode="+feecode.value;
		var response=AjaxReturnValue(url);
		var ajax_resp = response;
		//alert("Ajax Output===> "+ajax_resp);
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
		var feecode = document.getElementById("schemecode");
		var feedesc = document.getElementById("schemedesc");
		
		if(feecode.value == "")
		{
			//alert("ENTER FEECODE");
			errMessage(schemecode,"ENTER FEECODE");
			return false;
		}
		if(feedesc.value == "")
		{
			//alert("ENTER FEE DESC");
			errMessage(schemedesc,"ENTER FEE DESC");
			return false;
		}
		
		var feelength = document.getElementsByName("hidaactionlist").length;
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

function validateFee () {
 
	var childfeetype = document.getElementById("childfeetype");
	var splfeename = document.getElementById("splfeename");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	var hidaactionname = document.getElementsByName("hidaactionname");
	 
	 
	if( childfeetype ){		if( childfeetype.value == "-1"){ errMessage(childfeetype,"Select the fee type"); return false; }	}
	
	if( splfeename ){		if( splfeename.value == ""){ errMessage(splfeename,"Enter the fee name"); return false; }	}
	
	if( childfeetype.value == "S" ){
		if( fromdate ){		if( fromdate.value == ""){ errMessage(fromdate,"Select the from date"); return false; }	} 
		if( todate ){		if( todate.value == ""){ errMessage(todate, "Select the to date"); return false; }	}
	}

	  for ( var i=0; i< hidaactionname.length; i++ ){
			var modid = document.getElementById("mode"+i); 
			var action_code = document.getElementById("action_code"+i);
			if( action_code.value == "") { errMessage(action_code, "Entry shoud not be empty for " + hidaactionname[i].value  ); return false;  } 
			if( modid.value == "-1") { errMessage(modid, "Select Mode for " + hidaactionname[i].value); return false;  }  
	  } 
	  
	  parent.showprocessing();
}
	
	 
	
	 
 
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
	#textcolor
	{
	color: maroon;
	font-size: small;
	}
</style>
</head>
 
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="configSchemeMerchantProcess.do" name="orderform" autocomplete = "off"  namespace="/"   onsubmit="return validateFee()" >

	<s:hidden name="feetype" id="feetype" value="%{mprbean.feetypebean}"/>
	<table border='0' cellpadding='0' cellspacing='0' width='90%' class="formtable">
 
	<tr> 
		
		<td> Fee Name </td>
		<td>
			:<s:property value="mprbean.feenamebean"/>
			
			<s:hidden name="msterfeename" id="msterfeename"  value="%{mprbean.feenamebean}"  />
			<s:hidden name="mastercodebean" id="mastercodebean"  value="%{mprbean.mastercodebean}"  />
		</td>
		
		
		
	</tr>
	
<tr>  
	<td> Child Fee Type </td>
		<td>
			:  
			<s:select id="childfeetype"  name="childfeetype" list="#{'D':'DEFAULT FEE','S':'SPECIAL FEE'}" headerKey="-1" headerValue="-SELECT-" onchange="showSplFee(this.value)"/>
		</td> 
	
	<td> Special Fee Name </td>
	<td>
	 	<s:textfield name="splfeename" id="splfeename" />
	</td> 
</tr>

<tr id="spldaterow" style="display:none">  
		<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
        
        <td> 
		To Date
		</td>
		<td> :  
				<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	 
</tr>


	
	<tr>
		<td colspan='4' style='padding-top:30px'>
			<table border='0' cellpadding='0' cellspacing='0' width='60%'>
			<tr> <td colspan="4"> <small class="dt"> If Fee is not required. Enter 0 (Zero) </small> </small></td> </tr>
			<%int i=0,j=0,k=0; %>
			<s:iterator value="mprbean.schemeactionlist">
				 <input type='hidden' name='hidaactionlist' id='hidaactionlist' value='${ACTION_CODE}' />
				 <input type='hidden' name='hidaactionname' id='hidaactionname<%=k++%>' value='${ACTION_DESC}' />
				 
				<tr>
					<td>${ACTION_DESC}</td> 
					<td> : <input type='text' name='${ACTION_CODE}' id='action_code<%=i++%>'    style='width:70px'  maxlength='4'/></td>
					
					<td>
						<select name="mode"  id='mode<%=j++%>' style='width:120px'>
							<option value="-1">Select Mode</option>
							<option value="F">FLAT</option>
							<option value="P">PERCENTAGE</option>
						</select>
					</td> 
				</tr> 
			</s:iterator> 
				
				<tr>
					<td colspan='3' style='text-align:center'> 
						<input type="submit" name="submit" value="Add"  />
						<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
					</td>
				</tr>
			</table>
		</td> 
	</tr>
 
	<s:if test="%{mprbean.issubfeeexist}">
	<tr>
		<td colspan='4' style='padding-top:30px'>
			<table border='0' cellpadding='0' cellspacing='0' width='90%'>
			<tr> 
				<th>Fee Name</th>
				<th>From Date </th>
				<th>To Date </th>
				<th> Fee type </th>
				<th>Fee Configured date</th>
				<th>Configured by</th>
			</tr>
		 
			<s:iterator value="mprbean.subfeelist">
				<tr> 
					<td>${SUBFEE_DESC}</td>
					<td>${FROMDATE}</td>
					<td>${TODATE}</td>
					<td> 
						${SUBFEE_TYPE}
					</td>
					<td>${CONFIG_DATE}</td>
					<td>${USER_CODE}</td>
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