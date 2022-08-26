<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function checkForm()
{
	valid=true;
	var subproduct=( document.ProductAddFofm.subproduct );
	if(subproduct.value=="00")
	{
		errMessage(subproduct,"Please Select The Profile");
		return false;
	}	
	
	return valid;

}
</script>
<%String profileflag = (String)session.getAttribute("PROFILE_FLAG");  %>
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
<s:form name="ProductAddFofm" action="saveEditUserManagementAction" autocomplete="off">
<table id="errmsg"></table>
	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
	
			<s:if test="%{instproflist.isEmpty()}">
				<b style="color: red">No Profile Configured</b>
			</s:if>
			<s:else>		
				<tr align="left">
					<td>Institution :</td>
					<td>
						 <s:textfield name="instname" id="instname" readonly="true"></s:textfield>
					</td>
				</tr>
				<tr align="left">
				<td>Select Profile : </td>
				<td>
					<select  name="subproduct" id="subproduct" >
						<option value='00'>Select</option>
						<s:iterator  value="instproflist">
							<option value="${PROFILE_ID}">${PROFILE_NAME}</option>	
						</s:iterator>
					</select>
				</td>
				<tr><td></td><td><s:submit value="Next" name="submit" onclick="return checkForm();" /></td></tr>
			</s:else>
	
	</table>
</s:form>
</div>

