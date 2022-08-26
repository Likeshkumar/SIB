<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function validationselect(){
	//alert("WELCOME");
	var alertid = document.getElementById("alertid");
	//alert("WELCOME "+alertid);
	if(alertid)
	{
		//alert("WELCOME iNSIDE "+alertid);
		var opt = "<option value='-1'>SELECT</option>";
		var options = document.getElementById('alertid').options, count = 0;
		for (var i=0; i < options.length; i++) {
		  if (options[i].selected) {			  
			  opt += "<option value='"+options[i].value+"'>"+options[i].value+"</option>";
			  count++;	  
		  }		 
		}
		if( count == 0 ){
			//alert(count);
			errMessage(document.getElementById("alertid"), "Select Configuration");
			return false;
		} 
	}
	
	parent.showprocessing();
}

</script>
</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addmailcontentAddMailAction.do" method="post" autocomplete="off">
	<table>
		<tr>
			<td colspan="2">
				<small class="dt">
					Select the module which u want to Add mailer addresses and content.
				</small>
			</td>
		</tr>
		<tr>
			<td>Select Configuration:</td>
			<td>
				<s:select list="%{alertdetails}" id="alertid" name="alertid"
						listKey="EMAIL_ALERT_LIST" listValue="DESCRIPTION" multiple="true"
						style='height: 200px'/>
				<br/> <small style='font-size:10px;color:#000'> &nbsp;&nbsp;&nbsp; press ctrl to select multiple</small>				
			</td>	
		</tr>
			<tr>
			<td></td>
			<td><s:submit value="Next" onclick="return validationselect();"/></td>
			</tr>
	</table>
</s:form>
</body>
</html>