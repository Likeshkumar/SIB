<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
 
<script>

	function showSpouse( marstatus ){
		//alert(marstatus);
		var mstat=document.getElementById(marstatus);
		var spname = document.getElementById("spname");
		if( mstat.value == "M"){
			
			spname.disabled=false;
			spname.focus();
			
		}else{
			spname.value = "";
			spname.disabled=true;
			
		}
	}
	function showIdno( doc ) {
		 
		var idno = document.getElementById("photoidno");
		if( doc != "-1"){
			idno.disabled=false;
			idno.focus(); 
			
		}else{
			idno.disabled=true;
			
		}
	}
	function showResident( rescheck ){
		var resadd1 = document.getElementById("resaddress1");
		var resadd2 = document.getElementById("resaddress2");
		var resadd3 = document.getElementById("resaddress3");
		var resadd4 = document.getElementById("resaddress4");
		
		if(!rescheck){
			resadd1.disabled=false;
			resadd2.disabled=false;
			resadd3.disabled=false;
			resadd4.disabled=false;
			resadd1.focus();
		}else{
			resadd1.disabled=true;
			resadd2.disabled=true;
			resadd3.disabled=true;
			resadd4.disabled=true;
		}
	}

</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
<s:form action="registerCustomerActionLateralRegister.do"  name="custinfoform" id="custinfoform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">

 <s:hidden name="custtype" value="%{custregbean.custtype}" />

 <s:hidden name="cardno" value="%{cardno}" />  

 <s:hidden name="custid" value="%{custid}" /> 
 
 <s:set name="custidavail" value="%{custid}" /> 

