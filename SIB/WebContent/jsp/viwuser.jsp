<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<style type="text/css">
table.formtable td{
	border:1px solid #efefef;
	padding:  10px  ;
}
</style>
<script>
function showShadow( rowid ){
	document.getElementById(rowid).style.backgroundColor = "#D8D8D8";
	 
}

function removeShadow( rowid ){
	document.getElementById(rowid).style.backgroundColor = "";	 
}

</script>
<body>
<table align="center" border="0"  cellspacing="0" cellpadding="0" width="100%" class="formtable viewtable">
	<tr style="color: maroon;">
		<th>Branch Name</th>
		<th>User Name</th>
	    <th>Institute Id</th>
	    <th>Profile Name</th>
	    <th>User Status</th>
	    <th>User Block Status</th>
	    <th>Added By </th>
	    <th>Added Date </th>
	    <th>Auth/De-Auth By </th>
	    <th>Auth/De-Auth Date</th>
	    <th>Auth/De-Auth Status</th>
	    <th>Remarks</th>
	</tr>
	<% int rowcnt = 0; Boolean alt=true; %>  
	<s:iterator  value="username">
		<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>  id='row<%=rowcnt%>' onmouseover='showShadow( this.id )' onmouseout="removeShadow( this.id )">
				<td> ${BRANCH_NAME} </td>
				<td> ${USERNAME} </td> 
				<td align=center> ${INSTID} </td>
				<td align=center> ${PROFILE_NAME} </td>
			    <td align=center> ${USERSTATUS} </td> 
			    <td align=center> ${USERBLOCK} </td>
			    <td align=center> ${ADDED_BY} </td>
			    <td align=center> ${ADDED_DATE} </td>
			    <td align=center> ${AUTH_BY} </td>
			    <td align=center> ${AUTH_DATE} </td>
			    <td align=center> ${AUTH_STATUS} </td>
			     <td align=center> ${REMARKS} </td>
			    
		</tr>
	</s:iterator>
	
	
	
</table>

</body>
</html>
