<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script>
function viewbin(bin)
{
	popupWindow = window.open('binDetailsAddBinAction.do?bin='+bin,'','left=350,top=150,width=600,height=400,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
	popupWindow.focus();

}

function selectall()
{
	valid=true;
	var selectbox = document.getElementById("limit_id");
	if(selectbox.value=="-1")
	{
		errMessage(limit_id,"SELECT LIMIT");
		return false;
	}
	return valid;
}

function getListOfCurrency( limitid ){
	//alert( limitid );
	var url = "listofLimitCurrencyAddLimitAction.do?limitid="+limitid;
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
<%
	String instid = (String)session.getAttribute("Instname"); 
 
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="ajaxlimitAddLimitAction.do" method="post" autocomplete="off">
	     <table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
			
						<tr>
							<td align="right">
								LIMIT:
							</td>
							<td>
								<select id="limit_id" name="limit_id" >
									<option value="-1">-SELECT LIMIT-</option>
									<s:iterator  value="masterfeelist" >
									<option value="${LIMIT_ID}">${LIMIT_DESC}</option>
									</s:iterator>
								</select>
							</td>
					    </tr>    
					    
					    
					    
					    
			
	      </table>
	
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%">
				<tr>
					<td align="center"><input type=submit value="view" onclick="return selectall();"/></td>
				</tr>
			</table>
</form>
