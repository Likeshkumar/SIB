<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
  
</head>

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="savemccMerchantCategoryCode.java.do" autocomplete="off">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
				 
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		 
				<%
				int i=0,k=0,no_cols = 4;
				%>
				<s:iterator value="mccbeans.mccinst">
				  <s:set name="mccdes">${MCC_CODE}</s:set>
					<%
					if(i==0)
					{
						
					%>
					<tr>	
					<% 
					}
					%>
					<td width="10%">
					 
						${MCCDESC}
											
					</td>

					<%
					if(i==no_cols)
					{
						i=0;	
					%>
					</tr>
					<%	
					}
					else
					{
						i=i+1;
					}
					
					%>
				</s:iterator>
	</table>
 
</form>
