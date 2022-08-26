<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
 .tbheader{
 	font-weight:bold;
 }
 .txt{
 	font-weight:bold;
 }
 </style>
 <script>
 	function getDeauthReason(){
 		var reason = prompt("Enter the reason for Reject");
 		if( reason == null ){
 			return false;
 		}
 		
 		document.getElementById("reason").value = reason;
 		return true;
 	}
 	
 	function selectTab(){
 		 
		var jscurrenttab  = document.getElementById("jscurrenttab").value;   
		document.getElementById( jscurrenttab ).removeAttribute('disabled'); 
		
	 	 var reglevel =document.getElementById("reglevel").value;  
		 document.getElementById( jscurrenttab ).removeAttribute('disabled');
	  	 
		 for ( var i=reglevel; 1 <= i+1 ; i-- ){ 
			var prevtab = document.getElementById(i);
			prevtab.removeAttribute('disabled');
		 }   
	}
 	
 </script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 
 
<title>View</title>
<sx:head/>
</head>

<jsp:include page="/displayresult.jsp"></jsp:include>
<body onload="selectTab()">
<s:hidden name="reglevel" id="reglevel" value="%{creditbean.reglevel}"/>
<s:hidden name="jscurrenttab" id="jscurrenttab" value="%{creditbean.gototab}"/> 

<s:property value="creditbean.valid"/>
<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 100%; margin:0 auto; height: 500px;" doLayout="true" selectedTab="%{creditbean.gototab}"  >

<sx:div id="1" name="tab"  label="Personal Details" value="1"  disabled="true" >
 	<s:form action="customerDetailsViewActCreditCustRegisteration.do"  id="regform" onsubmit="return validateForm()" name="regform1" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Personal Information
		<input type="hidden" id="reqfrom" name="reqfrom" value="1" />     
		<input type="hidden" id="goto" name="goto" value="2" />
		<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
		<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		<s:hidden name="addnew" id="addnew" value="%{creditbean.addnew}"/>
	</th><tr> 
	<tr>
		 <td class="txt"> First Name <span class="mand">*</span> </td> <td> <s:property value="creditbean.firstname"/>   </td>
		  <td class="txt"> Middle Name   </td> <td><s:property value="creditbean.middlename"/>    </td>
		   <td class="txt"> Last Name </td> <td> <s:property value="creditbean.lastname"/>  </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Date Of Birth <span class="mand">*</span> </td> 
		 <td> 
		  <s:property value="creditbean.dob"/>  
		   
		 </td>
		  <td class="txt"> Nationality <span class="mand">*</span>  </td> <td> <s:property value="creditbean.nationality"/>  </td>
		   <td class="txt"> Name On Card <span class="mand">*</span> </td> <td>  <s:property value="creditbean.embname"/> </td>
	</tr> 
	
	
	
	<tr>
		 <td class="txt"> Gender <s:property value="creditbean.f"/> <span class="mand">*</span> </td> <td>  <s:property value="creditbean.gender"/>    </td>
		  <td class="txt"> Marital Status <span class="mand">*</span> </td> <td> <s:property value="creditbean.mstatus"/>     </td>
		   
	</tr> 
	
	<tr>
		 <td class="txt"> Residence <span class="mand">*</span> </td> <td> <s:property value="creditbean.residence"/>          </td>
		  <td class="txt"> Own Vehicle <span class="mand">*</span> </td> <td> <s:property value="creditbean.vehicle"/>     </td>
		   <td class="txt"> Vehicle Number   </td> <td> <s:property value="creditbean.vehicleno"/>    </td>
	</tr> 
	
	<tr>
		 <td class="txt"> Citizenship / Passport <span class="mand">*</span> </td> <td>   <s:property value="creditbean.idproof"/> 
		   
		  </td>
		 
		   <td class="txt"> Document Number  <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.idproofno"/>      </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Date of Issue <span class="mand">*</span> </td> 
		  <td> 
		  	  <s:property value="creditbean.dateofissue"/>  
		   
			 </td>
		 
		   <td class="txt"> District of Issue  <span class="mand">*</span>  </td> <td>   <s:property value="creditbean.districofissue"/>      </td>
	</tr>  
	
	<tr>
		 <td class="txt"> Type of Card <span class="mand">*</span> </td> <td> <s:property value="creditbean.typeofcard"/>     </td>
		 
		 <td class="txt"> Statement Delivery <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.stmtdelivery"/>      </td>
		 
		 <td class="txt"> Sms Alert Required <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.smsalert"/>      </td>
	</tr>  
	
	
	 <tr><th colspan="6" style="text-align:left" class="tbheader"> Family Details </th><tr> 
	
	<tr>
		<td class="txt"> Spouce Name   </td> <td>  <s:property value="creditbean.spousename"/>      </td>
		 <td class="txt"> Mother's Name <span class="mand">*</span> </td> <td>  <s:property value="creditbean.mothername"/>     </td>
		  <td class="txt"> Father Name  </td> <td>  <s:property value="creditbean.fathername"/>      </td> 
	</tr> 
	
	<tr> 
		   <td class="txt"> Grand Father Name </td> <td>  <s:property value="creditbean.grandfathername"/>   </td>
	</tr> 
	
	
	</table>	
	
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table>

	</s:form>
