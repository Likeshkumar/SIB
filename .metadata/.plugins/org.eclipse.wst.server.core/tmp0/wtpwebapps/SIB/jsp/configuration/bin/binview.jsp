<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>

<style>

table{  
	padding-top:10px;
	padding:0 auto;
	width:750px;
}



</style>
 <script>
 function validation_authbin(){
	 	var auth = document.getElementById("auth0").value;
	 	var bin = document.getElementById("bin").value;
	 	var reason = prompt('Enter the Reason for Reject');
		 if( reason ){
			 var url = "authdeauthbinlistAddBinAction.do?bin="+bin+"&reason="+reason+"&auth="+auth;
			 window.location = url; 
		 }  
		 return false;
	}
 
 function deleteBin(){
 	var bin = document.getElementById("bin");
 	var productcount = document.getElementById("productcount").value;
 	if( productcount > 0 ){
 	if( !confirm( productcount + " Product(s) Already configured. Do you want to delete anymore ? " ) ){
 		return false;
 	}
 	}
 	var reason = prompt("Reason For Delete :");
 	if( reason == null ){
 		return false;
 	}
 	var url ="deleteBinActionAddBinAction.do?bin="+bin.value+"&reason="+reason;
 	window.location=url;
 	return false;
	 
 }
 
 function deleteBinAuthorze(){
	 	 
	 	if( !confirm( "Do you want to continue" ) ){
	 		return false;
	 	}
	 	var url ="deleteBinAuthActionAddBinAction.do?bin="+bin.value;
	 	window.location=url;
	 	return false;
		 
	 }
 
 </script>
