<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script>
 	function validateEOD(){
 		var nxtbusdate = document.getElementById("nxtbusdate");
 		
 		if( nxtbusdate.value == "" ){
 			errMessage(nxtbusdate, "Next business date should not be empty...");
 			return false;
 		}
 		if( confirm("Do you want to continue...") ){
 			return true;
 		}
 		return false;
 	}
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

 <jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="eodActionEODAction" name="schemeprocess"  onsubmit="parent.showprocessing()" autocomplete = "off" >
	 
	<table border='0' cellpadding='0' cellspacing='0' class="subaction" width='80%' style="text-align:center">
	 
 
	<tr>
		<td style='text-align:center'> <b> Current Business date  : <s:property value="eodbean.curbusinessdate"/> </b> </td>
		<input type='hidden' name='currentbusdate' id='currrentbusdate' value=' <s:property value="eodbean.curbusinessdate"/>'/>
	</tr>
	
	
	<tr>
		<td> 
			Next Business day :  	<s:textfield name="nxtbusdate" id="nxtbusdate" value="%{cardregbean.dob}" readonly="true" style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.schemeprocess.nxtbusdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td> 
	</tr>
	<tr>
		<td>
			<input type="submit" name="bod" id="bod" value=" Day End" onclick="return validateEOD()">
		
		
		</td>
	</tr>
	
	</table>
</s:form> 

</body>
</html>