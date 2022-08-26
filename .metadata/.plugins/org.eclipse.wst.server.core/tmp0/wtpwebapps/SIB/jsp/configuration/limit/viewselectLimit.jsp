<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript">
function delConfirm()
{
var r=confirm("Are You Sure 'Delete'");
return r;
	
}
function selectall()
{
	valid=true;
	var selectbox = document.getElementById("limitid");
	if(selectbox.value=="-1")
	{
		errMessage(limitid,"SELECT LIMIT DESC");
		return false;
	}
	return valid;
}

function getListOfCurrency( limitid ){
	//alert( limitid );
	var url = "listofLimitCurrencyAddLimitAction.do?doaction=EDIT&limitid="+limitid;
	//alert( url );
	var result = AjaxReturnValue(url);
	document.getElementById("curcode").innerHTML=result;
	return false;
}


</script>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>

<form action="editLimitdetailsAddLimitAction.do" method="post" autocomplete="off">
	<div align="center">
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="50%" class="formtable">
				<tr>
					<td align="right">LIMIT: </td>
					<td>
						<select name="limitid" id="limitid"  onchange="getListOfCurrency(this.value);">
									<option value="-1">-SELECT LIMIT -</option>
											<s:iterator value="limitdesclist">
										<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
											</s:iterator>
						</select>
					</td>
				</tr>
				
				<tr>
							<td align="right">
								CURRENCY :
							</td>
							<td>
								<select id="curcode" name="curcode">
									<option value="-1">-SELECT CURRENCY-</option> 
								</select>
							</td>
					    </tr>
					    
			</table>
			<table  border="0"  cellspacing="0" cellpadding="0" width="90%">
				<tr align="center">
					<td>
						<s:submit value="View"  onclick="return selectall()"></s:submit>
					</td>
				</tr>
			</table>
	</div>
</form>
</body>
</html>