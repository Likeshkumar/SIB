<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s"%> 
<%@taglib prefix="sx" uri="/struts-dojo-tags" %>
<head>

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script type="text/javascript">
	function callAjaxfunctions()
	{
		alert("Calling");
		
		alert("The Value is ==> "+val);
		
		var url="getAjaxvaluefromactionPersonalCardOrderAction.do?values="+val;
		var response=AjaxReturnValue(url);
		document.getElementById('ajax').innerHTML = response;
		document.getElementById('subname').innerHTML = "Select Product";
		return false;
	}
</script>
<sx:head/>
</head>

<h1>Auto Complete</h1>
My List : <sx:autocompleter name="search_value" id="search_value" list="{'0'}" resultsLimit="10" showDownArrow="false" onkeyup="callAjaxfunctions();"/>

 <!-- 
<input type="text" name="search_value" id="search_value" onkeyup="callAjaxfunctions(this.value);">
 -->