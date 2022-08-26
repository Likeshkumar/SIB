<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript">
function confirmation()
{
var r=confirm("Are You Sure 'Delete'");
return r;
	
}
function selectall()
{
	valid=true;
	var selectbox = document.getElementById("currencycode");
	if(selectbox.value=="-1")
	{
		errMessage(currencycode,"SELECT CURRENCY NAME");
		return false;
	}
	return valid;
}
</script>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
<%
	String instid = (String)session.getAttribute("Instname"); 
 
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="viewCurrencyDetailCurrencyAction.do" method="post">	
	<div align="center">
		<table align="center" border="0"  cellspacing="1" cellpadding="0" width="90%">
			
				<tr>
					<td align="right">SELECT CURRENCY NAME :</td>
					<td>
						<select name="currencycode" id="currencycode">
							<option value="-1">SELECT CURRENCY NAME</option>
							<s:iterator  value="currencydetails">
							<option value="${CURRENCY_CODE}">${CURRENCY_DESC}</option>
							</s:iterator>	
						</select>
					</td>
				</tr>
			  
		</table>
		<table>
		<tr><td><s:submit value="View"  onclick="return selectall()"/></td></tr>
		</table>
	</div>
</form>
