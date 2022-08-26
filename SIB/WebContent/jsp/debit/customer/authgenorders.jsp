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

function AuthorizesubmitOrder()
{
	var ensure = document.getElementById("ensure");
	//alert("ensure::::"+ensure);
	if(ensure.checked==false)
		{
		errMessage(ensure, "Ensure All Data are Verified !");return false; 
		}
}
function DeAuthorizeOrder(){
	
	var ensure = document.getElementById("ensure");
	//alert("ensure::::"+ensure);
	if(ensure.checked==false)
		{
		errMessage(ensure, "Ensure All Data are Verified !");return false; 
		}
	
	var check = confirm("Are you sure, You want to delete the application");
	if(check){
		document.regform.action = "deleteApplicationDebitCustomerRegister.do";
		document.regform.submit;
	}else{
		return false;
	}
}
function getSubProd(selprodid ){   
	var url = "getSubProductListByProductDebitCustomerRegister.do?prodid="+selprodid+"&status=1";   
	//alert(url);
	result = AjaxReturnValue(url);   
	//alert(result);
	document.getElementById("subproduct").innerHTML = result;  
	
	//var hidsubproduct = document.getElementById("hidsubproduct").value; 
	//document.getElementById("subproduct").value=hidsubproduct;
	//getLimitBySubProduct(hidsubproduct);
	//getFeeBySubProduct(hidsubproduct);
	   
} 
function getSubmit(){
	document.getElementById("product_tab").style.display = "block";
}
function getLimitBySubProduct( subproductid ){
	// alert('getLimitBySubProduct'+subproductid);
	var  productid = document.getElementById("productcode");
	if( productcode.value == "-1"){
		errMessage(productcode, "Select Product code");
		return false;
	} 
	var url = "getLimitBySubProductDebitCustomerRegister.do?productid="+productid.value+"&subproductid="+subproductid;
	 
	var result = AjaxReturnValue(url);
	document.getElementById("limitid").innerHTML=result;
	
	
	
	
}
function getFeeBySubProduct( subproductid ){
	//alert('getFeeBySubProduct'+subproductid);
	var  productid = document.getElementById("productcode");
	if( productcode.value == "-1"){
		errMessage(productcode, "Select Product code");
		return false;
	} 
	var url = "getFeeBySubProductDebitCustomerRegister.do?PRODUCT="+productid.value+"&SUBPRODUCT="+subproductid;
	 
	var result = AjaxReturnValue(url);
	document.getElementById("feecode").innerHTML=result;
	
}
</script>


<title>Customer Registration</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<body ><!-- selectTab(); -->

<sx:div id="default"  name="defaultValue"  value="default"   >
<s:if test ="%{dbtcustregbean.applicationid!=null}">
<!-- ORDER NO : <s:property value="%{dbtcustregbean.applicationid}" /> -->
</s:if>  
</sx:div>


<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 100%; margin:0 auto; height: 700px;" doLayout="true" 

selectedTab="%{dbtcustregbean.reglevel}" 

 >
 <s:form action="authorizecardOrderDebitCustomerRegister.do"  id="regform" name="regform" autocomplete = "off" namespace="/" onsubmit="return tab4_validation()">
