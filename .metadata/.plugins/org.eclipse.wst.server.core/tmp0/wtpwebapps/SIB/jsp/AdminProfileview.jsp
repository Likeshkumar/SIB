<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<script>
 
function getValue(control)
{
	
var value1=(control.value);
var name=(control.name);
var ids=(control.id);

var pad="z";
var  newname=(pad.concat(name));
alert("newname  "+newname);
alert(ids);

//preveledge.newname.value=value1;
document.preveledge.z11.value=ids;
var c=(document.getElementById("newname").value);
alert("test "+ c);

}
 </script>



<s:form name="preveledge" action="instAdmnPrevUserManagementAction" autocomplete="off">

<div align="center">
<table><tr><td align="center"><b> Profile For <font color="red"><s:property value="#session.prev_prof_name" /> </font> </b></td></tr>
<s:hidden name="prof_id" id="prof_id" value="#session.prev_prof_id"></s:hidden>
<tr>
    
	
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</div>
<div align='center'>
<table border='1' cellpadding='0' cellspacing='0' width='50%' align="center" >
	
	<s:iterator  value="adminproflist">
		<tr>
			<td>
				<s:checkbox name="mainmenu" id="mainmenu" fieldValue="%{MENUID}"/>${MENUNAME}
			</td>
		</tr>	    
					<s:action name="adminProfSubMenuList" executeResult="false"  var="sunny">
						<s:param name="menuid" >${MENUID}</s:param>
					</s:action>
					
					<s:iterator  value="#sunny.adminsubmenulist"> 
								<tr> 
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<s:checkbox name="mainmenu" id="mainmenu" fieldValue="%{MENUID}"/>${MENUNAME}</td>	
								</tr>
					</s:iterator>
		
	</s:iterator>
	<tr><td>&nbsp;</td></tr>
	</table>
<table width='20%'>
	<tr align="center"><td colspan='2'><s:submit name="submitprofile" id="submitprofile" value="Save"/></td>
	<td><s:reset name="reset" id="reset" value="Reset"/></td></tr>
</table> 
</div>

</s:form>


