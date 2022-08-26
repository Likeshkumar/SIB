<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
</head>
<script type="text/javascript">
function validatedtls(){
	//alert("hiiii");
	var embname = document.getElementById("embossingname");
	//alert(embname.value);
	var mobileno = document.getElementById("mobileno");
	//alert(mobileno.value);
	var fname = document.getElementById("fname");
	//alert(fname.value);
	var mname =  document.getElementById("mname");
	//alert(mname.value);
	var lname = document.getElementById("lname");
	//alert(lname.value);
	var dob = document.getElementById("dob");
	//alert(dob.value);
	var marstat = document.getElementById("martialstat");
	//alert(marstat.value);
	var nation = document.getElementById("nationality");
	var spouse = document.getElementById("spouse");
	var mothername = document.getElementById("mothername");
	var fathername= document.getElementById("fathername");
	var mail = document.getElementById("email");
	
	var pobox = document.getElementById("pobox");
	var phouseno = document.getElementById("phouseno");
	var pstname = document.getElementById("pstname");
	var pwardname = document.getElementById("pwardname");
	var pcity = document.getElementById("pcity");
	var pdist = document.getElementById("pdist");
	
	if(embname.value == ''){
		errMessage(embname, "Please Enter Embossing Name");
		return false;
	}
	if(mobileno.value == ''){
		errMessage(mobileno, "Please Enter Mobile No");
		return false;
	}
	if(fname.value == ''){
		errMessage(fname, "Please Enter First Name ");
		return false;
	}
	if(mname.value == ''){
		errMessage(mname, "Please Enter Middle Name ");
		return false;
		}
	if(lname.value == ''){
		errMessage(lname, "Please Enter Last Name ");
		return false;
		}
	if(dob.value == ''){
		errMessage(dob, "Please Enter DOB ");
		return false;
		}
	if(marstat.value == ""){
		//alert("Test");
		errMessage(marstat, "Please Enter Martial Status");
		return false;
		}
	if(nation.value == ''){
		errMessage(nation, "Please Enter Nationality ");
		return false;
		}
	if(spouse.value == ''){
		errMessage(spouse, "Please Enter Spouse Name");
		return false;
		}
	if(mothername.value == ''){
		errMessage(mothername, "Please Enter Mother Name ");
		return false;
		}
	if(fathername.value == ''){
		errMessage(fathername, "Please Enter Father Name ");
		return false;
		}
	if(mail.value == ''){
		errMessage(mail, "Please Enter Mail-Id  ");
		return false;
		}
	
	if(pobox.value == ''){
		errMessage(pobox, "Please Enter Po-Box  ");
		return false;
		}
	if(phouseno.value == ''){
		errMessage(phouseno, "Please Enter P-Houseno  ");
		return false;
		}
	if(pstname.value == ''){
		errMessage(pstname, "Please Enter P-Street name  ");
		return false;
		}
	if(pwardname.value == ''){
		errMessage(pwardname, "Please Enter P-Ward Name  ");
		return false;
		}
	if(pcity.value == ''){
		errMessage(pcity, "Please Enter P-City  ");
		return false;
		}
	if(pdist.value == ''){
		errMessage(pdist, "Please Enter P-District  ");
		return false;
		}
}
</script>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="customerdetailform" action="updateCustomerDetailsCustomerDetailsAction.do" autocomplete="off" >
  
 	<table border="0" cellpadding="0" cellspacing="0" width="50%" class="table">
	 		<tr>
	 		 	<td> Embossing Name</td>
	 			<td> <s:textfield  value="%{embossingname}" name="embossingname" id="embossingname" maxlength="25"/> </td>	 
	 		
	 		 	<td> Mobile No</td>
	 			<td> <s:textfield  value="%{mobileno}" name="mobileno" id="mobileno" maxlength="15"/> </td>	 
	 		</tr>
	 		
	 		<tr>
	 		 	<td> Fname</td>
	 			<td> <s:textfield  value="%{fname}" name="fname" id="fname" maxlength="25"/> </td>	 
	 		
	 		 	<td> Mname</td>
	 			<td> <s:textfield  value="%{mname}" name="mname" id="mname" maxlength="25"/> </td>	 
	 		</tr>
	 		
	 		<tr>
	 		 	<td> Lname</td>
	 			<td> <s:textfield  value="%{lname}" name="lname" id="lname" maxlength="10"/> </td>	 
	 		
	 		 	<td> DOB</td>
	 			<td> <s:textfield  value="%{dob}" name="dob" id="dob" maxlength="10"/> </td>	 
	 		</tr>
	 		
	 		<tr>
	 		 	<td> Martial Status</td>
	 			<td> <s:textfield  value="%{martialstatus}" name="martialstatus" id="martialstat" maxlength="2"/> </td>	 
	 		
	 		 	<td> Nationality</td>
	 			<td> <s:textfield  value="%{nationality}" name="nationality" id="nationality" maxlength="10"/> </td>	 
	 		</tr>
	 		
	 		<tr>
	 		 	<td>Spouse Name</td>
	 			<td> <s:textfield  value="%{spousename}" name="spousename" id="spouse" maxlength="25"/> </td>	 
	 		
	 		 	<td>Mother Name</td>
	 			<td> <s:textfield  value="%{mothername}" name="mothername" id="mothername" maxlength="25"/> </td>	 
	 		</tr>
	 		
	 		<tr>
	 		 	<td>Father Name</td>
	 			<td> <s:textfield  value="%{fathername}" name="fathername" id="fathername" maxlength="25"/> </td>	 
	 		
	 		 	<td>E-Mail</td>
	 			<td> <s:textfield  value="%{email}" name="email" id="email" maxlength="25"/> </td>	 
	 		</tr>
	 		
	 		
	 		<tr>
	 		 	<td>P Box Name</td>
	 			<td> <s:textfield  value="%{pobox}" name="pobox" id="pobox" maxlength="20"/> </td>	 
	 		
	 		 	<td>P House No</td>
	 			<td> <s:textfield  value="%{phouseno}" name="phouseno" id="phouseno" maxlength="20"/> </td>	 
	 		</tr>
	 		
	 		 
	 		
	 		<tr>
	 		 	<td>P Street Name</td>
	 			<td> <s:textfield  value="%{pstname}" name="pstname" id="pstname" maxlength="20"/> </td>	 
	 		
	 		 	<td>P Ward No</td>
	 			<td> <s:textfield  value="%{pwardname}" name="pwardname" id="pwardname" maxlength="20"/> </td>	 
	 		</tr>
	 		
	 		<tr>
	 		 	<td>P City</td>
	 			<td> <s:textfield  value="%{pcity}" name="pcity" id="pcity" maxlength="25"/> </td>	 
	 		
	 		 	<td>P Dist</td>
	 			<td> <s:textfield  value="%{pdist}" name="pdist" id="pdist" maxlength="25"/> </td>	 
	 		</tr>
	 		
	 		<table border=0 cellpadding="0" cellspacing="0" width="35%" class="table">
	 		<tr>
				<td>Secondary Account No</td>
				<td>: 	
					<select  id="acccountno" name="acccountno" style='height:80px' multiple="multiple">
					<s:iterator value="acctno">
						<option value="${ACCOUNTNO}">${ACCOUNTNO}</option>
					</s:iterator> 
					</select>
				</td>				
			</tr>
	 		</table>
	 		
	 		<s:hidden name="cardno"/>
	 		<s:hidden name="custid"/>
	</table>
	<br><br>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
			<td> <s:submit value="Update" name="submit" id="submit" onclick="return validatedtls()"/> </td>
			<td> <input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/> </td>
		</tr>
	</table>
</s:form> 
</body>
