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
<script>
function validation_authfee(subfeecode,feecode){
 	var auth = document.getElementById("auth0").value;
 	var reason = prompt('Enter the Reason for Reject?');
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

<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="feeamountDeauthorizeFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">
		<s:hidden name="feecode" id="feecode" value="%{feebean.masterfeecode}"/><s:hidden name="subfeecode" id="subfeecode" value="%{feebean.subfeecode}"/>		
		<table border='0' cellpadding='0' cellspacing='0' width='50%'> 
			<tr>
				<td colspan='4' style='padding-top:30px'>
					<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable">
						<tr> 
							<th>Fee Name</th>
							<th>Sub Fee Desc</th>										
						</tr>													
							<tr> 
								<td><s:property value="feebean.masterfeename"/></td>
								<td><s:property value="feebean.subfeename"/></td>		
							</tr>						 
					</table>
				</td>
			</tr>
			<tr>
				<td style='text-align:center'> 
					<!-- <input type="submit" name="auth" id="auth1" value="Authorize"/> -->
					<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authfee(${feebean.subfeecode},${feebean.masterfeecode})"/>
				</td>
			</tr>
		</table>
</s:form>
</body>
</html>