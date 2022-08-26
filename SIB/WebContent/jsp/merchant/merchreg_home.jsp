<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="jsp/merchant/script/merchscript.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>

	
<script type="text/javascript">

	function getCardDetails(){
		var catogorytype = document.getElementById("catogorytype");
		var cardno = document.getElementById("rawacctno");
		var merchantid= document.getElementById("merchantid");
		var firstname = document.getElementById("firstname"); 
		var lastname = document.getElementById("lastname");
		var cbsacctno = document.getElementById("cbsacctno");
		var primary_mobileno = document.getElementById("primary_mobileno");
		var sec_mobileno = document.getElementById("sec_mobileno");
		var dob = document.getElementById("dob");
		var primary_mailid = document.getElementById("primary_mailid");
		var paddress1 = document.getElementById("paddress1");
		var paddress2 = document.getElementById("paddress2");
		var paddress3 = document.getElementById("paddress3");
		//alert( catogorytype.value );
		if( catogorytype.value == "-1"){ errMessage(catogorytype, "Select Type ");  return false;  }
		
		if( cardno.value == "" ){ errMessage(cardno, "Enter card number...");  return false; }
		
		var url = "getCardDetailsMerchantRegister.do?cardno="+cardno.value; 
		var result = AjaxReturnValue(url);
		var res = result.split("~");
		if(res[1] != 0 ){
			errMessage( cardno, res[0]);
		}else{
			errMessage( cardno, "");
			var jsonobj = JSON.parse( res[0] );  
			
			if( catogorytype.value == "$MERCH"){
				merchantid.readOnly=true;
				merchantid.value = jsonobj["$MERCHANTID"];	
			}else{
				merchantid.readOnly=false;
				merchantid.value = "";
			}
			
			firstname.value = jsonobj["$FIRSTNAME"];
			lastname.value = jsonobj["$LASTNAME"];
			cbsacctno.value = cardno.value;
			primary_mobileno.value = jsonobj["$MOBILENO"];
			sec_mobileno.value = jsonobj["$PHONENO"];
			primary_mailid.value = jsonobj["$EMAILADDRESS"];
			dob.value = jsonobj["$DOB"];
			paddress1.value = jsonobj["$ADDR1"]; 
			
			if( jsonobj["$ADDR2"] != null ){
				paddress2.value = jsonobj["$ADDR2"];	
			}
			
			if( jsonobj["$ADDR3"] != null ){
				paddress3.value = jsonobj["$ADDR3"];	
			}
			
			
			
		}
		
		 
		
		return false;
	}

	function getAcctTypeVal( accttypeval ){
		if( accttypeval != "-1"){
			var url = "getAcctTypeDescMerchantRegister.do?acctypekey="+accttypeval; 
			var result = AjaxReturnValue(url);
			document.getElementById("acctnolable").innerHTML=result;
		}else{
			document.getElementById("acctnolable").disabled = true;
		}
		
	}

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
	
function editFileds( formname ){
	 
	var inpfld = formname.getElementsByTagName("input");
	for( var i=0; i<inpfld.length; i++){	
		inpfld[i].disabled=true;
	}
	 
	
	var selectfld = formname.getElementsByTagName("select");
	for( var i=0; i<selectfld.length; i++){
		selectfld[i].disabled=true;
	}
	
	
}

function selectMultiple(){
	var opt = "<option value='-1'>SELECT</option>";
 
	var options = document.getElementById('curcode').options, count = 0;
	for (var i=0; i < options.length; i++) {
	  if (options[i].selected) {  
		  var url = "currencyDescriptionMerchantRegister.do?CURCODE="+options[i].value;
		  var curdesc = AjaxReturnValue(url);
		 
		  opt += "<option value='"+options[i].value+"'>"+curdesc+"</option>";
		  count++;	
		  
	  }
	 
	}
	
	if( count == 0 ){
		errMessage( document.getElementById('curcode'), "SELECT CURRENCY");
		return false;
	}
	 
	if( count > 1 ){ 
		
		document.getElementById("primcurfld").style.visibility='visible';
		document.getElementById("primarycur").style.visibility='visible'; 
		
		document.getElementById("primarycur").innerHTML=opt;
	}else{
		document.getElementById("primcurfld").style.visibility='hidden';
		document.getElementById("primarycur").style.visibility='hidden'; 
	}
}

