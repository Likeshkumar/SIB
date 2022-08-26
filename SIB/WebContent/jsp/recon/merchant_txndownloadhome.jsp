<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
  <script>
 	function getPREFiles(   selprodid ){  
 		alert( selprodid );
 		
 		if( selprodid != "-1" ){
 			prodid = selprodid; 
	 		var url = "getPREFilesInstCardPREProcess.do?instid="+instid+"&prodid="+prodid;   
	 		result = AjaxReturnValue(url);   
	 		document.getElementById("prefilename").innerHTML = result;
 		}else{
 			document.getElementById("prefilename").innerHTML = result;	
 		}
 		  
 	}
 	
 	function getDownloadingFileList( bin ){
 		var opt = "<option value='-1'>-SEELCT-</option>";
 		if( bin != "-1" ){ 
	 		var url = "getDownloadingFileListRecon.do?bin="+bin;   
	 		result = AjaxReturnValue(url);   
 		}
 		document.getElementById("reconfilename").innerHTML = opt+result; 
 	}
 	
 	function countDownTime ( reconfilename ){
 		
 	}
 </script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
 
<s:form action="downMMSFileRecon.do"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">	 
	 
    
 	<tr>
        	<td colspan="3">
        		 <table border='0' cellpadding='0' cellspacing='0' width='50%'>
        		 <tr> <td> Bin </td> 
        		 <td> : 
				 
					<s:select name="bin" id="bin" listKey="BIN" listValue="BIN_DESC" list="reconbean.binlist"  headerValue="-SELECT-"  
					headerKey="-1" value="%{BIN_DESC}" tooltip="Select BIN" onchange="getDownloadingFileList(this.value)"/>
				</td> </tr>
				
				<tr>	
					<td> 
					File Name
					</td>
					<td> :  <select name="reconfilename" id="reconfilename" onchange="countDownTime(this.value)"> 
								<option value="-1">-SELECT-</option>
							</select>
					</td>
				</tr>  
				
				
        		 </table>
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
 
 