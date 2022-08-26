<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/script.js"></script> 

<title>SECURITY</title>

<script>
	function enableSMS( chkstatus, smsgrpid ){
		 
		if( chkstatus ){
			document.getElementById(smsgrpid.id).disabled=false; 
		}else{
			document.getElementById(smsgrpid.id).disabled=true;
		}
	}
	
	function enableMAIL( chkstatus, mailgrpid ){
		if( chkstatus ){
			document.getElementById(mailgrpid.id).disabled=false; 
		}else{
			document.getElementById(mailgrpid.id).disabled=true;
		}
	}
</script>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>



<sx:tabbedpanel id="test" cssClass="d" cssStyle="width: 100%; margin:0 auto; height: 400px;" doLayout="true">

<sx:div id="one" label="NO OF PIN ATTEMPTS" labelposition="bottom" >
	<br> 
	<table border="0" cellpadding="0" cellspacing="10" RULES="NONE" width="100%"  style="border-color: gray;">	
	<s:form action="addSecurityActionFraudManagementAction.do"  name="perscardgen" autocomplete = "off" namespace="/">
		<tr align="center">
			<td colspan="4" class="formtitle"><h2> NO OF PIN ATTEMPTS CONFIGURATION </h2></td>
			<s:hidden name="entityid" value="NPIN" />
		</tr>
		<tr>  
			<td> NO OF PIN ATTEMPTS </td>
			<td> : <s:textfield name="entityvalue" id="entityvalue" /> </td>
			
			<td> MERCHANT GROUP ID </td>
			<td> :  <s:select list="smsgrplist" name="cardgrpid" id="cardgrpid"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{CARD_DESC}"
						listKey="%{CARDGRP}" 
					 />  </td>
			
		</tr>  
		
		
		<tr>  
			<td> SMS REQUIRED </td>
			<td> : <s:checkbox name="smsrequired" id="smsrequired" onclick="enableSMS(this.checked, smsgrpid1)"/> </td>
			
			<td id="tdsmsgrp1"> SMS GROUP </td>
			<td id="tdsmsgrp2" > : <s:select list="smsgrouplist" name="smsgrpid" id="smsgrpid1"  disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{DOC_TYPE}"
						listKey="%{DOC_ID}" 
			 /> </td>
		</tr>  
		<tr>  
			<td> MAIL REQUIRED </td>
			<td> : <s:checkbox name="mailrequired" id="mailrequired" onclick="enableMAIL(this.checked, mailgrp1)"/> </td>
			
			<td> MAIL GROUP </td>
			<td> :
				  <s:select list="maillist" name="mailgrp" id="mailgrp1" disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{MAILGRP_NAME}"
						listKey="%{MAILGRP_ID}"  />
			 
			 </td>
		</tr>  
		<tr> 	
			<td> IF FRAUD FOUND </td>
			<td> : <s:select name="fraudresult" id="fraudresut" list="#{'CAPTURE':'CAPTURE TRANSACTION','REJECT':'REJECT TRANSACTION'}" headerKey="-1" headerValue="-SELECT-" /> </td>
	    </tr>  
		<tr align="center">
			<td colspan="4"><input type="submit" value="SUBMIT"></td>
		</tr>
		</s:form>
	</table>
</sx:div>

