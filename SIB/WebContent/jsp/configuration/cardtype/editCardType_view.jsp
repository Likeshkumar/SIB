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
</head>

<script>
function validateCardType(){
	
	var cardtypeidlen = document.getElementById("cardtypelenval").value;
	var cardtypeid = document.getElementById("cardtypeid");
	var cardtypedesc = document.getElementById("cardtypename");
	
	//alert("Validating...."+cardtypeid.value.length);
	
	if( cardtypeid.value == "" ){
		errMessage(cardtypeid, "Enter card type value ");
		return false;
	}else{
		if( cardtypeid.value.length != cardtypeidlen ){
			errMessage(cardtypeid, "Card type value length should be "+cardtypeidlen+" digit");
			return false;
		}
	}
	
	if( cardtypedesc.value == "" ){
		errMessage(cardtypedesc, "Enter card type name ");
		return false;
	}
	
	return true;
}

function putName ( cardtypeid )
{
	
	
	
	if( cardtypeid != -1 ){
		var url = "fchCardtypeDescCardTypeAction.do?cardtypeid="+cardtypeid;
		var result = AjaxReturnValue(url);
		document.getElementById("cardtypedesc").value = result;
	}else{
		document.getElementById("cardtypedesc").value = '';
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

function validateNumber (field,id,enteredchar)
{
	charvalue= "0123456789";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
    	
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
       	//alert(document.getElementById(id).value.charAt(i));
       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}
	 
</script>
<%String act = (String)session.getAttribute("act");%>
<body>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form name="cardtypeform" action="editsaveCardTypeCardTypeAction.do" autocomplete="off" >
<input type="hidden" name="act"  id="act" value="<%=act%>"> 
  
<div align="center">
 <table border="0" cellpadding="0" cellspacing="0" width="50%" class="table" align="center">
		
		<s:iterator value="cardtypelist">	
		<tr>
 			<td>CARD TYPE ID
 			</td> 
			<td><s:textfield name="cardtypeid" id="cardtypeid"  value="%{CARD_TYPE_ID}" readonly="true" onkeyup="validateNumber('CARD TYPE ID',this.id,this.value)" ></s:textfield></td> 
			 
		</tr> 
		
		<tr>
 			<td>CARD TYPE NAME
 			</td> 
			<%-- <td><s:textfield name="cardtypename" id="cardtypename" value="%{CARD_TYPE_DESC}" onkeyup="chkChars('CARD TYPE NAME',this.id,this.value)" ></s:textfield></td> --%> 
			 <td><s:textfield name="cardtypename" id="cardtypename" value="%{CARD_TYPE_DESC}"  ></s:textfield>
			 <s:fielderror fieldName="cardtypename" cssClass="errmsg" />
			 </td> 
			 
			 
		</tr> 
 		 
		</s:iterator>
</table>

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Update" name="submit" id="submit" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>

</div>
</s:form>

 
</body>
