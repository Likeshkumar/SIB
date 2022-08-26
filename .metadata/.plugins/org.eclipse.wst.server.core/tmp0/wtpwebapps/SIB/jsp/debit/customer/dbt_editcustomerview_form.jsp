<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<style>
table.viewtable td{
	padding-top:20px;
}
</style>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script> 
 

<script>
	function viewCustomerData(){
		var customers = document.getElementsByName('customerid'); 
		var customerid;
		for(var i = 0; i < customers.length; i++){
		    if(customers[i].checked){
		    	customerid = customers[i].value;
		    }
		} 
		var url = "viewCustomerDataForViewCreditCustRegisteration.do?doact=VIEW&customerid="+customerid;
		alert( url );
		window.location = url;
		
	}
	
	function authCustomerData(){
		var customers = document.getElementsByName('customerid'); 
		var customerid;
		for(var i = 0; i < customers.length; i++){
		    if(customers[i].checked){
		    	customerid = customers[i].value;
		    }
		} 
		var url = "viewCustomerDataForViewCreditCustRegisteration.do?doact=AUTH&customerid="+customerid;
		//alert( url );
		window.location = url;
		
	}
	
	function validation(){		
		var applicationid = document.getElementById("applicationid");
		if(applicationid.value=='-1' || applicationid.value==''){
			 errMessage(applicationid,"Select Any Record !");return false;
		}
	}
	
	function showProcessing(){
		parent.showprocessing();
	}
	
	function searchCustomer(){
		
		alert('test)');
		
		var applicationid = document.getElementById("applicationid");
		
		//var doact = document.getElementById("doact"); 
		var qrystr = "customerSearchDebitCustomerRegister.do?doact="+doact.value+"&applicationid="+applicationid.value;
		
			//if( applicationid.value == "-1"){ errMessage(applicationid,"Select Application Id !");return false;}
			//			 return false;
		
		
	 
		 
		
		var result = AjaxReturnValue(qrystr);
		alert(result);
	//	var res = result.split("~");
		//if( res[1] == "0"){
	//		var submitbtntable = document.getElementById("submitbtntable");
		//	submitbtntable.style.display="table";
	//	}
		document.getElementById("submitbtntable").innerHTML=result;
		return false;
	}
	
	
	function enableField( key ){
		var applicationid = document.getElementById("applicationid");
		var dob = document.getElementById("dob");
		var customername = document.getElementById("customername");
		
		if( key == "$APPID"){ 
			applicationid.disabled = false;
			applicationid.focus();
			dob.disabled=true;
			customername.disabled=true;
		}else if( key == "$DOB"){
			dob.disabled=false;
			dob.focus();
			applicationid.disabled = true;			
			customername.disabled=true;
		}else if( key == "$NAME"){
			customername.disabled=false;
			customername.focus();
			applicationid.disabled = true;
			dob.disabled=true;
			
		}
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include> 
 <s:form action='EditcustomerSearchDebitCustomerRegister.do' name='form1' id='form1'> 
 <%String act = request.getParameter("act")==null?"":request.getParameter("act"); %>
 <input type="hidden" name = "act" value="<%=act%>" />
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">

<s:if test="%{#session.USERTYPE =='INSTADMIN'}">
<!-- 
			<tr>
				<td class="fnt"> Select Branch</td>
				<td> : <select id="branchcode" name="branchcode" >
					<s:if test ="%{dbtcustregbean.branchlist.isEmpty()}">
					<option value="">No Branch Found</option>
					</s:if>
					<s:else>
								<option value="" >-Select Branch-</option>   
								<option value="ALL">ALL BRANCH </option>
								<s:iterator value="dbtcustregbean.branchlist">
								<option value="<s:property value="BRANCH_CODE"/>" > 
								<s:property value="BRANCH_NAME"/></option>
								</s:iterator>
					</s:else>
					</select> </td>
				<td>
				
				</td>
			</tr>
			 -->
	</s:if>
	<s:else>
			<tr>
			<td class="fnt"> Branch</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
	</s:else>
	
<tr>
	<s:hidden name="doact" id="doact" value="ALTER" />
	
	
	
	<td>   Select Application Number </td>
	<td>  : <s:select list="%{dbtcustregbean.applicationlist}" id="applicationid"  name="applicationid"    headerKey="-1" headerValue="-SELECT-"  listKey="ORDER_REF_NO"  listValue="APPLICANT" value="%{custregbean.applicationid}"/> </td>
	
	
	
</tr>

 
 
</table> 

<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<input type="submit" value="Edit" name="order" id="order" onclick="return validation()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>



</s:form>


 
 