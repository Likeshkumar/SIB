<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
 .tbheader{
 	font-weight:bold;
 }
 .txt{
 	font-weight:bold;
 }
 </style>
<head>

<%
String instid = (String)session.getAttribute("Instname");
 
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 
<script>

function validateDate(selecteddate)
{
	//alert(selecteddate);
	var url = "validateSelectedDateDebitCustomerRegister.do?selecteddate="+selecteddate;
	 
	var result = AjaxReturnValue(url);
	//alert(result);
	
	if(result.trim()!='TRUE')
		{
		errMessage(document.getElementById("dob"), result);
		document.getElementById("dob").value='';
		return false;
		}
	
}
 
function validatetab1()
{
	var branchcode = document.getElementById("branchcode");
	if( branchcode ){ if( branchcode.value == "") { errMessage(branchcode, "Select Branch Code !");return false; } }
	
	var embname = document.getElementById("embname")
	if( embname ){ if( embname.value == "") { errMessage(embname, "Enter Embosing Name !");return false; } }
	
	var encname = document.getElementById("encname");
	if( encname ){ if( encname.value == "") { errMessage(encname, "Enter Encoding Name !");return false; } }
	
	var productcode = document.getElementById("productcode");
	if( productcode ){ if( productcode.value == "" || productcode.value == "-1") { errMessage(productcode, "Select Product Code !");return false; } }
	
	var subproduct = document.getElementById("subproduct");
	if( subproduct ){ if( subproduct.value == ""  || subproduct.value == "-1") { errMessage(subproduct, "Select Sub Product Code !");return false; } }
	
	var cinidbasedon = document.getElementById("cinidbasedon");
	var custidlength = document.getElementById("custidlength");
	var customeridno = document.getElementById("customeridno");
	
	//alert('cinidbasedon'+cinidbasedon.value);
	//alert('custidlength'+custidlength.value);
	//alert('customeridno'+customeridno.value);
	
	if( cinidbasedon ){ if( cinidbasedon.value == "CBS")
		{
		
			if( customeridno ){ if( customeridno.value == ""  || customeridno.value == "-1") { errMessage(customeridno, "Enter Customer Id !");return false; } }
			
			
			if(customeridno.value.length!=custidlength.value)
			{
				//alert(customeridno.value.length+"-----"+custidlength.value);
			errMessage(customeridno, "CustomerId Should Be "+custidlength.value+" Digit!");
			return false;
			}
			
			var url = "checkCustomerIdExistDebitCustomerRegister.do?customerid="+customeridno.value;
			var result = AjaxReturnValue(url);
			//alert(result);
			if(result!='NEW')
				{
				errMessage(customeridno, "Customer Id Alredy Exist Try Different !");
				return false; 
				
				}
			
		}
	}
	  
		var limitid = document.getElementById("limitid");
	if( limitid ){ if( limitid.value == ""  || limitid.value == "-1") { errMessage(limitid, "Select Limit !");return false; } }
	
	var feecode = document.getElementById("feecode");
	if( feecode ){ if( feecode.value == ""  || feecode.value == "-1") { errMessage(feecode, "Select Fee  !");return false; } }

	var accounttypevalue = document.getElementById("accounttypevalue");
	if( accounttypevalue ){ if( accounttypevalue.value == ""  || accounttypevalue.value == "-1") { errMessage(accounttypevalue, "Select accounttypevalue !");return false; } }
	var accountsubtypevalue = document.getElementById("accountsubtypevalue");
	if( accountsubtypevalue ){ if( accountsubtypevalue.value == ""  || accountsubtypevalue.value == "-1") { errMessage(accountsubtypevalue, "Selct accountsubtypevalue !");return false; } }
	var tab2_currency = document.getElementById("tab2_currency");
	if( tab2_currency ){ if( tab2_currency.value == ""  || tab2_currency.value == "-1") { errMessage(tab2_currency, "Select Currency !");return false; } }
	var accountnovalue = document.getElementById("accountnovalue");
	var accountnolength = document.getElementById("accountnolength");	
	//alert('accountno value -'+accountnovalue.value.length+":::::"+accountnolength.value);
	
	if(accountnovalue.value.length!=accountnolength.value)
		{
		errMessage(accountnovalue, "Account No Should be "+accountnolength.value+" Digit!");
		return false;
		}
	
	var url = "checkAccountNoExistDebitCustomerRegister.do?accountnovalue="+accountnovalue.value;
	var result = AjaxReturnValue(url);
	//alert(result);
	if(result!='NEW')
		{
		errMessage(accountnovalue, "Account No Alredy Exist Try Differant !");
		return false; 
		
		}
	
	
	return true;
	
}

