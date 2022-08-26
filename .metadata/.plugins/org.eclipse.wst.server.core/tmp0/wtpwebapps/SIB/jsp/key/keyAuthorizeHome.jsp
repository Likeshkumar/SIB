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
function validate()
{
	valid=true;
	var selectbox = document.getElementById("keyid");
	if(selectbox.value=="-1")
	{
		errMessage(selectbox,"SELECT KEY");
		return false;
	}
	return valid;
}
</script>
<form action="viewAuthpadssConfig1PadssConfigAction.do" method="post" autocomplete="off"> 
	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
		<tr>
			<td>SELECT KEY</td>
			<td align="center">
					:					
			</td>
			<td>
				<select name="keyid" id="keyid">
					<option value="-1">-SELECT KEY-</option>
					<s:iterator  value="keylist" >
					<option value="${KEYID}">${KEYDESC}</option>
					</s:iterator>
				</select>
			</td>
		</tr>
	</table>
		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="20%" >	
				<tr align="center">
					<td> <input type="submit"  name="submitcur" id="submitcur"  value="View" onclick="return validate()"/></td>
					<td>
						<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
					</td>
				</tr>
			</table>
		</div>
</form>




