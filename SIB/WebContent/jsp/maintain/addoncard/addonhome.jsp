<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>   
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/table.css"/>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">
	
	
	function getaddonname(){ 
		//alert("etest");
		var addonname = document.getElementById("addonname");
		var cardnumber = document.getElementById("cardnumber").value;
		
		var productcode = document.getElementById("productcode").value;
		var accountno = document.getElementById("accountno").value;
		var maskedcardno = document.getElementById("maskedcardno").value;
		var dob = document.getElementById("dob");
		//alert(dob);
		
		//addonname=addonname..trim();
		if( addonname.value.trim()== ""){
			errMessage(addonname,"Enter the Add on Name");
  			return false;
		}
		
		
		if (dob.value.trim()=="")
		{
			errMessage(dob,"Please select the Date of Birth");
  			return false;
			
		}
		
		
		//addondetails = cardnumber="+cardnumber+"&username="+username;
		//addondetails = "username="+username+"&cardnumber="+cardnumber+"&productcode="+productcode+"&accountno="+accountno+"&maskedcardno="+maskedcardno;
		
		 
			
		//alert(addonname.length);
		var url="generateAddoncardActionAddonCardAction.do?addonname="+addonname.value+"&cardnumber="+cardnumber+"&productcode="+productcode+"&accountno="+accountno+"&maskedcardno="+maskedcardno+"&dob="+dob.value;
		//alert( url );
		var result = AjaxReturnValue(url);
		//alert(result);
		var r=confirm("Are You Sure to Check wheather add on name and Date of birth modified?");
		return r;
		
	}
	function isNumberKey(evt)
	{
	   var charCode = (evt.which) ? evt.which : event.keyCode;
	   if (charCode > 31 && (charCode < 48 || charCode > 57))
	      return false;
	   return true;
	}
	function set_textboxname(value)
	{	
		if(value=='O')
		{
			document.getElementById("cardnum").value = "";
			document.getElementById("orderrefdiv").style.display = "table-row";
			document.getElementById("cardnumdiv").style.display = "none";
		}
		else
		{
			document.getElementById("orderref").value = "";
			document.getElementById("cardnumdiv").style.display = "table-row";
			document.getElementById("orderrefdiv").style.display = "none";
		}
	}
	

/* function searchcard()
{
	//alert("Welcome");
	var cardnum = document.getElementById("cardnum").value;
	var orderref = document.getElementById("orderref").value;
	var url = "fidocument.orderform.dobndcardstatusFindcardAction.do?orderref="+orderref+"&cardnum="+cardnum;	
	//alert(url);
	result = AjaxReturnValue(url);
	//alert(result);
	document.getElementById("displayresult1").innerHTML = result;
} */