<sx:div id="two" label="NO OF CVV ATTEMPTS" labelposition="top" >
	<br> 
	<table border="0" cellpadding="0" cellspacing="10" RULES="NONE" width="100%"  style="border-color: gray;">	
	<s:form action="addSecurityActionFraudManagementAction.do"  name="perscardgen" autocomplete = "off" namespace="/">
		<tr align="center">
			<td colspan="4" class="formtitle"><h2> NO OF CVV ATTEMPTS   </h2></td>
			<s:hidden name="entityid" value="NCVV" />
		</tr>
		<tr>  
			<td> NO OF CVV ATTEMPTS </td>
			<td> : <s:textfield name="entityvalue" id="entityvalue" /> </td>
			
			<td>MERCHANT GROUP ID </td>
			<td> :  <s:select list="smsgrplist" name="cardgrpid" id="cardgrpid"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{CARD_DESC}"
						listKey="%{CARDGRP}" 
					 />  </td>
			
		</tr>  
		<tr>  
			<td> SMS REQUIRED </td>
			<td> : <s:checkbox name="smsrequired" id="smsrequired" onclick="enableSMS(this.checked, smsgrpid2)"/> </td>
			
			<td id="tdsmsgrp1"> SMS GROUP </td>
			<td id="tdsmsgrp2" > : <s:select list="smsgrouplist" name="smsgrpid" id="smsgrpid2"  disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{DOC_TYPE}"
						listKey="%{DOC_ID}" 
			 /> </td>
		</tr>  
		<tr>  
			<td> MAIL REQUIRED </td>
			<td> : <s:checkbox name="mailrequired" id="mailrequired" onclick="enableMAIL(this.checked, mailgrp2)"/> </td>
			
			<td> MAIL GROUP </td>
			<td> :
				  <s:select list="maillist" name="mailgrp" id="mailgrp2" disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{MAILGRP_NAME}"
						listKey="%{MAILGRP_ID}"  />
			 
			 </td>
		</tr>  
		<tr> 	
			<td> IF FRAUD FOUND </td>
			<td> : <s:select name="fraudresult" id="fraudresut" list="#{'CAPTURE':'CAPTURE TRANSACTION','REJECT':'REJECT TRANSACTION'}" headerKey="-1" headerValue="-SELECT-" /> </td>
	    </tr>  
		<tr align="center">
			<td colspan="4"><input type="submit" value="OK"></td>
		</tr>
		</s:form>
	</table>
</sx:div>

<sx:div id="three" label="AMOUNT BASED" labelposition="top" >
	<br> 
	<table border="0" cellpadding="0" cellspacing="10" RULES="NONE" width="100%"  style="border-color: gray;">	
	<s:form action="addSecurityActionFraudManagementAction.do"  name="perscardgen" autocomplete = "off" namespace="/">
		<tr align="center">
			<td colspan="4" class="formtitle"><h2> AMOUNT BASED CONFIGURATION  </h2></td>
			<s:hidden name="entityid" value="AMTBASED" />
		</tr>
		<tr>  
			<td> AMOUNT   </td>
			<td> : <s:textfield name="entityvalue" id="entityvalue" /> </td>
			
			<td> MERCHANT GROUP ID  </td>
			<td> : 
					 
					  <s:select list="smsgrplist" name="cardgrpid" id="cardgrpid"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{CARD_DESC}"
						listKey="%{CARDGRP}" 
					 /> 
			</td> 
			
		</tr> 
		
		<tr>  
			<td> CURRENCY CODE </td>
			<td> : <s:textfield name="currency" id="currency" maxlength="3" /> </td>
			
			<td> COUNT </td>
			<td> : <s:textfield name="txncount" id="txncount" /> </td>
			
		</tr>  
		
		 
		<tr>  
			<td> SMS REQUIRED </td>
			<td> : <s:checkbox name="smsrequired" id="smsrequired" onclick="enableSMS(this.checked, smsgrpid3)"/> </td>
			
			<td id="tdsmsgrp1"> SMS GROUP </td>
			<td id="tdsmsgrp2" > : <s:select list="smsgrouplist" name="smsgrpid" id="smsgrpid3"  disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{DOC_TYPE}"
						listKey="%{DOC_ID}" 
			 /> </td>
		</tr>  
		<tr>  
			<td> MAIL REQUIRED </td>
			<td> : <s:checkbox name="mailrequired" id="mailrequired" onclick="enableMAIL(this.checked, mailgrp3)"/> </td>
			
			<td> MAIL GROUP </td>
			<td> :
				  <s:select list="maillist" name="mailgrp" id="mailgrp3" disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{MAILGRP_NAME}"
						listKey="%{MAILGRP_ID}"  />
			 
			 </td>
		</tr>  
		<tr> 	
			<td> IF FRAUD FOUND </td>
			<td> : <s:select name="fraudresult" id="fraudresut" list="#{'CAPTURE':'CAPTURE TRANSACTION','REJECT':'REJECT TRANSACTION'}" headerKey="-1" headerValue="-SELECT-" /> </td>
	    </tr>  
		<tr align="center">
			<td colspan="4"><input type="submit" value="OK"></td>
		</tr>
		</s:form>
	</table>