<sx:div id="1"  name="tab" label="Product Information" value="1"   >
	 
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
			 </tr>
			 <tr>
			 <td class="txt"> Encoding Name  </td> <td> 
			 <s:property 
			 value="%{dbtcustregbean.encname}" 
			 />
		 </td>  
		<s:if test ="%{dbtcustregbean.cinidbasedon=='CBS'}">
			  <td class="txt"> CustomerID  </td> <td> 
			 <s:property value="%{dbtcustregbean.customeridno}" />
			 <s:hidden name="customerid" id="customerid" value="%{dbtcustregbean.customeridno}"/>
			 </td>  
			 </s:if>
		 </tr>
		 <tr>
		 
			  <td class="txt"> Product   </td>   
			  <td> 
						<select id="productcode"  name="productcode"  onchange="getSubProd(this.value);"  >
						 <s:iterator value="dbtcustregbean.productlist">   
					 				<option value="<s:property value="PRODUCT_CODE"/>"
					 				<s:if test="PRODUCT_CODE==dbtcustregbean.productcode" >
									selected
									</s:if>
					 				
					 				><s:property value="CARD_TYPE_NAME"/></option>
					</s:iterator> 
					</select>
			 </td>
		 
			   <td class="txt"> Sub Product     </td>
			    <td>
			    <select  id="subproduct"  name="subproduct" onchange="getLimitBySubProduct(this.value);getFeeBySubProduct(this.value);" >
			   <s:iterator value="dbtcustregbean.subproductlist">   
					 <s:if test="SUB_PROD_ID==dbtcustregbean.subproduct" >
					 					<option value="<s:property value="SUB_PROD_ID"/>"><s:property value="SUB_PRODUCT_NAME"/></option>  
					 </s:if>  
					 <s:else>
					 	<option value="<s:property value="SUB_PROD_ID"/>"><s:property value="SUB_PRODUCT_NAME"/></option>
					 </s:else>
					</s:iterator>  
					</select>
				</td>
			   	
			
			 
			  </tr> 
		 <tr>
			  
			   <td class="txt"> Limit  </td> 
			  
			  <td> 
			  <select name="limitid" id="limitid">
			  <option value = "<s:property value="%{dbtcustregbean.limitid}" />"><s:property value="%{dbtcustregbean.limitname}" /></option>
			    </select> 
			  </td>   
			   <td class="txt" > Fee  </td> 
			   <td > 
			   <select name="feecode" id="feecode" >
			   <option value=" <s:property value="%{dbtcustregbean.feecode}" />"> <s:property value="%{dbtcustregbean.feename}" /></option>
			   </select> 
			   </td>
			   
		</tr> 
		<tr>
		<td class="txt">Renewal</td>
		 <td><s:property value="%{dbtcustregbean.renewal}" /></td>
		 <td class="txt">Card Collection Branch</td>
		 <td><s:property value="%{dbtcustregbean.collectbranch}" /></td>
		</tr>
		<tr>
		
		<th colspan="6" style="text-align:left" class="tbheader"> Account Information
		 </th>
		 <tr>
		 <td class="txt">Account Type</td>
		  <td class="txt">Account Description</td>
		  <td class="txt">Currency</td>
		  <td class="txt">Primary Account</td>
		  </tr>
		 	<tr>
		 		
		 		<td class="txt">
		 		<!-- accttypelist -->
								<s:iterator value="dbtcustregbean.accttypelist">
								<s:if test="ACCTTYPEID==dbtcustregbean.accounttypevalue" >
								<input type="text" value="<s:property value="ACCTTYPEDESC"/>" readonly/>
								</s:if>
								</s:iterator>
		 		</td>
		 		<td class="txt">
		 		<!-- acctsubtypelist -->
		 		<s:iterator value="dbtcustregbean.accuntsubtypelist">
		 		<s:if test="ACCTSUBTYPEID==dbtcustregbean.accountsubtypevalue" >
								<input type="text" value="<s:property value="ACCTSUBTYPEDESC"/>" readonly/>
				</s:if>
		 		</s:iterator>
		 		</td>
		 		<td class="txt">
		 		<s:iterator value="dbtcustregbean.currencylist">  
								<s:if test="NUMERIC_CODE==dbtcustregbean.tab2_currency" >
								<input type="text" value="<s:property value="CURRENCY_DESC"/>" readonly/>
								</s:if>
								</s:iterator>
		 		</td>
		 		<td class="txt">  
		 		<s:textfield  id="accountnovalue" name="accountnovalue" maxlength="%{dbtcustregbean.acctnolen}" readonly="true" /> 
		 		</td> 
 			</tr>
 			
 			</table>
 	<%-- <div id="product_tab" style="display: none;">		
 		<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" onclick="return validatetab1()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>   
	</table> 	
			
 </div> --%>
