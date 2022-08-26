<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>File Upload</title>
<script>
function showBulkStatus(filename)
{
	alert(filename);   
	var url = "editViewCustomerActionInstCardRegisterProcess.do?actiontype=VIEW&inputtype=cardno&filename="+filename; 
	newwindow = window.open(url,'','left=350,top=150,width=800,height=400,location=no,menubar=no,menu=no,status=no,scrollbars=yes,resizable=yes,toolbar=no,titlebar=0,fullscreen = yes,directories=no');
	
	  
}
</script>

</head>


<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="UploadFileDebitBulkCustomerRegister.do" method="POST" enctype="multipart/form-data">
<table border='0' cellpadding='0' cellspacing='0' width='90%'> 
			<tr>
				<td colspan='4' style='padding-top:30px'>
					<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
					<s:if test="uploadStatus.size()>0">	
						<tr> 
							<th style="text-align:center">FILE NAME</th>
							<th style="text-align:center">UPLOAD DATE</th>
							<th style="text-align:center">UPLOADED BY</th>		
							<th style="text-align:center">TOTAL RECORD</th>
							<th style="text-align:center">SUCCESS</th>		
							<th style="text-align:center">FAIL</th>		  
							<th style="text-align:center">REJECT</th>	
							
																
						</tr>
						</s:if>		   
						<% int rowcnt = 0; Boolean alt=true; %> 
						<!--<s:iterator value="uploadStatus">	
												
							<tr	<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>	> 
								<td>${FILENAME}</td>
								<td>${UPLOADDATE}</td>
								<td>${UPLOADEDBY}</td>
								<td>${TATAL_RECORD}</td>    
								<td>${SUCCESS}</td>
								<td style="color:red" onclick="showBulkStatus('${FILENAME}')" >${FAIL}</td>      
								<td>${REJECT_REC}</td>
								
							</tr>    
						</s:iterator>-->    
					</table>
				</td>
			</tr>
</table>			
</s:form>
   
   
</body>
</html>