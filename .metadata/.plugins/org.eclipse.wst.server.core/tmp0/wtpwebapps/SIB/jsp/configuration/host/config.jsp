<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<script>
	function displayIp( hostval ){
		var ip = document.getElementById("ipaddress");
		if( hostval != -1 ){
			var url = "showIpHostConfigAction.do?hostname="+hostval;
			var result = AjaxReturnValue(url);
			if( result == "000.000.000.000"){
				var hostorgip = prompt( hostval+ " IP ADDRESS NOT CONFIGURED. ENTER THE IP ADDRESS" );
				 
				if(hostorgip != ""){
					var hostipurl = "updateIpHostConfigAction.do?hostname="+hostval+"&orgip="+hostorgip;
				    if(!validateIP(hostorgip)){
				    	alert( "ENTERED IP ADDRESS IS INVALID");
				    	return false;
				    }
					var resultip = AjaxReturnValue(hostipurl);
					if(resultip != 1 ){
						alert( "COULD NOT UPDATE IP ADDRESS.");
						return false;
					}
					document.getElementById("ipaddress").value = hostorgip;
				}
			}
			else{
				ip.value = result;
			}
		}else{
			ip.value = "";
		}
	}
	
	function validateform()
	{
		 
		var connect = document.getElementById("connect");
		var connect_desc = document.getElementById("connect_desc");
		var ipaddress = document.getElementById("ipaddress");
		var port = document.getElementById("port");
		if(connect)
		{
			if(connect.value == "-1")
			{
				errMessage(connect,"Please select Connection");
				return false;
			}
		}
		if(connect_desc)
		{
			if(connect_desc.value == "")
			{
				errMessage(connect_desc,"Please Enter connection desc");
				return false;
			}
		}
		if(ipaddress)
		{
			if(ipaddress.value == "")
			{
				errMessage(ipaddress,"Please Enter ipaddress");
				return false;
			}
		}
		if(port)
		{
			if(port.value == "")
			{
				errMessage(port,"Please Enter port");
				return false;
			}
		}
		return true;
	}
</script>
<style>
	table.formtable td{
		text-align:center; 
	}
</style>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<div align="center">
<s:form action="saveHostConfigHostConfigAction.do"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   
	<table width="50%" cellpadding="0"  border="0"  cellspacing="0" style="text-align:left" class="formtable"  >
				 
				 	<tr>
				 		<td>CONNECTION </td>
				 		<td> :
				 		<s:select name="connect" id="connect" 
				 			headerKey="-1" headerValue="-SELECT-"  listKey="HOST_ID" listValue="HOST_ID"
				 			list="hostiplist" value="%{connect}" onchange="displayIp(this.value)"></s:select>
				 			 
				 		</td>
					</tr>
					
					<tr>
				 		<td>CONNECTION DESC</td>
				 		<td> :
				 			<s:textfield name="connect_desc" id="connect_desc" maxlength="10" value="%{connect_desc}"/>
				 		</td>
					</tr>
					
					
					<tr>
				 		<td>IP ADDRESS</td>
				 		<td> :
				 			<s:textfield name="ipaddress" id="ipaddress" readonly="true"   maxlength="15" value="%{ipaddress}"/>
				 		</td>
					</tr>
					
					<tr>
				 		<td>PORT</td>
				 		<td> :
				 			<s:textfield name="port" id="port" maxlength="6" onKeyPress="return numerals(event);" value="%{port}"/>
				 		</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<table border="0" cellpadding="0" cellspacing="4" width="20%" >
								<tr>
								<td>
									<s:submit value="Submit" name="order" id="order" onclick="return validateform();"/>
								</td>
								<td>
									<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
									 
								</td>
								</tr>
						</table>
						
						</td>
					</tr>
	 	</table> 
	 	
	 	

 
</s:form>
</div>
 