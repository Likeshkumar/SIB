<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>

<script type="text/javascript" >
function chk_confirm(){
  return(confirm("Are You Sure"));
}
function check_form(){
 	 	var ip=document.addHSM.HSMADDRESS.value;
  		var filter = /^(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)$/;    	
		valid = true;
		
		var hsmipval = document.addHSM.HSMADDRESS.value.substr(0,1);
		var hsmportvalue = document.addHSM.HSMPORT.value.substr(0,1);
		var hsmtimeoutval = document.addHSM.HSMTIMEOUT.value.substr(0,1);
		var hsmconintervalvalue = document.addHSM.CONNECTIONINTERVAL.value.substr(0,1);
		var headlengthvalue = document.addHSM.HEADERLEN.value.substr(0,1);
		
        if ( document.addHSM.HSMNAME.value == "" ){
        	 errMessage(HSMNAME,"HSM NAME CANNOT BE EMPTY" );
        	 return false;
       	}else if( document.addHSM.HSMNAME.value.trim().length == 0){
       		errMessage(HSMNAME,"HSM NAME WILL NOT ALLOW WHITE SPACE" );
       	 	return false;
       	}else if( document.addHSM.HSMSTATUS.value == ""  ){
        	 errMessage(HSMSTATUS,"HSM STATUS CANNOT BE EMPTY");
    		 return false;
    	}else if( document.addHSM.HSMADDRESS.value == ""  ){	
        	errMessage(HSMADDRESS,"HSM IP ADDRESS CANNOT BE EMPTY" );
        	return false;
    	}else if (!filter.test(ip)){
	   	 	document.addHSM.HSMADDRESS.focus();
	   		return false;
    	 }else if(hsmipval == "0"){
    		errMessage(HSMADDRESS," HSM IP ADDRESS CANNOT START WITH ZERO " );
        	return false;
    	}else if( document.addHSM.HSMPORT.value == ""  ){	
        	errMessage(HSMPORT,"HSM PORT CANNOT BE EMPTY" );
        	return false;
    	}else if(hsmportvalue == "0"){
    		errMessage(HSMPORT," HSM PORT CANNOT START WITH ZERO " );
        	return false;
    	}else if( document.addHSM.HSMPROTOCOL.value == "-1"  ){	
        	errMessage(HSMPROTOCOL, "PLEASE SELECT HSM PROTOCOL" );
        	return false;
    	}else if( document.addHSM.HSMTYPE.value == "-1"  ){	
        	errMessage(HSMTYPE,"PLEASE SELECT HSM TYPE" );
        	return false;
    	}else if( document.addHSM.HEADERTYPE.value == "-1"  ){	
        	errMessage(HEADERTYPE,"PLEASE SELECT HEADER TYPE" );
        	return false;
    	}else if( document.addHSM.HSMHEADERLEN.value == "-1"  ){	
        	errMessage(HSMHEADERLEN,"PLEASE SELECT HSM HEADER LENGTH" );
        	return false;
    	}else if( document.addHSM.HSMTIMEOUT.value == ""  ){	
        	errMessage(HSMTIMEOUT,"HSM TIMEOUT CANNOT BE EMPTY" );
        	return false;
    	}else if(hsmtimeoutval == "0"){
    		errMessage(HSMTIMEOUT," HSM TIMEOUT CANNOT START WITH ZERO " );
        	return false;
        }else if( document.addHSM.CONNECTIONINTERVAL.value == ""  ){	
        	errMessage(CONNECTIONINTERVAL,"HSM CONNECTION INTERVAL CANNOT BE EMPTY" );
        	return false;
    	}else if(hsmconintervalvalue== "0"){
    		errMessage(CONNECTIONINTERVAL,"HSM CONNECTION INTERVAL CANNOT START WITH ZERO " );
        	return false;
    	}else if( document.addHSM.HEADERLEN.value == ""  ){	
    		errMessage(HEADERLEN,"HEADER LENGTH CANNOT BE EMPTY" );
        	return false;
    	}else if(headlengthvalue == "0"){
    		errMessage(HEADERLEN,"HEADER LENGTH CANNOT START WITH ZERO " );
        	return false;
    	}
    	var r=confirm("Do You Want To Submit");
    	if(r){
			return true;
		}else{
    		return false;
    	}	
	}

