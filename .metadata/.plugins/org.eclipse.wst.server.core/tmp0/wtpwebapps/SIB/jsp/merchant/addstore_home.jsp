<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<html>
<head>
<sx:head />
</head>

<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript"
	src="jsp/merchant/script/merchscript.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>


<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>



<%
Boolean editable = true;
String merchname ;

if( request.getParameter("merchname") != null ){
	 merchname = request.getParameter("merchname").replace("~", ""); 
}else{
	merchname = " "; 
	%>

<%
}
 
%>
<% 
String acttype = (String)session.getAttribute("act");
System.out.println(acttype);
String btnval = null;
if(acttype.equals("SAVE"))
{
	btnval = "Save";
}
else
{
	btnval = "Update";
} 
String storeid = (String)session.getAttribute("storeid");
System.out.println(storeid);
%>
<body onload="test()">
	<s:form action="saveStoreMerchantRegister.do" name="custinfoform"
		id="custinfoform" autocomplete="off" namespace="/">

		<table border="0" cellpadding="0" cellspacing="0" width="95%"
			style='border: 1px solid #efefef' align="center" class="filtertab">
			<s:hidden name="storeid" id="storeid" value="%{#session.storeid}"/>
			<tr>
				<td>MERCHANT NAME </td>
				<td>: <s:property value='mregbean.merchname' />
				</td>
				<td>MERCHANT ID</td>
				<td>: <s:property value='mregbean.merchid' /> <input type='hidden'
					name='merhid' id='merchid'
					value='<s:property value='mregbean.merchid'/>' />
				</td>
			</tr>


			<tr>
				<td>Store Name <span class="mand">*</span></td>
				<td>: <s:textfield name="storename" id="storename" maxlength="32" value="%{mregbean.storename}"/></td>

				
			</tr>


			<tr>
				<td>Phone1 <span class="mand">*</span></td>
				<td>: <s:textfield name="phoneone" id="phoneone" maxlength="16" value="%{mregbean.prime_phnum}"/></td>

				<td>Phone2 </td>
				<td>: <s:textfield name="phonetwo" id="phonetwo" maxlength="16"   value="%{mregbean.secp_hnum}"/></td>
			</tr>
			
			<tr>
				<td>Address1 </td>
				<td> &nbsp; <s:textarea name="address" id="address" rows="2" maxlength="64" value="%{mregbean.store_adrs}"/></td>

				<td>City </td>
				<td>: <s:textfield name="city" id="city" maxlength="32" value="%{mregbean.city_name}"/></td>
			</tr>
			
			
			
			<tr>
				 <td>City Code </td>
				<td>: <s:textfield name="citycode" id="citycode" maxlength="16" value="%{mregbean.city_code}"/></td>

				<td>State</td>
				<td>: <s:textfield name="stae" id="state" maxlength="32" value="%{mregbean.state_name}"/></td>
			</tr>
			
			
			<tr>
				 <td>Country </td>
				<!-- <td>: <s:textfield name="country" id="country"  maxlength="32" value="%{country_name}"/></td> -->
				<td>: <s:select list="%{mregbean.merchantnation}" listKey="NATION_ID"  listValue="NATION"  name="country" id="country"  headerValue="SELECT"  headerKey="-1"
				maxlength="32" value="%{mregbean.country_name}"/></td>
				
				<td> &nbsp; </td>
				<td> &nbsp; </td>
			</tr>
			
			<tr>
				<td>Opening Time </td>
				<td>: 
				 
				 <s:select
						list="%{mregbean.hourlist}" 
						id="openhour" name="openhour" value="%{mregbean.open_time1}"  headerKey="-1" headerValue="-HOUR-" cssStyle="width:80px"  />
						
				<s:select
						list="%{mregbean.minutelist}" 
						id="openminute" name="openminute" value="%{mregbean.open_time2}"  headerKey="-1" headerValue="-MINUTE-" cssStyle="width:80px"  />
				</td>

				<td>Closing Time </td>
				<td>: 
				 <s:select
						list="%{mregbean.hourlist}" 
						id="closehour" name="closehour" value="%{mregbean.close_time1}"   headerKey="-1" headerValue="SELECT" cssStyle="width:80px"  />
				<s:select
						list="%{mregbean.minutelist}" 
						id="closeminute" name="closeminute" value="%{mregbean.close_time2}"  headerKey="-1" headerValue="-MINUTE-" cssStyle="width:80px"  />
				
				</td> 
			</tr>
			
			
			
			
			<tr>
				<td>Status</td>
				<td> : <s:select
						list="#{'1':'ACTIVE','0':'IN-ACTIVE' }"
						name='storestatus' id='storestatus' headerKey="-1" headerValue="-SELECT-" value="%{mregbean.store_status}"/></td>
			</tr>

		</table>
		<table border="0" cellpadding="0" cellspacing="4" width="20%">
			<tr>
				<td><input type="submit" value="<%=btnval%>" name="action" id="action" onclick="return addstore_validation()"/></td>
				<td><input type="button" name="cancel" id="cancel"
					value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

				</td>
			</tr>
		</table>





	</s:form>

</body>