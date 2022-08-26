<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="jsp/merchant/script/merchscript.js"></script> 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<style>
a{
	color:blue;
}
</style>
<script>

	function addSeperator( dateid, dateval ){
		var newval = dateval.replace("-","");
		if( dateval.length == 2 ){
			if ( newval.length == 2 ){
			document.getElementById(dateid).value  = dateval+"-";
			}
		}
		 
		if( dateval.length == 5 ){
			 
				document.getElementById(dateid).value  = dateval+"-";
			 
		}
		 
		
	}
	function searchRecords(){
		
		var merchnamefld = document.getElementById("merchname");
		
		 
		 document.getElementById("displayresult1").innerHTML = "<div style='text-align:center'>PROCESSING...</div>";
		 
		//alert( document.getElementById("displayresult1") );
	 
		 var url = "searchListMerchantRegister.do?merchname="+merchnamefld.value+"&methodtype=TERMINAL";
		 
		 result = AjaxReturnValue(url);
		// alert(result);
		 document.getElementById("displayresult1").innerHTML = result;
		 
		return false;
			
	}
	
	function showMaintain( instid, cardno ){
		var url = "showMaintainCardMaintainAction.do?instid="+instid+"&cardno="+cardno;
		var x = confirm( "Do you want to continue the maintenance activity for the card  " + cardno );
		if( !x ){
			return false;
		}
		window.location=url;
	}
	
	function showSelect( rowid ){
		document.getElementById(rowid).style.background="#F0F0F0";
	}
	
	function showDeSelect( rowid ){
		document.getElementById(rowid).style.background="#fff";
	}
	

</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
 
<s:form action="#"  name="orderform" onsubmit="return searchRecords()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="90%"  align="center" >	 
	<tr>
		 <td>
		 	<table border="0" cellpadding="0" cellspacing="4" width="100%" align="center" class="formtable" >
		 	<tr>
		 		<td>
			 			<table border="0" cellpadding="0" cellspacing="0" width="50%" >
							<tr>
								<td colspan='2' style='text-align:center' >Merchant Name </td> <td> <input type='text' name='merchname' id='merchname' />
									<br/> <small style='color:#000;font-size:9px'> Enter merchant name to be Terminal Add / View / Edit or Delete </small>
								</td>
							</tr>
						</table>
		 		</td>
		 		
		 		 
		 	</tr> 
		 	<tr>
				<table border="0" cellpadding="0" cellspacing="0" width="20%" >
				<tr>
					<td> <s:submit value="Submit" name="order" id="order" onclick="return addmerchhome_validation()"/> </td>
					<td><input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/></td>
				</tr>
				</table>
			</tr> 
	
		 	
		 	</table>
		 </td>
	</tr> 
	
	
	<tr>
	<td>
		<div id="displayresult1">
		 &nbsp;
		</div>
	</td>
	</tr>
	
</table> 

</s:form>
 
 