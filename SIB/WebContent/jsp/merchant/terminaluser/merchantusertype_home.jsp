<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" /> 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>
	function getMerchantDetails(){
		 
		var merchantcode = document.getElementById("merchantcode");
		if( merchantcode.value == "" ){
			errMessage(merchantcode, "Merchant code should not be empty");
			return false;
		} 
		errMessage(merchantcode, "Processing....");
		var url = "getMerchantDetailsMerchantRegister.do?merchantcode="+merchantcode.value;
		var result = AjaxReturnValue(url);
		 
		var jsonobj = JSON.parse(result);
		if( jsonobj["RESPCODE"] != 0 ){
			errmsg.innerHTML=jsonobj["RESPREASON"];
			document.getElementById("usertypetable").style.display="none";
			return false;
		}else{
			errMessage(merchantcode, "");
			var storelist = document.getElementById("storelist");
			var storecount = jsonobj["STORECOUNT"];
		 
			var opt = "<option value='-1'> -SELECT- </option>";
			 for( var i=0; i<storecount; i++ ){
				 
				var key = "STOREKEY"+i;
				var storename = "STORENAME"+i;
				opt += "<option value='"+jsonobj[key]+"'> "+jsonobj[key] +"-"+ jsonobj[storename]+"</option>";
			}  
			storelist.innerHTML=opt;
			document.getElementById("merchantid").value = merchantcode.value;
			
			document.getElementById("firstname").innerHTML=jsonobj["FIRSTNAME"];
			document.getElementById("lastname").innerHTML=jsonobj["LASTNAME"];
			document.getElementById("merchname").innerHTML=jsonobj["MERCHANTNAME"];
			document.getElementById("usertypetable").style.display="table";
		}   
		 
	}
	function getTerminalList( storeid ){
		var merchantid = document.getElementById("merchantid").value;
		var url = "getTerminalListMerchantRegister.do?merchantid="+merchantid+"&storeid="+storeid;
		var result = AjaxReturnValue(url);
		 
		var jsonobj = JSON.parse(result);
		if( jsonobj["RESPCODE"] != 0 ){
			errmsg.innerHTML=jsonobj["RESPREASON"];
			document.getElementById("usertypetable").style.display="none";
			return false;
		}else{
			var terminallist = document.getElementById("terminalid");
			var terminalcount = jsonobj["TERMINALCOUNT"];
			 
			var opt = "<option value='-1'> -SELECT- </option>";
			 for( var i=0; i<terminalcount; i++ ){ 
				var key = "TERMINALKEY"+i;
				var termname = "TERMINALNAME"+i;
				 
				opt += "<option value='"+jsonobj[key]+"'>"+jsonobj[key] +"-"+ jsonobj[termname]+"</option>";
			}
			 terminallist.innerHTML=opt;
		}
	}
	function validateForm(){
		 
		var name = document.getElementById("name");
		var username = document.getElementById("username");
		var merchantusertype = document.getElementById("merchantusertype");
		var storelist  = document.getElementById("storelist");
		var terminalid  = document.getElementById("terminalid");
		if( storelist ){
			if( storelist.value =="-1" ){ errMessage(storelist, "Select Store");return false; }
		}
		if( terminalid ){
			if( terminalid.value =="-1" ){ errMessage(terminalid, "Select Terminal");return false; }
		}
		
		if( name ){
			if( name.value =="" ){ errMessage(name, "Name of the user is empty...");return false; }
		}
		if( username ){
			if( username.value =="" ){ errMessage(username, "Terminal Login Username  is empty...");return false; }
		}
		if( merchantusertype ){
			if( merchantusertype.value =="-1" ){ errMessage(merchantusertype, "Select User type ");return false; }
		}
	}
	
	function printPassword(){
		 var printContents = document.getElementById("printable").innerHTML;
	     var originalContents = document.body.innerHTML; 
	     document.body.innerHTML = printContents; 
	     window.print(); 
	     document.body.innerHTML = originalContents;   
	}
</script>

<style>
	div.divcont{
		clear:left;
	}
a {
	color: blue;
}
 
	@media print {
      body {
          width: 100%;
      }
	}
 
</style>
 

