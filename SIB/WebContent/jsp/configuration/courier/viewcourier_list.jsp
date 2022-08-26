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
		<th>Courier Name</th>
		<th>Address</th>
	    <th>Contact Number</th>
	     <th>Status</th>
	    <th>Edit</th> 
	</tr>
	<% int rowcnt = 0; Boolean alt=true; %>   
	<s:iterator  value="courierbean.allcourierlist">
		<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>  id='row<%=rowcnt%>' onmouseover='showShadow( this.id )' onmouseout="removeShadow( this.id )">
				 
				<td style="text-align:center"> ${COURIER_NAME} </td>
				<td style="text-align:center"> ${COURIER_HOFFICE} </td>
			    <td style="text-align:center"> ${COURIER_CONTACTNO} </td> 
			    <td style="text-align:center"> ${COURIER_STATUS} </td>
			     <td style="text-align:center"> 
			     	<s:form action="editCourierCourierConfig.do">
			     		<s:hidden name="courierid" value="%{COURIERMASTER_ID}"/>	<s:submit value="Edit"></s:submit>
			     	</s:form> </td>
			    
			    
		</tr>
	</s:iterator>
</table>

</body>
</html>
