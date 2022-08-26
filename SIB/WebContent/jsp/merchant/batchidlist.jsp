<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
 
	function Mapping( batchname )
	{
		var url = "generateReportMerchantReports.do?batchname="+batchname;
		result = AjaxReturnValue(url);  
		document.getElementById("prefilename").innerHTML = result; 
	}
</script>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String merchname = (String)session.getAttribute("merchname");
	String merchid = (String)session.getAttribute("merchid"); 

 	//out.println(merchname);
%>
<s:if test="%{batchtype=='open'}">
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="100%" cellspacing="0"  >
	<tr>
		<td class="textcolor" align="center"><b>BATCHID</b></td>
		<td class="textcolor" align="center"><b>TERMID</b></td>
		<td class="textcolor" align="center"><b>OPENDATE</b></td>
		<td class="textcolor" align="center"><b>OPENTIME</b></td>
		<td class="textcolor" align="center"><b>SALECOUNT</b></td>
		<td class="textcolor" align="center"><b>SALEAMOUNT</b></td>
	</tr>
	
	<s:iterator value="batchlist">
	<tr>
		<td align="center">
			<!-- <a href="generateReportMerchantReports.do" style="color: black;">${BATCH_ID}</a> -->
							
			<form action="generateBatchReportMerchantReports.do" method="post" autocomplete="off"> 
				<input type="submit" name="batchid" id="batchid" value="${BATCHID}">
				<input type="hidden" name="terminalid" id="terminalid" value="${TERMID}"/>
				<input type="hidden" name="batch_type" id="batch_type" value="open"/>
			</form>
		</td>
		<td align="center">
		${TERMID}
		</td>
		<td align="center">
		${OPENDATE}
		</td>
		<td align="center">
		${OPENTIME}
		</td>
		<td align="center">
		${SALECOUNT}
		</td>
		<td align="center">
		${SALEAMOUNT}
		</td>
	</tr>
	</s:iterator>	
</table>
<table>
	<tr><td><div id="prefilename" style="width:600px;height:150px;">
			 &nbsp;
	</div></td></tr>
</table>
</s:if>
<s:else >
<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="100%" cellspacing="0"  >
	<tr>
		<td class="textcolor" align="center"><b>BATCHID</b></td>
		<td class="textcolor" align="center"><b>TERMID</b></td>
		<td class="textcolor" align="center"><b>OPENDATE</b></td>
		<td class="textcolor" align="center"><b>OPENTIME</b></td>
		<td class="textcolor" align="center"><b>CLOSEDATE</b></td>
		<td class="textcolor" align="center"><b>CLOSETIME</b></td>		
		<td class="textcolor" align="center"><b>HOSTSALEAMOUNT</b></td>
		<td class="textcolor" align="center"><b>TERMSALEAMOUNT</b></td>
		<td class="textcolor" align="center"><b>MATCHFLAG </b></td>
	</tr>
	
	<s:iterator value="batchlist">
	<tr>
		<td align="center">
			<!-- <a href="generateReportMerchantReports.do" style="color: black;">${BATCH_ID}</a> -->
							
			<form action="generateBatchReportMerchantReports.do" method="post" autocomplete="off"> 
				<input type="submit" name="batchid" id="batchid" value="${BATCHID}">
				<input type="hidden" name="terminalid" id="terminalid" value="${TERMID}"/>
				<input type="hidden" name="batch_type" id="batch_type" value="close"/>
			</form>
		</td>
		<td align="center">
		${TERMID}
		</td>
		<td align="center">
		${opendate}
		</td>
		<td align="center"> 
		${opentime}
		</td>
		<td align="center">
		${CLOSEDATE}
		</td>
		<td align="center">
		${CLOSETIME}
		</td>
		<td>
		${HOSTSALEAMOUNT}
		</td>
		<td align="center">
		${TERMSALEAMOUNT}
		</td>
		<td align="center">
		${MATCHFLAG}
		</td>
	</tr>
	</s:iterator>	
</table>
<table>
	<tr><td><div id="prefilename" style="width:600px;height:150px;">
			 &nbsp;
	</div></td></tr>
</table>
</s:else>

		

</body>
</html>