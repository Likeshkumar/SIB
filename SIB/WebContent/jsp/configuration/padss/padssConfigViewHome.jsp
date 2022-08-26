<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" >


function goBack()
{
	window.history.back()
}

function validation_authkey(){
 	var reason = prompt('Enter the Reason for Reject?');
	 if( reason!=null ){
		 document.getElementById("remarks").value = reason;
		 return true;
	 }
	 return false;
}
</script>
<style>
#textcolor
{
color: maroon;
font-size: small;
font-weight: bold;
}

#tdbold{
font-weight: bold;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"/>

<div align="center">
<s:form action="" autocomplete="off">
<s:hidden name="remarks" id="remarks" value=""></s:hidden>
		<table align="center" border="1"  cellspacing="0" cellpadding="0" width="80%" class="formtable">
		<tr>
		 <td id="tdbold" nowrap >KEY NAME</td>
		 <td id="tdbold" nowrap>CHECK DIGIT DMK KVC </td>
		  <td id="tdbold" nowrap>CHECK DIGIT DPK KVC </td>
		  <td id="tdbold" nowrap>AUTH STATUS</td>
		  <td id="tdbold" nowrap>ADDED BY</td>
		  <td id="tdbold" nowrap>ADDED DATE</td>
		  <td id="tdbold" nowrap>AUTH BY</td>
		  <td id="tdbold" nowrap>AUTH DATE</td>
		</tr>
			<s:iterator value="keylist">
			       <tr>
				 		<td id="textcolor">${KEYDESC}</td>
				  		 <td id="textcolor">${DMK_KVC}</td>
				     	<td id="textcolor">${DPK_KVC}</td>
				        <td id="textcolor">${AUTH_CODE}</td>
					   <td id="textcolor">${ADDED_BY}</td>
					   <td id="textcolor">${ADDED_DATE}</td>
					   <td id="textcolor">${AUTH_BY}</td>
					   <td id="textcolor">${AUTH_DATE}</td>
					</tr>	
			</s:iterator>
		</table>
		<br>
		
	</s:form>
</div>