function validatetab2_customer()
{
	//alert(document.getElementById("accountnovalue").value);
var firstname = document.getElementById("firstname");
if( firstname ){ if( firstname.value == ""  || firstname.value == "-1") { errMessage(firstname, "Enter firstname !");return false; } }
var middlename = document.getElementById("middlename");
/*if( middlename ){ if( middlename.value == ""  || middlename.value == "-1") { errMessage(middlename, "Select middlename !");return false; } }*/
var lastname = document.getElementById("lastname");
/*if( lastname ){ if( lastname.value == ""  || lastname.value == "-1") { errMessage(lastname, "Enter lastname !");return false; } }*/
var dob = document.getElementById("dob");
if( dob ){ if( dob.value == ""  || dob.value == "-1") { errMessage(dob, "Select Date of Birth !");return false; } }
var gender = document.getElementById("gender");
if( gender ){ if( gender.value == ""  || gender.value == "-1") { errMessage(gender, "Select Gender !");return false; } }
var mstatus = document.getElementById("mstatus");
if( mstatus ){ if( mstatus.value == ""  || mstatus.value == "-1") { errMessage(mstatus, "Select merital Status !");return false; } }
var nationality = document.getElementById("nationality");
if( nationality ){ if( nationality.value == ""  || nationality.value == "-1") { errMessage(nationality, "Enter Nationality !");return false; } }
var documentprovided = document.getElementById("documentprovided");
if( documentprovided ){ if( documentprovided.value == ""  || documentprovided.value == "-1") { errMessage(documentprovided, "Select documentprovided !");return false; } }
var documentnumber = document.getElementById("documentnumber");
if( documentnumber ){ if( documentnumber.value == ""  || documentnumber.value == "-1") { errMessage(documentnumber, "Enter document number  !");return false; } }
var spousename = document.getElementById("spousename");
//alert(mstatus.value);
if(mstatus.value=='M'){
if( spousename ){ if( spousename.value == ""  || spousename.value == "-1") { errMessage(spousename, "Enter spousename !");return false; } }
}
var mothername = document.getElementById("mothername");
if( mothername ){ if( mothername.value == ""  || mothername.value == "-1") { errMessage(mothername, "Enter mothername !");return false; } }
var fathername = document.getElementById("fathername");
//if( fathername ){ if( fathername.value == ""  || fathername.value == "-1") { errMessage(fathername, "Enter fathername !");return false; } }


//alert(acccountnovalue.value.length+"-----"+accountnolength.value);
//if(acccountnovalue.length!=accountnolength.value)
//	{
//	errMessage(accountnovalue, "Account No Length Should be  "+accountnolength.value+" Digit!");return false;
//	}

//}

}

function ismarried()
{
	var mstatus= document.getElementById("mstatus");
	var spousename = document.getElementById("spousename");
	if(mstatus.value!='M'){
		spousename.readOnly = true;
		spousename.value="";
	}else
	{
		
		spousename.readOnly = false;
	}	
}


