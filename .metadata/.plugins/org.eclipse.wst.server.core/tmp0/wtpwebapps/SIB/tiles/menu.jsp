<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<head> 


<link href="style/default.css" media="screen" rel="stylesheet" type="text/css" />  
</head>

 <script>
 function changeColour()
 {
	 alert('testing');
 }
 </script>


 <% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>

<%-- <s:if test="%{#session.SS_USERNAME=='ifpadmin' || #session.EXTERNALUSER=='ifpadmin2'}"> --%>
<s:if test="%{#session.USERTYPE =='SUPERADMIN'}">
	
	<s:action name="adminMenuAction!crtPrntLst"/>
	<s:iterator  value="#session.menulist" >
			 	<ul id="nav" class="dropdown dropdown-horizontal1">
				 	<li class="dir">
					<s style="FALSE"> </s>
				  	<s:if test="ACTION=='NA'">
				  			  					${MENUNAME}
				  			  				</s:if>
				  			  				<s:else>
						  	<a href="${ACTION}" target="mainframe">${MENUNAME}</a>
						  	</s:else> 
				  	<s:bean name="com.ifg.usermgt.adminPageLoginDetailsAction" var="resd" >
						  	<s:action name="adminMenuAction!subMenuList" executeResult="false"  var="sunny">
						  			<s:param name="menuid" >${MENUID}</s:param>
						  	</s:action>  
				  	<ul id="nav_1" class="dropdown dropdown-horizontal1">
				  				<s:iterator  value="#sunny.submenulist">
			  					<li class="dir">
				  				<s:if test="ACTION=='NA'">
				  			  					${MENUNAME}
				  			  				</s:if>
				  			  				<s:else>
						  		<a href="${ACTION}" target="mainframe">${MENUNAME}</a>
						  		</s:else> 
				  			  	
				  			  	<s:action name="adminMenuAction!childMenuList" executeResult="false"  var="sss">
				  			  	 <s:param name="submenuid1" value="%{MENUID}"/>
				  			  	</s:action>
				  			  	<ul id="nav_2" class="dropdown dropdown-horizontal">
				  			  				<s:iterator  value="#sss.childmenulist">
						  							<a href="${ACTION}" target="mainframe"><li class="dir">
						  							<s:if test="ACTION=='NA'">
				  			  								${MENUNAME}
				  			  						</s:if>
				  			  						<s:else>
												  			${MENUNAME}
												  	</s:else> </li></a>
									  			</s:iterator>
				  			  	
				  			  	
				  			  	</ul>
				  			  	
				  			  	</li>
				  			  	</s:iterator>
				  			  	
				 </ul>
			  </s:bean>
				
			 
			  
				
</li>
</ul>
</s:iterator>	
</s:if>



	  
	<!-- InstAdmin  and branchuser-->  
	<s:else>
	<s:action name="crtPrntLstInstAdmin"/>  
		
		<s:iterator  value="#session.menulist" >
					 	<ul id="nav_3" class="dropdown dropdown-horizontal1">
						 	<li class="dir">
							<s style="FALSE"> </s>
							<s:if test="ACTION=='NA'">
				  			  					${MENUNAME}
				  			  				</s:if>  
				  			  				<s:else>
						  	<a href="${ACTION}" target="mainframe" >${MENUNAME}</a>
						  	</s:else> 
						  	<s:bean name="com.ifg.usermgt.InstAdminLoginAction" var="resd" >
								  	<s:action  name="subMenuListInstAdmin" executeResult="false"  var="sunny">
								  			<s:param name="menuid" >${MENUID}</s:param>
								  			<s:param name="profilelist" >${profilelist}</s:param>
								  	</s:action>
						  	<ul id="nav_4" class="dropdown dropdown-horizontal">
						  				<s:iterator  value="#sunny.submenulist">
					  					<li class="dir">
					  					
					  					<s:if test="ACTION=='NA'">
				  			  					${MENUNAME}
				  			  			</s:if>
				  			  			<s:else>
						  						<a href="${ACTION}" target="mainframe">${MENUNAME}</a>
						  				</s:else> 
						  				
						  			  <s:action name="childMenuListInstAdmin" executeResult="false"  var="sss">
				  			  	 <s:param name="submenuid1" value="%{MENUID}"/>
				  			  	 <s:param name="profilelist">${profilelist}</s:param>
				  			  		</s:action>
				  			  	<ul id="nav_5" class="dropdown dropdown-horizontal">
				  			  				<s:iterator  value="#sss.childmenulist">
						  							<li class="dir">
							  							<s:if test="ACTION=='NA'">
					  			  							${MENUNAME}
					  			  						</s:if>
					  			  						<s:else>
						  									<a href="${ACTION}" target="mainframe">${MENUNAME}</a>
							  							</s:else> 
						  							</li>
									  			</s:iterator>				  			  	
				  			  	</ul>
				  			  	
				  			  	</li>
				  			  	</s:iterator>
				  			  	
				 </ul>
			  </s:bean>
				
</li>
</ul>
</s:iterator>			
</s:else>


