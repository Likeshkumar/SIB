<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<head>
	<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
	<script type="text/javascript" src="js/script.js"></script>
	
		<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>

	<script type="text/javascript">
	function putName ( cardtypeid )
	{
		//alert("cardtype==> "+cardtypeid);
		if( cardtypeid != -1 ){
			var url = "fchCardtypeDescCardTypeAction.do?cardtypeid="+cardtypeid;
			var result = AjaxReturnValue(url);
			//alert("result"+result);
			document.getElementById("cardtypedesc").value = result;
		}else{
			document.getElementById("cardtypedesc").value = '';
		}
	}
	
	function validate_form ( )
	{
		//var deployment=document.ProductAddFofm.deployment;
		var globcardtype = document.ProductAddFofm.globcardtype;
		var cardtypedesc = document.ProductAddFofm.cardtypedesc;
		var prodsubtype = document.ProductAddFofm.prodsubtype;
		var bin = document.ProductAddFofm.bin;
		var glcode = document.ProductAddFofm.glcode;

		valid = true;

		if( globcardtype ){ 
			if ( globcardtype.value == "-1" )
			 {
			 errMessage(globcardtype,"Please Select Card Type Name");
			 return false;
			 }
		}
		if( bin ){ 
			 if( bin.value == "-1" )
			 {
			  errMessage(bin,"Please Select BIN");
			  return false;		
			 }
		}
		if( cardtypedesc )
		{ 
			if ( cardtypedesc.value == "" )
			{
			errMessage(cardtypedesc,"Enter Product Name");
			return false;
		    }
		}
		if( prodsubtype ){ 
			if ( prodsubtype.value == "" )
			{
	    	errMessage(prodsubtype,"Please Enter No Of Subtype");
			return false;
			}
		}
		if( glcode ){ 
		   if( glcode.value == "-1" )
		    {
		    errMessage(glcode,"Please Select GL");
			return false;	
			}
		}
		
		if( confirm("Do you want to continue ") ){
			return valid;
		}else{
			return false;
		}
		
	  }
	function chkChars(field,id,enteredchar)
	{
		//alert('1:'+field+'2:'+id+'3:'+enteredchar);
		charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
		//str = document.getElementById(id).value;
	    for (var i = 0; i < document.getElementById(id).value.length; i++) {
	       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
	    	//alert(document.getElementById(id).value.charAt(i));   
	    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
	    	document.getElementById(id).value = '';
	    	return false;
	    	}
	    }
	}
	
	
	</script>
</head>
<body>
<%String act = (String)session.getAttribute("act"); %>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="ProductAddFofm" action="%{productbean.formaction}" autocomplete="off" onsubmit="parent.showprocessing()"> <!-- savedataAddMainProductAction -->
<input type="hidden" name="act"  id="act" value="<%=act%>"> 

<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="center">
 	<s:hidden name="binmap" value="%{binmap}" id="binmap"/>
 	<s:hidden name="cardtypemap" value="%{cardtypemap}" id="cardtypemap"/>
		  
		
 		<tr>  
			<td>Card Type</td>
			<td> : <s:select label="-Select-" 
				headerKey="-1" headerValue="--- Select ---"
				list="cardtypelist" 
				listValue="CARD_TYPE_DESC" onchange="putName ( this.value )"
 				listKey="CARD_TYPE_ID" 
   				name="globcardtype" id="globcardtype"
   				value = "%{productbean.cardtypeid}"
   				 /><s:fielderror fieldName="globcardtype" cssClass="errmsg" />
			</td>
		</tr>
		
		<tr>
			<td>BIN</td>
			<td> : <s:select label="-Select-" 
				headerKey="-1" headerValue="--- Select ---"
				list="binlist" 
				listValue="BIN_DESC"
    			listKey="BIN" 
    			value = "%{productbean.bin}"
   				name="bin" id="bin" />
   				<s:fielderror fieldName="bin" cssClass="errmsg" />
   				</td>
		</tr> 
 		
 		<tr>
			<td>Product Name</td>
			 <td> : <s:textfield name="cardtypedesc" id="cardtypedesc"  value = "%{productbean.productname}"  onkeyup="chkChars('Product Name',this.id,this.value)"/> 
		<%-- <td> : <s:textfield name="cardtypedesc" id="cardtypedesc" onkeyup="chkChars('Product Name',this.id,this.value)"/> --%>
			<s:fielderror fieldName="cardtypedesc" cssClass="errmsg" /></td>	
		</tr>
		
		<tr>
		<td>
			<s:hidden name="prodsubtype" id="prodsubtype" onkeypress="return numerals(event);" maxlength="2" value="99" />
		
			<s:hidden name="glcode" id="glcode"  onkeypress="return numerals(event);"  value="0000" />
		
		</td>
		</tr>
		
		  
		
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<%-- <s:submit value="Submit" name="submit" id="submit" onclick="return validate_form();"/> --%>
			 <s:submit value="Submit" name="submit" id="submit"/> 
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>


</div>
</s:form>

 
</body>
