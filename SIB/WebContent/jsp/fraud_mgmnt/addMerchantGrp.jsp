<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<SCRIPT language="javascript" >
function validation(){
	var merchname = document.getElementById("merchname");
	var merchgrp = document.getElementsByName("merchgrpdes");
	if(merchname.value==""){
		errMessage(merchname, "Please Enter the Merchant Group Name");
   	 	return false; 
	}
	if(merchgrp.value==""){
		errMessage(merchgrp, "Please Enter the Merchant Group Description");
   	 	return false; 
	}
	

}
</SCRIPT>
</head>
<body>
	<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="saveMerchantGrpFraudManagementAction.do" method="get" autocomplete="off">
		<table border="0" cellpadding="0" cellspacing="0" width="50%" align="center">
			<tr>
				<td>Merchant Name</td>
				<td>: </td><td>
				 	<input type='text' id="merchname"  name='merchname' maxlength="32"/>
				</td>
			</tr>
			<tr>
				<td>Merchant Group</td>
				<td>: </td><td>
				 	<textarea id="merchgrpdes" name="merchgrpdes" maxlength="64" > </textarea>
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