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
<s:form action="keyupdateauthorizedeauthorizePadssConfigAction.do" autocomplete="off">
<s:hidden name="remarks" id="remarks" value=""></s:hidden>
		<table align="center" border="1"  cellspacing="0" cellpadding="0" width="40%" class="formtable">
			<s:iterator value="keylist">
			<input type="hidden" name="keyid" id="keyid" value="${KEYID}" />
			<input type="hidden" name="keydesc" id="keydesc" value="${KEYDESC}" />
			       <tr>
				 		 <td id="tdbold">KEY NAME</td><td id="textcolor">${KEYDESC}</td>
				   </tr>
				   <tr>
				 		 <td id="tdbold">CHECK DIGIT DMK KVC </td><td id="textcolor">${DMK_KVC}</td>
				   </tr>
				   <tr>
				 		 <td id="tdbold">CHECK DIGIT DPK KVC </td><td id="textcolor">${DPK_KVC}</td>
				   </tr>
				   
				   
				   <tr>
				   <td id="tdbold">AUTH STATUS</td><td id="textcolor">${AUTH_CODE}</td>
					</tr>	
					 <tr>
				   <td id="tdbold">ADDED BY</td><td id="textcolor">${ADDED_BY}</td>
					</tr>	
					 <tr>
				   <td id="tdbold">ADDED DATE</td><td id="textcolor">${ADDED_DATE}</td>
					</tr>	
					
					
							  		
			</s:iterator>
		</table>
		<br>
		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="100%" >
				<tr align="center">
					<td>
						<input type="submit" name="auth" id="auth1" value="Authorize"/>
						<input type="submit" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authkey();"/>						
					</td>
				</tr>
		    </table>
		</div>
	</s:form>
</div>


