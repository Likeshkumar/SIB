<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@ page import="java.util.*" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/customeselectbox.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/customselectbox.js"></script>
<script type="text/javascript">


 function validateForm(){
	  
	  var year = document.getElementById("year");
	  var fromdate = document.getElementById("fromdate");
	  var todate = document.getElementById("todate");

	   if( year ){
		  if( year.value == "-1" ){  
			  errMessage(year, "Select Year");  
			  return false;  
			  }
	  	}
	   
	   if(fromdate){
		   if(fromdate.value == ""){
			   errMessage(fromdate, "Select the fromdate");
			   return false;
		   }
	   }
	   
	   if(todate){
		   if(todate.value == ""){
			   errMessage(todate, "Select the todate");
			   return false;
		   }
	   }
	
	return true;   
	   
 }
 
 

</script>
</head>
 <jsp:include page="/displayresult.jsp"/>    
<div align="center">
	<s:form action="searchListCardMigration.do" autocomplete="off"  name="reportsgen" namespace="/">
	
	   <table border='0' cellpadding='0' cellspacing='0' width='50%' class=" viewtable" >
	    
		<tr>
			
			<td>
				Select Bin :
			</td>
			<td>
			<select  id="binlist" name="binlist"   >
					<option value='ALL'> ALL </option>     
				<s:iterator value="cardmigratebean.binList">	        
					<option value='${BIN}'> ${BIN} </option>    
				</s:iterator>	
			</select>    
			
			</td>
		</tr>
		<tr>
			<td>
				Select Branch :
			</td>
			<td>
			<select  id="branchlist" name="branchlist"   >
					<option value='ALL'> ALL </option>     
				<s:iterator value="cardmigratebean.branchList">	        
					<option value='${BRANCH_CODE}'> ${BRANCH_NAME} </option>    
				</s:iterator>	
			</select>    
			
			</td>
		</tr>	
		
		<%-- <tr>
			<td>
				Select Product :
			</td>
			<td>
			<select  id="productCode" name="productCode"   >
						<option value='ALL'> ALL </option>     
					<s:iterator value="cardmigratebean.productList">	        
						<option value='${PRODUCT_CODE}'> ${CARD_TYPE_NAME} </option>    
					</s:iterator>	
			</select>    
			
			</td>
		</tr> --%>
 
	</table>
		<table border='0' cellpadding='0' cellspacing='0' width='20%' >		
		<tr>
			<td>
				<s:submit name="submit" id="submit" value="Submit" onclick="return validateForm();"/>    
			</td>
			<td>
			<s:submit name="cancel" id="cancel" value="cancel" onclick="return validateForm();"/>
			 
		</td>
			
		</tr>
	</table>	
	</s:form>
</div>