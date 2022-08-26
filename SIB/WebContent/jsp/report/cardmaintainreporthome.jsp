<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">
/* $(function() {
	
	//alert( parseInt($("#actionhead option").length) )
    $("#actionhead").attr("size", parseInt($("#actionhead option").length)); 
});
 */
 
/*  $('#actionhead').change(function() {
	  alert('The option with value ' + $(this).val() + ' and text ' + $(this).text() + ' was selected.');
	});
  */
 
 function loadSubAction( actionhead ){
	//alert( actionhead ); 
         
  	var url = "getsubActionListCardMaintainReport.do?actionhead="+actionhead;
	var result = AjaxReturnValue(url);     
	document.getElementById("subaction").innerHTML=result;
} 
  
  function enableField( checkedval, fldid ){
	  
	  var cardno = document.getElementById("cardno");
	  var usercode = document.getElementById("usercode");
	  var actionrow = document.getElementById("actionrow");
	 // alert(checkedval+""+fldid);
	  if( checkedval ){ 
			if( fldid == "$CARDBASED" ){ 
			 cardno.disabled=false;
			 cardno.focus();  
			}
			
			if( fldid == "$USERBASED" ){ 
				usercode.disabled=false;
				usercode.focus();  
			}
			
			if( fldid == "$ACTBASED" ){ 
				actionrow.style.display="table-row";
				cardno.focus();  
			} 
	  }else{
		  if( fldid == "$CARDBASED" ){
			 cardno.value="";
			 cardno.disabled=true;
			 
			}
			
			if( fldid == "$USERBASED" ){ 
				usercode.value="";
				usercode.disabled=true;
				 
			}
			
			if( fldid == "$ACTBASED" ){ 
				actionrow.style.display="none";
				cardno.focus();  
			} 
	  }
  }

 function validateForm(){
	  var cardno = document.getElementById("cardno");
	  var usercode = document.getElementById("usercode");
	  var actionrow = document.getElementById("actionrow");
	  var fromdate = document.getElementById("fromdate");
	  var todate = document.getElementById("todate");
	  var cardbased = document.getElementById("$CARDBASED");
	  var userbased = document.getElementById("$USERBASED");
	  var actbased = document.getElementById("$ACTBASED");
	  var orderby = document.getElementById("orderby");
	  
	  if( !cardbased.checked && !userbased.checked &&  !actbased.checked  ){
		  errMessage(cardbased, "Select Type");  return false;
	  }
	  if( cardbased.checked ){
		  if( cardno.value == ""){  errMessage(cardno, "Enter Card Number");  return false;  }
		  else if( cardno.value.length < 16 ){  errMessage(cardno, "Invalid Card Number");  return false;  }
	  }
	  
	  if( userbased.checked ){
		  if( usercode.value == ""){  errMessage(usercode, "Enter User Name");  return false;  } 
	  }
	  
	  if( fromdate ){
		  if( fromdate.value == "" ){  errMessage(fromdate, "Select From Date");  return false;  }
	  }
	  
	  if( todate ){
		  if( todate.value == "" ){  errMessage(todate, "Select To Date");  return false;  }
	  }
	  
	  
	  if( actbased.checked ){
		  if(  $("select[name='actionhead'] option:selected").length == 0 ){
			  errMessage(actionhead, "Select Any Action");  return false; 
		  } 
		  if(  $("select[name='actionhead'] option:selected").length > 0 ){
			  if(  $("select[name='subaction'] option:selected").length == 0 ){
				  errMessage(subaction, "Select Any Sub-Action");  return false; 
			  }
			   
		  } 
	  }
	  
	  if( orderby.value == -1 ){
		  errMessage(orderby, "Select Order By");  return false; 
	  }
	  
	 return true;
 }
</script>
</head>
 <jsp:include page="/displayresult.jsp"/>
