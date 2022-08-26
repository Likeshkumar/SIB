<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
 <script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
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
		var paddress1 = document.getElementById("paddress1");
		var paddress2= document.getElementById("paddress2");
		var paddress3= document.getElementById("paddress3");
		var paddress4= document.getElementById("paddress4");
		
		if(!rescheck){
			paddress1.disabled=false;
			paddress2.disabled=false;
			paddress3.disabled=false;
			paddress4.disabled=false;
			paddress1.focus();
		}else{
			paddress1.disabled=true;
			paddress2.disabled=true;
			paddress3.disabled=true;
			paddress4.disabled=true;
		}
	}

 function validate(){
	  alert(document.getElementById("uploadedphoto").size  );
	 return false;
	 
 }
	
</script>
<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
 <!--  onsubmit="return validateFilter()" -->
<s:form action="saveCustDetailInstCardRegisterProcess" enctype="multipart/form-data"   name="custinfoform" id="custinfoform" autocomplete = "off"  namespace="/">

 <s:hidden name="custtype" value="%{cardregbean.custtype}" />

 <s:hidden name="cardno" value="%{cardregbean.cardno}" />  

 <s:hidden name="custid" value="%{cardregbean.custid}" /> 
 
 <s:set name="custidavail" value="%{cardregbean.custid}" /> 