</sx:div>
  
 

  
<sx:div id="2" name="tab"  label="Personal Details" value="2"   >
 		<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/> 
 		
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Personal Information

	</th><tr> 
	<tr>
		 <td class="txt"> First Name  </td> <td> 
		 <s:textfield name="firstname" id="firstname" maxlength="32"  value="%{dbtcustregbean.firstname}" readonly="true"/> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="middlename" id="middlename" maxlength="32" value="%{dbtcustregbean.middlename}"  readonly="true"/> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="lastname" id="lastname" maxlength="32" value="%{dbtcustregbean.lastname}"  readonly="true"/> </td>
	</tr> 
	
	
	<tr>
		 
		 <td class="txt"> Date Of Birth  </td> 
		 <td> 
		 	<s:textfield name="dob" id="dob" readonly="true"  value="%{dbtcustregbean.dob}"  style="width:160px"/>
				 
		 </td>
		 
		   <td class="txt"> Gender   </td> 
		   <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="gender"  headerKey="-1" headerValue="-SELECT-" name="gender" value="%{dbtcustregbean.gender}" disabled="true"/> </td>
		  <td class="txt"> Marital Status  </td> 
		  <td> <s:select list="#{'M':'Married','U':'Un-Married','O':'Other'}" id="mstatus"  disabled="true" headerKey="-1" headerValue="-SELECT-" name="mstatus" value="%{dbtcustregbean.mstatus}"/> </td>
		  
	</tr> 
	
	
	
	<tr>
		 <td class="txt"> Nationality   </td> 
		 <td> <s:textfield name="nationality" id="nationality" maxlength="32" value="%{dbtcustregbean.nationality}" readonly="true"/> </td>
		   <td class="txt"> Document Provided  </td> <td> 
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
					 
		  	  
		  </td>
		 
		   <td class="txt"> Document Number    </td> 
		   <td> <s:textfield name="dcumentnumber" id="dcumentnumber" maxlength="32"  value="%{dbtcustregbean.documentnumber}" readonly="true"/> </td>
	
	</tr> 
	<tr>
		<td class="txt"> Spouce Name   </td> <td> 
		<s:textfield name="spousename" id="spousename" maxlength="32" value="%{dbtcustregbean.spousename}"  readonly="true" /> </td>
		 <td class="txt"> Mother's Name  </td> 
		 <td> <s:textfield name="mothername" id="mothername" maxlength="32" value="%{dbtcustregbean.mothername}" readonly="true" /> </td>
		  <td class="txt"> Father Name  </td> 
		  <td> <s:textfield name="fathername" id="fathername" maxlength="32"  value="%{dbtcustregbean.fathername}" readonly="true" /> </td> 
	</tr> 
	
	 
	 
	
	</table>	
	
</sx:div>


<sx:div id="3"  name="tab" label="Contact Details" value="3"  >
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	
		<tr>   
			 <td class="txt"> Mobile     </td> 
			 <td> <s:textfield name="comobileno" id="comobileno" maxlength="16"   value="%{dbtcustregbean.mobile}" readonly="true" /> </td>
			 <td class="txt"> E-Mail      </td> 
			 <td> <s:textfield name="secemail" id="secemail" maxlength="32"   value="%{dbtcustregbean.email}"  readonly="true" /> </td>
		 
		</tr> 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Permenent Address
			
		</th><tr> 
		<tr>
			 <td class="txt"> Po.Box   </td> <td> 
			 <s:textfield name="p_poxbox" id="p_poxbox" maxlength="32" value="%{dbtcustregbean.p_poxbox}" readonly="true"/> </td>
			  <td class="txt"> House No.     </td> 
			  <td> <s:textfield name="p_houseno" id="p_houseno" maxlength="32" value="%{dbtcustregbean.p_houseno}"  readonly="true"/> </td>
			   <td class="txt"> Street Name    </td> 
			   <td> <s:textfield name="p_streetname" id="p_streetname" maxlength="32" value="%{dbtcustregbean.p_streetname}" readonly="true"/> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> 
			 <td> <s:textfield name="p_wardnumber" id="p_wardnumber" maxlength="32" value="%{dbtcustregbean.p_wardnumber}" readonly="true"/> </td>
			  <td class="txt"> City    </td> 
			  <td> <s:textfield name="p_city" id="p_city" maxlength="32" value="%{dbtcustregbean.p_city}" readonly="true"/> </td>
			   <td class="txt"> District   </td> 
			   <td> <s:textfield name="p_district" id="p_district" maxlength="32" value="%{dbtcustregbean.p_district}" readonly="true"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> 
			 <td> <s:textfield name="p_phone1" id="p_phone1" maxlength="16"  value="%{dbtcustregbean.p_phone1}" readonly="true"/> </td>
			 <td class="txt"> Phone2  </td> 
			 <td> <s:textfield name="p_phone2" id="p_phone2" maxlength="16"  value="%{dbtcustregbean.p_phone2}" readonly="true"/> </td>
			   
		</tr> 
		
		 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> 
		 Contact Address </th>
		<tr>
			 <td class="txt"> Po.Box  </td> 
			 <td> <s:textfield name="c_poxbox" id="c_poxbox" maxlength="32" value="%{dbtcustregbean.c_poxbox}"  readonly="true" /> </td>
			  <td class="txt"> House No.     </td> 
			  <td> <s:textfield name="c_houseno" id="c_houseno" maxlength="32" value="%{dbtcustregbean.c_houseno}"  readonly="true"/> </td>
			   <td class="txt"> Street Name    </td> 
			   <td> <s:textfield name="c_streetname" id="c_streetname" maxlength="32" value="%{dbtcustregbean.c_streetname}" readonly="true" /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> 
			 <td> <s:textfield name="c_wardnumber" id="c_wardnumber" maxlength="32" value="%{dbtcustregbean.c_wardnumber}" readonly="true"/> </td>
			  <td class="txt"> City    </td> 
			  <td> <s:textfield name="c_city" id="c_city" maxlength="32" value="%{dbtcustregbean.c_city}" readonly="true"/> </td>
			   <td class="txt"> District   </td> 
			   <td> <s:textfield name="c_district" id="c_district" maxlength="32" value="%{dbtcustregbean.c_district}" readonly="true"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> 
			 <td> <s:textfield name="c_phone1" id="c_phone1" maxlength="16"  value="%{dbtcustregbean.c_phone1}" readonly="true" /> </td>
			 <td class="txt"> Phone2  </td> 
			 <td> <s:textfield name="c_phone2" id="c_phone2" maxlength="16" value="%{dbtcustregbean.c_phone2}" readonly="true" /> </td>
			   
		</tr> 
		
		 
		
		 
		
	</table>	 
	
