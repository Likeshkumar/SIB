<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
<script>
function validation_scheme()
{
	var action_code=document.getElementById("action_code");	
	var getlength=document.getElementsByName("hidaactionlist").length;
	//alert("length"+getlength);
	valid=true;
	if(document.getElementById("schemecode").value == "")
	{
		errMessage(schemecode,"Fee Code is empty" );
		return false;
	}
	
	if(document.getElementById("schemedesc").value == "")
	{
		errMessage(schemedesc,"Fee Description is empty" );
		return false;
	}
	chker = false;
	for(var i=0;i<getlength;i++)
	{
		var getvalue=document.getElementById("action_code"+i);
		//alert("getvalue"+getvalue.value);
		if(getvalue.value == "")
		{
			chker=true;
			break;
		}
	}
	if(chker)
	{
		//alert("value not entered ");
		errMessage(schemecode,"Fee cannot be empty" );
		return false;
	}
	else{
		return true;
	}
}
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="updateFeeConfigurationSchemeConfigAction" name="schemeprocess" autocomplete = "off"  namespace="/">
	<table border='0' cellpadding='0' cellspacing='0' width='90%'>
		<s:iterator value="runningSchemeList">
			<tr>
				<td>Fee Code</td>
				<td><input type="text" name="feecode" id="feecode" value="${FEE_CODE}" readonly="readonly"    KeyPress="return numerals(event);" onchange="checkSchemeExist()" maxlength="6"></td>
				
				<td>FEE DESCRIPTION</td>
				<td><input type="text" name="feedesc" id="feedesc" value="${FEE_DESC}" onKeyPress="return alphabets(event);" maxlength="25"></td>
			</tr>
		</s:iterator>
		<tr align="center">
			<td colspan="4">
				<table border='0' cellpadding='0' cellspacing='0' width='50%' class='subaction'>
					<%int i=0; %>
					<s:iterator value="schemeactionlist">
					<s:set name="actionlist">${ACTION_CODE}</s:set>
					<input type='hidden' name='hidaactionlist' id='hidaactionlist' value='${ACTION_DESC}' />
					<tr>
						<td>${ACTION_DESC}</td>
						<td>:</td>
						<td>
							<input type='text' name='${ACTION_CODE}' id='action_code<%=i++%>' 
										<s:iterator value="schemeFeeList">
												<s:set name="feelist">${FEE_ACTION}</s:set>
													<s:if test="%{#actionlist==#feelist}">
													value='${FEE_AMOUNT}'
													</s:if> 
										</s:iterator>
							 onKeyPress="return numerals(event);" style='width:50px'  maxlength='4'>
						</td>					
					</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
	<table  border='0' cellpadding='0' cellspacing='0' width='30%'>
		<tr align="center">
			<td><input type="submit" name="submit" value=" Update " onclick="return validation_scheme();"/></td>
			<td><input type='button' name='cancel' value='Cancel'/></td>
		</tr>
	</table>

</s:form>