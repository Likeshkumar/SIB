<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
  <script>
 	function getPREFiles( instid, selprodid ){  
 		if( selprodid != "-1" ){
 			prodid = selprodid; 
	 		var url = "getPREFilesInstCardPREProcess.do?instid="+instid+"&prodid="+prodid;   
	 		result = AjaxReturnValue(url);  
	 		document.getElementById("prefilename").innerHTML = result;  
 		}
 	}
 </script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
<s:form action="decriptLogAction.do"  name="orderform" enctype="multipart/form-data" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable" align="center">	 
	 
    
	<tr>
		 
    
    <tr>	
		<td> 
		File  
		</td>
		<td> :   <s:file name="fileUpload" id="fileUpload" />
		</td>
	</tr>  
</table> 

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Download" name="submit" id="order" onclick="return validFilter()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		
		<td>
		<input type="submit" name="submit" id="delete" value="Delete" onclick="return delConfirm()"  />
		</td>
		
		
		</tr>
</table>
</s:form>
 
 