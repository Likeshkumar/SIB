<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function mailaddressvalidation()
{
	var institution = document.getElementById("inst");
	 
	var smsurl = document.getElementById("smsurl");
	
	if(institution)
	{
		if(institution.value == "-1")
		{
			errMessage(institution,"Please Enter institution");
			return false;
		}	
	}
	if(smsurl.value == "")
	{
		errMessage(smsurl,"Enter URL");
		return false;
	}
	  
}
</script>
<%String username = (String)session.getAttribute("SS_USERNAME");
String usertype = (String)session.getAttribute("USERTYPE");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addSmsUrlActionSMSAction.do" method="post" autocomplete="off">
<table border="0">
<% if(usertype.equals("SUPERADMIN")){  %>

	<tr>
		<td>INSTITUTION :</td>
		<td>
		<s:select list="%{institutionlist}" id="inst" name="inst"
					listKey="INST_ID" listValue="INST_ID" headerKey="-1"
					headerValue="-SELECT-"/>
		</td>
	</tr>

<%} %>

	<tr><td>SMS URL :</td><td><textarea name='smsurl' id='smsurl' style='width:500px'></textarea>
		<br/> <small class='dt'>Enter the sms url provided by the sms vendor</small>
		 <small class='dt'> ( KEYWORDS :  TO = $TO$, MESSAGE=$MSG$ ) </small>
	</td></tr>
	 
	<tr align="center"><td></td><td><s:submit value="submit" onclick="return mailaddressvalidation();"/></td></tr>
</table>
</s:form>