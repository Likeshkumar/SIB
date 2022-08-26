<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<head>
<script type="text/javascript" src="js/script.js"></script> 
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<jsp:include page="/jsp/include_header/include.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>

<div id="fw_container">	
<s:form name="diputeraiseform" action="authorizeOrderInstCardorderAction" onsubmit="parent.showprocessing()" autocomplete="off" namespace="/" method="post" >
	<table id="example" border="0" cellpadding="0" width="95%" cellspacing="0" class="pretty">
		
		<thead>
			<tr>
						<th> Sl no </th>
						<th>  <input type='checkbox' onclick="checkedAll(this.form)"  id="checkall"/> </th>
						<th> Order Ref No </th>
						<!-- <th>No.of Cards</th> -->
						<th> Emb Name </th>
						<th> Card type </th>
						<th> Product Bin </th>
						<th> Ordered Date </th>
						<th> Ordered by </th>
					</tr>	
		</thead>
		<% int rowcnt = 0; Boolean alt=true; %> 
				<s:iterator value="instorderlist">
				<%-- <%System.out.println("test--->"); %> --%>
					<tr
					<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
					>
					
				<%-- 	<%System.out.println("test111--->"+rowcount); %> --%>
						<td> <%= rowcount %> </td>
						<td> <input type="checkbox" name="instorderrefnum" value="${ORDER_REF_NO}"  id='orderrefnum<%=rowcount%>'/>  </td>
						<td> ${ORDER_REF_NO}  </td>
						<%-- <td> ${CARD_QUANTITY}  </td> --%>
						<td> ${EMBOSSING_NAME} </td>
						<td> ${CARDTYPEDESC}   </td>
						<td> ${ PRODBINDESC }  </td>
						<td> ${ ORDERED_DATE }</td> 
					    <td> ${ USERNAME }</td> 
					</tr> 
			 	</s:iterator>
			<%--  	<tr>
						<td colspan="8">
							<s:submit value="Authorize" name="authorize" id="authorize" onclick="return showCount()"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Reject" name="deauthorize" id="deauthorize" />
				 
						</td>
					</tr> --%>
	</table>
	<%-- ---><s:property value="totalcount"/>
	---><s:property value="filtercondition"/> --%>
	<s:hidden name="totalcount"/>
	<s:hidden name="filtercondition"/>
	<table>
		<tr><td>
			<input type="hidden" name="token" id="csrfToken"value="${token}">
		<s:submit value="Authorize" name="authorize" id="authorize" onclick="return showCount()"/>
						 
							<input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/>
							
							<s:submit value="Reject" name="deauthorize" id="deauthorize" /></td></tr>
	</table> 
	
</s:form>
</div>
</body>
</html>