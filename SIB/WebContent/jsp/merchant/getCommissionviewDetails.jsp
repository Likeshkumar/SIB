<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
</head>
<form>
	<table border="0" cellpadding="0" cellspacing="6" class="formtable"> 

		<s:iterator value="mprbean.subcommission_list">
				<tr><th align="center" colspan="2"><b>${SUB_COM_DESC}</b></th></tr>
				<s:if test="%{mprbean.amtcnt=='N'}">
					<tr>
						<td style="color: brown">LOW TXN AMOUNT</td>
						<td style="color: brown">HIGH TXN AMOUNT</td>
					</tr>
					<tr>
						<td>${LOWTXNAMT}</td>
						<td>${HIGHTXNAMT}</td>
					</tr>
				</s:if>
			<%-- 	<s:if test="%{mprbean.amtcnt=='Y'}"> --%>
					<tr>
						<td style="color: brown">LOW TXN COUNT</td>
						<td style="color: brown">HIGH TXN COUNT</td>
					</tr>
					<tr>
						<td>${LOWTXNCNT}</td>
						<td>${HIGHTXNCNT}</td>
					</tr>
				<%-- </s:if>			 --%>
		</s:iterator>
		<tr>
			<td colspan="2">
			 
				 <table border="0" cellpadding="0" cellspacing="6" width="100%">
					 <tr>
						<th style="color: brown">TRANSACTION NAME</th>
						<th style="color: brown">COMMISSION AMOUNT</th>
						<th style="color: brown">COMMISSION MODE</th>
					</tr>
					<s:iterator value="mprbean.runningSchemeList">
						<tr>
							<td>${TXN_CODE}</td>
							<td>${COM_AMT}</td>
							<td>${COMMODE}</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</form>