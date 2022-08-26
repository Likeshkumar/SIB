<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<head>

<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>

<script type="text/javascript" language="javascript">
function set_usermail(usermailvalue)
{	
	if(usermailvalue=='U')
	{
		document.getElementById("userdiv").style.display = "table-row";
		document.getElementById("maildiv").style.display = "none";
	}
	else
	{
		document.getElementById("maildiv").style.display = "table-row";
		document.getElementById("userdiv").style.display = "none";
	}
}

function passwordvalidation()
{
	var instid = document.getElementById("instid");
	var usertext = document.getElementById("usertext");
	var mailtext = document.getElementById("mailtext");

	if(usertext.value=="")
	{
		//alert(instid.value);
		errMessage(usertext,"Enter Username");
		return false;
	}
	if(instid.value=="")
	{
		//alert(instid.value);
		errMessage(instid,"Enter Institution");
		return false;
	}
	
	

	/*
     var form = document.getElementsByName("user_mail");
     for(var i = 0; i < form.length; i++)
     {
          if(form[i].checked)
          {
          var selectedValue = form[i].value;
          //alert(selectedValue);
          	if(selectedValue=='U')
          	{
          		if(usertext.value=="")
          		{
          			errMessage(usertext,"Enter Username");
          			return false;
          		}
          	}
          	if(selectedValue=='E')
          	{
          		if(mailtext.value=="")
          		{
          			errMessage(mailtext,"Enter Email-id");
          			return false;
          		}
          		else
          	    {
          			 if( !emailvalidator( mailtext ) )
          			 {
          				errMessage(mailtext, "InValid E-Mail Address" );
          			 	return false;
          			 }

          	    }
          	}

          }
     }
     
     */
     
     
	return true;
	
}

function goBack(){
	window.history.back();
}
</script>
</head>

<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="updateForgetpasswordrequestForgetpasswordAction" autocomplete="off" namespace="/" name="sendmail">
		<table>
			
			<!-- <tr>
				<td>Select:</td><td><s:radio id="user_mail"  name="user_mail" list="#{'U':'USERNAME'}" onclick="set_usermail(this.value);"  value="'U'" ></s:radio></td>
			</tr> -->
			
   
								<!-- <input type="hidden" name="token" id="csrfToken"value="123"> -->
				<tr id="userdiv">
					<td>Enter Username:</td>
					<%-- <td><s:textfield name="usertext" id="usertext" onkeypress='return alphanumerals(event)' value="%{bean.usertext}"/> --%>
					<td><s:textfield name="usertext" id="usertext" onkeypress='return alphanumerals(event)' />
					<s:fielderror fieldName="usertext" cssClass="errmsg" />
					</td>
				</tr>
				<tr id="maildiv" style="display:none">
					<td>Enter Mailid:</td>
					<td><input type="text" name="mailtext" id="mailtext"/>
					<s:fielderror fieldName="usertext" cssClass="errmsg" />
					</td>
				</tr>
				
				<tr>
				<%-- <td>Select Institution:</td>
				<td><s:select list="institutionlist" id="instid"
					name="instid" listKey="INST_ID" listValue="INST_NAME"
					headerKey="-1" headerValue="-SELECT-" /> </td> --%>
					
				<td>Institution:</td>
					<%-- <td><s:textfield  id="instid"	name="instid" listKey="INST_ID" listValue="INST_NAME" onkeypress='return alphanumerals(event)' value="%{bean.instid}" /> --%> 
						<td><s:textfield  id="instid"	name="instid" listKey="INST_ID" listValue="INST_NAME" onkeypress='return alphanumerals(event)' />
						<s:fielderror fieldName="instid" cssClass="errmsg" />
				</td>
					
			</tr>
			
		</table>
		<table>
			<tr>
				<!-- <td><input type="submit" name="submit" id="submit" value="Request"  onclick="return passwordvalidation();"></td> -->
				
							
							<input type="hidden" name="token" id="csrfToken"value="${sessionScope.csrfToken}">
				<td><input type="submit" name="submit" id="submit" value="Request"></td>
				<td><s:submit name="Submit" action="loginLink.do" value='Back'></s:submit></td>
			</tr>
		</table>
</s:form>
</body>
</html>


