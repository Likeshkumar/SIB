<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
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
</script>
</head>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<table align="center" border="0"  cellspacing="1" cellpadding="0" width="90%" class="formtable">
	<tr style="color: maroon;">
			<th>INSTITUTION NAME</th>
			<th>INSTITUTION ID</th>
			<!-- <th>STATUS</th>   -->
			<th>ADDED/EDITED BY</th>
			<th>AUTH STATUS</th>
			<th>AUTH/DE-AUTH BY</th>						
			<th>REASON</th>
			<th>VIEW</th>
	</tr>
	<% int rowcnt = 0; Boolean alt=true; %> 
	<s:iterator  value="instlist">
		<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
				<td style="text-align:center;">${INST_NAME} </td>
				<td> ${INST_ID} </td>  
				<%-- <td>${STATUS}</td>   --%>
				<td> ${ADDED_BY} </td> 
				<td> ${AUTH_CODE} </td> 
				<td> ${AUTH_BY} </td> 				
				<td> ${REMARKS} </td> 
				<td align="center">
					<s:if test="%{flag_status=='view'}"><form action="instDetalsAddInstitutionAction.do" method="post" autocomplete="off"><input type="image"  src="images/view.png" alt="View Institution"><input type="hidden" name="instid" value="${INST_ID}"></form></s:if>
					<s:elseif test="%{flag_status=='auth'}"><form action="authinstDetalsAddInstitutionAction.do" method="post" autocomplete="off"><input type="image"  src="images/view.png" alt="View Institution"><input type="hidden" name="instid" value="${INST_ID}"></form></s:elseif>
				</td>
		</tr>
	</s:iterator>
</table>
<table>
	<tr>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />
		</td>
	</tr>
</table>
</body>
</html>


	
	
	
