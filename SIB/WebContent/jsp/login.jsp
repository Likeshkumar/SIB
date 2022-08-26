<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
.Logintab {
	height: 150px;
	width: 350px;
}

.MessageStyle {
	border: 0px solid #434343;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-weight: bold;
	font-size: 10px;
	text-align: center;
	overflow: auto;
	
}

.logtab {
	padding-top: 5px;
	font-family: Georgia;
	font-size: 13px'
}

.prabu {
	color: #ca1a1a;
	font-size: 17px;
	font-style: italic;
	font-weight: bold;
}

a {
	text-decoration: none;
	color: blue;
	font-size: 10px;
}

.aa {
	color: #CA1A1A;
	font-size: 10px;
}

.loginbut {
	color: #FFFFFF;
	background: red;
}

.version {
	color: darkblue;
	font-family: -moz-fixed;
	font-size: 12px;
	height: 14px;
	font-weight: bold;
	text-align: center;
}
</style>

<script src="js/jquery.min.js"></script>
<script src="js/jquery.capslockstate.js"></script>


<script>
	$(document)
			.ready(
					function() {
						$(window).capslockstate();

						$(window)
								.bind(
										"capsOn",
										function(event) {
											if ($("#pass:focus").length > 0) {
												document
														.getElementById('divMayus').style.visibility = 'visible';
											}
										});
						$(window)
								.bind(
										"capsOff capsUnknown",
										function(event) {
											document.getElementById('divMayus').style.visibility = 'hidden';
										});
						$("#pass")
								.bind(
										"focusout",
										function(event) {
											document.getElementById('divMayus').style.visibility = 'hidden';
										});
						$("#pass")
								.bind(
										"focusin",
										function(event) {
											if ($(window)
													.capslockstate("state") === true) {
												document
														.getElementById('divMayus').style.visibility = 'visible';
											}
										});
					});
</script>
<script type="text/javascript">
	function display(reporttype) {

		if (reporttype == 'admin') {

			document.getElementById('instname').disabled = true;

		}
		if (reporttype == 'inst') {

			document.getElementById('instname').disabled = false;

		}

	}

	function validate_form() {

		var instname = document.getElementById("instname");
		var username = document.getElementById("username");
		var instname = document.getElementById("instname");

		if (instname.value == "SUPERADMIN" || instname.value == "superadmin") {
			logintype.value = "admin";
		} else {
			logintype.value = "inst";
		}

		valid = true;
		var userType = document.getElementById("logintypeA");

		if (username.value == "") {
			alert("Enter User Name");
			return false;

		}
		if (document.login.pass.value == "") {
			alert("Enter Password ");
			valid = false;

		}

		if (InstName.value == "") {
			alert(" Enter Instituion");
			return false;
		}

		return true;
	}

	var httpObject = null;

	//Get the HTTP Object
	function getHTTPObjectForBrowser() {
		if (window.ActiveXObject)
			return new ActiveXObject("Microsoft.XMLHTTP");
		else if (window.XMLHttpRequest)
			return new XMLHttpRequest();
		else {
			alert("Browser does not support AJAX...........");
			return null;
		}
	}
	//Change the value of the outputText field
	function setAjaxOutput() {
		if (httpObject.readyState == 4) {

			alert(httpObject.responseText);

			if (httpObject.responseText != "") {
				document.getElementById('sumbitbutton').innerHTML = httpObject.responseText;
				document.getElementById('ajax').innerHTML = "";
			} else {
				document.getElementById('ajax').innerHTML = "";
				document.getElementById('sumbitbutton').innerHTML = "";

			}

		}
	}
	//Implement business logic
	function doAjaxCall() {

		var inst_id = document.getElementById('instname').value;
		httpObject = getHTTPObjectForBrowser();
		if (httpObject != null) {
			httpObject.open("GET",
					"ajax_InstLicence_HandlerAddInstitutionAction.do?inst_id="
							+ inst_id, true);
			httpObject.send(null);

			httpObject.onreadystatechange = setAjaxOutput;

		}
	}
</script>



<c:set var="homeimage" value="images/SIB-login.jpg" />

<c:choose>

	<c:when test="${empty  APPLICATIONTYPE}">
		<c:out value="COULD NOT CONNECT DATABASE"></c:out>
	</c:when>
	<c:when test="${APPLICATIONTYPE=='PREPAID'}">
		<c:set var="homeimage" value="images/homepage_prepaid.jpg" />
	</c:when>

	<c:when test="${APPLICATIONTYPE=='MERCHANT'}">
		<c:set var="homeimage" value="images/homepage_merchant.jpg" />
	</c:when>
	<c:when test="${APPLICATIONTYPE=='CREDIT'}">
		<c:set var="homeimage" value="images/homepage_credit.jpg" />
	</c:when>

	<c:when test="${APPLICATIONTYPE=='FMS'}">
		<c:set var="homeimage" value="images/homepage_fms.jpg" />
	</c:when>
	<c:when test="${APPLICATIONTYPE=='BOTH'}">
		<c:set var="homeimage" value="images/homepage_dcms.jpg.jpg" />
	</c:when>

	<c:otherwise>
		<c:set var="homeimage" value="images/SIB-login.jpg" />
	</c:otherwise>
</c:choose>

<%-- <% 
String apptype = (String)session.getAttribute("APPLICATIONTYPE");
if( apptype == null ){
	out.println( "COULD NOT CONNECT DATABASE ");
} 
 
