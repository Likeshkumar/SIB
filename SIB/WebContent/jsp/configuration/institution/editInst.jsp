<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script>
function viewbin(bin)
{
	popupWindow = window.open('binDetailsAddBinAction.do?bin='+bin,'','left=350,top=150,width=600,height=400,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
	popupWindow.focus();

}

function selectall()
{
	valid=true;
	var selectbox = document.getElementById("instid");
	if(selectbox.value=="-1")
	{
		errMessage(selectbox,"SELECT INST ID");
		return false;
	}
	return valid;
}
</script>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
<%
	String instid = (String)session.getAttribute("Instname"); 
 
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="editinstDetalsAddInstitutionAction.do" method="post" autocomplete="off">
<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
	     <table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
			
						<tr>
							<td align="right">
								SELECT INSTITUTION:
							</td>
							<td>
								<select id="instid" name="instid">
									<option value="-1">-SELECT INSTITUTION-</option>
									<s:iterator  value="instlist" >
									<option value="${INST_ID}">${INST_NAME}</option>
									</s:iterator>
								</select>
							</td>
					    </tr>
			
	      </table>
	
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%">
				<tr>
					<td align="center"><input type=submit value="view" onclick="return selectall();"/></td>
				</tr>
			</table>
</form>
