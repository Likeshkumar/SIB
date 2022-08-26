<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="jsp/merchant/script/merchscript.js"></script>
<script>

	function fillTerminalName ( val ){
		var terminalname =  document.getElementById("terminalname");
		terminalname.value = val;
	}
	function setMachineId( termtype ){
		var machineid = document.getElementById("machineid");
		var terminalname = document.getElementById("terminalname");
		
		var terminalmasterkey = document.getElementById("terminalmasterkey");
		var mackey = document.getElementById("mackey");
		var keyintvl = document.getElementById("keyintvl");
		var echointvl = document.getElementById("echointvl");
		var txnkeyintvl = document.getElementById("txnkeyintvl");
		var termstatus  = document.getElementById("termstatus");
		var chipenabled = document.getElementById("chipenabled");
		if( termtype == null ){
			return false;
		}
		chipenabled.value="Y";
		termstatus.value ="1";
		
		if( termtype == "BTMPOS" ){
			machineid.value = "0";
			machineid.readOnly = true;
			
			terminalmasterkey.value = "1111111111111111";
			mackey.value = "1111111111111111";
			keyintvl.value = "300";
			echointvl.value = "300";
			txnkeyintvl.value = "300";
			
			
		}else{
			machineid.value = "";
			machineid.readOnly = false;
			
			terminalmasterkey.value = "";
			mackey.value = "";
			keyintvl.value = "";
			echointvl.value = "";
			txnkeyintvl.value = "";
			 
		}
		 
	}
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>

 

<%
Boolean editable = true;
String merchname ;

if( request.getParameter("merchname") != null ){
	 merchname = request.getParameter("merchname").replace("~", ""); 
}else{
	merchname = " "; 
	%>
 
<%
}
 
