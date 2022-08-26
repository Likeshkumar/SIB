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
					String viewAuthProcessListAction_error_message = null;
					viewAuthProcessListAction_error_message = (String) session.getAttribute("viewAuthProcessListAction_error_message");
					session.removeAttribute("viewAuthProcessListAction_error_message");
						
					String viewAuthProcessListAction_error_status = null;
					viewAuthProcessListAction_error_status = (String) session.getAttribute("viewAuthProcessListAction_error_status");
					session.removeAttribute("viewAuthProcessListAction_error_status");
				%>
				<%
					if (viewAuthProcessListAction_error_message != null) 
					{
				%>
					<font color="Red"><b><%=viewAuthProcessListAction_error_message%></b></font>
					<br><br>
				<%
					} 
				%>
				
				<% 
					if(!(viewAuthProcessListAction_error_status=="E"))
					{
				%>
				
				<table border='1' cellpadding='0' cellspacing='0' width='50%'>
						
						<tr bgcolor="#88D4D8">
							<th>Auth Process Id</th>
							<th>Message Translation </th>
							<th>Auth List </th>
							<th>Delete</th>
						</tr>
					<s:iterator value="AuthClassList">
						<tr>
							<td>&nbsp;${auth_proc_id} </td> 
							<td>&nbsp;${msg_transl_id} </td>
							<td>&nbsp;${auth_list} </td>
							<td>  <a href='deleteAuthProcessListAction.do?AUTH_PROCESS_ID=${auth_proc_id}'   onclick='return confDel()'> <img src='images/delete.png' alt='Delete' /> </a>  </td>    
						</tr>	
					</s:iterator>
					
				</table>
				<%
					}
				%>
</div>