</sx:div>

 

<sx:div id="2"  name="tab" label="Contact Details" value="2" disabled="true"  >
 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="regform" onsubmit="return valiteContactDetails()" name="regform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Permenent Address
			<input type="hidden" id="reqfrom" name="reqfrom" value="2" /> 
			<input type="hidden" id="goto" name="goto" value="3" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Po.Box <s:property value="creditbean.prphone1"/> <span class="mand">*</span> </td> <td>   <s:property value="creditbean.prpobox"/>     </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td>   <s:property value="creditbean.prhouseno"/>       </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td>   <s:property value="creditbean.prstreetname"/>        </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:property value="creditbean.prwardname"/>   </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:property value="creditbean.prcity"/>  </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.prdistrict"/>     </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td>   <s:property value="creditbean.prphone1"/>      </td>
			 <td class="txt"> Phone2  </td> <td>  <s:property value="creditbean.prphone2"/>       </td>
			   <td class="txt"> Fax  </td> <td> <s:property value="creditbean.prfax"/>       </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> <td> <s:property value="creditbean.prmobileno"/>  </td>
			 <td class="txt"> E-Mail  <span class="mand">*</span>   </td> <td> <s:property value="creditbean.primaryemail"/>   </td>
			 
		</tr> 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader">   Contact Address </th>
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> <td>  <s:property value="creditbean.copobox"/>    </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td> <s:property value="creditbean.cohouseno"/>      </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.costreetname"/>          </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td>  <s:property value="creditbean.cowardname"/>         </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.cocity"/>          </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.codistrict"/>        </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td>   <s:property value="creditbean.cophone1"/>   </td>
			 <td class="txt"> Phone2  </td> <td>  <s:property value="creditbean.cophone2"/>     </td>
			   <td class="txt"> Fax  </td> <td>  <s:property value="creditbean.cofax"/>       </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> <td>    <s:property value="creditbean.comobileno"/>      </td>
			 <td class="txt"> E-Mail      </td> <td>  <s:property value="creditbean.secemail"/>         </td>
		 
		</tr> 
		
		 
		
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div>

<sx:div id="3"  name="tab" label="Occupation Details" value="3"  disabled="true" >
	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="regform" onsubmit="return validateBusiness()" name="regform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px">	 
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Occupation Details
			<input type="hidden" id="reqfrom" name="reqfrom" value="3" /> 
			<input type="hidden" id="goto" name="goto" value="4" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Occupation <span class="mand">*</span> </td> 
			 <td> 
			 	<td>  <s:property value="creditbean.occupation"/>    
			 </td>
			  <td class="txt"> Work For <span class="mand">*</span> </td> 
			 <td> 
			 	<td> <s:property value="creditbean.workfor"/>       
			 </td> 
			 
		</tr>
		<tr id="trcomp" style="display:none">
			 <td class="txt"> Company Name  <span class="mand">*</span>   </td> <td>  <s:property value="creditbean.companyname"/> </td>
			 
		</tr>
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Name of the Employer/Company/Firm</th></tr> 
		<tr>
			 <td class="txt"> Designation  <span class="mand">*</span>   </td> <td>  <s:property value="creditbean.designation"/>     </td>
			 <td class="txt"> Nature of Business      </td> <td>  <s:property value="creditbean.natofbusiness"/>  </td>
		 	 <td class="txt"> Employed/Business Since     </td> <td> <s:property value="creditbean.employedsince"/>   </td>
		</tr> 
		
		 <tr><th colspan="6" style="text-align:left" class="tbheader"> Employer / Business Address </th></tr> 
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> <td>  <s:property value="creditbean.emppobox"/>  </td>
			  <td class="txt"> Office/Business/Firm No.  <span class="mand">*</span>   </td> <td>   <s:property value="creditbean.emphouseno"/> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.empstreetname"/> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td>  <s:property value="creditbean.empwardname"/></td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td>  <s:property value="creditbean.empcity"/> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td>   <s:property value="creditbean.empdistrict"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1 <span class="mand">*</span>  </td> <td>   <s:property value="creditbean.empphone1"/> </td>
			 <td class="txt"> Phone2  </td> <td>  <s:property value="creditbean.empphone2"/>  </td>
			   <td class="txt"> Fax  </td> <td>   <s:property value="creditbean.empfax"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Mobile  </td> <td> <s:property value="creditbean.empmobileno"/>   </td>
			 <td class="txt"> E-Mail  <span class="mand">*</span>   </td> <td> <s:property value="creditbean.empemail"/>      </td>
			 
		</tr> 
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div> 

