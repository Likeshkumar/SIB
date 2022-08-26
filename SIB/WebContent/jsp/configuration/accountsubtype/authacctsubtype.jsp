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
function editSubfee( subfeeid,masterfeecode ){	 
	//var masterfeecode = document.getElementById("masterfeecode"); 
	alert(" masterfeecode "+masterfeecode);
	var url = "editSubFeeHomeFeeConfig.do?masterfeecode="+masterfeecode+"&subfeecode="+subfeeid+"&flag="+"Auth";
	if( confirm( "Do you want to continue...") ){
		window.location=url;
		parent.showprocessing();
	}else{
		return false;
	}
}

function validation_deauthcardtype(){
	//alert("Hai");
	var acctsubtypeid = document.getElementById("acctsubtypeid");
 	var auth = document.getElementById("auth0").value;
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		// alert("EMPTY");
		 var url = "authdeauthacctsubtypeAccountSubTypeAction.do?reason="+reason+"&auth="+auth+"&acctsubtypeid="+acctsubtypeid.value;
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
.bordercl td
{
  border:1px solid #CCCCCC;
}
 .th{
 	align:center;
 }
</style>

</head>

<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form  name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/" action ="authdeauthacctsubtypeAccountSubTypeAction.do">
		<table border='0' cellpadding='0' cellspacing='0' width='90%'> 
			<tr>
				<td colspan='4' style='padding-top:30px'>
					<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
						<tr> 
							<th style="text-align:center">ACCT TYPE ID</th>
							<th style="text-align:center">ACCT SUB TYPE ID</th>
							<th style="text-align:center">ACCT SUB TYPE DESC</th>		
							<th style="text-align:center">CONFIGURED BY</th>														
						</tr>
						<% int rowcnt = 0; Boolean alt=true; %> 
						<s:iterator value="acctsubtypelist">	
							<s:hidden name="acctsubtypeid" id="acctsubtypeid" value="%{ACCTSUBTYPEID}"></s:hidden>					
							<tr	<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>	> 
								<td>${ACCTTYPEID}</td>
								<td>${ACCTSUBTYPEID}</td>
								<td>${ACCTSUBTYPEDESC}</td>
								<td>${ADDEDBY}</td>
							</tr>
						</s:iterator> 
					</table>
				</td>
			</tr>
			<tr>
				<td colspan='1' style='text-align:center'> 
					<input type="submit" name="auth" id="auth1" value="Authorize" />
					<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_deauthcardtype()"/>
				</td>
			</tr>
		</table>		
</s:form>
</body>
</html>