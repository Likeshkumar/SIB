<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>
<script>
	function showFeeStruct( schemecode )
	{
		window.open('viewSchemeFeeStructSchemeConfigAction.do?schemecode='+schemecode,'','left=350,top=150,width=300,height=400,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
	}
	function confUpdate(){
		if ( confirm('Do you want to Change Status ? ') ){
			return true;	
		}
		return false;
		
	}
	
	function confEdit(){
		var valid = false;
		if ( confirm('Do you want to Edit ? ') ){
			valid = true;	
		}
		 
		return valid;		
	}
	function confDel(){
		var valid = false;
		if ( confirm('Do you want to Delete ? ') ){
			valid = true;	
		}
		return valid;	
	}
	function selectall()
	{
		valid=true;
		var selectbox = document.getElementById("feecode");
		if(selectbox.value=="-1")
		{
			errMessage(feecode,"SELECT FEE");
			return false;
		}
		return valid;
	}
	
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
	text-align:center;
}

table.formtable td{
	border:1px solid gray;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="viewSchemedetailsSchemeConfigAction.do" autocomplete="off">
		<table align="center" border="0"  cellspacing="1" cellpadding="0" width="50%">
				<tr>
					<td align="right">SELECT FEE:</td>
					<td>
						<select name="feecode" id="feecode">
							<option value="-1">-SELECT FEE-</option>
								<s:iterator  value="runningSchemeList" >
							<option value="${FEE_CODE}">${FEE_DESC}</option>
								</s:iterator>
						</select>
					</td>
				</tr>
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="View"  onclick="return selectall()"/>
					</td>
				</tr>
		</table>
	</form>