%>
<% 
String acttype = (String)session.getAttribute("act");
System.out.println(acttype);
String btnval = null;
if(acttype.equals("SAVE"))
{
	btnval = "Save";
}
else
{
	btnval = "Update";
} 
String terminalid = (String)session.getAttribute("terminalid");
System.out.println(terminalid);
%>
<body onload="test()">
<s:form action="saveTerminalInfoMerchantRegister.do"  name="custinfoform" id="custinfoform"  autocomplete = "off"  namespace="/">
	  
	<table border="0" cellpadding="0" cellspacing="0" width="95%" style='border:1px solid #efefef' align="center" class="filtertab">	 
			 
			<tr><td> MERCHANT NAME    </td>   <td> : <s:property value='mregbean.merchname'/> </td>
				<td> MERCHANT ID     </td>   <td> :   <s:property value='mregbean.merchid'/> <input type='hidden' name='merhid' id='merchid' value='<s:property value='mregbean.merchid'/>' /> </td>
			</tr>
			   
		 
	 		<tr> 
	 		
				 <td>Store Name <span class="mand">*</span></td>
			 	 <td> : 
			 	 <%if(acttype.equals("UPDATE")){ %>
			 	 <s:textfield id="storeid" name="storeid" value="%{mregbean.storename}" readonly="%{flag}"/>
				<%} else{%>
				<s:select list="%{mregbean.storelist}" id="storeid" name="storeid"  listKey="STORE_ID" listValue="STORE_NAME"  headerKey="-1-" headerValue="-SELECT-" value="%{storename}"/>
				<%} %> </td>
				
	    		<td> Terminal Type </td>
				<td> : <s:select list="%{mregbean.terminallist}" id="merchterminal" name="merchterminal"  listKey="TERMINAL_TYPE" listValue="TERMINAL_TYPE_DESC"  headerKey="-1" headerValue="-SELECT-" value="%{mregbean.terminaltype}" onchange="setMachineId(this.value)"/> </td>
				
		     </tr>
   			 <tr> 
 		 			
			 <td> Machine id <span class="mand">*</span></td>
		 		<td> :  <s:textfield name="machineid" id="machineid" maxlength="32" value="%{mregbean.machine_id}" readonly="%{mregbean.flag}"/>
    	
			 <td>Terminal Id </td>
		 	 <td> : <s:textfield name="terminalid" id="terminalid" maxlength="8" value="%{mregbean.term_id}" readonly="%{mregbean.flag}"  onkeyup="fillTerminalName( this.value )"/>
		 	  </td>   
    		</tr> 
	   		 
	  		  <tr> 
	 			  <td> Terminal Name <span class="mand">*</span></td>
			 	  <td> : <s:textfield name="terminalname" id="terminalname" maxlength="32" value="%{mregbean.term_name}"/>
				
				
				 <td>Terminal Location <span class="mand">*</span></td>
			 	 <td> :  <s:textfield name="termlocation" id="termlocation" maxlength="64" value="%{mregbean.term_location}"/> </td>
				
				 
				 
	    	</tr>
	     
	    	 <tr> 
	 			
				<td>Chip Enabled </td>
				<td> : <s:select name="chipenabled" id="chipenabled" list="#{'Y':'YES','N':'NO'}" headerKey="-1" headerValue="-SELECT-" value="%{mregbean.chip_enabled}"/>
				</td>
				 <td>  Master Key <span class="mand">*</span></td>
			 	 <td> :  <s:textfield name="terminalmasterkey" id="terminalmasterkey" maxlength="64" value="%{mregbean.term_masterkey}"/> </td>
				
				 <!-- <td>Pin Key </td> -->
			 	 <td> : <s:hidden name="pinkey" id="pinkey" maxlength="64" value="1111"/><!-- %{mregbean.term_pinkey} -->
			 	  </td>  
	    	</tr>
	    	 <tr> 
	 		
				 <td>  MAC Key <span class="mand">*</span></td>
			 	 <td> :  <s:textfield name="mackey" id="mackey" maxlength="64" value="%{mregbean.term_mackey}"/> </td>
				
				<td> Key Intervel </td>
				 <td> :  <s:textfield name="keyintvl" id="keyintvl" onKeyPress=" return numerals(event);" value="%{mregbean.term_keyintrvl}" maxlength="6" /> </td> 
				 
	    	</tr>
	    	
	    	 <tr> 
	 		
				 <td>  Echo Intervel <span class="mand">*</span></td>
			 	 <td> :  <s:textfield name="echointvl" id="echointvl" onKeyPress=" return numerals(event);" value="%{mregbean.term_echointrvl}"  maxlength="6"  /> </td>
				
				<td> Txn Key Intervel </td>
				 <td> :  <s:textfield name="txnkeyintvl" id="txnkeyintvl" onKeyPress=" return numerals(event);" value="%{mregbean.txn_keyintrvl}"   maxlength="6" /> </td> 			 
	    	</tr>
	    	
	    	 <tr>
		    	 <td> MCC  <span class="mand">*</span></td>
			 	 <td> : <s:select list="%{mregbean.mccllist}" id="merchmcc" name="merchmcc"  listKey="MCC_CODE" listValue="MCC_DESC"  headerKey="-1" headerValue="-SELECT-" value="%{mregbean.mcccode}"/> </td> 
				  
				<td> Status </td>
				<td> : <s:select name='termstatus' id='termstatus' list="#{'1':'ACTIVE','0':'IN-ACTIVE'}" headerKey="-1" headerValue="-SELECT-" value="%{mregbean.term_status}"/>
				</td>
				 
	    	</tr>
	     
	    	<tr>
		    	 <td> Currency  <span class="mand">*</span></td>
			 	 <td> : <s:select list="%{mregbean.currncylist}" id="merchcur" name="merchcur"  listKey="CUR_CODE" listValue="CUR_DESC"  headerKey="-1" headerValue="-SELECT-" value="%{mregbean.curcode}"/> </td> 
				  
					 
	    	</tr>
		 
	</table>
	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
			<tr>
			<td>
				<input type="submit" value="<%=btnval%>" name="action" id="action" onclick="return addterminal_validation()"/>
			</td>
			<td>
				<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
				 
			</td>
			</tr>
	</table>
</s:form>
 
 </body>