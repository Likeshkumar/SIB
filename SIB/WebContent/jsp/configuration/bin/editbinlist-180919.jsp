
	valid=true;
	var selectbox = document.getElementById("bin");
	if(selectbox.value=="-1")
	{
		errMessage(bin,"SELECT BIN");
		return false;
	}
	return valid;
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
<form action="editBinDetailsAddBinAction.do" method="post" autocomplete="off">
<s:hidden name="flag" id="flag" value="auth"></s:hidden>
	     <table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%" class="formtable">
			
						<tr>
							<td align="right">
								SELECT BIN:
							</td>
							<td>
								<select id="bin" name="bin">
									<option value="-1">-SELECT BIN-</option>
									<s:iterator  value="binlist" >
									<option value="${BIN}">${BIN_DESC}</option>
									</s:iterator>
								</select>
							</td>
					    </tr>
			
	      </table>
	
			<table align="center" border="0"  cellspacing="1" cellpadding="0" width="60%">
				<tr>
					<!-- <td align="center"><input type=submit value="Submit" onclick="return selectall();"/></td> -->
					<td align="center"><input type=submit value="Submit"/></td>
				</tr>
			</table>
</form>