</sx:div>

<sx:div id="four" label="NO OF PIN TXN" labelposition="top" >
	<br> 
	<table border="0" cellpadding="0" cellspacing="10" RULES="NONE" width="100%"  style="border-color: gray;">	
	<s:form action="addSecurityActionFraudManagementAction.do"  name="perscardgen" autocomplete = "off" namespace="/">
		<tr align="center">
			<td colspan="4" class="formtitle"><h2> NO OF PIN TRANSACTION  </h2></td>
			<s:hidden name="entityid" value="NPINTXN" />
		</tr>
		<tr>  
			<td> NO OF PIN TXN </td>
			<td> : <s:textfield name="entityvalue" id="entityvalue" /> </td>
			
			<td> MERCHANT GROUP ID </td>
			<td> :  <s:select list="smsgrplist" name="cardgrpid" id="cardgrpid"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{CARD_DESC}"
						listKey="%{CARDGRP}" 
					 />  </td>
			
		</tr>  
		<tr>  
			<td> SMS REQUIRED </td>
			<td> : <s:checkbox name="smsrequired" id="smsrequired" onclick="enableSMS(this.checked, smsgrpid2)"/> </td>
			
			<td id="tdsmsgrp1"> SMS GROUP </td>
			<td id="tdsmsgrp2" > : <s:select list="smsgrouplist" name="smsgrpid" id="smsgrpid2"  disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{DOC_TYPE}"
						listKey="%{DOC_ID}" 
			 /> </td>
		</tr>  
		<tr>  
			<td> MAIL REQUIRED </td>
			<td> : <s:checkbox name="mailrequired" id="mailrequired" onclick="enableMAIL(this.checked, mailgrp4)"/> </td>
			
			<td> MAIL GROUP </td>
			<td> :
				  <s:select list="maillist" name="mailgrp" id="mailgrp4" disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{MAILGRP_NAME}"
						listKey="%{MAILGRP_ID}"  />
			 
			 </td>
		</tr>  
		<tr> 	
			<td> IF FRAUD FOUND </td>
			<td> : <s:select name="fraudresult" id="fraudresut" list="#{'CAPTURE':'CAPTURE TRANSACTION','REJECT':'REJECT TRANSACTION'}" headerKey="-1" headerValue="-SELECT-" /> </td>
	    </tr>  
		<tr align="center">
			<td colspan="4"><input type="submit" value="OK"></td>
		</tr>
		</s:form>
	</table>
</sx:div>


