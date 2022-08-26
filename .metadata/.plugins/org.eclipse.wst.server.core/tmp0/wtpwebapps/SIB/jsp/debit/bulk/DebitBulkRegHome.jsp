<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>
<title>File Upload</title>
<script>
function showBulkStatus(filename)
{
	//alert(window.location);   
		var url = "reportGenMasterReportGenration.do?RNO=2&URL=jsp/report/ReportGenerator.jsp?RNO=2$filename="+filename;
		window.location=url;
}       
	           
	


function excelgeneration()
{
	var url = "ExcelReportGenMasterReportGenration.do?RNO=1";
	var result = AjaxReturnValue(url);
	window.location = url;
}

function validBulk()
{
	var LoadSQLFile = document.getElementById("uploadFile");
	
	//alert(accountsubtypeid.value.length + accountsubtypelenval);
	
	if( uploadFile.value == "" ){
		errMessage(LoadSQLFile, "Select File ");
		return false;
	}
}
function showFailReport(reportrandomno){
	alert("view "+reportrandomno);
	var url = "viewFailedRecordsbulkregBulkCustomerRegister.do?filename="+reportrandomno;
	window.location = url;
}
</script>




<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="InsertbulkregrecordsBulkCustomerRegister.do" method="POST" onsubmit="parent.showprocessing()" enctype="multipart/form-data">
<table border="0" cellpadding="0" cellspacing="0" width="40%" class="formtable">	 
	<tr>
		<td> Upload File </td>
		<td> : <s:file name="uploadFile" id="uploadFile" label="Choose File" size="40" />  <br/>
		
			<small id="smalldt" class="dt"> 
			 </small>
		</td> 
	</tr>
	 
</table>


<table border="0" cellpadding="10" cellspacing="4" width="20%" >
		<tr>
		<td>
		<input type="button" name="Reject Report" id="Reject Report" value="Reject Report" class="Reject Reportbtn" onclick="return confirmReject Report()"/>
		</td>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return validBulk()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>


<table border='0' cellpadding='0' cellspacing='0' width='90%'> 
			<tr>
				<td colspan='4' style='padding-top:30px'>
					<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
					<s:if test="uploadStatus.size()>0">	
						<tr> 
						<!-- 	<th style="text-align:center">FILE NAME</th> -->
							<th style="text-align:center">UPLOAD DATE</th>
						<th style="text-align:center">UPLOADED BY</th>
						<th style="text-align:center">REPORTNO</th>			
						<th style="text-align:center">TOTAL RECORD</th>
						<th style="text-align:center">SUCCESS</th>		
						<th style="text-align:center">FAIL</th>				
							
																
						</tr>
						</s:if>		   
						
					<% int rowcnt = 0; Boolean alt=true; %> 
					<s:iterator value="uploadStatus" var="stat">						
						<tr	<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>	> 
							<%-- <td>${FILENAME}</td> --%>
							
							<td>${UPLOADDATE}</td>
							<td>${UPLOADEDBY}</td>
							<td>${REPORTRANDOMNO}</td>  
							<td>${TOTALRECORD}</td>    
							<td>${SUCCESS}</td>
							
							<s:if test="%{#stat.FAIL==0}">
								<td><span style="color:red;">${FAIL}</span><br></td>
							</s:if>  
							<s:else>
							 
								<td style="cursor: pointer;" onclick="showFailReport('${REPORTRANDOMNO}')" ><span style="color:red;">${FAIL}</span><br> <span style="color:blue;">Get Report</span></td>
							</s:else>    
						</tr>  
						 
					</s:iterator> 
					</table>
				</td>
			</tr>
</table>			
</s:form>
   
   
</body>
</html>