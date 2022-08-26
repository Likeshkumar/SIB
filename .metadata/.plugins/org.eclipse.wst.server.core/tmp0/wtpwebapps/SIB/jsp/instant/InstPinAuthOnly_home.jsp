<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/calendar.js"></script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>
 
<s:form action="viewPinAuthListInstantOnlyPinGeneration"  name="orderform" onsubmit="return validateFilter()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="70%" align="center"  >
	 
	   <tr bgcolor="#3197e0" align="center">
                    <td > Select Branch</td>
                    <td > Product</td>
                    <td >From Date</td>
                    <td > To Date</td>
                   
                    </tr>
	 <tr>
	 
	<%if(usertype.equals("INSTADMIN"))
		{
	%>
				<%-- <td> <s:select name="branchcode" id="branch" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1" tooltip="Select Branch"/> --%>
				
				<td>
					<select name="branchcode" id="branch">
					<option value="ALL">ALL</option>
					<s:iterator  value="branchlist">
					<option value="${BRANCH_CODE}">${BRANCH_CODE} - ${BRANCH_NAME}</option>
					</s:iterator>
					</select>
				</td>
						
	<%	
		}
				else
				{
			%>
					
					
					<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
						<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
					
			<%		
				}
			%>
    
	 
		<td> :
				
 				<select name="cardtype" id="cardtype" >
 				<option value="ALL">ALL</option>
 				<s:iterator  value="Instpersonalproductlist">
 				<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				</select>
		</td>
    
    	<td> : 
    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation();" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
        </td>
    
    	
     
		<td> : <input type="hidden" name="Ordertype" value="I">
			<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation();" style="width:160px">
				<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.orderform.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> 
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Submit" name="order" id="order" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
 
 