function tab3_validation(){
var mobile = document.getElementById("mobile");
if( mobile ){ if( mobile.value == ""  || mobile.value == "-1") { errMessage(mobile, "Enter Mobile no !"); return false; } }
var email = document.getElementById("email");
/*
if( email ){ if( email.value == ""  || email.value == "-1") { errMessage(email, "Enter Email id !");return false; } 
else
 {
	//alert('test valid');
	 if( !emailvalidator(email.value) )
	 {
	 	errMessage(email,"InValid E-Mail Address");
	 	return false;
	 }
 }
*/
//}
var p_poxbox = document.getElementById("p_poxbox");
if( p_poxbox ){ if( p_poxbox.value == ""  || p_poxbox.value == "-1") { errMessage(p_poxbox, "Enter PO Box NO !");return false; } }
var p_houseno = document.getElementById("p_houseno");
if( p_houseno ){ if( p_houseno.value == ""  || p_houseno.value == "-1") { errMessage(p_houseno, "Enter House no !");return false; } }
var p_streetname = document.getElementById("p_streetname");
if( p_streetname ){ if( p_streetname.value == ""  || p_streetname.value == "-1") { errMessage(p_streetname, "Enter Street name !");return false; } }
var p_wardnumber = document.getElementById("p_wardnumber");
//if( p_wardnumber ){ if( p_wardnumber.value == ""  || p_wardnumber.value == "-1") { errMessage(p_wardnumber, "Enter Ward no !");return false; } }
var p_city = document.getElementById("p_city");
if( p_city ){ if( p_city.value == ""  || p_city.value == "-1") { errMessage(p_city, "Enter City !");return false; } }
var p_district = document.getElementById("p_district");
if( p_district ){ if( p_district.value == ""  || p_district.value == "-1") { errMessage(p_district, "Enter District !");return false; } }
var p_phone1 = document.getElementById("p_phone1");
//if( p_phone1 ){ if( p_phone1.value == ""  || p_phone1.value == "-1") { errMessage(p_phone1, "Enter Phone 1 !");return false; } }
var p_phone2 = document.getElementById("p_phone2");
//if( p_phone2 ){ if( p_phone2.value == ""  || p_phone2.value == "-1") { errMessage(p_phone2, "Enter Phone 2 !");return false; } }

//sameasperm



var c_poxbox = document.getElementById("c_poxbox");
if( c_poxbox ){ if( c_poxbox.value == ""  || c_poxbox.value == "-1") { errMessage(c_poxbox, "Enter Contact PO Box NO !");return false; } }
var c_houseno = document.getElementById("c_houseno");
if( c_houseno ){ if( c_houseno.value == ""  || c_houseno.value == "-1") { errMessage(c_houseno, "Enter Contact House no !");return false; } }
var c_streetname = document.getElementById("c_streetname");
if( c_streetname ){ if( c_streetname.value == ""  || c_streetname.value == "-1") { errMessage(c_streetname, "Enter Contact Street name !");return false; } }
var c_wardnumber = document.getElementById("c_wardnumber");
//if( c_wardnumber ){ if( c_wardnumber.value == ""  || c_wardnumber.value == "-1") { errMessage(c_wardnumber, "Enter Contact Ward no !");return false; } }
var c_city = document.getElementById("c_city");
if( c_city ){ if( c_city.value == ""  || c_city.value == "-1") { errMessage(c_city, "Enter Contact City !");return false; } }
var c_district = document.getElementById("c_district");
if( c_district ){ if( c_district.value == ""  || c_district.value == "-1") { errMessage(c_district, "Enter Contact District !");return false; } }
var c_phone1 = document.getElementById("c_phone1");
//if( c_phone1 ){ if( c_phone1.value == ""  || c_phone1.value == "-1") { errMessage(c_phone1, "Enter Contact Phone 1 !");return false; } }
var c_phone2 = document.getElementById("c_phone2");
//if( c_phone2 ){ if( c_phone2.value == ""  || c_phone2.value == "-1") { errMessage(c_phone2, "Enter Contact Phone 2 !");return false; } }
}

function tab4_validation()
{
	var ensure = document.getElementById("ensure");
	//alert("ensure::::"+ensure);
	if(ensure.checked==false)
		{
		errMessage(c_phone2, "Ensure All Data are Verified !");return false; 
		}
}

function sameparam()
{
	var sameasperm = document.getElementById("sameasperm");
	if( sameasperm.checked ){
	alert('yes');
	}
}

</script>


<title>Customer Registration1</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body ><!-- selectTab(); -->

<sx:div id="default"  name="defaultValue"  value="default"   >
<s:if test ="%{dbtcustregbean.applicationid!=null}">
<!-- ORDER NO : <s:property value="%{dbtcustregbean.applicationid}" /> -->
</s:if>  
</sx:div>


<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 100%; margin:0 auto; height: 600px;" doLayout="true" 

selectedTab="%{dbtcustregbean.reglevel}" 

 >
