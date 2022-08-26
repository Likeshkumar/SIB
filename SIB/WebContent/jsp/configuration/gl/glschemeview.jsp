<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">
function selectall()
{
	valid=true;
	var selectbox = document.getElementById("schemegl");
	if(selectbox.value=="-1")
	{
		errMessage(schemegl,"SELECT SCHEME GL");
		return false;
	}
	return valid;
}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="viewschemeGLConfigure.do" method="post" autocomplete="off"> 
	<table align="center" border="0"  cellspacing="1" cellpadding="0" width="40%" class="formtable">
		<tr>
			<td>SELECT SCHEME GL</td>
			<td>
				<select name="schemegl" id="schemegl">
					<option value="-1">-SELECT SCHEME GL-</option>
					<s:iterator  value="schemenamelist">
					<option value="${SCH_CODE}">${SCH_NAME}</option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<s:submit value="View"  onclick="return selectall()"/>
			</td>
		</tr>
	</table>
</form>