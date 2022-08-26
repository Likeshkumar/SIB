<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %> 
<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function showFeename( feeconfig ){
	if( feeconfig == "A"){
		document.getElementById("trfeename").style.display = 'table-row';
		document.getElementById("trfeelist").style.display = 'none';
	}else{
		document.getElementById("trfeelist").style.display = 'table-row';
		document.getElementById("trfeename").style.display = 'none';
	}
}

function validateFeeHome()
{
  
	var feelist = document.getElementById("feelist");
	var feename = document.getElementById("feename");
	var existfee = document.getElementById("existfee");
	var trfeelist = document.getElementById("trfeelist");
	if( existfee.value == "-1"){ errMessage(existfee, "Select Fee Type..."); return false; }
	
	if( trfeelist.style.display != "none" ){
	 if( feelist ){
		 if( feelist.value == "-1"){ errMessage(feelist, "Select Fee..."); return false;}
	 }
	}
	 else if( feename ){
		 if( feename.value == ""){ errMessage(feename, "Enter Fee Name ..."); return false;}
	 }
 
 parent.showprocessing();
}
</script>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<body>
<%
String instid = (String)session.getAttribute("Instname"); 
String displayalerts = (String)session.getAttribute("alertdescs");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addMerchFeeMerchantProcess.do"  onsubmit="return validateFeeHome()" autocomplete="off">
<input type="hidden" name="instid" id="instid" value="<%=instid%>" />
	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
	 
	    <tr>
		    <td width="50%" > Fee    </td>
		    <td> :
		   
				<select name="existfee" id="existfee" onchange="showFeename(this.value)">
					<option value="-1"> - SELECT - </option>
					<option value="A"> Add New Fee </option>
					 
						<option value="E"> Add with Existing Fee </option>
				 
				</select>	 
			</td>
	    </tr>
	    
	    <s:if test="%{mprbean.isfeelistexistbean}">
	     <tr  id="trfeelist" style="display:none" >
		    <td> Fee List </td>
		     <td> :
				<s:select id="feelist"  name="feelist" list="%{mprbean.feelistbean}" headerKey="-1" headerValue="-SELECT-" listValue="FEE_DESC" listKey="FEE_CODE" /> 
			</td>  
	    </tr>
	    </s:if>
	    <tr id="trfeename" style="display:none">
	    	 <td> Enter Fee Name </td>
		    <td> :
				<s:textfield name="feename" id="feename"/> 
			</td>
	    </tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="20%" style="padding-top:20px">
	<tr>
		<td> <s:submit value="Submit" name="submit" id="submit"  /> </td>
		<td><input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/></td>
	</tr>
	</table>
</s:form>
</body>