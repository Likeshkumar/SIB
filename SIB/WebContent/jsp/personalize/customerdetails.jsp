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


<%
	String ErrorStatus=null;
	String StatusMsg=null;
	ErrorStatus = (String) session.getAttribute("getcustomedetailsErrorStatus");
	StatusMsg=(String) session.getAttribute("getcustomedetailsStatusMsg");
	session.removeAttribute("getcustomedetailsErrorStatus");
	session.removeAttribute("getcustomedetailsStatusMsg");
	if(ErrorStatus.equals("E"))
	{
%>
		<div align="center">
		<table border='0' cellpadding='0' cellspacing='0' width='80%' >
		<tr align="center">
			<td colspan="2">
				<font color="Red"><b><%=StatusMsg%></b></font>
			</td>
		</tr>
		</table>
		</div>
<%	
	}
	else
	{
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<div align="center">
<s:form action="savepersonalorderPersonalCardOrderAction"  name="custinfoform" enctype="multipart/form-data" onsubmit="parent.showprocessing()" autocomplete = "off" namespace="/">
	<s:hidden name="cardissuetype" id="cardissuetype" value="%{personalbean.cardissuetypecode}" />
  <fieldset style="width:870px;"><legend>  Personal Information  </legend>
  <table border='0' width="100%" >
  <s:hidden name="parentcard" id="parentcard" value="%{personalbean.parentcard}"/>
   <tr>
  	<td align="left" >Registration Type <b class="mand">*</b></td>
	<td align ="left">
	<s:if test='%{personalbean.cardissuetypecode!="$SUPLIMENT"}'> 
		<s:select list="#{'$FC':'Customer Card','$MC':'Merchant Card'}" id="regtype" headerValue="-SELECT-" headerKey="-1" name="regtype" style="width:150px" onchange="setMerchantCard(this.value)" />
	</s:if>
	<s:else>
		<s:property value="personalbean.cardissuetypedesc"/>
	</s:else>
	</td>
	<td colspan="6"> 
		&nbsp;
	</td>
  </tr>
  <tr>
  	<td align="left" >First Name<b class="mand">*</b></td>
	<td align ="left">
		<s:textfield name="fname" id="fname" maxlength="15"   style="width:150px" value="%{personalbean.firstname}" />
	</td>
	<td align="left">Middle Name </td>
	<td>
		<s:textfield  name="mname" id="mname" maxlength="15"  style="width:150px"  value="%{personalbean.middlename}" />
	</td>
	<td align="left">Last Name<b class="mand">*</b></td>
	<td> 
		<s:textfield  name="lname" id="lname" maxlength="15"   style="width:150px" value="%{personalbean.lastname}" />
	</td>
  </tr>
  <tr>
	<td align="left">Father Name </td>
	<td>
		<s:textfield name="f_name" id="f_name" maxlength="40" onKeyPress=" return alphabets(event);" style="width:150px" value="%{personalbean.fathername}" />
	</td>
	<td align="left">Mother Name </td>
	<td>
		<s:textfield  name="m_name" id="m_name" maxlength="40" onKeyPress=" return alphabets(event);" style="width:150px" value="%{personalbean.mothername}" />
	</td>
	<td align="left">Gender<b class="mand">*</b></td>
	<td>
		<s:select list="#{'M':'Male','F':'Female'}" id="gender" name="gender" headerKey="-1" headerValue="-SELECT-" value="%{personalbean.sex}"/>
	</td>
 </tr>
  <tr>
		<td align="left">MaritalStatus<b class="mand">*</b></td>
		<td>
			<s:select list="#{'U':'Single','M':'Married'}" name="mstatus" id="mstatus" onchange="enableSpoucenamefiled(this.value);"  style="width:150px" value="%{personalbean.married}"/>
		</td>
		<td align="left">Spouse Name </td>
		<td>
			<s:textfield  name="spname" id="spname" maxlength="25"  disabled="disabled" onKeyPress=" return alphabets(event);" style="width:150px"  />
		</td>
		<td align="left">Occupation<b class="mand">*</b></td>
		<td>
		<s:textfield  name="work" id="work" maxlength="25" onKeyPress=" return alphabets(event);" style="width:150px" value="%{personalbean.occupation}"/>
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
		name="nations" style="width:200px"
		value="%{personalbean.nationality}"
		 />
	</td>
    <td align="left">Date Of Birth<b class="mand">*</b></td>
	<td>
	 
		<s:textfield name="dob" id="dob"      onchange="return yearvalidation(this.id);" style="width:160px" value="%{personalbean.bday}" />		
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.custinfoform.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		<br/><small class="dt"> dd-mm-yyyy </small>
	</td>
	<td align="left">E-Mail Address<b class="mand">*</b></td>
	<td><s:textfield name="email" id="email"  value="%{personalbean.email}" /></td>
  </tr>
  <tr>
	<td align="left">Mobile Number<b class="mand">*</b></td>
	<td><s:textfield name="mobile" id="mobile"  maxlength="16"   value="%{personalbean.mobileno}" /></td>
	<td align="left">Phone Number </td>
	<td><s:textfield name="phone" id="phone" maxlength="16"   value="%{personalbean.phoneno}" /></td>
    <td align="left">Application Date<b class="mand">*</b></td>
	<td>
		<input type="text" name="appdate" id="appdate" value="<%=session.getAttribute("todaydate")%>"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<!-- <img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.custinfoform.appdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> -->
	</td> 
  </tr>


  <tr>
	<td align="left"><b style="color: Red">*</b>Identity Document   </td>
	<td>
		<s:select 
		headerKey="-1" headerValue="Select Document"
		list="documenttypelist" 
		listValue="%{DOC_TYPE}"
		listKey="%{DOC_ID}" 
		id="iddoc" 
		name="iddoc" style="width:200px"
		 value="%{personalbean.identitydocument}" 
		/>		
	</td>
   	<td align="left">Identity Number<b class="mand"  value="%{personalbean.phoneno}" />*</b></td>
   	<td><s:textfield  name="idno" id="idno" maxlength="30"   value="%{personalbean.identitydocument}"  /></td>
	<%-- <td align="left">Application Number </td>
	<td><s:textfield name="appno" id="appno" maxlength="30" onKeyPress=" return numerals(event);" value="00"/></td>   	 --%>
  </tr>
  <tr><td colspan="6"> <small class="dt"> Uploading file should be [.jpg, .jpeg, .gif, .png]. File size should not be more than 2 MB</small></td></tr>
  
		  <s:if test="uploadreq"> <input type="hidden" id="uploadreq" name="uploaderq" value="1"/> </s:if> 
		  <s:else> <input type="hidden" id="uploadreq" name="uploaderq" value="0"/>  </s:else>
 
  <tr>
  	<td>Upload Photo</td><td><s:file name="uploadphoto" id="uploadphoto" /></td>
  	<td>Upload Signature</td><td><s:file name="uploadsignature" id="uploadsignature" /></td>
  	<td>Upload Id-Proof</td><td><s:file name="uploadidproof" id="uploadidproof" /></td>
  	
  </tr>
  
  
  </table>
 </fieldset>
 <br>
<fieldset><legend>  Address Details </legend>
<table border='0' width="100%">
<%-- <tr>
	<td align="left">Postal Address 1<b class="mand">*</b></td>
	<td>
			<s:textfield name="paddress1" id="paddress1" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 1<b class="mand">*</b></td>
	<td>
		<s:textfield name="raddress1" id="raddress1" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr> --%>

 <tr>
	
	 
	 <td align="left">Resident Address <b class="mand">*</b></td>
	<td>
		<s:textarea name="raddress1" id="raddress1" maxlength="150"  value="%{personalbean.residentaladdress}"   /> 
	</td>
	
	<td align="left">Postal Address <b class="mand">*</b></td>
	<td>
		<s:textarea name="paddress1" id="paddress1" maxlength="150"  value="%{personalbean.postaladdress}" /> 
	</td>
	
</tr> 

 

<%-- <tr>
	<td align="left">Postal Address 2<b class="mand">*</b></td>
	<td>
			<s:textfield name="paddress2" id="paddress2" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 2<b class="mand">*</b></td>
	<td>
		<s:textfield name="raddress2" id="raddress2" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr>
<tr>
	<td align="left">Postal Address 3</td>
	<td>
			<s:textfield name="paddress3" id="paddress3" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 3</td>
	<td>
		<s:textfield name="raddress3" id="raddress3" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr>
<tr>
	<td align="left">Postal Address 4</td>
	<td>
			<s:textfield name="paddress4" id="paddress4" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>
	<td align="left">Resident Address 4</td>
	<td>
		<s:textfield name="raddress4" id="raddress4" maxlength="50" onKeyPress=" return alphanumerals(event);"/>
	</td>	
</tr> --%>
 </table>
 <table border='0' width="100%">
	<tr>
			<td colspan="6">
				<font color="red">Use the  Resident Address  as Postal Address</font>
				<input type="checkbox" name="same" id="same" value="Y" onclick="disabletheResidetnaddress();"/>
			</td>
	  </tr>

  </table>
</fieldset>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>
			<s:submit value="Order" name="Order" onclick="return customerinfo_checker();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>

</s:form>
</div>
<% } %>
