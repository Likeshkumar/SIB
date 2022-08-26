<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script>
<script>
 	 
 	function deleteMapping( parentschemecode, affectschemecode ){ 
 			
 	 		if( confirm ("Do you want to delete ") ){  
 	 	 			var url = "deleteMappedSchemeGLConfigure.do?parentschcode="+parentschemecode+"&affectschemecode="+affectschemecode;
 	 	 			parent.showprocessing();
 	 	 			window.location = url;
 	 	 	}
 	 		
 	 			
 	 	 
 	}
 	
 	function enableSubGl( schemetype ){
 		/* if( schemetype == "$FEE"){
 			document.getElementById("subgl").disabled=false;
 		}else{
 			document.getElementById("subgl").disabled=true;
 		} */
 	}
 	function goHome(){
 		var url = "glMapHomeGLConfigure.do";
 		window.location = url;
 	}
 </script>

<style>
table.mappedschemetable {
	padding-top: 15px;
}

table.mappedschemetable th,table.mappedschemetable td {
	text-align: center;
	padding-top: 10px;
	border: 1px solid gray;
}
</style>
<jsp:include page="/displayresult.jsp"></jsp:include>

<form action="saveGlMappingGLConfigure.do" method="post" autocomplete="off">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border: 1px solid #efefef; padding: 0 auto"	cellpadding="0" border="0" align="center" width="100%" cellspacing="0">
		<s:property value="glbeans.accrulebean"/>
		 <s:iterator value="glbeans.accoutrulelist">
		 	 	<tr>
			<td>
				<table style="border: 1px solid #efefef;" cellpadding="0" cellspacing="0" width="100%">
					<tr>	
						<s:hidden name="acctrulecode" id="acctrulecode" value="%{ACCT_RULEID}"/>
						<s:hidden name="acctrulename" id="acctrulename" value="%{ACCT_RULEID}"/>
						<s:hidden name="txncode" id="acctrulecode" value=""/>
						<td>Rule Name</td>
							<td class="textcolor" >: ${RECORDID} 
							</td>
							<td>Product</td>
							<td class="textcolor" >:  ${PRODUCTCODE} 
							</td>
							<td>  Sub Product</td>
							<td class="textcolor" >:   ${SUBPRODUCT} <%-- <s:property value="accrulebean.subproductdesc"/> --%> 
					</tr> 
					<tr>
							<td>Txn Code</td>
							<td class="textcolor" >:  ${TXNCODE} <%--  <s:property value="accrulebean.strtxncode"/>   --%>
							</td> 
							<td>Message Type</td>
							<td class="textcolor" >:   ${MSGTYPE}<%-- <s:property value="accrulebean.strmsgtype"/>   --%>
							</td> 
							<td>Response Code</td>
							<td class="textcolor" >:  ${RESPCODE}<%--  <s:property value="accrulebean.strrespcode"/>   --%>
							</td> 
					</tr> 
						
					<tr> 
							<td>Orgin Channel</td>
							<td class="textcolor" >: ${TXNSRC}<%--  <s:property value="accrulebean.strorgchannel"/>  --%>
							</td>
							<td>Device Type</td>
							<td class="textcolor" >: ${DEVICETYPE}<%--  <s:property value="accrulebean.strdevicetype"/>  --%>
							<td>Financial Transactioin</td>
							<td class="textcolor" >:  ${FINTXNFLAG}<%--  <s:property value="accrulebean.fintxndesc"/>  --%>
							</select> 
							</td>
					</tr> 
					<tr>
							<td>Transaction Category</td>
							<td class="textcolor" >: ${DEVICETYPE}<%--  <s:property value="accrulebean.revtxn"/>   --%>
							</td> 
							<td>Reversal Type</td>
							<td class="textcolor" >:  ${FULLREVFLAG}<%--  <s:property value="accrulebean.revtxntype"/>   --%>
							</td>
					</tr>

				</table>
			</td>
		</tr>
		 </s:iterator>
		



		<tr>
			<td>
				<table style="border: 1px solid #efefef;" cellpadding="0" cellspacing="0" width="100%">
				<tr>
				
						<td>Order </td>
						<td>:  <s:textfield name="schemeorder" id="schemeorder" style="width:50px" maxlength="2" onkeypress="numerals(event)"/>
						</td> 
						
						
					<td> Scheme Type </td>
						<td>:  
						<s:select name="schemetype" id="schemetype"  list="glbeans.glkeys" listKey="TXNKEY" listValue="TXNKEYDESC"  onchange="enableSubGl(this.value)" headerKey="-1" headerValue="-SELECT-"   />
					 	</td>  
						
						<td>Transaction type</td>
						<td>: <select name="trantype" id="trantype">
								<option value="-1">-SELECT-</option>
								<option value="CR">CREDIT</option>
								<option value="DR">DEBIT</option> 
						</select>
						</td> 
						
						<td>Entry </td>
						<td>: <select name="entrytype" id="entrytype"> 
								<option value="S" selected > Single Entry </option>
								<!-- <option value="M"> Multiple Entry </option> --> 
						</select>
						</td> 
						
						
						<td>Sub GL</td>
						<td>: <select name="subgl" id="subgl" >
								<option value="NOT-REQUIRED"> NOT REQUIRED </option>
								<s:iterator value="glbeans.childschemelist">
									<option value="${SCH_CODE}">${SCH_NAME}</option>
								</s:iterator>
						</select>
						</td>  
						
						
				</tr>
				</table>
			</td>
		</tr> 
		<tr>
			<td colspan="6">
				<table border="0" cellpadding="0" cellspacing="4" width="20%">
					<tr>
						<td><s:submit value="Submit" name="submit" id="submit"
								onclick="return glvalidation()" />
						<td><input type="button" onclick="goHome()" name="cancel"
							id="cancel" value="Back" class="cancelbtn" /></td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<td colspan="6">
				<table border="0" class="formtable" cellpadding="0" 	cellspacing="0" width="100%">
					 
					<s:if test="glbeans.headerreq">
						<tr>
							<th>Account Rule id</th>
							<th> Scheme </th>
							<th> Transaction type </th>
							<th> GL </th>
							<!-- <th> Entry </th> -->
						<!-- 	<th> Transaction Code  </th> -->
							<th>Delete</th>
						</tr>
					</s:if>

					<s:iterator value="glbeans.mappedscheme"> 
							<td>${ ACCT_RULEID }</td>
							<td>${ GLKEY_DESC }</td>
							<td>${ TRAN_TYPE }</td>
							<td>${ SCH_CODE }</td>
						<%-- 	<td>${ SCH_ENTRY }</td> --%>
					<%-- 		<td>${ ACTION_CODE }</td> --%>
							<td><a href="#"
								onclick="deleteMapping( '<s:property value="glbeans.txnactioncode"/>', '${ACCT_RULEID}' )"><img
									src="images/delete.png" border="0"></a></td>
						</tr>
					</s:iterator>
					<!-- src="images/delete.png"    -->
				</table>
			</td>
		</tr> 

	</table>
</form>