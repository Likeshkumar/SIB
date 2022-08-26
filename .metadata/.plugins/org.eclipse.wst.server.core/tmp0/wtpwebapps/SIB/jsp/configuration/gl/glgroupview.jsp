<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
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

function viewgl(grpcode)
{
	//alert("grpcode"+grpcode);
	window.open('glgrpdetailsGLConfigure.do?grpcode='+grpcode,'','left=350,top=150,width=600,height=400,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
}


function selectall()
{
	valid=true;
	var selectbox = document.getElementById("glgrpcode");
	if(selectbox.value=="-1")
	{
		errMessage(glgrpcode,"SELECT GL GROUP");
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
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 

<div align="center">
	<form action="viewgrpglGLConfigure.do" method="post" autocomplete="off">
		<div align="center">
				<table align="center" border="0"  cellspacing="1" cellpadding="0" class="formtable" width="40%">
					<tr>
						<td align="right">SELECT GL GROUP:</td>
						<td>
							<select name="glgrpcode" id="glgrpcode" >
									<option value="-1">-SELECT GLGROUP-</option>
									<s:iterator value="glgrpdetails">
										<option value="${GROUP_CODE}">${GROUP_NAME}</option>
									</s:iterator>
							</select>
						</td>
					</tr>
				</table>
				<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
					<tr align="center">
						<td>
						<s:submit value="View"  onclick="return selectall()"></s:submit>
						</td>
					</tr>
				</table>
		</div>
	</form>
</div>
