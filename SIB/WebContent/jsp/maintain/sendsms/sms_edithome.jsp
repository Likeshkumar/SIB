<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="cache-control" content="no-cache" />
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function updateSMSContent(){
	var bulksmscode = document.getElementById("bulksmscode");
	var smscontent = document.getElementById("smscontent");
	
	if( smscontent.value == ""){
		errMessage(smscontent, "Message should not be empty");
		return false;
	}
	var url = "updateSMSContentSMSSender.do?bulksmsid="+bulksmscode.value+"&smscontent="+smscontent.value; 
	if( !confirm("Do you want to edit ?") ){
		return false;
	}
	
	var result = AjaxReturnValue(url)	;
	alert(result);
	window.opener.location.reload();
    window.close();
}
	
	 
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="#"  name="transanalysesform" autocomplete = "off" namespace="/" javascriptTooltip="true">
<table border="0" cellpadding="0" class="formtable" cellspacing="0" width="80%" id="maxcount"  >
 
<tr>
	<td style="width:50%;color:red">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"  >
			 <tr id="smscontentrow" >
					<td>  SMS Content </td>
					<td>  &nbsp; <s:textarea name="smscontent" id="smscontent" value="%{smsbean.smscontent}"/>
						<s:hidden name="bulksmscode" id="bulksmscode"  value="%{smsbean.bulksmsid}" />
					  </td>
			</tr>  
		</table> 
	</td> 
	 
</tr> 
</table>

<table border="0" cellpadding="0" cellspacing="0" width="20%"  >
	<tr>  
			<td>	<s:submit value="Update" name="submit" id="submit" onclick="return updateSMSContent();"/>	</td>
			<td>  </td> 
	
	</tr>
</table>
</s:form>

 