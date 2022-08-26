<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>

<script>
	function showProcessing(){
		parent.showprocessing();
	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	String datefilter_req = (String)session.getAttribute("DATEFILTER_REQ"); 
%>
 
<s:form action="creditIssuanceListCreditIssuance.do"  name="orderform" onsubmit="return showProcessing()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab formtable">	 
	
    		<% 	if(usertype.equals("INSTADMIN"))
				{
			%>
		<tr>
				<td> Select Branch</td>
				<td> :&nbsp;<s:select name="branchcode" id="branch" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="crissuebean.branchlist"  headerValue="Select Branch" headerKey="-1" value="%{BRANCH_CODE}" tooltip="Select Branch"/>
				
				</td>
			</tr>
			
			<%	
				}
				else
				{
			%>
			<tr>
			<td class="fnt"> Branch:</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
			<%		
				}
			%>
    
    
    
	<tr>
		<td>
		  Product 
		</td>
		<td> :
				
 				<select name="productcode" id="cardtype" >
 				<option value="-1-">--Select -- </option>
 				<s:iterator  value="crissuebean.productlist">
 				<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				</select>
		</td>
    </tr>
    <tr>
    </tr>
    <%  if( datefilter_req.equals("1")) { %>   
     <tr> 
    	<td>
    		 From Date
    	</td>
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
    </tr>	
    	
    <tr>	
		<td> 
		To Date
		</td>
		<td> : <input type="hidden" name="Ordertype" value="I">
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> 
	<% }  %>  	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" onclick="return validFilter()"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 