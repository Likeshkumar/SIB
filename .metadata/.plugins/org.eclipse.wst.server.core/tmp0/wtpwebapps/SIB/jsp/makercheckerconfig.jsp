<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="insertmkckrUserManagementAction" autocomplete="off">
<div align="center">
	<table border='0' cellpadding='0' cellspacing='0' width='60%' align="center" >
			<s:iterator  value="mkrchkrmenu">
			<tr>
			                <td>${MENUNAME}</td>
						    <td align="center">
							<select name="mainmenu">
						    <option value="E-${MENUID}">Enabled</option>
						    <option value="D-${MENUID}">Disabled</option>
						    </select>
							</td>
					        
			</tr>
			</s:iterator>
			<tr>
				<td align="center" colspan="3">
					<s:submit name="submitprofile" id="submitprofile" value="Save"/>
				</td>
			</tr>
	</table> 
</div>
</s:form>

