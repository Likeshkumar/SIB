<%@taglib uri="/struts-tags" prefix="s" %>
<head>
<script type="text/javascript">
function passwordvalidator()
{
	
	var oldpassword=document.getElementById("oldpassword").value;
	var newpassword=document.getElementById("newpassword").value;
	var cnewpassword=document.getElementById("cnewpassword").value;
	if(oldpassword=="")
	{
	 alert(" Old Password Cannot be Empty");
	 return false;
	}
	
	if(newpassword=="")
	{
	 alert(" New Password Cannot be Empty");
	 return false;
	}
	
	if(cnewpassword=="")
	{
	 alert(" Confirm New Password Cannot be Empty");
	 return false;
	}
	
	if(oldpassword==newpassword)
		{
			alert(" Old Password and New Password Should Not Be Same ");
			return false;
		}
	
	if(cnewpassword!=newpassword)
	{
		alert(" New Password and Confirm Password Should Be Same ");
		return false;
	}
	
	
return true;

}

</script>


</head>
<div align="center">
<s:form action="forcePasswordChangeLoginAction" autocomplete="off">

	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td></td><td align="center"><font color="red">Your Password Has To Be Change For Security Reason</font></td>
		</tr>
		<tr>
			<td>Old Password : </td>
			<td><input type="password" name="oldpassword" id="oldpassword"></td>
		</tr>
	
		<tr>
			<td>New Password : </td>
			<td><input type="password" name="newpassword" id="newpassword"></td>
		</tr>
		
		<tr>
			<td>Confirm New Password : </td>
			<td><input type="password" name="cnewpassword" id="cnewpassword"></td>
		</tr>
		
		<tr>
			<td></td><td><input type="submit" name="submit" onclick="return passwordvalidator();" value="Change"></td>
		</tr>
	</table>

</s:form>
</div> 

 
 