function passwordvalidation()
{
	var instid = document.getElementById("instid");
	var usertext = document.getElementById("usertext");
	var mailtext = document.getElementById("mailtext");

	if(instid.value=="-1")
	{
		//alert(instid.value);
		errMessage(instid,"Select Institution");
		return false;
	}

     var form = document.getElementsByName("user_mail");
     for(var i = 0; i < form.length; i++)
     {
          if(form[i].checked)
          {
          var selectedValue = form[i].value;
          //alert(selectedValue);
          	if(selectedValue=='U')
          	{
          		if(usertext.value=="")
          		{
          			errMessage(usertext,"Enter Username");
          			return false;
          		}
          	}
          	if(selectedValue=='E')
          	{
          		if(mailtext.value=="")
          		{
          			errMessage(mailtext,"Enter Email-id");
          			return false;
          		}
          	}

          }
     }
	return true;
	
}
function findcard_validation(){	
	   var orderref = document.getElementById("orderref");
	   var cardnum = document.getElementById("cardnum");
	   var form = document.getElementsByName("orderrefnum_cardno");
	     for(var i = 0; i < form.length; i++)
	     {
	          if(form[i].checked)
	          {
	          var selectedValue = form[i].value;
	          //alert(selectedValue);
	          	if(selectedValue=='O')
	          	{
	          		if(orderref.value=="")
	          		{
	          			errMessage(orderref,"Enter Order reference number");
	          			return false;
	          		}
	          	}document.orderform.dob
	          	if(selectedValue=='C')
	          	{
	          		if(cardnum.value=="")
	          		{
	          			errMessage(cardnum,"Enter Card number");
	          			return false;
	          		}
	          	}

	          }
	     }
	     var collectbranch = document.getElementById("collectbranch");
	 	 if( collectbranch ){ if( collectbranch.value == "") { errMessage(collectbranch, "Select Collection Branch !");return false; } }
	 	
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
/* function enableHide(){
	document.getElementById("collecthide").style.display = "block";
} */
</script>

<style>

.textcolor
{
color: maroon;
font-size: small;
align:center;
}



</style>


<%-- <%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ");  
%> --%>


    <input type="hidden" name="Instname" value="${Instname}" />
     <input type="hidden" name="BRANCHCODE" value="${BRANCHCODE}" />
     <input type="hidden" name="USERTYPE" value="${USERTYPE}" />
     <input type="hidden" name="BRANCH_DESC" value="${BRANCH_DESC}" />
     <input type="hidden" name="DATEFILTER_REQ" value="${DATEFILTER_REQ}" />

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="addonCardActionAddonCardAction.do"  name="orderform" autocomplete = "off"  namespace="/">

	<table border="0" cellpadding="0" cellspacing="4" width="40%" class="formtable">
	
				<tr id="cardnumdiv" >
					<td>Enter Card No :</td>
					<td ><s:textfield style='text-align: center;' name="cardnum" id="cardnum"  maxlength="19" onkeypress='return isNumberKey(event)' value="%{bean.cardno}"/></td>
				</tr>
				
				<tr>
					<td>Card Collection Branch :</td>
					<td> <select style='text-align: center;' name="collectbranch" id="collectbranch">
								<option value="">Select Branch</option>
								<s:iterator value="cardcollectbranchlist">
									<option value="${BRANCH_CODE}">${BRANCH_NAME}</option>
								</s:iterator>
						</select>
					</td>
				</tr>
	  
	</table>	
	<br>
	<table width='20%'>
		<tr align="center">
			<!-- <td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" onclick="return findcard_validation();"/></td> -->
			  <input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="findcard" id="findcard" value="Search" /></td>
			
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
	</table>
	</s:form>
	
	<br><br>
	
	<s:form action="generateAddoncardActionAddonCardAction.do"  name="orderform" id="orderform" autocomplete = "off"  namespace="/" onsubmit="return getaddonname();">
<%-- 	<%String act = request.getParameter("act")==null?"":request.getParameter("act"); %> --%>
	<%-- <input type="hidden" value="${act}" name = "act"  /> --%>
	
	<input type="hidden" value="${fn:escapeXml(act)}">
	<div id='fw_container'>	
	<table border='0' cellpadding='1' cellspacing='1 'width='80%'   class="formtable"  >
	<s:if test ="%{!carddetlist.isEmpty()}">
	<tr> 
	<th>CARD NO</th> 
	<th>ACCOUNT NO</th>
	<th>MOBILE NO</th>  
	<th>CUSTOMER  NAME</th> 
	<!-- <th>DOB </th>  -->
	<th>CUSTOMER ID </th> 
	<th>BIN</th>  
	<th>CARD STATUS</th> 
	<th>PRODUCT</th>  
	<!-- <th>ORIGINAL CARDNO</th> -->  </tr>
	</s:if>
	<s:iterator value="carddetlist">	
	<s:if test='padssenabled=="Y"' >
	<input type="hidden" id="cardnumber" name="cardnumber" value="${ORG_CHN}"/>
	</s:if>
	<s:else>
	<input type="hidden" id="cardnumber" name="cardnumber" value="${CARD_NO}"/>
	</s:else>
	<input type="hidden" id="productcode" name="productcode" value="${PRODUCT_CODE}"/>
	<input type="hidden" id="accountno" name="accountno" value="${ACCOUNT_NO}"/>
	<input type="hidden" id="maskedcardno" name="maskedcardno" value="${MCARD_NO}"/>
		<input type="hidden" id="bulkregid" name="bulkregid" value="${BULK_REG_ID}"/>
		<input type="hidden" id="subprodid" name="subprodid" value="${SUB_PROD_ID}"/>
		<input type="hidden" id="cin" name="cin" value="${CIN}"/>
		
	
	
	<%-- <input type="text" name="maskedcardno" value="${MCARD_NO}"/> --%>
							<tr> 
								<td style="text-align:center">${MCARD_NO}</td>
								<td style="text-align:center">${ACCOUNT_NO}</td>
								<td style="text-align:center">${MOBILE}</td>
								<!--  <td style="text-align:center">${NAME}</td>-->
								<td style="text-align:center"><input type="text" id="addonname" name="addonname"  value="${NAME} " maxlength="26" /></td>								
								<%-- <td>
									<input type="text" name="dob" id="dob"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px" value="${DOB}">
									<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.getElementById('dob'),'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
								</td> --%>															
								<!--  <td style="text-align:center">${DOB}</td>-->
								<td style="text-align:center">${CIN}</td>
								<td style="text-align:center">${BIN}</td>
								<td style="text-align:center;color:red;">${STATUS_DESC}</td>
								<td style="text-align:center">${PRODUCT_DESC}</td>
								<!-- <td style="text-align:center">${ORG_CARDNO}</td> -->
								
							</tr>
						</s:iterator> 
						<s:hidden name="collectbranch"/>
	</table>
	<br>
	<table width='20%'>
	<s:if test ="%{!carddetlist.isEmpty()}">
		<tr align="center">
		 <input type="hidden" name="token" id="csrfToken" value="${token}">
			<td colspan='2'><input type="submit" name="generatecard" id="generatecard" value="Generate Add-on Card"/></td>
			<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel();"/></td>
		</tr>
		</s:if>
	</table>
	
	
</div>


</s:form>
	