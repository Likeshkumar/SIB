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
					var MSG_TRANSLATE_ID=document.getElementById("MSG_TRANSLATE_ID");
					var TRANSLATION_DESC=document.getElementById("TRANSLATION_DESC");
				 	if(MSG_TRANSLATE_ID.value=="")
				 		{
				 			alert("'Message Translation Id ' Cannot Be Empty");
				 			MSG_TRANSLATE_ID.focus();
				 			return false;
				 		}
				 	if(TRANSLATION_DESC.value=="")
			 		{
			 			alert("'Message Translation Description' Cannot Be Empty");
			 			TRANSLATION_DESC.focus();
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
					String MsgTranslationAction_error_message = null;
					MsgTranslationAction_error_message = (String) session.getAttribute("MsgTranslationAction_error_message");
					session.removeAttribute("MsgTranslationAction_error_message");
						
					String MsgTranslationAction_error_status = null;
					MsgTranslationAction_error_status = (String) session.getAttribute("MsgTranslationAction_error_status");
					session.removeAttribute("MsgTranslationAction_error_status");
				%>
				<%
					if (MsgTranslationAction_error_message != null) 
					{
				%>
					<s:div align="center">		
					<font color="Red"><b><%=MsgTranslationAction_error_message%></b></font>
					</s:div>
				<%
					} 
				%>
				
				<% 
					if(!(MsgTranslationAction_error_status=="E"))
					{
				%>
					
				<s:div align="center" >
				<s:form name="MessageTranslationPage" action="saveDataMsgTranslationAction" autocomplete="off">
				<table border="0" width="50%">
					<tr>
						<td>&nbsp;
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Message Translation Id</b>
						</td>
						<td>
							:<s:textfield name="MSG_TRANSLATE_ID" id="MSG_TRANSLATE_ID" maxlength="32" ></s:textfield>
						</td>
					</tr>
					<tr>
						<td align="left">
							<b>Message Translation Description</b>
						</td>
						<td>
							:<s:textfield name="TRANSLATION_DESC" id="TRANSLATION_DESC" maxlength="128"></s:textfield>
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
				
		