<sx:div id="1"  name="tab" label="Product Information1" value="1"   >
	<s:form action="saveProductInformationDebitCustomerRegister.do"  id="regform" name="regform" autocomplete = "off" namespace="/"> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Product Information
		</th></tr> 
		<tr>
			 <td class="txt"> Branch </td>
		  <td>
		  <s:property value="dbtcustregbean.branchcode"/>
		  
			
		 </td>  
			 <td class="txt"> Embossing Name  </td> 
			 <td> 
			  <s:property 
			 value="%{dbtcustregbean.embname}" 
			 />
			 </td>
			 <td class="txt"> Encoding Name  </td> <td> 
			 <s:property 
			 value="%{dbtcustregbean.encname}" 
			 />
		 </td>  
		</tr>
		<tr>
		  <td class="txt"> Product     </td>   
		  <td> 
		 <s:iterator value="dbtcustregbean.productlist">   
					 <s:if test="PRODUCT_CODE==dbtcustregbean.productcode" >
					 					 <s:property value="CARD_TYPE_NAME"/>   
					 </s:if>  
					</s:iterator>   
		 </td>
			   <td class="txt"> Sub Product     </td>
			    <td>
			   <s:iterator value="dbtcustregbean.subproductlist">   
					 <s:if test="SUB_PROD_ID==dbtcustregbean.subproduct" >
					 					 <s:property value="SUB_PRODUCT_NAME"/>   
					 </s:if>  
					</s:iterator>  
				</td>
			   	
			<s:if test ="%{dbtcustregbean.cinidbasedon=='CBS'}">
			  <td class="txt"> CustomerID  </td> <td> 
			 <s:property value="%{dbtcustregbean.customeridno}" />
			 </td>  
			 </s:if>
			 
			  </tr> 
		 <tr>
		 <td class="txt"> Limit     </td> 
			  <td> 
			  <s:property value="%{dbtcustregbean.limitname}" />
			  </td>
		
		
		 <td class="txt"> Fee     </td> 
			  <td> 
			  <s:property value="%{dbtcustregbean.feename}" />
			  </td>
		</tr> 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Account Information
		 </th>
		 <tr>
		 <td colspan="6" width="100%" >
		 	<table border='1' id="AccountInformation"  style='display:block' >
		 	<tr>
		 		
		 		<td>
		 		<!-- accttypelist -->
		 		Account Type : 
		 		<select id="accounttypevalue" name="accounttypevalue" onchange="getAcctSubTypeList()">
					<s:if test ="%{dbtcustregbean.accttypelist.isEmpty()}">
					<option value="">No Account Type Found</option>
					</s:if>
					<s:else>
								<option value="" >Account Type</option>   
								<s:iterator value="dbtcustregbean.accttypelist">
								<option value="<s:property value="ACCTTYPEID"/>" 
								<s:if test="ACCTTYPEID==dbtcustregbean.accounttypevalue" >
								selected
								</s:if>
								> 
								<s:property value="ACCTTYPEDESC"/></option>
								</s:iterator>
					</s:else>
					</select> 
					
		 		</td>
		 		<td>
		 		
		 		<td>
		 		<!-- acctsubtypelist -->
		 		Account SUB Type :
		 		<select id="accountsubtypevalue" name="accountsubtypevalue" >
		 		<option>-SELECT-</option>
		 		<s:iterator value="dbtcustregbean.accuntsubtypelist">
		 		<option value="<s:property value="ACCTSUBTYPEID"/>" 
		 		<s:if test="ACCTSUBTYPEID==dbtcustregbean.accountsubtypevalue" >
								selected
				</s:if>
		 		><s:property value="ACCTSUBTYPEDESC"/>
		 		</s:iterator>
		 		
				</select> 
		 		</td>
		 		<td>
		 		
		 		
		 		</td>
		 		<td>Currency:
		 		<select id="tab2_currency" name="tab2_currency" onchange="">
					<s:if test ="%{dbtcustregbean.currencylist.isEmpty()}">
					<option value="">No Currency Found</option>
					</s:if>
					<s:else>
								<option value="" >Select Currency</option>   
								<s:iterator value="dbtcustregbean.currencylist">  
								<option value="<s:property value="NUMERIC_CODE"/>"
								<s:if test="NUMERIC_CODE==dbtcustregbean.tab2_currency" >
								selected
								</s:if>
								><s:property value="CURRENCY_DESC"/></option>
								</s:iterator>
					</s:else>
					</select> 
		 		
		 		</td>
		 		<td> Primary Account Number : 
		 		<s:textfield  id="accountnovalue" name="accountnovalue" maxlength="%{dbtcustregbean.acctnolen}"  /> 
		 		<!-- <input onclick="addRow(this.form);" type="button" value="Add Account" />  --></td> 
 			</tr>
 			
 			</table>
 			
 			
 			
		 </td>
		 </tr>
	</table>	
 
