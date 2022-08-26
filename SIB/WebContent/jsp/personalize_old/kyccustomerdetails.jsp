<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>


<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>

</head>
<style>
div table td{
	padding-top:5px;
}
</style>
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
<s:form action="savepersonalKYCorderPersonalCardOrderAction"  name="custinfoform" autocomplete = "off" namespace="/">
<div>

<s:iterator value="kyccustdetails">
  <fieldset style="width:870px;"><legend>  Personal Information  </legend>
  <table border='0' width="100%" >
  <tr>
  	<td align="left" >First Name<b class="mand">*</b></td>
	<td align ="left">
		<input type="text" name="fname" id="fname" maxlength="15" value="${FNAME}" onKeyPress=" return alphabets(event);" style="width:150px">
	</td>
	<td align="left">Middle Name<b class="mand">*</b></td>
	<td>
		<input type="text" name="mname" id="mname" maxlength="15" value="${MNAME}" onKeyPress=" return alphabets(event);" style="width:150px">
	</td>
	<td align="left">Last Name<b class="mand">*</b></td>
	<td> 
		<input type="text" name="lname" id="lname" maxlength="15" value="${LNAME}" onKeyPress=" return alphabets(event);" style="width:150px">
	</td>
  </tr>
  <tr>
	<td align="left">Father Name<b class="mand">*</b></td>
	<td>
		<input type="text" name="f_name" id="f_name" maxlength="15" value="${FATHER_NAME}" onKeyPress=" return alphabets(event);" style="width:150px">
	</td>
	<td align="left">Mother Name<b class="mand">*</b></td>
	<td>
		<input type="text" name="m_name" id="m_name" maxlength="15" value="${MOTHER_NAME}" onKeyPress=" return alphabets(event);" style="width:150px">
	</td>
	<td align="left">Gender<b class="mand">*</b></td>
	<td>
		<s:radio list="#{'M':'Male','F':'Female'}" id="gender" name="gender" value="%{GENDER}"/>
	</td>
 </tr>
  <tr>
		<td align="left">MaritalStatus<b class="mand">*</b></td>
		<td>
			<s:radio list="#{'M':'Married','U':'Single'}" name="mstatus" id="mstatus" value="%{MARITAL_STATUS}" onchange="enableSpoucenamefiled(this.value);"/>
		</td>
		<td align="left">Spouse Name<b class="mand">*</b></td>
		<td>
			<input type="text" name="spname" id="spname" maxlength="25" value="${SPOUSE_NAME}" onKeyPress=" return alphabets(event);" style="width:150px">
		</td>
		<td align="left">Occupation<b class="mand">*</b></td>
		<td>
		<input type="text" name="work" id="work" maxlength="25" value="${OCCUPATION}"  onKeyPress=" return alphabets(event);" style="width:150px">
		</td>
  </tr>
  </table>
 </fieldset>
 <br>
 <fieldset><legend>  Identity Information   </legend>
 <table border='0' width="100%" cellspacing="5" cellpadding="5">
  <tr>
	<td align="left" width="15%">Nationality<b class="mand">*</b></td>
	<td>  
		<s:select label="Nationality"  
		headerKey="-1" headerValue="Select Nation"
		list="nationlist" 
		listValue="%{NATION}"
		listKey="%{NATION_ID}" 
		id="nations" 
		name="nations" 
		value="%{NATIONALITY}" 
		style="width:200px" />
	</td>
    <td align="left">Date Of Birth<b class="mand">*</b></td>
	<td>
		<input type="text" name="dob" id="dob"  readonly="readonly" value="${DOB}" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.custinfoform.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
	</td>
	<td align="left">E-Mail Address<b class="mand">*</b></td>
	<td><s:textfield name="email" id="email" value="%{EMAIL_ADDRESS}" /></td>
  </tr>
  <tr>
	<td align="left">Mobile Number<b class="mand">*</b></td>
	<td><s:textfield name="mobile" id="mobile"  maxlength="13" value="%{MOBILE_NO}"  /></td>
	<td align="left">Phone Number<b class="mand">*</b></td>
	<td><s:textfield name="phone" id="phone" maxlength="15" value="%{PHONE_NO}"  /></td>
  </tr>


  <tr>
	<td align="left"><b style="color: Red">*</b>Identity Document</td>
	<td>
		<s:select 
		headerKey="-1" headerValue="Select Document"
		list="documenttypelist" 
		listValue="%{DOC_TYPE}"
		listKey="%{DOC_ID}" 
		id="iddoc" 
		name="iddoc" 
		value="%{ID_DOCUMENT}" 
		style="width:200px"/>		
	</td>
   	<td align="left">Identity Number<b class="mand">*</b></td>
   	<td><input type="text" name="idno" id="idno" maxlength="30" value="${ID_NUMBER}" onKeyPress=" return alphanumerals(event);"></td>
  </tr>
  </table>
 </fieldset>
 <br>
<fieldset><legend>  Address Details </legend>
<table border='0' width="100%">
<tr>
	<td align="left">Postal Address 1<b class="mand">*</b></td>
	<td>
			<s:textfield name="paddress1" id="paddress1" maxlength="50" value="%{POST_ADDR1}" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 1<b class="mand">*</b></td>
	<td>
		<s:textfield name="raddress1" id="raddress1" maxlength="50" value="%{RES_ADDR1}" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr>
<tr>
	<td align="left">Postal Address 2<b class="mand">*</b></td>
	<td>
			<s:textfield name="paddress2" id="paddress2" maxlength="50" value="%{POST_ADDR2}" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 2<b class="mand">*</b></td>
	<td>
		<s:textfield name="raddress2" id="raddress2" maxlength="50" value="%{RES_ADDR2}" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr>
<tr>
	<td align="left">Postal Address 3</td>
	<td>
			<s:textfield name="paddress3" id="paddress3" maxlength="50" value="%{POST_ADDR3}" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 3</td>
	<td>
		<s:textfield name="raddress3" id="raddress3" maxlength="50" value="%{RES_ADDR3}" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr>
<tr>
	<td align="left">Postal Address 4</td>
	<td>
			<s:textfield name="paddress4" id="paddress4" maxlength="50" value="%{POST_ADDR4}" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 4</td>
	<td>
		<s:textfield name="raddress4" id="raddress4" maxlength="50" value="%{RES_ADDR4}" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr>
 </table>
 <table border='0' width="100%">
	<tr>
			<td colspan="6">
				<font color="red">Use the Postal Address as Resident Address</font>
				<input type="checkbox" name="same" id="same" value="Y" onclick="disabletheResidetnaddress();"/>
			</td>
	  </tr>

  </table>
</fieldset>
</s:iterator>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >

	<tr>
		<td>
			<s:submit value="Order" name="Order" onclick="return customerinfo_checker();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			<!--  <input type="button" name="cancle" value="Cancel"  onclick="return forwardtoCardOrderPage();"> -->
		</td>
	</tr>
</table>
</div>
</s:form>
</div>