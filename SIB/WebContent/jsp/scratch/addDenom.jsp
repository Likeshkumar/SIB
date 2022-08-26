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
<script type="text/javascript" src="js/script.js"></script> 
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="saveDenominationDenomConfiguration.do" autocomplete="off" name="denomconfig" >
<s:hidden name="act" id="act" value="%{scratchbeans.flag}"></s:hidden>	
		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
			<tr>
				<td align="right">
					Select Product
				</td>
				<td align="center">
					:					
				</td>
				<td><!-- onchange="loaddenomvalue();" -->
					<select name="productcode" id="productcode">
							<option value="-1">-SELECT PRODUCT-</option>	
						<s:iterator  value="scratchbeans.scratchlist" >							
							<option value="${SCHPROD_CODE}">${SCHPROD_NAME}</option>
						</s:iterator>							
					</select>
				</td>
				
			</tr>			 
			<tr>
					<td align="right">Enter Denom Value</td>
				    <td align="center">:</td>
					<td> 
					 <%-- <select name="denomvalue" id="denomvalue">
							<option value="-1">-SELECT DENOM-</option>
								<s:iterator  value="limitbean.masterlimitlist" >
							<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
								</s:iterator>
						</select> --%>
						<input type="text" name="denomvalue" id="denomvalue" onKeyPress="return numerals(event);" maxlength="6" onkeyup="changeDenomDesc()"/>	
					</td>
			</tr>
			<tr>
					<td align="right">Enter Denom Description</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="denomdesc" id="denomdesc" maxlength="30" readonly/>						
					</td>
			</tr>
			<tr>
					<td align="right">Expiry Period(In Months)</td>
				    <td align="center">:</td>
					<td>	
						<input type="text"  name="expiryperiod" id="expiryperiod" onKeyPress="return numerals(event);" maxlength="2">						
					</td>
			</tr>
			<tr>
						<td align="right">Batch ID Length</td>
					    <td align="center">:</td>
						<td>	
							<input type="text" name="batchlen" id="batchlen" onKeyPress="return numerals(event);" maxlength="2">						
						</td>
			</tr>
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="Submit"  onclick="return validation_denomination();"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
	</form>
</body>
</html>