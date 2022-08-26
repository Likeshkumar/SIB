<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script>
 
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:20px;
}

.errmsg{
	color:red;
	font-weight:bold;
}

 

</style>
<title>Insert title here</title>
<sx:head/>
</head>
<body>

 
<s:form action="updateEODDaybiginAction" name="schemeprocess"  autocomplete = "off" >
	 
	<table border='0' cellpadding='0' cellspacing='0' class="subaction" width='80%' style="text-align:center">
	
	<tr>
		<td class="errmsg">
		<%
			if ( request.getParameter("resmsg") != null) {
				out.println( request.getParameter("resmsg")  );
			}
		
		%>
		</td>
	</tr>
	
	<tr>
		<td> &nbsp; </td>
	</tr> 
	
	<tr>
		<td style='text-align:center'> <b> Current Business date  :  ${  curbusdate  }  </b> </td>
		<input type='hidden' name='currentbusdate' id='currrentbusdate' value='${curbusdate} '/>
	</tr>
	
	
	<tr>
		<td style='text-align:center'>Next Business day :     <sx:datetimepicker name="nxtbusdate"  displayFormat="dd-MMM-yyyy" > </sx:datetimepicker>   </td>
	</tr>
	<tr>
		<td>
			<input type="submit" name="bod" id="bod" value=" Day End">
		
		
		</td>
	</tr>
	
	</table>
</s:form> 

</body>
</html>