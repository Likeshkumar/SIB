<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
 
<script>
 

	function validateAcctRules(){
		var acctrules = document.getElementById("acctrulecode");
		if(acctrules){
			if(acctrules.value == "-1"){
				errMessage(acctrules,"Select Account Rule");
				return false;
			}
		}
		parent.showprocessing();		
	}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

 

<s:form action="showAccountRuleDetailAcctRule.do" name="orderform" onsubmit="return validateAcctRules()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="30%"	align="center" class="formtable"> 
		 
		<tr>
			 <td> Account Rule  </td>
			<td>:   
			<s:select list="%{accrulebean.acctrulelist}" id="acctrulecode" name="acctrulecode" listKey="ACCOUNTRULECODE" listValue="ACCT_RULEID"
					headerKey="-1" headerValue="-SELECT-" /> 
					
			</td>

		</tr>


	</table>


	<table border="0" cellpadding="0" cellspacing="4" width="20%">
		<tr>
			<td><s:submit value="View" name="next" id="next"
					onclick="return validFilter()" /></td>
			<td><input type="button" name="cancel" id="cancel"
				value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

			</td>

		</tr>
	</table>
</s:form>

