<%@taglib uri="/struts-tags" prefix="s" %>


<s:if test="%{resultstatus=='Y'}">
<div align="center">
<table>
<tr>
	<td>
		    <s:property value="userpasswordchangeresult"/>
	</td>
</tr>

				
<tr>
	<td>  Please Logout And Relogin Again
	</td>
</tr>



<tr>
	<td align="center"><a href="logoutAction.do">
		<font color="blue">
			<b>Click here For Logout </b>
		</font></a>
	</td>
	
</tr>
</table>
</div>
</s:if>



<s:if test="%{resultstatus=='N'}">
<div align="center">
<table>
<tr>
	<td>
		    <s:property value="userpasswordchangeresult"/>
	</td>
</tr>

				
<tr>
	<td>  Please Try Again
	</td>
</tr>



<tr>
	<td align="center"><a href="changepasswordChangePasswordAction.do">
		<font color="blue">
			<b>Click here For Retry </b>
		</font></a>
	</td>
	
</tr>
</table>
</div>
</s:if>