<!-- ---------------------- -->

<sx:div id="4"  name="tab" label="Document Details" value="4" disabled="true"> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px">	 
	<tr><th colspan="4" style="text-align:left" class="tbheader"> Reference
			<input type="hidden" id="reqfrom" name="reqfrom" value="4" /> 
			<input type="hidden" id="goto" name="goto" value="5" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th>
	</tr> 
		<s:form action="referenceAddCreditCustRegisteration.do" onsubmit="return validateReference()" id="refform"  name="refform" autocomplete = "off" namespace="/">
			<s:hidden name="applicationid" id="applicationid2" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid2" value="%{creditbean.customerid}"/>
		<tr>
			 
			<td valign="top">
				<table border="1" cellpadding="0" cellspacing="0" width="50%"  style="border:none;padding-top:10px">
					<input type="hidden"  name="refmastercount" id="refmastercount" value='2' />
						<input type="hidden"  name="refcount" id="refcount" value='<s:property value="creditbean.reflist.size()"/>' />  
					<tr>
						<s:if test="creditbean.hasreference">
						  <th> Reference  </th> <th> Phone</th>  
						</s:if>
						<s:iterator value="creditbean.reflist">
						
							<tr><td> ${REFERNCENAME}  </td> <td> ${REFERENCEPHONE} </td>   </td>  </tr> 
						</s:iterator>
					</tr>
				</table>
			</td>
		</tr>  
	</s:form>
	
	
		<tr><th colspan="4" style="text-align:left" class="tbheader"> Documentation </th> 
		<s:form action="documentAddCreditCustRegisteration.do"  id="doc" onsubmit="return validateDocType()" name="doc" autocomplete = "off" namespace="/">
			<s:hidden name="applicationid" id="applicationid2" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid2" value="%{creditbean.customerid}"/>
		<tr>
			 
			<td valign="top">
				<table border="1" cellpadding="0" cellspacing="0" width="50%"  style="border:none;padding-top:10px">	 
					<tr>
						<input type="hidden"  name="documentmastercount" id="documentmastercount" value='2' />
						<input type="hidden"  name="documentcount" id="documentcount" value='<s:property value="creditbean.configdocumentlist.size()"/>' /> 
						<s:if test="creditbean.hasdocument">
						  <th> Document Name  </th> <th> Document Number </th> 
						</s:if>
						<s:iterator value="creditbean.configdocumentlist">
							 
							<tr><td> ${DOCUMENTTYPE}  </td> <td> ${DOCUMENTNUMBER} </td>  </tr> 
						</s:iterator>
					</tr>
				</table>
			</td>
		</tr>
		</s:form>
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" style="padding-top:20px" >
		<tr>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table>  
	
</sx:div> 


<!-- ------------------------------ -->

