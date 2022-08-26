<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 

 <script>
 	function validateCourier(){
 	 
 		var crname = document.getElementById("crname");
 		var courieroffice = document.getElementById("courieroffice");
 		var contactnumber = document.getElementById("contactnumber");
 		
 		if( crname.value == "" ){
 			errMessage(crname, "Enter Courier Name"); return false;
 		}
 	}
 </script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="%{courierbean.actionname}" onsubmit="return validateCourier()">
	 
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
	
	 <s:hidden name="courierid" id="courierid" value="%{courierbean.courierid}"/>
		<tr > 
			 <td>Courier Name <span class="mand">*</span></td>
			 <td> : <s:textfield name="crname" id="crname"  value="%{courierbean.couriername}" /> </td>
		 
			  <td>Office</td>
			 <td> :  <s:textfield name="courieroffice" id="courieroffice"  value="%{courierbean.courieroffice}" />  </td>
		</tr>
		 <tr > 
			  
			  <td>Contact Number</td>
			 <td> :  <s:textfield name="contactnumber" id="contactnumber"  value="%{courierbean.contactnumber}" />  </td>
			 
			   <td>Status</td>
			 <td> : <s:select name="cstatus" list="#{'1':'Active','0':'In-Active'}"  value="%{courierbean.cstatus}"/> </td>
			 
		</tr> 
	 
	 	<tr>
	 		<td colspan="4">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
						<td> 
							<s:submit value="Submit" name="order" id="order" /> 
						<td>
							<input type="button"  onclick="return confirmCancel()" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  />
							 
						</td>
						</tr>
					</table>
	 		</td>
	 	</tr>
	</table>


</s:form> 
 