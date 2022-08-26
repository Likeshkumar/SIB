<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 

<title>Insert title here</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="insertSetSettings"  name="perscardgen" autocomplete = "off" namespace="/">

<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 700px; margin:0 auto; height: 300px;" doLayout="true">

<sx:div id="one" label="FRONT END SETTINGS" labelposition="top" >
	<br><br><br>
	<table border="1" cellpadding="1" cellspacing="10" RULES="NONE" width="75%"  style="border-color: gray;">	
		<tr>
			<td align="center">DATE FILTER REQUIRED:&nbsp;&nbsp;<s:radio id="date_req"  name="date_req" list="#{'1':'YES','0':'NO'}"  value="%{usersetting}"></s:radio></td>
		</tr>
		<tr align="center">
			<td><input type="submit" value="OK"></td>
		</tr>
	</table>
</sx:div>

<!--

<sx:div id="two" label="PERSONALIZED CARD" >

    <br><br><br>
	<table border="1" cellpadding="0" cellspacing="0" RULES="NONE" width="75%"  style="border-color: gray;">
	    <tr>
	    	<td align="center">DATE FILTER REQUIRED:&nbsp;&nbsp;<s:radio id="date_req"  name="date_req" list="#{'1':'YES','0':'NO'}"></s:radio></td>
	    </tr>
	    <tr align="center">
		    <td><input type="button" value="OK"></td>
	    </tr>
	</table>

</sx:div>
 
--> 

</sx:tabbedpanel>

</s:form>