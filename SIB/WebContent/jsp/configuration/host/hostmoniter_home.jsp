<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>  
<jsp:include page="/displayresult.jsp"></jsp:include>

<script>
	function checkingHostStatus(){
		window.location="moniterHomeHostConfigAction.do?";
		parent.showprocessing();
	}
</script>


<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
<div align="center">
<s:form action="reciveInsCardActionInstReceiveEProcess"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
   <table border="0" cellpadding="0" cellspacing="0" width="100%">
   <tr><td style="text-align:center"> <input type="button" value="Check Status" onclick="checkingHostStatus()" /></td></tr>
   </table>
	<table width="60%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable"  >
				 
				 
					<tr>  
						<th>HOST ID </th>
						<th> IP ADDRESS </th>
						<th> PORT </th>
						<th>STATUS</th>
						 
					</tr>
				<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="hostlist">
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
						 
						
						<td> ${CONN_DESC}  </td>
						<td> ${IP_ADDR}  </td>
						<td> ${PORT}  </td>
						<td> ${PORT_ST}</td>
					</tr> 
			 	</s:iterator>
			 		 
	 	</table>  

 
</s:form>
</div>
 