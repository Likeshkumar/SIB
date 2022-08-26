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
		var tablename="IFS_DENOM_MASTER_TEMP";
		var productcode = document.getElementById("productcode").value;
		if( productcode != -1 ){
			var url = "getDenomValueDenomConfiguration.do?productcode="+productcode+"&tablename="+tablename;
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
<style>
#textcolor
{
color: maroon;
font-size: small;
}
</style>
<body>
<%String flag = (String)session.getAttribute("flag");
  String flag1 = (String)session.getAttribute("flag1");%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="denomAuthorizeDeauthorizeDenomConfiguration.do" autocomplete="off" name="denomconfig" >
	<s:hidden name="act" id="act" value="%{scratchbeans.flag}"></s:hidden>	
	 <s:iterator  value="scratchbeans.scratchlist">
	 <input type="hidden" name="schprodcode" id="schprodcode" value="${SCHPROD_CODE}">
	 <input type="hidden" name="demonvalue" id="demonvalue" value="${DENOM_VALUE}">
			<table border="0" cellpadding="0" cellspacing="0" width="50%" class="viewtable">
				<tr>
					<th class="textcolor">
						Product
					</th>
					<td>
						:					
					</td>
					<td>
						<s:property value="%{SCHPROD_NAME}"></s:property>
					</td>
					
				</tr>			 
				<tr>
						<th class="textcolor">Denom Value</th>
					    <td>:</td>
						<td> 
						 	<s:property value="%{DENOM_VALUE}"></s:property>
						</td>
				</tr>
				<tr>
						<th class="textcolor">Denom Description</th>
					    <td>:</td>
						<td>
							<s:property value="%{DENOM_NAME}"></s:property>			
						</td>
				</tr>
				<tr>
						<th class="textcolor">Expiry Period(In Months)</th>
					    <td>:</td>
						<td>	
							<s:property value="%{EXPIRY_PERIOD}"></s:property>									
						</td>
				</tr>
				<tr>
						<th class="textcolor">Batch ID Length</th>
					    <td>:</td>
						<td>	
							<s:property value="%{BATCH_LEN}"></s:property>									
						</td>
				</tr>
				<tr>
						<th class="textcolor">Current Status</td>
					    <td>:</td>
						<td>	
							<s:property value="%{AUTH_STATUS}"></s:property>									
						</td>
				</tr>
				<tr>
						<th class="textcolor">Added Date</th>
					    <td>:</td>
						<td>	
							<s:property value="%{ADDED_DATE}"></s:property>									
						</td>
				</tr>
				
				<tr>
						<th class="textcolor" align="right">Added By</th>
					    <td>:</td>
						<td>	
							<s:property value="%{ADDED_NAME}"></s:property>									
						</td>
				</tr>
				<tr>
						<th class="textcolor">Auth Date</th>
					    <td>:</td>
						<td>	
							<s:property value="%{AUTH_DATE}"></s:property>									
						</td>
				</tr>
				
				<tr>
						<th class="textcolor">Auth By</th>
					    <td align="center">:</td>
						<td>	
							<s:property value="%{AUTH_NAME}"></s:property>									
						</td>
				</tr>
				<tr>
						<th class="textcolor">Remarks</th>
					    <td>:</td>
						<td>	
							<s:property value="%{REASON}"></s:property>									
						</td>
				</tr>
			</table>
	</s:iterator>
			<table>
				<tr>
					<td><s:if test='scratchbeans.flag== "M"'></s:if>
					<s:elseif test='scratchbeans.flag== "D"'></s:elseif>
						<s:else>
							<input type="submit" name="auth" id="auth1" value="Authorize"/>
							<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authdenomvalue();"/>
						</s:else>
					</td>
				</tr>
		</table>
	</form>
</body>
</html>