<jsp:include page="/displayresult.jsp"></jsp:include>
<table border='0' class="orderform" width="30%" align="center">
	<s:if test="mregbean.isuseradded">
	<tr><td colspan="2"> Do you want to print password for the user name <s:property value="mregbean.terminalusername"/> ? </td></tr>
		<s:form action="#">
			<s:hidden name="merchantid" value='%{mregbean.merchid}' />
			<s:hidden name="storeid" value='%{mregbean.storeid}' />
			<s:hidden name="terminalid" value='%{mregbean.terminalid}' />
			<s:hidden name="username" value='%{mregbean.terminalusername}' />
			<tr><td><s:submit name="submit" value="Yes" onclick="printPassword();"/> &nbsp; <input type="button" name="cancel" value="No" class="cancelbtn" onclick="confirmCancel()" /> </td> </tr>
			
			 
				<div id="printable" style="text-align:center; color: blue; border: 1px solid gray;display:none">
					<div style="padding:5px;">
					<div class="divcont"  style="text-align:left; color: blue;" > Merchant id  : <s:property value="mregbean.merchid"/>  </div>  
					<div class="divcont"  style="text-align:left; color: blue;" > Merchant Name  : <s:property value="mregbean.merchname"/>  </div> 
					<div class="divcont"  style="text-align:left; color: blue;" >  Store Name   : <s:property value="mregbean.storename"/> </div>
					<div class="divcont"  style="text-align:left; color: blue;" > Terminal id    : <s:property value="mregbean.terminalid"/>  </div>
					<div class="divcont"  style="text-align:left; color: blue;" >  User Name  :  <s:property value="mregbean.terminalusername"/> </div>
					<div class="divcont"  style="text-align:left; color: blue;" >   passowrd    : <s:property value="mregbean.terminaluserpassword"/></div>
				</div>  
			</div>
		</s:form>
	 </s:if>
</table>


<s:form action="savemerchantUserTypeMerchantRegister.do" name="orderform" onsubmit="return validateForm()"  autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="100%"  align="center">
		
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="4" width="100%" class="formtable"
					align="center">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="50%">
								<tr>
									<td colspan='2' style='text-align: center'>Mernchant Code 
									</td>
									<td> : <input type='text' name='merchantcode' id='merchantcode' /></td> 
									
									<td> <input type="button" name="get" value="Get Details" onclick="getMerchantDetails();"/>  </td>
									 
								</tr>
							 
							 </table>
							 
							 
							 <table border="0" cellpadding="0" cellspacing="0" width="70%" style="display:none" id="usertypetable"  >
							 	<input type="hidden" name="merchantcode" />
								<tr>
									<td class="textcolor" >Merchant Name : </td> <input type="hidden"  id="merchantid" name="merchantid" value=""/>
									 <td  style="text-align:left"  id="merchname"></td>  
									<td class="textcolor" >First Name : </td>	<td style="text-align:left" id="firstname"></td> 
								</tr>
								<tr>
									<td class="textcolor" >Last Name : </td>	<td style="text-align:left" id="lastname"  ></td>  
									<td class="textcolor" >Store List : </td>	<td style="text-align:left" id="storelistid"> <select name="storename" id="storelist" onchange="getTerminalList(this.value);"> </select></td> 
								
								</tr> 
								<tr>
									<td class="textcolor" >Terminal List : </td>	  
									 <td id="terminallistid"> <select name="terminalid" id="terminalid"> </select></td> 
								
								</tr> 
								
								<tr>		<td> &nbsp; </td>						</tr>
								
								<tr>
									<td class="textcolor" > Name of the user : </td> <td> <s:textfield name="name" id="name"/> </td>
									
									<td class="textcolor"> Terminal Login Username : </td> <td><s:textfield name="username" id="username"/> </td>
								</tr>
								<tr>	
									<td class="textcolor"> User type : </td>
									<td> <s:select list="%{mregbean.merchantusertypelist}" id="merchantusertype" name="merchantusertype"
						listKey="MASTERCODE" listValue="MASTERCODEDESC" headerKey="-1"
						headerValue="-SELECT-" /> </td>
								</tr>
								
								<tr>
									<td colspan="6"> <s:submit name="submit" value="Submit" /> </td>
								</tr>
							</table>

				</table>
			</td>
		</tr>


		<tr>
			<td>
				<div id="displayresult1">&nbsp;</div>
			</td>
		</tr>

	</table>

</s:form>