<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="filtertab">	 
 
		<s:if test="cardregbean.existingcust"> 
		<tr><td colspan="6">
			<table border='0' cellpadding='0' cellspacing='0' width='100%'>
				<tr>  <td> CARD NUMBER: <s:property value="cardregbean.cardno"/></td><td></td>
				
				<td  valign="middle" >Photo</td>
				 
				<td>  
					<s:set var="photourlaction">imageActionInstCardRegisterProcess?imagetype=PHOTO&imageId=<s:property value='cardregbean.photourl'/> </s:set>  
					<img src="<s:url action='%{photourlaction}' />"  width="100" />
					<s:hidden name="oldphoto" value="%{cardregbean.photourl}" />
				</td> 
				
				<td  valign="middle" > Signature </td>
				<td>
					<s:set var="photourlaction">imageActionInstCardRegisterProcess?imagetype=SIGNATURE&imageId=<s:property value='cardregbean.photourl'/> </s:set>  
					<img src="<s:url action='%{photourlaction}' />"  width="100" />
					<s:hidden name="oldphoto" value="%{cardregbean.photourl}" />
				</td> 
				
			 
				<td  valign="middle" >ID-Proof</td>
				<td>
					<s:set var="photourlaction">imageActionInstCardRegisterProcess?imagetype=IDPROOF&imageId=<s:property value='cardregbean.photourl'/> </s:set>  
					<img src="<s:url action='%{photourlaction}' />"  width="100" />
					<s:hidden name="oldphoto" value="%{cardregbean.photourl}" />
				</td> 
				
				<%-- <td></td>
				  <td> CUSTOMER ID : <s:property value="cardregbean.custid"/> </td>  --%>
				  </tr>
			</table>
		</td></tr>
		</s:if>
	 	<tr>
			<td>First Name<span class="mand">*</span></td>
			<td><s:textfield name="firstname" id="firstname" maxlength="15"  value="%{cardregbean.firstname}"  /></td>
			
			 <td>Middle Name </td>
		 	<td><s:textfield name="midname" id="midname" maxlength="15" value="%{cardregbean.midname}"  /></td>
			
			 <td >Last Name </td>
	 	     <td> <s:textfield   name="lastname" id="lastname" maxlength="15" value="%{cardregbean.lastname}"  /></td>  
	    </tr>
	 
	 	<tr>
			<td>Father Name </td>
			<td><s:textfield name="fahtername" id="fathername" maxlength="40" value="%{cardregbean.fahtername}" onKeyPress=" return alphabets(event);"  /></td> 
			
			<s:hidden name="fahtername" id="fathername" maxlength="40"  value="NA" onKeyPress=" return alphabets(event);"  />
			
			 <td>Mother Name </td>
		 	<td><s:textfield name="mothername" id="mothername" maxlength="15" value="%{cardregbean.mothername}" onKeyPress=" return alphabets(event);" /></td>
			
			 <td>&nbsp; </td>
	 	     <td> &nbsp; </td>  
	    </tr>
	 
	 
	 	<tr>
	 	
	 		<td> Gender <span class="mand">*</span></td>
			<td>
				<s:select list="#{'-1':'-SELECT-','M':'Male','F':'Female'}" id="gender" name="gender" value="%{cardregbean.gender}"/>
				 
			</td>
			
			
			<td>Marital Status<span class="mand">*</span></td>
			<td>
				<s:select list="#{'-1':'-SELECT-','M':'Married','U':'Un Married'}" id="mstatus" name="mstatus" value="%{cardregbean.mstatus}" onchange="showSpouse( this.id )"/>
				 
			</td>
			
			 <td> Spouse Name </td>
		 	<td><s:textfield name="spname" id="spname" maxlength="15" value="%{cardregbean.spname}"/></td> 
	    </tr>
	    
	 
	 	<tr>
			<td>Occupation<span class="mand">*</span></td>
			<td><s:textfield name="occupation" id="occupation" maxlength="15" value="%{cardregbean.occupation}" onKeyPress="return alphabets(event);" /></td>
			
			 <td>Nationality<b class="mand">*</b> </td>
			  
			 <td>
			 	<%-- <s:textfield name="nations" id="nations" value="Tanzania"/> --%>
			 	 
			 	<s:select name="nations" id="nations" listKey="NATION_ID" listValue="NATION" list="cardregbean.nationalitylist"  value="%{cardregbean.nationality}" />
			 	 
			 </td>
			  
			
			 <td>Date Of Birth<b class="mand">*</b></td>
			<td>
			 
				<s:textfield name="dob" id="dob" value="%{cardregbean.dob}" readonly="true" style="width:160px"/>
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.custinfoform.dob,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td> 
	    </tr> 
	 
		 <tr>
			<td >Mobile No.<b class="mand">*</b></td>
			<td><s:textfield name="mobileno" id="mobileno"  maxlength="13" value="%{cardregbean.mobileno}"  /></td> 
			
			<td >Phone Number </td>
				<td><s:textfield name="phoneno" id="phoneno" maxlength="15" value="%{cardregbean.phoneno}"  /></td> 
	   
			<td >E-Mail Address </td>
			<td><s:textfield name="email"  value="%{cardregbean.email}" id="email" /> </td> 
		</tr>
		
		<tr>
			  <td>Identity Name<b class="mand">*</b></td>
			<td>
			
				<s:select name="documentname" id="documentid" listKey="DOC_ID" listValue="DOC_TYPE" list="cardregbean.documenttypelist"  value="%{cardregbean.reqdocuement}" />
				
				 
				 
			 
			<td >ID Number </td>
				<td><s:textfield name="documentnumber" id="documentid" maxlength="15" value="%{cardregbean.documentid}"/></td> 
	   
			<td> &nbsp; </td>
				<td>&nbsp; </td> 
				
				<td> &nbsp; </td>
				<td>&nbsp; </td> 
		</tr>
		
		<tr>
			<%-- <td >Application Date </td>
			<td>
				 <s:textfield  name="appdate" id="appdate" value="%{cardregbean.appdate}" readonly="true"/>
				 <img src="style/images/cal.gif" id="image1" onclick="displayCalendar(document.custinfoform.appdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"> 
			</td> 
			<td colspan="4">&nbsp;</td> --%>
			<%-- <td >App No. </td>
				<td><s:textfield name="appno" id="appno" value="%{cardregbean.appno}" maxlength="15"  />< /td>  --%>
	   
			 
		</tr> 
		<tr>
			 <td > Photo  </td>
				<td> : <s:file name="uploadedphoto" id="uploadedphoto" value="%{cardregbean.uploadedphoto}" />
				<small class="dt"> <br/>.jpg, .jpeg, .gif only, Max file size 2 MB</small>
				</td>  
				
			 <td > Signature  Upload </td>
				<td> : <s:file name="uploadsignature" id="uploadsignature" value="%{cardregbean.uploadedphoto}" />
				<small class="dt"> <br/>.jpg, .jpeg, .gif only, Max file size 2 MB</small>
				</td>  
			
			 <td > ID-Proof Upload </td>
				<td> : <s:file name="uploadidproof" id="uploadidproof" value="%{cardregbean.uploadedphoto}" />
				<small class="dt"> <br/>.jpg, .jpeg, .gif only, Max file size 2 MB</small>
				</td>  
				
		</tr>
		
		
	 <tr><td colspan="6"><span style="color:blue;font-size:10px"><input type='checkbox' name="residentreq" onclick="showResident(this.checked)" /> Use Resident as Postal address </</span></td></tr>
	 
	  <tr>
		 	<td colspan="6">
		 	<fieldset>
		 		<legend>Resident Address :</legend>
		 		 
			 	<table>
			 	
			 	<tr>
					<td>  Address1<b class="mand">*</b></td>
					<td>
						<s:textfield name="resaddress1" id="resaddress1" maxlength="50" value="%{cardregbean.resaddress1}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				 
					<td>  Address2 </td>
					<td>
						<s:textfield name="resaddress2" id="resaddress2" maxlength="50" value="%{cardregbean.resaddress2}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				  
					<td>  Address3</td>
					<td>
						<s:textfield name="resaddress3" id="resaddress3" maxlength="50" value="%{cardregbean.resaddress3}"  onKeyPress=" return alphanumerals(event);"/>
					</td>   
				</tr>
				<tr>
					<td>  Address4 </td>
					<td>
						<s:textfield name="resaddress4" id="resaddress4" maxlength="50" value="%{cardregbean.resaddress4}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				</tr>
				</table>
				</fieldset>
			</td>
	 </tr> 
	  <tr>
		 	<td colspan="6">
		 	<fieldset>
		 		<legend>Postal Address :</legend>
			 	<table>
			 	
			 	<tr>
					<td>  Address1<b class="mand">*</b></td>
					<td>
						<s:textfield name="paddress1" id="paddress1" maxlength="50" value="%{cardregbean.paddress1}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				 
					<td>  Address2 </td>
					<td>
						<s:textfield name="paddress2" id="paddress2" maxlength="50" value="%{cardregbean.paddress2}"  onKeyPress=" return alphanumerals(event);"/>
					</td>
				  
					<td>  Address3</td>
					<td>
						<s:textfield name="paddress3" id="paddress3" maxlength="50" value="%{cardregbean.paddress3}"  onKeyPress=" return alphanumerals(event);"/>
					</td>   
				</tr>
				<tr>
					<td>  Address4 </td>
					<td>
						<s:textfield name="paddress4" id="paddress4" maxlength="50" value="%{cardregbean.paddress4}"  onKeyPress=" return alphanumerals(event);"/>
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
			<s:submit value="Submit" name="order" id="order"  onclick="return customerinfo_checker()" /><!--  -->
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 