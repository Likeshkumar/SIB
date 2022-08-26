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

 
	function showRevenus( recordid ){
		//alert("recordid  --- "+recordid);
		var tabrow =document.getElementById("row"+recordid);
		if ( tabrow.style.display == "none"){
			tabrow.style.display="table-row";
		}else{
			tabrow.style.display="none";
		}
		
	}

	function enableCombined( feemode ){
		var combinedfeerow = document.getElementById("combinedfeerow");
		var amt1 = document.getElementById("amt1");
		var amt2 = document.getElementById("amt2");
		if( feemode == "C"){
			amt1.innerHTML='Percentage Amount';
			amt2.innerHTML='Flat Amount';
			combinedfeerow.style.display="table-row";
		}else{
			amt1.innerHTML='Amount';
			combinedfeerow.style.display="none";
		}
	}

function showFeetable( feetype ){
	 
	var splitfeetable = document.getElementById("splitfeetable");
	var singlefeetable = document.getElementById("singlefeetable");
	var amount = document.getElementById("amount");
	var minamount = document.getElementById("minamount");
	if( feetype == "SINGLE" ){
		singlefeetable.style.display="table-row";
		splitfeetable.style.display="none";
		amount.focus();
	}else if( feetype == "SPLIT" ){
		splitfeetable.style.display="table-row";
		singlefeetable.style.display="table-row";
		minamount.focus();
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
	var combinedfee= document.getElementById("combinedfee");
	var entitylist = document.getElementsByName("ENTITYLIST");
	var amt1 = document.getElementById("amt1");
	var amt2 = document.getElementById("amt2");
	
	var total = 0;
	
	 
	 
	if( terminaltype ){
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
			if((parseInt(minamount.value)) > (parseInt(maxamount.value)))
			{
				alert("Max amount should be greater than Min amount");
				return false;
			}
		}
	}
	 
	
	if( amount ){
		if( amount.value == ""){ errMessage(amount,"Enter the " + amt1.innerHTML );return false;  }		
	}
	 
	if( feemode ){
		if ( feemode.value == "-1" ){ errMessage(feemode,"Select the fee mode ");return false;  } 
		if ( feemode.value == "C" ){
			 if( combinedfee.value == "" ){ errMessage(combinedfee,"Enter The  " + amt2.innerHTML  );return false; }  
		}
	}
	
	for ( var x=1; x<=entitylist.length; x++ ){
		var key = "REV"+x;
		var perceval = document.getElementById(key);
		if(perceval.value==""){ errMessage(perceval,"Revenue Sharing value should not be empty");return false; 	}
		if(isNaN( perceval.value )){  errMessage(perceval,"Entered value is not number. Please enter number");return false; 	}
		total = parseInt(total) + parseInt(perceval.value); 
	} 
	total_mod = total % 100; 
	//if( total_mod != 0){ errMessage(perceval,"Revenue is not devided properly. sum of revenue should be multiples of 100");return false;  }
	
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

function doDelete( recordid )
{
	var masterfeecode = document.getElementById("masterfeecode"); 
	var subfeecode = document.getElementById("subfeecode");
	var act = document.getElementById("act");
	var url = "deleteFeeInfoFeeConfig.do?masterfeecode="+masterfeecode.value+"&subfeecode="+subfeecode.value+"&recordid="+recordid+"&act="+act.value;
	if( confirm( "Do you want to delete...")){
		window.location=url;
		parent.showprocessing();   
	}
}
 function validation_authfee(subfeecode,feecode){
	 	var auth = document.getElementById("auth0").value;
	 	var reason = prompt('Enter the Reason for Reject');
		 if( reason ){
			 var url = "authorizeDeauthorizeFeeConfig.do?subfeecode="+subfeecode+"&feecode="+feecode+"&reason="+reason+"&auth="+auth;
			 window.location = url; 
		 }  
		 return false;
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
<%String actflag = (String)session.getAttribute("actflag"); %>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:if test="%{feebean.fee_flag=='Add'}">
<s:form action="editFeeActionFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">
	<input type="hidden" name="flag" id="flag" value="Add">
	<input type="hidden" name="act" id="act" value="<%=actflag%>"> 
	<table border='0' cellpadding='0' cellspacing='0' width='90%' class="formtable"> 
		<s:hidden name="masterfeecode" id="masterfeecode" value="%{feebean.masterfeecode}" />
		<tr> 		
			<td> Fee Name </td>
			<td  align="left">
				:<s:property value="feebean.masterfeename"/>
				<s:hidden name="masterfeecode" id="masterfeecode"  value="%{feebean.masterfeecode}"  />
			</td>	
		</tr>
		<tr>   
			<td> Special Fee Name </td>
			<td>:<s:property value="feebean.subfeename"/>
				<s:hidden name="subfeecode" id="subfeecode"  value="%{feebean.subfeecode}"  />	  
			</td> 
		</tr>
	<tr>
		<td colspan='4' style='padding-top:30px'>
			<table border='0' cellpadding='0' cellspacing='0' width='60%'>	
			 	<tr>
					<td> Terminal Type </td> 
					<td> :<s:select name="terminaltype" id="terminaltype" list="feebean.devicetypelist" listKey="DEVICETYPECODE" listValue="DEVICETYPE" headerKey="-1" headerValue="-SELECT-" /></td>
				</tr>				
				 <tr>
					<td> Transaction  </td> 
					<td> : <s:select  list="feebean.txnlist"  name="txncode" id="txncode" listKey="ACTION_CODE" listValue="ACTION_DESC" headerKey="-1" headerValue="-SELECT TXN-"></s:select></td>
				</tr>				
				<tr>
					<td> Fee type </td> 
					<td> :<s:select id="feemodetype"  name="feemodetype" list="#{'SINGLE':'Single Fee','SPLIT':'Split Fee'}" headerKey="-1" headerValue="-SELECT-"  onchange="showFeetable(this.value)"   /></td>
				</tr>
			</table>			
			<table border='0' cellpadding='0' cellspacing='0' width='60%'  >
				<tr id="splitfeetable" style="display:none">
					<td> Minimum Amount </td> <td> : <s:textfield name="minamount" id="minamount" /> </td>
					<td> Maximux Amount </td><td> :<s:textfield name="maxamount" id="maxamount" /></td>
				</tr>				
				<tr id="singlefeetable" style="display:none">
					<td> Mode </td>
					 <td> : <s:select id="feemode"  name="feemode" list="#{'F':'Flat','P':'Percentage','C':'Combined'}" headerKey="-1" headerValue="-SELECT-" onchange='enableCombined(this.value)'  /> </td>
					<td id='amt1'> Amount </td> <td> : <s:textfield name="amount" id="amount" />  </td>
				</tr>				
				<tr id="combinedfeerow" style="display:none" >
					<td id='amt2'> Amount 2 </td> <td> : <s:textfield name="combinedfee" id="combinedfee" />  </td><td>&nbsp; </td>					 
				</tr>				
			</table>
			<table border="0" cellpadding="0" cellspacing="0" width="50%" style="text-align:left">
				<tr><td colspan="3"> <h2>Revenue Sharing </h2></td></tr>
				<s:property value="ENTITY1"/>
				<% int i = 1; %>
				<s:iterator value="feebean.revenueentitieslist" status="CNT">
					<%-- <tr> <td> ${ENTITY_DESC } </td> <td> : <s:textfield name="ENTITYLIST" id="REV%{#CNT.count}" value="%{ENTITY_CODE}" style="width:50px;text-align:right"/> % </td>	</tr> --%>
					<tr><td  style="text-align:right" > ${ENTITY_DESC } </td> <td> : <input type="text" name="ENTITYLIST" id="REV<%=i%>" value="<% if(session.getAttribute("ENTITY"+i) != null) { out.println(session.getAttribute("ENTITY"+i) ); }%>" style="width:50px;text-align:right"/> 
						<s:select id="revfeemode"  name="revfeemodelist" list="#{'P':'Percentage','F':'Flat','C':'Combined'}"   style="width:200px"   /> 
					 </td></tr> 
					<s:hidden name="ENTITI%{#CNT.count}" value="%{ENTITY_CODE}"/>					
					<%i++;%>
				</s:iterator>
				 
				<s:if test="feebean.remember_rev">
					<tr><td colspan="2"><s:checkbox name="revenue_remember" id="revenue_rememeber" checked="true" /> Remember Revenue sharing for next configuration </td></tr>
				</s:if>
				<s:else>
					<tr><td colspan="2"><s:checkbox name="revenue_remember" id="revenue_rememeber" /> Remember Revenue sharing for next configuration </td></tr>
				</s:else>
		</table>			
		<table>
			<tr>
				<td colspan='3' style='text-align:center'> 
					<input type="submit" name="funsubmit" id="funsubmit" value="Add" onclick="return validation_scheme();"/>
					<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
				</td>
			</tr>
		</table>			
		<table border='0' cellpadding='0' cellspacing='0' width='100%'   id="formtable">
				 <tr>
				 	<th>Transaction </th><th>Terminal Type</th> <th>Fee type</th>  <th>Min-Amount</th><th>Max-Amount</th>
				 	<th>Fee-Amount</th><th>Fee-Mode</th><th>Delete</th>
				 </tr>				 
				 <s:iterator value="feebean.feeamtlist">
					 <tr onclick='showRevenus(${RECORD_ID})'>
					 	<td style="">${FEE_ACTION}</td>
					 	<td>${TERMINAL_TYPE}</td>
					 	<td>${FEE_TYPE}</td>  
					 	<td style="text-align:right">${MIN_AMOUNT}</td>
					 	<td style="text-align:right">${MAX_AMOUNT}</td>
					 	<td style="text-align:right">${FEE_AMOUNT}</td>
					 	<td style="text-align:left"> 
					 		<s:if test='%{FEE_MODE=="F"}'> Flat </s:if>
					 		<s:elseif test='%{FEE_MODE=="P"}'> Percentage </s:elseif>
					 		<s:elseif test='%{FEE_MODE=="C"}'> Combined ( Percetage + Flat ) </s:elseif>
					 		<s:else> Undefined  </s:else>
					 	</td> 
					 	<td>
							<a onclick="return doDelete(${RECORD_ID});"><img src="images/delete.png" alt="submit Button"/></a>
						</td>
					 </tr>				 
					 <tr style='display:none' id='row${RECORD_ID}'>
						 <td colspan='8' > 
						 	<table border='1' cellpadding='0' cellspacing='0' width='50%' style="color:green">
						 		<s:action name="editFeeActionFeeConfig!getRevenues" executeResult="false"  var="sss">
				  			  		<s:param name="instidbean" >${RECORD_ID}</s:param> 
				  			  		<s:param name="recordid" >${RECORD_ID}</s:param> 
				  			  	</s:action>
						 		<s:iterator  value="#sss.revelist">  
								 	<tr>
								 		<td style='text-align:left'>  ${TXNKEYDESC}  </td><td  style='text-align:right'>  ${REVENUE_AMT}  </td><td style='text-align:left'>  ${REV_MODE}  </td>
								 	</tr>
							 	 </s:iterator> 
						 	</table>								  			
						 </td>
					 </tr>
				 </s:iterator>
		</table>	
	</table>
	</s:form>
</s:if>
<s:else>
	<%-- <s:form action="deleteauthorizeDeauthorizeFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/"> --%>
	<%-- <s:form action="authorizeDeauthorizeFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/"> --%>
	<s:form action="authorizeDeauthorizeDeletedRecordsFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">	
		<s:hidden name="subfeecode" id="subfeecode" value="%{feebean.subfeecode}"></s:hidden>
		<s:hidden name="feecode" id="feecode" value="%{feebean.masterfeecode}"></s:hidden>			
		<table border='0' cellpadding='0' cellspacing='0' width='90%' class="formtable">
		<tr><td colspan="2" class="textcolor">MASTER FEE : </td><td><s:property value="feebean.masterfeename"/></td><td  class="textcolor">SUB FEE : </td><td><s:property value="feebean.subfeename"/></td></tr> 
						 <tr>
						 	<th>Transaction </th><th>Terminal Type</th> <th>Fee type</th>  <th>Min-Amount</th><th>Max-Amount</th>
						 	<th>Fee-Amount</th><th>Fee-Mode</th><th>Aquirer-Id</th><th>Status</th><th>Deleted Status</th>
						 </tr>
						 <% int rowcnt = 0; Boolean alt=true; %> 				 
						 <s:iterator value="feebean.feeamtlist">						 
							 <tr 
							 <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
							 onclick='showRevenus(${RECORD_ID})'>
							 	<s:hidden name="recordid"  id="recordid<%=rowcount%>" value="%{RECORD_ID}"></s:hidden>
							 	<td style="">${FEE_ACTION}</td>
							 	<td>${TERMINAL_TYPE}</td>
							 	<td>${FEE_TYPE}</td>  
							 	<td style="text-align:right">${MIN_AMOUNT}</td>
							 	<td style="text-align:right">${MAX_AMOUNT}</td>
							 	<td style="text-align:right">${FEE_AMOUNT}</td>
							 	<td style="text-align:left"> 
							 		<s:if test='%{FEE_MODE=="F"}'> Flat </s:if>
							 		<s:elseif test='%{FEE_MODE=="P"}'> Percentage </s:elseif>
							 		<s:elseif test='%{FEE_MODE=="C"}'> Combined ( Percetage + Flat ) </s:elseif>
							 		<s:else> Undefined  </s:else>
							 	</td> 
							 	<td>${ACQUIRERID}</td>
							  	<td>${AUTH_CODE}</td>
								<td>${DELETED_FLAG}</td>
							 </tr>				 
							 <tr style='display:none' id='row${RECORD_ID}'>
								 <td colspan='8' > 
								 	<table border='1' cellpadding='0' cellspacing='0' width='50%' style="color:green">
								 		<s:action name="editFeeActionFeeConfig!getRevenues" executeResult="false"  var="sss">
						  			  		<s:param name="recordid" >${RECORD_ID}</s:param> 
						  			  	</s:action>
								 		<s:iterator  value="#sss.revelist">  
										 	<tr>
										 		<td style='text-align:left'>  ${TXNKEYDESC}  </td><td  style='text-align:right'>  ${REVENUE_AMT}  </td><td style='text-align:left'>  ${REV_MODE}  </td>
										 	</tr>
									 	 </s:iterator> 
								 	</table>								  			
								 </td>
							 </tr>
						 </s:iterator>			
					<tr>
						<td colspan='8' style='text-align:center'> 
							<input type="submit" name="auth" id="auth1" value="Authorize"/>
							<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authfee(${feebean.subfeecode},${feebean.masterfeecode})"/>
						</td>
					</tr>
			</table>
	</s:form>
</s:else>

</body>
</html>