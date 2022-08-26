<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
		function check_AuthParam_form()
		{
			 
					var INST_GROUP=document.getElementById("INST_GROUP");
					var CHANNEL_TYPE=document.getElementById("CHANNEL_TYPE");
					var CHANNEL_NAME=document.getElementById("CHANNEL_NAME");
					var DEVICE_TYPE=document.getElementById("DEVICE_TYPE");
					var MSG_TYPE=document.getElementById("MSG_TYPE");
					var TXN_CODE=document.getElementById("TXN_CODE");
					var RESP_CODE=document.getElementById("RESP_CODE");
					var CARD_TYPE=document.getElementById("CARD_TYPE");
					var AUTH_SCHEME_CODE=document.getElementById("AUTH_SCHEME_CODE");
					var ISSUER_BIN=document.getElementById("ISSUER_BIN");
					var ACQUR_BIN=document.getElementById("ACQUR_BIN");
					var RE1=document.getElementById("RE1");
					var RE1_VAL=document.getElementById("RE1_VAL");
					var RE2=document.getElementById("RE2");
					var RE2_VAL=document.getElementById("RE2_VAL");
					var AUTH_PROCESS_ID=document.getElementById("AUTH_PROCESS_ID");
					if(INST_GROUP.value=="")
				 		{
				 			alert("' Institution Group ' Cannot Be Empty");
				 			INST_GROUP.focus();
				 			return false;
				 		}
				 	if(CHANNEL_TYPE.value=="")
				 		{
				 			alert("' Channel Type ' Cannot Be Empty");
				 			CHANNEL_TYPE.focus();
				 			return false;
				 		}
				 	if(CHANNEL_NAME.value=="")
				 		{
				 			alert("' Channel Name ' Cannot Be Empty");
				 			CHANNEL_NAME.focus();
				 			return false;
				 		}
					if(DEVICE_TYPE.value=="")
			 		{
			 			alert("' Device Type ' Cannot Be Empty");
			 			DEVICE_TYPE.focus();
			 			return false;
			 		}
				 	if(MSG_TYPE.value=="")
			 		{
			 			alert("' Message Type ' Cannot Be Empty");
			 			MSG_TYPE.focus();
			 			return false;
			 		}
				 	if(TXN_CODE.value=="")
			 		{
			 			alert("' Txn Code ' Cannot Be Empty");
			 			TXN_CODE.focus();
			 			return false;
			 		}
				 	if(RESP_CODE.value=="")
			 		{
			 			alert("' Response Code ' Cannot Be Empty");
			 			RESP_CODE.focus();
			 			return false;
			 		}
				 	if(CARD_TYPE.value=="")
			 		{
			 			alert("' Card Type ' Cannot Be Empty");
			 			CARD_TYPE.focus();
			 			return false;
			 		}
				 	if(AUTH_SCHEME_CODE.value=="")
			 		{
			 			alert("' Auth Scheme Code ' Cannot Be Empty");
			 			AUTH_SCHEME_CODE.focus();
			 			return false;
			 		}
				 	if(ISSUER_BIN.value=="")
			 		{
			 			alert("' Issuer Bin ' Cannot Be Empty");
			 			ISSUER_BIN.focus();
			 			return false;
			 		}
				 	if(ACQUR_BIN.value=="")
			 		{
			 			alert("' Acquirer Bin ' Cannot Be Empty");
			 			ACQUR_BIN.focus();
			 			return false;
			 		}
				 	if(RE1.value=="")
			 		{
			 			alert("'Regular Expression 1 ' Cannot Be Empty");
			 			RE1.focus();
			 			return false;
			 		}
				 	if(RE1_VAL.value=="")
			 		{
			 			alert("' Regular Expression 1 Value ' Cannot Be Empty");
			 			RE1_VAL.focus();
			 			return false;
			 		}
				 	if(RE2.value=="")
			 		{
			 			alert("' Regular Expression 2 ' Cannot Be Empty");
			 			RE2.focus();
			 			return false;
			 		}
					if(RE2_VAL.value=="")
			 		{
			 			alert("' Regular Expression 2 Value ' Cannot Be Empty");
			 			RE2_VAL.focus();
			 			return false;
			 		}
					if(AUTH_PROCESS_ID.value=="-1")
			 		{
			 			alert("Please Select ' Auth Process List ' ");
			 			// AUTH_PROCESS_ID.options[0].focus();
			 			AuthParam_form.AUTH_PROCESS_ID.focus();
			 			return false;
			 		}
					
				 
				 	//alert("return true");
			var conform_flag=confirm("Do you Want To Submit");
			if(conform_flag)
				{
				 	return true;
				}
			else
				{
					return false;
				}
		}
