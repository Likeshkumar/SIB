<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<link rel="stylesheet" type="text/css" href="style/customeselectbox.css"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/jquery/jquery.js"></script>
<script type="text/javascript" src="js/customselectbox.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function vasvalidationselect(){
	var fromdate = document.getElementById("fromdate");	
	var todate = document.getElementById("todate");
	if( fromdate ){
		if( fromdate.value == "" ){
			errMessage(fromdate,"Enter from date");
			return false;
		}	
	}
	if( todate ){
		if( todate.value == "" ){
			errMessage(todate,"Enter to date");
			return false;
		}
	}
	valid=true;
	var orderref = null; 
	var oneselected = false;
	var orderreflen = document.getElementsByName("glkeys");
	for(var i=1;i<=orderreflen.length;i++){
		var glkeysid  = 	"glkeys"+i;
		var orderref = document.getElementById(glkeysid);
		if(orderref.checked==true){
			oneselected = true;	 
			break;
		}
	}
	if(!oneselected){
		errMessage(orderref,"Select atleast one transactions");
		valid= false;
		return false;
	}
	return true;
}

function txnvalidationselect(){
	var fromdate = document.getElementById("txnfromdate");	
	var todate = document.getElementById("txntodate");
	if( fromdate ){
		if( fromdate.value == "" ){
			errMessage(fromdate,"Enter from date");
			return false;
		}	
	}
	if( todate ){
		if( todate.value == "" ){
			errMessage(todate,"Enter to date");
			return false;
		}
	}
	valid=true;
	var orderref = null; 
	var oneselected = false;
	var orderreflen = document.getElementsByName("txnglkeys");
	for(var i=0;i<orderreflen.length;i++){
		var glkeysid  = 	"txnglkeys"+i;
		var orderref = document.getElementById(glkeysid);
		if(orderref.checked==true){
			oneselected = true;	 
			break;
		}
	}if(!oneselected){
		errMessage(orderref,"Select atleast one transactions");
		valid= false;
		return false;
	}
	return true;
}
</script>
<title>VAS/Transaction Details Report</title>
<sx:head/>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<sx:tabbedpanel id="test" cssClass="d" cssStyle="width:95%; margin:0 auto;" doLayout="false">
<%-- <sx:div id="one" label="VAS Report">
	<s:form action="getVastxnreportReportgenerationAction.do" method="post" name="vasreport" autocomplete="off"> 
		<table border='0' cellpadding='0' cellspacing='0' width="60%" class="formtable">
			<tr><td colspan="2" style="text-align:center;font-weight:bold;color:maroon"> <div> VAS Report </div></td></tr>
	  		<tr><td> From Date </td>  	<td class="textcolor" style="text-align:left"> : <input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">&nbsp;<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.vasreport.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"></td></tr>
			<tr><td> To Date   </td>	<td class="textcolor" style="text-align:left"> : <input type="text" name="todate" 	id="todate"    readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">&nbsp;<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.vasreport.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"></td></tr>
			<tr>	
		  		<td> Select Transactions </td>	
	 	 		<td class="textcolor" style="text-align:left"> :				  	
					<div class="cusmselect"><a href="#" onclick="sildedown(this)"><p class="hida">Select</p></a>
						<div class="mutliSelect">
							<ul>	 		
								<% int x=0; %>
								<s:iterator value="vastxnList">
									<% x++; %> 
									<li><input type='checkbox' name="glkeys" value="${TXNKEY}" id="glkeys<%=x%>"/><label>${TXNKEYDESC}</label></li> 
								</s:iterator>			 
					  		</ul>
						</div>	
			   		</div>
				</td>
			</tr>	
			 <tr>
				<td colspan="2" align="center">
					<table>
						<tr>
							<td ><s:submit name="submit" id="submit" value="PDF" onclick="return vasvalidationselect();"/></td>
							<td style="padding-left: 5px"><s:submit name="submit" id="submit" value="EXCEL" onclick="return vasvalidationselect();"/></td>
						</tr>
					</table>
				</td>	
			</tr>
		</table>
	</s:form>
</sx:div> --%>
<sx:div id="two" label=" Transaction Details Report ">
	<s:form action="getTxndetailsreportReportgenerationAction.do" method="post" name="txndetails" autocomplete="off"> 
			<table border='0' cellpadding='0' cellspacing='0' width="60%" class="formtable">
	 			<tr><td colspan="2" style="text-align:center;font-weight:bold;color:maroon"> <div> Transaction Details Report </div> </td></tr>
				<tr><td> From Date </td><td class="textcolor" style="text-align:left"> : <input type="text" name="txnfromdate" id="txnfromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">&nbsp;<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.txndetails.txnfromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"></td></tr>
				<tr><td> To Date </td><td class="textcolor" style="text-align:left"> : <input type="text" name="txntodate" 	id="txntodate"    readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">&nbsp;<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.txndetails.txntodate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17"></td></tr>
				<tr><td> Select Bin </td><td class="textcolor" style="text-align:left"> :<s:select name="bin" id="bin" listKey="BIN" listValue="BIN_DESC" list="binList"  headerValue="ALL" headerKey="ALL" value="%{BIN_DESC}" tooltip="Select BIN" style="width: 178px;"/></td></tr>	
				 <tr>	
				 	  <td> Select Transactions </td>	
				  	  <td class="textcolor" style="text-align:left"> :				  	
					  <div class="cusmselect">
					  	<a href="#" onclick="sildedown(this)"><p class="hida">Select</p></a>
					    <div class="mutliSelect">
						  <ul>	 		 
					 			  <% int y=0; %>
								  <s:iterator value="vastxnList">
							 			<% y++; %> 
							 			 <li><input type='checkbox' name="txnglkeys" value="${TXNKEY}" id="txnglkeys<%=y%>"/><label>${TXNKEYDESC}</label></li> 
							 		</s:iterator>		
							 				 
						  </ul>
						</div>	
					  </div>
					</td>
				</tr>	
				 <tr>
					<td colspan="2" align="center">
						<table>
							<tr>
								<td><s:submit name="submit" id="submit" value="PDF" onclick="return txnvalidationselect();"/></td>
								<td style="padding-left: 5px"><s:submit name="submit" id="submit" value="EXCEL" onclick="return txnvalidationselect();"/></td>
							</tr>
						</table>
					</td>	
				</tr>
			</table>
	</s:form>
</sx:div> 
</sx:tabbedpanel>

 