<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>

function validation(){		
	var cardtype = document.getElementById("cardtype");
	if(cardtype.value=='-1' || cardtype.value==''){
		 errMessage(cardtype,"Select Product !");return false;
	}
}  
</script> 
</head>
 
<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="bulkrenualListBulkRenual"  name="perscardgen" autocomplete = "off" namespace="/">

<s:if test="%{!branchlist.isEmpty()}">       
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">
	<% 
	String usertype=(String)session.getAttribute("USERTYPE");   
		if(usertype.equals("INSTADMIN"))
		{
	%>
			<tr>
				<td class="fnt"> Select Branch</td>
				<td> : <s:select name="branchcode" id="branchcode" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1"/></td>
			</tr>
	<%	
		}
		else
		{
	%>
			<tr>
			<td class="fnt"> Branch</td>
			<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
				<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
			</tr>
	<%		
		}
	%>
	<tr>
		<td class="fnt">
		Select Product
		</td>
		<td> :
 				<select name="cardtype" id="cardtype">
	 				<option value="-1">--Select Product--</option>
	 				<s:iterator  value="personalproductlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
    
    <tr>
		<td class="fnt">
		Generation Type
		</td>
		<td> :
 				<select name="cardgentype" id="cardgentype">
	 				<option value="-1">--Select Generation Type--</option>
	 				<option value="NEWCARD">New Card</option>
	 				<!-- <option value="EXISTCARD">Same Card</option> -->
	 				
 				</select>
		</td>
    </tr>
    
    <% 
    String datefilter_req =(String)session.getAttribute("DATEFILTER_REQ")==null?"":(String)session.getAttribute("DATEFILTER_REQ");
    if( datefilter_req.equals("1"))
    {
    %> 
	<!-- <tr>
	   <td align="left" class="fnt">From Date<font class="mand">*</font></td>
		<td> :
		<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.perscardgen.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr>
	<tr>
	    <td align="left" class="fnt">To Date <font class="mand">*</font></td>
		<td> :
		<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id),fromdatetodatevalidation();" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.perscardgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> -->
		<% }  %>  
</table>
<table border="0" cellpadding="0" cellspacing="0" width="20%" >
	<tr>
		<td>
			<s:submit value="Next" name="Next" onclick="return validation();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:if>
<s:else>

<table border='0' cellpadding='0' cellspacing='0' width='80%'
	style='text-align: center;'>
	<tr>
		<td style='text-align: center;'>   
<font color="red"><b>	No Cards Waiting For Renewal .....</b></font>	
</td>
</tr>
</table> 

</s:else>
<s:hidden name="flag" value=""/>
</s:form>
