<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
 <script type="text/javascript">
 function currencyadd()
 {
 	valid= true;
 	var addcurrdesc = document.getElementById("currdesc");
 	var addcurrcode = document.getElementById("currcode");
 	var numbercode = document.getElementById("numericcode");
 	var cursymbol = document.getElementById("currsymbol");
 	
 	
 if( addcurrdesc ){
 	if (addcurrdesc.value == "" )
 		 {
 		//alert("Select Currency Description");
 		errMessage(addcurrdesc,"Enter Currency Description" );
 	    return false;
 		}		  
 	}
 	if( addcurrcode  )
 	{	
 		if( addcurrcode.value == "" ){
 		//alert("Select Currency code");
 		errMessage(addcurrcode,"Enter Currency code" );
 		return false;
 		}
 	}
 	if( numbercode  )
 	{	
 		if( numbercode.value == "" ){
 		//alert("Select Currency code");
 		errMessage(numbercode,"Enter Numeric Code" );
 		return false;
 	}
 	}
 	if( cursymbol  )
 	{	
 		if( cursymbol.value == "" ){
 		//alert("Select Currency code");
 		errMessage(cursymbol,"Enter Currency Symbol Code" );
 		return false;
 	}  		
 	}
 	return valid;
 }
 function validation_authcurrency(){
	 	var auth = document.getElementById("auth0").value;
	 	var currcode = document.getElementById("currcode").value;
	 	var reason = prompt('Enter the Reason for Reject?');
		 if( reason ){
			 var url = "authdeauthcurrencylistCurrencyAction.do?currcode="+currcode+"&reason="+reason+"&auth="+auth;
			 window.location = url; 
		 }  
		 return false;
 }
</script><jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="authdeauthcurrencylistCurrencyAction.do" method="post">
<s:iterator value="currencydetails">
<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
<div align="center">
	<table border='0' cellpadding='0' cellspacing='0' width='40%' >  
	    <tr> 
	    	<td>
	    		 Currency Code
	    	</td>
	    	<td> : 
	    		<s:textfield name="currcode" id="currcode" value="%{CURRENCY_CODE}" style="width:160px" onKeyPress=" return alphabets(event);" readonly="true" maxlength="5"/>
	        </td>
	    </tr>		 
		<tr> 
	    	<td>
	    		 Currency Description
	    	</td>
	    	<td>:
	    			<s:textfield name="currdesc" id="currdesc"  value="%{CURRENCY_DESC}" style="width:160px" maxlength="30" readonly="true"/>
					
	        </td>
	    </tr>	
	

	    <tr> 
	    	<td>
	    		 Numeric Code
	    	</td>
	    	<td> : 
	    			<s:textfield name="numericcode" id="numericcode" value="%{NUMERIC_CODE}" style="width:160px" onKeyPress="return numerals(event);" maxlength="5" readonly="true"/>
					
	        </td>
	    </tr>	    
	      <tr> 
	    	<td>
	    		 Currency Symbol
	    	</td>
	    	<td> : 
	    			<s:textfield name="currsymbol" id="currsymbol" value="%{CUR_SYMBOL}" style="width:160px" maxlength="5" readonly="true" onkeyup="chkChars('Currency Symbol ',this.id,this.value)" />
					
	        </td>
	    </tr>	
	</table>
		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="100%" >
				<tr align="center">
					<td>
						<input type="submit" name="auth" id="auth1" value="Authorize"/>
						<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authcurrency();"/>						
					</td>
				</tr>
		    </table>
		</div>
	   
</div>
</s:iterator>
</s:form>
 
 