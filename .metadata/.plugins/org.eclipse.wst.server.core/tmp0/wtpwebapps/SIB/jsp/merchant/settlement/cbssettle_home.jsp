<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="jsp/merchant/script/merchscript.js"></script> 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<sx:head />
<style>
a{
	color:blue;
}
</style>
<script>

 
	
	function searchRecords(){
		parent.showprocessing();
	}

</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
 
<s:form action="cbsHostViewSettlementProcess.do"  name="orderform" onsubmit="return searchRecords()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="30%"  align="center" >	 
	<tr>
		 
		 <td>
		 	<table border="0" cellpadding="0" cellspacing="4" width="100%" align="center"  >
		 	 
			 				<tr>
			 				<td>
			 					Account Type 
			 				</td>
			 					<td> :
			 					 <s:select label="ACCTTYPE"
								headerKey="-1" headerValue="--- Select---"
								list="setbean.merchaccttypelist" 
								listValue="%{ACCT_TYPE_DESC}"
				    			listKey="%{ACCT_TYPE}"  
				   				name="merchaccttype" id="merchaccttype" > 
				   				</s:select>  
				   				
			 					</td>
			 				</tr>
			 				 
		 		</table>
			 </td>
		</tr>  
		<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0" width="30%" >
				<tr>
				<td>
				<s:submit name="viewmerchant" id="viewmerchant" value="View" /> 
				</td> 
				<td>
					<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> 
				</td> 
			</tr>
			</table>
		</td>
	</tr>
	
</table> 

</s:form>
 
 