<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="filtertab">	 
		<s:if test="%{#custidavail!=''}">
		<tr><td colspan="4"> CARD NUMBER : <s:property value="custregbean.cardno"/> </td></tr>
		</s:if>
	 	<tr>
			<td>First Name<span class="mand">*</span></td>
			<td><s:textfield name="firstname" id="firstname" maxlength="15"  value="%{custregbean.firstname}" onKeyPress=" return alphabets(event);" /></td>
			
			 <td>Middle Name </td>
		 	<td><s:textfield name="midname" id="midname" maxlength="15" value="%{custregbean.midname}" onKeyPress=" return alphabets(event);" /></td>
			
			 <td >Last Name </td>
	 	     <td> <s:textfield   name="lastname" id="lastname" maxlength="15" value="%{custregbean.lastname}" onKeyPress=" return alphabets(event);" /></td>  
	    </tr>
	 
	 	<tr>
			<td>Father Name<span class="mand">*</span></td>
			<td><s:textfield name="fahtername" id="fathername" maxlength="40" value="%{custregbean.fahtername}" onKeyPress=" return alphabets(event);"  /></td>
			
			 <td>Mother Name </td>
		 	<td><s:textfield name="mothername" id="mothername" maxlength="40" value="%{custregbean.mothername}" onKeyPress=" return alphabets(event);" /></td>
			
			 <td>&nbsp; </td>
	 	     <td> &nbsp; </td>  
	    </tr>
	 
	 
	 	<tr>
	 	
	 		<td> Gender <span class="mand">*</span></td>
			<td>
				<s:select list="#{'-1':'-SELECT-','M':'Male','F':'Female'}" id="gender" name="gender" value="%{custregbean.gender}"/>
				 
			</td>
			
			
			<td>Marital Status<span class="mand">*</span></td>
			<td>
				<s:select list="#{'-1':'-SELECT-','M':'Married','U':'Un Married'}" id="mstatus" name="mstatus" value="%{custregbean.mstatus}" onchange="showSpouse( this.id )"/>
				 
			</td>
			
			 <td> Spouse Name </td>
		 	<td><s:textfield name="spname" id="spname" maxlength="15" value="%{custregbean.spname}"/></td> 
	    </tr>
	    
	 
	 	<tr>
	 	 <td>Date Of Birth<b class="mand">*</b></td>
			<td>
			 
				<s:textfield name="dob" id="dob" value="%{custregbean.dob}" readonly="true" style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.custinfoform.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td> 
			
			<td>Occupation<span class="mand">*</span></td>
			<td><s:textfield name="occupation" id="occupation" maxlength="15" value="%{custregbean.occupation}" onKeyPress="return alphabets(event);" /></td>
			
			 <td>Nationality<b class="mand">*</b></td>
			<td>  
				<s:select label="Nationality"  
				headerKey="-1" headerValue="Select Nation"
				list="custregbean.nationalitylist" 
				listValue="%{NATION}"
				listKey="%{NATION_ID}" 
				id="nations" 
				name="nations"
				value="%{custregbean.nationality}" 
				style="width:200px" 
				/>
			</td>
			
			
	    </tr> 
	 
		 <tr>
			<td >Mobile No.<b class="mand">*</b></td>
			<td><s:textfield name="mobileno" id="mobileno"  maxlength="13" value="%{custregbean.mobileno}" onKeyPress="return numerals(event);"/></td> 
			
			<td >Phone Number </td>
				<td><s:textfield name="phoneno" id="phoneno" maxlength="15" value="%{custregbean.phoneno}" onKeyPress=" return numerals(event);"/></td> 
	   
			<td >E-Mail Address </td>
			<td><s:textfield name="email"  value="%{custregbean.email}" id="email" /> </td> 
		</tr>
		
		<tr>
			<td>Identity Document<b class="mand">*</b></td>
			<td>
				<s:select 
				headerKey="-1" headerValue="Select Document"
				list="custregbean.documenttypelist" 
				listValue="%{DOC_TYPE}"
				listKey="%{DOC_ID}" 
				id="reqdocuement" 
				name="reqdocuement"
				value="%{custregbean.reqdocuement}"  
				style="width:200px"/>		
			</td>			
			 
			<td >ID Number </td>
				<td><s:textfield name="documentid" id="documentid" maxlength="15" value="%{custregbean.documentid}"/></td> 
	   
			<td> &nbsp; </td>
				<td>&nbsp; </td> 
		</tr>
		
		<tr>
			<td >Application Date </td>
			<td>
				 <s:textfield  name="appdate" id="appdate" value="%{custregbean.appdate}" readonly="true"/>
				 <img src="style/images/cal.gif" id="image1" onclick="displayCalendar(document.custinfoform.appdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
			</td> 
			
			<td >App No. </td>
				<td><s:textfield name="appno" id="appno" value="%{custregbean.appno}" maxlength="15"  /></td> 
	   
			<td> &nbsp; </td>
				<td>&nbsp; </td> 
		</tr> 
		
		 <tr>
		 	<td colspan="6">
		 	<fieldset>
		 		<legend>Postal Address :</legend>
			 	<table>
			 	
			 	<tr>
					<td>  Address1<b class="mand">*</b></td>
					<td>
						<s:textfield name="paddress1" id="paddress1" maxlength="50" value="%{custregbean.paddress1}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				 
					<td>  Address2 </td>
					<td>
						<s:textfield name="paddress2" id="paddress2" maxlength="50" value="%{custregbean.paddress2}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				  
					<td>  Address3</td>
					<td>
						<s:textfield name="paddress3" id="paddress3" maxlength="50" value="%{custregbean.paddress3}"  onKeyPress=" return alphanumerals(event);"/>
					</td>   
				</tr>
				<tr>
					<td>  Address4 </td>
					<td>
						<s:textfield name="paddress4" id="paddress4" maxlength="50" value="%{custregbean.paddress4}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				</tr>
				</table>
				</fieldset>
			</td>
	 </tr>  
	 <tr><td colspan="6"><span style="color:blue;font-size:10px"><input type='checkbox' name="residentreq" onclick="showResident(this.checked)" /> Use Postal address as Resident </</span></td></tr>
	 
	  <tr>
		 	<td colspan="6">
		 	<fieldset>
		 		<legend>Resident Address :</legend>
		 		 
			 	<table>
			 	
			 	<tr>
					<td>  Address1<b class="mand">*</b></td>
					<td>
						<s:textfield name="resaddress1" id="resaddress1" maxlength="50" value="%{custregbean.resaddress1}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				 
					<td>  Address2 </td>
					<td>
						<s:textfield name="resaddress2" id="resaddress2" maxlength="50" value="%{custregbean.resaddress2}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				  
					<td>  Address3</td>
					<td>
						<s:textfield name="resaddress3" id="resaddress3" maxlength="50" value="%{custregbean.resaddress3}"  onKeyPress=" return alphanumerals(event);"/>
					</td>   
				</tr>
				<tr>
					<td>  Address4 </td>
					<td>
						<s:textfield name="resaddress4" id="resaddress4" maxlength="50" value="%{custregbean.resaddress4}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				</tr>
				</table>
				</fieldset>
			</td>
	 </tr> 
	 
		
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return customerinfo_checker()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 