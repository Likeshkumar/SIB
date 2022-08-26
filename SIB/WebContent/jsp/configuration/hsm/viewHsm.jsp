<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" >
function chk_confirm()
{
  return(confirm("Are You Sure"));
}
function check_form()
{
	/* var r=confirm("Do You Want To Edit");
  	if(r)
  	{
        valid = true;

        if ( document.addHSM.HSMNAME.value == "" )
       		 {
        	 alert ( "'hsmname CANNOT BE EMPTY'" );
        	 valid= false;
       		} */
        
        			        
         if( document.addHSM.HSMADDRESS.value == ""  )
        {	
        	 alert ( "'hsmip CANNOT BE EMPTY' " );
        	 return false;
       		 
    	}
        
        else if( document.addHSM.HSMPORT.value == ""  )
        {	
        	 alert ( "'HSMPORT CANNOT BE EMPTY' " );
        	 return false;
       		 
    	}
      
        else if( document.addHSM.HSMTIMEOUT.value == ""  )
        {	
        	 alert ( "'hsmip CANNOT BE EMPTY' " );
        	 return false;
       		 
    	}
        

        else if( document.addHSM.CONNECTIONINTERVAL.value == ""  )
        {	
        	 alert ( "'hsmip CANNOT BE EMPTY' " );
        	 return false;
       		 
    	}
        

        else if( document.addHSM.HEADERLEN.value == ""  )
        {	
        	 alert ( "'hsmip CANNOT BE EMPTY' " );
        	 return false;
       		 
    	}
      
        return valid; 
        
	}
    return r;

function confirmation()
{
var r=confirm("Are You Sure 'Delete'");
return r;
	
}

function goBack()
{
	window.history.back()
}
</script>
<style>
#textcolor
{
color: maroon;
font-size: small;
font-weight: bold;
}

#tdbold{
font-weight: bold;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"/>

<div align="center">
		<table align="center" border="1"  cellspacing="0" cellpadding="0" width="80%" class="formtable">
			<s:iterator value="beans.hsmdetails">
			    	 <tr>
				 		 <td id="tdbold">HSM NAME</td><td id="textcolor">${HSMNAME}</td>
						 <td id="tdbold">HSM STATUS</td><td id="textcolor">${HSMSTATUS}</td>
				   </tr>
				   
			  		 <tr>
					 	 <td id="tdbold">HSM IP ADDRESS</td><td id="textcolor">${HSMADDRESS}</td>
					 	 <td id="tdbold">HSM PORT </td><td id="textcolor">${HSMPORT}</td>
					  </tr>
			  
					  <tr>
					 	 <td id="tdbold">HSM PROTOCOL</td><td id="textcolor">${HSMPROTOCOL}</td>
					 	 <td id="tdbold">HSM TYPE </td><td id="textcolor">${HSMTYPE}</td>
					  </tr>
					  
					  <tr>
					   	 <td id="tdbold">HEADER TYPE </td><td id="textcolor">${HEADERTYPE}</td>
					 	 <td id="tdbold">HSM HEADER LENGTH</td><td id="textcolor">${HSMHEADERLEN}</td>
					 	
					  </tr>
					
					<tr>
					 	 <td id="tdbold">HSM TIMEOUT</td><td id="textcolor">${HSMTIMEOUT}</td>
					 	 <td id="tdbold">HSM CONNECTION INTERVAL </td><td id="textcolor">${CONNECTIONINTERVAL}</td>
					  </tr>	
					  				
					  <tr>					 	
					 	 <td id="tdbold">HEADER LENGTH </td><td id="textcolor">${HEADERLEN}</td>
					 	 <s:hidden name="hsmid" id="hsmid" value="%{HSM_ID} "></s:hidden>
					  </tr>
					  <tr>					 	
					 	 <td id="tdbold"> ADDED BY </td><td id="textcolor">${ADDED_BY}</td>
					 	  <td id="tdbold">ADDED DATE </td><td id="textcolor">${ADDED_DATE}</td>
					  </tr>
					  
					  <tr>					 	
					 	 <td id="tdbold">AUTH BY </td><td id="textcolor">${AUTH_BY}</td>
					 	 <td id="tdbold">AUTH DATE </td><td id="textcolor">${AUTH_DATE}</td>
					  </tr>
					  
					  <tr>					 	
					 	 <td id="tdbold">AUTH STATUS </td><td id="textcolor">${AUTH_CODE}</td>
					 	 <td id="tdbold">REMARKS </td><td id="textcolor">${REMARKS}</td>
					  </tr>
					  
			</s:iterator>
		</table>
		<br>
		<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
			<tr align="center">
				<td>
					<s:form name="addHSM"  action="editHsmAction" autocomplete="off">
						<s:hidden name="hsmid" id="hsmid" value="%{beans.hsm_idno} "></s:hidden>
						
						<%-- <s:submit name="submit" value="Edit" onclick="return check_form();" ></s:submit> --%>
						
						<s:submit name="submit" value="Edit" ></s:submit>
					</s:form>
				</td>
				<td>
					<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()" />		 
				</td>
			</tr>
		</table>
</div>


