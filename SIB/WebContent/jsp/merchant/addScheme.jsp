<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
	function validation()
	{
		//alert("validation");
		var bin = document.getElementById("bin");
		var schemename = document.getElementById("schemename");
		var schemedes = document.getElementById("schemedes");
		if(bin.value == "")
		{
			errMessage(bin,"Enter the BIN Number ");
			return false;
		}
		if(schemename.value == "")
		{
			errMessage(schemename,"Enter the Scheme Name ");
			return false;
		}
		if(schemedes.value == "")
		{
			errMessage(schemedes,"Enter the Scheme Description");
			return false;
		}

		return true;
	}

</script>
</head>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="saveschemeMerchantProcess.do" method="get" autocomplete="off">
	<table border="0" cellpadding="0" width="50%" cellspacing="0" class="formtable">
		<tr>
			<td>BIN <span class="mand">*</span></td><td>&nbsp; : <s:textfield name="bin" id="bin" style="width: 250px" maxlength="6" onkeypress="return numerals(event);"/></td>
		</tr>
		
		<tr>
			<td>SCHEME_NAME <span class="mand">*</span></td><td>&nbsp; : <s:textfield name="schemename" id="schemename" style="width: 250px" maxlength="16"/></td>
		</tr>

		<tr>
			<td>SCHEME_DESC <span class="mand">*</span> </td>
			<td>
				&nbsp; : <s:textfield name="schemedes" id="schemedes"  maxlength="16" style="width: 250px" />
			</td>
		</tr>
		<tr> 
			 
			<td colspan="2"><center><s:submit value="submit" onclick="return validation()"/>&nbsp;<input type="button" onclick="return confirmCancel()" class="cancelbtn" value="Cancel" id="cancel" name="cancel"></center></td>
			
		</tr>
	</table>
</form>
</body>
</html>