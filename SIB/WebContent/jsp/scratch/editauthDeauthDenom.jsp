<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/scratchcard.js" ></script>
<script>
function loaddenomvalue (){
		var productcode = document.getElementById("productcode").value;
		var authstatus ="MKCKSTATUS='M'";var tablename="IFS_DENOM_MASTER_TEMP";
		if( productcode != -1 ){
			var url = "getDenomValueDenomConfiguration.do?productcode="+productcode+"&authstatus="+authstatus+"&tablename="+tablename;
			var result = AjaxReturnValue(url);
			document.getElementById("denomvalue").value = result;
		}else{
			document.getElementById("denomvalue").value = "No Records";
		}
}
function validation_authdenomvalue(){
	var schprodcode=document.getElementById("schprodcode").value; 
	//alert(schprodcode);
	var demonvalue=document.getElementById("demonvalue").value; 	
	//alert(demonvalue);
 	var auth = document.getElementById("auth0").value; 	
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		 var url = "denomAuthorizeDeauthorizeDenomConfiguration.do?schprodcode="+schprodcode+"&reason="+reason+"&auth="+auth+"&demonvalue="+demonvalue;
		 window.location = url; 
	 }  
	 return false;
}
</script>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="updatedenomDenomConfiguration.do" autocomplete="off" name="denomconfig" >
	<s:hidden name="act" id="act" value="%{scratchbeans.flag}"></s:hidden>	
	 <s:iterator  value="scratchbeans.scratchlist">
	 <input type="hidden" name="schprodcode" id="schprodcode" value="${SCHPROD_CODE}">
	 <input type="hidden" name="demonvalue" id="demonvalue" value="${DENOM_VALUE}">
			<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
				<tr>
					<td align="right">
						Select Product
					</td>
					<td align="center">
						:					
					</td>
					<td>
						<s:property value="%{SCHPROD_NAME}"></s:property>
					</td>
					
				</tr>			 
				<tr>
						<td align="right">Enter Denom Value</td>
					    <td align="center">:</td>
						<td> 
						 	<s:property value="%{DENOM_VALUE}"></s:property>
						</td>
				</tr>
				<tr>
						<td align="right">Batch ID Length</td>
					    <td align="center">:</td>
						<td>	
							<s:property value="%{BATCH_LEN}"></s:property>						
						</td>
				</tr>
				<tr>
						<td align="right">Enter Denom Description</td>
					    <td align="center">:</td>
						<td>
							<s:property value="%{DENOM_NAME}"></s:property>						
						</td>
				</tr>
				<tr>
						<td align="right">Expiry Period(In Months)</td>
					    <td align="center">:</td>
						<td>	
							<input type="text" name="expiryperiod" id="expiryperiod" onKeyPress="return numerals(event);" value="${EXPIRY_PERIOD}" maxlength="2">						
						</td>
				</tr>
			</table>
	</s:iterator>
			<table>
				<tr>
					<td>
							<input type="submit" name="auth" id="auth1" value="Update"/>
					</td>
				</tr>
		</table>
	</form>
</body>
</html>