</s:form>
</sx:div>
  
 

  
<sx:div id="2" name="tab"  label="Personal Details" value="2"   >
 	<s:form action="customerDetailsAddDebitCustomerRegister.do"  id="regform1" name="regform1" autocomplete = "off" namespace="/">
 		<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/> 
 		
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Personal Information

	</th><tr> 
	<tr>
		 <td class="txt"> First Name <span class="mand">*</span> </td> <td> 
		 <s:textfield name="firstname" id="firstname" maxlength="32"  value="%{dbtcustregbean.firstname}" /> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="middlename" id="middlename" maxlength="32" value="%{dbtcustregbean.middlename}"  /> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="lastname" id="lastname" maxlength="32" value="%{dbtcustregbean.lastname}"  /> </td>
	</tr> 
	
	
	<tr>
		 
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob"   value="%{dbtcustregbean.dob}"  style="width:160px" onchange="validateDate(this.value)"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.regform1.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">	 
		 </td>
		 
		   <td class="txt"> Gender <s:property value="dbtcustregbean.f"/> <span class="mand">*</span> </td> 
		   <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="gender"  headerKey="-1" headerValue="-SELECT-" name="gender" value="%{dbtcustregbean.gender}" /> </td>
		  <td class="txt"> Marital Status <span class="mand">*</span> </td> 
		  <td> <s:select list="#{'M':'Married','U':'Un-Married','O':'Other'}" id="mstatus"   headerKey="-1" headerValue="-SELECT-" name="mstatus" value="%{dbtcustregbean.mstatus}"/> </td>
		  
	</tr> 
	
	
	
	<tr>
		 <td class="txt"> Nationality <span class="mand">*</span>  </td> 
		 <td> <s:textfield name="nationality" id="nationality" maxlength="32" value="%{dbtcustregbean.nationality}" /> </td>
		   <td class="txt"> Document Provided <span class="mand">*</span> </td> <td> 
		  	 		<select id="documentprovided" name="documentprovided" >
					<s:if test ="%{dbtcustregbean.documentlist.isEmpty()}">
					<option value="">No Document Type Found</option>
					</s:if>
					<s:else>
								<option value="" >Select Document</option>   
								<s:iterator value="dbtcustregbean.documentlist">
								<option value="<s:property value="DOC_ID"/>" 
								<s:if test="DOC_ID==dbtcustregbean.documentprovided" >
								selected
								</s:if>
								> 
								<s:property value="DOC_TYPE"/></option>
								</s:iterator>
					</s:else>
					</select> 
			<s:iterator value="dbtcustregbean.documentlist">   
					<!--<s:property value="BRANCH_CODE" /> == <s:property value="%{dbtcustregbean.branchcode}"/> <s:property value="BRANCH_NAME"/> <br>-->
					  
					 <s:if test="DOC_ID==dbtcustregbean.documentprovided" >
					 					 <s:property value="DOC_TYPE"/>   
					 </s:if>  
					</s:iterator>  
					 
		  	  
		  </td>
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> 
		   <td> <s:textfield name="dcumentnumber" id="dcumentnumber" maxlength="32"  value="%{dbtcustregbean.documentnumber}" /> </td>
	
	</tr> 
	<tr>
		<td class="txt"> Spouce Name   </td> <td> 
		<s:textfield name="spousename" id="spousename" maxlength="32" value="%{dbtcustregbean.spousename}"   /> </td>
		 <td class="txt"> Mother's Name <span class="mand">*</span> </td> 
		 <td> <s:textfield name="mothername" id="mothername" maxlength="32" value="%{dbtcustregbean.mothername}"  /> </td>
		  <td class="txt"> Father Name  </td> 
		  <td> <s:textfield name="fathername" id="fathername" maxlength="32"  value="%{dbtcustregbean.fathername}"  /> </td> 
	</tr> 
	
	 
	 
	
	</table>	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" id="button">
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return validatetab2_customer()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	 </s:form>
</sx:div>