String homeimage = "";
if(apptype.equals("PREPAID")){
	 homeimage = "images/homepage_prepaid.jpg";
}
else if( apptype.equals("MERCHANT")){
	 homeimage = "images/homepage_merchant.jpg";
}else if( apptype.equals("CREDIT")){
	 homeimage = "images/homepage_credit.jpg";
}else if( apptype.equals("FMS")){
	 homeimage = "images/homepage_fms.jpg";
}else if( apptype.equals("BOTH")){
	 homeimage = "images/homepage_dcms.jpg.jpg";
}else{
	 homeimage = "images/homepage_bic.jpg";
}



//out.println( "homeimage "+homeimage);
%> --%>
 <% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>


<c:if test="${not empty msg}">
	<span style='color: red; text-align: center; display: block'>
	<c:out value="${msg}"></c:out></span>
</c:if>
<style>
.maintab {
	background: url("<c:out value="${homeimage}"></c:out>") no-repeat scroll 0 0/1330px 600px
		rgba(0, 0, 0, 0);
	height: 600px;
	width: 1330px;
}
</style>

<%-- 
<% 
if( request.getParameter("msg") != null ){
	%>
	<span style='color:red;text-align:center;display:block'>  <%= request.getParameter("msg") %>  </span> 
<% 
}
%>
<style>
.maintab {
    background: url("<%=homeimage%>") no-repeat scroll 0 0 / 1000px 621px rgba(0, 0, 0, 0);
    height: 622px;
    width: 1000px;
}

</style> --%>

<SCRIPT type="text/javascript">
	window.history.forward();
	function noBack() {
		window.history.forward();
	}
</SCRIPT>

<BODY onload="noBack();" onpageshow="if (event.persisted) noBack();"
	onunload="">

	<jsp:include page="/displayresult.jsp"></jsp:include>
	<div class="MessageStyle" align="center">
		<!-- 	<font color="red" size="2"><b>	User Is Already Loged In/Not Logged Out Properly. Try Ulock User Link </b></font> 
			<font color="red" size="2"><b> 2Time User Name/Password mismatch </b></font>  -->


		<c:if test="${ empty  message}">
			<c:remove var="message" />
		</c:if>

		<c:if test="${not  empty  message }">
			<font color="red" size="2"><b> <c:out value="${message}"></c:out></b></font>
		</c:if>

<%-- 
		<%
			String message = null;
				message = (String) session.getAttribute("message");
				session.removeAttribute("message");
		%>
		<%
			if (message != null) {
		%>
		<font color="red" size="2"><b><%=message%></b></font>
		<%
			}
		%> --%>
	</div>



	<div align="center">
		<table align="center" border="0" class='maintab'>
			<tr>
				<td align="center" class="centertd">

					<div class="Logintab">
						<s:form action="userTestLoginAction" name="login" namespace="/" method="post" autocomplete="off">
							<table border="0" cellpadding="4" cellspacing="7" align="center"class='logtab'>
								<!--  <tr>
		<td colspan="2" align="center">
		<span class='orient'>Orient Bank</span> 
		<hr style="height: 1px; color: blue; width: 75% " />
		</td>
	</tr> -->


								<!-- <tr>
		<td><b>Login Type</b> </td>
		<td>
			<input type="radio" name="logintype" id="logintypeA" value="admin"  onclick='display(this.value)'>Admin
			<input type="radio" name="logintype" id="logintypeU" value="inst"  onclick='display(this.value)' checked='checked'>Institution
		</td>
    </tr> -->


								<input type="hidden" name="logintype" id="logintype" value="">
								<input type="hidden" name="token1" id="csrfToken"value="${csrfToken}">
								<!--<input type="hidden" name="token" id="csrfToken"value="123">-->


								<br>

								<span class='version'  style="color: White">&nbsp; &nbsp;
									&nbsp;&nbsp; 1.A15.42.33.1.0.</span>
								</br>

								<tr>

									<td><b>User Name</b></td>
									<td><s:textfield name="username" id="username"
											maxlength="15" /></td>
								</tr>
								<tr>
									<td><b>Password</b></td>
									<td><s:password name="pass" id="pass" maxlength="30" /></td>
								</tr>
								<tr>
									<td><b>Institution</b></td>
									<td><s:action name="gettingInstitutionLoginAction"
											var="resd" /> <select name="instname" id="instname">
											<option value="-1">--Select Institution--</option>
											<option value="superadmin">superadmin</option>
											<!-- <option value="superadmin">superadmin2</option> -->
											<s:iterator value="#resd.Instlist">
												<option value="${INST_ID}">${INST_NAME}</option>
											</s:iterator>
									</select> <%-- <s:textfield name="instname" id="instname" maxlength="10"/> --%>
									</td>
								</tr>

								<tr>
									<td align='left' colspan='2'  style="white"><a   
										href="forgetPasswordhomeForgetpasswordAction.do">   <b  style="color: Yellow">  Forgot 									
											Password </a> <span id="divMayus" style="visibility: hidden" style="color:white">Caps
											Lock Detected</span> <s:submit name="login" id="login"
											onclick="return validate_form();" value="Login"
											style="color:#FFF;background:blue;cursor:pointer" /> <%-- 	<s:reset name="cancel"  value="Cancel" class="loginbut" style="color:#FFF;background:#CD181D;cursor:pointer" /> --%>
									</td>
								</tr>


							</table>

						</s:form>

					</div>
				</td>
			</tr>

			<tr>
				<td class="version" >Debit Card Management System </td>
			</tr>
		</table>
	</div>