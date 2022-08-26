<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>

<script type="text/javascript">

function validateForm(){
	var cardno = document.getElementById("cardno");
	var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	
	if( cardno ){
	if( cardno.value == ""){  
		errMessage(cardno, "Enter Card Number");  
		return false;  
		}
	  else if( cardno.value.length < 16 ){  
		  errMessage(cardno, "Invalid Card Number");  
		  return false;  
		  }
	}
	
	
	/* if( fromdate ){
		  if( fromdate.value == "" ){  
			  errMessage(fromdate, "Select From Date");  
			  return false;  
			  }
	  }
	  
	  if( todate ){
		  if( todate.value == "" ){  
			  errMessage(todate, "Select To Date");  
			  return false;  
			  }
	  } */
}

</script>

</head>
<body>
<jsp:include page="/displayresult.jsp"/>
<div align="center">
	<s:form action="searchCardActSwitchActivityReport" autocomplete="off" name="reportsgen">
		<table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
			<tr id="cardrow">
				<td>
					Enter Card Number 
				</td>
				<td>
					<input type='text' id='cardno' name='cardno' maxlength="19" value=""/>
				</td>
			</tr>
			
			<!-- <tr id="fromcardrow">
			<td>
				From Date 
			</td>
			<td>
				<input type='text' id='fromdate' name='fromdate' maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
				
			</td>
		</tr>
		
		<tr id="tocardrow">
			<td>
				To Date 
			</td>
			<td>
				<input type='text' id='todate' name='todate' maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr> -->
		
		
		</table>
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<td>
				<s:submit name="getreport" id="getreport" value="  Get Report  " onclick="return validateForm()"/>
			</td>
			<td>
				<s:reset value="Reset"/>
			</td>
		</tr>
		</table>	
	</s:form>
</div>

</body>
</html>