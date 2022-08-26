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
	  var fromdate = document.getElementById("fromdate");
	  var todate = document.getElementById("todate");
	  

	   if( fromdate ){
		  if( fromdate.value == "" ){  errMessage(fromdate, "Select From Date");  return false;  }
	  }
	  
	  if( todate ){
		  if( todate.value == "" ){  errMessage(todate, "Select To Date");  return false;  }
	  }
	  
	  var batchno = document.getElementById("batchno");
	  if( batchno.value == "" )
	  {  
		  errMessage(batchno, "Enter Batch No");  return false; 
	  }else {
		  var url = "batchNoValidationFeeReport.do?batchno="+batchno.value;
			var result = AjaxReturnValue(url);
			if(result == '1'){
				errMessage(batchno, "Batch No Already Exists, Try Another");
				return false;
	  		}
	  }
	  var actioncode = document.getElementById("actioncode");
	  if( actioncode.value == "" ){  errMessage(actioncode, "Select Card Activity");  return false;  }
}

</script>
</head>
<body>
<jsp:include page="/displayresult.jsp"/>    
<div align="center">
	<s:form action="feeViewFeeReport.do" autocomplete="off"  name="reportsgen" namespace="/">   
		<table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
		
		<tr>
			<td align="left" class="fnt">From Date<font class="mand">*</font> :
			</td>
		    <td>
			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
			<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		<tr>
			<td align="left" class="fnt">To Date<font class="mand">*</font> :</td>
			<td>
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
			<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>      
		</tr>
		<tr>
			<td align="left" class="fnt">Batch No<font class="mand">*</font> :</td>
			<td>
			<input type="text" name="batchno" id="batchno"  style="width:160px" maxlength="4">
			</td>      
		</tr>
		<tr>
			<td>
				Select Card Activity :
			</td>
			<td>
			<%-- <select  id="actionhead" name="actionhead" style='height:120px' multiple="multiple"  >
						 	<!-- <option value='$ALL'> ALL </option> -->
							<s:iterator value="reportbean.actionheadlist">
								<option value='${AUDIT_ACTIONCODE}'> ${AUDIT_ACTIONDESC} </option>
							</s:iterator>
						</select> --%>
			
			<select id="actioncode" name="actioncode"  style='height:120px' multiple="multiple" >
					<!-- <option value='ALL'> ALL </option>     --> 
				<s:iterator value="actionlist">	        
					<option value='${ACTION_CODE}'> ${ACTION_DESC} </option>    
				</s:iterator>	
			</select>  
			</td>
		</tr>
		</table>
		
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<td>
				<s:submit name="REPORTTYPE" id="EXCEL" value="Get Report" onclick="return validateForm()"/>	 
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