<s:form action="authdeauthbinlistAddBinAction.do" method="post" autocomplete="off">
<s:hidden name="doact" id="doact" value="%{binbean.doact}"/>
<s:hidden name="reason" id="reason" value="" />
<s:hidden name="productcount" id="productcount" value="%{binbean.productcount}"/>
  <s:iterator value="lst_bindetails">
	<s:hidden name="brcodlen"  id="brcodlen" ></s:hidden><s:hidden name="bin" id="bin" value="%{BIN}"></s:hidden>
 	  <fieldset>
      	<legend><b> AUTH DETAILS</b></legend> 
 	     <table border="0" cellpadding="0" cellspacing="0" width="100%"  align='center' rules="none" frame="box">
				<tr>
					<td width="50%">CONFIGURED BY</td>						
					<td class="textcolor"> : ${MAKER_NAME} </td>					
				</tr>
				<tr>
					<td width="50%">CONFIGURED DATE</td>						
					<td class="textcolor"> : ${MAKER_DATE} </td>					
				</tr>
				
				<tr>
					<td> STATUS </td>
					<td class="textcolor"> : ${AUTH_CODE}</td>
			   </tr>
			   <tr>
					<td> AUTHORIZED BY </td>
					<td class="textcolor"> : ${CHECKER_NAME}</td>
			   </tr>
			   <tr>
					<td> AUTHORIZED DATE </td>
					<td class="textcolor"> : ${CHECKER_DATE}</td>
			   </tr>
 				<tr>
 					<td> REMARKS </td>
		    		<td class="textcolor"> : ${REMARKS}</td>
				</tr>
	     </table>
      </fieldset> 
      <br><br>
 	   <fieldset>
      	<legend><b> BIN DETAILS</b></legend> 
 	     <table border="0" cellpadding="0" cellspacing="0" width="100%"  align='center' rules="none" frame="box">
				<tr>
					<td width="50%">INSTITUTION ID</td>
						
					<td class="textcolor"> : ${INST_ID} </td>
					
				</tr>
				<tr>
					<td> BIN NUMBER</td>
					<td class="textcolor"> : ${BIN}</td>
			   </tr>
 	
 				 
 				<tr>
 					<td>BIN DESC</td>
		    		<td class="textcolor"> : ${BIN_DESC}</td>
				</tr>
	     </table>
      </fieldset> 
      <br><br>
      <fieldset>
      <legend><b> CHN DETAILS</b></legend>
       <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		    
		    <tr>
					<td width="50%">CHN LENGTH </td>
					<td class="textcolor"> : ${CHNLEN}</td>		
			
			</tr>
			<tr>
					<td>ATTACH CARD TYPE/PRODUCT CODE  </td>
					<td class="textcolor"> : ${ATTACH_PRODTYPE_CARDTYPE} <s:hidden name="attachproductcode" id="attachproductcode" value="%{ATTACH_PRODTYPE_CARDTYPE}"/> </td>
			</tr>
			
			<tr>
					<td>ATTACH  BRANCH CODEss  </td>
					<td class="textcolor"> : ${ATTACH_BRCODE} <s:hidden name="attachbranchcode" id="attachbranchcode" value="%{ATTACH_BRCODE}"/></td>	 
			</tr>
			
			<%-- <s:hidden name="attachaccttype" id="attachaccttype" value="%{ATTACH_ACCTTYPE}"/>
			<s:hidden name="attachedaccttype" id="attachedaccttype" value="%{ATTACHED_ACCTTYPE}"/> --%>
			
			<tr>
				<td>BASE LENGTH </td>
				<td class="textcolor"> : ${BASELEN} <s:hidden name="baselen" id="baselen" value="%{BASELEN}"></s:hidden> </td>		
			</tr>
			
			<tr>
				<td>NO CARDS CAN BE GENERATED </td>
				<td class="textcolor"> : ${NOS_CARDS_GEN}</td>		
			</tr>	
			
			<tr>
				<td>SERVICE CODE</td>
				<td class="textcolor"> : ${SERVICE_CODE}</td>		
			</tr>

		 </table>
     </fieldset>
     <br><br>
     
     <fieldset>
      	<legend><b> SECURITY DATA OPTIONS</b></legend> 
 	     <table border="0" cellpadding="0" cellspacing="0" width="100%"  align='center' rules="none" frame="box">
				<tr>
					<td width="50%">CARD TYPE</td>
						
					<td class="textcolor"> : ${CARD_TYPE} </td>
					
				</tr>
				<tr>
					<td> SECURITY TYPE</td>
					<td class="textcolor"> : ${SEQ_OPTION}</td>
			   </tr>
 	     </table>
      </fieldset> 
    
	    <s:if test='SEC_CUR=="YES"'> 
		    <fieldset>
	      		<legend><b> MULTI-CURRENCY DETAILS</b></legend>
				      <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" rules="none" frame="box">		
						<s:iterator value="binmulccylist">
							<tr>
								<td width="20%" class="textcolor">${CURRENCY_DESC}</td><td align="center">Minimum Amount</td><td class="textcolor"> : ${MIN_AMOUNT}</td><td align="center">Maximum Amount</td><td class="textcolor"> : ${MAX_AMOUNT}</td>
							</tr>
						</s:iterator>	
					 </table>
			</fieldset>
		</s:if> 
		<s:else></s:else>
      <br/><br/>
     
       <fieldset>
	    	<legend> <b>SECURITY PARAMETERS</b></legend>
            	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" rules="none" frame="box">
		 	  <tr>
					<td>SELECT HSM </td>
					<td class="textcolor">  
						: ${HSMNAME}
					</td>
			
					
					<td> PIN ENCRYPTION KEY LENGTH  </td>
					<td class="textcolor">  
						: ${DESLENGTH} Digits
					</td>
			</tr>
			<tr>
					<td>PAN VALIDATION <br>OFFSET</td>
					<td class="textcolor"> : ${PAN_OFFSET}</td>
					
					<td>PAN VALIDATION <br>LENGHT</td>
					<td class="textcolor"> : ${PANVALIDATION_LENGTH}</td>
			</tr>
			<tr>
					<td >PIN LENGHT</td>
					<td class="textcolor"> : ${PIN_LENGTH}</td>
			
					<td>PIN OFFSET <br>LENGHT</td>
					<td class="textcolor"> : ${PINOFFSET_LENGTH}</td>
					
			</tr>
			<tr>
					<td>DECIMILISATION TABLE</td>
					<td class="textcolor"> : ${DECIMILISATION_TABLE}</td>
			
					<td>PAN PAD CHARACTER </td>
					<td class="textcolor"> : ${PANPADCHAR}</td>
						
			</tr>
			<tr>
					
					
					<td>PIN GENERATION TYPE</td>
					<td class="textcolor"> : ${GEN_METHOD}
					<td>
						PINMAILER
					</td>
					<td class="textcolor">  
						: ${PINMAILER_NAME}
					</td>
			</tr>		

			<tr>
					<td>PIN VERIFICATION KEY INDEX</td>
					<td class="textcolor"> : ${PVKI}</td>
					
					<td>PIN VERIFICATION KEY1</td>
					<td class="textcolor"> : ${PVK1}</td>
			</tr>
			<tr>
					<td>PIN VERIFICATION KEY2</td>
					<td class="textcolor"> : ${PVK2}</td>
					
					<td>PIN VERIFICATION KEY</td>
					<td class="textcolor"> :${PVK}</td>
			
			</tr>
			<tr>
			      
					<td>CVV REQUIRED</td>
					<td class="textcolor"> : ${CVV_REQUIRED}</td>
 
			</tr>
			<tr>
					<td>CVV KEY 1</td>
					<td class="textcolor"> : ${CVK1}</td>
					
					<td>CVV KEY 2</td>
					<td class="textcolor"> : ${CVK2}</td>
			
			</tr>
		 </table>
  </fieldset>
	 <table border="0" cellpadding="0" cellspacing="0" width="25%" align="center" rules="none" frame="box">
				<tr align="center">    
				 
					<s:if test="%{binbean.doact=='$DEL'}">
						<td> <input type="submit" name="delete" id="delete" value="Delete" onclick="return deleteBin();"/>  
						 <input type="button" name="cancel" id="cancel" value="Cance"  class="cancelbtn"  onclick="return confirmCancel()"/></td>
					</s:if>
					<s:elseif test="%{binbean.doact=='$DELAUTH'}">
						<td> <input type="submit" name="deleteauth" id="deleteauth" value="Authorize" onclick="return deleteBinAuthorze();"/>  
						 <input type="button" name="cancel" id="cancel" value="Cance"  class="cancelbtn"  onclick="return confirmCancel()"/>
						 <input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authbin()"/>
						 </td>
					</s:elseif>
						<td>
						<s:if test="%{flg=='add'}"><%-- <s:submit value="Back" name="submit" id="submit"  onclick="window.history.back(-2);"/> --%></s:if><s:else>
							<input type="submit" name="auth" id="auth1" value="Authorize" onclick="return validation_authfee();"/>
							<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authbin()"/>
						</s:else>
						</td>
				</tr>
    </table>
  </s:iterator>
</s:form>
