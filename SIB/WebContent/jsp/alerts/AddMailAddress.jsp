<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function mailaddressvalidation()
{
	var institution = document.getElementById("inst");
	var email = document.getElementById("mailaddress");
	var username = document.getElementById("username");
	
	if(institution)
	{
		if(institution.value == "-1")
		{
			errMessage(institution,"Please Enter institution");
			return false;
		}	
	}
	if(username.value == "")
	{
		errMessage(username,"Please Enter username");
		return false;
	}
	 if(email.value == "")
	 {
		 errMessage(email,"Please Enter email id");
		 return false;
	 }
	 else
	 {
		 if( !emailvalidator( email.value ) )
		 {
		 	errMessage(email,"InValid E-Mail Address");
		 	return false;
		 }
	  }
}
</script>
<%String username = (String)session.getAttribute("SS_USERNAME");
String usertype = (String)session.getAttribute("USERTYPE");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="saveaddaddressAddMailAction.do" method="post" autocomplete="off">
<table border="0">
<%if(usertype.equals("SUPERADMIN")){ %>

	<tr>
		<td>INSTITUTION :</td>
		<td>
		<s:select list="%{institutionlist}" id="inst" name="inst"
					listKey="INST_ID" listValue="INST_ID" headerKey="-1"
					headerValue="-SELECT-"/>
		</td>
	</tr>

<%} %>

	<tr><td>USER NAME :</td><td><s:textfield name="username" id="username"/></td></tr>
	<tr><td>MAIL ADDRESS :</td><td><s:textfield name="mailaddress" id="mailaddress"/></td></tr>
	<tr align="left"><td></td><td><s:submit value="submit" onclick="return mailaddressvalidation();"/></td></tr>
</table>
</s:form>