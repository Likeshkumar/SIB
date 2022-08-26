<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

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

window.history.forward();
function noBack() { window.history.forward(); }

</script>




 
<BODY onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">

<jsp:include page="/displayresult.jsp"></jsp:include>		
<s:form name="licenseform" action="encryptLicenceManagerAction"  autocomplete="off">	
		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">										
			<tr>
				<td>ENTER INSTITUTION ID<b class="mand">*</b></td>
				<td align="center"> : </td> 
				<td><s:textfield name="inst_id" id="inst_id" maxlength="6" onkeyup="chkChars('INSTITUTION ID',this.id,this.value)" ></s:textfield></td>
			<tr>
		</table>
	    <div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="20%" >	
				<tr align="center">
					<td> <input type="submit"  name="Submit" id="Submit"  value="Submit"onclick="return form_check();"/></td>
					<td>
						<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
					</td>
				</tr>
			</table>
		</div>
</s:form>



