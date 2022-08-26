<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<s:form action="customerDetailsAddDebitCustomerRegister.do"  id="regform1" onsubmit="return valiteContactDetails()" name="regform1" autocomplete = "off" namespace="/"> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Personal Information
	</th><tr> 
	<tr>
		 <td class="txt"> First Name <span class="mand">*</span> </td> <td> <s:textfield name="firstname" id="firstname" maxlength="32"  value="" /> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="middlename" id="middlename" maxlength="32" value=""  /> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="lastname" id="lastname" maxlength="32" value=""  /> </td>
	</tr> 
	
	
	<tr>
		 
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob" readonly="true"  value=""  style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		 
		   <td class="txt"> Gender <s:property value=""/> <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="gender"  headerKey="-1" headerValue="-SELECT-" name="gender" value="" /> </td>
		  <td class="txt"> Marital Status <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Married','U':'Un-Married','O':'Other'}" id="mstatus"  headerKey="-1" headerValue="-SELECT-" name="mstatus" value=""/> </td>
		  
	</tr> 
	
	
	
	<tr> 
		 <td class="txt"> Nationality <span class="mand">*</span>  </td> <td> <s:textfield name="nationality" id="nationality" maxlength="32" value=""/> </td>
		   <td class="txt"> Document Provided <span class="mand">*</span> </td> <td> 
		  	
		  </td>
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  value="" /> </td>
	
	</tr> 
	
	 
	
	  
	
	
	 <tr><th colspan="6" style="text-align:left" class="tbheader"> Family Details </th><tr> 
	
	<tr>
		<td class="txt"> Spouce Name   </td> <td> <s:textfield name="spousename" id="spousename" maxlength="32" value=""   /> </td>
		 <td class="txt"> Mother's Name <span class="mand">*</span> </td> <td> <s:textfield name="mothername" id="mothername" maxlength="32" value=""  /> </td>
		  <td class="txt"> Father Name  </td> <td> <s:textfield name="fathername" id="fathername" maxlength="32"  value=""  /> </td> 
	</tr> 
	
	 
	 <tr><th colspan="6" style="text-align:left" class="tbheader"> Account Information
		 </th>
		 <tr>
		 <td colspan="6" width="100%" >
		 	<table border='1' id="itemRows" cellpadding='100%' style='display:block'>
		 	<tr>
		 		<td>  Account Type: <select id="accttype0" name="accttype"></select> </td>
		 		<td>&nbsp;</td>
		 		<td> Primary Account Number : <s:textfield  name="acctno" id="acctno0" maxlength="%{dbtcustregbean.acctnolen}" /> <input onclick="addRow(this.form);" type="button" value="Add Account" /> </td> 
 			</tr></table>
		 </td>
		 </tr>
	
	</table>	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	 </s:form>
</body>
</html>