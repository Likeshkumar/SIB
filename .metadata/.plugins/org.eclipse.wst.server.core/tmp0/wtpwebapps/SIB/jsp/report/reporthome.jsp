<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 
<title>Insert title here</title>
</head>
<script>
function chkChars(field,id,enteredchar)
{
	//alert('1:'+field+'2:'+id+'3:'+enteredchar);
	charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
    	//alert(document.getElementById(id).value.charAt(i));   
    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

function validateNumber (field,id,enteredchar)
{
	charvalue= "0123456789";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
    	
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
       	//alert(document.getElementById(id).value.charAt(i));
       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

function validation()
{
	var field = document.getElementsByName("field");
	var fielddesc = document.getElementsByName("fielddesc");
	var minlength = document.getElementsByName("minlength");
	var rno =  document.getElementById("rno").value;
	//alert(field.length);
	var jsmsg = "?RNO="+rno;
	for(var i=0;i<field.length;i++) {	
		//alert(i);
		var textfield = document.getElementById("field_"+fielddesc[i].value).value;
		var textlength = document.getElementById("length_"+fielddesc[i].value).value;
//		alert('textfield::'+document.getElementById("field_"+fielddesc[i].value).value);
		//alert('fieldlength:'+textfield.length);
		//alert(document.getElementById("field_"+fielddesc[i].value));
		if(textfield=="" || textfield=="-1")
				{
	///		alert(document.getElementById("field_"+fielddesc[i].value));
				errMessage(document.getElementById("field_"+fielddesc[i].value),"<b>"+fielddesc[i].value+"</b> : Is Empty");
				return false;	
				}
		else if( (textlength != textfield.length )){
		errMessage(document.getElementById("field_"+fielddesc[i].value),"<b>"+fielddesc[i].value+"</b> value length should be "+textlength+" digit");
			return false;
		}
		
		jsmsg=jsmsg+"$"+fielddesc[i].value+"="+textfield;	
		
		//alert(jsmsg);    
		var urllink = "reportGenMasterHomeReportGenration.do"+jsmsg;          
		var url = "reportGenMasterReportGenration.do?RNO=1&URL="+urllink;
		//alert(url);
		window.location=url;
		
	//	alert(url);
	//	var result = AjaxReturnValue(url);
	//	alert(result);
	//	document.getElementById("filenamediv").innerHTML="Report Generated Click Here to Download ..."; 
	//	document.getElementById("afile").href=result; 
	//	afile
}
	   
	
}

function excelgeneration()
{
	var url = "ExcelReportGenMasterReportGenration.do?RNO=1";
	var result = AjaxReturnValue(url);
	window.location = url;
}
</script>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form id="reportform">
<s:hidden type="text" name="rno" id="rno" value="%{rno}" />
<s:hidden type="text" name="reportlocation" id="reportlocation" value="%{reportlocation}" />


<table border="0" cellpadding="0" cellspacing="5" width="50%" id="maxcount" class="formtable">


	
	<s:iterator value="reportList">  
				<tr>
				<td class="fnt"> <s:property value="DISP_NAME" /> </td>
				<td> 
				<input type="hidden" name="minlength" id="length_${COLUMN_NAME}" value="${MINLENGTH}" />
				<input type="hidden" name="fielddesc" id= "fielddesc_${COLUMN_NAME}" value="${COLUMN_NAME}" />
				<input id="field_${COLUMN_NAME}" name="field" maxlength="${MAXLENGTH}" 
				
				<s:if test='VALID_TYPE=="INT"' >
				onkeyup="validateNumber('${DISP_NAME}',this.id,this.value)"
				</s:if>
				<s:if test='DATE_FIELD=="N"' >
				type="text" 
				</s:if>
				/>
				
				</td>
				
				
		</tr>
	</s:iterator>
	
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			
			<input type="button" name="cancel" id="cancel" value="SUBMIT PDF"  class="cancelbtn"  onclick="return validation()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Excel"  class="cancelbtn"  onclick="return excelgeneration()"/>
			 
		</td>
		</tr>
	</table>
	<table>
	<tr>
	<td >
  <%String filename="TESTPDF.pdf"; %>
</a>
<A  id = "afile" download><h style="color:black" id="filenamediv" target="_blank"> </h></A><BR><BR>
</td>
</tr>
	</table> 
</s:form>
</body>
</html>