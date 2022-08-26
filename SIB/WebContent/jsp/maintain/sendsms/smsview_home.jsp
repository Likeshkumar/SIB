<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>

	 function validateRecords(){
		 var cardno = document.getElementById("cardno");
		 
		 if( acctno.value != reacctno.value ){
			 errMessage(acctno, "Account Number and Re-Entered Account number is not valid..."); return false; 
		 }
		 
		  
	 }
	
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="viewSMSContentSMSSender.do" name="smsform" method="get"
	onsubmit="return validateRecords()" autocomplete="off" namespace="/">
 
		<table border="0" cellpadding="0" cellspacing="4" width="40%" class="formtable" 	align="center">
		 
			<tr> 
				<td>Select SMS Subject </td>
				 <td> : <s:select name="bulksmscode" id="bulksmscode" list="%{smsbean.smslist}"   listKey="BULKSMSCODE" listValue="SUBJECT"  headerKey="-1" headerValue="-SELECT-" /></td>
			</tr>  
			 
			
			<tr>
				<table border="0" cellpadding="0" cellspacing="0" width="20%" >
					<tr>
						<td><s:submit   name="btnname" id="btnname" value="View" /></td>
						<td><input type="button" name="cancel" id="cancel"
							value="Cancel" class="cancelbtn"
							onclick="return confirmCancel()" /></td>
					</tr>
				</table>
			</tr>


		</table>
		 

</s:form>

