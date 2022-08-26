<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<%
	String dispDelLocalErrorStatus=null;
	String dispDelLocalErrorMessage=null;
	String delsubProdError = null;
	String delsubProdErrorMessage = null;


	dispDelLocalErrorStatus = (String) session.getAttribute("dispDelLocalErrorStatus");
	dispDelLocalErrorMessage=(String) session.getAttribute("dispDelLocalErrorMessage");
	delsubProdError=(String) session.getAttribute("delsubProdError");
	delsubProdErrorMessage=(String) session.getAttribute("delsubProdErrorMessage");
	
	session.removeAttribute("dispDelLocalErrorStatus");
	session.removeAttribute("dispDelLocalErrorMessage");
	session.removeAttribute("delsubProdError");
	session.removeAttribute("delsubProdErrorMessage");
	
 
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
 
<div align="center">
	<s:iterator var="subproditr" value="prod_idlist">
	<br>
		<div align="center">
			<table border="0" width="95%">
				<tr>
					<td>
						<b>PRODUCT : <font color="blue">${PRODUCT_NAME}</font> </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>BIN :<font color="blue">${BIN}</font></b>
					</td>
				</tr>
			</table>
		</div>
	
		<table border="1" cellspacing="0" cellpadding="0" width="95%">
			<tr>
				<th>SUB PRODUT NAME</th>
				<th>PRODUCT ID</th>
				<th>PRODUCT CODE</th>
				<th>PERSONALIZED</th>
				<th>RELOADABLE</th>
				<th>ATM TRANSACTION</th>
				<th>SCHEME</th>
				<th>DELETE</th></tr>
					<s:action name="generateSubprodlistAddSubProdAction" var="subpordbean" >
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
							<td>
								<form action="delsubprodAddSubProdAction.do" method="post" autocomplete="off">
									<input type="image"  src="images/delete.png" alt="submit Button" onclick="return ask_confirm();">
									<input type="hidden" name="subprodid" value="${CARDTYPE_ID}">
									<input type="hidden" name="binnumber" value="${BIN}">
								</form>
							</td>
						</tr>				
					</s:iterator>
		</table>
	
	</s:iterator>
</div>
 		 
 
