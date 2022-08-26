<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="jsp/merchant/script/merchscript.js"></script> 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<sx:head />
<style>
a{
	color:blue;
}
</style>
<script>

	function hideOf( settlementid ){
		if( settlementid ==  "I"){
			document.getElementById("tdsettlemode1").style.display = 'table-row';
			document.getElementById("trnoofdays").style.display =  'table-row';
			document.getElementById("trsettletime").style.display =  'table-row';
			
		}else{
			document.getElementById("tdsettlemode1").style.display = 'table-row';
			document.getElementById("trnoofdays").style.display =  'none';
			document.getElementById("trsettletime").style.display =  'table-row';
		}
	}
	
	function hideOfNew( setid ){
		if( setid ==  "MAN"){
			document.getElementById("trsettletime").style.display =  'none';
		}else{
			document.getElementById("trsettletime").style.display =  'table-row';
		}
	}
	
	
	function settlementhome_validation()
	{
		var settlementon = document.getElementById("settleinterval");
		var settlemode = document.getElementById("settlemode");
		var settletime = document.getElementById("settletime");
		var noofdays = document.getElementById("noofdays");
		var autoCompleter = dojo.widget.byId('mercahntid'); 
		if(autoCompleter)
		{
			if(autoCompleter.getSelectedKey()==""){
					errMessage(settlementon,"Select Merchant");
					return false;	
				}
		}
		if(settlementon){
			//alert(settlementon.value);
			if(settlementon.value=="-1"){
				errMessage(errmsg,"Select Settlement on");
				return false;		
			}
			if(settlementon.value=="D")	{
				if(settlemode.value=="-1"){
					errMessage(settlemode,"Select Settlement type");
					return false;	
				}
				if( settlemode.value=="AUTO"){
					if(settletime.value==""){
						errMessage(settletime,"Select Settlement time");
						return false;	
					}
				}
				
			}
			if(settlementon.value=="W")	{
				if(settlemode.value=="-1"){
					errMessage(settlemode,"Select Settlement type");
					return false;	
				}
				if( settlemode.value=="AUTO" ){
					if(settletime.value==""){
						errMessage(settletime,"Select Settlement time");
						return false;	
					}
				}
			}
			if(settlementon.value=="M")	{
				if(settlemode.value=="-1")	{
					errMessage(settlemode,"Select Settlement type");
					return false;	
				}
				if( settlemode.value=="AUTO" )	{
					if(settletime.value==""){
						errMessage(settletime,"Enter Settlement time");
						return false;	
					}
				}
			}
			
			if(settlementon.value=="I")	{
				if(settlemode.value=="-1")	{
					errMessage(settlemode,"Select Settlement type");
					return false;	
				}
				if( settlemode.value=="AUTO" )	{
					if(settletime.value==""){
						errMessage(settletime,"Enter Settlement time");
						return false;	
					}
				}
				if(noofdays.value=="")	{
					errMessage(noofdays,"Enter Days interval");
					return false;
				}
			} 
		}
	}

</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
 
<s:form action="settlementConfigureActionSettlementProcess.do"  name="orderform" onsubmit="return searchRecords()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="90%"  align="center" >	 
	<tr>
		 
		 <s:hidden name='hidprocesstype' id='hidprocesstype'  value='%{processtype}' />
		 	<table border="0" cellpadding="0" cellspacing="4" width="30%" align="center"  >
		 	<tr>
		 		<td>
			 			<table border="0" cellpadding="0" cellspacing="0" width="50%" >
			 				<tr>
			 					<td colspan="2" style="text-align:center">
			 						<sx:autocompleter 
										name="mercahntid" id="mercahntid" autoComplete="false"  cssStyle="width:300px"
										list="merchlist"  
										listValue="NEWMERCHDESC"
										/>
			 					
			 					</td>
			 				</tr>
			 				<tr >
							<td style='text-align:center' >Settlement on </td> <td> : 
							<s:select headerKey="-1" headerValue="--Select  --" id="settleinterval"  name="settleinterval"  onchange="return hideOf(this.value)"
							list="#{'D':'DAILY','W':'WEEKLY','M':'MONTHLY','I':'INTERVEL-BASED'}"></s:select>
							</td>
							</tr>
							
							<tr id="tdsettlemode1" style="display:none">
								<td> Settle type  </td>
								<td > :
								<s:select  headerKey="-1" headerValue="--Select  --" id="settlemode"  name="settlemode"  onchange="return hideOfNew(this.value)"
								list="#{'AUTO':'AUTOMATIC','MAN':'MANUAL'}"></s:select>
								</td>
							</tr> 
							
							<tr style="display:none" id="trsettletime" >
								<td>  Settlement time  </td>
								<td > :
								<s:textfield name="settletime" id="settletime" placeholder="eg: HHMMSS" />
								</td>
							</tr> 
							
							
							<tr style="display:none" id="trnoofdays" >
								<td> Days interval  </td>
								<td > :
								<s:textfield name="noofdays" id="noofdays" />
								</td>
							</tr> 

 		
 			<tr>
 				<td> &nbsp; </td>
 			</tr>
		 	<tr>
		 		<td>
					<table border="0" cellpadding="0" cellspacing="0" width="20%" >
					<tr>
						<td> <s:submit value="Submit" name="order" id="order" onclick="return settlementhome_validation()" /> </td>
						<td><input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/></td>
					</tr>
					</table>
				</td>
			</tr> 
	
		 	
		 	</table>
		 </td>
	</tr> 
	
	
	<tr>
	<td>
		<div id="displayresult1">
		 &nbsp;
		</div>
	</td>
	</tr>
	
</table> 

</s:form>
 
 