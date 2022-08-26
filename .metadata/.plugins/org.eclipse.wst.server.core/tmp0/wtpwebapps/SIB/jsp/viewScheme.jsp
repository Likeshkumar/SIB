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


function goBack()
{
	window.history.back();
}
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
#textcolor
{
color: maroon;
font-size: small;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="schemeEditSchemeConfigAction" name="schemeprocess" autocomplete = "off"  namespace="/">
<fieldset><legend>FEE DETAILS</legend>	
	<table border='0' cellpadding='0' cellspacing='0' width='80%' class='subaction'>
		<s:iterator value="runningSchemeList">
			<tr>
				<td>
					<table border='0' cellpadding='0' cellspacing='0' width='80%' class='subaction'>
									<s:hidden name="feecode" id="feecode" value="%{FEE_CODE}"/>
			<tr>
				<td>FEE CODE</td>
				<td>:</td>
				<td width="20%"></td>
				<td id="textcolor">${FEE_CODE}</td>
			</tr>
			<tr>	
				<td>FEE DESCRIPTION </td>
				<td>:</td>
				<td width="20%"></td>
				<td id="textcolor">${FEE_DESC}</td>
			</tr>
						
						
						
						
						<%int i=0; %>
							<s:iterator value="schemeactionlist">
								<s:set name="actionlist">${ACTION_CODE}</s:set>
								<input type='hidden' name='hidaactionlist' id='hidaactionlist' value='${ACTION_DESC}' />
									<tr>
										<td>${ACTION_DESC}</td>
										<td>:</td>
										<td width="20%"></td>
										<td id="textcolor">
												<s:iterator value="schemeFeeList">
												<s:set name="feelist">${FEE_ACTION}</s:set>
													<s:if test="%{#actionlist==#feelist}">
													${FEE_AMOUNT}
													</s:if> 
												</s:iterator>											
										</td>
										<td width="20%"></td>
										<td id="textcolor" >
												<s:iterator value="schemeFeeList">
												<s:set name="feelist">${FEE_ACTION}</s:set>
													<s:if test="%{#actionlist==#feelist}">
													${FEE_MODE}
													</s:if> 
												</s:iterator>											
										</td>
									</tr>
							</s:iterator>
							<tr>
								<td>CHANGE STATUS</td><td>:</td>
								<td width="20%"></td>
								<td>
									<s:if test="STATUS_FLAG==1">  
										<a href="feeStatusupdateSchemeConfigAction.do?schemecode=${FEE_CODE}&flagcode=${STATUS_FLAG}" onclick='return confUpdate()'>  <img src='images/enable.gif' alt='Enable'/> </a> 
									</s:if>
									<s:else> 
										<a href="feeStatusupdateSchemeConfigAction.do?schemecode=${FEE_CODE}&flagcode=${STATUS_FLAG}" onclick='return confUpdate()'>  <img src='images/disabled.gif' alt='Disabled'/> </a>
								    </s:else>
							
								</td>
							</tr>
					</table>
				</td>
			</tr>
		</s:iterator>
	</table>
</fieldset>

<table  border='0' cellpadding='0' cellspacing='0' width='30%'>
			<tr align="center">
				<td><input type='button' name='goback' value='Back' onclick="goBack()"/></td>
			</tr>
</table>
<!-- 
			Commented Fee configuration doesn't need Edit
	<table  border='0' cellpadding='0' cellspacing='0' width='30%'>
			<tr align="center">
				<td><input type="submit" name="submit" value=" Edit " onclick="return validation_scheme();"/></td>
				<td><input type='button' name='cancel' value='Cancel'/></td>
			</tr>
	</table>
 -->	

</s:form>