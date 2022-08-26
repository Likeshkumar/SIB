<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>403</title>
 
 <style>
	*{
		font-family:verdana;
		font-size:13px;
	}
	table.formatable{
		border:1px solid gray;
		padding: 10px;
	}
</style>
</head>
<body>
 <form action="continueWithExpiryLoginAction.do" method="post" autocomplete="off">
 <table border="0" cellpadding="0" cellspacing="0" class="formatable" width="50%">
 	<tr>
 		<td>
 		 
 		  <h2>Information about expire the product</h2>	  
		<P>
			This is the message for the information about the product will expiry <%=session.getAttribute("EXPIRYWARNINGDAYS")%> day(s) time period. <br/>
			Please contact the administrator for the renewal of this product. <br/>
			<small>Thanks for your valuable time. </small> 
		</P>  
 	</td>
 	</tr>
 	
 	<tr>
 		<td style="text-align:center"> <input type="submit" value="Continue" id="continue" name="continue"/></td>
 	</tr>
 </table>
 </form>
 </body>
	
	<!--  john -->
	
	
	
	 
	 
</body>
</html>