<div align="center">
	<s:form action="generateAuditReportCardMaintainReport.do" autocomplete="off"  name="reportsgen" onsubmit="return validateForm()" namespace="/">
		<table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
		<tr>
			<td>
				Select Type
			</td>
			<td>
			<input type="checkbox" id="$CARDBASED" name="$CARDBASED" onclick="enableField(this.checked, this.id)" /> Card Number  &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" id="$USERBASED" name="$USERBASED" onclick="enableField(this.checked, this.id)"  /> User Based   &nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" id="$ACTBASED" name="$ACTBASED" onclick="enableField(this.checked, this.id)"  /> Action Based
				<%-- <select>
					<option value='-1'> -SELECT - </option>
					<option value='$CARD'> Card Number  Based </option>
					<option value='$USER'> User Based </option>
					<option value='$ACTION'> Action Based </option> 
				</select> --%>
			</td>
		</tr>	
		
		
		<tr>
			<td>
				Select Branch :
			</td>
			<td>
			<select  id="branchlist" name="branchlist"   >
						<option value='ALL'> ALL </option>     
					<s:iterator value="branchList">	        
						<option value='${BRANCH_CODE}'> ${BRANCH_NAME} </option>    
					</s:iterator>	
						</select>    
			
			</td>
		</tr>
		<tr>
			<td>
				Select Product :
			</td>
			<td>
			<select  id="productCode" name="productCode"   >
						<option value='ALL'> ALL </option>     
					<s:iterator value="productList">	        
						<option value='${PRODUCT_CODE}'> ${CARD_TYPE_NAME} </option>    
					</s:iterator>	
			</select>    
			
			</td>
		</tr>
		
		
		<tr id="fromcardrow">
			<td>
				From Date 
			</td>
			<td>
				<input type='text' id='fromdate' name='fromdate' maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
				
			</td>
		</tr>
		
		<tr id="tocardrow">
			<td>
				To Date 
			</td>
			<td>
				<input type='text' id='todate' name='todate' maxlength="19" readonly />
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
		
		<tr id="cardrow">
			<td>
				Enter Card Number 
			</td>
			<td>
				<input type='text' id='cardno' name='cardno' maxlength="19" disabled />
			</td>
		</tr>
		
		<tr id="userrow">
			<td>
				Enter UserId
			</td>
			<td>
				<input type='text' id='usercode' name='usercode'  disabled />
			</td>
		</tr>
		
		<tr id="actionrow" style="display:none">
			<td>
				Select Action 
			</td>
			<td>
				<table border='0' cellpadding='0' cellspacing='0' width='100%' style="margin:0 0"   >
				<tr>
					<td>
						<select  id="actionhead" name="actionhead" style='height:120px' multiple="multiple"  >
						 	<!-- <option value='$ALL'> ALL </option> -->
							<s:iterator value="reportbean.actionheadlist">
								<option value='${AUDIT_ACTIONCODE}'> ${AUDIT_ACTIONDESC} </option>
							</s:iterator>
						</select>
					</td>
					<%-- <td >
							 
						<select  id="subaction"  name="subaction"  style='height:120px' multiple="multiple" > 
				  
						</select>
						 
					</td> --%>
				</tr>
				</table>  
			</td>
		</tr>
		
		 <tr>
			<td>
				Report Order By
			</td>
			<td>
				<select id="orderby" name="orderby">
					<option value='-1'> -SELECT - </option>
					<option value='$ACTDATE' selected> Date & Time Wise </option>
					<option value='$ACTIONWISE'> Action Wise </option> 
				</select>
				
				<select id="ascdesc" name="ascdesc"> 
					<option value='ASC' selected> Ascending </option>
					<option value='DESC'> Descending </option> 
				</select>
				
				
			</td>
		</tr>
		
		<%-- <tr>
			<td>
				Select Action 
			</td>
			<td>
				<select multiple  style='height:100%;'> 
					<option value='$ALL'> ALL </option>
					<s:iterator value="reportbean.actionheadlist">
						<option value='${AUDIT_ACTIONCODE}'> ${AUDIT_ACTIONDESC} </option>
					</s:iterator>
				</select>
			</td>
		</tr> --%>
			
		</table>
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<td>
				<s:submit name="getreport" id="getreport"  value="PDF" onclick=" return validateReportpage();"/>
			</td>
			
			<td>
			<s:submit name="getreport" id="EXCEL" value="EXCEL"/>
			 
		</td>
			
			<td>
				<s:reset value="Reset"/>
			</td>
		</tr>
	</table>	
	</s:form>
</div>