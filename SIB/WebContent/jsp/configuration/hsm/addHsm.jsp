<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
    <link rel="stylesheet" type="text/css" href="style/calendar.css"/>
    <script type="text/javascript" src="js/script.js"></script>
    
<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
    
    <script type="text/javascript" >
    function checkipformat()
    {
    	var ip=document.addHSM.hsmip;
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
    		errMessage(hsmip,"IP ADDRESS IS INVALID" );
    		return false;
    	}
    }
    
    
    
    
    function validate_form(){
 	 	var ip=document.addHSM.hsmip.value;
  		var filter = /^(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)\.(([1-9][0-9]{0,2})|0)$/;    	
		valid = true;
		var hsmtimeoutval = document.addHSM.hsmtimeout.value.substr(0,1);
		var hsmconintervalvalue = document.addHSM.hsmconinterval.value.substr(0,1);
		var headlengthvalue = document.addHSM.headlength.value.substr(0,1);
		var hsmportvalue = document.addHSM.hsmport.value.substr(0,1);
		var hsmipval = document.addHSM.hsmip.value.substr(0,1);		
       
		if ( document.addHSM.hsmname.value == "" ){
        	 errMessage(hsmname,"HSM NAME CANNOT BE EMPTY" );
        	 return false;
       	}
		else if( document.addHSM.hsmname.value.trim().length == 0){
       		errMessage(hsmname,"HSM NAME WILL NOT ALLOW WHITE SPACE" );
       	 	return false;
       	}else if( document.addHSM.hsmstatus.value == ""  ){
        	 errMessage(hsmstatus,"HSM STATUS CANNOT BE EMPTY");
    		 return false;
    	}else if( document.addHSM.hsmip.value == ""  ){	
        	errMessage(hsmip,"HSM IP ADDRESS CANNOT BE EMPTY" );
        	return false;
    	}else if (!filter.test(ip)){
    		errMessage(hsmip,"HSM IP ADDRESS IS INVALID" );
	   		return false;
    	 }else if(hsmipval == "0"){
    		errMessage(hsmip," HSM IP ADDRESS CANNOT START WITH ZERO " );
        	return false;
    	}else if( document.addHSM.hsmport.value == ""  ){	
        	errMessage(hsmport,"HSM PORT CANNOT BE EMPTY" );
        	return false;
    	}else if(hsmportvalue == "0"){
    		errMessage(hsmport," HSM PORT CANNOT START WITH ZERO " );
        	return false;
    	}else if( document.addHSM.hsmprotocol.value == "-1"  ){	
        	errMessage(hsmprotocol, "PLEASE SELECT HSM PROTOCOL" );
        	return false;
    	}else if( document.addHSM.hsmtype.value == "-1"  ){	
        	errMessage(hsmtype,"PLEASE SELECT HSM TYPE" );
        	return false;
    	}else if( document.addHSM.hsmheadertype.value == "-1"  ){	
        	errMessage(hsmheadertype,"PLEASE SELECT HEADER TYPE" );
        	return false;
    	}else if( document.addHSM.hsmhedlen.value == "-1"  ){	
        	errMessage(hsmhedlen,"PLEASE SELECT HSM HEADER LENGTH" );
        	return false;
    	}else if( document.addHSM.hsmtimeout.value == ""  ){	
        	errMessage(hsmtimeout,"HSM TIMEOUT CANNOT BE EMPTY" );
        	return false;
    	}else if(hsmtimeoutval == "0"){
    		errMessage(hsmtimeout," HSM TIMEOUT CANNOT START WITH ZERO " );
        	return false;
        }else if( document.addHSM.hsmconinterval.value == ""  ){	
        	errMessage(hsmconinterval,"HSM CONNECTION INTERVAL CANNOT BE EMPTY" );
        	return false;
    	}else if(hsmconintervalvalue== "0"){
    		errMessage(hsmconinterval,"HSM CONNECTION INTERVAL CANNOT START WITH ZERO " );
        	return false;
    	}else if( document.addHSM.headlength.value == ""  ){	
    		errMessage(headlength,"HEADER LENGTH CANNOT BE EMPTY" );
        	return false;
    	}else if(headlengthvalue == "0"){
    		errMessage(headlength,"HEADER LENGTH CANNOT START WITH ZERO " );
        	return false;
    	}
    	var r=confirm("Do You Want To Submit");
    	if(r){
			return true;
		}else{
    		return false;
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
    function numberOnlyWithdot(evt){
    	var keyvalue=evt.charCode? evt.charCode : evt.keyCode
    	//alert(keyvalue);
    		if((!(keyvalue<48 || keyvalue>57)) || (keyvalue == 8) || (keyvalue== 9) || (keyvalue== 13)|| (keyvalue== 46))
    		{
    			return true;
    		}
    		else
    		{ 
    			return false;
    		} 
    	}    
</script>
    <jsp:include page="/displayresult.jsp"></jsp:include>
	<div align="center">
	 <s:form name="addHSM" action="saveaddHsmAction" autocomplete="off" cssClass="/ifp/style/ifps.css">
	 	<s:hidden name="mkrchkr" id="mkrchkr" value="%{beans.mkrchkrstatus}"></s:hidden>
		<table align="center" border="0"  cellspacing="5" cellpadding="0" width="80%" class="formtable">
	       <tr>
		 	 <%-- <td>HSM NAME <b class="mand">*</b></td><td><s:textfield name="hsmname" id="hsmname"  maxlength="15" onkeypress="return alphanumerals(event);" value="%{bean.hsmname}" ></s:textfield> --%>	 	 
		 	 <td>HSM NAME <b class="mand">*</b></td><td><s:textfield name="hsmname" id="hsmname"  maxlength="15" onkeypress="return alphanumerals(event);" value="%{bean.hsmname}" ></s:textfield>
		 	<s:fielderror fieldName="hsmname" cssClass="errmsg" />  
		 	 </td>
		 	 
 			 <td>HSM STATUS <b class="mand">*</b></td><td><s:radio name="hsmstatus" value="1" id="hsmstatus" list="#{'1':'ENABLE','0':'DISABLE'} "></s:radio>
 			 </td>
 		   </tr>
 			
		   <tr>
		 	 <td>HSM IP ADDRESS <b class="mand">*</b></td><td><s:textfield name="hsmip" id="hsmip" onkeypress='return numberOnlyWithdot(event)' onchange="checkipformat(this);" maxlength="15" value="%{bean.hsmip}"></s:textfield>
		 	<s:fielderror fieldName="hsmip" cssClass="errmsg" /> 
		 	 </td>
		 	 <%-- <td>HSM PORT <b class="mand">*</b></td><td><s:textfield name="hsmport" id="hsmport"  onkeypress='return numerals(event)' maxlength="4" value="%{bean.hsmport}"></s:textfield> --%>
		 	 <td>HSM PORT <b class="mand">*</b></td><td><s:textfield name="hsmport" id="hsmport"  onkeypress='return numerals(event)' maxlength="4" value="%{bean.hsmport}"></s:textfield>
		 	 <s:fielderror fieldName="hsmport" cssClass="errmsg" /> 
		 	 </td>
		  </tr>
   
		  <tr>
		 	 <td>HSM PROTOCOL <b class="mand">*</b></td><td><s:select name="hsmprotocol" id="hsmprotocol" headerKey="-1" headerValue="--Select--" list="#{'TCP/IP':'TCP/IP','SERIAL':'SERIAL'}" value="%{bean.hsmprotocol}"></s:select>
		 	 <s:fielderror fieldName="hsmprotocol" cssClass="errmsg" />
		 	 </td>
		 	 <td>HSM TYPE <b class="mand">*</b></td><td><s:select name="hsmtype" id="hsmtype"  headerKey="-1" headerValue="--Select--" list="#{'THALES':'THALES','SEFENET':'SAFENET'}" value="%{bean.hsmtype}"></s:select>
		 	 <s:fielderror fieldName="hsmtype" cssClass="errmsg" />
		 	 </td>
		  </tr>
		  
		  <tr>
		   	 <td>HEADER TYPE <b class="mand">*</b></td><td><s:select name="hsmheadertype" id="hsmheadertype" headerKey="-1" headerValue="--Select--" list="#{'ASCII':'ASCII','HEX':'HEX'}" value="%{bean.hsmheadertype}"></s:select>
		   	  <s:fielderror fieldName="hsmheadertype" cssClass="errmsg" />
		   	 </td>
		 	 <td>HSM HEADER LENGTH <b class="mand">*</b></td><td><s:select name="hsmhedlen" id="hsmhedlen" headerKey="-1" headerValue="--Select--" list="#{'2':'2','4':'4'}" value="%{bean.hsmhedlen}"> </s:select>
		 	 <s:fielderror fieldName="hsmhedlen" cssClass="errmsg" />
		 	 </td>		 	
		  </tr>
		
		 <tr>
		 	 <td>HSM TIMEOUT <b class="mand">*</b></td><td><s:textfield name="hsmtimeout" id="hsmtimeout" onkeypress='return numerals(event)' maxlength="2" value="%{bean.hsmport}"></s:textfield>
		 	  <s:fielderror fieldName="hsmtimeout" cssClass="errmsg" />
		 	 </td>
		 	 <td>HSM CONNECTION INTERVAL <b class="mand">*</b></td><td><s:textfield name="hsmconinterval" id="hsmconinterval"   onkeypress='return numerals(event)' maxlength="2" value="%{bean.hsmconinterval}"></s:textfield>
		 	  <s:fielderror fieldName="hsmconinterval" cssClass="errmsg" />
		 	 </td>
		  </tr>
		
		  <tr>		 	
		 	 <td>HEADER LENGTH <b class="mand">*</b></td><td><s:textfield name="headlength" id="headlength"  onkeypress='return numerals(event)' maxlength="2" value="%{bean.headlength}"></s:textfield>
		 	 <s:fielderror fieldName="headlength" cssClass="errmsg" />
		 	 </td>		 	 
		  </tr>
	</table>
	<div align="center">	
			<table border="0" cellpadding="0" cellspacing="0" width="20%" >	
				<tr align="center">
					<!-- <td> <input type="submit"  name="submit" id="submit" value="Save" onclick="return validate_form();"/></td>  -->
					<td> <input type="submit"  name="submit" id="submit" value="Save"/></td> 
					<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" /></td>
				</tr>
			</table>
	</div>	   
</s:form>
</div>