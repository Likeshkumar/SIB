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
    var fromdate = document.getElementById("fromdate");
	var todate = document.getElementById("todate");
	
  	var url = "getsubActionListCardProductionReport.do?actionhead="+actionhead;
  	alert(url);
	var result = AjaxReturnValue(url);     
	document.getElementById("subaction").innerHTML=result;
} 
  
  function enableField( checkedval, fldid ){
	  
	  var cardno = document.getElementById("cardno");
	  var usercode = document.getElementById("usercode");
	  var actionrow = document.getElementById("actionrow");
	  
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
	  var fromdate = document.getElementById("fromdate");
	  var todate = document.getElementById("todate");
	  var subaction = document.getElementById("subaction");
	  
	 // alert(actionhead.value);
	  
	   if( fromdate ){
		  if( fromdate.value == "" ){  errMessage(fromdate, "Select From Date");  return false;  }
	  }
	  
	  if( todate ){
		  if( todate.value == "" ){  errMessage(todate, "Select To Date");  return false;  }
	  }
	  
	 
	  if( subaction ){
		  if( subaction.value == "" ){  errMessage(subaction, "Select any Sub Action");  return false;  }
	  }
	  
		var arrFrom=fromdate.value.split('-');
		var arrTo=todate.value.split('-');

		var strFromDate=arrFrom[0];
		var strFromMonth=arrFrom[1];
		var strFromYear=arrFrom[2];

		var strToDt=arrTo[0];
		var strToMonth=arrTo[1];
		var strToYear=arrTo[2];


		if(parseInt(arrFrom[0])<10)
		{
		strFromDate=arrFrom[0].replace('0','');
		}
		if(parseInt(arrFrom[1])<10)
		{
		strFromMonth=arrFrom[1].replace('0','');
		}
		
		
		if(parseInt(arrTo[0])<10)
		{
		strToDt=arrTo[0].replace('0','');
		}
		if(parseInt(arrTo[1])<10)
		{
		strToMonth=arrTo[1].replace('0','');
		}

		var mon1 = parseInt(strFromMonth)-1;
		var dt1 = parseInt(strFromDate);
		var yr1 = parseInt(arrFrom[2]);

		var mon2 = parseInt(strToMonth)-1;
		var dt2 = parseInt(strToDt);
		var yr2 = parseInt(arrTo[2]);


		var from_Date = new Date(yr1, mon1, dt1);
		var to_Date = new Date(yr2, mon2, dt2);
		if(to_Date < from_Date)
		{
		errMessage(document.getElementById("fromdate"),"From date should be lesser than to-date");
		return false;
		}
				
		if (fromdate.value == todate.value) 
		{
		    //alert("Error! ...");
		    // whatever you want to do here
		}	  		
	      
	 return true;
 }
 
 function excelgeneration()
 {     
	 
	if(validateForm()){ 
 	var url = "ExcelReportGenMasterCardProductionReport.do?RNO=4";
 	var result = AjaxReturnValue(url);
 	window.location = url;
	}
 }

 
</script>
</head>
 <jsp:include page="/displayresult.jsp"/>    
<div align="center">
	<s:form action="generateCardReportBranchDispatchReport.do" autocomplete="off"  name="reportsgen" namespace="/"> 
	<%-- <s:form action="generateCardReport1ActiveCardReport.do" autocomplete="off"  name="reportsgen" namespace="/">    --%>
		<table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
		<tr>
			<td>
				Select Branch :
			</td>
			<td>
			<select  id="branchlist" name="branchlist"   >
						<option value='ALL'> ALL </option>     
					<s:iterator value="cardbean.branchList">	        
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
					<s:iterator value="cardbean.productList">	        
						<option value='${PRODUCT_CODE}'> ${CARD_TYPE_NAME} </option>    
					</s:iterator>	
			</select>    
			
			</td>
		</tr>
		
		<tr>
		<td align="left" class="fnt">From Date<font class="mand">*</font> :
		</td>
	   <td>
		<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
		<td>
		</tr>
		<tr>
		<td align="left" class="fnt">to Date<font class="mand">*</font> :</td>
		<td>
		<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.reportsgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>      
	</tr>
		
		
		<%-- 
		<tr id="actionrow" >
			<td>
				Select Action 
			</td>
			<td>
				<table border='0' cellpadding='0' cellspacing='0' width='100%' style="margin:0 0"   >
				<tr>
					<td>
						<select  id="subaction" name="subaction" style='height:120px' multiple="multiple"  >
						<option value='$ACT'>ACTIVE</option>
						<option value='$NOTACT'>Cards Not  Normal</option>
						<option value='$CARDSTATUS'>Cards Status History </option>
						<option value='$ADDON'>ADDON CARDS</option>
						<option value='$RENEWAL'> RENEWAL </option>
						<!-- <option value='$MIGRATION'> MIGRATION CRDS </option> -->
						<!--<option value='$TEMPBLOCK'> TEMPBLOCK </option>
						<option value='$REISSUE'> REISSUE </option>
						<option value='$REPIN'> REPIN </option> -->
						
						</select>    
					</td>
					<td >   
						
						 
					</td>
				</tr>
				</table>  
			</td>
		</tr>
		
 --%>
		</table>
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<%-- <td>
				<s:submit name="REPORTTYPE" id="PDF" value="PDFREPORT" onclick="return validateForm()"/>    
			</td> --%>
			<td>
			<s:submit name="REPORTTYPE" id="EXCEL" value="EXCEL" onclick="return validateForm()"/>
			 
		</td>
			<td>
				<s:reset value="Reset"/>
			</td>
		</tr>
	</table>	
	</s:form>
</div>