function goBack(){
	window.history.back(-1)
}
function checkipformat(){
	var ip=document.addHSM.HSMADDRESS;
	/*
	var partern="^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
	var ip=document.addHSM.hsmport.value;
	if(ip.match(partern))
		{
		alert(partern);
		}
	else alert(ip);
	*/
	if(!validateIP(ip.value))
	{
		errMessage(HSMADDRESS,"IP ADDRESS IS INVALID" );
		return false;
	}
}
</script>	
<jsp:include page="/displayresult.jsp"/>

<s:form name="addHSM"  action="updateHsmAction" autocomplete="off">
<table align="center" border="0"  cellspacing="5" cellpadding="0" width="80%" class="formtable">
<s:iterator value="beans.hsmdetails">
		<tr>
	 		 <td>HSM NAME</td><td><s:textfield name="HSMNAME" id="HSMNAME"  value="%{HSMNAME}"  maxlength="15"   onkeypress="return alphanumerals(event);"></s:textfield></td>
			 <td>HSM STATUS</td><td><s:radio name="HSMSTATUS" id="HSMSTATUS" list="#{'1':'ENABLE','0':'DISABLE'}" value="%{HSMSTATUS}"></s:radio></td>		   
		</tr>
	   
  		 <tr>
		 	 <td>HSM IP ADDRESS</td><td><s:textfield name="HSMADDRESS" value="%{HSMADDRESS}" id="HSMADDRESS" onkeypress='return numberOnlyWithdot(event)' onchange="checkipformat(this);" maxlength="15"></s:textfield></td>
		 	 <td>HSM PORT </td><td><s:textfield name="HSMPORT" id="HSMPORT" value="%{HSMPORT}" onkeypress='return numerals(event)' maxlength="4"></s:textfield></td>
		  </tr>
  
		  <tr>
		 	 <td>HSM PROTOCOL</td><td><s:select name="HSMPROTOCOL" id="HSMPROTOCOL" headerKey="-1" headerValue="--Select--" list="#{'TCP/IP':'TCP/IP','SERIAL':'SERIAL'}" value="%{HSMPROTOCOL}"></s:select></td>
		 	 <td>HSM TYPE </td><td><s:select name="HSMTYPE" id="HSMTYPE"  headerKey="-1" headerValue="--Select--" list="#{'THALES':'THALES','SEFENET':'SAFENET'}" value="%{HSMTYPE}"></s:select></td>
		  </tr>
		  
		  <tr>
		   	 <td>HEADER TYPE </td><td><s:select name="HEADERTYPE" id="HEADERTYPE" headerKey="-1" headerValue="--Select--" list="#{'ASCII':'ASCII','HEX':'HEX'}"  value="%{HEADERTYPE}"></s:select></td>
		 	 <td>HSM HEADER LENGTH</td><td><s:select name="HSMHEADERLEN" id="HSMHEADERLEN" headerKey="-1" headerValue="--Select--" list="#{'2':'2','4':'4'}"  value="%{HSMHEADERLEN}"> </s:select></td>
		 	
		  </tr>
		
			<tr>
		 	 <td>HSM TIMEOUT</td><td><s:textfield name="HSMTIMEOUT" id="HSMTIMEOUT" onkeypress='return numerals(event)' value="%{HSMTIMEOUT}" maxlength="2"></s:textfield></td>
		 	 <td>HSM CONNECTION INTERVAL </td><td><s:textfield name="CONNECTIONINTERVAL" id="CONNECTIONINTERVAL"  value="%{CONNECTIONINTERVAL}" onkeypress='return numerals(event)' maxlength="2"></s:textfield></td>
		  </tr>
		
		  <tr>
		 	
		 	 <td>HEADER LENGTH </td><td><s:textfield name="HEADERLEN" id="HEADERLEN" value="%{HEADERLEN}" onkeypress='return numerals(event)' maxlength="2"></s:textfield></td>
		 	 <s:hidden name="HSM_ID" id="HSM_ID" value="%{HSM_ID} "></s:hidden>
		  </tr>
</s:iterator>
	</table>
	<br>
	<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
		<tr align="center">
			<td>
				<%-- <s:submit name="submit" value="Update" onclick="return check_form();" ></s:submit> --%>
					<s:submit name="submit" value="Update"></s:submit>
			</td>
			<td>
				<input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" />
			</td>
		</tr>	
	</table>
</s:form>



