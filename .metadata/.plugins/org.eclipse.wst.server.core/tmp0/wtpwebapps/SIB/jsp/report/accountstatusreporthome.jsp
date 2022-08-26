<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">

function validateForm(){
	var cardno = document.getElementById("cardno");

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
}

function getAccountstatus(){
	
	var cardno = document.getElementById("cardno").value;
	document.getElementById("displayresult1").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";
	var url = "generateAcctStatusSwitchActivityReport.do?cardno="+cardno; 
	var result = AjaxReturnValue(url); 
	var jsonobj = JSON.parse(result);  
	var tablerec = "<table border='0' cellpadding='0' cellspacing='0' width='100%' class='formtable'>";
	if( jsonobj["RESP"] != 0 ){
		 tablerec +="<tr><td colspan='7' style='text-align:center'><b>"+ jsonobj["REASON"] +"</b></td></tr>";
	}else{
		tablerec += "<tr><th>Card No</th> <th>Oredr Ref No</th> <th>Account No</th> <th>Account Id</th> </tr>";
		
		var count = jsonobj["COUNT"] ;
		
		for (var i=0; i<count; i++ ){
			
			tablerec +="<tr>";
			tablerec += "<td>"+jsonobj['CHN']+"</td>";
			
			tablerec += "<td>"+jsonobj['ORDER_REF_NUM']+"</td>";
			tablerec += "<td>"+jsonobj['ACCOUNT_NO']+"</td>";
			tablerec += "<td>"+jsonobj['ACCTTYPE_ID']+"</td>";
			tablerec += "</tr>";
		}
	}
	tablerec += "</table>";
	
	document.getElementById("displayresult1").innerHTML = tablerec;
	
	return false;
}

</script>

</head>
<body>
<jsp:include page="/displayresult.jsp"/>

<div align="center">
	<s:form action="" autocomplete="off" name="reportsgen" onsubmit="return getAccountstatus()">
		<table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
			<tr id="cardrow">
				<td>
					Enter Card Number 
				</td>
				<td>
					<input type='text' id='cardno' name='cardno' maxlength="16" />
				</td>
			</tr>
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
		
		<tr>
			<td>
				<div id="displayresult1">&nbsp;</div>
			</td>
		</tr>
	</s:form>
</div>
			
</body>
</html>