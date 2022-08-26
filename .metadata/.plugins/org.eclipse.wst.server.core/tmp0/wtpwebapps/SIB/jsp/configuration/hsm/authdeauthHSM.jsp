<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<jsp:include page="/displayresult.jsp"/>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function ask_confirm()
{
	var c=confirm("Do You Want To Delete");
	return c;
}

function edit_confirm()
{
	var d=confirm("Do You Want To Edit");
	return d;
}
function selectall()
{
	valid=true;
	var selectbox = document.getElementById("hsmid");
	if(selectbox.value=="-1")
	{
		errMessage(hsmid,"SELECT HSM");
		return false;
	}
	return valid;
}
</script>
<form action="viewAuthHsmAction.do" method="post" autocomplete="off"> 
	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
		<tr>
			<td>SELECT HSM</td>
			<td align="center">
					:					
			</td>
			<td>
				<select name="hsmid" id="hsmid">
					<option value="-1">-SELECT HSM-</option>
					<s:iterator  value="beans.hsmlist" >
					<option value="${HSM_ID}">${HSMNAME}</option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>
		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="20%" >	
				<tr align="center">
					<!-- <td> <input type="submit"  name="submitcur" id="submitcur"  value="View" onclick="return selectall()"/></td> -->
					<td> <input type="submit"  name="submitcur" id="submitcur"  value="View" /></td>
					<td>
						<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
					</td>
				</tr>
			</table>
		</div>
</form>




