<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
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
					String MsgTranslationAction_error_message = null;
					MsgTranslationAction_error_message = (String) session.getAttribute("viewMsgTranslationAction_error_message");
					session.removeAttribute("viewMsgTranslationAction_error_message");
						
					String MsgTranslationAction_error_status = null;
					MsgTranslationAction_error_status = (String) session.getAttribute("viewMsgTranslationAction_error_status");
					session.removeAttribute("viewMsgTranslationAction_error_status");
				%>
				<%
					if (MsgTranslationAction_error_message != null) 
					{
				%>
					<font color="Red"><b><%=MsgTranslationAction_error_message%></b></font>
				<%
					} 
				%>
				
				<% 
					if(!(MsgTranslationAction_error_status=="E"))
					{
				%>
				
				<table border='1' cellpadding='0' cellspacing='0' width='50%'>
						<tr bgcolor="#88D4D8">
							<th>Message Translation Id</th>
							<th>Message Translation Description</th>
							<th> Delete </th>
						</tr>
					<s:iterator value="message_translation_list">
						<tr> 
							<td>&nbsp;${id} </td> 
							<td>&nbsp;${desc} </td>
							<td>  <a href='deleteMsgTranslationAction.do?MSG_TRANSLATE_ID=${id}'   onclick='return confDel()'> <img src='images/delete.png' alt='Delete' /> </a>  </td>    
						</tr>	
					</s:iterator>
				</table>
				<%
					}
				%>
</div>