function numberOnly(evt){
	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
	//alert(keyvalue);
		if((!(keyvalue<48 || keyvalue>57)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13))
		{
			return true;
		}
		else
		{ 
			return false;
		} 
	}
	

	function getConfiguredVal( merchtypecode ){
			 
			var url = "getSubglcodeListMerchantRegister.do?merchtypecode="+merchtypecode; 
			var result = AjaxReturnValue(url);
			var jsonobj = JSON.parse(result);
			
			var feelistid = document.getElementById("feelistid");
			var commisionid = document.getElementById("commisionid");
			var discountid = document.getElementById("discountid");
			
			var actbutton = document.getElementById("actbutton");
			var errmsg = document.getElementById("feelistid");
			var discountbased = document.getElementById("discountbased");
			var commissionbased = document.getElementById("commissionbased");
			if( jsonobj["RESP"] != 0 ){
				errmsg.innerHTML=jsonobj["MESSAGE"];
				//actbutton.disabled=true;
				return false;
			}else{
				 
				if(  jsonobj["FEE_REQ"] == "Y"){
					feelistid.disabled = false;
				}else{ 
					feelistid.disabled = false;
					feelistid.innerHTML = "<option value='NOTREQ'> FEE NOT REQUIRED </option>";
				}
				
				if(  jsonobj["COMMISSION_REQ"] == "Y"){
					commisionid.disabled = false;
				}else{ 
					feelistid.disabled = false;
					commisionid.innerHTML = "<option value='NOTREQ'> COMMISSION NOT REQUIRED </option>";
					commissionbased.innerHTML = "<option value='NOTREQ'>  NOT REQUIRED </option>";
					 
				}
				
				//alert( "disc : " + jsonobj["DISCOUNT_REQ"] );
				if(  jsonobj["DISCOUNT_REQ"] == "Y"){
					discountid.disabled = false;
				}else{ 
					discountid.disabled = false;
					discountid.innerHTML = "<option value='NOTREQ'> DISCOUNT NOT REQUIRED </option>";
					discountbased.innerHTML = "<option value='NOTREQ'>  NOT REQUIRED </option>";
				}
			}
			 
	}
	function checkConfigExist( PRICINGTYPE, PRICINGCODE, PRICINGMODE ){
		if( PRICINGCODE != "-1"){
			var url = "checkPricingExistMerchantRegister.do?PRICINGTYPE="+PRICINGTYPE+"&PRICINGCODE="+PRICINGCODE+"&PRICINGMODE="+PRICINGMODE; 
			//var result = AjaxReturnValue(url);
		}else{
			alert( "SELECT " + PRICINGTYPE );
			return false;
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



<%
Boolean editable = true;
String merchname ;

if( request.getParameter("merchname") != null ){
	 merchname = request.getParameter("merchname").replace("~", ""); 
}else{
	merchname = " "; 
	%>

<%
}
 
%>

<body onload="test()">
	<s:form action="addMerchangInfoMerchantRegister.do?action=save"
		name="custinfoform" id="custinfoform" autocomplete="off" namespace="/"  >

		<table border="0" cellpadding="0" cellspacing="0" width="100%"	style='border: 1px solid #efefef' align="center" class="filtertab">
			<tr> 	
			
			
				<td>Type  </td>
				<td>:  
						
					 <s:select  name="catogorytype" id="catogorytype" list="#{'$MERCH':'Merchant','$AGE':'Agent'}" headerKey="-1" value="%{mregbean.mercahntcatogory}" headerValue="-SELECT-"></s:select>
					 														<!-- '$CORP':'Coporate' -->
						 														
				</td>
				
			<td><span id="acctnolable"> Card No. <span class="mand">*</span> </span> </td>
				<td>: <s:textfield name="rawacctno"	id="rawacctno" value='%{mregbean.rawacctno}' maxlength="19" />
				&nbsp;
				 <s:submit  name="getdetails" id="getdetails" value="Get Details" onclick="return getCardDetails( )" />
				 
				</td></tr>
			
			<tr>
				<td>Merchant Id    </td>
				
			 
				<td>: <s:textfield name="merchantid" id="merchantid" value='%{mregbean.merchid}'  
						 maxlength="15" onkeypress='return numberOnly(event)'  />
						 
						
				</td>
				
				<td>MERCHANT NAME</td>
				<td>: <s:textfield name="merchantname" id="merchantname"
						maxlength="32" value='%{mregbean.merchname}' />
				</td>
				
				
				<td>Parent Type </td>
				<td>:  
						
					 <s:select  name="parentid" id="parentid" listKey="MERCH_ID" listValue="MERCH_NAME" list="%{mregbean.parentmerchantlist}" 
					 value="%{mregbean.merchantparenttype}" headerKey="$DEF" headerValue="Default" ></s:select>
						 														
				</td>
				
			</tr>

			<tr>
				
				<td>FIRST NAME</td>
				<td>: <s:textfield name="firstname" id="firstname"
						maxlength="32" value='%{mregbean.firstname}' />
				</td>
				
				<td>LAST NAME</td>
				<td>: <s:textfield name="lastname" id="lastname"
						maxlength="32" value='%{mregbean.lastname}' />
				</td>	
				
				<td>Primary Location </td> <td> <s:textfield name="primlocation" id="primlocation"/> </td> 
					
			</tr>
			<s:hidden name="merchid" value="%{mregbean.merchid}" />

			<tr>
			
				<td>MERCHANT TYPE  </td>
				   <td>: <s:select
						list="%{mregbean.merchtypelist}" 
						id="merchenttype" name="merchenttype" value="%{mregbean.merchanttypeid}" listKey="MERCHANT_TYPE_ID" 
						listValue="MERCHANT_TYPE_NAME" headerKey="-1" headerValue="SELECT"/>
					</td>
				   
				 
				<td>MCC<span class="mand">*</span>
				</td> 
				
				
				<td> : 
				  <s:select
						list="%{mregbean.mccllist}" 
						id="mcc" name="mcc" value="%{mregbean.mccid}" listKey="MCC" 
						listValue="MCC_DESC" headerKey="-1" headerValue="SELECT"/> 
				 
				</td> 
			</tr>
			
			<tr>
				
				<td>Account Type  </td>
					 
				 	<td>: <s:select list="%{mregbean.accttypelist}" id="accttype" name="accttype"
						listKey="ACCT_TYPE" listValue="ACCT_TYPE_DESC" headerKey="-1"
						headerValue="-SELECT-" value='%{mregbean.acctypecode}' />
					</td> 
					 
			 
				
				<td><span id="acctnolable"> Acct No. <span class="mand">*</span> </span> </td>
				<td>: <s:textfield name="cbsacctno"	id="cbsacctno" value='%{mregbean.cbsacctno}' maxlength="19" onkeypress='return numberOnly(event)' readonly="true"/></td>
			
			
				<td>Primary <br/> Mobile No <span class="mand">*</span></td>
				<td>: <s:textfield name="primary_mobileno"
						id="primary_mobileno" value='%{mregbean.primaymobile}' maxlength="12"  /></td>

				
			</tr> 

			<tr> 
				
				<td>Secondary <br /> Mobile No </td>
				<td>: <s:textfield name="sec_mobileno" id="sec_mobileno"
						value='%{mregbean.secmobile}' maxlength="12" /></td>
						
				<td>DOB</td>
				<td>: <s:textfield name="dob" id="dob" readonly="true"
						style="width:160px" value='%{mregbean.dob}' /> <img
					src="style/images/cal.gif" id="image"
					onclick="displayCalendar(document.custinfoform.dob,'dd-mm-yy',this);"
					title="Click Here to Pick up the date" border="0" width="15"
					height="17">
				</td>
				
				<td>Primary <br /> Mail Id <span class="mand">*</span></td>
				<td>: <s:textfield name="primary_mailid" id="primary_mailid"
						value='%{mregbean.primaymail}' /></td>

				
			</tr>

			<tr>
			
				<td>Secondary <br /> Mail Id
				</td>
				<td>: <s:textfield name="sec_mailid" id="sec_mailid"
						value='%{mregbean.secmail}' /></td> 
						
				 
				
				<td>Currency Code </td> 
				 
					<td>  &nbsp;  <s:select list="%{mregbean.curlist}" id="curcode" name="curcode"
							listKey="CUR_CODE" listValue="CUR_DESC" headerKey="-1-"
							 value="%{mregbean.curcodevkey}" multiple="true"  size="1"  cssStyle="width:200px; height:80px" onclick="selectMultiple()"/>  
							<br/> <small style='font-size:10px;color:#000'> &nbsp;&nbsp;&nbsp; <span class="mand">*</span> press ctrl to select multiple</small>
							
					</td>
				 			
						 
				
				<td> <span style='visibility:hidden' id='primcurfld'> Primary Currency </span> </td>
				 
				
				<td>  
					<select id="primarycur" name="primarycur" style='visibility:hidden' ></select>
				</td>  
			</tr>
			 
	<%-- <tr>
				<td>Sub gl code    
				</td>
				<td>: 
					<select id="subglcode" name="subglcode">
					</select>
				</td>
				
				<td> Select Fee <s:property value="mregbean.feecodevalue"/> </td>
				<td>:
					<s:select  name="feelistid" id="feelistid" list="mregbean.feeslist" listKey="FEE_CODE" value="%{mregbean.feecodevalue}"
					 listValue="FEE_DESC" headerKey="-1" headerValue="-- select Fee --"  ></s:select>
				</td>	
				
				<td>Commission</td>
				<td>: 
					<s:select  name="commisionid" id="commisionid" list="mregbean.commissionlist" listKey="COM_MASTERCODE" listValue="COM_DESC" headerKey="-1" headerValue="-- select Commission--"  ></s:select>
				</td>
				
				<td>Discount</td>
				<td>: 
					<s:select  name="discountid" id="discountid" list="mregbean.discountlist" listKey="DISC_MASTERCODE" listValue="DISC_DESC" headerKey="-1" headerValue="-- select Discount--"  ></s:select>
				</td>
			</tr> 

			<tr>
				 
				<td> Commission Based On </td>
				<td>:
					<s:select  name="commissionbased" id="commissionbased" list="#{'AMT':'Amount'}" headerKey="-1" onchange="checkConfigExist('COMMISSION', this.form.commisionid.value, this.value)"></s:select>
																				<!--  headerValue="- select -"   ,'CNT':'Count' -->
				</td>	
				<td> Discount Based On </td>
				<td>:
					<s:select  name="discountbased" id="discountbased" list="#{'AMT':'Amount'}" onchange="checkConfigExist('DISCOUNT', this.form.commisionid.value, this.value)" ></s:select>
																			<!--  headerKey="-1" headerValue="- select -"  ,'CNT':'Count' -->
				</td>
					<td>Pricing Mode
				</td>
				<td>: 
						<s:select  name="pricingmode" id="pricingmode" list="#{'GRP':'Group'}"   ></s:select>
								
												<!-- headerKey="-1" headerValue="- select -"  'IND':'Individual', -->
				</td>
												
			</tr>
	 --%>
			<tr>
				<td colspan="6">
					<fieldset>
						<legend> Address :</legend>
						<table>

							<tr>
								<td>Address1<b class="mand">*</b></td>
								<td>: <s:textfield name="paddress1" id="paddress1"
										maxlength="50" value="%{mregbean.addr1}" />
								</td>

								<td>Address2</td>
								<td>: <s:textfield name="paddress2" id="paddress2"
										maxlength="50" value="%{mregbean.addr2}" />
								</td>

								<td>Address3</td>
								<td>: <s:textfield name="paddress3" id="paddress3"
										maxlength="50" value="%{mregbean.addr3}" />
								</td>
							</tr>

						</table>
					</fieldset>
				</td>
			</tr>
		</table>

		<table border="0" cellpadding="0" cellspacing="4" width="20%">
			<tr>
				<td><s:submit value="%{mregbean.submitbtn}" name="actbutton"
						id="actbutton" onclick="return merchreg_validation();"/></td>
				<td><input type="button" name="cancel" id="cancel"
					value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

				</td>
			</tr>
		</table>





	</s:form>

</body>