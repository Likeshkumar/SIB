<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<style type="text/css">
td {
height:20px;

text-align:center;
vertical-align:bottom;
}
</style>
<script>
function confDel(){
		var valid = false;
		if ( confirm('Do you want to Delete ? ') ){
			valid = true;	
		}
		return valid;	
	}
</script>
<div align="center">		
				<%
						
					String viewAuthParamAction_error_message = null;
					viewAuthParamAction_error_message = (String) session.getAttribute("viewAuthParamAction_error_message");
					session.removeAttribute("viewAuthParamAction_error_message");
						
					String viewAuthParamAction_error_status = null;
					viewAuthParamAction_error_status = (String) session.getAttribute("viewAuthParamAction_error_status");
					session.removeAttribute("viewAuthParamAction_error_status");
				%>
				<%
					if (viewAuthParamAction_error_message != null) 
					{
				%>
					<font color="Red"><b><%=viewAuthParamAction_error_message%></b></font>
				<%
					} 
				%>
				
				<% 
					if(!(viewAuthParamAction_error_status=="E"))
					{
						
				%>
				
				<table border='1' cellpadding='0' cellspacing='0' width='50%'>
						<tr bgcolor="#88D4D8">
							<th>INST GROUP</th>
							<th>CHANNEL TYPE</th>
							<th>CHANNEL NAME</th>
							<th>DEVICE TYPE</th>
							<th>MSG_TYPE</th>
							<th>TXN_CODE</th>
							<th>RESP_CODE</th>
							<th>CARD_TYPE</th>
							<th>AUTH_SCHEME_CODE</th>
							<th>ISSUER_BIN</th>
							<th>ACQUR_BIN</th>
							<th>RE1</th>
							<th>RE1_VAL</th>
							<th>RE2</th>
							<th>RE2_VAL</th>
							<th>AUTH_PROCESS_ID</th>
						</tr>
					<s:iterator value="auth_param_list">
						<tr> 
							<td>&nbsp;${INST_GROUP} </td> 
							<td>&nbsp;${CHANNEL_TYPE} </td>
							<td>&nbsp;${CHANNEL_NAME}</td>
							<td>&nbsp;${DEVICE_TYPE}</td>
							<td>&nbsp;${MSG_TYPE}</td>
							<td>&nbsp;${TXN_CODE}</td>
							<td>&nbsp;${RESP_CODE}</td>
							<td>&nbsp;${CARD_TYPE}</td>
							<td>&nbsp;${AUTH_SCHEME_CODE}</td>
							<td>&nbsp;${ISSUER_BIN}</td>
							<td>&nbsp;${ACQUR_BIN}</td>
							<td>&nbsp;${RE1}</td>
							<td>&nbsp;${RE1_VAL}</td>
							<td>&nbsp;${RE2}</td>
							<td>&nbsp;${RE2_VAL}</td>
							<td>&nbsp;${AUTH_PROCESS_ID}</td>
							<!-- <td>  <a href='deleteMsgTranslationAction.do?MSG_TRANSLATE_ID=${INST_GROUP}'   onclick='return confDel()'> <img src='images/delete.png' alt='Delete' /> </a>  </td>  -->    
						</tr>	
					</s:iterator>
				</table>
				
				<%
					}
				%>
</div>