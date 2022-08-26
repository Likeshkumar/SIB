<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@ page import="java.util.*" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/customeselectbox.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/customselectbox.js"></script>
<script type="text/javascript">


 function validateForm(){
	  
	  var period = document.getElementById("period");

	   if( period ){
		  if( period.value == "-1" ){  
			  errMessage(period, "Select period");  
			  return false;  
			  }
	  	}
	   
	return true;   
	   
 }
 
 function excelgeneration()
 {     
	 
	if(validateForm()){ 
 	var url = "ExcelReportGenMasterCardProductionReport.do?RNO=4";
 	var result = AjaxReturnValue(url);
 	window.location = url;
	}
 }

</script>
</head>
 <jsp:include page="/displayresult.jsp"/>    
<div align="center">
	<s:form action="generatereportCardsNotUsedReport.do" autocomplete="off"  name="reportsgen" namespace="/">
	<input type="hidden"  id="subaction" name="subaction" style='height:120px' multiple="multiple"  value='$RENEWAL'>   
		<table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
		<tr>
			<td>
				Select Period :
			</td>
			<td>
			<select  id="period" name="period"   >
					<option value='-1'> SELECT </option> 
					<option value='3'> 3 MONTHS </option> 
					<option value='6'> 6 MONTHS </option>
					<option value='9'> 9 MONTHS </option>
					<!-- <option value='12'> 12 MONTHS </option>     -->
			</select>    
			
			</td>
		</tr>	
		
 
	</table>
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<td>
				<s:submit name="REPORTTYPE" id="PDF" value="PDF" onclick="return validateForm();"/>    
			</td>
			<td>
			<s:submit name="REPORTTYPE" id="EXCEL" value="EXCEL" onclick="return validateForm();"/>
			 
		</td>
		</tr>
	</table>	
	</s:form>
</div>