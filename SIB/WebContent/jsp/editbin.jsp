<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" >
function numberOnly(evt){
		var keyvalue=evt.charCode? evt.charCode : evt.keyCode
	//	var keyvalue=evt.charCode;
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
function aks_confermation()
{
var t=confirm("Do you want to reset");
return t;
}

function validate_form( )
{
	var deslength=document.editbinform.deslength.value;
	//alert(deslength);
	var binn=document.editbinform.bin.value;
    valid = true;
    var t=confirm("Do you want to Submit");
   if(t==true)
		{
				    if ( binn == "" )
				   		 {
				    	 alert ( "'BIN CANNOT BE EMPTY'" );
				    	 document.getElementById("bin").focus();
						 return false;
				    	
				   		}
				    
				   if (binn.length<6 || binn.length>6  )
					{
				    	
						alert ("'BIN SHOULD BE 6 DIGIT'");
				    	 document.getElementById("bin").focus();
						 return false;
					}
				    
					 if ( document.editbinform.product.value == "-1" )
				    {	
				    	 alert ( "'PRODUCT CANNOT BE EMPTY' " );
				    	 document.getElementById("product").focus();
				    	 return false;
				   		 
					}
				    
				    if(document.editbinform.chnlen.value == "" )
					{
						alert("Please Select CHN Length");
						 document.getElementById("chnlen").focus();
						return false;
						
					}
				    
				    if ( document.editbinform.hsmid.value == "-1" )
				    {	
				    	 alert ( "'PLEASE SELECT HSM' " );
				    	 document.getElementById("hsmid").focus();
				    	 return false;
				   		 
					}
				    
				    if ( document.editbinform.deslength.value == "-1" )
				    {	
				    	 alert ( "'PLEASE SELECT PIN Encryption Key length' " );
				    	 document.getElementById("deslength").focus();
				    	 return false;
				   		 
					}
				    if ( document.editbinform.panoffset.value == "" )
					{	
						 alert ( "'PANOFFSET CANNOT BE EMPTY' " );
						 document.editbinform.panoffset.value="";
						 document.getElementById("panoffset").focus();
						 return false;
							 
					} 
				    if ( document.editbinform.panvalidationlenght.value == "" )
				    {	
				    	 alert ( "'panvalidation LENGHT CANNOT BE EMPTY' " );
				    	 document.getElementById("panvalidationlenght").focus();
				    	 return false;
				   		 
					}
				    
					if ( document.editbinform.pinlenght.value == "" )
				    {	
				    	 alert ( "'PIN LENGHT CANNOT BE EMPTY' " );
				    	 document.getElementById("pinlenght").focus();
				    	 return false;
				   		 
					}
						    
					if ( document.editbinform.pinlenght.value<4 )
						{	
							 alert ( "'PIN LENGHT SHOULD BE MINIMUM 4' " );
							// document.editbinform.pinlenght.value="";
							 document.getElementById("pinlenght").focus();
							 return false;
						}
					   
					 if ( document.editbinform.pinoffsetlenght.value == "" )
						{	
							 alert ( "'PINOFFSET LENGHT CANNOT BE EMPTY' " );
							 document.editbinform.pinoffsetlenght.value="";
							 document.getElementById("pinoffsetlenght").focus();
							 return false;
								 
						}
					 if ( document.editbinform.decimilisation_table.value == "" )
						{	
							 alert ( "'decimilisation table CANNOT BE EMPTY' " );
							 document.editbinform.decimilisation_table.value="";
							 document.getElementById("decimilisation_table").focus();
							 return false;
								 
						}
					 if ( document.editbinform.decimilisation_table.value.length<16 )
						{	
							 alert ( "'decimilisation table value should be 16 digits' " );
							 document.editbinform.decimilisation_table.value="";
							 document.getElementById("decimilisation_table").focus();
							 return false;
								 
						}
					if ( document.editbinform.panpadchar.value == "" )
						{	
							 alert ( "'PAN PAD CHARACTER CANNOT BE EMPTY' " );
							 document.editbinform.panpadchar.value="";
							 document.getElementById("panpadchar").focus();
							 return false;
								 
						}
					if ( document.editbinform.pinmailer.value == "-1" )
					{	
						 alert ( "'PINMAILER CANNOT BE EMPTY' " );
						 document.editbinform.pinmailer.value="";
						 document.getElementById("pinmailer").focus();
						 return false;
							 
					}
					if(document.editbinform.pin_methodIBMDES.checked)
					{
						if ( document.editbinform.pvk.value == "" )
							{	
								 alert ( "'PVK CANNOT BE EMPTY' " );
								 document.getElementById("pvk").focus();
								 return false;
							}
						if(document.editbinform.pvk.value.length != deslength)
						 {
								 alert ( "'PVK lenght Should be "+deslength+" digit");
								 document.getElementById("pvk").focus();
								 return false;
						 }	
						 
						
					}
					if(document.editbinform.pin_methodVISA.checked)
					{
						if ( document.editbinform.pvki.value == "" )
						{	
							 alert ( "'PVK INDEX CANNOT BE EMPTY' " );
							 document.getElementById("pvki").focus();
							 return false;
								 
						}
							
						if ( document.editbinform.pvk1.value == "" )
						{	
							 alert ( "'PVK1 CANNOT BE EMPTY' " );
							 document.getElementById("pvk1").focus();
							 return false;
								 
						}
						
						if(document.editbinform.pvk1.value.length != deslength)
						 {
								 alert ( "'PVK1 lenght Should be "+deslength+" digit");
								 document.getElementById("pvk1").focus();
								 return false;
						 }
						
						if ( document.editbinform.pvk2.value == "" )
							{	
								 alert ( "'PVK2 CANNOT BE EMPTY' " );
								 return false;
							}
						
						if(document.editbinform.pvk2.value.length != deslength)
						 {
								 alert ( "'PVK2 lenght Should be "+deslength+" digit");
								 document.getElementById("pvk2").focus();
								 return false;
						 }
					
					}
					if(document.editbinform.cvv_reqY.checked)
						{
							 if ( document.editbinform.cvv1.value == "" )
								{	
									 alert ( "'CVV1  CANNOT BE EMPTY' " );
									 return false;
										 
								}
							 if(document.editbinform.cvv1.value.length != deslength)
							 {
									 alert ( "'CVV1 lenght Should be "+deslength+" digit");
									 document.getElementById("cvv1").focus();
									 return false;
							 }
							 
							 if ( document.editbinform.cvv2.value == "" )
								{	
									 alert ( "'CVV2  CANNOT BE EMPTY' " );
									 return false;
										 
								}
							 if(document.editbinform.cvv2.value.length != deslength)
							 {
									 alert ( "'CVV2 lenght Should be "+deslength+" digit");
									 document.getElementById("cvv2").focus();
									 return false;
							 }
							 
							 
							 
						}
				     return valid;
		}
   else return t;
}

function set_baselen()
{
	var len=document.getElementById("chnlen").value;
	var brcodelength=document.getElementById("brcodlen").value;
	alert (brcodelength);
	if(document.editbinform.chnlen.value == "" )
	{
		alert("Please Select CHN Length");
		return 0;
		
	}
	len=len-7; // for checksum and bin
	if(document.getElementById("attach_prodtype_cardtypeC").checked)
	{
		len=len-4;// for card type
		if(document.getElementById("attach_brcodeY").checked)
			{
			len=len-brcodelength;//for branch code
			}
				
	}
	
	if(document.getElementById("attach_prodtype_cardtypeP").checked)
	{
		len=len-2;// for card type
		 if(document.getElementById("attach_brcodeY").checked)
			{
			len=len-brcodelength;//for branch code
			}
				
	}
	
	if(document.getElementById("attach_prodtype_cardtypeN").checked)
	{
		 if(document.getElementById("attach_brcodeY").checked)
			{
			len=len-brcodelength;//for branch code
			}
				
	}
	
	var no=10,i=0;
	no=no--;
	document.editbinform.baselen.value=(len);
	for (i=0;i<(len-1);i=i+1)
		{
			no=no*10;
		}
	no-=1;
	document.editbinform.nos_cards_gen.value=(no);
	alert ("YOU CAN GENERATE '"+(no)+"' CARDS ");
	
}

function setmethod()
{
	var ibmdes=document.getElementById("pin_methodIBMDES");
	var visa=document.getElementById("pin_methodVISA");
	var pvk=document.getElementById("pvk");
	var pvki=document.getElementById("pvki");
	var pvk1=document.getElementById("pvk1");
	var pvk2=document.getElementById("pvk2");
	
	if(document.editbinform.pin_methodIBMDES.checked)
	{
		pvki.value="";
		pvk1.value="";
		pvk2.value="";
		pvk.value=1;
		pvk.readOnly=false;
		pvki.readOnly=true;
		pvk1.readOnly=true;
		pvk2.readOnly=true;
	
	}
	
	
	if(document.editbinform.pin_methodVISA.checked)
	{
		pvki.value=1;
		pvk1.value=1;
		pvk2.value=1;
		pvk.value="";
		pvk.readOnly=true;
		pvki.readOnly=false;
		pvk1.readOnly=false;
		pvk2.readOnly=false;

	
	}
}

function setcvvreg()
{
	var cvv1=document.getElementById("cvv1");
	var cvv2=document.getElementById("cvv2");
	if(document.editbinform.cvv_reqY.checked)
		{
		cvv1.readOnly=false;
		cvv2.readOnly=false;
		cvv1.value=1;
		cvv2.value=1;
		
		}
	if(document.editbinform.cvv_reqN.checked)
		{
			cvv1.value="";
			cvv2.value="";
			cvv1.readOnly=true;
			cvv2.readOnly=true;
			
		}
}


var httpObject = null;

//Get the HTTP Object
function getHTTPObjectForBrowser(){
      if (window.ActiveXObject)
          return new ActiveXObject("Microsoft.XMLHTTP");
      else if (window.XMLHttpRequest) return new XMLHttpRequest();
      else {
          alert("Browser does not support AJAX...........");
      return null;
}
}
//Change the value of the outputText field
function setAjaxOutput(){
  if(httpObject.readyState == 4){
  	
  	alert(httpObject.responseText);
      document.getElementById('ajax').innerHTML = httpObject.responseText;
      
  }
}
//Implement business logic
  function doAjaxCall(){
		
	//alert ("SURESH");
 		 var inst_id=document.getElementById('instid').value;
 		 var bin_id=document.getElementById('bin').value;
 		 var product=document.getElementById('product').value;
 		 
 		
      httpObject = getHTTPObjectForBrowser();
      if (httpObject != null) {
      	// alert(document.getElementById('product').value);
      	//alert(bin_id);       	 
          httpObject.open("GET", "ajax_BinLicence_HandlerAddBinAction.do?inst_id="+inst_id+"&bin="+bin_id+"&product="+product, true);
          
          httpObject.send(null);
     
      httpObject.onreadystatechange = setAjaxOutput;
    
  }
}


</script>


	<table border="0" cellpadding="0" cellspacing="0" width="90%"><tr> <td  align="center"> 
		<tr align="center">
			<td colspan="2">
				<%
					String editBinMessage = null;
					editBinMessage = (String) session.getAttribute("editBinMessage");
					session.removeAttribute("editBinMessage");
					String editBinErrStat = null;
					editBinErrStat = (String) session.getAttribute("editBinErrStat");
					session.removeAttribute("editBinErrStat");
				%>
				<%
					if (editBinMessage != null) 
					{
				%>
					<font color="Red"><b><%=editBinMessage%></b></font>
				<%
					} 
				%>	
			</td>
		</tr>
	</table>
		<%
			if(editBinErrStat != null)
			{
				if(!(editBinErrStat.equals("E")))
					{
		%>



<div align="center" ><B><font color="blue">  BIN EDIT </font></B></div>
<br><br>
<s:form name="editbinform" action="saveEditBinAddBinAction" autocomplete="off">
<s:iterator  value="bindetail"  >
<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="90%" >
 <tr align="center">
  <td>
	 <fieldset style="width:800px; height: 150px;">
	    <legend><b>BIN DETAILS:</b></legend>
	     <table border="0" cellpadding="0" cellspacing="0" width="60%" >
					<tr align="center"><td colspan="4">
					<b><font color="red">
					<div id="ajax">
					</div>
					</font></b>
					</td></tr>
					    	
		    	
			<tr>
					<td> <s:if test="%{#message !=''}">
					<td></td>
					<td align="center"><font color="red"><s:property value="message"></s:property></font></td>
					</s:if>
			</tr>
			
			
		<tr><td><br></td></tr>
		    	
		    	<tr>
					<td>INSTID</td>
					<td>		
								<h4><s:textfield label="INSTID" readonly="true" maxlength="10"
											name="instid" id="instid"/>
								</h4>
					</td>
				</tr>
				<tr>
					<td> BIN</td>
					<td><s:textfield name="bin" id="bin" onkeypress='return numberOnly(event)'  maxlength="6" value="%{BIN}" ></s:textfield></td>
			   </tr>
 	
 				<tr>
 					<td>ADD PRODUCT </td>
		    		<td>	
		    	 		<h4>
						<s:select label="Select a Product"  
						headerKey="-1" headerValue="--- Select Product ---"
						list="productlist" 
						listValue="%{productid}"
		    			listKey="%{productname}" 
		   				name="product" 
		   				id="product"
		   				value="%{PRODUCT_ID}"
						onchange="doAjaxCall();"
						/>
						</h4>
					 </td>
		
				</tr>
		
	    </table>
	 </fieldset>
   </td>
 </tr>
 <tr align="center">
	 <td>
		 <fieldset style="width: 800px; height: 200px;" >
		    <legend><b>CHN BREAK UP:</b></legend>
		    <table><tr><td>&nbsp;</td></tr></table>
		   <table border="0" cellpadding="0" cellspacing="0" width="80%" >
		       <tr>
					<td>CHN LENGTH </td><td>: <s:select headerKey="" headerValue="--Select CHN LENGTH --" id="chnlen"  name="chnlen"   list="#{'16':'16 DIGIT','19':'19 DIGIT' }" onchange="set_baselen();"  value="%{CHNLEN}"></s:select> </td>		
			</tr>
			<tr>
					<td align="left"> CARD TYPE  </td>
					<td>: <s:radio id="attach_prodtype_cardtype"  name="attach_prodtype_cardtype" list="#{'C':'CARD TYPE','N':'NO'}" onclick="set_baselen();" value="%{ATTACH_PRODTYPE_CARDTYPE}"></s:radio></td>
			</tr>
			
			<tr>
					<td align="left">ATTACH  BRANCH CODE  </td>
					<td>: <s:radio id="attach_brcode"  name="attach_brcode" list="#{'Y':'YES','N':'NO'}" onclick="set_baselen();"  value="%{ATTACH_BRCODE}" ></s:radio>
						 
			</tr>
			
			<tr>
				<td>BASE LENGTH </td><td>: <s:textfield id="baselen"  name="baselen" readonly="true" value="%{BASELEN}"></s:textfield> </td>		
			</tr>
			
			<tr>
				<td>NO CARDS CAN BE GENERATED </td><td>: <s:textfield id="nos_cards_gen"  name="nos_cards_gen" readonly="true" value="%{NOS_CARDS_GEN}"></s:textfield> </td>		
			</tr>
			
			
			
		    </table>
		  </fieldset>
	 </td>
 </tr>
 <tr align="center">
	 <td>
	 <fieldset style="width: 800px; height: 350px;">
	    <legend><b>SECURITY PARAMETERS:</b></legend>
	      
	      
	      <table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
	    
		   <tr>
		
					<td style="width:40px" align="left">SELECT HSM </td>
					<td style="width:60px" align="center">  
						<s:select label="HSM"  
						headerKey="-1" headerValue="--Select THE HSM --"
						list="hsmlist" 
						listValue="%{hsmid}"
		    			listKey="%{hsmname}" 
		    			id="hsmid" 
						value="%{HSM_ID}"
		   				name="hsmid" style="width:170px" />
					</td>
			
					
					<td style="width:60px" align="left">PIN Encryption Key length  </td>
					<td style="width:40px" align="center">  
						<s:select label="deslength"  
						headerKey="-1" headerValue="--Select The Length --"
						list="#{'16':'Single Length -> 16 ','32':'Double Length-> 32','48':' Triple Length -> 48' }" 
						id="deslength" 
						value="%{DESLENGTH}"
		   				name="deslength" style="width:170px"/>
					</td>
			<tr>
					<td align="left">PAN VALIDATION<br>OFFSET</td>
					<td align="center"><s:textfield value="%{PAN_OFFSET}" name="panoffset" id="panoffset" onkeypress='return  numberOnly(event)'  maxlength="1" style="width:170px"></s:textfield></td>
					<td align="left">PAN VALIDATION <br>LENGHT</td>
					<td align="center"><s:textfield value="%{PANVALIDATION_LENGTH}" name="panvalidationlenght" id="panvalidationlenght" onkeypress='return  numberOnly(event)'  maxlength="2" style="width:170px" ></s:textfield></td>
					
			</tr>
			<tr>
					<td align="left">PIN LENGHT</td>
					<td align="center"><s:textfield value="%{PIN_LENGTH}" name="pinlenght" id="pinlenght" onkeypress='return  numberOnly(event)'  maxlength="1" style="width:170px"></s:textfield></td>
					<td align="left">PIN OFFSET <br>LENGHT</td>
					<td align="center"><s:textfield value="%{PINOFFSET_LENGTH}" name="pinoffsetlenght" id="pinoffsetlenght" onkeypress='return  numberOnly(event)'  maxlength="1" style="width:170px"></s:textfield></td>
			
					
			</tr>
			<tr>
					<td align="left">DECIMILISATION TABLE</td>
					<td align="center"><s:textfield value="%{DECIMILISATION_TABLE}" name="decimilisation_table" id="decimilisation_table" onkeypress='return numberOnly(event)'  maxlength="1"  style="width:170px" ></s:textfield></td>
			
					<td align="left">PAN PAD CHARACTER </td>
					<td align="center"><s:textfield name="panpadchar" id="panpadchar"  maxlength="1" value="%{PIN_PAD_CHAR}"  style="width:170px"></s:textfield></td>
						
			</tr>
			<tr>
					<td align="left" style="width:150px" >PIN GENERATION TYPE</td>
					
					<td colspan="1" align="center"><s:radio value="%{GEN_METHOD}" name="pin_method"  id="pin_method" onclick="return setmethod();" list="#{'IBMDES':'IBMDES','VISA':'VISA'}" >   </s:radio> </td>
				    <td align="left" style="width:150px" >PINMAILER</td>
						
					</td>
					
					<td align="center">  
						<s:select label="Pinmailer"  
						headerKey="-1" headerValue="Select"
						list="pinmailerlist" 
						listValue="%{pinmailername}"
		    			listKey="%{pinmailerid}" 
		    			value="%{PINMAILER_ID}"
		    			id="pinmailer" 
		   				name="pinmailer" style="width:170px"  />
					</td>								
			</tr>
			<tr>
					<td align="left">PVK INDEX</td>
					<td align="center"><s:textfield value="%{PVKI}" name="pvki" id="pvki" onkeypress='return  numberOnly(event)'  maxlength="2" style="width:170px"  ></s:textfield></td>
					
					<td align="left">PVK1</td>
					<td align="center"><s:textfield value="%{PVK1}" name="pvk1" id="pvk1" onkeypress='return numberOnly(event)'  maxlength="48" style="width:170px"   ></s:textfield></td>
			</tr>
			<tr>
					<td align="left">PVK2</td>
					<td align="center"><s:textfield value="%{PVK2}" name="pvk2" id="pvk2" onkeypress='return numberOnly(event)'  maxlength="48" style="width:170px"   ></s:textfield></td>
					
					<td align="left">PVK</td>
					<td align="center"><s:textfield value="%{PVK}" name="pvk" id="pvk" onkeypress='return numberOnly(event)'  maxlength="48" style="width:170px"  ></s:textfield></td>
			
			</tr>
			
			<tr>
					<td align="left" style="width:150px" >CVV REQUIRED</td>
					<td colspan="1" align="center"><s:radio value="%{CVV_REQUIRED}" name="cvv_req"  id="cvv_req" onclick="return setcvvreg();" list="#{'Y':'YES','N':'NO'}">   </s:radio> </td>
					
			</tr>
			
			<tr>
					<td align="left">CVV1</td>
					<td align="center"><s:textfield value="%{CVVK1}" name="cvv1" id="cvv1" onkeypress='return numberOnly(event)'  maxlength="48" style="width:170px"  ></s:textfield></td>
					
					<td align="left">CVV2</td>
					<td align="center"><s:textfield value="%{CVVK2}" name="cvv2" id="cvv2" onkeypress='return numberOnly(event)'  maxlength="48" style="width:170px" ></s:textfield></td>
			
			</tr>
			
			<s:hidden name="brcodlen"  id="brcodlen" value="%{brcodlen}"></s:hidden>
			
			
		 </table>
	  </fieldset>
	 </td>
 </tr>
</table>
</div>
</s:iterator>
<div align="center">
<table border="0" cellpadding="0" cellspacing="0" width="25%" align="center">
	<tr align="center">
		<td><s:submit value="Submit" name="submit" onclick="return validate_form ( )" /> </td>     
		<td><s:reset name="reset" onclick="return aks_confermation();"></s:reset></td>
	</tr>
</table>
</div>
</s:form>
<% }  }%>
 
