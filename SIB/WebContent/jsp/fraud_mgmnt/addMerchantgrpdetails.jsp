<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<SCRIPT language="javascript" >
function validation(){
	var merchant = document.getElementById("merchantgrp");
	var merchid = document.getElementById("merchid");
	var merchname = document.getElementsByName("merchname");
	var merchno = document.getElementsByName("merchno");
	var merchdob = document.getElementsByName("merchdob");
	var merchadd = document.getElementsByName("merchadd");
	
	if(merchant.value=="-1"){
		errMessage(merchid, "Please Enter the Merchant Group Name");
   	 	return false; 
	}
	if(merchid.value==""){
		errMessage(merchid, "Please Enter the Merchant id");
   	 	return false; 
	}
	if(merchname.value==""){
		errMessage(merchname, "Please Enter the Merchant Name ");
   	 	return false; 
	}
	if(merchno.value==""){
		errMessage(merchno, "Please Enter the Mobile No ");
   	 	return false; 
	}
	if(merchdob.value==""){
		errMessage(merchno, "Please Enter the Date of Birth");
   	 	return false; 
	}
	if(merchadd.value==""){
		errMessage(merchno, "Please Enter the Merchant address");
   	 	return false; 
	}
	

}
</SCRIPT>
</head>
<body>
	<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="saveMerchantFraudManagementAction.do"  autocomplete="off" name="merchant" method="get">
		<table border="0" cellpadding="0" cellspacing="0" width="50%" align="center">
			<tr>
				<td>Merchant Group Name</td>
				<td>: </td><td>
				 	<s:select list="%{merchgrplist}" id="merchantgrp" name="merchantgrp"
						listKey="MERCHGRPID" listValue="MERCHANTGRP" headerKey="-1"
						headerValue="-SELECT-"/>
				</td>
			</tr>
			<tr>
				<td>Merchant ID</td>
				<td>: </td><td>
				 	<input type='text' id="merchid" name="merchid" maxlength="32" onKeyPress="return numerals(event);"/> 
				</td>
			</tr>
			<tr>
				<td>Merchant Name </td>
				<td>: </td><td>
				 	<input type='text' id="merchname" name="merchname" maxlength="32" /> 
				</td>
			</tr>
			<tr>
				<td>Mobile No</td>
				<td>: </td><td>
				 	<input type='text' id="merchno" name="merchno" maxlength="16" onKeyPress="return numerals(event);"/> 
				</td>
			</tr>
			<tr>
				<td>Date of Birth</td>
				<td>: </td><td>
				 	<input type='text' id="merchdob" name="merchdob" maxlength="64" /> 
				 	<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.merchant.merchdob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
				</td>
			</tr>
			<tr>
				<td>ADDRESS</td>
				<td>: </td><td>
				 	<textarea id="merchadd" name="merchadd" maxlength="64" > </textarea>
				</td>
			</tr>
			
			<tr>
				<td>
					<table align="center">
					 	<tr>
							<td></td>
							<td><s:submit value="Submit" onclick="return validation()"/>
							&nbsp;<input type="button" onclick="return confirmCancel()" class="cancelbtn" value="Cancel" id="cancel" name="cancel"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>