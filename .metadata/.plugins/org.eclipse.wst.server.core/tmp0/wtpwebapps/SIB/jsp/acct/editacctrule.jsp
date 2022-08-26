<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="updateAccountRuleHomeAcctRule.do" name="orderform" onsubmit="return validateAcctRules()" autocomplete="off" namespace="/">
	<s:iterator value="acc_details">	
		<table border="0" cellpadding="0" cellspacing="0" width="80%"
			align="center" class="filtertab">
			<s:hidden name="accruleid" id="accruleid" value="%{ACCTRULEID}"></s:hidden>
			<tr>
				<td>Message Type</td>
				<td>: <s:select list="%{msgtype}" id="msgtypeid"
						name="msgtypeid" listKey="MSGTYPE" listValue="MSGTYPE"
						headerKey="-1" headerValue="-SELECT-" value="%{MSGTYPE}" />
	
				</td>
	
				<td>Response Code</td>
				<td>: <s:select list="%{respcode}" id="respcode" name="respcode"
						listKey="RESPCODE" listValue="RESPCODE" headerKey="-1"
						headerValue="-SELECT-" value="%{RESPCODE}"/>
	
				</td>
	
			</tr>
	
	
			<tr>
	
	
				<td>Txn Code</td>
				<td>: <s:select list="%{txncode}" id="txncode" name="txncode"
						listKey="TXN_CODE" listValue="ACTION_DESC" headerKey="-1"
						headerValue="-SELECT-" value="%{TXNCODE}"/>
	
				</td>
	
	
				<td>Orgin ChannelF</td>
				<td>: <s:select list="%{orginchannel}" id="orginchannel"
						name="orginchannel" listKey="ORGIN_CHANNEL"
						listValue="ORGIN_CHANNEL" headerKey="-1" headerValue="-SELECT-" value="%{ORGINCHANNEL}"/>
	
				</td>
	
	
			</tr>
	
	
	
	
			<tr>
				<td>Device Type</td>
				<td>: <s:select list="%{devicetype}" id="devicetype"
						name="devicetype" listKey="DEVICETYPECODE" listValue="DEVICETYPE"
						headerKey="-1" headerValue="-SELECT-" value="%{DEVICETYPE}"/>
	
				</td>
	
				<td></td>
				<td></td>
			</tr>
	
	
		</table>
	
	
		<table border="0" cellpadding="0" cellspacing="4" width="20%">
			<tr>
				<td><s:submit value="Update" name="order" id="order"
						onclick="return validFilter()" /></td>
				<td><input type="button" name="cancel" id="cancel"
					value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />
	
				</td>
			</tr>
		</table>
	</s:iterator>	
</s:form>
