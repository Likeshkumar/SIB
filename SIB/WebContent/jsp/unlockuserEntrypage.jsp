<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
function form_check(){
		var instname=document.unlockuserform.instname.value;
		var username=document.unlockuserform.username.value;
		var password=document.unlockuserform.password.value;
		if(instname=="-1")
			{
				alert("Please Select The Institution");
				return false;
			}
		if(username=="")
			{
				alert("User Name Cannot Be Empty");
				return false;
			}
		if(password=="")
			{
				alert("Password Cannot Be Empty");
				return false;
			}
		return true;
}
</script>
<div align="center">
<table border='0' width='40%'>
		<tr>
			<td colspan='2'><font color='blue'><b>If You Have Not Logged Out Properly During You </b></font></td>
		</tr>
		<tr>	
			<td colspan='2' align="center"><font color='blue'><b>Last Login, You Can Logout From Here</b></font></td>
		</tr>
		<tr>
			<td colspan='2'>&nbsp;</td>
		</tr>
</table>
</div>
				<%
					String UnlockUserViewStatus=null;
					String unlockUserViewMessage=null;
					UnlockUserViewStatus=(String) session.getAttribute("UnlockUserViewStatus");
					unlockUserViewMessage=(String) session.getAttribute("unlockUserViewMessage");
					String checkuser_unlock_message = null;
					String checkuser_unlock_Error = null;
					checkuser_unlock_message = (String) session.getAttribute("checkuser_unlock_message");
					checkuser_unlock_Error=(String) session.getAttribute("checkuser_unlock_Error");
					session.removeAttribute("UnlockUserViewStatus");
					session.removeAttribute("unlockUserViewMessage");
					session.removeAttribute("checkuser_unlock_message");
					session.removeAttribute("checkuser_unlock_Error");
					if (checkuser_unlock_Error != null ) 
					{
						%>
						<div align="center">
						<table border='0' cellpadding='0' cellspacing='0' width='40%' >
						<s:form name="UserViewStatus" autocomplete="off">
						<tr align="center">
							<td colspan="2">
								<font color="Red"><b><%=checkuser_unlock_message%></b></font>
							</td>
						</tr>
						<%
						if (checkuser_unlock_Error == "E" ) 
						{
						%>
								<tr align="center">
									<td>
										<s:submit name="Submit" action="loginLink.do" value='BACK'></s:submit>
									</td>
								</tr>
						<%
						}
						%>
						<tr>
							<td colspan='2'>&nbsp;</td>
						</tr>
						<br>
						</s:form>
						</table>
						</div>
						<%
					} 
					if(checkuser_unlock_Error == "S" || checkuser_unlock_Error == null)
					{
							if(UnlockUserViewStatus =="E" )
							{
								%>
								<div align="center">
								<table border='0' cellpadding='0' cellspacing='0' width='40%' >
								<s:form name="CheckUserViewStatus" autocomplete="off">
								<tr align="center">
									<td colspan="2">
										<font color="Red"><b><%=unlockUserViewMessage%></b></font>
									</td>
								</tr>
								<tr align="center">
									<td>
										<s:submit name="Submit" action="loginLink.do" value='BACK'></s:submit>
									</td>
								</tr>
								<tr>
									<td colspan='2'>&nbsp;</td>
								</tr>
								<br>
								</s:form>
								</table>
								</div>
								<%
							}
							else
							{
								%>




<s:form name="unlockuserform" action="checkuserUnlockuser" namespace="/" autocomplete="off">
<div align="center">
	<table border='0' width='40%'>
		
		<tr>
			<td>
				Institution : 
			</td>
				<td>
						<select name="instname" id="instname">
		 				<option value="-1">--Select Institution--</option>
			 				<s:iterator  value="institutes">
			 					<option value="${INST_ID}">${INST_ID}</option>
			 				</s:iterator>
		 				</select>
				</td>
	   	</tr>
	   	<tr>
	   		<td>
				User Name : 
	   		</td>
	   		<td>
	   			<s:textfield name="username" id="username"></s:textfield>
	   		<td>
	   	</tr>
	   	
	   	<tr>
	   		<td>
				Password : 
	   		</td>
	   		<td>
	   			<s:password name="password" id="password"></s:password>
	   		<td>
	   	</tr>
	   		<tr>
	   		
	   		<td colspan="2" align="center">
	   			<s:submit name="Submit" vaule="submit" onclick="return form_check();"></s:submit>
	   			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:reset name="Reset" value="Reset"></s:reset>
	   			&nbsp;&nbsp;<s:submit name="Submit" action="loginLink.do" value='Back'></s:submit>
	   			
	   		</td>
	   	
	   		
	   	</tr>
	   	</table>
	    </div> 
</s:form>
<%}} %>

  