</sx:div>

<sx:div id="4"  name="tab" label="Registration" value="4"  >
<%String act = request.getParameter("act")==null?"":request.getParameter("act"); %>
 <input type="hidden" name = "act" value="<%=act%>" />
<input type="hidden" value="<s:property value="%{dbtcustregbean.applicationid}"/>" id="tab4_applicationid" name="tab4_applicationid" /> 

<br><br>	
	<table border="0" cellpadding="10" cellspacing="0" width="60%" class="formtable">	 
<tr>
	<td>  <input type="checkbox" id = "ensure" name="ensure" > </td>
	<td  class="tbheader" style="text-align:left">  Ensure All Data are Verified !  </td>
</tr>
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="SUBMIT"   name="fourthtab" id="fourthtab" onclick="return AuthorizesubmitOrder();" />
		</td>
		<td>
			<s:submit style="color : red;" value="De - Authorize"  onclick="return DeAuthorizeOrder();" />
		</td>
		</tr>
</table>
	

</sx:div>

</s:form>
  

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
		 <td class="txt"> First Name  </td> <td> <s:textfield name="supfirstname" id="supfirstname" maxlength="32"  /> </td>
		  <td class="txt"> Middle Name   </td> <td> <s:textfield name="supmidname" id="supmidname" maxlength="32"/> </td>
		   <td class="txt"> Last Name </td> <td> <s:textfield name="suplastname" id="suplastname" maxlength="32"/> </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Date Of Birth  </td> 
		 <td> 
		 	<s:textfield name="supdob" id="supdob" value="%{custregbean.supdob}" readonly="true"   style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.supplimentform.supdob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
		 </td>
		  <td class="txt"> Nationality   </td> <td> <s:textfield name="supnationality" id="supnationality" maxlength="32"/> </td>
		   <td class="txt"> Name On Card  </td> <td> <s:textfield name="supnameoncard" id="supnameoncard" maxlength="32"/> </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Document Provided  </td> <td>
		 <s:select list="%{dbtcustregbean.documentlist}"  id="idproof"  name="idproof"  listKey="DOC_ID" listValue="DOC_TYPE" headerKey="-1" headerValue="-SELECT-" />  
		 
		   <td class="txt"> Document Number    </td> <td> <s:textfield name="idproofno" id="idproofno" maxlength="32"  /> </td>
	</tr>  
	
	

	<tr>
		 <td class="txt"> Gender  </td> <td> <s:select list="#{'M':'Male','F':'Female','O':'Other'}" id="supgender" onchange = "getSupRelation(this.value)"  headerKey="-1" headerValue="-SELECT-" name="supgender" value="%{custregbean.gender}"/> </td>
		  <td class="txt"> Relationship  </td> <td> <s:select  list="%{dbtcustregbean.supplimentrelationlist}" id="suprelationship" listKey="RELATIONCODE" listValue="RELATIONDESC" name="suprelationship"  headerKey="-1" headerValue="-SELECT-" /> </td>
		   
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