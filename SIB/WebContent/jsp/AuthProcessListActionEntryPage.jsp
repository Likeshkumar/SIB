<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@taglib uri="/struts-tags" prefix="s" %>
		<script type="text/javascript">
		function check_MessageTranslationPage()
		{
			var conform_flag=confirm("Do you Want To Submit");
			if(conform_flag)
				{
					var AUTH_PROCESS_ID=document.getElementById("AUTH_PROCESS_ID");
					var MSG_TRANSLATE_ID=document.getElementById("MSG_TRANSLATE_ID");
					var AUTH_LIST=document.getElementById("AUTH_LIST");
					
					if(AUTH_PROCESS_ID.value=="")
				 		{
				 			alert("' Auth Process Id ' Cannot Be Empty");
				 			AUTH_PROCESS_ID.focus();
				 			return false;
				 		}
				 	if(MSG_TRANSLATE_ID.value=="-1")
				 		{
				 			alert("Please Select 'Message Translation'");
				 			MSG_TRANSLATE_ID.focus();
				 			return false;
				 		}
				 	if(AUTH_LIST.value=="")
				 		{
				 			alert("' Auth List ' Cannot Be Empty");
				 			AUTH_LIST.focus();
				 			return false;
				 		}
				 	//alert("return true");
				 	return true;
				}
			else
				{
					return false;
				}
			
			
		}
</script>
		
		
				<%
					String AuthProcessListAction_error_message = null;
					AuthProcessListAction_error_message = (String) session.getAttribute("AuthProcessListAction_error_message");
					session.removeAttribute("AuthProcessListAction_error_message");
						
					String AuthProcessListAction_error_status = null;
					AuthProcessListAction_error_status = (String) session.getAttribute("AuthProcessListAction_error_status");
					session.removeAttribute("AuthProcessListAction_error_status");
				%>
				<%
					if (AuthProcessListAction_error_message != null) 
					{
				%>
					<s:div align="center">		
					<font color="Red"><b><%=AuthProcessListAction_error_message%></b></font>
					</s:div>
				<%
					} 
				%>
				
				<% 
					if(!(AuthProcessListAction_error_status=="E"))
					{
				%>
					
				<s:div align="center" >
				<s:form name="AuthProcessList_form" action="saveDataAuthProcessListAction" autocomplete="off">
				<table border="0" width="50%">
					<tr>
						<td>&nbsp;
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Auth Process Id</b>
						</td>
						<td>
						<s:action name="generateMessage_translation_listAuthProcessListAction">
							:<s:textfield name="AUTH_PROCESS_ID" id="AUTH_PROCESS_ID" maxlength="32" ></s:textfield>
						</s:action>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Message Translation</b>
						</td>
						<td>
							:<s:select name="MSG_TRANSLATE_ID"
							 id="MSG_TRANSLATE_ID" 
							 list="message_translation_list"
							 listKey="id"
							 listValue="desc"
							 maxlength="128"
							 headerKey="-1"
							 headerValue="Select Message Translation" />
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Auth List</b>
						</td>
						<td>
							:<s:textfield name="AUTH_LIST" id="AUTH_LIST" maxlength="128" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td>&nbsp;
						</td>
					</tr>
					<tr align="center">
						<td colspan="2">
							<s:submit name="submit" value="Submit" onclick="return check_MessageTranslationPage();"></s:submit>
						</td>
					</tr>
				</table>
				</s:form>
				</s:div>
				
				<%
					} 
				%>
	