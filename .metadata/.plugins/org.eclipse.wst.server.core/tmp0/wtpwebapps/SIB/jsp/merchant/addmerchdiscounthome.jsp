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

function validation()
{
	var existdis = document.getElementById("existdis");
	var disname = document.getElementById("disname");
	var dislist = document.getElementById("dislist");
	
	if(existdis.value=="-1")
	{
		errMessage(existdis,"Select Discount Type");
		return false;
	}
	if(existdis.value=="A")
	{
		if(disname.value=="")
		{
			errMessage(disname,"Enter Discount Name");
			return false;
		}
	}
	if(existdis.value=="E")
	{
		if(dislist.value=="-1")
		{
			errMessage(dislist,"Select Discount");
			return false;		
			
		}
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
<s:form action="addMerchDiscountMerchantProcess.do" autocomplete="off">
<input type="hidden" name="instid" id="instid" value="<%=instid%>" />
	<table border="0" cellpadding="0" cellspacing="6" width="60%" class="formtable">
	 
	    <tr>
		    <td width="50%" > Discount </td>
		    <td> :
				<select name="existdis" id="existdis" onchange="showFeename(this.value)">
					<option value="-1"> - SELECT - </option>
					<option value="A"> Add New Discount </option>
					 <s:if test="%{mprbean.isfeelistexistbean}">
					<option value="E"> Add with Existing Discount </option>
					</s:if>
				</select>	 
			</td>
	    </tr>
	    
	    <s:if test="%{mprbean.isfeelistexistbean}">
	     <tr  id="trfeelist" style="display:none" >
		    <td> Discount List </td>
		    <td> :
				<s:select id="dislist"  name="dislist" list="%{mprbean.feelistbean}" headerKey="-1" headerValue="-SELECT-" listValue="DISC_DESC" listKey="DISC_MASTERCODE" /> 
			</td>
	    </tr>
	    </s:if>
	    <tr id="trfeename" style="display:none">
	    	 <td> Enter Discount Name </td>
		    <td> :
				<s:textfield name="disname" id="disname"/> 
			</td>
	    </tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="20%" style="padding-top:20px">
	<tr>
		<td> <s:submit value="Submit" onclick="return validation()"/> </td>
		<td><input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/></td>
	</tr>
	</table>
</s:form>
</body>