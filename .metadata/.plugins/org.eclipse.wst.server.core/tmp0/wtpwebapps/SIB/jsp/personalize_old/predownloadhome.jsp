<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
 <script>
 	function getPREFiles( instid,selprodid ){
 		var branch = document.getElementById("branchcode");
 		if(branch.value == "-1")
 		{
 			errMessage( branch, "Please Select Branch " );
 			return false;	
 		}
 		if( selprodid != "-1" )
 		{
 			alert("Branch==>"+branch.value+" Product id===> "+selprodid);
  			prodid = selprodid; 
	 		var url = "getPREFilesPreprocessAction.do?instid="+instid+"&branchcode="+branch.value+"&prodid="+prodid;   
	 		result = AjaxReturnValue(url);  
	 		document.getElementById("prefilename").innerHTML = result;  
 		}
 	}
 	
 	function Validation()
 	{
 		var branchcode = document.getElementById("branchcode");
 		var cardtype = document.getElementById("cardtype");
 		if( branchcode )
 		{
			if(branchcode.value == "-1")
			{
				errMessage(branchcode," Select Branch ");
				return false;
			}
 		}
 		if( cardtype )
 		{
 			if(cardtype.value == "-1")
 			{
 				errMessage(cardtype," Select Card ");
				return false;	
 			}
 		}

 	}
 </script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
%>
<s:form action="prefileDownloadPreprocessAction"  name="perscardgen" autocomplete = "off" namespace="/">

<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">
	<% String usertype=(String)session.getAttribute("USERTYPE");
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
		Select Card
		</td>
		<td> :
 				<select name="cardtype" id="cardtype" onchange="getPREFiles('<%=instid%>',this.value)">
	 				<option value="-1">--Select Card--</option>
	 				<s:iterator  value="personalproductlist">
	 					<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
	 				</s:iterator>
 				</select>
		</td>
    </tr>
    <tr>	
		<td> 
		File Name
		</td>
		<td> :  <select name="prefilename" id="prefilename" > 
 				</select>
		</td>
	</tr>      
</table>
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Download" name="submit" id="order" onclick="return Validation();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		
		<td>
			<input type="submit" name="submit" id="delete" value="Delete" onclick="return delConfirm()"  />
			 
		</td>
		
		
		</tr>
</table>

</s:form>
