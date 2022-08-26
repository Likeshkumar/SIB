<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function validationselect()
{
	var alertid = document.getElementById("alertid");
	 
	if(alertid)
	{
		if( alertid.value == "-1" ){
			errMessage(document.getElementById("alertid"), "Select Configuration");
			return false;
		} 
	}
	parent.showprocessing();
}

</script>
</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="editmailcontentdetailsAddMailAction.do" method="post" autocomplete="off">
	<table>
		<tr>
			<td colspan="2">
				<small class="dt">
					Select the module which u want to Edit mailer addresses and content.
				</small>
			</td>
		</tr>
		
		<tr>
			<td>Select Configuration:</td>
			<td>
				<s:select list="%{alertdetails}" id="alertid" name="alertid" listKey="ALERT_ID" listValue="DESCRIPTION"  headerKey="-1"  headerValue="-SELECT-"/>
			</td>	
		</tr>
			<tr>
			<td></td>
			<td><s:submit value="Next" onclick="return validationselect();"/></td>
			</tr>
	</table>
</s:form>
</body>
</html>