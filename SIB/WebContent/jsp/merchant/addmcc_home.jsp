<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript">
	function validateform()
	{
		//alert("welcome");
		var flag = true;
		var chckbox_len = document.getElementsByName("mccid").length;
		for(var i=0;i<chckbox_len;i++)
		{
			//alert(i);
			var chcked = document.getElementById("mccid"+i).checked;
			//alert("chcked--> "+chcked);
			if(chcked)
			{
				flag = false;
				break;
			}
			if(!(chcked))
			{
				flag = true;
			}
		}
		if(flag)
		{
			//alert("SELECT ATLEAST ONE CHKBOX --> "+chcked);
			errMessage(errmsg,"SELECT ATLEAST ONE MCC");
			return false;	
		}
		parent.showprocessing();
		return true;
	}
</script>
		<% 
		String usertype=(String)session.getAttribute("USERTYPE");
		%>
</head>

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="savemccMerchantCategoryCode.java.do" autocomplete="off">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td> INSTITUTION ID: <%out.println( session.getAttribute("Instname") ); %></td>
						<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
				</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<%
				int i=0,k=0,no_cols = 4;
				%>
				<s:iterator value="mccbeans.mccdescp">
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
						<input type="checkbox" name="mccid" 
							<s:iterator value="mccbeans.mccinst">
				   				<s:set name="mccinstitution">${MCC}</s:set>
				   				 
								<s:if test="%{#mccdes==#mccinstitution}"> checked='checked' </s:if> 
							</s:iterator> 
						
						id="mccid<%=k++%>" value="${MCC_CODE}" />
						${MCC_DESC}
											
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
	<table><tr><td></td><td><s:submit value="Submit" onclick="return validateform();"></s:submit></td></tr>			</table>
</form>
