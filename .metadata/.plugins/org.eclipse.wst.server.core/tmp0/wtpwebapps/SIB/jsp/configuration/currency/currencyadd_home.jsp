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
	 var addcurrcode = document.getElementById("currcode");
	 if( addcurrcode  )
	 	{	
	 		if( addcurrcode.value == "-1" ){
	 		//alert("Select Currency code");
	 		errMessage(addcurrcode,"Select Currency code" );
	 		return false;
	 		}
	 	}
 	valid= true;
 	var addcurrdesc = document.getElementById("currdesc");
 	
 	var numbercode = document.getElementById("numericcode");
 	var cursymbol = document.getElementById("currsymbol");
 	
 	//return false;
 	
 	
 if( addcurrdesc ){
 	if (addcurrdesc.value == "" )
 		 {
 		//alert("Select Currency Description");
 		errMessage(addcurrdesc,"Enter Currency Description" );
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
 
 function getcurrencystandarddetails( currencycode ){ 
		if( currencycode != -1 ){
			// document.getElementById('textbox_creater').innerHTML 
			//var url = "glSchemeCodeAjaxSubProduct.do?prodcode="+prodcode; 
			var url = "getCurencystdCurrencyAction.do?alphacode="+currencycode;
			var result = AjaxReturnValue(url); 
			document.getElementById('ajaxvalue').innerHTML =result;
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


 </script>


<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String acttype = (String)session.getAttribute("act_type");
	String btnval=null;
	if( acttype.equals("insert")){
		btnval = "Save";
	}else if( acttype.equals("edit")){
		btnval = "Update";
	}
%>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="saveCurrencyCurrencyAction"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">
<div align="center">
	<table border='0' cellpadding='0' cellspacing='0' width='40%' >  
	    <tr> 
	    	<td>
	    		 Currency Code           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    	</td>
	    		    	
	    	<td>  
	    		<select name="currcode" id="currcode" onchange="getcurrencystandarddetails(this.value)" style="width:50%;" >
							<option value="-1">SELECT CURRENCY NAME</option>
						<s:iterator value ="CurrencyStandardsList" >
						<option value="${ALPHABETIC_CODE}">${ALPHABETIC_CODE}</option>
						</s:iterator>
						</select>
	        </td>
	    </tr>
	    </table>		 
	    <div id="ajaxvalue">
	    
	    </div>
		
	
<%-- 	<table width='30%'>
			<tr align="center">
				<td>
					<input type="submit"  name="submitcur" id="submitcur" value="<%=btnval%>" onclick="return currencyadd()"/>
					<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />
				</td>		
			</tr>
	</table> --%>
	
		<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="40%" >	
				<tr align="center">
					<%-- <td> <input type="submit"  name="submitcur" id="submitcur" value="<%=btnval%>" onclick="return currencyadd()"/></td> --%>
					<td> <input type="submit"  name="submitcur" id="submitcur" value="<%=btnval%>"/></td>
					<td>
						<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
					</td>
				</tr>
			</table>
		</div>
	   
</div>
</s:form>
 
 