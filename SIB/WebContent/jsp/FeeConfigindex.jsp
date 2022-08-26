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
	
	function loaddiv(selctid)
	{	var checkdiv = document.getElementById(selctid);	
		if(checkdiv.value!="-1"){
			var NameDiv = document.getElementById("FeeNameDiv");
			var Existdiv = document.getElementById("FeeExistdiv");
			if(checkdiv.value=="new")
			{
				NameDiv.style.display = 'table-row';
				Existdiv.style.display = 'none';
			}
			else
			{
				Existdiv.style.display = 'table-row';
				NameDiv.style.display = 'none';
			}
		}
		
	}
	
	
	
	
	function validation()
	{
		var commtype = document.getElementById("feetype").value;
		var commname = document.getElementById("feename").value;
		var commcode = document.getElementById("feecode").value;
		//alert(commtype);
		if(commtype=="-1")
		{
			errMessage(feetype,"Select fee Type");
			return false;
		}
		if(commtype=="new")
		{
			if(commname=="")
			{
				errMessage(feename,"Enter fee Name");
				return false;
			}
		}
		if(commtype=="exist")
		{
			if(commcode=="-1")
			{
				errMessage(feecode,"Select fee");
				return false;		
				
			}
		}
	}
</script>

</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="addNewfeeconfigSchemeConfigAction.do" autocomplete="off">
		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
			<tr>
				<td align="right">
					SELECT FEE TYPE
				</td>
				<td align="center">
					:					
				</td>
				<td>
					<select name="feetype" id="feetype" onchange="loaddiv(this.id);">
							<option value="-1">-SELECT FEE TYPE-</option>								
							<option value="new">New FEE</option>
							<option value="exist">Add with existing FEE</option>								
					</select>
				</td>
				
			</tr>
			<tr style="display:none;" id="FeeExistdiv">
					<td align="right">SELECT FEE</td>
				    <td align="center">:</td>
					<td>
						<select name="feecode" id="feecode">
							<option value="-1">-SELECT FEE-</option>
								<s:iterator  value="runningSchemeList" >
							<option value="${FEE_CODE}">${FEE_DESC}</option>
								</s:iterator>
						</select>
					</td>
			</tr>
			<tr style="display:none;" id="FeeNameDiv">
					<td align="right">Enter FEE Name</td>
				    <td align="center">:</td>
					<td>
						<input type="text" name="feename" id="feename" value=""/>						
					</td>
			</tr>
		</table>
		<table>
				<tr>
					<td>
						<s:submit value="Next" onclick="return validation()"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
	</form>