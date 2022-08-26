<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
	<script type="text/javascript">
	function putName ( cardtypeid ){
		//alert("cardtype==> "+cardtypeid);
		if( cardtypeid != -1 ){
			var url = "fchCardtypeDescCardTypeAction.do?cardtypeid="+cardtypeid;
			var result = AjaxReturnValue(url);
			//alert("result"+result);
			document.getElementById("cardtypedesc").value = result;
		}else{
			document.getElementById("cardtypedesc").value = '';
		}
	}	
	function validation_authcardtype(){
	 	var auth = document.getElementById("auth0").value;
	 	var cardtype = document.getElementById("cardtype").value;
	 	var prodcode = document.getElementById("prodcode").value;
	 	var reason = prompt('Enter the Reason for Reject');
		 if( reason ){
			 var url = "authDeauthProductAddMainProductAction.do?cardtype="+cardtype+"&reason="+reason+"&auth="+auth+"&prodcode="+prodcode;
			 window.location = url; 
		 }  
		 return false;
	}
	
	function deleteProduct(){
		
		var prodcode = document.getElementById("prodcode"); 
		var reason = prompt("Reason For Delete ");
		if( reason == null ){
			return false;
		}
		
		var url = "deleteProductActionAddMainProductAction.do?productcode="+prodcode.value+"&reason="+reason;
		window.location=url;
		return false;
	}
	
	function deleteProductAuthorize( doact ){
		var prodcode = document.getElementById("prodcode"); 
		var url = "deleteProductAuthActionAddMainProductAction.do?productcode="+prodcode.value+"&reason="+reason+"&doact="+doact;
		window.location=url;
		return false;
	}
	</script>
	<style type="text/css">
	
	#textcolor
	{
	color: maroon;
	font-size: small;
	}	
	</style>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="authDeauthProductAddMainProductAction.do" autocomplete="off">
<div align="center">
	<center><h1>CARD TYPE DETAILS</h1></center><br><br>
			  <s:iterator  value="prod_detail">
			  <s:hidden name="cardtype" id="cardtype" value="%{CARD_TYPE_ID}"></s:hidden>
			  <s:hidden name="prodcode" id="prodcode" value="%{PRODUCT_CODE}"></s:hidden>
			  	<table border="1" cellpadding="0" cellspacing="2" width="65%">
						
						<tr>
							<td align="center">Card Type</td>
							<td id="textcolor"> : ${CARD_TYPE_NAME}
							</td>
						</tr>					
						<tr>
							<td align="center">BIN</td>
							<td id="textcolor"> : ${BIN_DESC}</td>
						</tr> 			 		
				 		<tr>
							<td align="center">Card Type Description</td>
							<td id="textcolor"> : ${CARD_TYPE_NAME} </td>	
						</tr>	
						<tr>
							<td align="center">No Of Subtype</td>
							<td id="textcolor"> : ${SUB_PROD_CNT} </td>
						</tr>
						<%-- <tr>
							<td align="center">GL</td>
							<td id="textcolor"> : ${GL_NAME} </td>
						</tr>  --%>
						<tr>
							<td align="center">Configured By</td>
							<td id="textcolor"> : ${CONFIG_BY} </td>
						</tr> 
						<tr>
							<td align="center">Status</td>
							<td id="textcolor"> : ${AUTH_CODE} </td>
						</tr> 
						<tr>
							<td align="center">Remarks</td>
							<td id="textcolor"> : ${REMARKS} </td>
						</tr> 
					</table>
			</s:iterator>		
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>			
			<s:if test="%{productbean.doact=='$DEL'}">
				<input type="submit" name="auth" id="auth1" value="Delete" onclick="return deleteProduct()"/>
				<input type="button" name="auth" id="auth0" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			</s:if>
			<s:elseif test="%{productbean.doact=='$DELAUTH'}">
				<input type="submit" name="auth" id="auth1" value="Delete" onclick="return deleteProductAuthorize('AUTH')"/>
				<input type="button" name="auth" id="auth0" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
				<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return deleteProductAuthorize('DEAUTH')"/>
			</s:elseif>
			<s:else>
				<s:if test="%{flag=='D'}">
					<%-- <s:submit value="Back" name="submit" id="submit"  onclick="window.history.back(-2);"/> --%>
				</s:if><s:else>
					<input type="submit" name="auth" id="auth1" value="Authorize"/>
					<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authcardtype()"/>
				</s:else>
			</s:else>
		</td>
		</tr>
</table>
</div>
</s:form>