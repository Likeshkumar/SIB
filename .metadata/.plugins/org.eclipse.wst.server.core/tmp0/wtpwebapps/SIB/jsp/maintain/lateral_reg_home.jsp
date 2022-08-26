<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
 

<script type="text/javascript" src="js/script.js"></script>
 
 <script>
 	function validate(){
 		var cardno = document.getElementById("cardno");
 		 
 		if( cardno.value == "" ){
 			errMessage( cardno, "Card Number should not be empty");
 			return false;
 		}
		parent.showprocessing();
 	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="customerEntryLateralRegister.do" name="orderform" onsubmit="return validate()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0"  width="30%"	align="center"> 
		<tr>
			<td> Card Number </td>
			<td> : <s:textfield name="cardno" id="cardno" /> </td>  
		</tr>  
   </table>
   
   <table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table>
</s:form>

