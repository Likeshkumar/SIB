<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript">
function discount()
{
	var discountname = document.getElementById("comissionname");
	var merchant = document.getElementById("merchant");
	if(merchant)
	{
		if(merchant.value=="-1")
		{
			errMessage(merchant,"Enter merchant NAme");
			return false;
		}
	}
	if(discountname)
	{
		if(discountname.value=="-1")
		{
			errMessage(discountname,"Enter Commission NAme");
			return false;
		}
	}
	
}

function getMerchantList()
{
	var comissionname = document.getElementById("comissionname").value;
	var url = "getMerchMerchantProcess.do?comissionname="+comissionname; 
	var result = AjaxReturnValue(url); 
	document.getElementById("glname").innerHTML = result;
	
}
</script>
</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
	<form action="commsionDetailsMerchantProcess.do" autocomplete="off" method="get" name="merchdiscount">
		<table>
		<tr>
			<td>Commission Name</td>
			<td>: <s:select list="%{Marchantlist}" id="comissionname" name="comissionname"
						listKey="COMMISSION_CODE" listValue="COMMISSION_DESC" headerKey="-1"
						headerValue="-SELECT-"/>
			</td>
		</tr>
	<!--
		<tr>
			<td> GL Name</td>
			<td>: <select name="glname" id="glname">
					<option value="-1">--Select --</option>

			</select>
			</td>
		</tr>
		 -->
		<tr><td></td><td><s:submit value="View" onclick="return discount()"/></td></tr>

		</table>
	</form>
</div>
</body>
</html>