</script>
		
		
				<%
					String AuthParamAction_error_message = null;
					AuthParamAction_error_message = (String) session.getAttribute("AuthParamAction_error_message");
					session.removeAttribute("AuthParamAction_error_message");
						
					String AuthParamAction_error_status = null;
					AuthParamAction_error_status = (String) session.getAttribute("AuthParamAction_error_status");
					session.removeAttribute("AuthParamAction_error_status");
				%>
				<%
					if (AuthParamAction_error_message != null) 
					{
				%>
					<s:div align="center">		
					<font color="Red"><b><%=AuthParamAction_error_message%></b></font>
					</s:div>
				<%
					} 
				%>
				
				<% 
					if(!(AuthParamAction_error_status=="E"))
					{
				%>
					
				<s:div align="center" >
				<s:form name="AuthParam_form" action="saveDataAuthParamAction" autocomplete="off">
				<table border="0" width="50%">
					<tr>
						<td>&nbsp;
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Institution Group</b>
						</td>
						<td>
							:<s:textfield name="INST_GROUP" id="INST_GROUP" maxlength="8" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Channel Type</b>
						</td>
						<td>
							:<s:textfield name="CHANNEL_TYPE" id="CHANNEL_TYPE" maxlength="16" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Channel Name</b>
						</td>
						<td>
							:<s:textfield name="CHANNEL_NAME" id="CHANNEL_NAME" maxlength="32" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Device Type</b>
						</td>
						<td>
							:<s:textfield name="DEVICE_TYPE" id="DEVICE_TYPE" maxlength="32" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Message Type</b>
						</td>
						<td>
							:<s:textfield name="MSG_TYPE" id="MSG_TYPE" maxlength="4" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Txn Code</b>
						</td>
						<td>
							:<s:textfield name="TXN_CODE" id="TXN_CODE" maxlength="6" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Response Code</b>
						</td>
						<td>
							:<s:textfield name="RESP_CODE" id="RESP_CODE" maxlength="3" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Card Type</b>
						</td>
						<td>
							:<s:textfield name="CARD_TYPE" id="CARD_TYPE" maxlength="16" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Auth Scheme Code</b>
						</td>
						<td>
							:<s:textfield name="AUTH_SCHEME_CODE" id="AUTH_SCHEME_CODE" maxlength="16" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Issuer Bin</b>
						</td>
						<td>
							:<s:textfield name="ISSUER_BIN" id="ISSUER_BIN" maxlength="16" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Acquire Bin</b>
						</td>
						<td>
							:<s:textfield name="ACQUR_BIN" id="ACQUR_BIN" maxlength="16" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Regular Exp 1</b>
						</td>
						<td>
							:<s:textfield name="RE1" id="RE1" maxlength="64" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Regular Exp1 Value</b>
						</td>
						<td>
							:<s:textfield name="RE1_VAL" id="RE1_VAL" maxlength="128" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Regular Exp 2</b>
						</td>
						<td>
							:<s:textfield name="RE2" id="RE2" maxlength="64" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Regular Exp2 Value</b>
						</td>
						<td>
							:<s:textfield name="RE2_VAL" id="RE2_VAL" maxlength="128" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Auth Process List</b>
						</td>
						<td>
							:<s:select name="AUTH_PROCESS_ID" id="AUTH_PROCESS_ID" list="AuthClassList" 
							 listKey="encypted_proc_id" listValue="decypted_proc_id"
							 headerKey="-1" headerValue="Select Auth Process List"
							 />
							
						</td>
					</tr>
					<tr>
						<td>&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<s:submit name="submit" value="Submit" onclick="return check_AuthParam_form();"></s:submit>
						</td>
					</tr>
				</table>
				</s:form>
				</s:div>
				
				<%
					} 
				%>
	