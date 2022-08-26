<%@page import="org.apache.struts2.components.Bean"%>
<%@page import="java.io.Writer"%>
<%@page import="java.util.List"%>
<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 


<html>
<head>
<script language=javascript>

function enablesubmenus(id,checked)
{ 
		var count = document.getElementsByName('mainmenu').length;
		for(var x=1; x<=count; x++)
		{
			
			var submenuid = id + x;
			var submenu = document.getElementById(submenuid);
			if(checked)
				{
					submenu.disabled = false;
				}
			else
				{
					submenu.disabled = true;
					submenu.checked = false;
				}
		}
}
function enablechildmenus(id,checked)
{
		
		var chcount = document.getElementsByName('submenu').length;
		for(var x=1; x<=chcount; x++)
		{
			var chmenuid = id + x;
			var chmenu = document.getElementById(chmenuid);
			if(checked)
			{
				
				chmenu.disabled = false;
				
			}
		else
			{
				chmenu.disabled = true;
				chmenu.checked = false;
				
			}
		}
}
	
</script>
</head>
<body>
<s:form action="prevaddAction" autocomplete="off">
<table border='1' cellpadding='0' cellspacing='0' width='100%' align="center" >
<tr>
	<td align="center">SELECTED
		<s:bean name="com.ifp.beans.profilebean" var="profile"></s:bean>
		<s:property  value="prof_name"/>
	</td>
</tr>
	<s:bean name="com.ifp.beans.menubean" var="resd" ></s:bean>
	<s:iterator  value="#resd.menulist">
	<tr>
		<td>
				<s:checkbox name="mainmenu" id="%{MENUID}" fieldValue="%{MENUID}"  onclick="return enablesubmenus(this.id,this.checked)"/>${MENUNAME}
		</td>
	</tr>
	<s:bean name="com.ifp.beans.menubean" var="menubean">
	<s:param name="menuid">${MENUID}</s:param>
	</s:bean>
	<s:action name="menuAction!subMenuList"/>
		<s:iterator  value="#resd.submenulist">
			<tr>
				<td>
					&nbsp;&nbsp;&nbsp;&nbsp;
					${MENUNAME}
					</td>
					<td>
						<s:checkbox name="makchkenable" id="%{MENUID}" fieldValue="%{MENUID}"/>Enable MakerChecker
					</td>
			
			</tr>
		</s:iterator>
	</s:iterator>
</table> 
<s:submit name="submitprofile" id="submitprofile" value="Save"/>
</s:form>
</body>
</html>