<sx:div id="5"  name="tab" label="Income Details" value="5"   disabled="true">
	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="incomeform" onsubmit="return validateIncomeDetails()" name="incomeform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px">	 
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Income Details
			<input type="hidden" id="reqfrom" name="reqfrom" value="5" /> 
			<input type="hidden" id="goto" name="goto" value="6" />
			<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Annual Salary <span class="mand">*</span> </td> 
			  <td> <s:property value="creditbean.annualsal"/>   </td>
			  
			 <td class="txt"> Annual Bonus/Incentive   </td> 
			  <td>  <s:property value="creditbean.annualbonus"/>      </td>
			 
			 <td class="txt"> Annual Business Income  </td> 
			  <td>  <s:property value="creditbean.annualbusinessincome"/>      </td> 
		</tr>
		
		<tr>
			 <td class="txt"> Rental Income </td> 
			  <td> <s:property value="creditbean.rentalincome"/>   </td>
			  
			 <td class="txt"> Agriculture Income  </td> 
			  <td>  <s:property value="creditbean.agriculture"/>   </td>
			 
			 <td class="txt"> Other Income  </td> 
			 <td> <s:property value="creditbean.otherincome"/>       </td> 
		</tr>
		<tr>
			 <td class="txt"> Total Annual Income </td> 
			  <td> <s:property value="creditbean.totannualincome"/>    </td>
		</tr>
		
		<tr>
			 <td> Existing Credit Card of Other Bank 
			 	     
			 	 
			</td>  
			<td><s:property value="creditbean.existcreditcard"/></td>
			 	
		</tr>
		
		<tr id="trcreditbankdet">  <!-- style="display:none"  -->
			 <td class="txt"> Name of the Bank  <span class="mand">*</span>   </td> <td> <s:property value="creditbean.excrcardbank"/>  </td>
			  <td class="txt"> Name of the Branch  <span class="mand">*</span>   </td> <td>  <s:property value="creditbean.excrcardbranch"/>     </td>
			  <td class="txt"> Account Type  <span class="mand">*</span>   </td> 
			  <td> <s:property value="creditbean.excrcardacctype"/>     </td>
		</tr>
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Standing Instruction </th></tr> 
		<tr>
			 <td  > Account With <%=session.getAttribute("Instname")%>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			 <td>
			 	 <s:property value="creditbean.acctwithprimarybank"/>     	
			 
			  </td> 
			 	 
			 <td class="txt"> Valid From  <span class="mand">*</span> </td> 
				 <td> 
				 	<s:property value="creditbean.validfrom"/>     	
		 			</td>
			   
			 <td class="txt"> Valid To     <span class="mand">*</span>  </td> 
		 	<td> 
		 		<s:property value="creditbean.validto"/>     	
	 		</td>
	 
		</tr> 
		
		<tr>
		 	<td class="txt"> Branch  <span class="mand">*</span> </td> <td> <s:property value="creditbean.primarybankbranch"/>    </td>
			 <td class="txt"> Debit Acct No.  <span class="mand">*</span> </td> <td> <s:property value="creditbean.primarybankacctno"/>      </td>
			 <td class="txt"> Currency   <span class="mand">*</span>  </td> <td> <s:property value="creditbean.primaryacctcur"/>        </td> 
		</tr> 
		
		<tr>
			 <td class="txt"> Pay Details   <span class="mand">*</span>  </td> <td><s:property value="creditbean.paytype"/>  </td>
			 <td class="txt"> Payable Amount  </td> <td> <s:property value="creditbean.payableamt"/>    </td>
			  <td class="txt"> Pay Day  <span class="mand">*</span>  </td> <td> <s:property value="creditbean.payableday"/>       </td>
		</tr> 
		
		 
	</table>	 
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		 
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
	</s:form>
</sx:div> 

<!-- --------------------- SUPPLIMENTARY DETAILS----------- -->


<sx:div id="6" name="tab"  label="Supplimentary Card Details" value="6"  disabled="true" >
 	<s:form action="authorizeCustomerCreditCustRegisteration.do"  id="supplimentform" onsubmit="return validateSuplimentDet()" name="supplimentform" autocomplete = "off" namespace="/">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	<tr><th colspan="6" style="text-align:left" class="tbheader"> Supplimentary Details
		<input type="hidden" id="reqfrom" name="reqfrom" value="6" /> 
		<input type="hidden" id="goto" name="goto" value="6" />
		<s:hidden name="applicationid" id="applicationid" value="%{creditbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{creditbean.customerid}"/>
			<s:hidden name="reason" id="reason" value=""/>
	</th> 
	  
	 
	
	 <tr>
	 	<td colspan="6">
	 		<table border='0' cellpadding='0' cellspacing='0' width='100%'>
	 		 <s:if test="creditbean.hassupliment"> 
	 			<tr><th>First Name</th><th>Middle Name</th><th>Last Name</th><th>Name on Name</th><th> Gender </th><th>DOB</th> 
	 			<th>Nationality</th> <th>Citizenship / Passport </th>  <th>Document Number </th>  <th> Relationship </th>    </tr>
			 </s:if>	
			 <s:else>
			 	<tr><th colspan="10"> NO SUPPLIMENTARY CARD DETAILS FOUND </th></tr>
			 </s:else>	
	 		<s:iterator value="creditbean.supplimentlist">
	 			<tr><td>${FIRSTNAME}</td><td>${MIDDLENAME}</td><td>${LASTNAME}</td><td>${ENCNAME}</td><td> ${GENDER} </td><td>${DOB}</td> 
	 			<td>${NATIONALITY}</td> <td>${IDPROOF} </td>  <td>${IDPROOFNO} </td>  <td> ${IDPROOF} </td> 
	 			   </tr>
	 			
	 		</s:iterator>
	 		</table>	 		
	 	</td>
	 </tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<%-- 	<tr><td>&nbsp; john <s:property value="creditbean.doact"/> pritto</td></tr> --%>
		<tr>
		<s:if test="%{creditbean.doact=='AUTH'}">
			<td>
		     	<s:submit value="Authorize"   name="authtbn" id="sixtab" onclick="return customerinfo_checker()"/>
			</td>  
			<td>
			<s:submit value="Reject"   name="deauthtbn" id="sixtab"  class="cancelbtn"  onclick="return getDeauthReason()"/>
			</td>
		</s:if>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
				 
		</td>
		
		</tr>
	</table> 
	</s:form>
</sx:div>


</sx:tabbedpanel>

 
</body>