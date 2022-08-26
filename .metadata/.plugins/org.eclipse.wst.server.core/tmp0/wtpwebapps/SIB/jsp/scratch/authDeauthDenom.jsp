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
<script type="text/javascript" src="js/script.js" ></script>
<script>
function loaddenomvalue(){
		var productcode = document.getElementById("productcode").value;
		var getflag = document.getElementById("flag").value;
		//alert(getflag);
		var act = document.getElementById("act").value;
		//alert(" -- MAKER/CHECKER STATUS -- "+act);
		if(getflag=="edit"){
			if(act=='D'){var authstatus ="and MKCKSTATUS='D'";}else{var authstatus ="and MKCKSTATUS='M'";}			
		}else if(getflag=="view"){
			if(act=='D'){var authstatus ="and MKCKSTATUS='D'";}else{var authstatus ="and MKCKSTATUS='M'";}
		}else{
			var authstatus ="and AUTH_STATUS='0' and MKCKSTATUS='M'";	
		}
		var tablename="IFS_DENOM_MASTER_TEMP";
		if( productcode != -1 ){
			var url = "getDenomValueDenomConfiguration.do?productcode="+productcode+"&authstatus="+authstatus+"&tablename="+tablename;
			var result = AjaxReturnValue(url);
			//alert(result);
			document.getElementById("denomvalue").innerHTML = result;
		}else{
			errMessage(productcode,"Select Product");
		}
		return false;
}
</script>
<%String flag = (String)session.getAttribute("flag1");%>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="viewDenominationDenomConfiguration.do" autocomplete="off" name="denomconfig" >
<s:hidden name="act" id="act" value="%{scratchbeans.flag}"></s:hidden>	
<input type="hidden" name="flag" id="flag" value="<%=flag%>">
	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">			
		<tr>
				<td align="right">
					Select Product
				</td>
				<td align="center">
					:					
				</td>
				<td>
					<select name="productcode" id="productcode" onchange="loaddenomvalue();">
							<option value="-1">-SELECT PRODUCT-</option>	
						<s:iterator  value="scratchbeans.scratchlist" >							
							<option value="${SCHPROD_CODE}">${SCHPROD_NAME}</option>
						</s:iterator>							
					</select>
				</td>				
		</tr>
		<tr>
			<td align="right">
				Select Denom Values
			</td>
			<td align="center">
				:					
			</td>
			<td>
				<div id="ajax">
    				<select name="denomvalue" id="denomvalue">
    					<option value="-1">-SELECT DENOM VALUES-</option>
    					
    				</select>
    			</div>
			</td>				
		</tr>
</table>
		<table>
				<tr>
					<td>
						<s:submit value="View" onclick="return viewDenominationvalidation()"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
	</form>
</body>
</html>