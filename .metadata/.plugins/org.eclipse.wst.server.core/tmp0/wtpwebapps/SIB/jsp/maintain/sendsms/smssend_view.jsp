<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="cache-control" content="no-cache" />
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>


<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>

function sendSMSToCustomer(){
	var bulksmscode = document.getElementById("bulksmscode");
	var url = "sendSMSToCustomerSMSSender.do?bulksmsid="+bulksmscode.value;
	if( !confirm("Confirm...?") ){
		return false;
	}
	//var result = AjaxReturnValue(url);
	window.location=url;
}


function verifySmsContents(){
	var bulksmscode = document.getElementById("bulksmscode"); 
	 
	var url = "verifySmsMsgSMSSender.do?bulksmsid="+bulksmscode.value;
	if( !confirm("Confirm...?") ){
		return false;
	}
	
	var result = AjaxReturnValue(url)	;
	alert(result);
	window.location.reload();
}
function editSmsContent(bulksmsid, smscontent ){
	var url = "editSmsMessageSMSSender.do?bulksmsid="+bulksmsid+"&smscontent="+smscontent; 
	if( !confirm("Do you want to edit ?") ){
		return false;
	}
	newwindow = window.open(url,'','left=350,top=150,width=500,height=300,location=no,menubar=no,menu=no,status=no,scrollbars=yes,resizable=yes,toolbar=no,titlebar=0,fullscreen = yes,directories=no');
	 
}

</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<style>
table  td{
	text-align:left;
	
}
</style>

<s:form action="sendSMSToCustomerSMSSender.do"  name="transanalysesform" autocomplete = "off" namespace="/" javascriptTooltip="true">
		<s:hidden name="bulksmscode" id="bulksmscode" value='%{smsbean.bulksmsid}' />
		<s:hidden name="bulksmscontent" id="bulksmscontent" value='%{smsbean.smscontent}' />
		<table border="1" cellpadding="0" class="formtable" cellspacing="0" width="80%" id="maxcount" class="smstablerow"  >
			<tr>
				<td class="fnt" width="25%">
					Message Group
					</td>
					<td class="textcolor" width="25%" style="text-align:left">
						<s:iterator value="smsbean.smsgrouplist">
							<s:property /> <br/>
						</s:iterator>
			 				 
					</td>
					
					<td> SMS Created by </td>
					<td class="textcolor" style="text-align:left"> : <s:property value="smsbean.makerid"/> </td>
					
			</tr> 
			 
			
			 <tr id="subjectrow" >
					<td> Enter SMS Subject </td>
					<td class="textcolor" style="text-align:left"> : <s:property value="smsbean.smssubject"/> </td>
					
					<td> Created Date </td>
					<td class="textcolor" style="text-align:left"> : <s:property value="smsbean.smsdate"/> </td>
					
			</tr>
			
			 <tr id="smscontentrow" >
					<td> Enter SMS Content </td>
					<td class="textcolor" >  : <s:property value="smsbean.smscontent"/> 
						<s:if test="smsbean.verifyneeded">
							<img src="images/edit.png" onclick="editSmsContent('<s:property value="smsbean.bulksmsid"/>','<s:property value="smsbean.smscontent"/>')" >
						</s:if>
					</td>
					
					<td> Status </td>
					<td class="textcolor" style="text-align:left"> : <s:property value="smsbean.smsstatus"/> </td>
					
			</tr> 
			
		 
			<tr id="cardnoexcept">
					<td> Except </td>
					<td style="text-align:left" class="textcolor">   <s:iterator value="smsbean.exceptlist">
							<s:property /> <br/>
						</s:iterator> </td> 
						
					<td> Checked By </td>
					<td class="textcolor" style="text-align:left"> : <s:property value="smsbean.checkerid"/> </td>
			</tr>
					
		</table> 
 

<table border="0" cellpadding="0" cellspacing="0" width="20%"  >
	<s:if test="smsbean.verifyneeded">
	<tr>  
			<td>	<input type="button" value="Verify" name="submit" id="submit" onclick="return verifySmsContents();"/>	</td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td> 
	
	</tr>
	</s:if>
	<s:if test="smsbean.sendsmsnow">
	<tr>  
			<td>	<s:submit value="Send SMS" name="submit" id="submit" onclick="return sendSMSToCustomer();"/>	</td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td> 
	
	</tr>
	</s:if>
	
</table>

</s:form>

 