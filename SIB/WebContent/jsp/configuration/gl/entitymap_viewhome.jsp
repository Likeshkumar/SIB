<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 

 <script>
function glvalidation( chkval, fldid ){
	var enitytype = document.getElementById("enititytype");
	var entityid = document.getElementById("entityid");
	var cardno = document.getElementById("cardno");
	if(enitytype.value=="-1"){	errMessage(enitytype,"Select entity type");	return false; }
	if(entityid.value==""){	errMessage(entityid,"Enter Value for entity");	return false; }
	if(cardno.value==""){	errMessage(cardno,"Enter card number"); return false; }
	else if(cardno.value.length < 16 ){	errMessage(cardno,"Entered Card Number length is invalid"); 	return false;	}
	
	return true;
}
 </script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<form action="viewEntitytMapListGLConfigure.do" method="post" autocomplete="off">
	
	<table style="border:1px solid #efefef" cellpadding="0" border="0"  style="text-align:center" width="30%" cellspacing="0"  >
	
	  
		 <tr> 	 
			  <td>Enitity   </td>
			 <td> :
			 	<select name="enititytype" id="enititytype" >
					<option value="-1">- SELECT GL -</option>
					<option value="ALL">ALL</option>
					<s:iterator value="glbeans.glkeyslist">
						<option value="${TXNKEY}">${TXNKEYDESC}</option>
					</s:iterator>
				</select>
			</td> 
		</tr> 
	 	<tr>
	 		<td colspan="2">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
						<td>
							<s:submit value="View" name="order" id="order" onclick="return glvalidation()" />
						<td>
							<input type="button"  onclick="return confirmCancel()" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  />							 
						</td>
						</tr>
					</table>
	 		</td>
	 	</tr>
	</table>


</form>
 
 