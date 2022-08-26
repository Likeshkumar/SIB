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
 
 function chkChars(field,id,enteredchar)
 {
 	//alert('1:'+field+'2:'+id+'3:'+enteredchar);
 	charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~0123456789";
 	//str = document.getElementById(id).value;
     for (var i = 0; i < document.getElementById(id).value.length; i++) {
        if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
     	//alert(document.getElementById(id).value.charAt(i));   
     	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
     	document.getElementById(id).value = '';
     	return false;
     	}
     }
 }

</script><jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="saveCurrencyCurrencyAction.do" method="post">

<input type="hidden" name="mkrchkr" id="mkrchkr" value="${beans.mkrchkrstatus}">
<div align="center">
	<table border='0' cellpadding='0' cellspacing='0' width='40%' >  
	    <tr> 
	    	<td>
	    		 Currency Code
	    	</td>
	    	<td> : 
	    		<s:textfield name="currcode" id="currcode" value="%{curcode}" style="width:160px" onKeyPress=" return alphabets(event);" readonly="true" maxlength="5"/>
	        </td>
	    </tr>		 
		<tr> 
	    	<td>
	    		 Currency Description
	    	</td>
	    	<td>:
	    			<s:textfield name="currdesc" id="currdesc"  value="%{curdesc}" style="width:160px" maxlength="30" readonly="true"/>
					
	        </td>
	    </tr>	
	

	    <tr> 
	    	<td>
	    		 Numeric Code
	    	</td>
	    	<td> : 
	    			<s:textfield name="numericcode" id="numericcode" value="%{number_code}" style="width:160px" onKeyPress="return numerals(event);" maxlength="5" readonly="true"/>
					
	        </td>
	    </tr>	    
	      <tr> 
	    	<td>
	    		 Currency Symbol
	    	</td>
	    	<td> : 
	    			<s:textfield name="currsymbol" id="currsymbol" value="%{cur_symbol}" style="width:160px" maxlength="5" onkeyup="chkChars('Currency Symbol',this.id,this.value)" />
					
	        </td>
	    </tr>	
	</table>
		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="30%" >
				<tr align="center">
					<td> <input type="submit"  name="submitcur" id="submitcur" value="Update" onclick="return currencyadd()"/></td>
					<td>
						<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
					</td>
				</tr>
		    </table>
		</div>
	   
</div>

</s:form>
 
 