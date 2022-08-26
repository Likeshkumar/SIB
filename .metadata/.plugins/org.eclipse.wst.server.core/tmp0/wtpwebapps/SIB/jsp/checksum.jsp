<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/script.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript">

function validation()
{
	var uploadFile = document.getElementById("uploadFile");
	
	
	if( uploadFile.value == "" ){
		errMessage(uploadFile, "Upload file is empty...");
		return false;
	}
}
var input = document.getElementById('uploadFile');
var list = document.getElementById('fileList');

//empty list for now...
while (list.hasChildNodes()) {
	list.removeChild(ul.firstChild);
}

//for every file...
alert("ssa");
for (var x = 0; x < input.input.length; x++) {
	//add to list
	var li = document.createElement('li');
	li.innerHTML = 'File ' + (x + 1) + ':  ' + input.files[x].name;
	list.append(li);
}
function showFailReport(reportrandomno){
	alert("view "+reportrandomno);
	var url = "viewFailedRecordsPersonalCardReceiveIssueAction.do?filename="+reportrandomno;
	window.location = url;
}

function makeFileList() {
	var input = document.getElementById("uploadFile");
	var ul = document.getElementById("fileList");
	while (ul.hasChildNodes()) {
		ul.removeChild(ul.firstChild);
	}
	/*  if( input.files.length>5){
		errMessage(input, "Upload file is empty...");
		return false;
	}  */
	for (var i = 0; i < input.files.length; i++) {
		var li = document.createElement("li");
		li.innerHTML = input.files[i].name;
		ul.appendChild(li);
	}
	if(!ul.hasChildNodes()) {
		var li = document.createElement("li");
		li.innerHTML = 'No Files Selected';
		ul.appendChild(li);
	}
}

</script>
<style>
#divScrollform{
    position: absolute;
    border: 1px solid gray;
    padding: 1px;
    background: white;
    width: 1000px;
    height: 100px;
    overflow-y: scroll;
}
</style>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="ChecksumprocessChecksumAction.do" method="POST" onsubmit="parent.showprocessing()" enctype="multipart/form-data">
<s:hidden name="act" id="act" value="%{act}" />
<table border="0" cellpadding="0" cellspacing="0" width="40%" class="formtable">	 
	<tr>
		<td> Upload File </td>
		<td> : <s:file name="uploadFile" id="uploadFile" multiple="multiple" type="file" size="40" onchange="makeFileList();" />  <br/>
			   <small id="smalldt" class="dt"></small>
			 
		</td> 
	</tr>	 
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
	<td>
		<s:submit value="Submit" name="order" id="order" onclick="return validation()"/>
	</td>
	<td>
		<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>	 
	</td>
	</tr>
</table>
</s:form>
</body>
</html>