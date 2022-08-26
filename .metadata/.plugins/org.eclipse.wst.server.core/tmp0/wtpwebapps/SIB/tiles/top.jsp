<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<SCRIPT type="text/javascript">
window.history.forward();
function noBack() { window.history.forward(); }
</SCRIPT>

<head>
	
	</head>
<BODY onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">
<div style="float:left;width:40%;color:white">Welcome <s:property value="#session.name" />
|     Userid : <s:property value="#session.SS_USERNAME"/>
|     Branch: <s:property value="#session.BRANCH_DESC"/>      
</div>

	<input type="hidden" name="token" id="csrfToken"value="#session.csrfToken">

<div style="float:left"><span style="color:white" >Last Login  <s:property value="#session.lastlogin" /> </span></div>
<%-- <s:if test="%{#session.SS_USERNAME=='ifpadmin'}"> --%>
<s:if test="%{#session.USERTYPE=='SUPERADMIN'}">
	<div style="float:right;width:30%;color:black" ><span style="color:black" >
		<!--   <a id="viewinfo" href="dashborddashboardAction.do" target="mainframe"> Dashboard 1</a> |  --> 
		 <% if ( session.getAttribute("USERTYPE") !=null && session.getAttribute("USERTYPE").equals("INSTADMIN")) { %>
		 	<a id="viewinfo" href="fraudDashboarddashboardAction.do" target="mainframe" onclick="showprocessing()"> Dashboard  </a> |
		 	<a href="changepasswordChangePasswordAction.do" target="mainframe">Change Password</a> | <a href="goConfigSettings.do" target="mainframe">Date Filter </a> |
		 <% } %>		 
		<a href="logoutAction.do">Logout </a>
		| <a href="changepasswordChangePasswordAction.do" target="mainframe">Change Password</a>  
		</span>
	</div> 
</s:if> 
<s:else>
	<div style="float:right;width:30%;color:black" > 
		<!--  <a id="viewinfo" href="dashborddashboardAction.do" target="mainframe">    Dashboard 1 </a> | -->
		
		<%--  <% if (session.getAttribute("USERTYPE") !=null && session.getAttribute("USERTYPE").equals("INSTADMIN")) { %>
		 	<a id="viewinfo" href="merchantDashBoarddashboardAction.do" target="mainframe"  onclick="showprocessing()" > Dashboard </a> |
		 <% } %>	 --%>	 
		   
		 <a href="logoutAction.do"><span style="color:white" >Logout </span></a>| 
		 <a href="changepasswordChangePasswordAction.do" target="mainframe"><span style="color:white" >Change Password</span></a> | <a href="goConfigSettings.do" target="mainframe"><span style="color:white" >Date Filter</span> </a>
	</div> 
</s:else>
</BODY>