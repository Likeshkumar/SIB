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
	var r=confirm("Do You Want To Edit");
  	if(r)
  	{
        valid = true;

        if ( document.addHSM.HSMNAME.value == "" )
       		 {
        	 alert ( "'hsmname CANNOT BE EMPTY'" );
        	 valid= false;
       		}
        
        			        
        else if( document.addHSM.HSMADDRESS.value == ""  )
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
    else return r;
}

function confirmation()
{
var r=confirm("Are You Sure 'Delete'");
return r;
	
}

function goBack()
{
	window.history.back();
}
</script>
<style>
#textcolor
{
color: maroon;
font-size: small;
}
</style>
</head>
<jsp:include page="/displayresult.jsp"/>

<div align="center">
	<center><h1><font color="maroon">CURRENCY DETAILS</font></h1></center><br><br>
		<table align="center" border="1"  cellspacing="5" cellpadding="0" width="80%" rules="none" frame="box">
			<s:iterator value="currencydetails">
			    	 <tr>
				 		 <td>CURRENCY </td><td id="textcolor">${CURRENCY_DESC}</td>
						 <td>CODE</td><td id="textcolor">${CURRENCY_CODE}</td>
				   </tr>
				   <tr>
					 	 <td>NUMERIC CODE</td><td id="textcolor">${NUMERIC_CODE}</td>
					 	<td>STATUS</td><td id="textcolor">${CURRENCY_STATUS}</td>
					  </tr>
			  		 <tr>
			  		  	 <s:hidden name="hsmid" id="hsmid" value="%{CURRENCY_CODE}"></s:hidden>
			  		  	 <s:set name="hsmid">${CURRENCY_CODE}</s:set>
			  		  	  
			  		  
					  </tr>
			</s:iterator>
		</table>
		<br>
		<table  border="0"  cellspacing="0" cellpadding="0" width="20%">
			<tr align="center">
				<td>
				<s:property value="hsmid"></s:property>
					<form action="editCurrencyCurrencyAction.do" method="post">
					<input type="hidden" name="currcode" id="currcode"
						value="${CURRENCY_CODE}" /> <input type="image"
						src="images/edit.png" alt="submit Button"
						onclick="return check_form();">
				</form>
			
		<td align="center">
			<form action="deleteCurrencyCurrencyAction.do" method="post">
				<input type="hidden" name="currcode" id="currcode"
					value="${CURRENCY_CODE}" /> <input type="hidden" name="currdesc"
					id="currdesc" value="${CURRENCY_CODE}" /> <input type="image"
					src="images/delete.png" alt="submit Button"
					onclick="return ask_confirm();">
			</form>
		</td>
		</td>
				<td><s:submit value="Back" name="submit" id="submit" onclick="goBack()"/></td>
			</tr>
	</table>
</div>