<sx:div id="five" label="MAGSTRIP TRANSACTION" labelposition="top" >
	<br> 
	<table border="0" cellpadding="0" cellspacing="10" RULES="NONE" width="100%"  style="border-color: gray;">	
	<s:form action="addSecurityActionFraudManagementAction.do"  name="perscardgen" autocomplete = "off" namespace="/">
		<tr align="center">
			<td colspan="4" class="formtitle"><h2> MAGSTRIP TRANSACTION  </h2></td>
			<s:hidden name="entityid" value="MAGSTRIP" />
		</tr>
		<tr>  
			<td> NO OF MAGSTRIP  TXN </td>
			<td> : <s:textfield name="entityvalue" id="entityvalue" /> </td>
			
			<td> MERCHANT GROUP ID </td>
			<td> :  <s:select list="smsgrplist" name="cardgrpid" id="cardgrpid"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{CARD_DESC}"
						listKey="%{CARDGRP}" 
					 />  </td>
			
		</tr>  
		<tr>  
			<td> SMS REQUIRED </td>
			<td> : <s:checkbox name="smsrequired" id="smsrequired" onclick="enableSMS(this.checked, smsgrpid5)"/> </td>
			
			<td id="tdsmsgrp1"> SMS GROUP </td>
			<td id="tdsmsgrp2" > : <s:select list="smsgrouplist" name="smsgrpid" id="smsgrpid5"  disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{DOC_TYPE}"
						listKey="%{DOC_ID}" 
			 /> </td>
		</tr>  
		<tr>  
			<td> MAIL REQUIRED </td>
			<td> : <s:checkbox name="mailrequired" id="mailrequired" onclick="enableMAIL(this.checked, mailgrp5)"/> </td>
			
			<td> MAIL GROUP </td>
			<td> :
				  <s:select list="maillist" name="mailgrp" id="mailgrp5" disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{MAILGRP_NAME}"
						listKey="%{MAILGRP_ID}"  />
			 
			 </td>
		</tr>  
		<tr> 	
			<td> IF FRAUD FOUND </td>
			<td> : <s:select name="fraudresult" id="fraudresut" list="#{'CAPTURE':'CAPTURE TRANSACTION','REJECT':'REJECT TRANSACTION'}" headerKey="-1" headerValue="-SELECT-" /> </td>
	    </tr>  
		<tr align="center">
			<td colspan="4"><input type="submit" value="OK"></td>
		</tr>
		</s:form>
	</table>
</sx:div>


<sx:div id="six" label="RESPONSE CODE BASED" labelposition="top" name="RESPONSECODE" >
	<br> 
	<table border="0" cellpadding="0" cellspacing="10" RULES="NONE" width="100%"  style="border-color: gray;">	
	<s:form action="addSecurityActionFraudManagementAction.do"  name="perscardgen" autocomplete = "off" namespace="/">
		<tr align="center">
			<td colspan="4" class="formtitle"><h2> RESPONSE CODE BASED </h2></td>
			<s:hidden name="entityid" value="RESPCODE" />
		</tr>
		<tr>  
			<td> RESPONSE CODE </td>
			<td> :  
					
					<s:select list="respcodelist" name="entityvalue" id="entityvalue"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{RESPCODE}"
						listKey="%{RESPCODE}" 
					 />
			
			 </td>
			
			<td> MERCHANT GROUP ID </td>
			<td> :  <s:select list="smsgrplist" name="cardgrpid" id="cardgrpid"  
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{CARD_DESC}"
						listKey="%{CARDGRP}" 
					 />  </td>
			
		</tr>  
		<tr>  
			<td> SMS REQUIRED </td>
			<td> : <s:checkbox name="smsrequired" id="smsrequired" onclick="enableSMS(this.checked, smsgrpid6)"/> </td>
			
			<td id="tdsmsgrp1"> SMS GROUP </td>
			<td id="tdsmsgrp2" > : <s:select list="smsgrouplist" name="smsgrpid" id="smsgrpid6"  disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{DOC_TYPE}"
						listKey="%{DOC_ID}" 
			 /> </td>
		</tr>  
		<tr>  
			<td> MAIL REQUIRED </td>
			<td> : <s:checkbox name="mailrequired" id="mailrequired" onclick="enableMAIL(this.checked, mailgrp6)"/> </td>
			
			<td> MAIL GROUP </td>
			<td> :
				  <s:select list="maillist" name="mailgrp" id="mailgrp6" disabled="true"
						headerKey="-1" headerValue="-SELECT-"
						listValue="%{MAILGRP_NAME}"
						listKey="%{MAILGRP_ID}"  />
			 
			 </td>
		</tr>  
		<tr> 	
			<td> IF FRAUD FOUND </td>
			<td> : <s:select name="fraudresult" id="fraudresut" list="#{'CAPTURE':'CAPTURE TRANSACTION','REJECT':'REJECT TRANSACTION'}" headerKey="-1" headerValue="-SELECT-" /> </td>
	    </tr>  
		<tr align="center">
			<td colspan="4"><input type="submit" value="OK"></td>
		</tr>
		</s:form>
	</table>
</sx:div>


</sx:tabbedpanel>

