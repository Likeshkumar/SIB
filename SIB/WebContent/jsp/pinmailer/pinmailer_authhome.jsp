<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript">
function delConfirm()
{
var r=confirm("Are You Sure 'Delete'");
return r;
	
}
function selectall()
{
	valid=true;
	var selectbox = document.getElementById("mailerid");
	if(selectbox.value=="-1")
	{
		errMessage(mailerid,"SELECT PINMAILER");
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
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>

<form action="authPinMailerDatapinMailerConfigurationAction.do" autocomplete="off" method="post">
	<div align="center"> 
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="50%" class="formtable">
				<tr>
					<td align="right">SELECT PINMAILER:</td>
					<td>
						<select name="mailerid" id="mailerid">
									<option value="-1">-SELECT PIN MAILER-</option>
								<s:iterator value="pinmailerlist">
									<option value="${MAILERID}">${MAILERNAME}</option>
								</s:iterator>
						</select>
					</td>
				</tr>
			</table>
			<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
				<tr align="center">
					<td>
					<s:submit value="View"  onclick="return selectall()"></s:submit>
					</td>
				</tr>
			</table>
	</div>
</form>
</body>
</html>