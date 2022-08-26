<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
<style type="text/css">
table,div {
   page-break-inside: avoid; 
}
</style>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 
<title>Card Production Report</title>
</head>
<h1 nowrap></h1>
<h1><%=(String)session.getAttribute("reportheader")%></h1>
<script>

</script>
<body >
<div align="right">
<img class='logo' src="images/orient/Azizi Logo.png" alt='Orient Bank'/>
</div>
<form name="Executeqry1" action=post>
<table>
<tr>    
<td>From Date:</td><td><%=(String)session.getAttribute("fromdate")%></td>
<td>To Date:</td><td><%=(String)session.getAttribute("todate")%></td>
</tr>
<tr>
<td>Generated with:</td><td><%=(String)session.getAttribute("reporttitle")%></td>
</tr>
</table>
<BR><BR>
<div style="page-break-inside:avoid !important; page-break-after:auto !important; overflow: hidden; display:block !important; width:100% ">
					<table width="100%" border=1 cellpadding=0 cellspacing=0 >
					<tr  >
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">SNO</font></th> 
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">BRANCH</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">ORDER REF NO</font></th> 
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">CARD NO</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">CUSTOMER ID</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">EMBOSING NAME</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">ACCOUNT NO</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">ACCOUNT FLAG</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">USER</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">ISSUED DATE</font></th>
					<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">STATUS</font></th>
					    
					
					<s:if test='condition != K'>
						<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">NEW CARD NO</font></th>
						<th bgcolor="#b3b8ff" ><font face="verdana" color="black" size="2">ADDON CARD</font></th>
					</s:if>
					</tr>
					<tr>
					<%int a = 1; %>
					<s:if test="!masterReportList.isEmpty()" >
					<s:iterator value="masterReportList"  status="stat">  
					<tr>
					
				<%-- 	<td ><%=a++ %></td> --%>
					<!--<s:property value="CARD_NO" /> -->
					        <td>${stat.index+1}</td>
							<td >${BRANCH_NAME}</td>
							<td >${ORDER_REF_NO}</td> 
							<td >${MCARD_NO}</td>
							<td >${CIN}</td>
							<td >${EMB_NAME}</td>
							<td >${ACCT_NO}</td>
							<td >${PC_FLAG}</td>
							<td >${MAKER_ID}</td>
							<td >${ISSUE_DATE}</td>
							<td >${CARDSTATUS}</td>
							<s:if test='condition != K'>
								<td >${NEW_CARD}</td>
								<td >${ADDON}</td>
							</s:if>
					</tr>   		
							</s:iterator>   
					</s:if>
					<s:else>
					<tr>
					<td colspan='7' style="text-align:center"> No Data Found
					</td>
					</tr>
					</s:else>		 
					</table>
					</div>

					    </form>

</body>
</html>