<sx:div id="3"  name="tab" label="Contact Details" value="3"  >
 	<s:form action="contactDetailsAddDebitCustomerRegister.do"  id="regform" onsubmit="return valiteContactDetails()" name="regform" autocomplete = "off" namespace="/">
 	<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	
		<tr>   
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> 
			 <td> <s:textfield name="comobileno" id="comobileno" maxlength="16"   value="%{dbtcustregbean.mobile}"  /> </td>
			 <td class="txt"> E-Mail      </td> 
			 <td> <s:textfield name="secemail" id="secemail" maxlength="32"   value="%{dbtcustregbean.email}"   /> </td>
		 
		</tr> 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Permenent Address
			
		</th><tr> 
		<tr>
			 <td class="txt"> Po.Box  <span class="mand">*</span> </td> <td> 
			 <s:textfield name="p_poxbox" id="p_poxbox" maxlength="32" value="%{dbtcustregbean.p_poxbox}" /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> 
			  <td> <s:textfield name="p_houseno" id="p_houseno" maxlength="32" value="%{dbtcustregbean.p_houseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="p_streetname" id="p_streetname" maxlength="32" value="%{dbtcustregbean.p_streetname}" /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> 
			 <td> <s:textfield name="p_wardnumber" id="p_wardnumber" maxlength="32" value="%{dbtcustregbean.p_wardnumber}" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> 
			  <td> <s:textfield name="p_city" id="p_city" maxlength="32" value="%{dbtcustregbean.p_city}" /> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="p_district" id="p_district" maxlength="32" value="%{dbtcustregbean.p_district}" /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> 
			 <td> <s:textfield name="p_phone1" id="p_phone1" maxlength="16"  value="%{dbtcustregbean.p_phone1}" /> </td>
			 <td class="txt"> Phone2  </td> 
			 <td> <s:textfield name="p_phone2" id="p_phone2" maxlength="16"  value="%{dbtcustregbean.p_phone2}" /> </td>
			   
		</tr> 
		
		 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> 
		 Contact Address </th>
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> 
			 <td> <s:textfield name="c_poxbox" id="c_poxbox" maxlength="32" value="%{dbtcustregbean.c_poxbox}"   /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> 
			  <td> <s:textfield name="c_houseno" id="c_houseno" maxlength="32" value="%{dbtcustregbean.c_houseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="c_streetname" id="c_streetname" maxlength="32" value="%{dbtcustregbean.c_streetname}"  /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> 
			 <td> <s:textfield name="c_wardnumber" id="c_wardnumber" maxlength="32" value="%{dbtcustregbean.c_wardnumber}" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> 
			  <td> <s:textfield name="c_city" id="c_city" maxlength="32" value="%{dbtcustregbean.c_city}" /> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> 
			   <td> <s:textfield name="c_district" id="c_district" maxlength="32" value="%{dbtcustregbean.c_district}" /> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> 
			 <td> <s:textfield name="c_phone1" id="c_phone1" maxlength="16"  value="%{dbtcustregbean.c_phone1}"  /> </td>
			 <td class="txt"> Phone2  </td> 
			 <td> <s:textfield name="c_phone2" id="c_phone2" maxlength="16" value="%{dbtcustregbean.c_phone2}"  /> </td>
			   
		</tr> 
		
		 
		
		 
		
	</table>	 
	
	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return tab3_validation()" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	
</s:form>
</sx:div>

<sx:div id="4"  name="tab" label="Registration" value="4" disabled="%{dbtcustregbean.tab4Status}" >
<s:form action="authorizecardOrderDebitCustomerRegister.do"  id="regform" onsubmit="return AuthorizesubmitOrder()" name="regform" autocomplete = "off" namespace="/">
<%String act = request.getParameter("act")==null?"":request.getParameter("act"); %>
 <input type="hidden" name = "act" value="<%=act%>" />
<input type="hidden" value="<s:property value="%{dbtcustregbean.applicationid}"/>" id="tab4_applicationid" name="tab4_applicationid" /> 

<br><br>	
	<table border="0" cellpadding="10" cellspacing="0" width="60%" class="formtable">	 
