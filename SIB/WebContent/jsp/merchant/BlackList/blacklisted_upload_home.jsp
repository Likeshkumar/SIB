<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
  
<jsp:include page="/displayresult.jsp"></jsp:include>
 
<script>
	function showProcessing(){
	   parent.showprocessing();
		 
	}
</script>

 

<body onload="test()">
	<s:form action="uploadFilesActionBlackListed.do?"
		name="custinfoform" id="custinfoform" autocomplete="off" namespace="/"  onsubmit="showProcessing()" >

		<table border="0" cellpadding="0" cellspacing="0" width="100%" style='border: 1px solid #efefef' align="center" class="filtertab">

			<tr>
				<td> <input type="submit" name="upload" id="upload" value="Upload"/></td>
			</tr>
		</table>





	</s:form>

</body>