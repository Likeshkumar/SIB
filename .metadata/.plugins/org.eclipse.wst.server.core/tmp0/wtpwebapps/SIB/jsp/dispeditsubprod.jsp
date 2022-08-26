<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<%
	String subProdEditErrorstatus=null;
	String subProdEditMessage=null;
	subProdEditErrorstatus=(String) session.getAttribute("subProdEditErrorstatus");
	subProdEditMessage=(String) session.getAttribute("subProdEditMessage");
	
	String editsaveErrorStatus = null;
	String editsaveErrorMessage = null;
	editsaveErrorStatus = (String) session.getAttribute("editsaveErrorStatus");
	editsaveErrorMessage=(String) session.getAttribute("editsaveErrorMessage");
	session.removeAttribute("subProdEditErrorstatus");
	session.removeAttribute("subProdEditMessage");
	session.removeAttribute("editsaveErrorStatus");
	session.removeAttribute("editsaveErrorMessage");
	
	if(editsaveErrorStatus != null)
	{
%>
						
						<div align="center">
						<table border='0' cellpadding='0' cellspacing='0' width='40%' >
						<tr align="center">
							<td colspan="2">
								<font color="Red"><b><%=editsaveErrorMessage%></b></font>
							</td>
						</tr>
						</table>
						</div>
<%
	}
if(editsaveErrorStatus == "S" || editsaveErrorStatus == null)
{
			if(subProdEditErrorstatus =="E" )
			{
			%>
			<div align="center">
			<table border='0' cellpadding='0' cellspacing='0' width='40%' >
			<tr align="center">
				<td colspan="2">
					<font color="Red"><b><%=subProdEditMessage%></b></font>
				</td>
			</tr>
			</table>
			</div>
			<%
			}
			else
			{
			%>

						
	<div align="center">
	<s:iterator var="subproditr" value="prod_idlist">
	<br>
	<div align="center">
		<table border="0" width="80%">
			<tr>
				<td>
					<b>PRODUCT  :&nbsp;&nbsp;<font color="blue">${PRODUCT_NAME}</font></b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<b>BIN :&nbsp;&nbsp;<font color="blue">${BIN}</font></b>
					<input type="hidden" name="binno" value="${BIN}">
				</td>
			</tr>
		</table>
	</div>
	<table border="1" cellspacing="0" cellpadding="0" width="80%">
			<tr bordercolor="green">
				<th>SUB PRODUT NAME</th><th>PRODUCT ID</th><th>PRODUCT CODE</th><th>PERSONALIZED</th><th>RELOADABLE</th><th>ATM </th><th>SCHEME</th><th>CCY </th><th>EDIT</th></tr>
						<s:action name="generateSubprodlistAddSubProdAction" var="subpordbean">
							<s:param name="produtid">${PRODUCT_ID}</s:param>
							<s:param name="bin_id">${BIN}</s:param>
						</s:action>
						<s:iterator var="subprdlst" value="#subpordbean.subprod_list">
							<tr align="center">
								<td style="text-align:left">${PRODUCT_NAME}</td>
								<td>${PRODUCT_ID}</td><td>${PRODUCT_CODE}</td> 
								<td>${PERSONALIZED}</td><td>${RELOADABLE}</td>
								<td>${ATM_TRANS}</td>
								<td>${SCHEME}</td>
								<td>${CARD_CCY}</td>
								<td>
									<form action="editsubprodAddSubProdAction.do" method="post" autocomplete="off">
										<input type="image"  src="images/edit.png" alt="submit Button" onclick="return ask_confirm();">
										<input type="hidden" name="subprodid" value="${CARDTYPE_ID}">
										<input type="hidden" name="bin_value" value="${BIN}">
									</form>
								</td>
							</tr>				
						</s:iterator>
	</table>
	</s:iterator>
	</div>
<%
	}
}
%>