<tr>
	<td>  <input type="checkbox" id = "ensure" name="ensure" > </td>
	<td  class="tbheader" style="text-align:left">  Authorize Order....  </td>
</tr>
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="SUBMIT"   name="fourthtab" id="fourthtab"  />
		</td>
		
		</tr>
</table>
	</s:form>

</sx:div>


  

<!-- --------------------- SUPPLIMENTARY DETAILS----------- -->

<%-- 
<sx:div id="4" name="tab"  label="Supplimentary Card Details" value="6"   disabled="true" >
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="supplimentform" onsubmit="return validateSuplimentDet()" name="supplimentform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Supplimentary Details
		<input type="hidden" id="reqfrom" name="reqfrom" value="6" /> 
		<input type="hidden" id="gotoval" name="gotoval" value="6" />
		<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{dbtcustregbean.customerid}"/>
	</th><tr> 
	<tr>
		 <td class="txt"> First Name <span class="mand">*</span> </td> <td> <s:textfield name="supfirstname" id="supfirstname" maxlength="32"  /> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="supmidname" id="supmidname" maxlength="32"/> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="suplastname" id="suplastname" maxlength="32"/> </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		 	<s:textfield name="supdob" id="supdob" value="%{custregbean.supdob}" readonly="true"   style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.supplimentform.supdob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		  <td class="txt"> Nationality <span class="mand">*</span>  </td> <td> <s:textfield name="supnationality" id="supnationality" maxlength="32"/> </td>
		   <td class="txt"> Name On Card <span class="mand">*</span> </td> <td> <s:textfield name="supnameoncard" id="supnameoncard" maxlength="32"/> </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Document Provided <span class="mand">*</span> </td> <td>
		 <s:select list="%{dbtcustregbean.documentlist}"  id="idproof"  name="idproof"  listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" />  
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  /> </td>
	</tr>  
	
	

	<tr>
		 <td class="txt"> Gender <span class="mand">*</span> </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="supgender" onchange = "getSupRelation(this.value)"  headerKey="-1" headerValue="-SELECT-" name="supgender" value="%{custregbean.gender}"/> </td>
		  <td class="txt"> Relationship <span class="mand">*</span> </td> <td> <s:select  list="%{dbtcustregbean.supplimentrelationlist}" id="suprelationship" listKey="RELATIONCODE" listValue="RELATIONDESC" name="suprelationship"  headerKey="-1" headerValue="-SELECT-" /> </td>
		   
	</tr> 
	 
	 <tr >
	 <td colspan="6" style="text-align:center">
	 		<s:submit value="Add"   name="suplimentadd" id="sixtab" onclick="return customerinfo_checker()"/>
	 </td>
	 </tr>
	 
	
	 <tr>
	 	<td colspan="6">
	 		<table border='0' cellpadding='0' cellspacing='0' width='100%'>
	 		 <s:if test="dbtcustregbean.hassupliment"> 
	 			<tr><th>First Name</th><th>Middle Name</th><th>Last Name</th><th>Name on Name</th><th> Gender </th><th>DOB</th> 
	 			<th>Nationality</th> <th>Citizenship / Passport </th>  <th>Document Number </th>  <th> Relationship </th>   <th> Delete </th> </tr>
			 </s:if>		
	 		<s:iterator value="dbtcustregbean.supplimentlist">
	 			<tr><td>${FIRSTNAME}</td><td>${MIDDLENAME}</td><td>${LASTNAME}</td><td>${ENCNAME}</td><td> ${GENDER} </td><td>${DOB}</td> 
	 			<td>${NATIONALITY}</td> <td>${IDPROOF} </td>  <td>${IDPROOFNO} </td>  <td> ${IDPROOF} </td> 
	 			<td>  <img src="images/delete.png" onclick="deleteSuppliment( '${CUSTOMERID}' )" />   </td> </tr>
	 			
	 		</s:iterator>
	 		</table>	 		
	 	</td>
	 </tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr><td>&nbsp;</td></tr>
		<tr>
		<td>
			<input type="button"   value="Complete"     name="sixtab" id="sixtab" onclick="return confirmComplete()"/>
		</td>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div>
 --%>

</sx:tabbedpanel>

 
	
	
 
 
</body>