<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">


function selectall()
{
	valid=true;
	var selectbox = document.getElementById("glcode");
	if(selectbox.value=="-1")
	{
		errMessage(glcode,"SELECT GL GROUP");
		return false;
	}
	return valid;
}


</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="viewcardglGLConfigure.do" method="post" autocomplete="off">
		<div align="center">
				<table align="center" border="0"  cellspacing="1" cellpadding="0" width="40%" class="formtable">
					<tr>
						<td align="right">SELECT GL DESC:</td>
						<td>
							<select name="glcode" id="glcode" >
									<option value="-1">-SELECT GLGROUP-</option>
									<s:iterator value="glcard">
										<option value="${GL_CODE}">${GL_NAME}</option>
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
	</s:form>