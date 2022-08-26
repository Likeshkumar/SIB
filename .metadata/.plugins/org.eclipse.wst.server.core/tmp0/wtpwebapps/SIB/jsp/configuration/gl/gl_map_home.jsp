<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 
<script>
function validation()
{
//alert("Welcome");
 
var acctrule = document.getElementById("acctrule");
	if(acctrule.value == "-1")
	{
		errMessage(acctrule,"Select Accounting Rule..");
		return false;
	}
	parent.showprocessing();
	return true;
}


</script>
<jsp:include page="/displayresult.jsp"></jsp:include>
 
<form action="configGlMappingGLConfigure.do" method="post" autocomplete="off">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="70%" cellspacing="0"  > 
		<tr > 
			<td colspan="6">
				<table  style="border:1px solid #efefef;" cellpadding="0" cellspacing="0" width="50%">
				<tr>
				 
				  <tr>
					 <td>Transaction</td>
				  	<td> :  
				  		<s:select name="mastertxncode" id="acctrule"  list="glbeans.glgroup" listKey="ACCOUNTRULECODE" listValue="ACCT_RULEID"   headerKey="-1" headerValue="-SELECT-"   ></s:select>
				  		  
				 	</td>
				</tr>  
				</table>
			</td>	
		</tr> 
		
		<tr>
	 		<td colspan="6">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
						<td>
							<s:submit value="Next" name="submit" id="submit" onclick="return validation();" /> 
						</td>
						<td>
							<input type="button"  onclick="return confirmCancel()" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  />
							 
						</td>
						</tr>
					</table>
	 		</td>
	 	</tr>
	 	
	</table>


</form>
 
 