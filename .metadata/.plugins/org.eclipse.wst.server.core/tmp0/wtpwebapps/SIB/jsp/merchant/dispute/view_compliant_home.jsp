<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function getDisputeviewdetails()
{
	var dispute = document.getElementById("disputeid");
	//alert("Dipute Is==> "+dispute);
	if(dispute.value == "-1")
	{
		return false;
	}else
		{
			var url="viewDisputeRegisterdetailsMerchantProcess.do?disputeid="+dispute.value;
			//alert("Url==> "+url);
			var response=AjaxReturnValue(url);
			//alert("Repoensre==> "+response);
			document.getElementById("disputedetails").innerHTML = response;
		}
}

function acceptfunction()
{
	var disputeid = document.getElementById("disputeid");
	if(disputeid.value=="-1")
	{
		errMessage(disputeid,"Select Dispute id");
	}
	else
	{
	errMessage(disputeid,"	'"+disputeid.value+"' Accepted");	
	}
	
	return false;
	
}
function rejectfunction()
{
	var disputeid = document.getElementById("disputeid");
	if(disputeid.value=="-1")
	{
		errMessage(disputeid,"Select Dispute id");
	}
	else
	{
		errMessage(disputeid,"	'"+disputeid.value+"' Rejected");	
	}
	
	return false;
}

function showDispMode( diputeid ){
	//alert( diputeid );
	//var dispid = document.getElementById(disputeid).value;
	//alert( dispid );
	if( diputeid == "dispenter" ){
		document.getElementById("entercode").style.display = 'table-row';
		document.getElementById("selectcode").style.display = 'none';
	}else{
		document.getElementById("selectcode").style.display = 'table-row';
		document.getElementById("entercode").style.display = 'none';
	}
}
function showProcessing(){
	parent.showprocessing();
}
</script>
<style>
p.test2
{
	width:250px;
	word-wrap:break-word;
}
.algintop{
 vertical-align: top;
 padding:12px; 
}
</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form  action="viewCompliantDataDispute.do" autocomplete="off" name="diputeraiseviewform" onsubmit="showProcessing()">
	<table border="0" cellpadding="0" cellspacing="0" width="50%">
		<tr> <td>&nbsp;</td><td> : <input type="radio" name="dispmode" value="dispenter"  id="dispenter" checked onclick="showDispMode(this.id)" /> : Enter Compliant Code  
						&nbsp;&nbsp;&nbsp;
					 <input type="radio" name="dispmode" id="dispselect" value="dispselect"  onclick="showDispMode(this.id)"/>  :Select Compliant Code</td> </tr>
		<tr id="entercode" >
			<td>
				 Enter Dispute Id sddfasfsdfd
			</td>
			<td> :
				<s:textfield name="enterdisputeid" id="disputeid1" />
			</td>
		</tr>		 
					 
		<tr  id="selectcode"  style="display:none" >
			<td>
				Select Dispute Id 
			</td>
			<td>:
				<s:select 
					list="%{dispute.compliantlist}" 
					id="disputeid"
					name="selectdisputeid" 
					listKey="COMPLIANT_CODE"
					listValue="COMPLIANT_CODE" 
					headerKey="-1" 
					headerValue="- Select Dispute ID -"
					onchange="return getDisputeviewdetails();" 
				/>
			</td>
		</tr>
	</table>
		<table><tr><td> <s:submit name="submit" id="submit" value="Submit" /> </td> </tr></table>
<!-- 	<div id="disputedetails"></div> -->
	
</s:form>