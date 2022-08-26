<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript">
function goBack()
{
	window.history.back()
}
function getReason(){
	var reason = prompt("Reason for Reject ?");
	if( reason == null || !reason ){
		return false;
	}
	document.getElementById("reason").value=reason; 
	 
	return true;
}
</script>
<style type="text/css">
#textcolor
{
color: maroon;
font-size: small;
}
</style>
</head>
<body>
<div align="center">
 

<s:form action="editpindatapinMailerConfigurationAction"  name="pinmailerdataaddform" namespace="/" autocomplete="off">
	<s:if test="%{pinmaildata_status =='Y'}">
		<table border="0" cellpadding="0" cellspacing="4" width="50%" >
		<s:iterator  value="pinmailername">
			<tr>
			<td colspan="2">
				<table border='0' class="formtable" cellspacing='0' cellpadding='0' width='65%'>
						<s:hidden name="reason" id="reason"  />
						 
						
							<tr>
								<td>Config/Edited by: </td>
								<td class="textcolor">  ${ADDED_BY} </td>
							 
								<td>Configured date : </td>
								<td class="textcolor">   ${ADDED_DATE}  </td>
							</tr>
							<tr>
								<td>Auth/De-auth by </td>
								<td class="textcolor">  ${AUTH_BY} </td>
							 
								<td>Authorized/Deauth date  </td>
								<td class="textcolor">  ${AUTH_DATE} </td>
							</tr>
							<tr>
								<td>Status </td>
								<td class="textcolor">  ${AUTH_CODE} </td>
							 
								<td>Remarks </td>
								<td class="textcolor">  ${REMARKS}</td>
							</tr>
				</table>
			</td>
			</tr>
		</s:iterator>
		<s:iterator  value="pinmailsdata"> 
			
			<input type="hidden" name="mailerid" id="mailerid" value="${PINMAILER_ID}"> 
			<tr>
				<td>Pin Mailer Name</td>
				<td id="textcolor"><s:iterator value="pinmailername">${PINMAILER_NAME}</s:iterator></td>
			</tr>
		    <tr>
				<td>Form Document Type</td>
				<td id="textcolor">
					${DOCUMENT_TYPE}
				</td>
			</tr>
			<tr>
	    		<td>Pin Mailer Height [ Max-X ]</td>
				<td id="textcolor">${MAILER_HEIGHT}</td>
			</tr>
			<tr>
				<td>Pin Mailer Lenght [ Max-X ]</td>
				<td id="textcolor">${MAILER_LENGHT}</td>
			</tr>
		</s:iterator>

		</table>
			 <table border="1" cellpadding="0" cellspacing="2" width="65%">
				 	<tr>
				 		<th>Field Name</th><th>Field Length</th><th>X-Position</th><th>Y-Position</th><th>Print Required</th>
				 	</tr>
				 	<s:iterator  value="pinmailerdata">
				 	<%int i=0;%>
					 	<tr align="center"> 
									<td>
										${FIELD_NAME}
									</td>								 
									<td> 
										${FIELD_LENGTH} 
									</td>								
									<td> 
										${X_POS} 
									</td>															
									<td>
										 ${Y_POS} 
									</td>
									<td>
										${PRINT_REQUIRED}
									</td>
						</tr>
					
			 		</s:iterator>						 	
			</table>
			11111111111111111<s:property value="editenabled"/>
			 <s:if test="%{editenabled=='YES'}">
					<s:submit name="editpinmailer" value="Edit"/> 
				<input type="button" value="Back" name="submit" id="submit" onclick="goBack()"/>
			</s:if>
		</s:if>
		<s:else>
					NO VALUES FOUND
		</s:else>
		<br><br>
</s:form>		
</div>
</body>
</html>